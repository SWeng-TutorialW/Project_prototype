package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.client;

public class PrimaryController {
	@FXML
	private Button Catalog;
	@FXML
	private TextField IP;

	@FXML
	private Label ip_label;

	@FXML
	private Label port;

	@FXML
	private TextField port_box;



	@FXML
	void sendWarning(ActionEvent event) {
		try {
			SimpleClient.getClient().sendToServer("#warning");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void initialize() {
		

	}



	public void show_connect(ActionEvent actionEvent) {
		SimpleClient.ip = IP.getText();
		SimpleClient.port = Integer.parseInt(port_box.getText());
		client = SimpleClient.getClient();


		try {
			SimpleClient.getClient().openConnection();
			SimpleClient.getClient().sendToServer("add client");
		} catch (IOException e) {
			client = null;
			Warning warning = new Warning("IP or Port is incorrect. Could not connect.");
			EventBus.getDefault().post(new WarningEvent(warning));
			e.printStackTrace();
			return;
		}
		move_to_connect_scene();
		if (SimpleClient.getClient().isConnected()) {
			System.out.println("move_to_connect_scene");
			move_to_connect_scene();
		}

	}


	public void move_to_connect_scene()
	{
		Platform.runLater(() -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("connect_scene.fxml"));
				Parent root = loader.load();

				connect_scene_Con controller = loader.getController();
				App.getScene().setRoot(root);

			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
