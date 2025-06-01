package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import java.io.IOException;

import java.util.List;


import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class CatalogController {

    @FXML
    private Button cart;

    @FXML
    private Button contact;

    @FXML
    private Label copyrightLBl;

    @FXML
    private HBox first_line;

    @FXML
    private Button five_5;

    @FXML
    private VBox flower_1;

    @FXML
    private VBox flower_2;

    @FXML
    private VBox flower_3;

    @FXML
    private VBox flower_4;

    @FXML
    private VBox flower_5;

    @FXML
    private VBox flower_6;

    @FXML
    private Button four_4;

    @FXML
    private Button my_account;

    @FXML
    private Label name_1;

    @FXML
    private Label name_2;

    @FXML
    private Label name_3;

    @FXML
    private Label name_4;

    @FXML
    private Label name_5;

    @FXML
    private Label name_6;

    @FXML
    private Button one_1;

    @FXML
    private ImageView pic_1;

    @FXML
    private ImageView pic_2;

    @FXML
    private ImageView pic_3;

    @FXML
    private ImageView pic_4;

    @FXML
    private ImageView pic_5;

    @FXML
    private ImageView pic_6;

    @FXML
    private TextField price_1;

    @FXML
    private TextField price_2;

    @FXML
    private TextField price_3;

    @FXML
    private TextField price_4;

    @FXML
    private TextField price_5;

    @FXML
    private TextField price_6;

    @FXML
    private HBox second_line;

    @FXML
    private Button six_6;

    @FXML
    private Button three_3;

    @FXML
    private Button two_2;

    @FXML
    private Label type_1;

    @FXML
    private Label type_2;

    @FXML
    private Label type_3;

    @FXML
    private Label type_4;

    @FXML
    private Label type_5;

    @FXML
    private Label type_6;

    @FXML
    private ComboBox<String> combo;


    @FXML
    private AnchorPane for_combo;

    @FXML
    private ImageView sort_image;


    private List<Flower> flowersList_c;


    public void setCatalogData(List<Flower> flowerList) {
        flowersList_c = flowerList;

        System.out.println("Received flowers: " + flowerList.size());
        for (Flower f : flowerList) {
            System.out.println(f.getFlowerName() + ", " + f.getFlowerPrice());
        }

        if (flowerList.size() > 0) {
            Flower f = flowerList.get(0);
            name_1.setText(f.getFlowerName());
            price_1.setText(String.format("%.2f", f.getFlowerPrice()));
            type_1.setText(f.getFlowerType());
            setImage(pic_1, f.getFlowerType());
        }
        if (flowerList.size() > 1) {
            Flower f = flowerList.get(1);
            name_2.setText(f.getFlowerName());
            price_2.setText(String.format("%.2f", f.getFlowerPrice()));
            type_2.setText(f.getFlowerType());
            setImage(pic_2, f.getFlowerType());
        }
        if (flowerList.size() > 2) {
            Flower f = flowerList.get(2);
            name_3.setText(f.getFlowerName());
            price_3.setText(String.format("%.2f", f.getFlowerPrice()));
            type_3.setText(f.getFlowerType());
            setImage(pic_3, f.getFlowerType());
        }
        if (flowerList.size() > 3) {
            Flower f = flowerList.get(3);
            name_4.setText(f.getFlowerName());
            price_4.setText(String.format("%.2f", f.getFlowerPrice()));
            type_4.setText(f.getFlowerType());
            setImage(pic_4, f.getFlowerType());
        }
        if (flowerList.size() > 4) {
            Flower f = flowerList.get(4);
            name_5.setText(f.getFlowerName());
            price_5.setText(String.format("%.2f", f.getFlowerPrice()));
            type_5.setText(f.getFlowerType());
            setImage(pic_5, f.getFlowerType());
        }
        if (flowerList.size() > 5) {
            Flower f = flowerList.get(5);
            name_6.setText(f.getFlowerName());
            price_6.setText(String.format("%.2f", f.getFlowerPrice()));
            type_6.setText(f.getFlowerType());
            setImage(pic_6, f.getFlowerType());
        }
    }

    private void setImage(ImageView imageView, String flowerName) {
        try {
            String imagePath = "/images/" + flowerName + ".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load image for: " + flowerName);
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        System.out.println("CatalogController initialized");
        combo.getItems().addAll("Price High to LOW", "Price Low to HIGH");
        combo.setValue("Sort");
        combo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setAlignment(Pos.CENTER);
                setTextFill(Color.web("#FFFAFA"));
            }
        });

        combo.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setAlignment(Pos.CENTER);
                setTextFill(Color.web("#C8A2C8"));
            }
        });

    }


    @FXML
    public void commitPrice(ActionEvent event) throws IOException {
        int flower_to_update = 0;
        TextField source = (TextField) event.getSource();
        List<TextField> priceFields = List.of(price_1, price_2, price_3, price_4, price_5, price_6);
        for (int i = 0; i < priceFields.size(); i++) {
            if (source == priceFields.get(i)) {
                flower_to_update = i;
                break;
            }
        }
        String str = String.valueOf(flower_to_update);
        String new_price = source.getText();
        double value = Double.parseDouble(new_price);
        try {
            SimpleClient.getClient().sendToServer("number#flower#to#update#_" + str + "_" + value); // try to send the flower to the DB
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleClient.getClient().sendToServer("update_catalog_after_change");
    }

    @FXML
    public void combo_choose(ActionEvent actionEvent) throws IOException {
        sort_image.setVisible(false);
        String selected = combo.getValue();

        if (selected == null) {
            return;
        }

        String message = "";
        if (selected.equals("Price High to LOW")) {
            message = "get_flowers_high_to_low";
        } else if (selected.equals("Price Low to HIGH")) {
            message = "get_flowers_low_to_high";
        }
        SimpleClient.getClient().sendToServer(message);
    }
}








