package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
import org.greenrobot.eventbus.Subscribe;
import org.hibernate.SessionFactory;

public class CatalogController_employee {

    @FXML
    private Button add_flower;

    @FXML
    private Button cart;

    @FXML
    private ComboBox<String> combo;
    @FXML
    private ComboBox<String> Stores;

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
    @FXML
    private Button users_btn;

    private List<Flower> flowersList_c;
    private Label[] nameLabels;
    private Label[] typeLabels;
    private TextField[] priceFields;
    private ImageView[] imageViews;
    private Text[] price_Before_sale;
    private int type=0 ; //, 1 for store 1 ,2 for store 2 ,3 for store 3 ,
    // 4 for the network
    private int sorting_type=-1 ; //, 1 for store 1 ,2 for store 2 ,3 for store 3 ,
    // 4 for the network ,-1 mean not sorted anything
    private List<Flower> flowersList_sorting;
    public void  set_type(int value)
    {
        type=value;
        if(type!=4)
        {
            discount.setText("Send request to the admin");
            discount.setPrefWidth(240);
            discount.setOnAction(this::request);
        }
    }
    private LoginRegCheck user;
    public void set_user(LoginRegCheck user) {
        this.user = user;
        System.out.println("set_user updated");
        System.out.println("user send?"+user.get_send_complain());
        System.out.println("user recieve?"+user.isReceive_answer());
        sorting_type= user.getStore();
    }
    boolean is_login=false;
    public void set_isLogin(boolean is_login) {
        this.is_login = is_login;
    }
    public LoginRegCheck getUser() {
        return user;
    }
    private ComplainController_employee complainController;
    public void setCatalogController(ComplainController_employee controller) {
        this.complainController = controller;
    }
    public void  set_sorting_type(int value)
    {
        sorting_type=value;
    }
    List<String> colors_in_catalog = new ArrayList<>();
    @FXML
    private ComboBox<String> colors;
    private boolean isUpdating = false;
    public  void setFlowersList_c(List<Flower> flowerList)
    {
        System.out.println("enter flowerList");


        flowersList_c = flowerList;
        if(!colors_in_catalog.isEmpty())
        {
            colors_in_catalog.clear();
        }
        for (Flower flower : flowerList) {
            String color = flower.getColor();
            if (!colors_in_catalog.contains(color)) {
                colors_in_catalog.add(color);
                System.out.println("Added color: " + color + " from flower: " + flower.getFlowerType());
            }
        }
        colors_in_catalog.add("all");
        isUpdating=true;


        colors.getItems().setAll(colors_in_catalog);
        colors.setValue("all");
        System.out.println("put colors");
        isUpdating=false;
    }

    int add_flower_flag=0;
    String flower_name="";


    @FXML
    public void colors_choose(ActionEvent actionEvent) throws IOException
    {
        if (isUpdating) return;
        combo.getSelectionModel().clearSelection();
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
        sort_image.setVisible(true);


        String selected = colors.getValue();
        if (selected == null) {
            System.out.println("No color selected — skipping colors_choose");
            return;
        }
        if (selected.equals("all"))
        {

            if(flowersList_sorting!=null)
            {
                setCatalogSorting(flowersList_sorting);
                return;
            }
            else
            {
                setFlowersList_c(flowersList_c);
                setCatalogData(flowersList_c);
                return;
            }



        }
        if(flowersList_sorting!=null)
        {
            List<Flower> filterFlowersbyColor=filterFlowersByColor(flowersList_sorting,selected);
            setCatalogSorting_by_color(filterFlowersbyColor);
            return;
        }
        List<Flower> filterFlowersbyColor=filterFlowersByColor(flowersList_c,selected);
        System.out.println("Received flowers inside colors: " + filterFlowersbyColor.size());
        for (Flower f : filterFlowersbyColor) {
            System.out.println(f.getFlowerName() + ", " + f.getFlowerPrice());
        }

        setCatalogSorting_by_color(filterFlowersbyColor);
        return;

    }
    List<Flower> flowerList_color;

    public void setCatalogSorting_by_color(List<Flower> flowerList)
    {
        flowerList_color = flowerList;

        Platform.runLater(() -> {
            clearCatalog();

            System.out.println("Received flowers: " + flowerList.size());
            for (Flower f : flowerList) {
                System.out.println(f.getFlowerName() + ", " + f.getFlowerPrice());
            }

            for (int i = 0; i < flowerList.size() && i < 12; i++) {
                Flower f = flowerList.get(i);

                nameLabels[i].setText(f.getFlowerName());

                if (f.isSale()) {
                    price_Before_sale[i].setVisible(true);
                    int discount_percent = f.getDiscount();
                    double remainingPercent = 100.0 - discount_percent;
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
        });
    }
    public void setCatalogSorting(List<Flower> flowerList)
    {
        flowersList_sorting=flowerList;
        isUpdating=true;

        if(!colors_in_catalog.isEmpty())
        {
            colors_in_catalog.clear();
        }
        for (Flower flower : flowerList) {
            String color = flower.getColor();
            if (!colors_in_catalog.contains(color)) {
                colors_in_catalog.add(color);
                System.out.println("Added color: " + color + " from flower: " + flower.getFlowerType());
            }
        }
        colors_in_catalog.add("all");






        Platform.runLater(() -> {
            isUpdating=true;


            colors.getItems().setAll(colors_in_catalog);
            colors.setValue("all");
            System.out.println("put colors");
            isUpdating=false;
            if (sorting_type == 0 || sorting_type == 4) {
                Stores.setValue("network");
            }
            if (sorting_type == 1) {
                Stores.setValue("Haifa");
            }
            if (sorting_type == 2) {
                Stores.setValue("Krayot");
            }
            if (sorting_type == 3) {
                Stores.setValue("Nahariyya");
            }

            clearCatalog();
            System.out.println("catalog cleard inside sorting");
            System.out.println("add flag: " + add_flower_flag);
            if(add_flower_flag==1 && type!=4)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("new flower in catalog");
                alert.setHeaderText("see our new flower :]");
                alert.setContentText("see our new flower :]");
                alert.show();
            }
            if(add_flower_flag==-1 && type!=4)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("flower deleted from the catalog");
                alert.setHeaderText("flower deleted from the catalog :[");
                alert.setContentText("flower deleted from the catalog:[");
                alert.show();
            }
            if(add_flower_flag==2 && type!=4)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("NEW PRICE FOR"+ flower_name);
                alert.setHeaderText("NEW PRICE FOR"+ flower_name);
                alert.setContentText("NEW PRICE FOR"+ flower_name);
                alert.show();
            }
            if(add_flower_flag==3&& type!=4)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ALL THE STORE IN DISCOUNTS");
                alert.setHeaderText("ALL THE STORE IN DISCOUNTS");
                alert.setContentText("ALL THE STORE IN DISCOUNTS");
                alert.show();
            }
            add_flower_flag=0;
            System.out.println("inside sorting");
            System.out.println("this employee is for store: " + type);
            System.out.println("this employee is see the store: " + sorting_type);
            System.out.println("Received flowers: " + flowersList_sorting.size());
            for (Flower f : flowersList_sorting) {
                System.out.println(f.getFlowerName() + ", " + f.getFlowerPrice());
            }

            for (int i = 0; i < flowersList_sorting.size() && i < 12; i++) {
                Flower f = flowersList_sorting.get(i);

                nameLabels[i].setText(f.getFlowerName());

                if (f.isSale()) {
                    price_Before_sale[i].setVisible(true);
                    int discount_percent = f.getDiscount();
                    double remainingPercent = 100.0 - discount_percent;
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
        });
    }

    public void clearCatalog() {
        for (int i = 0; i < 12; i++) {
            nameLabels[i].setText("");
            priceFields[i].setText("");
            typeLabels[i].setText("");
            price_Before_sale[i].setVisible(false);
            price_Before_sale[i].setText("");
            imageViews[i].setImage(null);
        }
        nine_9.setVisible(false);
        ten_10.setVisible(false);
        eleven_11.setVisible(false);
        twelve_12.setVisible(false);
        System.out.println("Catalog cleared.");
    }
    public static List<Flower> filterFlowersByColor(List<Flower> flowers, String color) {
        List<Flower> filtered = new ArrayList<>();
        for (Flower flower : flowers) {
            if (flower.getColor().equalsIgnoreCase(color)) {
                filtered.add(flower);
            }
        }
        return filtered;
    }
    public void setCatalogData(List<Flower> flowerList)
    {


        if(type==0||type==4)
        {
            setFlowersList_c(flowerList);
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
        System.out.println("this employee is for store: " + type);
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
    void initialize() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            System.out.println("CatalogController_employee registered");
        } else {
            System.out.println("CatalogController_employee already registered");
        }
        System.out.println("CatalogController employee initialized");
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
        price_Before_sale = new Text[] { price_1_before_sale, price_2_before_sale, price_3_before_sale, price_4_before_sale, price_5_before_sale, price_6_before_sale, price_7_before_sale, price_8_before_sale, price_9_before_sale, price_10_before_sale, price_11_before_sale, price_12_before_sale };
        Stage stage = App.getStage();
        stage.setOnCloseRequest(event1 -> {
            try {
                if (user != null) {
                    user.setIsLogin(0);
                    change_user_login tt = new change_user_login(user,0);
                    System.out.println("the user is " + user.getUsername()+"logged out");
                    SimpleClient.getClient().sendToServer(tt);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @FXML
    void gotoEmployeeAcc(ActionEvent event){
        if (user == null) {
            System.out.println("No user connected");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("my_account_employee.fxml"));
            Parent root = loader.load();

            EmployeeAccountController controller = loader.getController();
            controller.setCurrentUser(user);

            Stage stage = new Stage();
            stage.setTitle("Employee Account");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void show_users(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("users_table.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("User Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void handleCatalogUpdate(Add_flower_event event)throws IOException
    {

        System.out.println("enter handle " );

        if(event.get_flowers()==null)
        {
            System.out.println("the user is " + user.getUsername());
            if(type!=4)
            {
                System.out.println("the user that came from the event " +event.getUser().getUsername());
                if(user.getUsername().equals(event.getUser().getUsername()))
                {
                    set_user(event.getUser());
                    return;
                }

            }
            return;
        }
        if(event.get_catalog_type()==-1)
        {
            add_flower_flag=-1;
            setCatalogSorting(event.get_flowers());
            set_sorting_type(4);
            return;
        }
        add_flower_flag=1;
        setCatalogSorting(event.get_flowers());
        set_sorting_type(4);
        if(type!=4)
        {
            System.out.println("send message to server "+type);
            SimpleClient.getClient().sendToServer("I#need#to#update#my#store#catalog_"+type);
            return;
        }
        flowersList_c=event.get_flowers();


    }
    @Subscribe
    public void handleCatalogUpdate(discount_for_1_flower event)throws IOException
    {

        if(event.get_catalog_type()==1)
        {
            flower_name=event.get_flower_name();
            add_flower_flag=2;
            setCatalogSorting(event.get_flowers());
            set_sorting_type(4);
            return;
        }
        if(event.get_catalog_type()==2)
        {

            add_flower_flag=3;
            setCatalogSorting(event.get_flowers());
            set_sorting_type(4);
            return;
        }
        if(event.get_catalog_type()==-2)
        {

            add_flower_flag=0;
            setCatalogSorting(event.get_flowers());
            set_sorting_type(4);
            return;
        }

        if(type!=4)
        {
            System.out.println("send message to server "+type);
            SimpleClient.getClient().sendToServer("I#need#to#update#my#store#catalog_"+type);
            return;
        }
        flowersList_c=event.get_flowers();


    }
    @Subscribe
    public void handleCatalogUpdate(update_local_catalog event)
    {
        System.out.println("enter ok");
        if(type== event.get_catalog_type())
        {
            System.out.println("the local catalog updated " +event.get_catalog_type());
            flowersList_c=event.get_flowers();
            return;
        }
    }


    @FXML
    void stores_choose(ActionEvent event) throws IOException
    {
        System.out.println("enter stores");
        String selected = Stores.getValue();
        String message = "";
        if(selected.equals("Haifa")) {
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
        combo.getSelectionModel().clearSelection();
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
        sort_image.setVisible(true);

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


    @FXML
    public void combo_choose(ActionEvent actionEvent) throws IOException {

        sort_image.setVisible(false);
        String color=colors.getValue();
        if(color==null)
        {
            color="all";
        }
        String selected = combo.getValue();
        String selected_store = Stores.getValue();
        if(color.equals("all"))
        {

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
        else
        {
            if(selected != null)
            {
                if (selected.equals("Price High to LOW"))
                {
                    flowerList_color=getFlowersOrdered("desc",flowerList_color);
                }
                else if (selected.equals("Price Low to HIGH"))
                {
                    flowerList_color=getFlowersOrdered("asc",flowerList_color);
                }
                setCatalogSorting_by_color(flowerList_color);
            }



        }

    }
    private List<Flower> getFlowersOrdered(String direction, List<Flower> flowers) {
        List<Flower> result = new ArrayList<>(flowers);
        if (direction.equals("desc")) {
            result.sort((f1, f2) -> Double.compare(f2.getFlowerPrice(), f1.getFlowerPrice()));
        } else {
            result.sort((f1, f2) -> Double.compare(f1.getFlowerPrice(), f2.getFlowerPrice()));
        }
        return result;
    }
    @FXML
    void open_complain_box(ActionEvent event)throws IOException
    {
        SimpleClient.getClient().sendToServer("update_complainScene_after_change");
        if(type==4)
        {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("complain_handle_screen.fxml"));
                Parent root = fxmlLoader.load();
                ComplainController_employee complainControllerEmployee = fxmlLoader.getController();
                complainControllerEmployee.setCatalogController(this);

                Stage stage = new Stage();
                stage.setTitle("Complaints");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) event.getSource()).getScene().getWindow());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            if(user.isReceive_answer())
            {
                SimpleClient.getClient().sendToServer("I#want#to#see#my#answer_"+user.getUsername());
                System.out.println("I#want#to#see#my#answer_"+user.getUsername());
                return;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mailbox");
            alert.setHeaderText("");
            alert.setContentText("You dont have any messages");
            alert.showAndWait();
        }
    }
    @FXML
    void add_flower(ActionEvent event) {
        System.out.println("add_flower clicked!");
        System.out.println("type = " + type);
        if(type==4)
        {
            if(sorting_type!=4 && sorting_type!=-1)
            {
                Warning warning = new Warning("You are being transferred to the network's catalog.");
                EventBus.getDefault().post(new WarningEvent(warning));
                setCatalogData(flowersList_c);
                return;
            }
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
                addFlowerController.set_flowers_list(flowersList_c);

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
        else
        {
            if(sorting_type==-1)
            {
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
                return;
            }
            if(sorting_type!=type)
            {
                Warning warning = new Warning("You are being transferred to your store's catalog.");
                EventBus.getDefault().post(new WarningEvent(warning));
                setCatalogData(flowersList_c);
                return;
            }
            else
            {
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
                return;
            }
        }


    }
    @Subscribe
    public void show_answer(Complain event)
    {
        System.out.println("show_answer");
        Platform.runLater(() -> {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("answer_scene.fxml"));
            Parent root = fxmlLoader.load();
            AnswerScene controller = fxmlLoader.getController();
            controller.setComplain(event);
            controller.set_user(user);


            Stage stage = new Stage();
            stage.setTitle("Answer from the admin");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        });
        return;

    }

    public void receiveNewFlower(Flower flower) {
        System.out.println("Received flower: " + flower.getFlowerType());
        if(type==4)
        {
            try {
                SimpleClient.getClient().sendToServer(flower);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("the type is: " + type);
            Add_flower_wrapper wrapper = new Add_flower_wrapper(flower, type);
            try {
                SimpleClient.getClient().sendToServer(wrapper);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    @FXML
    void discount(ActionEvent event)
    {
        System.out.println("discount clicked");
        System.out.println("type = " + type);
        if(type==4)
        {
            if(sorting_type!=4 && sorting_type!=-1)
            {
                Warning warning = new Warning("You are being transferred to the network's catalog.");
                EventBus.getDefault().post(new WarningEvent(warning));
                setCatalogData(flowersList_c);
                return;
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("discount_scene.fxml"));
                Parent root = fxmlLoader.load();
                discount_Controller controller = fxmlLoader.getController();
                controller.setCatalogController(this);
                controller.setFlowersList(flowersList_c);

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
        else
        {
            Warning warning = new Warning("You are not allowed to discount please send request to  the administrator.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;

        }
    }
    @FXML
    void request(ActionEvent event)
    {
        if(user.get_send_complain())
        {
            Warning warning = new Warning("You already send a  request.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("worker_request_scene.fxml"));
            Parent root = fxmlLoader.load();
            worker_request_controller Controller = fxmlLoader.getController();
            Controller.setCatalogController(this);

            Stage stage = new Stage();
            stage.setTitle("Send request");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void receiveNewComplain(Complain complain)
    {
        complain.setClient(user.getUsername());
        user.set_send(true);
        change_sendOrRecieve_messages wrapper = new change_sendOrRecieve_messages(user, true,false);
        try {
            SimpleClient.getClient().sendToServer(complain);// try to send the complain to the DB
            SimpleClient.getClient().sendToServer(wrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    boolean sale=false;
    @FXML
    private Button end_sale_btn;
    @FXML
    void end_sale(ActionEvent event)
    {
        try {
            SimpleClient.getClient().sendToServer("end_sale");
        } catch (IOException e) {
            e.printStackTrace();
        }
        sale=false;
        end_sale_btn.setVisible(false);


    }

    public void receivediscount(int discount,Flower flower) {
        System.out.println("Received discount: " + discount);
        if(flower!=null)
        {
            sale=true;
            end_sale_btn.setVisible(true);
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
            sale=true;
            end_sale_btn.setVisible(true);
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
        if(flowersList_sorting!=null)
        {
            for (Flower flower : flowersList_sorting) {
                if (flower.getFlowerName().equalsIgnoreCase(flowerName)) {
                    targetFlower = flower;
                    break;
                }
            }
        }
        else
        {
            for (Flower flower : flowersList_c) {
                if (flower.getFlowerName().equalsIgnoreCase(flowerName)) {
                    targetFlower = flower;
                    break;
                }
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("delete_scene.fxml"));
            Parent root = loader.load();
            delete_controller orderController = loader.getController();
            orderController.setFlower(targetFlower);
            orderController.set_type(type);

            Stage stage = new Stage();
            stage.setTitle("Delete Flower");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}








