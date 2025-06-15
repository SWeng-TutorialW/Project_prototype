package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Complain;

import il.cshaifasweng.OCSFMediatorExample.entities.ComplainUpdateEvent;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;



public class ComplainController_employee implements Initializable {

    @FXML
    private TableView<Complain> complains;

    @FXML
    private TableColumn<Complain, String> complaintColumn;

    @FXML
    private TableColumn<Complain, String> nameColumn;

    @FXML
    private TableColumn<Complain, String> timeColumn;

    private CatalogController_employee catalogController;

    public void setCatalogController(CatalogController_employee controller) {
        this.catalogController = controller;
    }

    @FXML
    public void initialize(URL url, ResourceBundle resources) {
        EventBus.getDefault().register(this);
        System.out.println("complain handler initialized");
        complaintColumn.setCellValueFactory(new PropertyValueFactory<>("complaint"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        timeColumn.setCellValueFactory(cellData -> {
            LocalDateTime timestamp = cellData.getValue().getTimestamp();
            String formattedTime = timestamp.toString();
            return new SimpleStringProperty(formattedTime);
        });
        try {
            if (SimpleClient.getClient().isConnected())
                System.out.println("show_complain");
            SimpleClient.getClient().sendToServer("getComplaints");
        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    @Subscribe
    public void handleComplainUpdate(ComplainUpdateEvent event)
    {
       System.out.println("updating complaint list debuggg");
       List<Complain> complainList = event.getUpdatedItems();
        // Run on JavaFX thread to update UI
        Platform.runLater(() -> {
            complains.getItems().setAll(complainList);
        });
    }


}
