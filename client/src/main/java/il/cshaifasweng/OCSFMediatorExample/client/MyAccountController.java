/**
 * Sample Skeleton for 'my_account.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MyAccountController {

    @FXML // fx:id="accountTypeEmptyLbl"
    private Label accountTypeEmptyLbl; // Value injected by FXMLLoader

    @FXML // fx:id="accountTypeLbl"
    private Label accountTypeLbl; // Value injected by FXMLLoader

    @FXML // fx:id="accountTypeLbl1"
    private Label accountTypeLbl1; // Value injected by FXMLLoader

    @FXML // fx:id="emailEmptyLbl"
    private Label emailEmptyLbl; // Value injected by FXMLLoader

    @FXML // fx:id="emailLbl"
    private Label emailLbl; // Value injected by FXMLLoader

    @FXML // fx:id="emailLbl1"
    private Label emailLbl1; // Value injected by FXMLLoader

    @FXML // fx:id="employeePositionLbl"
    private Label employeePositionLbl; // Value injected by FXMLLoader

    @FXML // fx:id="employeePositionLbl1"
    private Label employeePositionLbl1; // Value injected by FXMLLoader

    @FXML // fx:id="employeeUsernameLbl"
    private Label employeeUsernameLbl; // Value injected by FXMLLoader

    @FXML // fx:id="employeeUsernameLbl1"
    private Label employeeUsernameLbl1; // Value injected by FXMLLoader

    @FXML // fx:id="myAccUsers"
    private AnchorPane myAccUsers; // Value injected by FXMLLoader

    @FXML // fx:id="myAccUsers1"
    private AnchorPane myAccUsers1; // Value injected by FXMLLoader

    @FXML // fx:id="myAccountLbl"
    private Label myAccountLbl; // Value injected by FXMLLoader

    @FXML // fx:id="myAccountLbl1"
    private Label myAccountLbl1; // Value injected by FXMLLoader

    @FXML // fx:id="my_account_data"
    private AnchorPane my_account_data; // Value injected by FXMLLoader

    @FXML // fx:id="subscribeBtn"
    private Button subscribeBtn; // Value injected by FXMLLoader

    @FXML // fx:id="usernameEmptyLbl"
    private Label usernameEmptyLbl; // Value injected by FXMLLoader

    @FXML // fx:id="usernameLbl"
    private Label usernameLbl; // Value injected by FXMLLoader

    @FXML // fx:id="usernameLbl1"
    private Label usernameLbl1; // Value injected by FXMLLoader

    @FXML // fx:id="workerEmailLbl"
    private Label workerEmailLbl; // Value injected by FXMLLoader

    @FXML // fx:id="workerEmailLbl1"
    private Label workerEmailLbl1; // Value injected by FXMLLoader

    @FXML
    private Button myOrdersButton;

    private LoginRegCheck currentUser;


    @FXML
    void onSubscribe(ActionEvent event) {

    }

    public void setCurrentUser(LoginRegCheck user) {
        this.currentUser = user;
    }
    
    @FXML
    private void handleMyOrdersButton() {
        if (currentUser == null) {
            System.out.println("No user logged in");
            return;

        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("orders_history.fxml"));
            Parent root = loader.load();
            OrdersHistoryController ordersController = loader.getController();
            ordersController.setCurrentUser(currentUser);
            ordersController.loadUserOrders();

            Stage stage = new Stage();
            stage.setTitle("My Order History");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the my account window
            ((Stage) myOrdersButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}