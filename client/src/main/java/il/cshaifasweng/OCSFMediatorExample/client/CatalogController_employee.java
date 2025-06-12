package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.event.ActionEvent;
import java.io.IOException;

import java.util.List;


import il.cshaifasweng.OCSFMediatorExample.entities.Flower;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.hibernate.SessionFactory;
public class CatalogController_employee {

    @FXML
    private Button add_flower;

    @FXML
    private Button cart;

    @FXML
    private ComboBox<String> combo;

    @FXML
    private Button contact;

    @FXML
    private Label copyrightLBl;

    @FXML
    private Button eight_8;

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
    private VBox flower_7;

    @FXML
    private VBox flower_8;

    @FXML
    private VBox flower_9;

    @FXML
    private AnchorPane for_combo;

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
    private Label name_7;

    @FXML
    private Label name_8;

    @FXML
    private Label name_9;

    @FXML
    private Button nine_9;

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
    private ImageView pic_7;

    @FXML
    private ImageView pic_8;

    @FXML
    private ImageView pic_9;

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
    private TextField price_7;

    @FXML
    private TextField price_8;

    @FXML
    private TextField price_9;

    @FXML
    private HBox second_line;

    @FXML
    private HBox second_line1;

    @FXML
    private Button seven_7;

    @FXML
    private Button six_6;

    @FXML
    private ImageView sort_image;

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
    private Label type_7;

    @FXML
    private Label type_8;

    @FXML
    private Label type_9;

    private List<Flower> flowersList_c;
    private Label[] nameLabels;
    private Label[] typeLabels;
    private TextField[] priceFields;
    private ImageView[] imageViews;


    public void setCatalogData(List<Flower> flowerList) {
        flowersList_c = flowerList;

        System.out.println("Received flowers: " + flowerList.size());
        for (Flower f : flowerList) {
            System.out.println(f.getFlowerName() + ", " + f.getFlowerPrice());
        }

        flowersList_c = flowerList;

        System.out.println("Received flowers: " + flowerList.size());
        for (int i = 0; i < flowerList.size() && i < 9; i++) {
            Flower f = flowerList.get(i);
            nameLabels[i].setText(f.getFlowerName());
            priceFields[i].setText(String.format("%.2f", f.getFlowerPrice()));
            typeLabels[i].setText(f.getFlowerType());
            setImage(imageViews[i], f.getFlowerType());


            if (i == 6) seven_7.setVisible(true);
            if (i == 7) eight_8.setVisible(true);
            if (i == 8) nine_9.setVisible(true);
        }
    }

    private void setImage(ImageView imageView, String flowerName) {
        try {
            String imagePath = "/images/" + flowerName + ".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load image for: " + flowerName);
            String imagePath = "/images/" + "no_photo"+".png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(image);
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

        // Set up cart button click handler
        cart.setOnAction(event -> openCart());

        nameLabels = new Label[] { name_1, name_2, name_3, name_4, name_5, name_6, name_7, name_8, name_9 };
        typeLabels = new Label[] { type_1, type_2, type_3, type_4, type_5, type_6, type_7, type_8, type_9 };
        priceFields = new TextField[] { price_1, price_2, price_3, price_4, price_5, price_6, price_7, price_8, price_9 };
        imageViews = new ImageView[] { pic_1, pic_2, pic_3, pic_4, pic_5, pic_6, pic_7, pic_8, pic_9 };
    }

    @FXML
    private void openCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cart.fxml"));
            Parent root = loader.load();
            CartController cartController = loader.getController();
            cartController.setCartItems(OrderPageController.getCartItems());
            
            Stage stage = new Stage();
            stage.setTitle("Shopping Cart");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Warning warning = new Warning("Error opening cart");
            EventBus.getDefault().post(new WarningEvent(warning));
        }
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
    @FXML
    void open_complain_box(ActionEvent event)
    {


    }
    @FXML
    void add_flower(ActionEvent event)
    {
        if (type_7.getText() != null && !type_7.getText().trim().isEmpty() &&
                type_8.getText() != null && !type_8.getText().trim().isEmpty() &&
                type_9.getText() != null && !type_9.getText().trim().isEmpty()) {

            Warning warning = new Warning("The catalog is currently full. Please contact support for further assistance.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add_flower.fxml"));
                Parent root = fxmlLoader.load();
                AddFlower_Controller addFlowerController = fxmlLoader.getController();
                addFlowerController.setCatalogController(this);

                Stage stage = new Stage();
                stage.setTitle("Add New Flower");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) event.getSource()).getScene().getWindow());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public void receiveNewFlower(Flower flower) {
        System.out.println("Received flower: " + flower);
        for (int i = 6; i <= 8; i++) {
            if (typeLabels[i].getText() == null || typeLabels[i].getText().trim().isEmpty()) {
                typeLabels[i].setText(flower.getFlowerType());
                nameLabels[i].setText(flower.getFlowerName());
                priceFields[i].setText(String.format("%.2f", flower.getFlowerPrice()));
                setImage(imageViews[i], flower.getFlowerType());

                if (i == 6) seven_7.setVisible(true);
                if (i == 7) eight_8.setVisible(true);
                if (i == 8) nine_9.setVisible(true);
                break;
            }
        }

        try {
            SimpleClient.getClient().sendToServer(flower);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}








