package il.cshaifasweng.OCSFMediatorExample.client;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
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
    private TableView<Flower> catalogTbl;

    @FXML
    private TableColumn<Flower, String> categoryCol;

    @FXML
    private TableColumn<Flower, Integer> idCol;

    @FXML
    private TableColumn<Flower, String> nameCol;

    @FXML
    private TableColumn<Flower, Double> priceCol;

    @FXML
    private TableColumn<Flower, Void> infoButtonCol;
    //
    public void setCatalogData(List<Flower> flowerList) {
        ObservableList<Flower> observableList = javafx.collections.FXCollections.observableArrayList(flowerList);

        idCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("flowerName"));
        priceCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("flowerPrice"));
        categoryCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("flowerType"));


        Platform.runLater(()-> {

            try{
                List<Flower> itemsList = flowerList;
                catalogTbl.setItems(observableList);

                infoButtonCol.setCellFactory(column -> new TableCell<>() {

                    final Button btn = new Button("Info");
                    {
                        btn.setOnAction(e ->

                        {

                            Flower item = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Item Info");
                            alert.setHeaderText("Item Information");      // Here is the window for information about the item
                            alert.setContentText("Name: " + item.getFlowerName() + "\n" +
                                    "Price: " + item.getFlowerPrice() + "\n" +
                                    "Category: " + item.getFlowerType());
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




        System.out.println("Received flowers: " + flowerList.size());
        for (Flower f : flowerList) {
            System.out.println(f.getFlowerName() + ", " + f.getFlowerPrice());
        }
    }
    // The runtime of this scares me





    @FXML
    void onCommitPrice(ActionEvent event){

        // send changes to the server...
        // and hopefully the server will update the database and return the result to us

    }

    @FXML
    void initialize() {
        System.out.println("CatalogController initialized");
        // EventBus.getDefault().register(this);
        // so we can receive events SimpleClient.

    }


    public void commitPrice(TableColumn.CellEditEvent<Flower, Double> event) {
        Flower flower = event.getRowValue();
        Double newPrice = event.getNewValue();
        flower.setFlowerPrice(newPrice);


    }
}
/*
 @Subscribe
    public void handleCatalogUpdate(CatalogUpdateEvent event) {
        // Handle the catalog update event here
        // For example, update the catalog table with new data
        // catalogTbl.setItems(event.getUpdatedCatalog()); <-- pseudocode
        System.out.println("asafi");

        Platform.runLater(()-> {

            try{
                List<Flower> itemsList = (List<Flower>)event.getUpdatedItems();
                catalogTbl.setItems((ObservableList<Flower>) itemsList);

                infoButtonCol.setCellFactory(column -> new TableCell<>() {

                    final Button btn = new Button("Info");
                    {
                        btn.setOnAction(e ->

                        {

                            Flower item = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Item Info");
                            alert.setHeaderText("Item Information");      // Here is the window for information about the item
                            alert.setContentText("Name: " + item.getFlowerName() + "\n" +
                                    "Price: " + item.getFlowerPrice() + "\n" +
                                    "Category: " + item.getFlowerType());
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

    }
 */