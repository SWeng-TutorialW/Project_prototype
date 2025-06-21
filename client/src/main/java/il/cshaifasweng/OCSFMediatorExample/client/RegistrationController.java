package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.IOException;
import java.util.List;

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

    private static LoginRegCheck user;
    public LoginRegCheck getUser() { return user; }
    public void setUser(LoginRegCheck user) { RegistrationController.user = user; }

    int store = -1;
    boolean is_yearly_subscription = false;
    List<LoginRegCheck> users;

    private CatalogController catalogController;
    public void setCatalogController(CatalogController controller) { this.catalogController = controller; }

    private connect_scene_Con con;
    public void setController(connect_scene_Con controller) { con = controller; }

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
        if (is_yearly_subscription) {
            if (userId == null || !userId.matches("\\d{9}")) {
                return "ID must be exactly 9 digits";
            }
        }
        return null;
    }


    @FXML
    void RegToSys(MouseEvent event) throws IOException {
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

        int isLogin = con != null ? 0 : 1;
        LoginRegCheck new_user = new LoginRegCheck(regUser, regPass, email, isLogin, false, store, phoneNumber, fullName, userId, false);

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
        regAnchPane.setVisible(true);
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
