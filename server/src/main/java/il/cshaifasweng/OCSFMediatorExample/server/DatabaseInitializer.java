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

            // Initialize orders for client users
            initializeOrders(session);

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
        
        Flower flower_5 = new Flower("Exotic Queen", 49.99, "Orchid", "images/FlowerImages/Orchid.png", "Pink", "Flower");
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
        
        Flower flower_10 = new Flower("Elegant Camellia", 24.99, "Lily", "images/FlowerImages/Camellia.png", "Pink", "Flower");
        session.save(flower_10); session.flush(); flowers.add(flower_10);
        


        // Premium flower



        Flower pf1 = new Flower("Classic Carnation", 12.99, "Lily", "images/FlowerImages/Carnation.png", "Pink", "Flower");
        session.save(pf1); session.flush(); flowers.add(pf1);
        
        Flower pf2 = new Flower("Autumn Chrysanthemum", 19.99, "Lily", "images/FlowerImages/Chrysanthemum.png", "White", "Flower");
        session.save(pf2); session.flush(); flowers.add(pf2);
        
        Flower pf3 = new Flower("Cosmic Cosmos", 16.99, "Daffodil", "images/FlowerImages/Cosmos.png", "White", "Flower");
        session.save(pf3); session.flush(); flowers.add(pf3);
        
        Flower pf4 = new Flower("Dazzling Dahlia", 21.99, "Daffodil", "images/FlowerImages/Dahlia.png", "Purple", "Flower");
        session.save(pf4); session.flush(); flowers.add(pf4);
        
        Flower pf5 = new Flower("Delicate Delphinium", 17.99, "Daffodil", "images/FlowerImages/Delphinium.png", "Blue", "Flower");
        session.save(pf5); session.flush(); flowers.add(pf5);
        
        Flower pf6 = new Flower("Fragrant Freesia", 14.99, "Daffodil", "images/FlowerImages/Freesia.png", "White", "Flower");
        session.save(pf6); session.flush(); flowers.add(pf6);
        
        Flower pf7 = new Flower("Gerbera Daisy", 13.99, "Daffodil", "images/FlowerImages/Gerbera.png", "Orange", "Flower");
        session.save(pf7); session.flush(); flowers.add(pf7);
        
        Flower pf10 = new Flower("Graceful Gladiolus", 20.99, "Orchid", "images/FlowerImages/Gladiolus.png", "Red", "Flower");
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
        
        Flower v5 = new Flower("Circle Vase", 17.99, "Vase", "images/FlowerImages/CircleVassa.png", "White", "Vase");
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
        LoginRegCheck tamar = new LoginRegCheck("tamar", "123", "tamar@", 0, false, 1,"0501112223", "tamar full", "326629754", false);
        session.save(tamar);
        session.flush();
        
        LoginRegCheck amit = new LoginRegCheck("amit", "123", "amit@", 0, false, 1, "0501112223", "amit full", "326227754", false);
        session.save(amit);
        session.flush();
        
        // Network Admin
        LoginRegCheck malci = new LoginRegCheck("malci", "123", "malci@", 0, true, 4, "0501112223", "malci full", "323229754", false);
        session.save(malci);
        session.flush();

        // System Administrator
        LoginRegCheck System = new LoginRegCheck("System", "123", "haifa.System@lilach.com", 0, true, 4, "0501112223", "System Cohen", "326529754", false);
        System.setEmployeetype(1);
        session.save(System);
        session.flush();

        // Customer service employee
        LoginRegCheck service = new LoginRegCheck("service", "123", "haifa.service@lilach.com", 0, true, 4, "0501112223", "service Cohen", "326209754", false);
        service.setEmployeetype(2);
        session.save(service);
        session.flush();

        //store managers
        LoginRegCheck Haifa = new LoginRegCheck("Haifa", "123", "haifa.HaifaWorker@gmail.com", 0, true, 1, "0501234567", "HaifaWorker", "326129754", false);
        session.save(Haifa);
        Haifa.setEmployeetype(0);
        session.flush();

        LoginRegCheck nahariyya = new LoginRegCheck("nahariyya", "123", "haifa.nahariyyaWorker@gmail.com", 0, true, 3, "0501234567", "nahariyyaWorker", "326222754", false);
        session.save(nahariyya);
        nahariyya.setEmployeetype(0);
        session.flush();

        LoginRegCheck Krayot = new LoginRegCheck("Krayot", "123", "haifa.KrayotWorker@gmail.com", 0, true, 2, "0501234567", "KrayotWorker", "326329754", false);
        session.save(Krayot);
        Krayot.setEmployeetype(0);
        session.flush();


        // Additional Client Users
        LoginRegCheck itayMalich = new LoginRegCheck("Itay", "123", "itay.malich2@gmail.com", 0, false, 4, "0501234567", "Itay Malich", "356229754", false);
        session.save(itayMalich);
        session.flush();

        //add workers of stores
        LoginRegCheck HaifaWorker = new LoginRegCheck("HaifaWorker", "123", "haifa.HaifaWorker@gmail.com", 0, true, 1, "0501234567", "HaifaWorker", "326227754", false);
        session.save(HaifaWorker);
        HaifaWorker.setEmployeetype(3);
        session.flush();

        LoginRegCheck nahariyyaWorker = new LoginRegCheck("nahariyyaWorker", "123", "haifa.nahariyyaWorker@gmail.com", 0, true, 3, "0501234567", "nahariyyaWorker", "328229754", false);
        session.save(nahariyyaWorker);
        nahariyyaWorker.setEmployeetype(3);
        session.flush();

        LoginRegCheck KrayotWorker = new LoginRegCheck("KrayotWorker", "123", "haifa.KrayotWorker@gmail.com", 0, true, 2, "0501234567", "KrayotWorker", "326929754", false);
        session.save(KrayotWorker);
        KrayotWorker.setEmployeetype(3);
        session.flush();

        
        LoginRegCheck asafYaakov = new LoginRegCheck("Asaf", "123", "Asafyaakov555@gmail.com", 0, false, 2, "0529876543", "Asaf Yaakov", "326209754", false);
        session.save(asafYaakov);
        session.flush();
        
        LoginRegCheck nissimDeri = new LoginRegCheck("Nissim", "123", "nissimderi123@gmail.com", 0, false, 1, "0545551234", "Nissim Deri", "326129754", false);
        session.save(nissimDeri);
        session.flush();
        
        LoginRegCheck yardNetziar = new LoginRegCheck("Yarden", "123", "yardnetziar@gmail.com", 0, false, 2, "0537778889", "Yarden Tziar", "326239754", false);
        session.save(yardNetziar);
        session.flush();
        
        LoginRegCheck dorG2005 = new LoginRegCheck("Dor", "12345", "dorg2005@gmail.com", 0, false, 1, "0584445556", "Dor Gilad", "326224754", false);
        session.save(dorG2005);
        session.flush();
        
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

    // Add 100 orders for client users, distributed by user and across different months (past and future)
    private static void initializeOrders(Session session) {
        System.out.println(">>> CREATING SAMPLE ORDERS FOR CLIENT USERS...");
        // Fetch all client users (type == false)
        List<LoginRegCheck> clients = session.createQuery("FROM LoginRegCheck WHERE type = false", LoginRegCheck.class).getResultList();
        if (clients.isEmpty()) {
            System.out.println("No client users found, skipping order creation.");
            return;
        }
        // Fetch all stores
        List<Store> stores = session.createQuery("FROM Store", Store.class).getResultList();
        if (stores.isEmpty()) {
            System.out.println("No stores found, skipping order creation.");
            return;
        }
        java.util.Random rand = new java.util.Random();
        int totalOrders = 100;
        int usersCount = clients.size();
        // We'll use a base date and offset by months for variety
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.DAY_OF_MONTH, 15); // Middle of month for variety
        long baseTime = cal.getTimeInMillis();
        int[] monthOffsets = {-3, -2, -1, 0, 1, 2, 3}; // 3 months back to 3 months forward
        int monthOffsetsLen = monthOffsets.length;
        // Example city/address data for realism
        String[] cities = {"Haifa", "Krayot", "Nahariyya", "Tel Aviv", "Jerusalem"};
        String[] streets = {"Herzl", "Ben Gurion", "Rothschild", "Weizmann", "Bialik", "Jabotinsky"};
        String[] greetingBackgrounds = {"bg1.jpg", "bg2.jpg", "bg3.jpg", "bg4.jpg"};
        String[] greetingMessages = {"Happy Birthday!", "Congratulations!", "Best Wishes!", "With Love!", "Thank You!"};
        // Skewed order distribution: some users get more orders
        int[] userOrderWeights = new int[clients.size()];
        int totalWeight = 0;
        for (int i = 0; i < clients.size(); i++) {
            userOrderWeights[i] = 1 + rand.nextInt(4); // 1-4 weight per user
            totalWeight += userOrderWeights[i];
        }
        // Assign orders proportionally to weights
        int[] ordersPerUser = new int[clients.size()];
        int assignedOrders = 0;
        for (int i = 0; i < clients.size(); i++) {
            ordersPerUser[i] = (int) Math.round((userOrderWeights[i] / (double) totalWeight) * totalOrders);
            assignedOrders += ordersPerUser[i];
        }
        // Adjust for rounding
        while (assignedOrders < totalOrders) { ordersPerUser[rand.nextInt(clients.size())]++; assignedOrders++; }
        while (assignedOrders > totalOrders) { int idx = rand.nextInt(clients.size()); if (ordersPerUser[idx] > 0) { ordersPerUser[idx]--; assignedOrders--; } }
        int orderIdx = 0;
        for (int u = 0; u < clients.size(); u++) {
            LoginRegCheck client = clients.get(u);
            int userOrders = ordersPerUser[u];
            // Determine allowed stores for this user
            List<Store> allowedStores = new ArrayList<>();
            if (client.getStore() == 4) {
                allowedStores.addAll(stores);
            } else {
                for (Store s : stores) {
                    if (s.getId() == client.getStore()) allowedStores.add(s);
                }
            }
            for (int i = 0; i < userOrders; i++, orderIdx++) {
                // Pick a random allowed store
                Store store = allowedStores.get(rand.nextInt(allowedStores.size()));
                // Pick 1-3 random flowers from the store
                List<Flower> storeFlowers = store.getFlowersList();
                int numFlowers = 1 + rand.nextInt(3);
                List<Flower> chosenFlowers = new ArrayList<>();
                for (int j = 0; j < numFlowers; j++) {
                    Flower flower = storeFlowers.get(rand.nextInt(storeFlowers.size()));
                    chosenFlowers.add(flower);
                }
                // Create order
                Order order = new Order(client.getUsername(), client.getEmail(), client);
                // Set the store ID where the order was created (user's store)
                order.setStoreId(client.getStore());
                // Set order status (no PENDING)
                String[] statuses = {"CANCELLED", "DELIVERED", "PICKED_UP", "CONFIRMED"};
                String status = statuses[rand.nextInt(statuses.length)];
                order.setStatus(status);
                boolean delivery = rand.nextBoolean();
                if (status.equals("DELIVERED")) {
                    order.setRequiresDelivery(true);
                } else if (status.equals("PICKED_UP")) {
                    order.setRequiresDelivery(false);
                } else {
                    order.setRequiresDelivery(delivery);
                }
                // Address details for delivery
                if (order.isRequiresDelivery()) {
                    order.setCity(cities[rand.nextInt(cities.length)]);
                    order.setStreetAddress(streets[rand.nextInt(streets.length)] + " " + (rand.nextInt(100) + 1));
                    order.setApartment(String.valueOf(rand.nextInt(20) + 1));
                }
                // Set delivery/pickup time: spread across different months (past/future)
                int monthOffset = monthOffsets[orderIdx % monthOffsetsLen];
                cal.setTimeInMillis(baseTime);
                cal.add(java.util.Calendar.MONTH, monthOffset);
                cal.set(java.util.Calendar.DAY_OF_MONTH, 1 + rand.nextInt(28));
                cal.set(java.util.Calendar.HOUR_OF_DAY, rand.nextInt(24));
                cal.set(java.util.Calendar.MINUTE, rand.nextInt(60));
                java.util.Date deliveryTime = cal.getTime();
                order.setDeliveryTime(deliveryTime);
                // Set order date: if delivery/pickup is in the future, order date should be before today
                java.util.Date nowDate = new java.util.Date();
                if (deliveryTime.after(nowDate)) {
                    long maxOrderTime = nowDate.getTime() - 3600_000L;
                    long minOrderTime = maxOrderTime - (14L * 24 * 3600_000L);
                    long orderDateMillis = minOrderTime + (long) (rand.nextDouble() * (maxOrderTime - minOrderTime));
                    order.setOrderDate(new java.util.Date(orderDateMillis));
                } else {
                    long orderDateMillis = deliveryTime.getTime() - (3600_000L * (1 + rand.nextInt(48)));
                    order.setOrderDate(new java.util.Date(orderDateMillis));
                }
                // Greeting card (randomly include)
                if (rand.nextDouble() < 0.4) { // 40% of orders have a greeting card
                    order.setIncludeGreetingCard(true);
                    order.setGreetingCardBackground(greetingBackgrounds[rand.nextInt(greetingBackgrounds.length)]);
                    order.setGreetingCardMessage(greetingMessages[rand.nextInt(greetingMessages.length)]);
                } else {
                    order.setIncludeGreetingCard(false);
                }
                // Add 1-3 cart items (already randomized above)
                for (Flower flower : chosenFlowers) {
                    int quantity = 1 + rand.nextInt(3);
                    CartItem item = new CartItem(flower, quantity, store.getStoreName());
                    order.addItem(item);
                }
                // No discounts applied to orders
                order.setDiscountAmount(0.0);
                
                // Ensure total includes delivery fee
                if (order.isRequiresDelivery()) {
                    order.setTotalAmount(order.getTotalAmount() + order.getDeliveryFee());
                }
                // Set order status logic for cancellation/refund
                if (status.equals("CANCELLED")) {
                    int hoursBefore = rand.nextInt(5);
                    long cancelTime = deliveryTime.getTime() - (hoursBefore * 3600_000L);
                    order.setCancellationDate(new java.util.Date(cancelTime));
                    if (hoursBefore >= 3) {
                        order.setRefundAmount(order.getTotalAmount());
                    } else if (hoursBefore >= 1) {
                        order.setRefundAmount(order.getTotalAmount() * 0.5);
                    } else {
                        order.setRefundAmount(0.0);
                    }
                    order.setCancellationReason("Cancelled by user");
                }
                session.save(order);
                session.flush();
            }
        }
        System.out.println(">>> CREATED 100 ORDERS FOR CLIENT USERS");
    }
}