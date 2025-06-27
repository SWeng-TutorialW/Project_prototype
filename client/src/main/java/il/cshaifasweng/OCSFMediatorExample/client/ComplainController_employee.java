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
import javafx.scene.control.TableRow;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ComplainController_employee implements Initializable {

    @FXML
    private TableView<Complain> complains;

    @FXML
    private TableColumn<Complain, String> orderIdColumn;

    @FXML
    private TableColumn<Complain, String> orderPriceColumn;

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
        
        // Set up order ID column - extract from complaint text
        orderIdColumn.setCellValueFactory(cellData -> {
            String complaintText = cellData.getValue().getComplaint();
            String orderId = extractOrderId(complaintText);
            return new SimpleStringProperty(orderId);
        });
        
        // Set up order price column - extract from complaint text
        orderPriceColumn.setCellValueFactory(cellData -> {
            String complaintText = cellData.getValue().getComplaint();
            String orderPrice = extractOrderPrice(complaintText);
            return new SimpleStringProperty(orderPrice);
        });
        
        complaintColumn.setCellValueFactory(new PropertyValueFactory<>("complaint"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("client"));
        timeColumn.setCellValueFactory(cellData -> {
            LocalDateTime timestamp = cellData.getValue().getTimestamp();
            String formattedTime = timestamp.toString();
            return new SimpleStringProperty(formattedTime);
        });
        
        try {
            if (catalogController != null) { //TODO see how to make it different for employee
                complains.setVisible(false);
            }
            if (SimpleClient.getClient().isConnected())
                System.out.println("show_complain");
            SimpleClient.getClient().sendToServer("getComplaints");
        } catch (Exception e) {
            e.printStackTrace();

        }
        complains.setRowFactory(tv -> {
            TableRow<Complain> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    Complain selected = row.getItem();
                    openReplyWindow(selected);
                }
            });
            return row;
        });


    }
    
    private String extractOrderId(String complaintText) {
        if (complaintText == null) return "N/A";
        
        // Look for "Order #123" pattern
        Pattern pattern = Pattern.compile("Order #(\\d+)");
        Matcher matcher = pattern.matcher(complaintText);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        return "N/A";
    }
    
    private String extractOrderPrice(String complaintText) {
        if (complaintText == null) return "N/A";
        
        // Look for price pattern like "Price: $123.45" or "Total: $123.45"
        Pattern pattern = Pattern.compile("(?:Price|Total): \\$([\\d.]+)");
        Matcher matcher = pattern.matcher(complaintText);
        
        if (matcher.find()) {
            return "$" + matcher.group(1);
        }
        
        return "N/A";
    }
    
    private void openReplyWindow(Complain complain) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reply_complain.fxml"));
            Parent root = loader.load();

            ReplyComplainController controller = loader.getController();
            controller.setComplain(complain);


            Stage stage = new Stage();
            stage.setTitle("Reply to Complaint");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
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
