package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.event.ActionEvent;

import java.io.IOException;

import java.util.List;


import il.cshaifasweng.OCSFMediatorExample.entities.CatalogUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;


public class CatalogController {

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
    private Label name_10;
    @FXML
    private Label name_11;
    @FXML
    private Label name_12;
    @FXML
    private Button nine_9;
    @FXML
    private Button ten_10;
    @FXML
    private Button twelve_12;
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
    private AnchorPane buttonsAncPane;
    @FXML
    private Label titleLbl;
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

    private List<Flower> flowersList_c;
    private Label[] nameLabels;
    private Label[] typeLabels;
    private TextField[] priceFields;
    private ImageView[] imageViews;
    private Text[] price_Before_sale;
    private int type=0 ; //0 for guest, 1 for store 1 ,2 for store 2 ,3 for store 3 ,
    // 4 for the network
    private int sorting_type=0 ; //0 for guest, 1 for store 1 ,2 for store 2 ,3 for store 3 ,
    // 4 for the network
    private List<Flower> flowersList_sorting;

    @FXML
    private ComboBox<String> Stores;
    @FXML
    void initialize() {
        System.out.println("CatalogController initialized");
        combo.getItems().addAll("Price High to LOW", "Price Low to HIGH");
        combo.setValue("Sort");
        Stores.getItems().addAll("Haifa", "Krayot","Nahariyya","network");
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
        setupClickHandler(flower_10, name_10, type_10, price_10, pic_10);
        setupClickHandler(flower_11, name_11, type_11, price_11, pic_11);
        setupClickHandler(flower_12, name_12, type_12, price_12, pic_12);
        setupClickHandler(flower_1, name_1, type_1, price_1, pic_1);
        setupClickHandler(flower_2, name_2, type_2, price_2, pic_2);
        setupClickHandler(flower_3, name_3, type_3, price_3, pic_3);
        setupClickHandler(flower_4, name_4, type_4, price_4, pic_4);
        setupClickHandler(flower_5, name_5, type_5, price_5, pic_5);
        setupClickHandler(flower_6, name_6, type_6, price_6, pic_6);
        setupClickHandler(flower_7, name_7, type_7, price_7, pic_7);
        setupClickHandler(flower_8, name_8, type_8, price_8, pic_8);
        setupClickHandler(flower_9, name_9, type_9, price_9, pic_9);
        nameLabels = new Label[] { name_1, name_2, name_3, name_4, name_5, name_6, name_7, name_8, name_9, name_10, name_11, name_12 };
        typeLabels = new Label[] { type_1, type_2, type_3, type_4, type_5, type_6, type_7, type_8, type_9, type_10, type_11, type_12 };
        priceFields = new TextField[] { price_1, price_2, price_3, price_4, price_5, price_6, price_7, price_8, price_9, price_10, price_11, price_12 };
        imageViews = new ImageView[] { pic_1, pic_2, pic_3, pic_4, pic_5, pic_6, pic_7, pic_8, pic_9, pic_10, pic_11, pic_12 };
        price_Before_sale = new Text[] { price_1_before_sale, price_2_before_sale, price_3_before_sale, price_4_before_sale, price_5_before_sale, price_6_before_sale, price_7_before_sale, price_8_before_sale, price_9_before_sale, price_10_before_sale, price_11_before_sale, price_12_before_sale };
    }
    public void  set_type(int value)
    {
        type=value;
    }
    public void  set_sorting_type(int value)
    {
        sorting_type=value;
    }
    public  void setFlowersList_c(List<Flower> flowerList)
    {
        flowersList_c = flowerList;
    }
    public void setCatalogSorting(List<Flower> flowerList) {
        flowersList_sorting = flowerList;
        if(sorting_type==0||sorting_type==4)
        {
            Stores.setValue("network");
        }
        if(sorting_type==1)
        {
            Stores.setValue("Haifa");
        }
        if(sorting_type==2)
        {
            Stores.setValue("Krayot");
        }
        if(sorting_type==3)
        {
            Stores.setValue("Nahariyya");
        }
        System.out.println("this client is see the  store: " + sorting_type);
        System.out.println("Received flowers: " + flowersList_sorting.size());
        for (Flower f : flowersList_sorting) {
            System.out.println(f.getFlowerName() + ", " + f.getFlowerPrice());
        }
        for (int i = 0; i < flowersList_sorting.size() && i < 12; i++) {
            Flower f = flowersList_sorting.get(i);
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

    public void setCatalogData(List<Flower> flowerList) {
        if(type==0||type==4)
        {
            flowersList_c = flowerList;
            Stores.setValue("network");
        }
        if(type==1)
        {
            Stores.setValue("Haifa");
        }
        if(type==2)
        {
            Stores.setValue("Krayot");
        }
        if(type==3)
        {
            Stores.setValue("Nahariyya");
        }
        System.out.println("this client is for store: " + type);
        System.out.println("Received flowers: " + flowersList_c.size());
        for (Flower f : flowersList_c) {
            System.out.println(f.getFlowerName() + ", " + f.getFlowerPrice());
        }
        for (int i = 0; i < flowersList_c.size() && i < 12; i++) {
            Flower f = flowersList_c.get(i);
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
    @FXML
    void stores_choose(ActionEvent event) throws IOException
    {
        System.out.println("enter stores");
        String selected = Stores.getValue();
        String message = "";
        if (selected.equals("Haifa")) {
            message = "Haifa";
        } else if (selected.equals("Krayot")) {
            message = "Krayot";

        }
        else if (selected.equals("Nahariyya")) {
            message = "Nahariyya";
        }
        else if (selected.equals("network")) {
            message = "network";
        }

         message = message + "_" + type;
        System.out.println("message: " + message);
        SimpleClient.getClient().sendToServer(message);


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
    public void receiveNewComplain(Complain complain)
    {
        try {
            SimpleClient.getClient().sendToServer(complain); // try to send the complain to the DB
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void gotoAcc(MouseEvent event) {

        if (SimpleClient.isGuest) {       //guest mode
            Platform.runLater(() -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration_screen.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Registration");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
        else{       //login user mode
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("my_account.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("My Account");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    void complaint(ActionEvent event)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("complain_scene.fxml"));
            Parent root = fxmlLoader.load();
            complain_controller Controller = fxmlLoader.getController();
            Controller.setCatalogController(this);

            Stage stage = new Stage();
            stage.setTitle("Complaint");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    @FXML
    public void combo_choose(ActionEvent actionEvent) throws IOException {
        sort_image.setVisible(false);
        String selected = combo.getValue();
        String selected_store = Stores.getValue();
        int localtype=1;

        if (selected == null) {
            return;
        }
        if(selected_store.equals("Haifa"))
        {
             localtype=1;
        }
        if(selected_store.equals("Krayot"))
        {
            localtype=2;
        }
        if(selected_store.equals("Nahariyya"))
        {
            localtype=3;
        }
        if(selected_store.equals("network"))
        {
            localtype=4;
        }
        System.out.println("localtype = " + localtype);
        String localtypeStr = String.valueOf(localtype);


        String message = "";
        if (selected.equals("Price High to LOW")) {
            message = "get_flowers_high_to_low_"+localtypeStr+"_"+type;
        } else if (selected.equals("Price Low to HIGH")) {
            message = "get_flowers_low_to_high_"+localtypeStr+"_"+type;
        }
        System.out.println("message = " + message);
        SimpleClient.getClient().sendToServer(message);
    }


    /// /////// yarden and dor
    private void setupClickHandler(VBox flowerBox, Label nameLabel, Label typeLabel, TextField priceField, ImageView imageView) {
        flowerBox.setOnMouseClicked(event -> {
            if (flowersList_c != null) {
                int index = getFlowerIndex(nameLabel.getText());
                if (index >= 0 && index < flowersList_c.size()) {
                    openOrderPage(flowersList_c.get(index));
                }
            }
        });
    }

    private int getFlowerIndex(String flowerName) {
        for (int i = 0; i < flowersList_c.size(); i++) {
            if (flowersList_c.get(i).getFlowerName().equals(flowerName)) {
                return i;
            }
        }
        return -1;
    }

    private void openOrderPage(Flower flower) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order_page.fxml"));
            Parent root = loader.load();
            OrderPageController orderController = loader.getController();
            orderController.setFlower(flower);

            Stage stage = new Stage();
            stage.setTitle("Order Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openCart(ActionEvent actionEvent) {
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
}








