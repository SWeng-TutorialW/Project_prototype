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
        userChangeTxtB.setEditable(false);
        AnchorPane.setBottomAnchor(my_account_data, 58.0);
        AnchorPane.setTopAnchor(my_account_data, 58.0);
        AnchorPane.setRightAnchor(my_account_data, 89.0);
        AnchorPane.setLeftAnchor(my_account_data, 89.0);
        if(idNumTxtB.getText().isEmpty() || Objects.equals(idNumTxtB.getText(), "null")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ID Number Missing");
            alert.setHeaderText("ID Number Not Found");
            alert.setContentText("Please enter your Israeli ID number to proceed with account updates.");
            alert.showAndWait();
            idNumTxtB.editableProperty().set(true);
        }
        if(current_User.isType()) // an employee
        {
            myOrdersButton.setDisable(true);
            myComplaintsButton.setDisable(true);
            subscribeBtn.setDisable(true);
            myOrdersButton.setVisible(false);
            subscribeBtn.setVisible(false);
            subscriptionExpireLbl.setVisible(false);
        }
        centerHelloAccountLbl();

        //add listeners
        newPassTxtB.textProperty().addListener((obs, oldText, newText) -> passErrorMsgLbl.setVisible(false));
        confNewPassTxtB.textProperty().addListener((obs, oldText, newText) -> passErrorMsgLbl.setVisible(false));
    }
    void centerHelloAccountLbl() {
        myAccountLbl.setText("Hello " + current_User.getFullName() != null ? current_User.getFullName() : "User");
        myAccountLbl.setAlignment(CENTER);
        //myAccountLbl.setLayoutX(myAccUsers.getWidth() / 2 - myAccountLbl.getWidth() / 2);

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

        if(warning.getWarning().getMessage().startsWith("#updateFail")){
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Details Update Warning");
                    alert.setHeaderText("ID Number Already Exists");
                    alert.setContentText("The ID number you entered is already taken. Please choose a different one.");
                    alert.showAndWait();
                    loadUserInfo();
                });
                idNumTxtB.setDisable(false);
                idNumTxtB.setEditable(true);
        }
    }

    @Subscribe
    public void onSuccessfulUpdate(UpdateUserEvent event) {
        System.out.println("onSuccessfulUpdate called with: " + event.getMsg());
        if(event.getMsg().startsWith("#userUpdateSuccess")) {
            System.out.println("#userUpdateSuccess detected, posting SuccessEvent");
            Platform.runLater(() -> {
                current_User = event.getUpdatedUser();
                // Alert alert = new Alert(Alert.AlertType.INFORMATION);
                // alert.setTitle("Update Success");
                // alert.setHeaderText("Successfully Updated User Details");
                // alert.setContentText("Your Details Are Up-To-Date.");
                // alert.showAndWait();
                Success success = new Success("Your details are up-to-date.");
                EventBus.getDefault().post(new SuccessEvent(success));
            });
            loadUserInfo();
        }
    }

    /*@Subscribe
    public void getUserDetails(UpdateUserEvent user) {
        if(Objects.equals(user.getUpdatedUser().getId(), current_User.getId())) { // just to make sure we are updating the correct user
            catalogController.set_user(current_User);
            loadUserInfo();
        }
    } IRRELEVANT */

    @FXML
    void sendUserUpdate(ActionEvent event) {

        if(!checkIsraeliID(idNumTxtB.getText())) {
            passErrorMsgLbl.setVisible(true);
            passErrorMsgLbl.setText("Invalid Israeli ID number. Please enter a valid 9-digit ID.");
            idNumTxtB.setDisable(false);
            idNumTxtB.setEditable(true);
            return;
        }

        passErrorMsgLbl.setVisible(false);
        UpdateUserEvent updatedUser;
        LoginRegCheck currentUser = current_User;
        currentUser.setUsername(userChangeTxtB.getText());
        currentUser.setEmail(emailChangeTxtB.getText());
        currentUser.setPhoneNum(phoneChangeTxtB.getText());
        currentUser.setFullName(fNameTxtB.getText());
        currentUser.setIdNum(idNumTxtB.getText());
        updatedUser = new UpdateUserEvent(currentUser);
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Update");
        confirmation.setHeaderText("Are you sure you want to update your details?");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == no) {
            // User chose not to update, return early
            return;
        }
        try {
            SimpleClient.client.sendToServer(updatedUser);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @FXML
    void onChangePassword(ActionEvent event) {
        if(checkIsraeliID(idNumTxtB.getText())) {
            passErrorMsgLbl.setVisible(false);
        } else {
            passErrorMsgLbl.setVisible(true);
            passErrorMsgLbl.setText("Invalid Israeli ID number. Please enter a valid 9-digit ID.");
            idNumTxtB.setDisable(false);
            idNumTxtB.setEditable(true);
            return;
        }
        if (newPassTxtB.getText().isEmpty() || confNewPassTxtB.getText().isEmpty()) {
            passErrorMsgLbl.setVisible(true);
            passErrorMsgLbl.setText("Please fill in both password fields.");
            return;
        }
        if (newPassTxtB.getText().length() < 3 || confNewPassTxtB.getText().length() < 3) {
            passErrorMsgLbl.setVisible(true);
            passErrorMsgLbl.setText("Password must be at least 3 characters long.");
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
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Password Change");
        confirmation.setHeaderText("Are you sure you want to update your Password?");

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(yes, no);
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == no) {
            // User chose not to update, return early
            return;
        }
        passErrorMsgLbl.setVisible(false);
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
    void onSubscribe(ActionEvent event) throws IOException {
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
            SimpleClient.getClient().sendToServer(new UpdateUserEvent(current_User)); // update user
        }

        openPaymentWindow();
    }

    public static boolean checkIsraeliID(String idNumber){

        // 1. Check length: Israeli ID numbers must be 9 digits long.
        if (idNumber == null || idNumber.length() != 9) {
            return false;
        }

        // 2. Check if all characters are digits.
        if (!idNumber.matches("\\d+")) {
            return false;
        }

        long sum = 0;
        for (int i = 0; i < idNumber.length(); i++) {
            // Get the digit from the right (least significant)
            int digit = Character.getNumericValue(idNumber.charAt(idNumber.length() - 1 - i));

            // Apply Luhn algorithm logic: multiply every second digit by 2
            // and sum the digits of the result if it's a two-digit number.
            if (i % 2 != 0) { // Every second digit from the right
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + (digit / 10); // Sum the digits (e.g., 12 -> 1+2=3)
                }
            }
            sum += digit;
        }

        // 3. The sum of the digits, after applying the Luhn algorithm, must be divisible by 10.
        return sum % 10 == 0;
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
