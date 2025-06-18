package il.cshaifasweng.OCSFMediatorExample.client;


import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.scene.Node;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;


/**
 * Sample Skeleton for 'registration_screen.fxml' Controller Class
 */

public class RegistrationController {

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

    @FXML // fx:id="regPassConfVisibleTxtB"
    private TextField regPassConfVisibleTxtB; // Value injected by FXMLLoader

    @FXML // fx:id="regPassLbl"
    private Label regPassLbl; // Value injected by FXMLLoader

    @FXML // fx:id="regPassTxtB"
    private PasswordField regPassTxtB; // Value injected by FXMLLoader

    @FXML // fx:id="regPassVisibleTxtB"
    private TextField regPassVisibleTxtB; // Value injected by FXMLLoader

    @FXML // fx:id="regShowPassCB"
    private CheckBox regShowPassCB; // Value injected by FXMLLoader

    @FXML // fx:id="regUserLbl"
    private Label regUserLbl; // Value injected by FXMLLoader

    @FXML // fx:id="regUserTxtB"
    private TextField regUserTxtB; // Value injected by FXMLLoader

    @FXML // fx:id="registerWin"
    private AnchorPane registerWin; // Value injected by FXMLLoader

    @FXML // fx:id="selectStoreCB"
    private ComboBox<String> select_account_type;

    @FXML // fx:id="selectStoreLbl"
    private Label selectStoreLbl; // Value injected by FXMLLoader
    @FXML
    private ComboBox<String> select_store;

    @FXML
    private Label select_store_label;
    int store=-1;
    boolean is_yearly_subscription=false;
    List<LoginRegCheck> users ;
    @FXML
    private Label credit_Card;

    @FXML
    private PasswordField credit_card_box;

    @FXML
    private Label id;

    @FXML
    private PasswordField id_text;
    private CatalogController catalogController;
    public void setCatalogController(CatalogController controller) {
        this.catalogController = controller;
    }
    private connect_scene_Con con;
    public void setController(connect_scene_Con controller) {
        con = controller;
    }





    @FXML
    void RegToSys(MouseEvent event) throws IOException {
        String confPass = regPassConfTxtB.getText();
        String confPass2 = regPassConfVisibleTxtB.getText();
        String email = regEmailTxtB.getText();
        String regUser = regUserTxtB.getText();
        String regPass = regPassTxtB.getText();
        String regPass2 = regPassVisibleTxtB.getText();

        if (catalogController != null) {
            if (isTextFieldEmpty(regPassTxtB) || isTextFieldEmpty(regEmailTxtB) || isTextFieldEmpty(regUserTxtB) || isTextFieldEmpty(regPassConfTxtB)) {
                Warning warning = new Warning("Please fill in all the fields");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }
            if (!regPass.equals(confPass)) {
                Warning warning = new Warning("passwords do not match");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }
            for (LoginRegCheck user : users) {
                if (user.getUsername().equals(regUser)) {
                    Warning warning = new Warning("Username already exists");
                    EventBus.getDefault().post(new WarningEvent(warning));
                    return;
                }
            }
            if (regUser.length() <= 4 || email.length() <= 4 ||
                    regPass.length() <= 4 || confPass.length() <= 4) {
                Warning warning = new Warning("Each field must be longer than 4 characters");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }
            if (!is_yearly_subscription) {
                if (store == -1) {
                    Warning warning = new Warning("you need to select a store");
                    EventBus.getDefault().post(new WarningEvent(warning));
                    return;
                }
                LoginRegCheck new_user = new LoginRegCheck(regUser, regPass, email, 1, false, store);
                SimpleClient.getClient().sendToServer(new_user);
                catalogController.set_user(new_user);
                catalogController.set_type(store);
            }
            if (is_yearly_subscription) {
                if (id_text.getText().length() <= 9) {
                    Warning warning = new Warning("ID must BE 9 characters");
                    EventBus.getDefault().post(new WarningEvent(warning));
                    return;
                }
                if (credit_card_box.getText().length() <= 16) {
                    Warning warning = new Warning("Credit card must BE 16 characters");
                    EventBus.getDefault().post(new WarningEvent(warning));
                    return;
                }
                String new_user_id = id_text.getText();
                String new_user_credit = credit_card_box.getText();
                LoginRegCheck new_user = new LoginRegCheck(regUser, regPass, email, 1, false, store, is_yearly_subscription);
                catalogController.set_user(new_user);
                catalogController.set_type(4);
                SimpleClient.getClient().sendToServer(new_user);
            }
        } else
        {
            if (isTextFieldEmpty(regPassTxtB) || isTextFieldEmpty(regEmailTxtB) || isTextFieldEmpty(regUserTxtB) || isTextFieldEmpty(regPassConfTxtB)) {
                Warning warning = new Warning("Please fill in all the fields");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }
            if (!regPass.equals(confPass)) {
                Warning warning = new Warning("passwords do not match");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }
            for (LoginRegCheck user : users) {
                if (user.getUsername().equals(regUser)) {
                    Warning warning = new Warning("Username already exists");
                    EventBus.getDefault().post(new WarningEvent(warning));
                    return;
                }
            }
            if (regUser.length() <= 4 || email.length() <= 4 ||
                    regPass.length() <= 4 || confPass.length() <= 4) {
                Warning warning = new Warning("Each field must be longer than 4 characters");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }
            if (!is_yearly_subscription) {
                if (store == -1) {
                    Warning warning = new Warning("you need to select a store");
                    EventBus.getDefault().post(new WarningEvent(warning));
                    return;
                }
                LoginRegCheck new_user = new LoginRegCheck(regUser, regPass, email, 0, false, store);
                SimpleClient.getClient().sendToServer(new_user);

            }
            if (is_yearly_subscription) {
                if (id_text.getText().length() <= 9) {
                    Warning warning = new Warning("ID must BE 9 characters");
                    EventBus.getDefault().post(new WarningEvent(warning));
                    return;
                }
                if (credit_card_box.getText().length() <= 16) {
                    Warning warning = new Warning("Credit card must BE 16 characters");
                    EventBus.getDefault().post(new WarningEvent(warning));
                    return;
                }
                String new_user_id = id_text.getText();
                String new_user_credit = credit_card_box.getText();
                LoginRegCheck new_user = new LoginRegCheck(regUser, regPass, email, 0, false, store, is_yearly_subscription);
                SimpleClient.getClient().sendToServer(new_user);
            }

        }

        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    void selected_account(ActionEvent event)
    {
        String account_type = select_account_type.getValue();
        if(account_type.equals("Store"))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Store Subscription");
            alert.setHeaderText("What is a Store Subscription?");
            alert.setContentText("A store subscription allows Costumer to buy only for specific Store");
            alert.showAndWait();
            select_store_label.setVisible(true);
            select_store.setVisible(true);

        }
        if(account_type.equals("Network"))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Network Subscription");
            alert.setHeaderText("What is a Network Subscription?");
            alert.setContentText("A network subscription allows customers to shop in all of our stores.");
            store=4;
            alert.showAndWait();
            select_store_label.setVisible(false);
            select_store.setVisible(false);
            id.setVisible(false);
            credit_Card.setVisible(false);
            id_text.setVisible(false);
            credit_card_box.setVisible(false);

        }
        if(account_type.equals("Yearly Subscription"))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Yearly Subscription");
            alert.setHeaderText("What is a Yearly Subscription?");
            alert.setContentText("A yearly subscription allows customers to shop in all of our stores. " +
                    "It costs 100 shekels and grants a 10% discount on every purchase above 50 shekels.");
            store=4;
            is_yearly_subscription=true;
            alert.showAndWait();
            select_store_label.setVisible(false);
            select_store.setVisible(false);
            id.setVisible(true);
            credit_Card.setVisible(true);
            id_text.setVisible(true);
            credit_card_box.setVisible(true);

        }


    }
    @FXML
    void select_store(ActionEvent event)
    {

        String selectStoreValue = select_store.getValue();
        if(selectStoreValue.equals("lilach_Haifa"))
        {
            store=1;
            is_yearly_subscription=false;
        }
        if(selectStoreValue.equals("lilach_Krayot"))
        {
            store=2;
            is_yearly_subscription=false;

        }
        if(selectStoreValue.equals("lilach_Nahariyya"))
        {
            store=3;
            is_yearly_subscription=false;

        }

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

    @FXML
    void initialize() throws IOException{
        EventBus.getDefault().register(this);
        System.out.println("Registered to EventBus - waiting for reg result");
        // REMEMBER: we only come to this window if we know the user is NOT logged in.
        regAnchPane.setVisible(true);
        select_account_type.getItems().addAll("Store", "Network", "Yearly Subscription");
        select_store.getItems().addAll("lilach_Haifa", "lilach_Krayot", "lilach_Nahariyya");
        SimpleClient.getClient().sendToServer("asks_for_users");


        // set register anchor
        AnchorPane.setBottomAnchor(regAnchPane, 58.0);
        AnchorPane.setTopAnchor(regAnchPane, 58.0);
        AnchorPane.setRightAnchor(regAnchPane, 89.0);
        AnchorPane.setLeftAnchor(regAnchPane, 89.0);
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
