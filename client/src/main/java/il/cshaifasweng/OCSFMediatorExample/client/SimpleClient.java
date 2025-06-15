package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.util.List;

public class SimpleClient extends AbstractClient {
	
	static SimpleClient client = null;
	public static int port = 3000;
	public static String ip = "localhost";
	public static boolean loggedIn = false; // to check if the user is logged in or not
	public static boolean isGuest = false;
	public static int selectedAccType = -1;
	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		String msgString = msg.toString();
		if (msg.getClass().equals(Warning.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		}

		else if(msg.getClass().equals(CatalogUpdateEvent.class)){
			EventBus.getDefault().post(msg); // post the catalog update to UI
		}
		else if(msg.getClass().equals(catalog_sort_event.class)){
			EventBus.getDefault().post(msg); // post the catalog update to UI
		}
		else if(msg.getClass().equals(Add_flower_event.class)){
			EventBus.getDefault().post(msg); // post the catalog update to UI
		}
		else if (msg instanceof List<?>) { // send users to reg scene
			EventBus.getDefault().post(msg);
		}
		else if(msg.getClass().equals(ComplainUpdateEvent.class)){
			EventBus.getDefault().post(msg); // post the catalog update to UI
		}
		else if(msg.getClass().equals(Complain.class)){
			System.out.println("I send the complain event");
			EventBus.getDefault().post(msg); // post the catalog update to UI
		}

		else if(msgString.startsWith("The network manager has added a flower."))
		{
			try {
				SimpleClient.getClient().sendToServer("update_catalog_after_manager_add_flower");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else if(msgString.startsWith("user_"))
		{
			String[] parts = msgString.split("_");
			try {
				SimpleClient.getClient().sendToServer("need#to#change#user#localy_"+parts[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else if(msgString.startsWith("update_complainScene_after_change"))
		{
			try {
				SimpleClient.getClient().sendToServer("getComplaints");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else if(msgString.startsWith("#user_exists") || msgString.startsWith("#login_failed") || msgString.startsWith("#login/reg_ok")){
			EventBus.getDefault().post((String)msg);
			System.out.println("I GOT THE MESSAGE");
		}


	}

	public static SimpleClient getClient() {
		if (client == null) {
			System.out.println(ip);
			System.out.println(port);
			client = new SimpleClient(ip, port);
		}
		return client;
	}
	public static void setSelectedAccType(int type) {
		selectedAccType = type;
	}
	public static int getSelectedAccType() {
		return selectedAccType;
	}


}
