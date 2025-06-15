package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.swing.*;
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
    private Label port;

    @FXML
    private TextField  password;
    @FXML
    private PrimaryController ctlr;
    boolean guess = false;
    boolean type_Client = false;
    boolean type_Employee = false;
    boolean type_guess = false;//only to know if is the first time to jet the catalog
    int type_local = 0;//1 for store 1 ,2 for store 2 ,3 for store 3, 4 for all of them
    private LoginRegCheck user;
    @FXML
    void initialize() {

        EventBus.getDefault().register(this);
        System.out.println("connect_scene_Con_initialize");



    }

    @FXML
    void guess_enter(ActionEvent event)
    {
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
    public void setCatalogController(PrimaryController controller) {
        this.ctlr = controller;
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
            System.out.println("Processing as guest");
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_win.fxml"));
                    Parent root = loader.load();

                    CatalogController controller = loader.getController();
                    controller.set_sorting_type(event.getSort_type());
                    controller.set_type(0);
                    controller.setCatalogSorting(event.get_Sorted_flowers());
                    controller.setFlowersList_c(event.get_original_catalog());
                    App.getScene().setRoot(root);
                    if(!type_guess)//to know this is the first time
                    {
                        type_guess = true;
                        System.out.println("show the catalog first time as guest");
                        App.getStage().setWidth(800);
                        App.getStage().setHeight(750);
                        App.getStage().centerOnScreen();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        if(type_Client) {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_win.fxml"));
                    Parent root = loader.load();
                    CatalogController controller = loader.getController();
                    if(type_local==4)// network
                    {
                        controller.set_type(4);
                        controller.set_sorting_type(event.getSort_type());
                        controller.setFlowersList_c(event.get_original_catalog());
                        controller.set_isLogin(true);
                        controller.set_user(user);
                        controller.setCatalogSorting(event.get_Sorted_flowers());
                        System.out.println("this client is network ");

                    }
                    else
                    {
                        controller.set_type(type_local);
                        controller.set_sorting_type(event.getSort_type());
                        System.out.println("this client is for store: " + type_local);
                        controller.set_isLogin(true);
                        controller.set_user(user);
                        controller.setFlowersList_c(event.get_original_catalog());
                        controller.setCatalogSorting(event.get_Sorted_flowers());

                    }
                    App.getScene().setRoot(root);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        if(type_Employee)
        {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_employee.fxml"));
                    Parent root = loader.load();
                    CatalogController_employee controller = loader.getController();

                    if(type_local==4)// network
                    {
                        controller.set_type(type_local);
                        controller.set_sorting_type(event.getSort_type());
                        controller.set_isLogin(true);
                        controller.set_user(user);
                        controller.setFlowersList_c(event.get_original_catalog());
                        controller.setCatalogSorting(event.get_Sorted_flowers());
                        System.out.println("this employee is network ");
                    }
                    else
                    {
                        controller.set_type(type_local);
                        controller.set_isLogin(true);
                        controller.set_user(user);
                        controller.set_sorting_type(event.getSort_type());
                        controller.setFlowersList_c(event.get_original_catalog());
                        System.out.println("this employee is for store: " + type_local);
                        controller.setCatalogSorting(event.get_Sorted_flowers());
                    }
                    App.getScene().setRoot(root);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }


    }
    @Subscribe
    public void handleCatalogUpdate(Add_flower_event event)
    {
        System.out.println("enter event method");
        System.out.println(type_Client);
        System.out.println(type_Employee);
        System.out.println(type_local);
        System.out.println(guess);
        System.out.println(event.get_catalog_type());
        if(guess)
        {

            add_flower_and_update_catalog(event.get_flowers());
            return;
        }
        if(type_local == event.get_catalog_type() && type_local==1)
        {
            add_flower_and_update_catalog(event.get_flowers());
            return;
        }
        if(type_local == event.get_catalog_type() && type_local==2)
        {
            add_flower_and_update_catalog(event.get_flowers());
            return;
        }
        if(type_local == event.get_catalog_type() && type_local==3)
        {
            add_flower_and_update_catalog(event.get_flowers());
            return;
        }
        if(type_local == event.get_catalog_type() && type_local==4)
        {
            add_flower_and_update_catalog(event.get_flowers());
            return;
        }


    }
    public void add_flower_and_update_catalog(List<Flower> flowerList)
    {
        System.out.println("add flower_situation");
        System.out.println(type_Client);
        System.out.println(type_Employee);
        System.out.println(type_local);
        if(guess)
        {
            System.out.println("Processing as guest");
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_win.fxml"));
                    Parent root = loader.load();

                    CatalogController controller = loader.getController();
                    controller.set_type(0);
                    controller.setFlowersList_c(flowerList);
                    controller.setCatalogData(flowerList);
                    App.getScene().setRoot(root);
                    if(!type_guess)//to know this is the first time
                    {
                        type_guess = true;
                        System.out.println("show the catalog first time as guest");
                        App.getStage().setWidth(800);
                        App.getStage().setHeight(750);
                        App.getStage().centerOnScreen();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        if(type_Client) {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_win.fxml"));
                    Parent root = loader.load();
                    CatalogController controller = loader.getController();
                    if(type_local==4)// network
                    {
                        controller.set_type(4);
                        controller.setFlowersList_c(flowerList);
                        controller.set_isLogin(true);
                        controller.set_user(user);
                        controller.setCatalogData(flowerList);
                        System.out.println("this client is network ");

                    }
                    else
                    {

                        System.out.println("this client is for store: " + type_local);
                        controller.set_isLogin(true);
                        controller.set_user(user);
                        controller.set_type(type_local);
                        controller.setFlowersList_c(flowerList);
                        controller.setCatalogData(flowerList);

                    }
                    App.getScene().setRoot(root);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        if(type_Employee)
        {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_employee.fxml"));
                    Parent root = loader.load();
                    CatalogController_employee controller = loader.getController();

                    if(type_local==4)// network
                    {
                        controller.set_type(4);
                        controller.setFlowersList_c(flowerList);
                        controller.setCatalogData(flowerList);
                        controller.set_isLogin(true);
                        controller.set_user(user);
                        System.out.println("this employee is network ");
                    }
                    else
                    {

                        System.out.println("this employee is for store: " + type_local);
                        controller.set_type(type_local);
                        controller.setFlowersList_c(flowerList);
                        controller.set_isLogin(true);
                        controller.set_user(user);
                        controller.setCatalogData(flowerList);
                    }
                    App.getScene().setRoot(root);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_win.fxml"));
                    Parent root = loader.load();

                    CatalogController controller = loader.getController();
                    controller.setCatalogData(event.getUpdatedItems());
                    App.getScene().setRoot(root);
                    if(!type_guess)//to know this is the first time
                    {
                        type_guess = true;
                        System.out.println("show the catalog first time as guest");
                        App.getStage().setWidth(800);
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
                        if (loginRegCheck.isType())
                        {
                            user = loginRegCheck;
                            type_Employee = true;
                            System.out.println("type_Employee is true");
                            type_local=loginRegCheck.getStore();
                            System.out.println("the employee is for store "+type_local);
                            if(loginRegCheck.getIsLogin()==1)
                            {
                                Warning warning = new Warning("this user already in the system");
                                EventBus.getDefault().post(new WarningEvent(warning));
                                return;
                            }
                        }
                        if (!loginRegCheck.isType())
                        {
                            user = loginRegCheck;
                            type_Client = true;
                            System.out.println("type_Client is true");
                            type_local=loginRegCheck.getStore();
                            System.out.println("the user is mnoy to store "+type_local);
                            if(loginRegCheck.getIsLogin()==1)
                            {
                                Warning warning = new Warning("this user already in the system");
                                EventBus.getDefault().post(new WarningEvent(warning));
                                return;
                            }
                        }
                        change_user_login wrapper = new change_user_login(user,1);
                        try {
                            SimpleClient.getClient().sendToServer(wrapper);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> {
                            try {
                                FXMLLoader loader;
                                Parent root;

                                if (loginRegCheck.isType()) {
                                    loader = new FXMLLoader(getClass().getResource("catalog_employee.fxml"));
                                    root = loader.load();
                                    CatalogController_employee controller = loader.getController();
                                    List<Store> stores=event.getStores();
                                    if(type_local==4)// network
                                    {
                                        System.out.println("this employee is network ");
                                        controller.set_type(type_local);
                                        controller.set_isLogin(true);
                                        controller.set_user(loginRegCheck);
                                        controller.setCatalogData(event.getUpdatedItems());

                                    }
                                    else
                                    {
                                        Store store=stores.get(type_local-1);
                                        System.out.println("this employee is for store: " + store.getStoreName());
                                        controller.setFlowersList_c(store.getFlowersList());
                                        controller.set_isLogin(true);
                                        controller.set_user(loginRegCheck);
                                        controller.set_type(type_local);
                                        controller.setCatalogData(event.getUpdatedItems());
                                    }


                                }
                                else {
                                    loader = new FXMLLoader(getClass().getResource("catalog_win.fxml"));
                                    root = loader.load();
                                    CatalogController controller = loader.getController();
                                    List<Store> stores=event.getStores();
                                    if(type_local==4)// network
                                    {
                                        controller.set_type(type_local);
                                        System.out.println("this client is network ");
                                        controller.set_isLogin(true);
                                        controller.set_user(loginRegCheck);
                                        controller.setCatalogData(event.getUpdatedItems());

                                    }
                                    else
                                    {
                                        Store store=stores.get(type_local-1);
                                        System.out.println("this client is for store: " + store.getStoreName());
                                        controller.setFlowersList_c(store.getFlowersList());
                                        controller.set_type(type_local);
                                        controller.set_isLogin(true);
                                        controller.set_user(loginRegCheck);
                                        controller.setCatalogData(event.getUpdatedItems());

                                    }



                                }


                                App.getScene().setRoot(root);
                                App.getStage().setWidth(800);
                                App.getStage().setHeight(750);
                                App.getStage().centerOnScreen();
                                System.out.println("show the catalog as user/employee first time");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        return;
                    }
                }
                Warning warning = new Warning("Username or password doesn't match");
                EventBus.getDefault().post(new WarningEvent(warning));
            }

    }

