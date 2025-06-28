package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


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
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            return configuration.buildSessionFactory(serviceRegistry);
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
            
        session = sessionFactory.openSession();
        session.beginTransaction();
        System.out.println(">>> INSERTING FLOWERS TO DB...");
        Flower flower_1 = new Flower("Romance Royalty",11.99,"Rose", "images/FlowerImages/Rose.png", "Red", "Flower");
        session.save(flower_1);
        session.flush();
        Flower flower_2 = new Flower("Sunny Smiler",6.99,"Sunflower", "images/FlowerImages/Sunflower.png", "Yellow", "Flower");
        session.save(flower_2);
        session.flush();
        Flower flower_3 = new Flower("Spring's Prince",3.99,"Tulip", "images/FlowerImages/Tulip.png", "Pink", "Flower");
        session.save(flower_3);
        session.flush();
        Flower flower_4 = new Flower("Purple Cloud",8.99,"Jacarande", "images/FlowerImages/Jacarande.png", "Purple", "Flower");
        session.save(flower_4);
        session.flush();
        Flower flower_5 = new Flower("Exotic Queen",9.99,"Orchid", "images/FlowerImages/Orchid.png", "White", "Flower");
        session.save(flower_5);
        session.flush();
        Flower flower_6 = new Flower("White Snowflake", 6.49, "Lily", "images/FlowerImages/Lily.png", "White", "Flower");
        session.save(flower_6);
        session.flush();
        Flower flower_7 = new Flower("Golden Breeze", 7.99, "Daffodil", "images/FlowerImages/Daffodil.png", "Yellow", "Flower");
        session.save(flower_7);
        session.flush();
        Flower flower_8 = new Flower("Blue Whisper", 5.49, "Hyacinth", "images/FlowerImages/Hyacinth.png", "Blue", "Flower");
        session.save(flower_8);
        session.flush();
        List<Flower> haifaFlowers = new ArrayList<>(Arrays.asList(flower_1, flower_2, flower_3, flower_4, flower_5, flower_6, flower_7));
        List<Flower> krayotFlowers = new ArrayList<>(Arrays.asList(flower_1, flower_2, flower_3, flower_4, flower_5, flower_6, flower_7, flower_8));
        List<Flower>nahariyyaFlowers = new ArrayList<>(Arrays.asList(flower_1, flower_2, flower_3, flower_4, flower_5, flower_6));
        Store lilach_Haifa = new Store("lilach_Haifa", "Abba khoushy", haifaFlowers);
        Store lilach_Krayot = new Store("lilach_Krayot", "Hasolel Bonneh", krayotFlowers);
        Store lilach_Nahariyya = new Store("lilach_Nahariyya", "Herzl", nahariyyaFlowers);;
        session.save(lilach_Haifa);
        session.flush();
        session.save(lilach_Krayot);
        session.flush();
        session.save(lilach_Nahariyya);
        session.flush();
        LoginRegCheck Nissim = new  LoginRegCheck("Nissim","123","Nissim@",0,true,2);
        session.save(Nissim);
        session.flush();
        LoginRegCheck Yarden = new  LoginRegCheck("Yarden","123","Yarden@",0,true,2);
        session.save(Yarden);
        session.flush();
        // Nissim and Yarden  are workes of karayot
        LoginRegCheck Itay = new  LoginRegCheck("Itay","123","Itay@",0,true,3);
        session.save(Itay);
        session.flush();
        LoginRegCheck Diana = new  LoginRegCheck("Diana","123","Diana@",0,true,3);
        session.save(Diana);
        session.flush();
        // Itay and Diana  are workes of Nahariyya
        LoginRegCheck Dor = new  LoginRegCheck("Dor","123","Dor@",0,true,1);
        session.save(Dor);
        session.flush();
        LoginRegCheck Asaf = new  LoginRegCheck("Asaf","123","Asaf@",0,true,1);
        session.save(Asaf);
        session.flush();
        // Asaf and Dor  are workes of Haifa
        LoginRegCheck tamar = new  LoginRegCheck("tamar","123","Asaf@",0,false,1);
        session.save(tamar);
        session.flush();

        LoginRegCheck amit = new  LoginRegCheck("amit","123","amit@",0,false,1);
        session.save(amit);
        session.flush();

        LoginRegCheck malci = new  LoginRegCheck("malci","123","Asaf@",0,true,4);
        session.save( malci);
        session.flush();

        // Load stores from database to ensure they have the proper flower relationships
        loadStoresFromDatabase();
        
        // Verify the relationships are working correctly
        verifyStoreFlowerRelationships();

        session.getTransaction().commit();// Save everything.



    } catch (Exception exception) {
    if (session != null) {
        session.getTransaction().rollback();
    }
    System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
} finally {
            if (session != null){
                session.close();
            }

}
        server = new SimpleServer(3000);
        server.listen();

        // Start the order status scheduler
        OrderStatusScheduler scheduler = new OrderStatusScheduler();
        scheduler.start();
    }


    }