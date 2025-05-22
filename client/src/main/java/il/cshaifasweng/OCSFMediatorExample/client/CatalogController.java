package il.cshaifasweng.OCSFMediatorExample.client;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.CatalogItem;
import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class CatalogController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label catalogLbl;



    @FXML
    private AnchorPane catalogWin;

    @FXML
    private Label copyrightLBl;

    //

    @FXML
    private TableView<CatalogItem> catalogTbl;

    @FXML
    private TableColumn<CatalogItem, String> categoryCol;

    @FXML
    private TableColumn<CatalogItem, Integer> idCol;

    @FXML
    private TableColumn<CatalogItem, String> nameCol;

    @FXML
    private TableColumn<CatalogItem, Double> priceCol;

    @FXML
    private TableColumn<CatalogItem, Void> infoButtonCol;
    //

    @Subscribe
    public void handleCatalogUpdate(CatalogUpdateEvent event) {
        // Handle the catalog update event here
        // For example, update the catalog table with new data
        // catalogTbl.setItems(event.getUpdatedCatalog()); <-- pseudocode
        Platform.runLater(()-> {

            try{
                List<CatalogItem> itemsList = (List<CatalogItem>)event.getUpdatedItems();
                catalogTbl.setItems((ObservableList<CatalogItem>) itemsList);

                infoButtonCol.setCellFactory(column -> new TableCell<>() {

                    final Button btn = new Button("Info");
                    {
                    btn.setOnAction(e ->

                        {

                            CatalogItem item = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Item Info");
                            alert.setHeaderText("Item Information");      // Here is the window for information about the item
                            alert.setContentText("Name: " + item.getItemName() + "\n" +
                                    "Price: " + item.getItemPrice() + "\n" +
                                    "Category: " + item.getItemCategory());
                            alert.showAndWait();
                        });
                    }
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }

                });

            }catch(Exception e){
                e.printStackTrace();
            }});

    } // The runtime of this scares me

    @FXML
    void onCommitPrice(ActionEvent event){

        // send changes to the server...
        // and hopefully the server will update the database and return the result to us

    }

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        // so we can receive events SimpleClient.
        try {
            if (SimpleClient.getClient().isConnected())
                SimpleClient.getClient().sendToServer("#getCatalogTable");
        } catch (Exception e) {
            e.printStackTrace();



        }
    }


}
