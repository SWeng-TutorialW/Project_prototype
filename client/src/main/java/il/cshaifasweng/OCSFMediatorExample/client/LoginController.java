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
        String username = userTxtB.getText();
        String password = passTxtB.getText();
        
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            Warning warning = new Warning("Please enter both username and password");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        for (LoginRegCheck loginRegCheck : users) {
            if(loginRegCheck.getUsername().equals(username) && loginRegCheck.getPassword().equals(password))
            {
                change_user_login wrapper = new change_user_login(loginRegCheck,1);
                try {
                    SimpleClient.getClient().sendToServer(wrapper);
                    System.out.println("Login successful for user: " + username);
                    
                    // Set up the catalog controller with user info
                    if (con != null) {
                        con.set_user(loginRegCheck);
                        con.set_type(loginRegCheck.getStore());
                        System.out.println("Catalog controller set up for user store: " + loginRegCheck.getStore());
                    } else {
                        System.err.println("Catalog controller is null!");
                    }
                    
                    // Close login window and show catalog
                    Stage stage = (Stage) loginAnchPane.getScene().getWindow();
                    stage.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                    Warning warning = new Warning("Error connecting to server");
                    EventBus.getDefault().post(new WarningEvent(warning));
                }
                return;
            }
        }
        
        // If we get here, login failed
        Warning warning = new Warning("Username or password doesn't match");
        EventBus.getDefault().post(new WarningEvent(warning));
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