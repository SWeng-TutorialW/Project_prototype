package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.change_user_login;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.xml.catalog.Catalog;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private SimpleClient client;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        EventBus.getDefault().register(this);
        primaryStage = stage; // ⬅️ שומר את ה־Stage
        scene = new Scene(loadFXML("primary"), 562, 386);
        stage.setScene(scene);
        stage.show();
    }
    public static Stage getStage() {
        return primaryStage;
    }


    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Scene getScene() {
        return scene;
    }

    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
    	EventBus.getDefault().unregister(this);
        SimpleClient.getClient().sendToServer("remove client");
        SimpleClient.getClient().closeConnection();
		super.stop();
	}
    
    @Subscribe
    public void onWarningEvent(WarningEvent event) {
    	Platform.runLater(() -> {
    		Alert alert = new Alert(AlertType.WARNING,
        			String.format("Message: %s\nTimestamp: %s\n",
        					event.getWarning().getMessage(),
        					event.getWarning().getTime().toString())
        	);
        	alert.show();
    	});
    	
    }

    @Subscribe
    public void onSuccessEvent(SuccessEvent event) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.NONE,
                    String.format("%s\nTimestamp: %s\n",
                            event.getSuccess().getMessage(),
                            event.getSuccess().getTime().toString())
            );
            alert.setTitle("Success");
            alert.setHeaderText("Success");

            // Set custom green checkmark icon
            javafx.scene.image.ImageView checkIcon = new javafx.scene.image.ImageView(
                getClass().getResource("/images/green_check.png").toExternalForm()
            );
            checkIcon.setFitHeight(48);
            checkIcon.setFitWidth(48);
            alert.setGraphic(checkIcon);

            alert.getButtonTypes().setAll(ButtonType.OK);

            alert.show();
        });
    }

	public static void main(String[] args) {
        launch();
    }

}