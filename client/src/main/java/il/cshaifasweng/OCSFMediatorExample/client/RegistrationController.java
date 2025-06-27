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
import java.util.List;


/**
 * Sample Skeleton for 'registration_screen.fxml' Controller Class
 */

public class RegistrationController {

    @FXML private Label credit_Card;
    @FXML private PasswordField credit_card_box;
    @FXML private Label id;
    @FXML private PasswordField id_text;
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
           if(user.isEmpty() || pass.isEmpty()){
               Warning warn = new Warning("Please fill all the fields");
                EventBus.getDefault().post(new WarningEvent(warn));
                return;
           }


    }

    @Subscribe
    public void onSuccessLogin(String msg){
        if(msg.startsWith("#loginSuccess")){
            SimpleClient.setCurrentUser(tempUser);
            if(catalogController != null) {
                catalogController.set_user(tempUser);
                catalogController.set_type(store);
            }
            System.out.println("Login successful for user: " + tempUser.getUsername());
            Warning warning = new Warning("Login Successful");
            EventBus.getDefault().post(new WarningEvent(warning));
            Platform.runLater(() -> {
                ((Stage) registerWin.getScene().getWindow()).close(); // close the window after successful login
            });
        }else if(msg.startsWith("#loginFailed")){
            SimpleClient.setCurrentUser(null);
            System.out.println("Login failed for user: " + tempUser.getUsername());
            Warning warning = new Warning("Incorrect Username or Password");
            EventBus.getDefault().post(new WarningEvent(warning));
        }
    }
    @Subscribe
    public void onRegisterFail(String msg) {
        if (msg.startsWith("#registerFailed")) {
            SimpleClient.setCurrentUser(null);
            System.out.println("Registration failed for user: " + tempUser.getUsername());
            Warning warning = new Warning("Registration Failed: Maybe This Username Already Exists?");
            EventBus.getDefault().post(new WarningEvent(warning));
        }
    }


    public String checkIfValid(String regUser, String email, String regPass, String confPass, String fullName, String phoneNumber, String account_type, String userId) {
        if (isTextFieldEmpty(regPassTxtB) || isTextFieldEmpty(regEmailTxtB) || isTextFieldEmpty(regUserTxtB) || isTextFieldEmpty(regPassConfTxtB) || isTextFieldEmpty(regFullNameTxtB) || isTextFieldEmpty(regPhoneTxtB)) {
            return "Please fill in all the fields";
        }
        if (!regPass.equals(confPass)) {
            return "Passwords do not match";
        }
        if (isComboBoxEmpty(select_account_type)) {
            return "Please select an account type";
        }
        if ("Store".equals(account_type) && isComboBoxEmpty(select_store)) {
            return "You need to select a store";
        }
        for (LoginRegCheck user : users) {
            if (user.getUsername().equals(regUser)) {
                return "Username already exists";
            }
        }
        if (!is_yearly_subscription && store == -1) {
            return "You need to select a store";
        }
        if (regUser.length() < 4|| regPass.length() < 4 || fullName.length() < 4) {
            return "Username, password and full name must be at least 4 characters long";
        }
        if (!phoneNumber.matches("\\d{10}")) {
            return "Phone number must be exactly 10 digits";
        }
        if (!email.contains("@") || !email.contains(".")) {
            return "Invalid email format";
        }
        /*if (is_yearly_subscription) {
            if (userId == null || !userId.matches("\\d{9}")) {
                return "ID must be exactly 9 digits";
            }
        }*/
        return null;
    }


    @FXML
    void RegToSys(MouseEvent event) throws IOException {
        if(SimpleClient.getCurrentUser() != null){ Warning warn = new Warning("Can't Register While Being Logged-In."); return;}
        String email = regEmailTxtB.getText();
        String regUser = regUserTxtB.getText();
        String fullName = regFullNameTxtB.getText();
        String phoneNumber = regPhoneTxtB.getText();
        String regPass = regShowPassCB.isSelected() ? regPassVisibleTxtB.getText() : regPassTxtB.getText();
        String confPass = regShowPassCB.isSelected() ? regPassConfVisibleTxtB.getText() : regPassConfTxtB.getText();
        String account_type = select_account_type.getValue();
        String userId = id_text.getText();

        String check = checkIfValid(regUser, email, regPass, confPass, fullName, phoneNumber, account_type, userId);
        if (check != null) {
            Warning warning = new Warning(check);
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        // int isLogin = con != null ? 0 : 1; // need further checking to see if this is necessary
        LoginRegCheck new_user = new LoginRegCheck(regUser, regPass, email, 0, false, store, phoneNumber, fullName, userId, false); // for now it's it meant to be for registration only.

        Runnable sendAndClose = () -> {
            try {
                SimpleClient.getClient().sendToServer(new_user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
                sendAndClose.run();
            }, sendAndClose, event);
        } else {
            sendAndClose.run();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Registration Completed!");
            alert.setHeaderText("Registration Completed!");
            alert.showAndWait();
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("payment.fxml"));
            Parent root = loader.load();

            PaymentController controller = loader.getController();
            Stage paymentStage = new Stage();

            controller.setPayUpgrade(true);
            controller.setStage(paymentStage);
            controller.postInitialize();

            controller.setOnPaymentSuccess(() -> {
                onSuccess.run();
                paymentStage.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registration Completed!");
                alert.setHeaderText("Your yearly subscription has been successfully activated.");
                alert.showAndWait();
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
        logOrReg++; // toggle between 0 and 1

        if (logOrReg % 2 == 1) { // we went from registration(0) to login(1)
            logAnchPane.setVisible(true);
            regAnchPane.setVisible(false);
            switchLoginRegbtn.setText("Go to Registration");

        } else { // we went from login(1) to registration(0)
            logAnchPane.setVisible(false);
            regAnchPane.setVisible(true);
            switchLoginRegbtn.setText("Go to Login");
        }
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
}

