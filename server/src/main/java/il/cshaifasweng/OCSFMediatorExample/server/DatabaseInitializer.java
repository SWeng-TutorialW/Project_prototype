package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Database Initializer - Handles all database initialization including flowers, stores, and users
 */
public class DatabaseInitializer {
    

    public static void initializeDatabase(SessionFactory sessionFactory) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            
            System.out.println(">>> STARTING DATABASE INITIALIZATION...");
            
            // Initialize flowers
            List<Flower> flowers = initializeFlowers(session);
            
            // Initialize stores with flowers
            initializeStores(session, flowers);
            
            // Initialize users
            initializeUsers(session);
            
            // Load stores from database to ensure they have the proper flower relationships
            App.loadStoresFromDatabase();
            
            // Verify the relationships are working correctly
            verifyStoreFlowerRelationships(session);
            
            session.getTransaction().commit();
            System.out.println(">>> DATABASE INITIALIZATION COMPLETED SUCCESSFULLY!");
            
        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occurred during database initialization, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    

    private static List<Flower> initializeFlowers(Session session) {
        System.out.println(">>> INSERTING FLOWERS TO DB...");
        
        List<Flower> flowers = new ArrayList<>();
        
        // Create all flowers
        Flower flower_1 = new Flower("Romance Royalty", 25.99, "Rose", "images/FlowerImages/Rose.png", "Red", "Flower");
        session.save(flower_1);
        session.flush();
        flowers.add(flower_1);
        
        Flower flower_2 = new Flower("Sunny Smiler", 15.99, "Sunflower", "images/FlowerImages/Sunflower.png", "Yellow", "Flower");
        session.save(flower_2);
        session.flush();
        flowers.add(flower_2);
        
        Flower flower_3 = new Flower("Spring's Prince", 16.99, "Tulip", "images/FlowerImages/Tulip.png", "Pink", "Flower");
        session.save(flower_3);
        session.flush();
        flowers.add(flower_3);
        
        Flower flower_4 = new Flower("Purple Cloud", 30.99, "Jacarande", "images/FlowerImages/Jacarande.png", "Purple", "Flower");
        session.save(flower_4);
        session.flush();
        flowers.add(flower_4);
        
        Flower flower_5 = new Flower("Exotic Queen", 49.99, "Orchid", "images/FlowerImages/Orchid.png", "White", "Flower");
        session.save(flower_5);
        session.flush();
        flowers.add(flower_5);
        
        Flower flower_6 = new Flower("White Snowflake", 22.99, "Lily", "images/FlowerImages/Lily.png", "White", "Flower");
        session.save(flower_6);
        session.flush();
        flowers.add(flower_6);
        
        Flower flower_7 = new Flower("Golden Breeze", 29.99, "Daffodil", "images/FlowerImages/Daffodil.png", "Yellow", "Flower");
        session.save(flower_7);
        session.flush();
        flowers.add(flower_7);
        
        Flower flower_8 = new Flower("Blue Whisper", 16.99, "Hyacinth", "images/FlowerImages/Hyacinth.png", "Blue", "Flower");
        session.save(flower_8);
        session.flush();
        flowers.add(flower_8);


        Flower flower_9 = new Flower("Spring Begonia", 18.99, "Lily", "images/FlowerImages/Begonia.png", "Pink", "Flower");
        session.save(flower_9); session.flush(); flowers.add(flower_9);
        
        Flower flower_10 = new Flower("Elegant Camellia", 24.99, "Lily", "images/FlowerImages/Camellia.png", "White", "Flower");
        session.save(flower_10); session.flush(); flowers.add(flower_10);
        


        // Premium flower



        Flower pf1 = new Flower("Classic Carnation", 12.99, "Lily", "images/FlowerImages/Carnation.png", "Pink", "Flower");
        session.save(pf1); session.flush(); flowers.add(pf1);
        
        Flower pf2 = new Flower("Autumn Chrysanthemum", 19.99, "Lily", "images/FlowerImages/Chrysanthemum.png", "Yellow", "Flower");
        session.save(pf2); session.flush(); flowers.add(pf2);
        
        Flower pf3 = new Flower("Cosmic Cosmos", 16.99, "Daffodil", "images/FlowerImages/Cosmos.png", "Pink", "Flower");
        session.save(pf3); session.flush(); flowers.add(pf3);
        
        Flower pf4 = new Flower("Dazzling Dahlia", 21.99, "Daffodil", "images/FlowerImages/Dahlia.png", "Purple", "Flower");
        session.save(pf4); session.flush(); flowers.add(pf4);
        
        Flower pf5 = new Flower("Delicate Delphinium", 17.99, "Daffodil", "images/FlowerImages/Delphinium.png", "Blue", "Flower");
        session.save(pf5); session.flush(); flowers.add(pf5);
        
        Flower pf6 = new Flower("Fragrant Freesia", 14.99, "Daffodil", "images/FlowerImages/Freesia.png", "White", "Flower");
        session.save(pf6); session.flush(); flowers.add(pf6);
        
        Flower pf7 = new Flower("Gerbera Daisy", 13.99, "Daffodil", "images/FlowerImages/Gerbera.png", "Orange", "Flower");
        session.save(pf7); session.flush(); flowers.add(pf7);
        
        Flower pf10 = new Flower("Graceful Gladiolus", 20.99, "Orchid", "images/FlowerImages/Gladiolus.png", "Pink", "Flower");
        session.save(pf10); session.flush(); flowers.add(pf10);
        

       

        // Flowers Wreath
        Flower fw1 = new Flower("Yellow Wreath", 24.99, "Wreath", "images/FlowerImages/YellowWreath.png", "Yellow", "Flowers Wreath");
        session.save(fw1); session.flush(); flowers.add(fw1);
        
        Flower fw2 = new Flower("Pink Wreath", 26.99, "Wreath", "images/FlowerImages/PinkWreath.png", "Pink", "Flowers Wreath");
        session.save(fw2); session.flush(); flowers.add(fw2);
        
        Flower fw3 = new Flower("Purple Wreath", 25.99, "Wreath", "images/FlowerImages/PurpleWreath.png", "Purple", "Flowers Wreath");
        session.save(fw3); session.flush(); flowers.add(fw3);


        //  Vases
        Flower v1 = new Flower("Pink Vase", 19.99, "Vase", "images/FlowerImages/PinkVassa.png", "Pink", "Vase");
        session.save(v1); session.flush(); flowers.add(v1);
        
        Flower v2 = new Flower("Green Vase", 16.99, "Vase", "images/FlowerImages/GreenVassa.png", "Green", "Vase");
        session.save(v2); session.flush(); flowers.add(v2);
        
        Flower v3 = new Flower("Purple Vase", 20.99, "Vase", "images/FlowerImages/PurpleVassa.png", "Purple", "Vase");
        session.save(v3); session.flush(); flowers.add(v3);
        
        Flower v4 = new Flower("Squared Vase", 18.99, "Vase", "images/FlowerImages/SquaredVassa.png", "White", "Vase");
        session.save(v4); session.flush(); flowers.add(v4);
        
        Flower v5 = new Flower("Circle Vase", 17.99, "Vase", "images/FlowerImages/CircleVassa.png", "Clear", "Vase");
        session.save(v5); session.flush(); flowers.add(v5);


        // Flower Crowns
        Flower fc1 = new Flower("White Crown", 29.99, "Crown", "images/FlowerImages/WhiteCrown.png", "White", "Flower Crowns");
        session.save(fc1); session.flush(); flowers.add(fc1);
        
        Flower fc2 = new Flower("Purple Crown", 31.99, "Crown", "images/FlowerImages/PurpleCrown.png", "Purple", "Flower Crowns");
        session.save(fc2); session.flush(); flowers.add(fc2);
        
        Flower fc3 = new Flower("Pink Crown", 28.99, "Crown", "images/FlowerImages/PinkCrown.png", "Pink", "Flower Crowns");
        session.save(fc3); session.flush(); flowers.add(fc3);

        
        System.out.println(">>> CREATED " + flowers.size() + " FLOWERS");
        return flowers;
    }
    
    /**
     * Initialize all stores with their flower relationships
     * @param session The Hibernate session
     * @param flowers List of all flowers
     */
    private static void initializeStores(Session session, List<Flower> flowers) {
        System.out.println(">>> CREATING STORES WITH FLOWERS...");
        
        // Create random flower lists for each store (12-15 flowers each)
        List<Flower> haifaFlowers = new ArrayList<>(Arrays.asList(
            flowers.get(0),  // Rose
            flowers.get(1),  // Sunflower
            flowers.get(4),  // Orchid
            flowers.get(5),  // Lily
            flowers.get(9),  // Camellia
            flowers.get(10), // Carnation
            flowers.get(18), // Yellow Wreath
            flowers.get(21), // Pink Vase
            flowers.get(22), // Green Vase
            flowers.get(27)  // Purple Crown
        ));
        
        List<Flower> krayotFlowers = new ArrayList<>(Arrays.asList(

            flowers.get(2),  // Tulip
            flowers.get(10), // Carnation
            flowers.get(11), // Chrysanthemum
            flowers.get(12), // Cosmos
            flowers.get(19), // Pink Wreath
            flowers.get(20), // Purple Wreath
            flowers.get(23), // Purple Vase
            flowers.get(28)  // Pink Crown
        ));
        
        List<Flower> nahariyyaFlowers = new ArrayList<>(Arrays.asList(
            flowers.get(1),  // Sunflower
            flowers.get(6),  // Daffodil
            flowers.get(7),  // Hyacinth
            flowers.get(10), // Carnation
            flowers.get(13), // Dahlia
            flowers.get(14), // Delphinium
            flowers.get(16), // Gerbera
            flowers.get(19), // Pink Wreath
            flowers.get(20), // Purple Wreath
            flowers.get(22), // Green Vase
            flowers.get(25), // Circle Vase            flowers.get(26), // White Crown
            flowers.get(27)  // Purple Crown

        ));
        
        // Create stores
        Store lilach_Haifa = new Store("lilach_Haifa", "Abba khoushy", haifaFlowers);
        session.save(lilach_Haifa);
        session.flush();
        
        Store lilach_Krayot = new Store("lilach_Krayot", "Hasolel Bonneh", krayotFlowers);
        session.save(lilach_Krayot);
        session.flush();
        
        Store lilach_Nahariyya = new Store("lilach_Nahariyya", "Herzl", nahariyyaFlowers);
        session.save(lilach_Nahariyya);
        session.flush();
        
        System.out.println(">>> CREATED 3 STORES");
    }
    
    /**
     * Initialize all users in the database
     * @param session The Hibernate session
     */
    private static void initializeUsers(Session session) {
        System.out.println(">>> CREATING USERS...");
        
        // Haifa Store Workers
        
        // Krayot Store Workers
        
        
        // Nahariyya Store Workers
        
        // Regular Customers
        LoginRegCheck tamar = new LoginRegCheck("tamar", "123", "tamar@", 0, false, 1);
        session.save(tamar);
        session.flush();
        
        LoginRegCheck amit = new LoginRegCheck("amit", "123", "amit@", 0, false, 1);
        session.save(amit);
        session.flush();
        
        // Network Admin
        LoginRegCheck malci = new LoginRegCheck("malci", "123", "malci@", 0, true, 4);
        session.save(malci);
        session.flush();
        
        // Store Workers
        LoginRegCheck haifaWorker = new LoginRegCheck("Haifa", "123", "haifa.worker@lilach.com", 0, true, 1, "050-1112223", "Haifa Cohen", "111222333", false);
        session.save(haifaWorker);
        session.flush();
        
        LoginRegCheck krayotWorker = new LoginRegCheck("Krayot", "123", "krayot.worker@lilach.com", 0, true, 2, "052-4445556", "Krayot Levy", "444555666", false);
        session.save(krayotWorker);
        session.flush();
        
        LoginRegCheck nahariyyaWorker = new LoginRegCheck("Nahariyya", "123", "nahariyya.worker@lilach.com", 0, true, 3, "054-7778889", "Nahariyya Rosenberg", "777888999", false);
        session.save(nahariyyaWorker);
        session.flush();
        
        // Additional Client Users
        LoginRegCheck itayMalich = new LoginRegCheck("Itay", "123", "itay.malich2@gmail.com", 0, false, 4, "050-1234567", "Itay Malich", "123456789", false);
        session.save(itayMalich);
        session.flush();
        
        LoginRegCheck asafYaakov = new LoginRegCheck("Asaf", "123", "Asafyaakov555@gmail.com", 0, false, 2, "052-9876543", "Asaf Yaakov", "987654321", false);
        session.save(asafYaakov);
        session.flush();
        
        LoginRegCheck nissimDeri = new LoginRegCheck("Nissim", "123", "nissimderi123@gmail.com", 0, false, 1, "054-5551234", "Nissim Deri", "456789123", false);
        session.save(nissimDeri);
        session.flush();
        
        LoginRegCheck yardNetziar = new LoginRegCheck("Yarden", "123", "yardnetziar@gmail.com", 0, false, 2, "053-7778889", "Yarden Tziar", "789123456", false);
        session.save(yardNetziar);
        session.flush();
        
        LoginRegCheck dorG2005 = new LoginRegCheck("Dor", "12345", "dorg2005@gmail.com", 0, false, 1, "058-4445556", "Dor Gilad", "321654987", false);
        session.save(dorG2005);
        session.flush();
        
        System.out.println(">>> CREATED 14 USERS");
    }
    
    /**
     * Verify store-flower relationships are working correctly
     * @param session The Hibernate session
     */
    private static void verifyStoreFlowerRelationships(Session session) {
        System.out.println(">>> VERIFYING STORE-FLOWER RELATIONSHIPS...");
        
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
    }
    
    /**
     * Check if database is already initialized
     * @param sessionFactory The Hibernate session factory
     * @return true if database has data, false otherwise
     */
    public static boolean isDatabaseInitialized(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            
            // Check if stores exist
            String hql = "FROM Store";
            List<Store> stores = session.createQuery(hql, Store.class).getResultList();
            
            // Check if flowers exist
            String flowerHql = "FROM Flower";
            List<Flower> flowers = session.createQuery(flowerHql, Flower.class).getResultList();
            
            // Check if users exist
            String userHql = "FROM LoginRegCheck";
            List<LoginRegCheck> users = session.createQuery(userHql, LoginRegCheck.class).getResultList();
            
            session.getTransaction().commit();
            
            boolean hasData = !stores.isEmpty() && !flowers.isEmpty() && !users.isEmpty();
            System.out.println("Database initialization check: " + 
                "Stores=" + stores.size() + ", Flowers=" + flowers.size() + ", Users=" + users.size() + 
                " -> " + (hasData ? "ALREADY INITIALIZED" : "NEEDS INITIALIZATION"));
            
            return hasData;
            
        } catch (Exception e) {
            System.err.println("Error checking database initialization: " + e.getMessage());
            return false;
        }
    }
} 