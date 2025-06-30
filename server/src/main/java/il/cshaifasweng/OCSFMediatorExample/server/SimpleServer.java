package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.entities.CartItem;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.util.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.SubscribedClient;
import org.hibernate.Session;

import il.cshaifasweng.OCSFMediatorExample.server.EmailService;



public class SimpleServer extends AbstractServer {
	private static ArrayList<SubscribedClient> SubscribersList = new ArrayList<>();
	// Add a map to track user-client associations
	private static Map<ConnectionToClient, String> clientUserMap = new HashMap<>();

	public SimpleServer(int port) {
		super(port);

	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws IOException {
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
			// Get the username associated with this client
			String username = clientUserMap.get(client);

			if (username != null) {
				System.out.println("Client disconnected, logging out user: " + username);

				// Log out the user in the database
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
						userInDb.setIsLogin(0); // Set to logged out
						session.update(userInDb);
						System.out.println("User " + username + " logged out due to client disconnect");
					}

					session.getTransaction().commit();
				} catch (Exception e) {
					if (session != null) session.getTransaction().rollback();
					e.printStackTrace();
				} finally {
					if (session != null) session.close();
				}

				// Remove from tracking
				clientUserMap.remove(client);
			}

			// Remove from subscribers list
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

				System.out.println("=== DEBUG: Store Loading ===");
				System.out.println("Stores list size: " + (Stores != null ? Stores.size() : "NULL"));
				if (Stores != null && !Stores.isEmpty()) {
					for (int i = 0; i < Stores.size(); i++) {
						Store store = Stores.get(i);
						System.out.println("Store " + i + ": " + store.getStoreName() + " - Flowers: " + store.getFlowersList().size());
					}
				} else {
					System.out.println("WARNING: Stores list is empty or null! Attempting to refresh...");
					App.refreshStores();
					Stores = App.get_stores();
					System.out.println("After refresh - Stores list size: " + (Stores != null ? Stores.size() : "NULL"));
				}

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
				System.err.println("ERROR in getCatalogTable_login: " + e.getMessage());
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
		else if (msgString.startsWith("delete_flower_from_store_"))
		{
			System.out.println("=== DELETE FLOWER FROM STORE DEBUG ===");
			System.out.println("Received message: " + msgString);
			
			try {
				// Remove the prefix to get the rest of the message
				String messageContent = msgString.substring("delete_flower_from_store_".length());
				
				// Split by underscore - format: flower_id_store_id
				String[] parts = messageContent.split("_");
				
				if (parts.length < 2) {
					System.err.println("Invalid message format: " + msgString);
					return;
				}
				
				// Parse the parts: flower_id, store_id
				int flower_id = Integer.parseInt(parts[0]);
				int store_id = Integer.parseInt(parts[1]);
				
				System.out.println("Removing flower with ID: " + flower_id + " from store ID: " + store_id);

				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				// Find the flower by ID
				Flower flowerToRemove = session.get(Flower.class, flower_id);
				
				if (flowerToRemove != null) {
					// Find the specific store by ID
					Store targetStore = session.get(Store.class, store_id);
					if (targetStore != null) {
						// Remove the flower from this specific store only
						boolean removed = targetStore.getFlowersList().removeIf(f -> f.getId() == flower_id);
						
						if (removed) {
							// Update the store in the database
							session.update(targetStore);
							session.getTransaction().commit();
							
							System.out.println("Flower '" + flowerToRemove.getFlowerName() + "' removed from store '" + targetStore.getStoreName() + "'");
							
							// Refresh stores from database to get updated data
							App.refreshStores();
							
							// Get the updated store with fresh flower list from database
							Store updatedStore = App.get_stores().stream()
								.filter(s -> s.getId() == store_id)
								.findFirst()
								.orElse(null);
							
							if (updatedStore != null) {
								// Send the updated flower list to all clients
								update_local_catalog storeEvent = new update_local_catalog(updatedStore.getFlowersList(), store_id);
								sendToAllClients(storeEvent);
								System.out.println("Sent updated flower list to clients: " + updatedStore.getFlowersList().size() + " flowers");
								System.out.println("Delete event details: catalog_type=" + store_id + ", store_name=" + targetStore.getStoreName() + ", deleted_flower=" + flowerToRemove.getFlowerName());
							}
						} else {
							System.err.println("Flower was not in the store");
							session.getTransaction().rollback();
						}
					} else {
						System.err.println("Store with ID " + store_id + " not found");
						session.getTransaction().rollback();
					}
				} else {
					System.err.println("Flower with ID " + flower_id + " not found");
					session.getTransaction().rollback();
				}
				session.close();
			} catch (Exception e) {
				System.err.println("Error processing delete_flower_from_store message: " + msgString);
				e.printStackTrace();
			}
			System.out.println("=== END DELETE FLOWER FROM STORE DEBUG ===");
		}
		else if (msgString.startsWith("delete_flower_")) {
			// Handle flower deletion
			String[] parts = msgString.split("_");
			if (parts.length >= 4) {
				try {
					int flowerId = Integer.parseInt(parts[2]);
					String storeName = parts[3];
					
					System.out.println("Deleting flower ID " + flowerId + " from store: " + storeName);
					
					Session session = App.getSessionFactory().openSession();
					session.beginTransaction();
					
					// Get the flower
					Flower flower = session.get(Flower.class, flowerId);
					if (flower == null) {
						System.out.println("Flower not found with ID: " + flowerId);
						session.close();
						return;
					}
					
					if ("network".equals(storeName)) {
						// Delete from Flowers table and all store_flowers relationships
						// First remove from all stores
						for (Store store : App.get_stores()) {
							store.getFlowersList().removeIf(f -> f.getId() == flowerId);
							session.update(store);
						}
						// Then delete the flower
						session.delete(flower);
						session.getTransaction().commit();
						System.out.println("Deleted flower from network (Flowers table)");
						
						// Send updated network flower list to all clients
						String hql = "FROM Flower";
						List<Flower> allFlowers = session.createQuery(hql, Flower.class).getResultList();
						Add_flower_event event = new Add_flower_event(allFlowers, -1);
						sendToAllClients(event);
						System.out.println("Sent network deletion event to all clients: catalog_type=-1, flowers_count=" + allFlowers.size() + ", deleted_flower=" + flower.getFlowerName());
					} else {
						// Remove from specific store only
						Store store = App.get_stores().stream()
							.filter(s -> s.getStoreName().equals(storeName))
							.findFirst()
							.orElse(null);
						
						if (store != null) {
							store.getFlowersList().removeIf(f -> f.getId() == flowerId);
							session.update(store);
							session.getTransaction().commit();
							System.out.println("Removed flower from store: " + storeName);
							
							// Send updated store flower list to all clients
							update_local_catalog storeEvent = new update_local_catalog(store.getFlowersList(), store.getId());
							sendToAllClients(storeEvent);
						}
					}
					session.close();
				} catch (NumberFormatException e) {
					System.err.println("Invalid flower ID format: " + parts[2]);
				}
			}
		} else if (msgString.startsWith("update_flower_price_")) {
			// Handle flower price updates
			String[] parts = msgString.split("_");
			if (parts.length >= 5) {
				try {
					int flowerId = Integer.parseInt(parts[3]);
					double newPrice = Double.parseDouble(parts[4]);
					
					System.out.println("Updating price for flower ID " + flowerId + " to " + newPrice);
					
					Session session = App.getSessionFactory().openSession();
					session.beginTransaction();
					
					// Get the flower and update its price
					Flower flower = session.get(Flower.class, flowerId);
					if (flower != null) {
						flower.setFlowerPrice(newPrice);
						session.update(flower);
						session.getTransaction().commit();
						System.out.println("Updated flower price in database");
						
						// Send updated network flower list to all clients
						String hql = "FROM Flower";
						List<Flower> allFlowers = session.createQuery(hql, Flower.class).getResultList();
						Add_flower_event event = new Add_flower_event(allFlowers, -1);
						sendToAllClients(event);
					} else {
						System.out.println("Flower not found with ID: " + flowerId);
						session.getTransaction().rollback();
					}
					session.close();
				} catch (NumberFormatException e) {
					System.err.println("Invalid flower ID or price format");
				}
			}
		}
		else if (msg.getClass().equals(Flower.class)) {
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
			
			// Send automatic update to all clients
			try {
				String hql = "FROM Flower";
				Session updateSession = App.getSessionFactory().openSession();
				updateSession.beginTransaction();
				List<Flower> updatedFlowers = updateSession.createQuery(hql, Flower.class).getResultList();
				updateSession.getTransaction().commit();
				updateSession.close();
				
				Add_flower_event addEvent = new Add_flower_event(updatedFlowers, 4);
				sendToAllClients(addEvent);
				System.out.println("Sent automatic update to all clients after adding new flower");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			sendToAllClients("The network manager has added a flower.");

		}
		if(msg.equals("end_sale"))
		{
			try {
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				System.out.println("finish sale for all");


				String hql = "FROM Flower";
				List<Flower> flowersFromDB = session.createQuery(hql, Flower.class).getResultList();

				for (Flower flower : flowersFromDB) {
					int discount_percent = flower.getDiscount();
					double remainingPercent = 100.0 - discount_percent;
					double originalPrice = flower.getFlowerPrice() * 100.0 / remainingPercent;

					System.out.println("  [DB] Flower: " + flower.getFlowerName());
					System.out.println("    [DB] Original Price: " + originalPrice);


					flower.setFlowerPrice(originalPrice);
					flower.setSale(false);
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

			sendToAllClients("new#price#in#flower_all_end");

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
					// Calculate original price - if flower is already on sale, calculate the original price first
					double originalPrice;
					if (flower.isSale()) {
						double discountedPrice = flower.getFlowerPrice();
						int currentDiscount = flower.getDiscount();
						double remainingPercent = 100.0 - currentDiscount;
						originalPrice = discountedPrice * 100.0 / remainingPercent;
					} else {
						originalPrice = flower.getFlowerPrice();
					}

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
				
				// Send automatic update to all clients
				try {
					String hql = "FROM Flower";
					Session updateSession = App.getSessionFactory().openSession();
					updateSession.beginTransaction();
					List<Flower> updatedFlowers = updateSession.createQuery(hql, Flower.class).getResultList();
					updateSession.getTransaction().commit();
					updateSession.close();
					
					Add_flower_event deleteEvent = new Add_flower_event(updatedFlowers, -1);
					sendToAllClients(deleteEvent);
					System.out.println("Sent automatic update to all clients after flower deletion");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				sendToAllClients("The network manager has deleted flower.");
				return;
			}
			if (discountPercent == -2)
			{
				// Calculate original price from discounted price
				double discountedPrice = flower.getFlowerPrice();
				int currentDiscount = flower.getDiscount();
				double remainingPercent = 100.0 - currentDiscount;
				double originalPrice = discountedPrice * 100.0 / remainingPercent;

				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Flower> query = builder.createQuery(Flower.class);
				Root<Flower> root = query.from(Flower.class);
				query.select(root).where(builder.equal(root.get("flowerName"), flower.getFlowerName()));
				Flower flowerToUpdate = session.createQuery(query).uniqueResult();
				flowerToUpdate.setFlowerPrice(originalPrice);
				flowerToUpdate.setSale(false);
				flowerToUpdate.setDiscount(0);
				session.update(flowerToUpdate);
				session.getTransaction().commit();
				System.out.println("Flower : " + flowerToUpdate.getFlowerName());
				System.out.println("Original Price Restored: " + originalPrice);
				session.close();
				for (Store store : App.get_stores()) {
					for (Flower f : store.getFlowersList()) {
						if (f.getFlowerName().equals(flowerToUpdate.getFlowerName())) {
							f.setFlowerPrice(originalPrice);
							f.setSale(false);
							f.setDiscount(0);
						}
					}
				}
				
				// Send automatic update to all clients
				try {
					String hql = "FROM Flower";
					Session updateSession = App.getSessionFactory().openSession();
					updateSession.beginTransaction();
					List<Flower> updatedFlowers = updateSession.createQuery(hql, Flower.class).getResultList();
					updateSession.getTransaction().commit();
					updateSession.close();
					
					discount_for_1_flower endSaleEvent = new discount_for_1_flower(updatedFlowers, -1, flowerToUpdate.getFlowerName());
					sendToAllClients(endSaleEvent);
					System.out.println("Sent automatic update to all clients after ending sale");
				} catch (Exception e) {
					e.printStackTrace();
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

			// Calculate original price - if flower is already on sale, calculate the original price first
			double originalPrice;
			if (flowerToUpdate.isSale()) {
				double discountedPrice = flowerToUpdate.getFlowerPrice();
				int currentDiscount = flowerToUpdate.getDiscount();
				double remainingPercent = 100.0 - currentDiscount;
				originalPrice = discountedPrice * 100.0 / remainingPercent;
			} else {
				originalPrice = flowerToUpdate.getFlowerPrice();
			}

			double newPrice = originalPrice * (1 - discountPercent / 100.0);
			flowerToUpdate.setFlowerPrice(newPrice);
			flowerToUpdate.setSale(true);
			flowerToUpdate.setDiscount(discountPercent);
			session.update(flowerToUpdate);
			session.getTransaction().commit();
			System.out.println("Updated price for " + flowerToUpdate.getFlowerName() + ": " + newPrice);
			System.out.println("Original price was: " + originalPrice + ", New discount: " + discountPercent + "%");
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
			// Send automatic update to all clients
			try {
				String hql = "FROM Flower";
				Session updateSession = App.getSessionFactory().openSession();
				updateSession.beginTransaction();
				List<Flower> updatedFlowers = updateSession.createQuery(hql, Flower.class).getResultList();
				updateSession.getTransaction().commit();
				updateSession.close();
				
				discount_for_1_flower discountEvent = new discount_for_1_flower(updatedFlowers, 4, flowerToUpdate.getFlowerName());
				sendToAllClients(discountEvent);
				System.out.println("Sent automatic update to all clients after applying discount");
				System.out.println("Discount event details: catalog_type=4, flower_name=" + flowerToUpdate.getFlowerName() + ", flowers_count=" + updatedFlowers.size());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			sendToAllClients("new#price#in#flower_" + flowerToUpdate.getFlowerName());
		}
		else if (msg.getClass().equals(LoginRegCheck.class)) // login and registration
		{
			System.out.println("entered LoginRegCheck");
			Session session = null;
			LoginRegCheck new_user = (LoginRegCheck) msg;
			System.out.println("new_user name : " + new_user.getUsername());

			try {
				session = App.getSessionFactory().openSession();
				session.beginTransaction();
				List<LoginRegCheck> existingUser = session.createQuery("FROM LoginRegCheck lr WHERE username = :username OR idNum=:idnum", LoginRegCheck.class)
						.setParameter("username", new_user.getUsername()).setParameter("idnum", new_user.getIdNum())
						.getResultList();
				session.getTransaction().commit();
				if (!existingUser.isEmpty()) {
					// User tried to set a username that already exists for another user
					System.err.println("ERROR: Someone tried to Register with a username/ID into someone else's, NOT ALLOWED!");
					client.sendToClient("#registerFail");
					session.close();
					return;
				}
				session.close();
				session = App.getSessionFactory().openSession();
				session.beginTransaction();
				session.save(new_user); // INSERT statement
				session.getTransaction().commit();
				System.out.println("User registered successfully: " + new_user.getUsername());
				client.sendToClient("#registerSuccess");

			}
			 catch (Exception e) {
				if (session != null) session.getTransaction().rollback();
				e.printStackTrace();
				System.err.println("ERROR: Could not register user, transaction failed.\n");
				return;
			} finally {
				if (session != null) session.close();
			}
		}
		else if (msg.getClass().equals(UpdateUserEvent.class)){ // USER UPDATE
			LoginRegCheck userToUpdate = ((UpdateUserEvent) msg).getUpdatedUser();
			Session session = null;
			try{
				session = App.getSessionFactory().openSession();
				session.beginTransaction();
				// Check if another user (not the current one) has the same username or ID
				List<LoginRegCheck> existingUser = session.createQuery("FROM LoginRegCheck lr WHERE idNum = :idnum AND username!=:user", LoginRegCheck.class)
						.setParameter("idnum", userToUpdate.getIdNum()).setParameter("user", userToUpdate.getUsername())
						.getResultList();
				session.getTransaction().commit();
				if(!existingUser.isEmpty()) {
					// Another user already has this username or ID
					System.err.println("ERROR: Someone tried to set his ID Number into someone else's, NOT ALLOWED!");
					client.sendToClient(new Warning("#updateFail"));
					session.close();
					return;
				}
				session.close();
				session = App.getSessionFactory().openSession();
				session.beginTransaction();
				session.update(userToUpdate); // UPDATE statement
				session.getTransaction().commit();
				client.sendToClient(new UpdateUserEvent(userToUpdate));
			} catch (IOException e) {
				if (session != null) session.getTransaction().rollback();
				e.printStackTrace();
				System.err.println("ERROR: Could not send update confirmation to client or update failed.\n");
			} finally {
				if (session != null) session.close();
			}
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

					// Track user-client association when user logs in
					if (new_state == 1) {
						clientUserMap.put(client, user.getUsername());
						System.out.println("User " + user.getUsername() + " logged in and associated with client");
					} else {
						// User logged out, remove from tracking
						clientUserMap.remove(client);
						System.out.println("User " + user.getUsername() + " logged out and removed from client tracking");
					}
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
				sendToAllClients("update_complainScene_after_change");
			} catch (Exception e) {
				session.getTransaction().rollback();
				e.printStackTrace();
			} finally {
				session.close();
			}


		}
		else if (msg instanceof update_user_values)
		{
			update_user_values update = (update_user_values) msg;

			Session session = App.getSessionFactory().openSession();

			try {
				session.beginTransaction();


				LoginRegCheck user = session.get(LoginRegCheck.class, update.getUser().getId());
				if (user == null) {
					System.out.println("User not found in database (ID = " + update.getUser().getId() + ")");
					session.getTransaction().rollback();
					return;
				}

				String column = update.getColumn();
				String newVal = update.getNew_value();
				System.out.println("User is " + user.getUsername());
				System.out.println("column is " + update.getColumn());
				System.out.println("newVal is " + newVal);



				switch (column) {
					case "username":
						user.setUsername(newVal);
						break;
					case "email":
						user.setEmail(newVal);
						break;
					case "store":
						int new_value = 0;
						if (update.getNew_value().equals("Haifa"))
							new_value = 1;
						else if (update.getNew_value().equals("Krayot"))
							new_value = 2;
						else if (update.getNew_value().equals("Nahariyya"))
							new_value = 3;
						else if (update.getNew_value().equals("Network"))
							new_value = 4;
						user.setStore(new_value);
						break;
					case "phoneNum":
						user.setPhoneNum(newVal);
						break;
					case "fullName":
						user.setFullName(newVal);
						break;
					case "idNum":
						user.setIdNum(newVal);
						break;
				}






				session.update(user);
				session.getTransaction().commit();
				System.out.println("User successfully updated: " + user.getUsername());

			} catch (Exception e) {
				if (session.getTransaction() != null) {
					session.getTransaction().rollback();
				}
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

				// Ensure the user is properly managed in the session
				if (order.getUser() != null) {
					// Get the user from the database to ensure it's managed
					CriteriaBuilder builder = session.getCriteriaBuilder();
					CriteriaQuery<LoginRegCheck> query = builder.createQuery(LoginRegCheck.class);
					Root<LoginRegCheck> root = query.from(LoginRegCheck.class);
					query.select(root).where(builder.equal(root.get("username"), order.getUser().getUsername()));
					LoginRegCheck managedUser = session.createQuery(query).uniqueResult();
					if (managedUser != null) {
						order.setUser(managedUser);
					}
				}

				// Fix: Ensure 'Yearly Subscription' flower is not duplicated
				for (CartItem item : order.getItems()) {
					Flower flower = item.getFlower();
					if (flower != null && "Yearly Subscription".equals(flower.getFlowerName())) {
						// Try to find existing subscription flower
						CriteriaBuilder builder = session.getCriteriaBuilder();
						CriteriaQuery<Flower> query = builder.createQuery(Flower.class);
						Root<Flower> root = query.from(Flower.class);
						query.select(root).where(builder.equal(root.get("flowerName"), "Yearly Subscription"));
						Flower existing = null;
						try {
							existing = session.createQuery(query).setMaxResults(1).uniqueResult();
						} catch (Exception ignored) {}
						if (existing == null) {
							// Create and persist if not found
							existing = new Flower("Yearly Subscription", flower.getFlowerPrice(), "Subscription", "", "Gold", "Subscription");
							session.save(existing);
						}
						item.setFlower(existing);
					}
					// Handle custom bouquet flowers
					else if (flower != null && flower.getFlowerName() != null && flower.getFlowerName().startsWith("custom flower:")) {
						// For custom bouquets, we need to save the flower first
						if (flower.getId() == 0) {
							// This is a new custom flower, save it first
							session.save(flower);
							session.flush(); // Ensure the ID is generated
						}
						// The flower is now managed and can be referenced
					}
					// Handle regular flowers - ensure they are managed
					else if (flower != null && flower.getId() == 0) {
						// This is a transient flower, try to find it in the database first
						CriteriaBuilder builder = session.getCriteriaBuilder();
						CriteriaQuery<Flower> query = builder.createQuery(Flower.class);
						Root<Flower> root = query.from(Flower.class);
						query.select(root).where(builder.equal(root.get("flowerName"), flower.getFlowerName()));
						Flower existing = null;
						try {
							existing = session.createQuery(query).setMaxResults(1).uniqueResult();
						} catch (Exception ignored) {}
						if (existing != null) {
							// Use the existing flower from database
							item.setFlower(existing);
						} else {
							// Save the new flower
							session.save(flower);
							session.flush();
						}
					}
				}

				session.save(order);
				session.getTransaction().commit();

				// Send confirmation back to client
				client.sendToClient("order_success");

				// Notify all clients about the new order
				//sendToAllClients("update_catalog_after_change");

				// Call EmailService after successful order save
				EmailService.sendOrderConfirmationEmail(order);

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
		else if (msgString.startsWith("getOrdersForUser_")) {
			// Get orders for a specific user
			String username = msgString.substring("getOrdersForUser_".length());
			Session session = App.getSessionFactory().openSession();
			try {
				session.beginTransaction();

				// First get the user
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<LoginRegCheck> userQuery = builder.createQuery(LoginRegCheck.class);
				Root<LoginRegCheck> userRoot = userQuery.from(LoginRegCheck.class);
				userQuery.select(userRoot).where(builder.equal(userRoot.get("username"), username));
				LoginRegCheck user = session.createQuery(userQuery).uniqueResult();

				if (user != null) {
					// Get orders for this user with items eagerly loaded
					String hql = "SELECT DISTINCT o FROM Order o " +
							   "LEFT JOIN FETCH o.items i " +
							   "LEFT JOIN FETCH i.flower " +
							   "WHERE o.user = :user";
					List<Order> userOrders = session.createQuery(hql, Order.class)
							.setParameter("user", user)
							.getResultList();

					session.getTransaction().commit();
					client.sendToClient(userOrders);
				} else {
					session.getTransaction().rollback();
					client.sendToClient("user_not_found");
				}
			} catch (Exception e) {
				session.getTransaction().rollback();
				e.printStackTrace();
				try {
					client.sendToClient("error_retrieving_orders");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} finally {
				session.close();
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

				// Also update the user's receive_answer flag to false since they're checking their answers
				CriteriaQuery<LoginRegCheck> userQuery = builder.createQuery(LoginRegCheck.class);
				Root<LoginRegCheck> userRoot = userQuery.from(LoginRegCheck.class);
				userQuery.select(userRoot).where(builder.equal(userRoot.get("username"), username));

				List<LoginRegCheck> users = session.createQuery(userQuery).getResultList();
				if (!users.isEmpty()) {
					LoginRegCheck user = users.get(0);
					user.set_receive_answer(false); // User has seen their answers
					session.update(user);
					System.out.println("Updated user " + username + " receive_answer flag to false");
				}

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
		else if (msgString.startsWith("testEmail")) {
			// Test email configuration
			System.out.println("Testing email configuration...");
			boolean emailTest = EmailService.testEmailConfiguration();
			try {
				if (emailTest) {
					client.sendToClient("Email configuration test successful!");
				} else {
					client.sendToClient("Email configuration test failed. Check server logs for details.");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (msg instanceof OrderCancellationRequest) {
			OrderCancellationRequest request = (OrderCancellationRequest) msg;
			Session session = App.getSessionFactory().openSession();
			
			try {
				session.beginTransaction();
				
				// Get the order
				Order order = session.get(Order.class, request.getOrderId());
				
				if (order == null) {
					OrderCancellationResponse response = new OrderCancellationResponse(
						false, "Order not found", 0.0, "", request.getOrderId());
					client.sendToClient(response);
					return;
				}
				
				// Verify the user owns this order
				if (order.getUser() == null || !order.getUser().getUsername().equals(request.getUsername())) {
					OrderCancellationResponse response = new OrderCancellationResponse(
						false, "You can only cancel your own orders", 0.0, "", request.getOrderId());
					client.sendToClient(response);
					return;
				}
				
				// Check if order can be cancelled
				if ("CANCELLED".equals(order.getStatus())) {
					OrderCancellationResponse response = new OrderCancellationResponse(
						false, "Order is already cancelled", 0.0, "", request.getOrderId());
					client.sendToClient(response);
					return;
				}
				
				if ("DELIVERED".equals(order.getStatus())) {
					OrderCancellationResponse response = new OrderCancellationResponse(
						false, "Cannot cancel delivered orders", 0.0, "", request.getOrderId());
					client.sendToClient(response);
					return;
				}
				
				// Calculate refund before cancellation
				double refundPercentage = order.calculateRefundPercentage();
				String refundPolicy = order.getRefundPolicyDescription();
				
				// Cancel the order
				boolean cancelled = order.cancelOrder(request.getCancellationReason());
				
				if (cancelled) {
					session.update(order);
					session.getTransaction().commit();
					
					// Send cancellation confirmation email
					EmailService.sendOrderCancellationEmail(order);
					
					OrderCancellationResponse response = new OrderCancellationResponse(
						true, "Order cancelled successfully", order.getRefundAmount(), refundPolicy, request.getOrderId());
					client.sendToClient(response);
					
					// Order cancellation doesn't need to notify all clients about catalog changes
					// Removed: sendToAllClients("update_catalog_after_change");
				} else {
					OrderCancellationResponse response = new OrderCancellationResponse(
						false, "Failed to cancel order", 0.0, "", request.getOrderId());
					client.sendToClient(response);
				}
				
			} catch (Exception e) {
				session.getTransaction().rollback();
				e.printStackTrace();
				try {
					OrderCancellationResponse response = new OrderCancellationResponse(
						false, "Error cancelling order: " + e.getMessage(), 0.0, "", request.getOrderId());
					client.sendToClient(response);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} finally {
				session.close();
			}
		}
		else if (msgString.startsWith("get_store_flowers_"))
		{
			System.out.println("=== GET STORE FLOWERS DEBUG ===");
			System.out.println("Received message: " + msgString);
			
			try {
				// Remove the prefix to get the store ID
				String storeIdStr = msgString.substring("get_store_flowers_".length());
				int store_id = Integer.parseInt(storeIdStr);
				
				System.out.println("Getting flowers for store ID: " + store_id);

				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				// Get the store with fresh flower list from database
				Store store = session.get(Store.class, store_id);
				
				if (store != null) {
					// Refresh the store to get the latest flower list
					session.refresh(store);
					
					System.out.println("Found store: " + store.getStoreName());
					System.out.println("Flowers in store: " + store.getFlowersList().size());
					
					// Send the fresh flower list to the client
					update_local_catalog storeEvent = new update_local_catalog(store.getFlowersList(), store_id);
					client.sendToClient(storeEvent);
					System.out.println("Sent fresh flower list to client: " + store.getFlowersList().size() + " flowers");
				} else {
					System.err.println("Store with ID " + store_id + " not found");
				}
				
				session.getTransaction().commit();
				session.close();
			} catch (Exception e) {
				System.err.println("Error processing get_store_flowers message: " + msgString);
				e.printStackTrace();
			}
			System.out.println("=== END GET STORE FLOWERS DEBUG ===");
		}
		else if (msgString.startsWith("get_catalog_"))
		{
			System.out.println("=== GET CATALOG DEBUG ===");
			System.out.println("Received message: " + msgString);
			
			try {
				// Remove the prefix to get the store ID
				String storeIdStr = msgString.substring("get_catalog_".length());
				int store_id = Integer.parseInt(storeIdStr);
				
				System.out.println("Getting catalog for store ID: " + store_id);

				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				// Get the store with fresh flower list from database
				Store store = session.get(Store.class, store_id);
				
				if (store != null) {
					// Refresh the store to get the latest flower list
					session.refresh(store);
					
					System.out.println("Found store: " + store.getStoreName());
					System.out.println("Flowers in store: " + store.getFlowersList().size());
					
					// Send the fresh flower list to the client
					update_local_catalog storeEvent = new update_local_catalog(store.getFlowersList(), store_id);
					client.sendToClient(storeEvent);
					System.out.println("Sent fresh catalog to client: " + store.getFlowersList().size() + " flowers");
				} else {
					System.err.println("Store with ID " + store_id + " not found");
				}
				
				session.getTransaction().commit();
				session.close();
			} catch (Exception e) {
				System.err.println("Error processing get_catalog message: " + msgString);
				e.printStackTrace();
			}
			System.out.println("=== END GET CATALOG DEBUG ===");
		}
		else if (msgString.equals("get_all_flowers"))
		{
			System.out.println("=== GET ALL FLOWERS DEBUG ===");
			System.out.println("Received message: " + msgString);
			
			try {
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				// Get all flowers from the Flowers table
				String hql = "FROM Flower";
				List<Flower> allFlowers = session.createQuery(hql, Flower.class).getResultList();
				
				System.out.println("Retrieved " + allFlowers.size() + " flowers from Flowers table");
				
				// Send all flowers to the client (use store_id = 4 for network view)
				update_local_catalog networkEvent = new update_local_catalog(allFlowers, 4);
				client.sendToClient(networkEvent);
				System.out.println("Sent all flowers to client for network view");
				
				session.getTransaction().commit();
				session.close();
			} catch (Exception e) {
				System.err.println("Error processing get_all_flowers message: " + msgString);
				e.printStackTrace();
			}
			System.out.println("=== END GET ALL FLOWERS DEBUG ===");
		}
		else if (msgString.startsWith("delete_flower_from_network_"))
		{
			System.out.println("=== DELETE FLOWER FROM NETWORK DEBUG ===");
			System.out.println("Received message: " + msgString);
			
			try {
				// Remove the prefix to get the flower ID
				String flowerIdStr = msgString.substring("delete_flower_from_network_".length());
				int flower_id = Integer.parseInt(flowerIdStr);
				
				System.out.println("Deleting flower with ID: " + flower_id + " from Flowers table and all store_flowers entries");

				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				// Find the flower by ID
				Flower flowerToDelete = session.get(Flower.class, flower_id);
				
				if (flowerToDelete != null) {
					System.out.println("Found flower: " + flowerToDelete.getFlowerName() + " (ID: " + flower_id + ")");
					
					// Remove the flower from all stores first
					for (Store store : App.get_stores()) {
						boolean removed = store.getFlowersList().removeIf(f -> f.getId() == flower_id);
						if (removed) {
							session.update(store);
							System.out.println("Removed flower from store: " + store.getStoreName());
						}
					}
					
					// Now delete the flower from the Flowers table
					session.delete(flowerToDelete);
					session.getTransaction().commit();
					
					System.out.println("Flower '" + flowerToDelete.getFlowerName() + "' deleted from Flowers table and all store_flowers entries");
					
					// Refresh stores from database
					App.refreshStores();
					
					// Get updated flower list from Flowers table and send to client
					String hql = "FROM Flower";
					List<Flower> updatedFlowers = session.createQuery(hql, Flower.class).getResultList();
					Add_flower_event networkEvent = new Add_flower_event(updatedFlowers, -1);
					sendToAllClients(networkEvent);
					System.out.println("Sent updated flower list to clients: " + updatedFlowers.size() + " flowers");
				} else {
					System.err.println("Flower with ID " + flower_id + " not found");
					session.getTransaction().rollback();
				}
				session.close();
			} catch (Exception e) {
				System.err.println("Error processing delete_flower_from_network message: " + msgString);
				e.printStackTrace();
			}
			System.out.println("=== END DELETE FLOWER FROM NETWORK DEBUG ===");
		}
		else if (msgString.startsWith("new#price#in#flower_"))
		{
			Session session = App.getSessionFactory().openSession();
			session.beginTransaction();
			String[] parts = msgString.split("_");
			String flower_name=parts[1];
			if(flower_name.equals("all"))
			{
				if(parts.length>2)
				{
					String hql = "FROM Flower";
					List<Flower> flowers = session.createQuery(hql, Flower.class).getResultList();
					session.getTransaction().commit();
					session.close();
					try {
						discount_for_1_flower event = new discount_for_1_flower(flowers,-2,flower_name);
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
			
			// Send automatic update to all clients
			try {
				String hql = "FROM Flower";
				Session updateSession = App.getSessionFactory().openSession();
				updateSession.beginTransaction();
				List<Flower> updatedFlowers = updateSession.createQuery(hql, Flower.class).getResultList();
				updateSession.getTransaction().commit();
				updateSession.close();
				
				Add_flower_event addEvent = new Add_flower_event(updatedFlowers, 4);
				sendToAllClients(addEvent);
				System.out.println("Sent automatic update to all clients after adding new flower");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			sendToAllClients("The network manager has added a flower.");

		}
		else if (msgString.startsWith("get_all_flowers_for_store_selection"))
		{
			System.out.println("=== GET ALL FLOWERS FOR STORE SELECTION DEBUG ===");
			System.out.println("Received message: " + msgString);
			
			try {
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				// Get all flowers from the Flowers table
				String hql = "FROM Flower";
				List<Flower> allFlowers = session.createQuery(hql, Flower.class).getResultList();
				
				System.out.println("Retrieved " + allFlowers.size() + " flowers for store selection");
				
				// Send all flowers to the client for store selection
				update_local_catalog selectionEvent = new update_local_catalog(allFlowers, -1);
				client.sendToClient(selectionEvent);
				System.out.println("Sent all flowers to client for store selection");
				
				session.getTransaction().commit();
				session.close();
			} catch (Exception e) {
				System.err.println("Error processing get_all_flowers_for_store_selection message: " + msgString);
				e.printStackTrace();
			}
			System.out.println("=== END GET ALL FLOWERS FOR STORE SELECTION DEBUG ===");
		}
		else if (msgString.startsWith("add_flower_to_store_"))
		{
			System.out.println("=== ADD FLOWER TO STORE DEBUG ===");
			System.out.println("Received message: " + msgString);
			try {
				// Parse both flower ID and store ID
				String[] parts = msgString.substring("add_flower_to_store_".length()).split("_");
				int flower_id = Integer.parseInt(parts[0]);
				int store_id = Integer.parseInt(parts[1]);
				System.out.println("Adding flower with ID: " + flower_id + " to store ID: " + store_id);
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();
				// Find the flower by ID
				Flower flowerToAdd = session.get(Flower.class, flower_id);
				Store targetStore = session.get(Store.class, store_id);
				if (flowerToAdd != null && targetStore != null) {
					// Check if flower is already in this store
					boolean alreadyInStore = targetStore.getFlowersList().stream()
						.anyMatch(f -> f.getId() == flower_id);
					if (!alreadyInStore) {
						targetStore.getFlowersList().add(flowerToAdd);
						session.update(targetStore);
						session.getTransaction().commit();
						System.out.println("Flower '" + flowerToAdd.getFlowerName() + "' added to store '" + targetStore.getStoreName() + "'");
						// Refresh stores from database
						App.refreshStores();
						// Send confirmation to client
						client.sendToClient("Flower '" + flowerToAdd.getFlowerName() + "' added to store successfully");
						// Send updated store flower list to all clients
						update_local_catalog storeEvent = new update_local_catalog(targetStore.getFlowersList(), targetStore.getId());
						sendToAllClients(storeEvent);
						System.out.println("Sent updated flower list to clients: " + targetStore.getFlowersList().size() + " flowers");
					} else {
						System.err.println("Flower is already in the store");
						session.getTransaction().rollback();
						client.sendToClient("Flower is already in this store");
					}
				} else {
					System.err.println("Flower or store not found");
					session.getTransaction().rollback();
					client.sendToClient("Flower or store not found");
				}
				session.close();
			} catch (Exception e) {
				System.err.println("Error processing add_flower_to_store message: " + msgString);
				e.printStackTrace();
				client.sendToClient("Error adding flower to store: " + e.getMessage());
			}
			System.out.println("=== END ADD FLOWER TO STORE DEBUG ===");
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
	
	public void sendToAllClients(Object message) {
		System.out.println("=== SEND TO ALL CLIENTS CALLED ===");
		System.out.println("Message type: " + message.getClass().getSimpleName());
		if (message instanceof Add_flower_event) {
			Add_flower_event event = (Add_flower_event) message;
			System.out.println("Add_flower_event details: catalog_type=" + event.get_catalog_type() + ", flowers_count=" + (event.get_flowers() != null ? event.get_flowers().size() : "null"));
		}
		System.out.println("Number of subscribed clients: " + SubscribersList.size());
		try {
			for (SubscribedClient subscribedClient : SubscribersList) {
				subscribedClient.getClient().sendToClient(message);
			}
			System.out.println("Message sent to all " + SubscribersList.size() + " clients");
		} catch (IOException e1) {
			System.err.println("Error sending message to clients: " + e1.getMessage());
			e1.printStackTrace();
		}
		System.out.println("=== END SEND TO ALL CLIENTS ===");
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

	@Override
	protected void clientDisconnected(ConnectionToClient client) {
		// Handle unexpected client disconnections
		String username = clientUserMap.get(client);

		if (username != null) {
			System.out.println("Client unexpectedly disconnected, logging out user: " + username);

			// Log out the user in the database
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
					userInDb.setIsLogin(0); // Set to logged out
					session.update(userInDb);
					System.out.println("User " + username + " logged out due to unexpected client disconnect");
				}

				session.getTransaction().commit();
			} catch (Exception e) {
				if (session != null) session.getTransaction().rollback();
				e.printStackTrace();
			} finally {
				if (session != null) session.close();
			}

			// Remove from tracking
			clientUserMap.remove(client);
		}

		// Remove from subscribers list
		if(!SubscribersList.isEmpty()){
			for(SubscribedClient subscribedClient: SubscribersList){
				if(subscribedClient.getClient().equals(client)){
					SubscribersList.remove(subscribedClient);
					break;
				}
			}
		}

		super.clientDisconnected(client);
	}

}




