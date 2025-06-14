package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.Subscribe;

/**
 * Sample Skeleton for 'select_account_type.fxml' Controller Class
 */

public class SelectAccTypeController {

    @FXML // fx:id="accountTypeChoice"
    private ChoiceBox<String> accountTypeChoice; // Value injected by FXMLLoader

    @FXML // fx:id="chooseAccLbl"
    private Label chooseAccLbl; // Value injected by FXMLLoader

    @FXML // fx:id="continueBtn"
    private Button continueBtn; // Value injected by FXMLLoader

    @FXML // fx:id="errMsgLbl"
    private Label errMsgLbl; // Value injected by FXMLLoader


    @FXML
    void continueToRegister(MouseEvent event) {

        String selected = accountTypeChoice.getValue();

        if (selected == null) {
            errMsgLbl.setText("Please choose an account type");
            errMsgLbl.setTextFill(Paint.valueOf("red"));
            errMsgLbl.setVisible(true);
            return;
        }

        int selectedAccountType = switch (selected) {
            case "Store Account" -> 0;
            case "Chain Of Stores Account" -> 1;
            case "Yearly Subscription Account" -> 2;
            default -> -1;
        };

        SimpleClient.setSelectedAccType(selectedAccountType);
            Platform.runLater(() -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration_screen.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Register");
                    stage.setScene(new Scene(root));
                    stage.show();
                    ((Stage) continueBtn.getScene().getWindow()).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

    }

    @FXML
    void initialize() {
        accountTypeChoice.getItems().addAll("Store Account", "Chain Of Stores Account", "Yearly Subscription Account");
    }
}
