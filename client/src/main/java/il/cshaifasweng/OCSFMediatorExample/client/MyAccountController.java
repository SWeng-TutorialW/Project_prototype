
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateUserEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.security.Key;
import java.util.Objects;
import java.util.Optional;

public class MyAccountController {
    @FXML TextField userChangeTxtB;
    @FXML TextField emailChangeTxtB;
    @FXML TextField phoneChangeTxtB;
    @FXML private Label accountTypeEmptyLbl, passErrorMsgLbl,  newPassLbl, confNewPassLbl,accTypeLbl, usrnmeLbl, emailLbl, phoneNumLbl, changePassLbl;
    @FXML private Button myOrdersButton, subscribeBtn, changeBtn;
    @FXML private AnchorPane myAccUsers, my_account_data;
    @FXML private PasswordField newPassTxtB, confNewPassTxtB;
    private LoginRegCheck tempUser;

    private CatalogController catalogController;

    public void setCatalogController(CatalogController catalogController) {
        this.catalogController = catalogController;
    }

    public void setCurrentUser(LoginRegCheck user) {
        SimpleClient.setCurrentUser(user);
        loadUserInfo();
    }

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        // AnchorPane margins
        loadUserInfo();
        AnchorPane.setBottomAnchor(my_account_data, 58.0);
        AnchorPane.setTopAnchor(my_account_data, 58.0);
        AnchorPane.setRightAnchor(my_account_data, 89.0);
        AnchorPane.setLeftAnchor(my_account_data, 89.0);
    }

    @FXML
    private void handleMyOrdersButton() {
        if (SimpleClient.getCurrentUser() == null) {
            System.out.println("No user logged in");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("orders_history.fxml"));
            Parent root = loader.load();
            OrdersHistoryController controller = loader.getController();
            controller.setCurrentUser(SimpleClient.getCurrentUser());
            controller.loadUserOrders();

            Stage stage = new Stage();
            stage.setTitle("My Order History");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) myOrdersButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUserInfo() {
        if (SimpleClient.getCurrentUser() == null) return;

        userChangeTxtB.setText(SimpleClient.getCurrentUser().getUsername());
        emailChangeTxtB.setText(SimpleClient.getCurrentUser().getEmail()); // what if the user changes their info?
        phoneChangeTxtB.setText(SimpleClient.getCurrentUser().getPhoneNum());
        String accountType = switch (SimpleClient.getCurrentUser().getStore()) {
            case 4 -> SimpleClient.getCurrentUser().is_yearly_subscription() ? "Yearly Subscription" : "Network";
            case 1, 2, 3 -> "Store - " + SimpleClient.getCurrentUser().getStoreName();
            default -> "Guest"; // shouldn't happen
        };

        accountTypeEmptyLbl.setText(accountType);
        subscribeBtn.setVisible(!SimpleClient.getCurrentUser().is_yearly_subscription());
    }
    @Subscribe
    public void onFailedUpdate(WarningEvent warning){

        if(warning.getWarning().getMessage().startsWith("#updateFail_UserExists")){
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Username Error");
                    alert.setHeaderText("Username Already Exists");
                    alert.setContentText("The username you entered is already taken. Please choose a different username.");
                    alert.showAndWait();
                    userChangeTxtB.setText(SimpleClient.getCurrentUser().getUsername());
                });
        }

    }
    @Subscribe
    public void onSuccessfulUpdate(String str) {
        if(str.startsWith("#userUpdateSuccess")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Success");
                alert.setHeaderText("Successfully Updated User Details");
                alert.setContentText("Your Details Are Up-To-Date.");
                alert.showAndWait();
            });
            loadUserInfo();
    }
    }
    @Subscribe
    public void getUserDetails(UpdateUserEvent user) {
        if(Objects.equals(user.getUpdatedUser().getId(), SimpleClient.getCurrentUser().getId())) { // just to make sure we are updating the correct user
            SimpleClient.setCurrentUser(user.getUpdatedUser());
            loadUserInfo();
        }

    }
    @FXML
    void sendUserUpdate(ActionEvent event){

            if(newPassTxtB.getText().equals(confNewPassTxtB.getText())) {
                passErrorMsgLbl.setVisible(false);
                UpdateUserEvent updatedUser;
                LoginRegCheck currentUser = SimpleClient.getCurrentUser();

                currentUser.setUsername(userChangeTxtB.getText());
                currentUser.setEmail(emailChangeTxtB.getText());
                currentUser.setPhoneNum(phoneChangeTxtB.getText());
                currentUser.setPassword(newPassTxtB.getText());
                updatedUser = new UpdateUserEvent(currentUser);
                try {
                    SimpleClient.client.sendToServer(updatedUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{passErrorMsgLbl.setVisible(true);}
    }
    @FXML
    void onSubscribe(ActionEvent event) {
        if (SimpleClient.getCurrentUser() == null) { // Good, we're making sure the user is indeed logged in
            System.out.println("No user logged in");
            return;
        }

        if (SimpleClient.getCurrentUser().getStore() >= 1 && SimpleClient.getCurrentUser().getStore() <= 3) {
            if (!showConfirmation("Upgrade Required",
                    "To subscribe to the yearly plan, you must first upgrade to a network subscription.",
                    "Do you want to proceed?")) {
                return;
            }
            SimpleClient.getCurrentUser().setStore(4);
            showAlert(Alert.AlertType.INFORMATION, "Upgraded", null,
                    "Your account has been upgraded to a Network account.\nYou may now proceed to register for the yearly subscription.");
        }

        openPaymentWindow();
    }

    private void openPaymentWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("payment.fxml"));
            Parent root = loader.load();
            PaymentController paymentController = loader.getController();

            Stage paymentStage = new Stage();
            paymentController.setStage(paymentStage);
            paymentController.setPayUpgrade(true);

            Runnable sendUpdateToServer = () -> {
                try {
                    SimpleClient.getClient().sendToServer(SimpleClient.getCurrentUser());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            paymentController.setOnPaymentSuccess(() -> {
                SimpleClient.getCurrentUser().set_yearly_subscription(true);
                sendUpdateToServer.run();

                if (catalogController != null) {
                    catalogController.set_user(SimpleClient.getCurrentUser());
                    catalogController.set_type(4);
                }

                showAlert(Alert.AlertType.INFORMATION, "Subscription Completed!", null,
                        "Your account was successfully upgraded to a Yearly Subscription.");

                Platform.runLater(() -> {
                    loadUserInfo();
                    paymentStage.close();
                    ((Stage) subscribeBtn.getScene().getWindow()).close();
                });
            });

            paymentController.setOnPaymentCancel(() -> {
                sendUpdateToServer.run();
                showAlert(Alert.AlertType.WARNING, "Payment Cancelled",
                        "Your payment was not completed.",
                        "You remain on your current account type.");
                loadUserInfo();
            });

            paymentStage.setOnCloseRequest(e -> paymentController.getOnPaymentCancel().run());

            paymentController.postInitialize();
            paymentStage.setTitle("Yearly Subscription Payment");
            paymentStage.setScene(new Scene(root));
            paymentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean showConfirmation(String title, String header, String content) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle(title);
        confirmation.setHeaderText(header);
        confirmation.setContentText(content);

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(yes, no);

        Optional<ButtonType> result = confirmation.showAndWait();
        return result.isPresent() && result.get() == yes;
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
