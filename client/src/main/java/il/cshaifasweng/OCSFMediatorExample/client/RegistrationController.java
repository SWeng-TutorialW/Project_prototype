package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.Store;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.entities.change_user_login;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


/**
 * Sample Skeleton for 'registration_screen.fxml' Controller Class
 */

public class RegistrationController {

    public static boolean cameFromConnect = false;
    @FXML private Label credit_Card;
    @FXML private PasswordField credit_card_box;
    @FXML private Label id;
    @FXML private AnchorPane regAnchPane;
    @FXML private Button regBtn;
    @FXML private TextField regEmailTxtB;
    @FXML private TextField regFullNameTxtB;
    @FXML private Label regLbl;
    @FXML private PasswordField regPassConfTxtB;
    @FXML private TextField regPassConfVisibleTxtB;
    @FXML private PasswordField regPassTxtB;
    @FXML private TextField regPassVisibleTxtB;
    @FXML private TextField regPhoneTxtB;
    @FXML private CheckBox regShowPassCB;
    @FXML private TextField regUserTxtB;
    @FXML private AnchorPane registerWin;
    @FXML private ComboBox<String> select_account_type;
    @FXML private ComboBox<String> select_store;
    @FXML private Label select_store_label;
    @FXML private AnchorPane logAnchPane;
    @FXML
    private Label regPassLbl;
    @FXML
    private Label regPhoneLbl;
    @FXML
    private Label regUserLbl;
    @FXML
    private Label regUserLbl1;
    @FXML
    private Label regUserLbl11;
    @FXML
    private Label selectStoreLbl;
    @FXML
    private Button switchLoginRegbtn;
    @FXML
    private TextField userLogTxtB;
    @FXML
    private PasswordField passLogTxtB;
    @FXML
    private TextField regIdTxtB;


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

    private CatalogController catalogController;
    public void setCatalogController(CatalogController controller) {
        this.catalogController = controller;
    }
    private connect_scene_Con con;

    int store=-1;
    boolean is_yearly_subscription=false;
    List<LoginRegCheck> users;
    public int logOrReg = 0; // 1 for login, 0 for register
    public boolean gotFromConnectScene = false; // true if we came from connect scene, false if we came from catalog scene
    LoginRegCheck tempUser = null;

    public void setController(connect_scene_Con controller) { con = controller; }
    @FXML
    void logToSys(MouseEvent event) throws IOException {
        String user = userLogTxtB.getText();
        String pass = passLogTxtB.getText();
        for (LoginRegCheck loginRegCheck : users) {

            if (loginRegCheck.getUsername().equals(user) && loginRegCheck.getPassword().equals(pass)) {
                // Check if user is already logged in
                if (loginRegCheck.getIsLogin() == 1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Login Error");
                    alert.setHeaderText("Already Logged In");
                    alert.setContentText("This user is already logged in elsewhere. Please log out from other devices first.");
                    alert.showAndWait();
                    return;
                }
                if (isPaymentStageOpen()) {
                    paymentStageInstance.close();
                }

                System.out.println("");
                change_user_login wrapper = new change_user_login(loginRegCheck, 1);
                try {
                    SimpleClient.getClient().sendToServer(wrapper);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loginRegCheck.setIsLogin(1);
                SimpleClient.isGuest = false;
                catalogController.set_user(loginRegCheck);
                catalogController.set_type(loginRegCheck.getStore());
                con.set_user(loginRegCheck);
                con.set_guest(false);
                con.set_type_client(true);
                con.set_type_local(loginRegCheck.getStore());
                Success success = new Success("Log-In Successfully!");
                EventBus.getDefault().post(new SuccessEvent(success));
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();


                if (loginRegCheck.isYearlySubscriptionExpired()) {
                    loginRegCheck.set_yearly_subscription(false);
                    loginRegCheck.setSubscriptionStartDate(null);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Subscription Expired");
                    alert.setHeaderText("Your yearly subscription has expired.");
                    alert.setContentText("You have been moved to a regular network account. You may renew your yearly plan at any time.");
                    alert.showAndWait();
                }
                return;
            }

        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Error");
        alert.setHeaderText("Login Failed");
        alert.setContentText("Your Username or Password are incorrect");
        alert.showAndWait();

    }


    public String checkIfValid(String regUser, String email, String regPass, String confPass, String fullName, String phoneNumber, String account_type, String userId) {
        // Check if all required fields are filled
        if (isTextFieldEmpty(regPassTxtB) || isTextFieldEmpty(regEmailTxtB) || isTextFieldEmpty(regUserTxtB) || isTextFieldEmpty(regPassConfTxtB) || isTextFieldEmpty(regFullNameTxtB) || isTextFieldEmpty(regPhoneTxtB)) {
            return "Please fill in all the fields";
        }
        
        // Check if passwords match
        if (!regPass.equals(confPass)) {
            return "Passwords do not match";
        }
        
        // Check account type selection
        if (isComboBoxEmpty(select_account_type)) {
            return "Please select an account type";
        }
        
        // Check store selection for Store account type
        if ("Store".equals(account_type) && isComboBoxEmpty(select_store)) {
            return "You need to select a store";
        }
        
        // Check if username already exists
        if (users != null) {
            for (LoginRegCheck user : users) {
                if (user.getUsername().equals(regUser)) {
                    return "Username already exists";
                }
            }
        }
        
        // Check store selection for non-yearly subscription
        if (!is_yearly_subscription && store == -1) {
            return "You need to select a store";
        }
        
        // Check minimum length requirements
        if (regUser.length() < 3 || regPass.length() < 3 || fullName.length() < 3) {
            return "Username, password and full name must be at least 3 characters long";
        }
        
        // Enhanced password validation (above 3 length)
        if (regPass.length() < 3) {
            return "Password must be at least 3 characters long";
        }
        
        // Enhanced phone number validation (Israeli format)
        if (!isValidIsraeliPhone(phoneNumber)) {
            return "Phone number must be in format: 05XXXXXXXX";
        }
        
        // Enhanced email validation
        if (!isValidEmail(email)) {
            return "Please enter a valid email address";
        }
        
        // Enhanced ID validation for yearly subscription
        if (is_yearly_subscription) {
            if (userId == null || userId.trim().isEmpty()) {
                return "ID is required for yearly subscription";
            }
            if (!isValidIsraeliId(userId)) {
                return "Please enter a valid 9-digit Israeli ID number";
            }
        }
        
        return null;
    }



    @FXML
    void RegToSys(MouseEvent event) throws IOException {

        String email = regEmailTxtB.getText().trim();
        String regUser = regUserTxtB.getText().trim();
        String fullName = regFullNameTxtB.getText();
        String phoneNumber = regPhoneTxtB.getText().trim();
        String regPass = regShowPassCB.isSelected() ? regPassVisibleTxtB.getText() : regPassTxtB.getText();
        String confPass = regShowPassCB.isSelected() ? regPassConfVisibleTxtB.getText() : regPassConfTxtB.getText();
        String account_type = select_account_type.getValue();
        String userId = regIdTxtB.getText();

        String check = checkIfValid(regUser, email, regPass, confPass, fullName, phoneNumber, account_type, userId);
        if (check != null) {
            Warning warning = new Warning(check);
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        int isLogin = RegistrationController.cameFromConnect ? 0 : 1; // if we came from connect scene, we are not logged in yet, so isLogin = 0, else isLogin = 1
        LoginRegCheck new_user = new LoginRegCheck(regUser, regPass, email, isLogin, false, store, phoneNumber, fullName, userId, false); // for now it's it meant to be for registration only.
        tempUser = new_user; // maybe this user will be registered successfully
        Runnable sendAndClose = () -> {
            try {
                SimpleClient.getClient().sendToServer(new_user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Set the user as logged in
            new_user.setIsLogin(1);
            SimpleClient.isGuest = false;
            
            if (catalogController != null) {
                catalogController.set_user(new_user);
                catalogController.set_type(store);
            }
            Platform.runLater(() -> {
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
                EventBus.getDefault().unregister(this);
            });
        };

        if (is_yearly_subscription) {
            openPaymentWindow(() -> {
                new_user.set_yearly_subscription(true);
                new_user.setSubscriptionStartDate(LocalDate.now());
                sendAndClose.run();
            }, sendAndClose, event);
        } else {
            SimpleClient.isGuest = false;
            CatalogController.set_user(new_user); // Set the user in CatalogController
            sendAndClose.run();
        }
    }
    @Subscribe
    public void onRegisterEvent(String message) {
        if(message == "#registerSuccess") {
            if (is_yearly_subscription) {
                Success success = new Success("Registration Completed!\nYour yearly subscription has been successfully activated.");
                EventBus.getDefault().post(new SuccessEvent(success));
            } else {
                Success success = new Success("Registration Successful!");
                EventBus.getDefault().post(new SuccessEvent(success));
            }
            // Set the user as logged in after successful registration
            tempUser.setIsLogin(1);
            CatalogController.set_user(tempUser); // Set the user in CatalogController
            EventBus.getDefault().unregister(this);
            Stage stage = (Stage) regAnchPane.getScene().getWindow();
            stage.close();


        } else if(message == "#registerFail") {
            Warning warning = new Warning("Registration Failed! Please try again.");
            EventBus.getDefault().post(new WarningEvent(warning));
            CatalogController.set_user(null);
        }
    }
    @FXML
    void initialize() throws IOException {
        EventBus.getDefault().register(this);

        if(gotFromConnectScene){
            gotFromConnectScene = false;
            logAnchPane.setVisible(false);
            regAnchPane.setVisible(true);
            switchLoginRegbtn.setVisible(false);
        } else{switchLoginRegbtn.setVisible(true);
            logAnchPane.setVisible(true);
            regAnchPane.setVisible(false);
        }

        AnchorPane.setBottomAnchor(logAnchPane, 109.0);
        AnchorPane.setTopAnchor(logAnchPane, 60.0);
        AnchorPane.setLeftAnchor(logAnchPane, 28.0);
        AnchorPane.setRightAnchor(logAnchPane, 28.0);

        AnchorPane.setBottomAnchor(regAnchPane, 80.0);
        AnchorPane.setTopAnchor(regAnchPane, 38.0);
        AnchorPane.setLeftAnchor(regAnchPane, 22.0);
        AnchorPane.setRightAnchor(regAnchPane, 22.0);

        select_account_type.getItems().addAll("Store", "Network", "Yearly Subscription");
        select_store.getItems().addAll("lilach_Haifa", "lilach_Krayot", "lilach_Nahariyya");
        SimpleClient.getClient().sendToServer("asks_for_users");
    }

    private void openPaymentWindow(Runnable onSuccess, Runnable onCancel, MouseEvent event) {

        connect_scene_Con.closeAllPopups();

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

            PaymentController controller = loader.getController();
            Stage paymentStage = new Stage();
            setPaymentStageInstance(paymentStage);
            controller.setPayUpgrade(true);
            controller.setStage(paymentStage);
            controller.postInitialize();

            controller.setOnPaymentSuccess(() -> {
                onSuccess.run();
                paymentStage.close();

            });

            controller.setOnPaymentCancel(() -> {
                onCancel.run();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Payment Cancelled");
                alert.setHeaderText("Your payment was not completed.");
                alert.setContentText("You remain on your current account type.");
                alert.showAndWait();
            });

            paymentStage.setOnCloseRequest(e -> controller.getOnPaymentCancel().run());
            paymentStage.setTitle("Yearly Subscription Payment");
            paymentStage.setScene(new Scene(root));
            paymentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void selected_account(ActionEvent event) {
        String account_type = select_account_type.getValue();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        regIdTxtB.setText("");
        switch (account_type) {
            case "Store" -> {
                alert.setTitle("Store Subscription");
                alert.setHeaderText("What is a Store Subscription?");
                alert.setContentText("A store subscription allows Customer to buy only for specific Store");
                store = -1;
                is_yearly_subscription = false;
                select_store_label.setVisible(true);
                select_store.setVisible(true);
                select_store.setValue(null);
            }
            case "Network" -> {
                alert.setTitle("Network Subscription");
                alert.setHeaderText("What is a Network Subscription?");
                alert.setContentText("A network subscription allows customers to shop in all of our stores.");
                store = 4;
                is_yearly_subscription = false;
                select_store_label.setVisible(false);
                select_store.setVisible(false);
                select_store.setValue(null);
            }
            case "Yearly Subscription" -> {
                alert.setTitle("Yearly Subscription");
                alert.setHeaderText("What is a Yearly Subscription?");
                alert.setContentText("A yearly subscription allows customers to shop in all of our stores. It costs 100 shekels and grants a 10% discount on every purchase above 50 shekels.");
                store = 4;
                is_yearly_subscription = true;
                select_store_label.setVisible(false);
                select_store.setVisible(false);
                select_store.setValue(null);
            }
        }
        alert.showAndWait();
    }

    @FXML
    void select_store(ActionEvent event) {
        switch (select_store.getValue()) {
            case "lilach_Haifa" -> store = 1;
            case "lilach_Krayot" -> store = 2;
            case "lilach_Nahariyya" -> store = 3;
        }
    }

    @FXML
    void togglePass(ActionEvent event) {
        boolean show = regShowPassCB.isSelected();
        regPassVisibleTxtB.setText(regPassTxtB.getText());
        regPassConfVisibleTxtB.setText(regPassConfTxtB.getText());

        regPassVisibleTxtB.setVisible(show);
        regPassTxtB.setVisible(!show);
        regPassConfVisibleTxtB.setVisible(show);
        regPassConfTxtB.setVisible(!show);
    }


    @FXML
    void decideLogOrReg(MouseEvent event) // whenever we press on the "Go to Registration/Login" button
    {
        if (logOrReg % 2 == 1) { // we went from registration(0) to login(1)
            logAnchPane.setVisible(true);
            regAnchPane.setVisible(false);
            switchLoginRegbtn.setText("Go to Registration");

        } else { // we went from login(1) to registration(0)
            logAnchPane.setVisible(false);
            regAnchPane.setVisible(true);
            switchLoginRegbtn.setText("Go to Login");

        }
        logOrReg++;// toggle between 0 and 1
    }


    @Subscribe
    public void get_users(List<LoginRegCheck> allUsers) {
        users = allUsers;
    }

    private boolean isTextFieldEmpty(TextField tf) {
        return tf.getText() == null || tf.getText().trim().isEmpty();
    }

    private boolean isComboBoxEmpty(ComboBox<String> cb) {
        return cb.getValue() == null || cb.getValue().trim().isEmpty();
    }

    private boolean isValidIsraeliId(String id) {
        if (id == null || id.length() != 9) {
            return false;
        }
        
        // Check if all characters are digits
        if (!id.matches("^[0-9]{9}$")) {
            return false;
        }
        
        return true;
    }
    

    private boolean isValidIsraeliPhone(String phone) {
        return phone.matches("^05[0-9]{8}$");
    }


    private boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
    

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 3;
    }
    

    private boolean isValidUsername(String username) {
        return username != null && username.length() >= 3 && username.matches("^[a-zA-Z0-9_]+$");
    }

    private boolean isUsernameAvailable(String username) {
        if (users == null || username == null || username.trim().isEmpty()) {
            return false;
        }
        
        for (LoginRegCheck user : users) {
            if (user.getUsername().equals(username.trim())) {
                return false;
            }
        }
        return true;
    }
}

