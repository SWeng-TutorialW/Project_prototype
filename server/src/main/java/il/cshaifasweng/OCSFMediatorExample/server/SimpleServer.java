package il.cshaifasweng.OCSFMediatorExample.server;


import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import org.hibernate.Session;



public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();

	public SimpleServer(int port) {
		super(port);

	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		System.out.println("Received message from client: " + msg);
		System.out.println("msg class: " + msg.getClass().getName());
		System.out.println("is Flower? " + (msg instanceof Flower));
		System.out.println("asaf");
		System.out.println("equals Flower.class? " + (msg.getClass().equals(Flower.class)));
		if (msgString.startsWith("#warning")) {
			Warning warning = new Warning("Warning from server!");
			try {
				client.sendToClient(warning);
				System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(msgString.startsWith("add client")){
			SubscribedClient connection = new SubscribedClient(client);
			SubscribersList.add(connection);
			try {
				client.sendToClient("client added successfully");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		else if(msgString.startsWith("remove client")){
			if(!SubscribersList.isEmpty()){
				for(SubscribedClient subscribedClient: SubscribersList){
					if(subscribedClient.getClient().equals(client)){
						SubscribersList.remove(subscribedClient);
						break;
					}
				}
			}
		}
		else if (msgString.startsWith("getCatalogTable_guest")) {
			try {
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Flower> query = builder.createQuery(Flower.class);
				query.from(Flower.class);


				List<Flower> flowerList = session.createQuery(query).getResultList();



				System.out.println("Flowers in DB: " + flowerList.size());
				for (Flower flower : flowerList) {
					System.out.println("Name: " + flower.getFlowerName() +
							", Price: " + flower.getFlowerPrice() +
							", Type: " + flower.getFlowerType());
				}

				session.getTransaction().commit();
				session.close();

				CatalogUpdateEvent event = new CatalogUpdateEvent(flowerList,null,null);
				client.sendToClient(event);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (msgString.startsWith("getCatalogTable_login")) {
			try {
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Flower> query = builder.createQuery(Flower.class);
				query.from(Flower.class);
				CriteriaQuery<LoginRegCheck> query1 = builder.createQuery(LoginRegCheck.class);
				query1.from(LoginRegCheck.class);


				List<Flower> flowerList = session.createQuery(query).getResultList();
				List<LoginRegCheck> loginRegCheckList = session.createQuery(query1).getResultList();
				List<Store> Stores = App.get_stores();


				System.out.println("Flowers in DB: " + flowerList.size());
				for (Flower flower : flowerList) {
					System.out.println("Name: " + flower.getFlowerName() +
							", Price: " + flower.getFlowerPrice() +
							", Type: " + flower.getFlowerType());
				}

				session.getTransaction().commit();
				session.close();

				CatalogUpdateEvent event = new CatalogUpdateEvent(flowerList,loginRegCheckList,Stores);
				client.sendToClient(event);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		else if(msgString.startsWith("number#flower#to#update#")){
			// we got a Flower from the client, it means we want to update this flower into our DB table.
			try {
				String[] parts = msgString.split("_");
				String flower_name = parts[1];
				double newPrice = Double.parseDouble(parts[2]);

				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Flower> query = builder.createQuery(Flower.class);
				Root<Flower> root = query.from(Flower.class);


				query.select(root).where(builder.equal(root.get("flowerName"), flower_name));


				Flower flowerToUpdate = session.createQuery(query).uniqueResult();
				flowerToUpdate.setFlowerPrice(newPrice);
				session.update(flowerToUpdate);

				session.getTransaction().commit();
				session.close();

				client.sendToClient("Flower updated successfully :)))))");
			} catch (Exception e) {
				e.printStackTrace();
				try {
					client.sendToClient("Error updating flower: " + e.getMessage());
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		else if(msgString.startsWith("update_catalog_after_change"))
		{
			sendToAllClients("update_catalog_after_change");

		}

		else if (msgString.startsWith("asks_for_users")) {
			Session session = null;
			List<LoginRegCheck> allUsers = null;

			try {
				session = App.getSessionFactory().openSession();
				session.beginTransaction();

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<LoginRegCheck> query = builder.createQuery(LoginRegCheck.class);
				Root<LoginRegCheck> root = query.from(LoginRegCheck.class);
				query.select(root);

				allUsers = session.createQuery(query).getResultList();

				session.getTransaction().commit();
			} catch (Exception e) {
				e.printStackTrace();
				if (session != null) session.getTransaction().rollback();
			} finally {
				if (session != null) session.close();
			}


			try {
				client.sendToClient(allUsers);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (msgString.startsWith("price_changed"))
		{
			List<Store> stores = App.get_stores();
			Store store = stores.get(0);
		}
		else if (msgString.startsWith("update_catalog_after_manager_add_flower"))
		{
			Session session = App.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM Flower";
			List<Flower> flowers = session.createQuery(hql, Flower.class).getResultList();
			session.getTransaction().commit();
			session.close();
			try {
				Add_flower_event event = new Add_flower_event(flowers,4);
				client.sendToClient(event);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else if (msgString.startsWith("I#need#to#update#my#store#catalog_"))
		{

			List<Store> stores = App.get_stores();
			Store store = stores.get(0);
			List<Flower> flowers=store.getFlowersList();

			try {
				update_local_catalog event = new update_local_catalog(flowers,1);
				sendToAllClients(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
			store=stores.get(1);
			flowers=store.getFlowersList();
			try {
				update_local_catalog event = new update_local_catalog(flowers,2);
				sendToAllClients(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
			store=stores.get(2);
			flowers=store.getFlowersList();
			try {
				update_local_catalog event = new update_local_catalog(flowers,3);
				sendToAllClients(event);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else if (msgString.startsWith("update_catalog_after_manager_delete_flower"))
		{
			Session session = App.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM Flower";
			List<Flower> flowers = session.createQuery(hql, Flower.class).getResultList();
			session.getTransaction().commit();
			session.close();
			try {
				Add_flower_event event = new Add_flower_event(flowers,-1);
				client.sendToClient(event);
			} catch (Exception e) {
				e.printStackTrace();
			}



		}
		else if (msgString.startsWith("new#price#in#flower_"))
		{
			Session session = App.getSessionFactory().openSession();
			session.beginTransaction();
			String[] parts = msgString.split("_");
			String flower_name=parts[1];
			if(flower_name.equals("all"))
			{
				String hql = "FROM Flower";
				List<Flower> flowers = session.createQuery(hql, Flower.class).getResultList();
				session.getTransaction().commit();
				session.close();
				try {
					discount_for_1_flower event = new discount_for_1_flower(flowers,2,flower_name);
					client.sendToClient(event);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}

			String hql = "FROM Flower";
			List<Flower> flowers = session.createQuery(hql, Flower.class).getResultList();
			session.getTransaction().commit();
			session.close();
			try {
				discount_for_1_flower event = new discount_for_1_flower(flowers,1,flower_name);
				client.sendToClient(event);
			} catch (Exception e) {
				e.printStackTrace();
			}



		}
		else if(msg.getClass().equals(Flower.class)) {
			Flower flower = (Flower) msg;

			Session session = App.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(flower);
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				e.printStackTrace();
			} finally {
				session.close();
			}
			List<Store> stores = App.get_stores();
			for (Store store : stores) {
				store.getFlowersList().add(flower);
			}
			System.out.println("server send message to client");
			sendToAllClients("The network manager has added a flower.");

		}
		else if (msg.getClass().equals(Integer.class)) {
			int discountPercent = (Integer) msg;

			try {
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				System.out.println("Updating flowers in database with " + discountPercent + "% discount...");


				String hql = "FROM Flower";
				List<Flower> flowersFromDB = session.createQuery(hql, Flower.class).getResultList();

				for (Flower flower : flowersFromDB) {
					double originalPrice = flower.getFlowerPrice();
					double newPrice = originalPrice * (1 - discountPercent / 100.0);

					System.out.println("  [DB] Flower: " + flower.getFlowerName());
					System.out.println("    [DB] Original Price: " + originalPrice);
					System.out.println("    [DB] New Price: " + newPrice);

					flower.setFlowerPrice(newPrice);
					flower.setSale(true);
					flower.setDiscount(discountPercent);
					session.update(flower);
				}

				session.getTransaction().commit();
				session.close();


				for (Store store : App.get_stores()) {
					List<Flower> updatedList = new ArrayList<>();

					for (Flower flowerInStore : store.getFlowersList()) {

						for (Flower flowerFromDB : flowersFromDB) {
							if (flowerFromDB.getFlowerName().equals(flowerInStore.getFlowerName())) {
								updatedList.add(flowerFromDB);
								break;
							}
						}
					}

					store.setFlowersList(updatedList);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			sendToAllClients("new#price#in#flower_all");
		}


		if(msg.getClass().equals(Add_flower_wrapper.class))
		{
			Add_flower_wrapper wrapper = (Add_flower_wrapper) msg;
			Flower flower = wrapper.getFlower();
			int type = wrapper.gettype();

			Session session = App.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(flower);
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				e.printStackTrace();
			} finally {
				session.close();
			}
			List<Store> stores = App.get_stores();
			Store store = stores.get(type-1);
			store.getFlowersList().add(flower);
			System.out.println("Employee of " + store.getStoreName() + " added a flower.");
			sendToAllClients("The network manager has added a flower.");
			//the same logic as manager so the message says manager but is employee

		}
		if (msg.getClass().equals(FlowerDiscountWrapper.class))
		{
			FlowerDiscountWrapper wrapper = (FlowerDiscountWrapper) msg;
			Flower flower = wrapper.getFlower();
			int discountPercent = wrapper.getDiscount();
			if (discountPercent == -1)
			{

				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Flower> query = builder.createQuery(Flower.class);
				Root<Flower> root = query.from(Flower.class);
				query.select(root).where(builder.equal(root.get("flowerName"), flower.getFlowerName()));
				Flower flowerToDelete = session.createQuery(query).uniqueResult();
				if(flowerToDelete!=null)
				{
					for (Store store : App.get_stores())
					{
						store.getFlowersList().removeIf(f -> f.getFlowerName().equals(flowerToDelete.getFlowerName()));
					}

				}
				session.delete(flowerToDelete);
				session.getTransaction().commit();
				System.out.println("Flower deleted: " + flowerToDelete.getFlowerName());
				session.close();
				sendToAllClients("The network manager has deleted flower.");
				return;
			}
			if (discountPercent == -2)
			{
				double new_price=flower.getFlowerPrice();

				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Flower> query = builder.createQuery(Flower.class);
				Root<Flower> root = query.from(Flower.class);
				query.select(root).where(builder.equal(root.get("flowerName"), flower.getFlowerName()));
				Flower flowerToUpdate = session.createQuery(query).uniqueResult();
				flowerToUpdate.setFlowerPrice(new_price);
				session.update(flowerToUpdate);
				session.getTransaction().commit();
				System.out.println("Flower : " + flowerToUpdate.getFlowerName());
				System.out.println("NOW THE PRICE IS : " + flowerToUpdate.getFlowerPrice());
				session.close();
				for (Store store : App.get_stores()) {
					for (Flower f : store.getFlowersList()) {
						if (f.getFlowerName().equals(flowerToUpdate.getFlowerName())) {
							f.setFlowerPrice(new_price);

						}
					}
				}
				sendToAllClients("new#price#in#flower_" + flowerToUpdate.getFlowerName());
				return;
			}
			Session session = App.getSessionFactory().openSession();
			session.beginTransaction();
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<Flower> query = builder.createQuery(Flower.class);
			Root<Flower> root = query.from(Flower.class);
			query.select(root).where(builder.equal(root.get("flowerName"), flower.getFlowerName()));
			Flower flowerToUpdate = session.createQuery(query).uniqueResult();
			double originalPrice = flowerToUpdate.getFlowerPrice();
			double newPrice = originalPrice * (1 - discountPercent / 100.0);
			flowerToUpdate.setFlowerPrice(newPrice);
			flowerToUpdate.setSale(true);
			flowerToUpdate.setDiscount(discountPercent);
			session.update(flowerToUpdate);
			session.getTransaction().commit();
			System.out.println("Updated price for " + flowerToUpdate.getFlowerName() + ": " + newPrice);
			session.close();
			for (Store store : App.get_stores()) {
				for (Flower f : store.getFlowersList()) {
					if (f.getFlowerName().equals(flowerToUpdate.getFlowerName())) {
						f.setFlowerPrice(newPrice);
						f.setSale(true);
						f.setDiscount(discountPercent);
					}
				}
			}
			sendToAllClients("new#price#in#flower_" + flowerToUpdate.getFlowerName());
		}
		else if (msg.getClass().equals(LoginRegCheck.class))
		{
			System.out.println("entered LoginRegCheck");
			Session session = App.getSessionFactory().openSession();
			session.beginTransaction();
			LoginRegCheck new_user = (LoginRegCheck) msg;
			System.out.println("new_user name : " + new_user.getUsername());
			session = App.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(new_user);
				session.getTransaction().commit();
					} catch (Exception e) {
						session.getTransaction().rollback();
						e.printStackTrace();
					} finally {
						session.close();
					}// registration

		}
		else if (msg.getClass().equals(change_user_login.class)) {
			change_user_login wrapper = (change_user_login) msg;
			LoginRegCheck user = wrapper.get_user();
			int new_state = wrapper.get_the_new_state();

			Session session = null;

			try {
				session = App.getSessionFactory().openSession();
				session.beginTransaction();


				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<LoginRegCheck> query = builder.createQuery(LoginRegCheck.class);
				Root<LoginRegCheck> root = query.from(LoginRegCheck.class);
				query.select(root).where(builder.equal(root.get("username"), user.getUsername()));

				LoginRegCheck userInDb = session.createQuery(query).uniqueResult();

				if (userInDb != null) {
					userInDb.setIsLogin(new_state);
					session.update(userInDb);
				}

				session.getTransaction().commit();
			} catch (Exception e) {
				if (session != null) session.getTransaction().rollback();
				e.printStackTrace();
			} finally {
				if (session != null) session.close();
			}
		}
		else if (msgString.startsWith("getComplaints")) {
			try {
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Complain> query = builder.createQuery(Complain.class);
				query.from(Complain.class);
				CriteriaQuery<LoginRegCheck> query1 = builder.createQuery(LoginRegCheck.class);
				query1.from(LoginRegCheck.class);

				List<Complain> complainList = session.createQuery(query).getResultList();
				List<LoginRegCheck> loginRegCheckList = session.createQuery(query1).getResultList();
				for (Complain c : complainList){
					System.out.println(c.getComplaint());
					System.out.println("server getComplaints work!");
				}
				session.getTransaction().commit();
				session.close();

				ComplainUpdateEvent event = new ComplainUpdateEvent(complainList,loginRegCheckList);
				client.sendToClient(event);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(msg.getClass().equals(Complain.class)) {
			System.out.println("add complaint");

			Complain complain = (Complain) msg;

			Session session = App.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(complain);
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				e.printStackTrace();
			} finally {
				session.close();
			}


		}
		else if (msg.getClass().equals(change_sendOrRecieve_messages.class)) {
			change_sendOrRecieve_messages wrapper = (change_sendOrRecieve_messages) msg;

			String username = null;

			if (wrapper.get_user_name() != null) {
				username = wrapper.get_user_name();
			} else if (wrapper.get_user() != null) {
				username = wrapper.get_user().getUsername();
			}

			if (username != null) {
				boolean newSendState = wrapper.get_the_new_state_for_send();
				boolean newReceiveState = wrapper.get_the_new_state_for_recieve();

				Session session = null;
				try {
					session = App.getSessionFactory().openSession();
					session.beginTransaction();

					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaQuery<LoginRegCheck> query = builder.createQuery(LoginRegCheck.class);
					Root<LoginRegCheck> root = query.from(LoginRegCheck.class);
					query.select(root).where(builder.equal(root.get("username"), username));

					LoginRegCheck userInDb = session.createQuery(query).uniqueResult();

					if (userInDb != null) {
						userInDb.set_send(newSendState);
						userInDb.set_receive_answer(newReceiveState);
						session.update(userInDb);
					}

					session.getTransaction().commit();
				} catch (Exception e) {
					if (session != null) session.getTransaction().rollback();
					e.printStackTrace();
				} finally {
					if (session != null) session.close();
				}
				System.out.println("the user is " + username);
				sendToAllClients("user_" + username);



			}
		}

		else if (msgString.startsWith("need#to#change#user#localy"))
		{
			Session session = App.getSessionFactory().openSession();
			session.beginTransaction();
			String[] parts = msgString.split("_");
			String username = parts[1];
			CriteriaBuilder builder = session.getCriteriaBuilder();
			CriteriaQuery<LoginRegCheck> query = builder.createQuery(LoginRegCheck.class);
			Root<LoginRegCheck> root = query.from(LoginRegCheck.class);
			query.select(root).where(builder.equal(root.get("username"), username));
			LoginRegCheck user = session.createQuery(query).uniqueResult();
			try {
				Add_flower_event event = new Add_flower_event(null,0,user);
				client.sendToClient(event);
			} catch (Exception e) {
				e.printStackTrace();
			}


		}
		else if (msg instanceof Order)
		{
			Order order = (Order) msg;
			Session session = App.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				session.save(order);
				session.getTransaction().commit();

				// Send confirmation back to client
				client.sendToClient("order_success");

				// Notify all clients about the new order
				sendToAllClients("update_catalog_after_change");
			} catch (Exception e) {
				session.getTransaction().rollback();
				e.printStackTrace();
				try {
					client.sendToClient("order_error");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} finally {
				session.close();
			}
		}
		else if (msg instanceof CustomerOrdersRequest)
		{
			CustomerOrdersRequest request = (CustomerOrdersRequest) msg;
			Session session = App.getSessionFactory().openSession();
			try {
				session.beginTransaction();
				
				// Simple query to get orders first
				String orderHql = "FROM Order o WHERE o.customerName = :customerName ORDER BY o.orderDate DESC";
				List<Order> orders = session.createQuery(orderHql, Order.class)
					.setParameter("customerName", request.getCustomerName())
					.getResultList();
				
				System.out.println("Found " + orders.size() + " orders for customer: " + request.getCustomerName());
				
				List<OrderSummary> orderSummaries = new ArrayList<>();
				for (Order order : orders) {
					// For each order, get the items separately
					String itemsHql = "SELECT f.flowerName, ci.quantity FROM CartItem ci JOIN ci.flower f WHERE ci.order.id = :orderId";
					List<Object[]> items = session.createQuery(itemsHql)
						.setParameter("orderId", order.getId())
						.getResultList();
					
					// Build items summary
					StringBuilder itemsSummary = new StringBuilder();
					for (Object[] item : items) {
						if (itemsSummary.length() > 0) itemsSummary.append(", ");
						itemsSummary.append(item[0]).append(" x").append(item[1]);
					}
					
					if (itemsSummary.length() == 0) {
						itemsSummary.append("No items");
					}
					
					orderSummaries.add(new OrderSummary(
						order.getId(),
						order.getOrderDate(),
						order.getTotalAmount(),
						order.getStatus(),
						order.isRequiresDelivery(),
						itemsSummary.toString()
					));
				}
				
				session.getTransaction().commit();
				
				// Send order summaries back to client
				CustomerOrdersResponse response = new CustomerOrdersResponse(orderSummaries);
				client.sendToClient(response);
				
			} catch (Exception e) {
				if (session != null) session.getTransaction().rollback();
				e.printStackTrace();
				System.err.println("Error fetching orders: " + e.getMessage());
				try {
					client.sendToClient("error_fetching_orders");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} finally {
				if (session != null) session.close();
			}
		}
		else if(msgString.startsWith("Haifa"))
		{
			List<Store> Stores = App.get_stores();
			Store store = Stores.get(0);
			List<Flower> original_flowers=store.getFlowersList();
			String[] parts = msgString.split("_");
			List<Flower> sorted_flowers=store.getFlowersList();
			if(parts[1].trim().equals("1"))
			{
				store = Stores.get(0);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("2"))
			{
				store = Stores.get(1);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("3"))
			{
				store = Stores.get(2);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("4")) {

				try {
					Session session = App.getSessionFactory().openSession();
					session.beginTransaction();

					original_flowers = session.createQuery("FROM Flower", Flower.class).getResultList();

					session.getTransaction().commit();
					session.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				catalog_sort_event event = new catalog_sort_event(sorted_flowers,1,original_flowers);
				client.sendToClient(event);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return;


		}
		else if(msgString.startsWith("Krayot"))
		{
			List<Store> Stores = App.get_stores();
			Store store = Stores.get(1);
			List<Flower> original_flowers=store.getFlowersList();
			String[] parts = msgString.split("_");
			List<Flower> sorted_flowers=store.getFlowersList();
			if(parts[1].trim().equals("1"))
			{
				store = Stores.get(0);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("2"))
			{
				store = Stores.get(1);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("3"))
			{
				store = Stores.get(2);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("4")) {

				try {
					Session session = App.getSessionFactory().openSession();
					session.beginTransaction();

					original_flowers = session.createQuery("FROM Flower", Flower.class).getResultList();

					session.getTransaction().commit();
					session.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				catalog_sort_event event = new catalog_sort_event(sorted_flowers,2,original_flowers);
				client.sendToClient(event);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return;


		}
		else if(msgString.startsWith("Nahariyya"))
		{
			List<Store> Stores = App.get_stores();
			Store store = Stores.get(2);
			List<Flower> original_flowers=store.getFlowersList();
			String[] parts = msgString.split("_");
			List<Flower> sorted_flowers=store.getFlowersList();
			if(parts[1].trim().equals("1"))
			{
				store = Stores.get(0);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("2"))
			{
				store = Stores.get(1);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("3"))
			{
				store = Stores.get(2);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("4")) {

				try {
					Session session = App.getSessionFactory().openSession();
					session.beginTransaction();

					original_flowers = session.createQuery("FROM Flower", Flower.class).getResultList();

					session.getTransaction().commit();
					session.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				catalog_sort_event event = new catalog_sort_event(sorted_flowers,3,original_flowers);
				client.sendToClient(event);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return;


		}
		else if(msgString.startsWith("network"))
		{
			List<Store> Stores = App.get_stores();
			Store store = Stores.get(1);
			List<Flower> original_flowers=store.getFlowersList();
			List<Flower> sorted_flowers=store.getFlowersList();
			String[] parts = msgString.split("_");
			try {
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				original_flowers = session.createQuery("FROM Flower", Flower.class).getResultList();
				sorted_flowers = session.createQuery("FROM Flower", Flower.class).getResultList();
				System.out.println("The sorted flowers  original flowers are the ones from the database.");
				session.getTransaction().commit();
				session.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if(parts[1].trim().equals("1"))
			{
				store = Stores.get(0);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("2"))
			{
				store = Stores.get(1);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("3"))
			{
				store = Stores.get(2);
				original_flowers=store.getFlowersList();
			}
			if(parts[1].trim().equals("4")) {

				try {
					Session session = App.getSessionFactory().openSession();
					session.beginTransaction();

					original_flowers = session.createQuery("FROM Flower", Flower.class).getResultList();
					sorted_flowers = session.createQuery("FROM Flower", Flower.class).getResultList();
					session.getTransaction().commit();
					session.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			try {
				catalog_sort_event event = new catalog_sort_event(sorted_flowers,4,original_flowers);
				client.sendToClient(event);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return;


		}
		else if(msgString.startsWith("get_flowers_low_to_high"))
		{
			List<Store> Stores = App.get_stores();
			Store store = Stores.get(0);
			List<Flower> original_flowers=store.getFlowersList();
			String[] parts = msgString.split("_");
			if(parts[6].trim().equals("1"))
			{
				store = Stores.get(0);
				original_flowers=store.getFlowersList();
			}
			if(parts[6].trim().equals("2"))
			{
				store = Stores.get(1);
				original_flowers=store.getFlowersList();
			}
			if(parts[6].trim().equals("3"))
			{
				store = Stores.get(2);
				original_flowers=store.getFlowersList();
			}
			if (parts[5].trim().equals("4") )
			{
				List<Flower> flowers = getSortedFlowersFromDatabase("asc");
				if(parts[6].trim().equals("4")||parts[6].trim().equals("0"))
				{
					try {
						catalog_sort_event event = new catalog_sort_event(flowers,4,flowers);
						client.sendToClient(event);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					return;
				}
				else
				{
					try {
						catalog_sort_event event = new catalog_sort_event(flowers,4,original_flowers);
						client.sendToClient(event);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					return;
				}

			}


			List<Flower> sorted_flowers=store.getFlowersList();
			if(parts[5].trim().equals("1"))
			{
				store = Stores.get(0);
				sorted_flowers=store.getFlowersList();
				sorted_flowers = getFlowersOrdered("asc",sorted_flowers);
				try {
					catalog_sort_event event = new catalog_sort_event(sorted_flowers,1,original_flowers);
					client.sendToClient(event);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return;

			}
			if(parts[5].trim().equals("2"))
			{
				store = Stores.get(1);
				sorted_flowers=store.getFlowersList();
				sorted_flowers = getFlowersOrdered("asc",sorted_flowers);
				try {
					catalog_sort_event event = new catalog_sort_event(sorted_flowers,2,original_flowers);
					client.sendToClient(event);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return;
			}
			if(parts[5].trim().equals("3"))
			{
				store = Stores.get(2);
				sorted_flowers=store.getFlowersList();
				sorted_flowers = getFlowersOrdered("asc",sorted_flowers);
				try {
					catalog_sort_event event = new catalog_sort_event(sorted_flowers,3,original_flowers);
					client.sendToClient(event);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return;
			}

		}
		else if(msgString.startsWith("get_flowers_high_to_low"))
		{

			List<Store> Stores = App.get_stores();
			Store store = Stores.get(0);
			List<Flower> original_flowers=store.getFlowersList();
			String[] parts = msgString.split("_");
			if(parts[6].trim().equals("1"))
			{
				store = Stores.get(0);
				original_flowers=store.getFlowersList();
			}
			if(parts[6].trim().equals("2"))
			{
				store = Stores.get(1);
				original_flowers=store.getFlowersList();
			}
			if(parts[6].trim().equals("3"))
			{
				store = Stores.get(2);
				original_flowers=store.getFlowersList();
			}
			if (parts[5].trim().equals("4") )
			{
				List<Flower> flowers = getSortedFlowersFromDatabase("desc");
				if(parts[6].trim().equals("4")||parts[6].trim().equals("0"))
				{
					try {
						catalog_sort_event event = new catalog_sort_event(flowers,4,flowers);
						client.sendToClient(event);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					return;
				}
				else
				{
					try {
						catalog_sort_event event = new catalog_sort_event(flowers,4,original_flowers);
						client.sendToClient(event);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
					return;
				}

			}


			List<Flower> sorted_flowers=store.getFlowersList();
			if(parts[5].trim().equals("1"))
			{
				store = Stores.get(0);
				sorted_flowers=store.getFlowersList();
				sorted_flowers = getFlowersOrdered("desc",sorted_flowers);
				try {
					catalog_sort_event event = new catalog_sort_event(sorted_flowers,1,original_flowers);
					client.sendToClient(event);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return;

			}
			if(parts[5].trim().equals("2"))
			{
				store = Stores.get(1);
				sorted_flowers=store.getFlowersList();
				sorted_flowers = getFlowersOrdered("desc",sorted_flowers);
				try {
					catalog_sort_event event = new catalog_sort_event(sorted_flowers,2,original_flowers);
					client.sendToClient(event);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return;
			}
			if(parts[5].trim().equals("3"))
			{
				store = Stores.get(2);
				sorted_flowers=store.getFlowersList();
				sorted_flowers = getFlowersOrdered("desc",sorted_flowers);
				try {
					catalog_sort_event event = new catalog_sort_event(sorted_flowers,3,original_flowers);
					client.sendToClient(event);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return;
			}
		}
		else if (msgString.startsWith("I#want#to#see#my#answer_")) {
			System.out.println("good");

			String[] parts = msgString.split("_");
			String username = parts[1];
			String toSearch = "answer to" + username;
			System.out.println("toSearch: " + toSearch);

			Session session = null;

			try {
				session = App.getSessionFactory().openSession();
				session.beginTransaction();

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Complain> query = builder.createQuery(Complain.class);
				Root<Complain> root = query.from(Complain.class);

				query.select(root).where(builder.equal(root.get("clientName"), toSearch));

				List<Complain> matchingComplaints = session.createQuery(query).getResultList();

				session.getTransaction().commit();

				if (!matchingComplaints.isEmpty()) {

					Complain latest = matchingComplaints.stream()
							.max((c1, c2) -> c1.getTimestamp().compareTo(c2.getTimestamp()))
							.orElse(null);

					if (latest != null)
					{
						System.out.println("Found complaint: " + latest.getComplaint());
						client.sendToClient(latest);
					}
				} else {
					System.out.println("No matching complaints found for: " + toSearch);
				}


			} catch (Exception e) {
				if (session != null) session.getTransaction().rollback();
				e.printStackTrace();
			} finally {
				if (session != null) session.close();
			}
		}



	}

	public void sendToAllClients(String message) {
		try {
			for (SubscribedClient subscribedClient : SubscribersList) {
				subscribedClient.getClient().sendToClient(message);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private List<Flower> getFlowersOrdered(String direction, List<Flower> flowers) {
		List<Flower> result = new ArrayList<>(flowers);
		if (direction.equals("desc")) {
			result.sort((f1, f2) -> Double.compare(f2.getFlowerPrice(), f1.getFlowerPrice()));
		} else {
			result.sort((f1, f2) -> Double.compare(f1.getFlowerPrice(), f2.getFlowerPrice()));
		}
		return result;
	}
	private List<Flower> getSortedFlowersFromDatabase(String direction) {
		List<Flower> result = new ArrayList<>();
		try {
			Session session = App.getSessionFactory().openSession();
			session.beginTransaction();

			String hql = "FROM Flower ORDER BY flowerPrice " + (direction.equals("desc") ? "DESC" : "ASC");
			result = session.createQuery(hql, Flower.class).getResultList();

			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}




