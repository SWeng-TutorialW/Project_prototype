package il.cshaifasweng.OCSFMediatorExample.server;


import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
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
			System.out.println("1");
			try {
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				CriteriaBuilder builder = session.getCriteriaBuilder();
				CriteriaQuery<Flower> query = builder.createQuery(Flower.class);
				query.from(Flower.class);

				List<Flower> flowerList = session.createQuery(query).getResultList();

				// 🟢 הדפסות בדיקה:
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
		else if(msg.getClass().equals(Flower.class)){
			// we got a Flower from the client, it means we want to update this flower into our DB table.
			try {
				Session session = App.getSessionFactory().openSession();
				session.beginTransaction();

				Flower flowerToUpdate = (Flower) msg;
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
}




