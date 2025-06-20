
package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

public class MyAccountController {

    @FXML private Label accountTypeEmptyLbl, accountTypeLbl, emailEmptyLbl, emailLbl, myAccountLbl, usernameEmptyLbl, usernameLbl;
    @FXML private Button myOrdersButton, subscribeBtn;
    @FXML private AnchorPane myAccUsers, my_account_data;

    private LoginRegCheck currentUser;
    private CatalogController catalogController;

    public void setCatalogController(CatalogController catalogController) {
        this.catalogController = catalogController;
    }

    public void setCurrentUser(LoginRegCheck user) {
        this.currentUser = user;
        loadUserInfo();
    }

    @FXML
    void initialize() {
        // AnchorPane margins
        AnchorPane.setBottomAnchor(my_account_data, 58.0);
        AnchorPane.setTopAnchor(my_account_data, 58.0);
        AnchorPane.setRightAnchor(my_account_data, 89.0);
        AnchorPane.setLeftAnchor(my_account_data, 89.0);
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
            OrdersHistoryController controller = loader.getController();
            controller.setCurrentUser(currentUser);
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
        if (currentUser == null) return;

        usernameEmptyLbl.setText(currentUser.getUsername());
        emailEmptyLbl.setText(currentUser.getEmail());

        String accountType = switch (currentUser.getStore()) {
            case 4 -> currentUser.is_yearly_subscription() ? "Yearly Subscription" : "Network";
            case 1, 2, 3 -> "Store - " + currentUser.getStoreName();
            default -> "Guest"; // shouldn't happen
        };

        accountTypeEmptyLbl.setText(accountType);
        subscribeBtn.setVisible(!currentUser.is_yearly_subscription());
    }

    @FXML
    void onSubscribe(ActionEvent event) {
        if (currentUser == null) {
            System.out.println("No user logged in");
            return;
        }

        if (currentUser.getStore() >= 1 && currentUser.getStore() <= 3) {
            if (!showConfirmation("Upgrade Required",
                    "To subscribe to the yearly plan, you must first upgrade to a network subscription.",
                    "Do you want to proceed?")) {
                return;
            }
            currentUser.setStore(4);
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
                    SimpleClient.getClient().sendToServer(currentUser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            paymentController.setOnPaymentSuccess(() -> {
                currentUser.set_yearly_subscription(true);
                sendUpdateToServer.run();

                if (catalogController != null) {
                    catalogController.set_user(currentUser);
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
