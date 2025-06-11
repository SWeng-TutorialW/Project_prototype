package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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
    private TextField password;
    @FXML
    private PrimaryController ctlr;

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);

    }

    @FXML
    void guess_enter(ActionEvent event)
    {
        SimpleClient.isGuest = true;
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
    public void handleCatalogUpdate(CatalogUpdateEvent event)
    {
        if(SimpleClient.isGuest)
        {
            System.out.println("Processing as guest");
            // SimpleClient.isGuest = false; // Irreverent I believe.
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_win.fxml"));
                    Parent root = loader.load();

                    CatalogController controller = loader.getController();
                    controller.setCatalogData(event.getUpdatedItems());

                    App.getScene().setRoot(root);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        else
        {
            System.out.println("Processing as regular login");
            String user_Name = user_name.getText().trim();
            String passWord = password.getText().trim();
            if(user_Name.isEmpty() || passWord.isEmpty())
                return;
            System.out.println("USERNAME: " + user_Name);
            System.out.println("PASSWORD: " + passWord);
            List<LoginRegCheck> users = event.getUsers();

            for (LoginRegCheck loginRegCheck : users) {
                if (loginRegCheck.getUsername().equals(user_Name) && loginRegCheck.getPassword().equals(passWord))
                {
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader;
                            Parent root;

                            if (loginRegCheck.isType()) {
                                loader = new FXMLLoader(getClass().getResource("catalog_employee.fxml"));
                                root = loader.load();
                                CatalogController_employee controller = loader.getController();
                                controller.setCatalogData(event.getUpdatedItems());
                            } else {
                                loader = new FXMLLoader(getClass().getResource("catalog_win.fxml"));
                                root = loader.load();
                                CatalogController controller = loader.getController();
                                controller.setCatalogData(event.getUpdatedItems());
                            }

                            App.getScene().setRoot(root);

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

}