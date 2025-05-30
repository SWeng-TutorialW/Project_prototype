package il.cshaifasweng.OCSFMediatorExample.client;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import javafx.util.converter.DoubleStringConverter;
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








    @FXML
    void initialize() {
        System.out.println("CatalogController initialized");
        catalogTbl.setEditable(true);


        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        // EventBus.getDefault().register(this);
        // so we can receive events SimpleClient.

    }

    @FXML
    public void commitPrice(TableColumn.CellEditEvent<Flower, Double> event) throws IOException {
        Flower flower = event.getRowValue();
        flower.setFlowerPrice(event.getNewValue());

            try {
                SimpleClient.getClient().sendToServer(flower); // try to send the flower to the DB
            }catch (IOException e){
                e.printStackTrace();
            }

    }
}
