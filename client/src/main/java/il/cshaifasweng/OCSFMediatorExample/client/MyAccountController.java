package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
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
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import static javafx.geometry.Pos.CENTER;

public class MyAccountController {

    @FXML
    private Button ChangeBtn;
    @FXML
    private Label accTypeLbl;
    @FXML
    private Label accountTypeEmptyLbl;
    @FXML
    private Button changePassBtn;
    @FXML
    private Label changePassLbl;
    @FXML
    private Label confNewPassLbl;
    @FXML
    private PasswordField confNewPassTxtB;
    @FXML
    private TextField emailChangeTxtB;
    @FXML
    private Label emailLbl;
    @FXML
    private TextField fNameTxtB;
    @FXML
    private Label fullNameLbl;
    @FXML
    private TextField idNumTxtB;
    @FXML
    private AnchorPane myAccUsers;
    @FXML
    private Label myAccountLbl;
    @FXML
    private Button myOrdersButton;
    @FXML
    private Button myComplaintsButton;
    @FXML
    private AnchorPane my_account_data;
    @FXML
    private Label newPassLbl;
    @FXML
    private PasswordField newPassTxtB;
    @FXML
    private Label passErrorMsgLbl;
    @FXML
    private TextField phoneChangeTxtB;
    @FXML
    private Label phoneNumLbl;
    @FXML
    private Button subscribeBtn;
    @FXML
    private Label subscriptionExpireLbl;
    @FXML
    private Label subscriptionExpireTitleLbl;
    @FXML
    private TextField userChangeTxtB;
    @FXML
    private Label userIdLbl;
    @FXML
    private Label usrnmeLbl;


    private static LoginRegCheck current_User;

    private static CatalogController catalogController;

    static Stage myAccountStage = null;
    public static boolean isMyAccountOpen() {
        return myAccountStage != null && myAccountStage.isShowing();
    }
    public static void setMyAccountStage(Stage stage) {
        myAccountStage = stage;
        if (myAccountStage != null) {
            myAccountStage.setOnHidden(e -> myAccountStage = null);
        }
    }

    private static Stage paymentStageInstance = null;
    public static boolean isPaymentStageOpen() {
        return paymentStageInstance != null && paymentStageInstance.isShowing();
    }

    public static void setPaymentStageInstance(Stage stage) {
        paymentStageInstance = stage;
        if (paymentStageInstance != null) {
            paymentStageInstance.setOnHidden(e -> paymentStageInstance = null);
        }
    }

    private static Stage myOrdersStage = null;
    public static boolean isMyOrdersOpen() {
        return myOrdersStage != null && myOrdersStage.isShowing();
    }
    public static void setMyOrdersStage(Stage stage) {
        myOrdersStage = stage;
        if (myOrdersStage != null) {
            myOrdersStage.setOnHidden(e -> myOrdersStage = null);
        }
    }

    public static void setCatalogController(CatalogController catalogCtrl) {
        catalogController = catalogCtrl;
    }

    public static void setCurrentUser(LoginRegCheck user) {
        current_User = user;

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

        if(current_User.isType()) // an employee
        {
            myOrdersButton.setDisable(true);
            myComplaintsButton.setDisable(true);
            subscribeBtn.setDisable(true);
        }
        myAccountLbl.setText("My Account - Hello " + current_User.getFullName());
        myAccountLbl.setAlignment(CENTER);


    }

    @FXML
    private void handleMyOrdersButton() {
        if (current_User == null) {
            System.out.println("No user logged in");
            return;
        }

        if (isMyOrdersOpen()) {
            myOrdersStage.close();
            // Do not return; continue to open a new window
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("orders_history.fxml"));
            Parent root = loader.load();
            OrdersHistoryController controller = loader.getController();
            controller.setCurrentUser(current_User);
            controller.loadUserOrders();

            Stage stage = new Stage();
            setMyOrdersStage(stage);
            stage.setTitle("My Order History");
            stage.setScene(new Scene(root));
            stage.show();

            // ((Stage) myOrdersButton.getScene().getWindow()).close(); // <-- Remove this line to keep My Account open
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMyComplaintsButton() {
        if (current_User == null) {
            System.out.println("No user logged in");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("my_complaints.fxml"));
            Parent root = loader.load();
            MyComplaintsController controller = loader.getController();
            controller.setCurrentUser(current_User);

            Stage stage = new Stage();
            stage.setTitle("My Complaints");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUserInfo() {
        if (current_User == null) return;

        idNumTxtB.setText(String.valueOf(current_User.getIdNum())); // Assuming getId() returns Long or int
        idNumTxtB.setEditable(false);
        userChangeTxtB.setText(current_User.getUsername());
        emailChangeTxtB.setText(current_User.getEmail()); // what if the user changes their info?
        phoneChangeTxtB.setText(current_User.getPhoneNum());
        fNameTxtB.setText(current_User.getFullName());
        String accountType = switch (current_User.getStore()) {
            case 4 -> current_User.is_yearly_subscription() ? "Yearly Subscription" : "Network";
            case 1, 2, 3 -> "Store - " + current_User.getStoreName();
            default -> "Guest"; // shouldn't happen
        };
        if (current_User.is_yearly_subscription() && current_User.getSubscriptionStartDate() != null) {
            LocalDate expire = current_User.getSubscriptionStartDate().plusYears(1);

            subscriptionExpireTitleLbl.setVisible(true);
            subscriptionExpireLbl.setVisible(true);
            subscriptionExpireTitleLbl.setText(expire.toString());
        } else {
            subscriptionExpireTitleLbl.setVisible(false);
            subscriptionExpireLbl.setVisible(false);
        }
        accountTypeEmptyLbl.setText(accountType);
        subscribeBtn.setVisible(!current_User.is_yearly_subscription());
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
                    userChangeTxtB.setText(current_User.getUsername());
                });
        }

    }

    @Subscribe
    public void onSuccessfulUpdate(String str) {
        System.out.println("onSuccessfulUpdate called with: " + str);
        if(str.startsWith("#userUpdateSuccess")) {
            System.out.println("#userUpdateSuccess detected, posting SuccessEvent");
            Platform.runLater(() -> {
                // Alert alert = new Alert(Alert.AlertType.INFORMATION);
                // alert.setTitle("Update Success");
                // alert.setHeaderText("Successfully Updated User Details");
                // alert.setContentText("Your Details Are Up-To-Date.");
                // alert.showAndWait();
                Success success = new Success("Your details are up-to-date.");
                org.greenrobot.eventbus.EventBus.getDefault().post(new SuccessEvent(success));
            });
            loadUserInfo();
        }
    }

    @Subscribe
    public void getUserDetails(UpdateUserEvent user) {
        if(Objects.equals(user.getUpdatedUser().getId(), current_User.getId())) { // just to make sure we are updating the correct user
            catalogController.set_user(current_User);
            loadUserInfo();
        }
    }

    @FXML
    void sendUserUpdate(ActionEvent event) {

        passErrorMsgLbl.setVisible(false);
        UpdateUserEvent updatedUser;
        LoginRegCheck currentUser = current_User;

        currentUser.setUsername(userChangeTxtB.getText());
        currentUser.setEmail(emailChangeTxtB.getText());
        currentUser.setPhoneNum(phoneChangeTxtB.getText());
        // currentUser.setPassword(newPassTxtB.getText()); Irrelevant, a different function handles password changes
        currentUser.setFullName(fNameTxtB.getText());
        updatedUser = new UpdateUserEvent(currentUser);
        try {
            SimpleClient.client.sendToServer(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> {
            // Alert alert = new Alert(Alert.AlertType.INFORMATION);
            // alert.setTitle("Update Success");
            // alert.setHeaderText("Successfully Updated User Details");
            // alert.setContentText("Your Details Are Up-To-Date.");
            // alert.showAndWait();
            Success success = new Success("Your details are up-to-date.");
            EventBus.getDefault().post(new SuccessEvent(success));
        });
    }


    @FXML
    void onChangePassword(ActionEvent event) {
        if (newPassTxtB.getText().isEmpty() || confNewPassTxtB.getText().isEmpty()) {
            passErrorMsgLbl.setVisible(true);
            passErrorMsgLbl.setText("Please fill in both password fields.");
            return;
        }

        if (!newPassTxtB.getText().equals(confNewPassTxtB.getText())) {
            passErrorMsgLbl.setVisible(true);
            passErrorMsgLbl.setText("Passwords do not match.");
            return;
        }
        if((newPassTxtB.equals(confNewPassTxtB)) && (newPassTxtB.getText().trim().equals(current_User.getPassword()))) {
            passErrorMsgLbl.setVisible(true);
            passErrorMsgLbl.setText("New password cannot be the same as the current password.");
            return;
        }

        current_User.setPassword(newPassTxtB.getText());
        Platform.runLater(() -> {
            Success success = new Success("Your New Password Has Been Changed Successfully.");
            EventBus.getDefault().post(new SuccessEvent(success));
        });
        try {
            SimpleClient.getClient().sendToServer(new UpdateUserEvent(current_User));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void onSubscribe(ActionEvent event) {
        if (current_User == null) { // Good, we're making sure the user is indeed logged in
            System.out.println("No user logged in");
            return;
        }

        if (current_User.getStore() >= 1 && current_User.getStore() <= 3) {
            if (!showConfirmation("Upgrade Required",
                    "To subscribe to the yearly plan, you must first upgrade to a network subscription.",
                    "Do you want to proceed?")) {
                return;
            }
            current_User.setStore(4);
            loadUserInfo();  //added
            showAlert(Alert.AlertType.INFORMATION, "Upgraded", null,
                    "Your account has been upgraded to a Network account.\nYou may now proceed to register for the yearly subscription.");
        }

        openPaymentWindow();
    }

    private void openPaymentWindow() {
        if (isPaymentStageOpen()) {
            Warning warning = new Warning("The subscription/payment window is already open.");
            EventBus.getDefault().post(new WarningEvent(warning));
            paymentStageInstance.toFront();
            paymentStageInstance.requestFocus();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("payment.fxml"));
            Parent root = loader.load();
            PaymentController paymentController = loader.getController();

            Stage paymentStage = new Stage();
            setPaymentStageInstance(paymentStage);
            paymentController.setStage(paymentStage);
            paymentController.setPayUpgrade(true);

            Runnable sendUpdateToServer = () -> {
                try {
                    SimpleClient.getClient().sendToServer(new UpdateUserEvent(current_User));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            int storeType = current_User.getStore();
            paymentController.setOnPaymentSuccess(() -> {
                current_User.set_yearly_subscription(true);
                current_User.setSubscriptionStartDate(LocalDate.now());

                sendUpdateToServer.run();

                // Notify other controllers (e.g., checkout) of the upgrade
                EventBus.getDefault().post(new UpdateUserEvent(current_User));

                // Add subscription order to user's orders
                Order subscriptionOrder = new Order(current_User.getUsername(), current_User.getEmail(), current_User);
                subscriptionOrder.setStatus("CONFIRMED");
                subscriptionOrder.setTotalAmount(100.0); // Assuming subscription price is 100
                subscriptionOrder.setDiscountAmount(0.0);
                subscriptionOrder.setRequiresDelivery(false);
                Flower subscriptionFlower = new Flower("Yearly Subscription", 100.0, "Subscription");
                CartItem subscriptionItem = new CartItem(subscriptionFlower, 1, current_User.getStoreName());
                subscriptionOrder.addItem(subscriptionItem);
                // Optionally, persist or notify the system about the new order
                // For now, just add to OrdersHistoryController if possible
                OrdersHistoryController.addOrderForUser(current_User, subscriptionOrder);

                if (catalogController != null) {
                    catalogController.set_user(current_User);
                    catalogController.set_type(4);
                }
                current_User.setStore(4); // Ensure the user is set to Network store type
                showAlert(Alert.AlertType.INFORMATION, "Subscription Completed!", null,
                        "Your account was successfully upgraded to a Yearly Subscription.");

                Platform.runLater(() -> {
                    loadUserInfo();
                    paymentStage.close();
                    // ((Stage) subscribeBtn.getScene().getWindow()).close(); // <-- Remove this line to keep My Account open
                });
            });

            paymentController.setOnPaymentCancel(() -> {
                sendUpdateToServer.run();
                showAlert(Alert.AlertType.WARNING, "Payment Cancelled",
                        "Your payment was not completed.",
                        "You remain on your current account type.");

                current_User.setStore(storeType);
                loadUserInfo();
            });

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
