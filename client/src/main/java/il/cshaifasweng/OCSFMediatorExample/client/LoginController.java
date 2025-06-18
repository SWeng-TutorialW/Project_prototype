package il.cshaifasweng.OCSFMediatorExample.client;

//package il.cshaifasweng.OCSFMediatorExample.client;
//
//import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
//import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.paint.Paint;
//import javafx.stage.Stage;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//
//import java.io.IOException;
//import java.util.Objects;
//
//public class LoginController {
//
//    private int isLogin=1;
//
//    @FXML
//    private Button gotoRegBtn;
//
//    @FXML
//    private Label gotoRegLbl;
//
//    @FXML
//    private AnchorPane loginAnchPane;
//
//    @FXML
//    private AnchorPane loginWin;
//
//    @FXML
//    private Button loginBtn;
//
//    @FXML
//    private Label loginLbl;
//
//    @FXML
//    private Label passLbl;
//
//    @FXML
//    private PasswordField passTxtB;
//
//    @FXML
//    private AnchorPane regAnchPane;
//
//    @FXML
//    private Button regBtn;
//
//    @FXML
//    private Label regEmailLbl;
//
//    @FXML
//    private PasswordField regPassTxtB;
//
//    @FXML
//    private Label regLbl;
//
//    @FXML
//    private Label regPassConfLbl;
//
//    @FXML
//    private PasswordField regPassConfTxtB;
//
//    @FXML
//    private Label regPassLbl;
//
//    @FXML
//    private CheckBox regShowPassCB;
//
//
//    @FXML
//    private Label regUserLbl;
//
//    @FXML
//    private TextField regEmailTxtB;
//
//    @FXML
//    private TextField regUserTxtB;
//
//    @FXML
//    private CheckBox showPassCbox;
//
//    @FXML
//    private TextField userTxtB;
//
//    @FXML
//    private Label usernameLbl;
//
//    @FXML
//    private Label loginErrMsgLbl;
//
//    @FXML
//    private Label regErrMsgLbl;
//
//    @FXML
//    void loginRegToSys(MouseEvent event) {
//        System.out.println("Login/Register button clicked");
//        switch(isLogin){
//            case 1: // login
//                String username = userTxtB.getText();
//                String password = passTxtB.getText();
//                System.out.println("Attempting login for user: " + username);
//                if(username.length() >=4 || password.length()>=4){
//                    try {
//                        loginErrMsgLbl.setVisible(false);
//                        SimpleClient.getClient().sendToServer(new LoginRegCheck(username, password, "", 1));
//                        System.out.println("Login request sent to server");
//                        return;
//                    }
//                    catch (Exception e) {
//                        System.err.println("Error sending login request: " + e.getMessage());
//                        e.printStackTrace();
//                    }
//                }
//                System.out.println("Invalid login format");
//                regErrMsgLbl.setText("Details Not in Correct Format");
//                regErrMsgLbl.textFillProperty().set(Paint.valueOf("red"));
//                regErrMsgLbl.setVisible(true);
//                break;
//            case 0: // registration
//                String confPass = regPassConfTxtB.getText();
//                String email = regEmailTxtB.getText();
//                String regUser = regUserTxtB.getText();
//                String regPass = regPassTxtB.getText();
//                if(Objects.equals(confPass, regPass) && (regUser.length() >=4 || regPass.length()>=4 || email.length() >=4)){
//                    try {
//                        regErrMsgLbl.setVisible(false);
//                        SimpleClient.getClient().sendToServer(new LoginRegCheck(regUser, regPass, email, 0));
//                        return;
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                regErrMsgLbl.setText("Details Not in Correct Format");
//                regErrMsgLbl.textFillProperty().set(Paint.valueOf("red"));
//                regErrMsgLbl.setVisible(true);
//                break;
//
//        }
//    }
//
//    @Subscribe
//    public void onLoginRegResult(String msg) {
//        System.out.println("Received login result: " + msg);
//        if(msg.startsWith("#login/reg_ok")){
//            System.out.println("Login successful, setting loggedIn to true");
//            SimpleClient.loggedIn = true;
//            System.out.println("Current login status: " + SimpleClient.loggedIn);
//            EventBus.getDefault().unregister(this);
//
//            // Close the login window
//            Platform.runLater(() -> {
//                System.out.println("Closing login window");
//                Stage stage = (Stage) loginAnchPane.getScene().getWindow();
//                stage.close();
//
//                // If we have cart items, reopen the cart window
//                if (!OrderPageController.getCartItems().isEmpty()) {
//                    System.out.println("Cart has items, reopening cart window");
//                    try {
//                        FXMLLoader loader = new FXMLLoader(getClass().getResource("cart.fxml"));
//                        Parent root = loader.load();
//                        CartController cartController = loader.getController();
//                        cartController.setCartItems(OrderPageController.getCartItems());
//
//                        Stage cartStage = new Stage();
//                        cartStage.setTitle("Shopping Cart");
//                        cartStage.setScene(new Scene(root));
//                        cartStage.show();
//                        System.out.println("Cart window reopened");
//                    } catch (IOException e) {
//                        System.err.println("Error reopening cart: " + e.getMessage());
//                        e.printStackTrace();
//                        Warning warning = new Warning("Error reopening cart");
//                        EventBus.getDefault().post(new WarningEvent(warning));
//                    }
//                }
//            });
//        }
//        else if (msg.startsWith("#login_failed")) {
//            System.out.println("Login failed");
//            Platform.runLater(()->{
//                loginErrMsgLbl.setText("User/Pass are incorrect");
//                loginErrMsgLbl.setVisible(true);
//                loginErrMsgLbl.textFillProperty().set(Paint.valueOf("red"));
//            });
//        }
//        else if(msg.startsWith("#user_exists")) {
//            System.out.println("User already exists");
//            Platform.runLater(()->{
//                regErrMsgLbl.setText("User already exists");
//                regErrMsgLbl.setVisible(true);
//                regErrMsgLbl.textFillProperty().set(Paint.valueOf("red"));
//            });
//        }
//    }
//
//
//
//    @FXML
//    void switchRegLog(MouseEvent event) {
//
//        switch(isLogin){
//            case 1: // if we are in login mode
//                loginAnchPane.setVisible(false);
//                regAnchPane.setVisible(true);
//                isLogin = 0; // switch to register mode
//                break;
//            case 0:
//                loginAnchPane.setVisible(true);
//                regAnchPane.setVisible(false);
//                isLogin = 1; // switch to login mode
//                break;
//        }
//
//    }
//
//    @FXML
//    void togglePass(ActionEvent event) {
//        switch(isLogin){
//            case 1: // if we are in login mode right now
//
//                break;
//            case 0:
//
//                break;
//        }
//        // this is way too complicated than it should be, why......
//    }
//
//
//    @FXML
//    void initialize() {
//
//        EventBus.getDefault().register(this);
//            // REMEMBER: we only come to this window if we know the user is NOT logged in.
//        loginAnchPane.setVisible(true);
//        regAnchPane.setVisible(false);
//        // set login anchor
//        AnchorPane.setBottomAnchor(loginAnchPane, 58.0);
//        AnchorPane.setTopAnchor(loginAnchPane, 58.0);
//        AnchorPane.setRightAnchor(loginAnchPane, 89.0);
//        AnchorPane.setLeftAnchor(loginAnchPane, 89.0);
//        // set register anchor
//        AnchorPane.setBottomAnchor(regAnchPane, 58.0);
//        AnchorPane.setTopAnchor(regAnchPane, 58.0);
//        AnchorPane.setRightAnchor(regAnchPane, 89.0);
//        AnchorPane.setLeftAnchor(regAnchPane, 89.0);
//
//
//        Stage stage = (Stage) loginAnchPane.getScene().getWindow();
//        stage.setWidth(400);
//        stage.setHeight(500);
//        ((Stage) loginAnchPane.getScene().getWindow()).resizableProperty().setValue(false);
//
//
//
//    }
//
//}
//

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.entities.change_user_login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class LoginController {

    @FXML
    private Button gotoRegBtn;

    @FXML
    private Label gotoRegLbl;

    @FXML
    private AnchorPane loginAnchPane;

    @FXML
    private Button loginBtn;

    @FXML
    private Label loginErrMsgLbl;

    @FXML
    private Label loginLbl;

    @FXML
    private AnchorPane loginWin;

    @FXML
    private Label passLbl;

    @FXML
    private PasswordField passTxtB;

    @FXML
    private CheckBox showPassCbox;

    @FXML
    private TextField userTxtB;

    @FXML
    private Label usernameLbl;
    List<LoginRegCheck> users ;

    private CatalogController con;

    public void setCatalogController(CatalogController catalogController) {
        con = catalogController;
    }

    @FXML
    void loginRegToSys(ActionEvent event)
    {

        for (LoginRegCheck loginRegCheck : users) {

            if(loginRegCheck.getUsername().equals(userTxtB) && loginRegCheck.getPassword().equals(passTxtB))
            {
                change_user_login wrapper = new change_user_login(loginRegCheck,1);
                try {
                    SimpleClient.getClient().sendToServer(wrapper);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                con.set_user(loginRegCheck);
                con.set_type(loginRegCheck.getStore());
                return;

            }
            else
            {
                Warning warning = new Warning("Username or password doesn't match");
                EventBus.getDefault().post(new WarningEvent(warning));

            }
        }

    }



    @FXML
    void register(ActionEvent event)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration_screen.fxml"));
            Parent root = fxmlLoader.load();


            RegistrationController regController = fxmlLoader.getController();
            regController.setCatalogController(con);

            Stage stage = new Stage();
            stage.setTitle("Create New Account");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void initialize() throws IOException{
        EventBus.getDefault().register(this);
        System.out.println("Registered to EventBus - waiting for reg result");
        SimpleClient.getClient().sendToServer("asks_for_users");
    }


    @Subscribe
    public void get_users(List<LoginRegCheck> allUsers)
    {
        users=allUsers;
    }


    private boolean isTextFieldEmpty(TextField tf) {
        return tf.getText() == null || tf.getText().trim().isEmpty();
    }


}