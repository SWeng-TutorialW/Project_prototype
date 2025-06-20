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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Optional;

public class MyAccountController {

    @FXML // fx:id="accountTypeEmptyLbl"
    private Label accountTypeEmptyLbl; // Value injected by FXMLLoader

    @FXML // fx:id="accountTypeLbl"
    private Label accountTypeLbl; // Value injected by FXMLLoader

    @FXML // fx:id="emailEmptyLbl"
    private Label emailEmptyLbl; // Value injected by FXMLLoader

    @FXML // fx:id="emailLbl"
    private Label emailLbl; // Value injected by FXMLLoader

    @FXML // fx:id="myAccUsers"
    private AnchorPane myAccUsers; // Value injected by FXMLLoader

    @FXML // fx:id="myAccountLbl"
    private Label myAccountLbl; // Value injected by FXMLLoader

    @FXML // fx:id="myOrdersButton"
    private Button myOrdersButton; // Value injected by FXMLLoader

    @FXML // fx:id="my_account_data"
    private AnchorPane my_account_data; // Value injected by FXMLLoader

    @FXML // fx:id="subscribeBtn"
    private Button subscribeBtn; // Value injected by FXMLLoader

    @FXML // fx:id="usernameEmptyLbl"
    private Label usernameEmptyLbl; // Value injected by FXMLLoader

    @FXML // fx:id="usernameLbl"
    private Label usernameLbl; // Value injected by FXMLLoader

    private LoginRegCheck currentUser;



        @FXML
        void onSubscribe(ActionEvent event) {
            if (currentUser == null) {
                System.out.println("No user logged in");
                return;
            }

            try {
                //check if first need to upgrade to Network account
                if (currentUser.getStore() >= 1 && currentUser.getStore() <= 3 && !currentUser.is_yearly_subscription()) {
                    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmation.setTitle("Upgrade Required");
                    confirmation.setHeaderText("Store Subscription Detected");
                    confirmation.setContentText("To subscribe to the yearly plan, you must first upgrade to a network subscription.\n\nDo you want to proceed?");

                    ButtonType yesButton = new ButtonType("Yes");
                    ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                    confirmation.getButtonTypes().setAll(yesButton, noButton);

                    Optional<ButtonType> result = confirmation.showAndWait();

                    if (result.isPresent() && result.get() == yesButton) {

                        currentUser.setStore(4);

                        Alert info = new Alert(Alert.AlertType.INFORMATION);
                        info.setTitle("Upgraded");
                        info.setHeaderText(null);
                        info.setContentText("Your account has been upgraded to a Network account.\nYou may now proceed to register for the yearly subscription.");
                        info.showAndWait();

                    } else {
                        return;
                    }
                }

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration_screen.fxml"));

                RegistrationController regController = new RegistrationController();
                regController.setUser(currentUser);
                regController.setUpgrade(true);

                fxmlLoader.setController(regController);
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Subscription");
                stage.setScene(new Scene(root));
                stage.show();

                ((Stage) subscribeBtn.getScene().getWindow()).close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    public void loadUserInfo() {
        if (currentUser == null) {
            System.out.println("No user data to display");
            return;
        }

        usernameEmptyLbl.setText(currentUser.getUsername());
        emailEmptyLbl.setText(currentUser.getEmail());

        String accountType;
        if (currentUser.is_yearly_subscription()) {
            accountType = "Yearly Subscription";
        } else if (currentUser.getStore() == 4) {
            accountType = "Network ";
        } else if (currentUser.getStore() >= 1 && currentUser.getStore() <= 3) {
            accountType = "Store - " + currentUser.getStoreName();
        } else {
            accountType = "Guest";      //should never happen
        }

        accountTypeEmptyLbl.setText(accountType);

        subscribeBtn.setVisible(!currentUser.is_yearly_subscription());
    }


    @FXML
    void initialize() throws IOException{

        // set register anchor
        AnchorPane.setBottomAnchor(my_account_data, 58.0);
        AnchorPane.setTopAnchor(my_account_data, 58.0);
        AnchorPane.setRightAnchor(my_account_data, 89.0);
        AnchorPane.setLeftAnchor(my_account_data, 89.0);
    }


    public void setCurrentUser(LoginRegCheck user) {
        this.currentUser = user;
        loadUserInfo();
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