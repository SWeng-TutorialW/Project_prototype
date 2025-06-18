/**
 * Sample Skeleton for 'my_account.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class MyAccountController {

    @FXML // fx:id="accountTypeEmptyLbl"
    private Label accountTypeEmptyLbl; // Value injected by FXMLLoader

    @FXML // fx:id="accountTypeLbl"
    private Label accountTypeLbl; // Value injected by FXMLLoader

    @FXML // fx:id="emailEmptyLbl"
    private Label emailEmptyLbl; // Value injected by FXMLLoader

    @FXML // fx:id="emailLbl"
    private Label emailLbl; // Value injected by FXMLLoader

    @FXML // fx:id="myAccountLbl"
    private Label myAccountLbl; // Value injected by FXMLLoader

    @FXML // fx:id="my_account_data"
    private AnchorPane my_account_data; // Value injected by FXMLLoader

    @FXML // fx:id="subscribeBtn"
    private Button subscribeBtn; // Value injected by FXMLLoader

    @FXML // fx:id="usernameEmptyLbl"
    private Label usernameEmptyLbl; // Value injected by FXMLLoader

    @FXML // fx:id="usernameLbl"
    private Label usernameLbl; // Value injected by FXMLLoader

    @FXML
    void onSubscribe(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }
}
