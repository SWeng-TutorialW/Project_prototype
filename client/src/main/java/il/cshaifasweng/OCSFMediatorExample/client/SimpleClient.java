package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg.getClass().equals(Warning.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		}
		else if(msg.getClass().equals(CatalogUpdateEvent.class)){
			EventBus.getDefault().post(msg); // post the catalog update to UI
		}
		else{
			String message = msg.toString();
			System.out.println(message);
		}
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("6.tcp.eu.ngrok.io", 17741);
		}
		return client;
	}

}
