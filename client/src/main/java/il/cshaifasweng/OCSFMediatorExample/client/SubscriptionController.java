
/**
 * Sample Skeleton for 'subscription_reg.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class SubscriptionController {

    @FXML // fx:id="creditCardLbl"
    private Label creditCardLbl; // Value injected by FXMLLoader

    @FXML // fx:id="creditCardTxtB"
    private TextField creditCardTxtB; // Value injected by FXMLLoader

    @FXML // fx:id="fullNameLbl"
    private Label fullNameLbl; // Value injected by FXMLLoader

    @FXML // fx:id="idNumLbl"
    private Label idNumLbl; // Value injected by FXMLLoader

    @FXML // fx:id="idNumberTxtB"
    private TextField idNumberTxtB; // Value injected by FXMLLoader

    @FXML // fx:id="passswordTxtB"
    private TextField passswordTxtB; // Value injected by FXMLLoader

    @FXML // fx:id="passwordLbl"
    private Label passwordLbl; // Value injected by FXMLLoader

    @FXML // fx:id="subErrLbl"
    private Label subErrLbl; // Value injected by FXMLLoader

    @FXML // fx:id="subWin"
    private AnchorPane subWin; // Value injected by FXMLLoader

    @FXML // fx:id="submitSubBtn"
    private Button submitSubBtn; // Value injected by FXMLLoader

    @FXML // fx:id="userNameTxtB"
    private TextField userNameTxtB; // Value injected by FXMLLoader

    private RegistrationController registrationController;

    @FXML
    void submitSub(MouseEvent event) {
        String id = idNumberTxtB.getText();
        String credit = creditCardTxtB.getText();

        if (id.length() >= 5 && credit.length() >= 8) {
            try {
                subErrLbl.setVisible(false);
                Platform.runLater(() -> {
                    registrationController.sendSubscriptionDetails(id, credit);
                    Stage stage = (Stage) submitSubBtn.getScene().getWindow();
                    stage.close();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            subErrLbl.setText("Invalid input");
            subErrLbl.setVisible(true);
        }
    }

    @FXML
    void initialize() {

    }

    public void setRegistrationController(RegistrationController controller) {
        this.registrationController = controller;
    }

}
