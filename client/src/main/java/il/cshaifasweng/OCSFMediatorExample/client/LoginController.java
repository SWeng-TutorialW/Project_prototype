package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

public class LoginController {

    private int isLogin=1;

    @FXML
    private Button gotoRegBtn;

    @FXML
    private Label gotoRegLbl;

    @FXML
    private AnchorPane loginAnchPane;

    @FXML
    private AnchorPane loginWin;

    @FXML
    private Button loginBtn;

    @FXML
    private Label loginLbl;

    @FXML
    private Label passLbl;

    @FXML
    private PasswordField passTxtB;

    @FXML
    private AnchorPane regAnchPane;

    @FXML
    private Button regBtn;

    @FXML
    private Label regEmailLbl;

    @FXML
    private PasswordField regPassTxtB;

    @FXML
    private Label regLbl;

    @FXML
    private Label regPassConfLbl;

    @FXML
    private PasswordField regPassConfTxtB;

    @FXML
    private Label regPassLbl;

    @FXML
    private CheckBox regShowPassCB;


    @FXML
    private Label regUserLbl;

    @FXML
    private TextField regEmailTxtB;

    @FXML
    private TextField regUserTxtB;

    @FXML
    private CheckBox showPassCbox;

    @FXML
    private TextField userTxtB;

    @FXML
    private Label usernameLbl;

    @FXML
    private Label loginErrMsgLbl;

    @FXML
    private Label regErrMsgLbl;

    @FXML
    void loginRegToSys(MouseEvent event) {
        // After we successfully log in, do SimpleClient.sendtoserver(we are logged) and SimpleClient.loggedIn = true;


        switch(isLogin){
            case 1: // login
                String username = userTxtB.getText();
                String password = passTxtB.getText();
                if(username.length() >=4 || password.length()>=4){

                    try {
                        loginErrMsgLbl.setVisible(false);
                        SimpleClient.getClient().sendToServer(new LoginRegCheck(username, password, "", 1));
                        return;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                regErrMsgLbl.setText("Details Not in Correct Format");
                regErrMsgLbl.textFillProperty().set(Paint.valueOf("red"));
                regErrMsgLbl.setVisible(true);
                break;
            case 0: // registration
                String confPass = regPassConfTxtB.getText();
                String email = regEmailTxtB.getText();
                String regUser = regUserTxtB.getText();
                String regPass = regPassTxtB.getText();
                if(Objects.equals(confPass, regPass) && (regUser.length() >=4 || regPass.length()>=4 || email.length() >=4)){
                    try {
                        regErrMsgLbl.setVisible(false);
                        SimpleClient.getClient().sendToServer(new LoginRegCheck(regUser, regPass, email, 0));
                        return;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                regErrMsgLbl.setText("Details Not in Correct Format");
                regErrMsgLbl.textFillProperty().set(Paint.valueOf("red"));
                regErrMsgLbl.setVisible(true);
                break;

        }
    }

    @Subscribe
    public void onLoginRegResult(String msg) {
        if(msg.startsWith("#login/reg_ok")){
            SimpleClient.loggedIn = true;
            EventBus.getDefault().unregister(this);
                // Close the window (because we logged in)
                Platform.runLater(() -> {Stage stage = (Stage) loginAnchPane.getScene().getWindow();
                    stage.close();});
        }
        else if (msg.startsWith("#login_failed"))
            Platform.runLater(()->{loginErrMsgLbl.setText("User/Pass are incorrect");
                loginErrMsgLbl.setVisible(true);
                loginErrMsgLbl.textFillProperty().set(Paint.valueOf("red"));});
        else if(msg.startsWith("#user_exists"))
            Platform.runLater(()->{regErrMsgLbl.setText("User already exists");
                regErrMsgLbl.setVisible(true);
                regErrMsgLbl.textFillProperty().set(Paint.valueOf("red"));});

    }



    @FXML
    void switchRegLog(MouseEvent event) {

        switch(isLogin){
            case 1: // if we are in login mode
                loginAnchPane.setVisible(false);
                regAnchPane.setVisible(true);
                isLogin = 0; // switch to register mode
                break;
            case 0:
                loginAnchPane.setVisible(true);
                regAnchPane.setVisible(false);
                isLogin = 1; // switch to login mode
                break;
        }

    }

    @FXML
    void togglePass(ActionEvent event) {
        switch(isLogin){
            case 1: // if we are in login mode right now

                break;
            case 0:

                break;
        }
        // this is way too complicated than it should be, why......
    }


    @FXML
    void initialize() {

        EventBus.getDefault().register(this);
            // REMEMBER: we only come to this window if we know the user is NOT logged in.
        loginAnchPane.setVisible(true);
        regAnchPane.setVisible(false);
        // set login anchor
        AnchorPane.setBottomAnchor(loginAnchPane, 58.0);
        AnchorPane.setTopAnchor(loginAnchPane, 58.0);
        AnchorPane.setRightAnchor(loginAnchPane, 89.0);
        AnchorPane.setLeftAnchor(loginAnchPane, 89.0);
        // set register anchor
        AnchorPane.setBottomAnchor(regAnchPane, 58.0);
        AnchorPane.setTopAnchor(regAnchPane, 58.0);
        AnchorPane.setRightAnchor(regAnchPane, 89.0);
        AnchorPane.setLeftAnchor(regAnchPane, 89.0);


/*
        Stage stage = (Stage) loginAnchPane.getScene().getWindow();
        stage.setWidth(400);
        stage.setHeight(500);
        ((Stage) loginAnchPane.getScene().getWindow()).resizableProperty().setValue(false);

*/

    }

}

