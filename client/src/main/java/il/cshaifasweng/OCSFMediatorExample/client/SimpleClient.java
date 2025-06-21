package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.scene.control.Alert;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.util.List;

public class SimpleClient extends AbstractClient {
	
	static SimpleClient client = null;
	public static int port = 3000;
	public static String ip = "localhost";
	public static boolean isGuest = false;
	public static int selectedAccType = -1;
	private static LoginRegCheck currentUser = null; // Store the current logged-in user
	// With each new insatnce of our application, there will be a new SimpleClient
	// so we don't have to worry about multiple instances of the client...

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
		else if(msg.getClass().equals(update_local_catalog.class)){
			EventBus.getDefault().post(msg); // post the catalog update to UI
		}
		else if(msg.getClass().equals(discount_for_1_flower.class)){
			EventBus.getDefault().post(msg); // post the catalog update to UI
		}
		else if(msg.getClass().equals(Complain.class)){
			System.out.println("I send the complain event");
			EventBus.getDefault().post(msg); // post the catalog update to UI
		}
		else if(msg.getClass().equals(CustomerOrdersResponse.class)){
			EventBus.getDefault().post(msg); // post the customer orders response to UI
		}
		else if(msg.getClass().equals(OrderCancellationResponse.class)){
			EventBus.getDefault().post(msg); // post the order cancellation response to UI
		}
		else if(msgString.equals("error_fetching_orders")){
			EventBus.getDefault().post(msgString); // post the error message to UI
		}

		else if(msgString.startsWith("The network manager has added a flower."))
		{
			try {
				SimpleClient.getClient().sendToServer("update_catalog_after_manager_add_flower");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else if(msgString.startsWith("new#price#in#flower_"))
		{
			try {
				SimpleClient.getClient().sendToServer(msgString);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else if(msgString.startsWith("#registerSuccess") || msgString.startsWith("#registerFailed")|| msgString.startsWith("#loginSuccess")||msgString.startsWith("#loginFailed")) {EventBus.getDefault().post(msgString);}
		else if(msgString.startsWith("The network manager has deleted flower."))
		{
			try {
				SimpleClient.getClient().sendToServer("update_catalog_after_manager_delete_flower");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		else if(msg.getClass().equals(UpdateUserEvent.class)){ // Update user event has been received
			SimpleClient.currentUser = ((UpdateUserEvent) msg).getUpdatedUser();
			System.out.println("Client: New Details for: " + SimpleClient.currentUser.getUsername());
			EventBus.getDefault().post(msg);
			EventBus.getDefault().post("#userUpdateSuccess");
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
		else if(msgString.startsWith("Email configuration test successful!")) {
			EventBus.getDefault().post("Email test successful!");
			System.out.println("Email configuration test successful!");
		}
		else if(msgString.startsWith("Email configuration test failed")) {
			EventBus.getDefault().post("Email test failed!");
			System.out.println("Email configuration test failed!");
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

	// Getter and setter for current user
	public static LoginRegCheck getCurrentUser() {
		return currentUser;
	}

	public static void setCurrentUser(LoginRegCheck user) {
		currentUser = user;
		System.out.println("SimpleClient: Current user set to: " + (user != null ? user.getUsername() : "null"));
	}

}
