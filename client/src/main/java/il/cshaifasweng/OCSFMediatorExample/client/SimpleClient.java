package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.scene.control.Alert;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.util.List;
import java.util.ArrayList;

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
		System.out.println("=== SIMPLE CLIENT: MESSAGE RECEIVED ===");
		System.out.println("Message type: " + msg.getClass().getSimpleName());
		System.out.println("Message content: " + msg.toString());
		
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
			System.out.println("=== SIMPLE CLIENT: ADD FLOWER EVENT RECEIVED ===");
			Add_flower_event event = (Add_flower_event) msg;
			System.out.println("Event catalog type: " + event.get_catalog_type());
			System.out.println("Event flowers count: " + (event.get_flowers() != null ? event.get_flowers().size() : "null"));
			if (event.get_catalog_type() == -1) {
				System.out.println("*** NETWORK DELETE EVENT DETECTED ***");
			}
			EventBus.getDefault().post(msg); // post the catalog update to UI
			System.out.println("Event posted to EventBus");
		}
		else if (msg instanceof List<?>) { // send users to reg scene
			List<?> list = (List<?>) msg;
			if (!list.isEmpty()) {
				Object firstElement = list.get(0);
				if (firstElement instanceof LoginRegCheck) {
					// This is a list of users
					@SuppressWarnings("unchecked")
					List<LoginRegCheck> users = (List<LoginRegCheck>) list;
					EventBus.getDefault().post(users);
				} else if (firstElement instanceof Order) {
					// This is a list of orders
					@SuppressWarnings("unchecked")
					List<Order> orders = (List<Order>) list;
					EventBus.getDefault().post(orders);
				} else if (firstElement instanceof Complain) {
					// This is a list of complaints
					@SuppressWarnings("unchecked")
					List<Complain> complaints = (List<Complain>) list;
					EventBus.getDefault().post(complaints);
				} else {
					// Unknown list type, post as generic list
					EventBus.getDefault().post(list);
				}
			} else {
				// Empty list, post as is
				EventBus.getDefault().post(list);
			}
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
			EventBus.getDefault().post(msg);
			EventBus.getDefault().post("#userUpdateSuccess");
		}
		else if(msg.getClass().equals(GetUserDetails.class)){
			EventBus.getDefault().post(msg); // This will send the user details to the UI
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
		else if(msgString.equals("order_success")) {
			EventBus.getDefault().post("order_success");
		}
		// Handle flower addition responses
		else if(msgString.startsWith("Flower '") && msgString.contains("' added to store successfully")) {
			EventBus.getDefault().post(msgString);
		}
		else if(msgString.startsWith("Flower is already in this store")) {
			EventBus.getDefault().post(msgString);
		}
		else if(msgString.startsWith("Flower or store not found")) {
			EventBus.getDefault().post(msgString);
		}
		else if(msgString.startsWith("Error adding flower to store:")) {
			EventBus.getDefault().post(msgString);
		}
		
		System.out.println("=== END SIMPLE CLIENT: MESSAGE PROCESSED ===");
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
