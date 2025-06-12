package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

public class SimpleClient extends AbstractClient {
	
	static SimpleClient client = null;
	public static int port = 3000;
	public static String ip = "localhost";
	public static boolean loggedIn = false; // to check if the user is logged in or not
	public static boolean isGuest = false;
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


		else if(msgString.startsWith("update_catalog_after_change"))
		{
			try {
				SimpleClient.getClient().sendToServer("price_changed");
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


}
