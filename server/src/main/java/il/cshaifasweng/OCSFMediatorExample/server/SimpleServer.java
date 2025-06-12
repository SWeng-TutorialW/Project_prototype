package il.cshaifasweng.OCSFMediatorExample.server;


import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import java.io.IOException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


import java.util.ArrayList;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
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
		else if (msgString.startsWith("getCatalogTable")) {
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

				CatalogUpdateEvent event = new CatalogUpdateEvent(flowerList);
				client.sendToClient(event);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if(msgString.startsWith("number#flower#to#update#")){
			// we got a Flower from the client, it means we want to update this flower into our DB table.
			try {
				String[] parts = msgString.split("_");
				int num = Integer.parseInt(parts[1]);
				double newPrice = Double.parseDouble(parts[2]);
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();
				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Flower> query = builder.createQuery(Flower.class);
				query.from(Flower.class);

				List<Flower> flowerList = session.createQuery(query).getResultList();

				Flower flowerToUpdate = flowerList.get(num);
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
		else if(msgString.startsWith("get_flowers_high_to_low"))
		{
			List<Flower> flowers = getFlowersOrdered("desc");
			try {
				CatalogUpdateEvent event = new CatalogUpdateEvent(flowers);
				client.sendToClient(event);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		else if(msgString.startsWith("get_flowers_low_to_high"))
		{
			List<Flower> flowers = getFlowersOrdered("asc");
			try {
				CatalogUpdateEvent event = new CatalogUpdateEvent(flowers);
				client.sendToClient(event);
			} catch (IOException ex) {
				ex.printStackTrace();
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
			sendToAllClients("update_catalog_after_change");

		}
		else if (msg.getClass().equals(LoginRegCheck.class)) {
			int isLogin = ((LoginRegCheck) msg).getIsLogin();
			// check for user is already exists :

			String username = ((LoginRegCheck) msg).getUsername();
			String password = ((LoginRegCheck) msg).getPassword();

			System.out.println("Username: " + username);
			System.out.println("Password: " + password);

			switch (isLogin) {
                case 1: // login

                    Session session = App.getSessionFactory().openSession();
                    session.beginTransaction();

                    List<LoginRegCheck> resultLogin = session.createQuery(
                                    "FROM LoginRegCheck lrc WHERE lrc.username = :username AND lrc.password = :password",
                                    LoginRegCheck.class
                            )
                            .setParameter("username", username)
                            .setParameter("password", password)
                            .getResultList();
                    session.getTransaction().commit();

                    if (resultLogin.isEmpty()) {
                        // user doesn't exist or password is wrong
                        try {
                            System.out.println("Login failed for user: " + username);
                            client.sendToClient("#login_failed");
                            session.close();
                        } catch (IOException e) {
                            System.out.println("User login failed ERR");
                            e.printStackTrace();
                        }
                        return;
                    }
                    try {
                        System.out.println("User Logged-in successfully: " + username);
                        session.close();
                        client.sendToClient("#login/reg_ok");

                    } catch (IOException e) {
                        System.out.println("User login ok ERR");
                        e.printStackTrace();
                    }
                    break;
                case 0: // registration
                    System.out.println("WE ARE IN REG");
                    Session session2 = null;
                    try {
                        // begin checking if this user already exists
                        session2 = App.getSessionFactory().openSession();
                        session2.beginTransaction();
                        List<LoginRegCheck> result = session2.createQuery("FROM LoginRegCheck lrc WHERE lrc.username = :username", LoginRegCheck.class)
                                .setParameter("username", username)
                                .getResultList();
                        session2.getTransaction().commit();

                        if (!result.isEmpty()) {
                            // user already exists
                            client.sendToClient("#user_exists");
                            System.out.println("User already exists: " + username);
                            session2.close();
                            return;
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        session2.close();
                    }

					Session insertSession=App.getSessionFactory().openSession();
					try {
                        // after user doesn't exist, we can insert into DB
                        insertSession = App.getSessionFactory().openSession();
                        insertSession.beginTransaction();
                        insertSession.save(msg); // This is the INSERT INTO statement
                        insertSession.getTransaction().commit();
                        insertSession.close();

                        client.sendToClient("#login/reg_ok");
                        System.out.println("User registered successfully: " + username);
                    } catch (IOException e) {
                        System.out.println("User register/login ERR");
                        insertSession.getTransaction().rollback();
                        e.printStackTrace();
                    } finally {
                        insertSession.close();
                    }
                    break;
            }

		}
		else if (msg instanceof Order) {
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
	private List<Flower> getFlowersOrdered(String direction) {
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




