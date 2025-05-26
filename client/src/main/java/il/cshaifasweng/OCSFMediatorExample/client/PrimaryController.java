package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class PrimaryController {
	@FXML
	private Button Catalog;

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
		try {
			SimpleClient.getClient().sendToServer("add client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void show_cata(ActionEvent actionEvent) {
		try {
			if (SimpleClient.getClient().isConnected())
				System.out.println("show_cata");
				SimpleClient.getClient().sendToServer("getCatalogTable");
		} catch (Exception e) {
			e.printStackTrace();

		}

	}
	@Subscribe
	public void handleCatalogUpdate(CatalogUpdateEvent event) {
		System.out.println("Loading catalog_win.fxml...");
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
}
