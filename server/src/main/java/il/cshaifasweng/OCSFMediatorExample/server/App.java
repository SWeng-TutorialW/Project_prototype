package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.swing.JOptionPane;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Hello world!
 *
 */
public class App
{

	private static SimpleServer server;

    private static Session session;
    private static SessionFactory sessionFactory = null;
    private static List<Store> stores;

    public static List<Store> get_stores() {
        if (stores == null || stores.isEmpty()) {
            System.out.println("WARNING: Stores list is null or empty, attempting to reload...");
            loadStoresFromDatabase();
        }
        return stores != null ? stores : new ArrayList<>();
    }
    
    public static void refreshStores() {
        loadStoresFromDatabase();
    }

    static SessionFactory getSessionFactory() throws HibernateException {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.setProperty("hibernate.show_sql", "true");
            configuration.addAnnotatedClass(Flower.class);
            configuration.addAnnotatedClass(LoginRegCheck.class);
            configuration.addAnnotatedClass(Complain.class);
            configuration.addAnnotatedClass(Order.class);
            configuration.addAnnotatedClass(CartItem.class);
            configuration.addAnnotatedClass(Store.class);
            // Prompt for DB password
            String password = JOptionPane.showInputDialog(null, "Enter database password:", "Database Login", JOptionPane.PLAIN_MESSAGE);
            if (password == null) password = "";
            configuration.setProperty("hibernate.connection.password", password);
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            return sessionFactory;
        }
        return sessionFactory;
    }
    
    public static void loadStoresFromDatabase() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            
            System.out.println(">>> LOADING STORES FROM DATABASE...");
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Store> query = builder.createQuery(Store.class);
            query.from(Store.class);
            
            stores = session.createQuery(query).getResultList();
            
            System.out.println(">>> LOADED STORES FROM DATABASE:");
            System.out.println("Total stores found: " + stores.size());
            
            if (stores.isEmpty()) {
                System.out.println("WARNING: No stores found in database!");
                System.out.println("Checking if stores table exists and has data...");
                
                // Try a simple query to see if the table exists
                try {
                    String hql = "FROM Store";
                    List<Store> testStores = session.createQuery(hql, Store.class).getResultList();
                    System.out.println("HQL query found " + testStores.size() + " stores");
                } catch (Exception e) {
                    System.err.println("Error with HQL query: " + e.getMessage());
                }
                
                // Check if flowers exist
                try {
                    String hql = "FROM Flower";
                    List<Flower> testFlowers = session.createQuery(hql, Flower.class).getResultList();
                    System.out.println("HQL query found " + testFlowers.size() + " flowers");
                } catch (Exception e) {
                    System.err.println("Error with Flower HQL query: " + e.getMessage());
                }
            } else {
                for (Store store : stores) {
                    System.out.println("Store: " + store.getStoreName() + " - Flowers: " + store.getFlowersList().size());
                    for (Flower flower : store.getFlowersList()) {
                        System.out.println("  - " + flower.getFlowerName() + " (" + flower.getFlowerType() + ")");
                    }
                }
            }
            
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error loading stores from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testDatabaseConnection() {
        System.out.println(">>> TESTING DATABASE CONNECTION...");
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            
            // Test basic connection
            System.out.println("Database connection successful");
            
            // Test if tables exist
            try {
                String hql = "FROM Store";
                List<Store> stores = session.createQuery(hql, Store.class).getResultList();
                System.out.println("Stores table exists with " + stores.size() + " records");
            } catch (Exception e) {
                System.err.println("Stores table error: " + e.getMessage());
            }
            
            try {
                String hql = "FROM Flower";
                List<Flower> flowers = session.createQuery(hql, Flower.class).getResultList();
                System.out.println("Flowers table exists with " + flowers.size() + " records");
            } catch (Exception e) {
                System.err.println("Flowers table error: " + e.getMessage());
            }
            
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void verifyStoreFlowerRelationships() {
        System.out.println(">>> VERIFYING STORE-FLOWER RELATIONSHIPS...");
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            
            // Load all stores
            String hql = "FROM Store";
            List<Store> allStores = session.createQuery(hql, Store.class).getResultList();
            
            System.out.println("Found " + allStores.size() + " stores in database");
            
            for (Store store : allStores) {
                System.out.println("Store: " + store.getStoreName() + " (ID: " + store.getId() + ")");
                System.out.println("  Flowers count: " + store.getFlowersList().size());
                
                for (Flower flower : store.getFlowersList()) {
                    System.out.println("    - " + flower.getFlowerName() + " (ID: " + flower.getId() + ")");
                }
            }
            
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error verifying store-flower relationships: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) throws IOException
    {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            
            // Test database connection first
            testDatabaseConnection();
            
            // Check if database needs initialization
            if (!DatabaseInitializer.isDatabaseInitialized(sessionFactory)) {
                // Initialize database with all data
                DatabaseInitializer.initializeDatabase(sessionFactory);
            } else {
                // Database already has data, just load stores
                loadStoresFromDatabase();
            }

        } catch (Exception exception) {
            System.err.println("An error occurred during database setup.");
            exception.printStackTrace();
        }

        server = new SimpleServer(3000);
        server.listen();

        // Start the order status scheduler
        OrderStatusScheduler scheduler = new OrderStatusScheduler();
        scheduler.start();
    }
}