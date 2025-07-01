package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
import javax.xml.catalog.Catalog;
import java.io.IOException;
import java.util.List;

import static il.cshaifasweng.OCSFMediatorExample.client.SimpleClient.client;

public class connect_scene_Con  {

    @FXML
    private Button Catalog;

    @FXML
    private TextField  user_name;

    @FXML
    private Button guess_btn;

    @FXML
    private ImageView image_1;

    @FXML
    private ImageView image_2;

    @FXML
    private Label ip_label;

    @FXML
    private CheckBox passCheckBox;
    @FXML
    private TextField passTxtB;
    @FXML
    private Label port;

    @FXML
    private PasswordField password;

    @FXML
    private Button registerBtn;

    @FXML
    private PrimaryController ctlr;



    boolean guess = false;
    boolean type_Client = false;
    boolean type_Employee = false;
    boolean type_guess = false;//only to know if is the first time to jet the catalog
    int type_local = 0;//1 for store 1 ,2 for store 2 ,3 for store 3, 4 for all of them
    private LoginRegCheck user;
    public void setCatalogController(PrimaryController controller) {
        this.ctlr = controller;
    }
    public void set_user(LoginRegCheck user) {
        this.user = user;
    }
    public void  set_guest(boolean value)
    {
        guess=value;
    }
    public void set_type_client(boolean type_client) {
        type_Client=type_client;
    }
    public void set_type_local(int type_guess) {
        type_local=type_guess;
    }
    public void set_type_employee(boolean type_employee) {
        type_Employee=type_employee;
    }
    private  CatalogController catalogController;
    private CatalogController_employee employeeController;
    private static Stage registerStageInstance = null;
    public static boolean isRegStageOpen() {
        return registerStageInstance != null && registerStageInstance.isShowing();
    }
    public static void setRegStageInstance(Stage stage) {
        registerStageInstance = stage;
        if (registerStageInstance != null) {
            registerStageInstance.setOnHidden(e -> registerStageInstance = null);
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
    public static void closeAllPopups() {
        if (isRegStageOpen()) registerStageInstance.close();
        if (isPaymentStageOpen()) paymentStageInstance.close();
    }


    @FXML
    void initialize() {

        if (EventBus.getDefault().isRegistered(this)) {
            System.out.println("already registered");
        } else {
            EventBus.getDefault().register(this);
        }
    }

    }


    @FXML
    void show_pass(ActionEvent event) {
        if (passCheckBox.isSelected()) {
            password.setVisible(false);
            passTxtB.setVisible(true);
            passTxtB.setText(password.getText());
        } else {
            password.setVisible(true);
            passTxtB.setVisible(false);
            password.setText(passTxtB.getText());
        }
    }
    @FXML
    void show_reg(ActionEvent event) {
        RegistrationController.cameFromConnect = true;
        if (isRegStageOpen()) {
            Warning warning = new Warning("Registration window is already open.");
            EventBus.getDefault().post(new WarningEvent(warning));
            RegistrationController.cameFromConnect = true;
            registerStageInstance.toFront();
            registerStageInstance.requestFocus();
            return;
        }
        if (isPaymentStageOpen()) {
            Warning warning = new Warning("Cannot open registration while payment window is open.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration_screen.fxml"));
            RegistrationController regController = fxmlLoader.getController();
            fxmlLoader.setControllerFactory(var -> {
                RegistrationController controller = new RegistrationController();
                controller.gotFromConnectScene = true;
                controller.setController(this);
                return controller;
            });
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Create New Account");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            setRegStageInstance(stage);

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void guess_enter(ActionEvent event)
    {
        connect_scene_Con.closeAllPopups();
        SimpleClient.isGuest = true;
        guess = true;
        try {
            if (SimpleClient.getClient().isConnected()) {
                System.out.println("show_cata_as_guess");
                SimpleClient.getClient().sendToServer("getCatalogTable_guest");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    public void show_cata(ActionEvent actionEvent) {
        try {
            if (SimpleClient.getClient().isConnected()) {
                System.out.println("show_cata");
                SimpleClient.getClient().sendToServer("getCatalogTable_login");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    @Subscribe
    public void handleCatalogUpdate(catalog_sort_event event)
    {
        System.out.println("enter type sorting");
        System.out.println(type_Client);
        System.out.println(type_Employee);
        System.out.println(type_local);
        if(guess)
        {
            catalogController.set_sorting_type(event.getSort_type());
            catalogController.setCatalogSorting(event.get_Sorted_flowers());
            return;
        }
        if (type_Client)
        {
            catalogController.set_sorting_type(event.getSort_type());
            catalogController.setCatalogSorting(event.get_Sorted_flowers());
            return;
        }

        if(type_Employee)
        {
            employeeController.set_sorting_type(event.getSort_type());
            employeeController.setCatalogSorting(event.get_Sorted_flowers());
            return;
        }

    }



    @Subscribe
    public void handleCatalogUpdate(CatalogUpdateEvent event)/// /  this method only for the first time to get the catalog
    {
        System.out.println(type_Client);
        System.out.println(type_Employee);
        System.out.println(type_local);
        if(guess)
        {
            System.out.println("Processing as guest");
            Platform.runLater(() -> {
                try {
                    connect_scene_Con.closeAllPopups();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_win.fxml"));
                    Parent root = loader.load();

                    CatalogController controller = loader.getController();
                    controller.setCatalogData(event.getUpdatedItems());
                    catalogController=controller;
                    controller.setController(this);
                    App.getScene().setRoot(root);
                    if(!type_guess)//to know this is the first time
                    {
                        type_guess = true;
                        System.out.println("show the catalog first time as guest");
                        App.getStage().setWidth(980);
                        App.getStage().setHeight(750);
                        App.getStage().centerOnScreen();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
            System.out.println("Processing as regular login");
            String user_Name = user_name.getText().trim();
            String passWord = password.getText().trim();
            System.out.println("USERNAME: " + user_Name);
            System.out.println("PASSWORD: " + passWord);
            List<LoginRegCheck> users = event.getUsers();
                for (LoginRegCheck loginRegCheck : users) {
                    user = loginRegCheck;
                    if (loginRegCheck.getUsername().equals(user_Name) && loginRegCheck.getPassword().equals(passWord)) {
                        CatalogController.set_user(user); // I forgot to add this. Now when logging in, the SimpleClient.currentUser isn't NULL. :)
                        System.out.println("User Logged-In: " + loginRegCheck.getUsername());
                        if (loginRegCheck.isType())
                        {
                            if(loginRegCheck.getIsLogin()==1)
                            {
                                Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Login Error");
                                    alert.setHeaderText("Already Logged In");
                                    alert.setContentText("This user is already logged in elsewhere. Please log out from other devices first.");
                                    alert.showAndWait();
                                });
                                user = null;
                                return;
                            }
                            user = loginRegCheck;
                            type_Employee = true;
                            System.out.println("type_Employee is true");
                            type_local=loginRegCheck.getStore();
                            System.out.println("the employee is for store "+type_local);

                        }
                        if (!loginRegCheck.isType())
                        {
                            if(loginRegCheck.getIsLogin()==1)
                            {
                                Platform.runLater(() -> {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Login Error");
                                    alert.setHeaderText("Already Logged In");
                                    alert.setContentText("This user is already logged in elsewhere. Please log out from other devices first.");
                                    alert.showAndWait();
                                });
                                user = null;
                                return;

                            }
                            user = loginRegCheck;
                            type_Client = true;
                            System.out.println("type_Client is true");
                            type_local=loginRegCheck.getStore();
                            System.out.println("the user is mnoy to store "+type_local);
                            SimpleClient.isGuest = false; // Yarden added this


                        }
                        change_user_login wrapper = new change_user_login(user,1);
                        try {
                            SimpleClient.getClient().sendToServer(wrapper);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        loginRegCheck.setIsLogin(1);
                        Platform.runLater(() -> {
                            try {
                                FXMLLoader loader;
                                Parent root;

                                if (loginRegCheck.isType()) {
                                    connect_scene_Con.closeAllPopups();
                                    loader = new FXMLLoader(getClass().getResource("catalog_employee.fxml"));
                                    root = loader.load();
                                    CatalogController_employee controller = loader.getController();
                                    List<Store> stores=event.getStores();
                                    
                                    System.out.println("DEBUG: Stores list size: " + (stores != null ? stores.size() : "NULL"));
                                    
                                    if(type_local==4)// network
                                    {
                                        System.out.println("this employee is network ");
                                        controller.set_type(type_local);
                                        controller.set_isLogin(true);
                                        controller.set_user(loginRegCheck);
                                        controller.setStoresList(stores);
                                        controller.setCatalogData(event.getUpdatedItems());
                                        employeeController=controller;

                                    }
                                    else
                                    {
                                        if (stores == null || stores.isEmpty()) {
                                            System.err.println("ERROR: Stores list is empty or null for employee!");
                                            Warning warning = new Warning("Database error: No stores available. Please contact administrator.");
                                            EventBus.getDefault().post(new WarningEvent(warning));
                                            return;
                                        }
                                        
                                        if (type_local < 1 || type_local > stores.size()) {
                                            System.err.println("ERROR: Invalid store index: " + type_local + " for stores size: " + stores.size());
                                            Warning warning = new Warning("Database error: Invalid store assignment. Please contact administrator.");
                                            EventBus.getDefault().post(new WarningEvent(warning));
                                            return;
                                        }
                                        
                                        Store store=stores.get(type_local-1);
                                        System.out.println("this employee is for store: " + store.getStoreName());
                                        controller.setFlowersList_c(store.getFlowersList());
                                        controller.set_isLogin(true);
                                        controller.set_user(loginRegCheck);
                                        controller.set_type(type_local);
                                        controller.setStoresList(stores);
                                        controller.setCatalogData(event.getUpdatedItems());
                                        employeeController=controller;
                                    }

                                }
                                else {
                                    connect_scene_Con.closeAllPopups();
                                    loader = new FXMLLoader(getClass().getResource("catalog_win.fxml"));
                                    root = loader.load();
                                    CatalogController controller = loader.getController();
                                    List<Store> stores=event.getStores();
                                    
                                    System.out.println("DEBUG: Stores list size: " + (stores != null ? stores.size() : "NULL"));
                                    
                                    if(type_local==4)// network
                                    {
                                        controller.set_type(type_local);
                                        System.out.println("this client is network ");
                                        controller.set_isLogin(true);
                                        controller.set_user(loginRegCheck);
                                        controller.setStoresList(stores);
                                        controller.setCatalogData(event.getUpdatedItems());
                                        catalogController=controller;
                                        controller.setController(this);

                                    }
                                    else
                                    {
                                        if (stores == null || stores.isEmpty()) {
                                            System.err.println("ERROR: Stores list is empty or null for client!");
                                            Warning warning = new Warning("Database error: No stores available. Please contact administrator.");
                                            EventBus.getDefault().post(new WarningEvent(warning));
                                            return;
                                        }
                                        
                                        if (type_local < 1 || type_local > stores.size()) {
                                            System.err.println("ERROR: Invalid store index: " + type_local + " for stores size: " + stores.size());
                                            Warning warning = new Warning("Database error: Invalid store assignment. Please contact administrator.");
                                            EventBus.getDefault().post(new WarningEvent(warning));
                                            return;
                                        }
                                        
                                        Store store=stores.get(type_local-1);
                                        System.out.println("this client is for store: " + store.getStoreName());
                                        controller.setFlowersList_c(store.getFlowersList());
                                        controller.set_type(type_local);
                                        controller.set_isLogin(true);
                                        controller.set_user(loginRegCheck);
                                        controller.setStoresList(stores);
                                        controller.setCatalogData(event.getUpdatedItems());
                                        catalogController=controller;
                                        controller.setController(this);

                                    }
                                }
                                if(catalogController!=null){
                                    App.getScene().setRoot(root);
                                    App.getStage().setWidth(1020);
                                    App.getStage().setHeight(750);
                                    App.getStage().centerOnScreen();
                                } else {
                                    App.getScene().setRoot(root);
                                    App.getStage().setWidth(750);
                                    App.getStage().setHeight(750);
                                    App.getStage().centerOnScreen();
                                }

                                System.out.println("show the catalog as user/employee first time");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        return;
                    }
                }
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid Credentials");
                alert.setContentText("Username or password is incorrect. Please check your credentials and try again.");
                alert.showAndWait();
            });

    }

    @Subscribe
    public void handleLoginResponse(String response) {
        if (response.equals("#loginFailed")) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid Credentials");
                alert.setContentText("Username or password is incorrect. Please check your credentials and try again.");
                alert.showAndWait();
            });
        }
    }

}

