package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OrderStatusScheduler {
    private static final long CHECK_INTERVAL_MINUTES = 5;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public void start() {
        scheduler.scheduleAtFixedRate(this::checkAndUpdateOrders, 0, CHECK_INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    private void checkAndUpdateOrders() {
        Session session = null;
        Transaction tx = null;
        try {
            session = App.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Date now = new Date();
            // Get all orders that are not yet delivered or picked up and have a delivery/pickup time in the past
            String hql = "FROM Order o WHERE (o.status = 'CONFIRMED' OR o.status = 'PENDING') AND o.deliveryTime IS NOT NULL AND o.deliveryTime <= :now";
            List<Order> orders = session.createQuery(hql, Order.class)
                    .setParameter("now", now)
                    .getResultList();
            for (Order order : orders) {
                if (order.isRequiresDelivery()) {
                    order.setStatus("DELIVERED");
                    session.update(order);
                    EmailService.sendOrderStatusUpdateEmail(order, "DELIVERED");
                } else {
                    order.setStatus("PICKED_UP");
                    session.update(order);
                    EmailService.sendOrderStatusUpdateEmail(order, "PICKED_UP");
                }
            }
            // Check for expired yearly subscriptions
            String userHql = "FROM il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck u WHERE u.is_yearly_subscription = true AND u.subscriptionStartDate IS NOT NULL";
            List<il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck> users = session.createQuery(userHql, il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck.class).getResultList();
            for (il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck user : users) {
                if (user.isYearlySubscriptionExpired()) {
                    user.set_yearly_subscription(false);
                    session.update(user);
                    EmailService.sendSubscriptionExpiredEmail(user);
                }
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }
} 