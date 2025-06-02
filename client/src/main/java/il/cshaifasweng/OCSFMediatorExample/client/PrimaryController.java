package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	private ComboBox<String> combo;

	 private String type;

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
		EventBus.getDefault().register(this);
		combo.getItems().addAll("client", "employee");
		combo.setValue("type");

	}
	@FXML
	void combo_select(ActionEvent event)
	{
		type = combo.getValue();
	}

	public void show_cata(ActionEvent actionEvent) {
		SimpleClient.ip = IP.getText();
		SimpleClient.port = Integer.parseInt(port_box.getText());
		client = SimpleClient.getClient();
		if (type == null)
		{
			Warning warning = new Warning("Please choose type,client or employee");
			EventBus.getDefault().post(new WarningEvent(warning));
		}

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
		try {
			if (SimpleClient.getClient().isConnected())
				System.out.println("show_cata");
				SimpleClient.getClient().sendToServer("getCatalogTable");
		} catch (Exception e) {
			e.printStackTrace();

		}

	}
	@Subscribe
	public void handleCatalogUpdate(CatalogUpdateEvent event)
	{
		System.out.println("Loading catalog_win.fxml...");
		if (type.startsWith("client"))
		{
			Platform.runLater(() -> {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_win.fxml"));
					Parent root = loader.load();

					CatalogController controller = loader.getController();
					controller.setCatalogData(event.getUpdatedItems());


					App.getScene().setRoot(root);

				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
		if (type.startsWith("employee"))
		{
			Platform.runLater(() -> {
				try {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_employee.fxml"));
					Parent root = loader.load();

					CatalogController_employee controller = loader.getController();
					controller.setCatalogData(event.getUpdatedItems());


					App.getScene().setRoot(root);

				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}

	}
}
