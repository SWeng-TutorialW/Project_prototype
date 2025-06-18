package il.cshaifasweng.OCSFMediatorExample.client;

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
