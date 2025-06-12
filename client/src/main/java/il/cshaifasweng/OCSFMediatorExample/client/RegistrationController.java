package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.Objects;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


/**
 * Sample Skeleton for 'registration_screen.fxml' Controller Class
 */

public class RegistrationController {

    //private int isLogin=1;
//
//   // @FXML // fx:id="loginAnchPane"
//   // private AnchorPane loginAnchPane; // Value injected by FXMLLoader
//
//   //@FXML // fx:id="loginErrMsgLbl"
//    //private Label loginErrMsgLbl; // Value injected by FXMLLoader
//
//    @FXML // fx:id="loginWin"
//    private AnchorPane loginWin; // Value injected by FXMLLoader
//
//    @FXML // fx:id="passLbl"
//    private Label passLbl; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regAnchPane"
//    private AnchorPane regAnchPane; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regBtn"
//    private Button regBtn; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regEmailLbl"
//    private Label regEmailLbl; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regEmailTxtB"
//    private TextField regEmailTxtB; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regErrMsgLbl"
//    private Label regErrMsgLbl; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regLbl"
//    private Label regLbl; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regPassConfLbl"
//    private Label regPassConfLbl; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regPassConfTxtB"
//    private PasswordField regPassConfTxtB; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regPassLbl"
//    private Label regPassLbl; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regPassTxtB"
//    private PasswordField regPassTxtB; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regShowPassCB"
//    private CheckBox regShowPassCB; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regUserLbl"
//    private Label regUserLbl; // Value injected by FXMLLoader
//
//    @FXML // fx:id="regUserTxtB"
//    private TextField regUserTxtB; // Value injected by FXMLLoader
//
//    @FXML // fx:id="usernameLbl"
//    private Label usernameLbl; // Value injected by FXMLLoader

        @FXML // fx:id="loginWin"
        private AnchorPane loginWin; // Value injected by FXMLLoader

        @FXML // fx:id="regAnchPane"
        private AnchorPane regAnchPane; // Value injected by FXMLLoader

        @FXML // fx:id="regBtn"
        private Button regBtn; // Value injected by FXMLLoader

        @FXML // fx:id="regEmailLbl"
        private Label regEmailLbl; // Value injected by FXMLLoader

        @FXML // fx:id="regEmailTxtB"
        private TextField regEmailTxtB; // Value injected by FXMLLoader

        @FXML // fx:id="regErrMsgLbl"
        private Label regErrMsgLbl; // Value injected by FXMLLoader

        @FXML // fx:id="regLbl"
        private Label regLbl; // Value injected by FXMLLoader

        @FXML // fx:id="regPassConfLbl"
        private Label regPassConfLbl; // Value injected by FXMLLoader

        @FXML // fx:id="regPassConfTxtB"
        private PasswordField regPassConfTxtB; // Value injected by FXMLLoader

        @FXML // fx:id="regPassLbl"
        private Label regPassLbl; // Value injected by FXMLLoader

        @FXML // fx:id="regPassTxtB"
        private PasswordField regPassTxtB; // Value injected by FXMLLoader

        @FXML // fx:id="regShowPassCB"
        private CheckBox regShowPassCB; // Value injected by FXMLLoader

        @FXML // fx:id="regUserLbl"
        private Label regUserLbl; // Value injected by FXMLLoader

        @FXML // fx:id="regUserTxtB"
        private TextField regUserTxtB; // Value injected by FXMLLoader

        @FXML // fx:id="regPassConfVisibleTxtB"
        private TextField  regPassConfVisibleTxtB; // Value injected by FXMLLoader

        @FXML // fx:id="regPassVisibleTxtB"
        private TextField  regPassVisibleTxtB; // Value injected by FXMLLoader


    @FXML
    void RegToSys(MouseEvent event) {
        String confPass = regPassConfTxtB.getText();
        String confPass2 = regPassConfVisibleTxtB.getText();
        String email = regEmailTxtB.getText();
        String regUser = regUserTxtB.getText();
        String regPass = regPassTxtB.getText();
        String regPass2 = regPassVisibleTxtB.getText();
        if ( ( Objects.equals(confPass, regPass) || Objects.equals(confPass2, regPass2) ) && (regUser.length() >= 4 && regPass.length() >= 4 && email.length() >= 4)) {
            try {
                regErrMsgLbl.setVisible(false);
                SimpleClient.getClient().sendToServer(new LoginRegCheck(regUser, regPass, email, 0));

                return;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        regErrMsgLbl.setText("Details Not in Correct Format");
        regErrMsgLbl.textFillProperty().set(Paint.valueOf("red"));
        regErrMsgLbl.setVisible(true);
    }

    @FXML
    void togglePass(ActionEvent event) {
        if (regShowPassCB.isSelected()) {
            regPassVisibleTxtB.setText(regPassTxtB.getText());
            regPassConfVisibleTxtB.setText(regPassConfTxtB.getText());
            regPassVisibleTxtB.setVisible(true);
            regPassTxtB.setVisible(false);
            regPassConfVisibleTxtB.setVisible(true);
            regPassConfTxtB.setVisible(false);
        } else {
            regPassTxtB.setText(regPassVisibleTxtB.getText());
            regPassConfTxtB.setText(regPassConfVisibleTxtB.getText());
            regPassTxtB.setVisible(true);
            regPassVisibleTxtB.setVisible(false);
            regPassConfTxtB.setVisible(true);
            regPassConfVisibleTxtB.setVisible(false);
        }
    }

    @Subscribe
    public void onRegResult(String msg) {

        if (msg.startsWith("#login/reg_ok")) {
            System.out.println("Registration OK - switching to Connect Scene");
            SimpleClient.isGuest = false;
            EventBus.getDefault().unregister(this);
            // Close the window (because we logged in)
            Platform.runLater(() -> {
                try {
                Stage stage = (Stage) regAnchPane.getScene().getWindow();
                stage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("connect_scene.fxml"));
                Parent root = loader.load();
                App.getScene().setRoot(root);
                } catch (IOException e) {
                e.printStackTrace();
                }
            });
        }
        else if(msg.startsWith("#user_exists")){
            Platform.runLater(()->{regErrMsgLbl.setText("User already exists");
                regErrMsgLbl.setVisible(true);
                regErrMsgLbl.textFillProperty().set(Paint.valueOf("red"));});
        }
}


@FXML
void initialize() {
    EventBus.getDefault().register(this);
    System.out.println("Registered to EventBus - waiting for reg result");
    // REMEMBER: we only come to this window if we know the user is NOT logged in.
    regAnchPane.setVisible(true);
    // set register anchor
    AnchorPane.setBottomAnchor(regAnchPane, 58.0);
    AnchorPane.setTopAnchor(regAnchPane, 58.0);
    AnchorPane.setRightAnchor(regAnchPane, 89.0);
    AnchorPane.setLeftAnchor(regAnchPane, 89.0);
}

}
