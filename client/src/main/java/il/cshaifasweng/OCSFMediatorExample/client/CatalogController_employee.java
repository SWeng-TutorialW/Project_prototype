package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.FlowerDiscountWrapper;
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
import javafx.scene.text.Text;
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
    private Button eleven_11;
    @FXML
    private VBox flower_10;

    @FXML
    private VBox flower_11;

    @FXML
    private VBox flower_12;
    @FXML
    private Button ten_10;
    @FXML
    private Button twelve_12;
    @FXML
    private Label name_10;

    @FXML
    private Label name_11;

    @FXML
    private Label name_12;


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
    private ImageView pic_10;

    @FXML
    private ImageView pic_11;

    @FXML
    private ImageView pic_12;

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
    private TextField price_10;

    @FXML
    private TextField price_11;

    @FXML
    private TextField price_12;



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
    @FXML
    private Label type_10;

    @FXML
    private Label type_11;

    @FXML
    private Label type_12;

    @FXML
    private Text price_1_before_sale;

    @FXML
    private Text price_2_before_sale;

    @FXML
    private Text price_3_before_sale;

    @FXML
    private Text price_4_before_sale;

    @FXML
    private Text price_5_before_sale;

    @FXML
    private Text price_6_before_sale;

    @FXML
    private Text price_7_before_sale;

    @FXML
    private Text price_8_before_sale;

    @FXML
    private Text price_9_before_sale;

    @FXML
    private Text price_10_before_sale;

    @FXML
    private Text price_11_before_sale;

    @FXML
    private Text price_12_before_sale;
    @FXML
    private Button discount;

    private List<Flower> flowersList_c;
    private Label[] nameLabels;
    private Label[] typeLabels;
    private TextField[] priceFields;
    private ImageView[] imageViews;
    private Text[] price_Before_sale;




    public void setCatalogData(List<Flower> flowerList) {
        flowersList_c = flowerList;

        System.out.println("Received flowers: " + flowerList.size());
        for (Flower f : flowerList) {
            System.out.println(f.getFlowerName() + ", " + f.getFlowerPrice());
        }

        flowersList_c = flowerList;



        for (int i = 0; i < flowerList.size() && i < 12; i++) {
            Flower f = flowerList.get(i);
            nameLabels[i].setText(f.getFlowerName());
            if (f.isSale())
            {
                price_Before_sale[i].setVisible(true);
                int discount_percent = f.getDiscount();
                double remainingPercent = 100.0 -discount_percent;
                double originalPrice = f.getFlowerPrice() * 100.0 / remainingPercent;
                String originalPriceStr = String.format("%.2f", originalPrice);
                price_Before_sale[i].setText(originalPriceStr);



            }
            priceFields[i].setText(String.format("%.2f", f.getFlowerPrice()));
            typeLabels[i].setText(f.getFlowerType());
            setImage(imageViews[i], f.getFlowerType());




            if (i == 8) nine_9.setVisible(true);
            if (i == 9) ten_10.setVisible(true);
            if (i == 10) eleven_11.setVisible(true);
            if (i == 11) twelve_12.setVisible(true);
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
        System.out.println("CatalogController employee initialized");
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
        nameLabels = new Label[] { name_1, name_2, name_3, name_4, name_5, name_6, name_7, name_8, name_9, name_10, name_11, name_12 };
        typeLabels = new Label[] { type_1, type_2, type_3, type_4, type_5, type_6, type_7, type_8, type_9, type_10, type_11, type_12 };
        priceFields = new TextField[] { price_1, price_2, price_3, price_4, price_5, price_6, price_7, price_8, price_9, price_10, price_11, price_12 };
        imageViews = new ImageView[] { pic_1, pic_2, pic_3, pic_4, pic_5, pic_6, pic_7, pic_8, pic_9, pic_10, pic_11, pic_12 };
        price_Before_sale = new Text[] { price_1_before_sale, price_2_before_sale, price_3_before_sale, price_4_before_sale, price_5_before_sale, price_6_before_sale, price_7_before_sale, price_8_before_sale, price_9_before_sale, price_10_before_sale, price_11_before_sale, price_12_before_sale };




    }


    @FXML
    public void commitPrice(ActionEvent event) throws IOException {
        int flower_to_update = 0;
        TextField source = (TextField) event.getSource();
        List<TextField> priceFields = List.of(
                price_1, price_2, price_3, price_4, price_5, price_6,
                price_7, price_8, price_9, price_10, price_11, price_12
        );

        for (int i = 0; i < priceFields.size(); i++) {
            if (source == priceFields.get(i)) {
                flower_to_update = i;
                break;
            }
        }

        Flower flower = flowersList_c.get(flower_to_update);
        String name= flower.getFlowerName();
        String new_price = source.getText();
        double value = Double.parseDouble(new_price);
        try {
            SimpleClient.getClient().sendToServer("number#flower#to#update#_" + name + "_" + value); // try to send the flower to the DB
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
    void add_flower(ActionEvent event) {
        System.out.println("add_flower clicked!");
        if (
                type_9.getText() != null && !type_9.getText().trim().isEmpty() &&
                        type_10.getText() != null && !type_10.getText().trim().isEmpty() &&
                        type_11.getText() != null && !type_11.getText().trim().isEmpty() &&
                        type_12.getText() != null && !type_12.getText().trim().isEmpty()
        ) {
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
        for (int i = 8; i <= 11; i++) {
            if (typeLabels[i].getText() == null || typeLabels[i].getText().trim().isEmpty()) {
                typeLabels[i].setText(flower.getFlowerType());
                nameLabels[i].setText(flower.getFlowerName());
                priceFields[i].setText(String.format("%.2f", flower.getFlowerPrice()));
                setImage(imageViews[i], flower.getFlowerType());

                if (i == 8) nine_9.setVisible(true);
                if (i == 9) ten_10.setVisible(true);
                if (i == 10) eleven_11.setVisible(true);
                if (i == 11) twelve_12.setVisible(true);
                break;
            }
        }

        try {
            SimpleClient.getClient().sendToServer(flower);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void discount(ActionEvent event)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("discount_scene.fxml"));
            Parent root = fxmlLoader.load();
            discount_Controller controller = fxmlLoader.getController();
            controller.setCatalogController(this);
            controller.setFlowersList_c(flowersList_c);

            Stage stage = new Stage();
            stage.setTitle("Set discount");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void receivediscount(int discount,Flower flower) {
        System.out.println("Received discount: " + discount);
        if(flower!=null)
        {
            System.out.println("On flower: " + flower.getFlowerName());
            FlowerDiscountWrapper wrapper = new FlowerDiscountWrapper(flower, discount);
            try {
                SimpleClient.getClient().sendToServer(wrapper);
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
        else
        {
            System.out.println("for all flowers");
            try {
                SimpleClient.getClient().sendToServer(discount);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    }
    @FXML
    void delete_flower(ActionEvent event)
    {
        Button clickedButton = (Button) event.getSource();
        VBox graphicVBox = (VBox) clickedButton.getGraphic();
        String flowerName="";

        for (Node node : graphicVBox.getChildren()) {
            if (node instanceof Label && ((Label) node).getId() != null && ((Label) node).getId().startsWith("name_")) {
                Label nameLabel = (Label) node;
                 flowerName = nameLabel.getText();
                System.out.println("Flower name: " + flowerName);
                break;
            }
        }
        Flower targetFlower = null;
        for (Flower flower : flowersList_c) {
            if (flower.getFlowerName().equalsIgnoreCase(flowerName)) {
                targetFlower = flower;
                break;
            }
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("delete_scene.fxml"));
            Parent root = loader.load();
            delete_controller orderController = loader.getController();
            orderController.setFlower(targetFlower);

            Stage stage = new Stage();
            stage.setTitle("Delete Flower");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void delete(Flower flower)
    {


    }


}








