package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.io.IOException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.hibernate.SessionFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import il.cshaifasweng.OCSFMediatorExample.client.Success;
import il.cshaifasweng.OCSFMediatorExample.client.SuccessEvent;

public class CatalogController_employee {

    @FXML
    private Button add_flower;

    @FXML
    private Button cart;



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

    // Add FlowPane for dynamic flower display
    @FXML
    private FlowPane catalogFlowPane;

    // Add missing button fields
    @FXML
    private Button end_sale_btn;

    @FXML
    private Button request_btn;

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
        
        // Set default store filter based on user's store
        setDefaultStoreFilter();
    }
    
    private void setDefaultStoreFilter() {
        if (user == null) return;
        
        int userStore = user.getStore();
        final String defaultStore;
        
        // Map user's store number to store name
        switch (userStore) {
            case 1:
                defaultStore = "Haifa";
                break;
            case 2:
                defaultStore = "Krayot";
                break;
            case 3:
                defaultStore = "Nahariyya";
                break;
            case 4:
                defaultStore = "network"; // Network employees see all stores
                break;
            default:
                defaultStore = "Haifa"; // Default fallback
                break;
        }
        
        // Set the default store in the ComboBox
        if (Stores != null && defaultStore != null) {
            Platform.runLater(() -> {
                Stores.setValue(defaultStore);
                System.out.println("Default store filter set to: " + defaultStore + " for user store: " + userStore);
                
                // Trigger the store selection to load the appropriate catalog
                try {
                    if (defaultStore.equals("network")) {
                        String message = "get_all_flowers";
                        SimpleClient.getClient().sendToServer(message);
                        System.out.println("Requested all flowers for network view");
                    } else {
                        int storeId = getCurrentStoreId(defaultStore);
                        if (storeId != -1) {
                            String message = "get_catalog_" + storeId;
                            SimpleClient.getClient().sendToServer(message);
                            System.out.println("Requested catalog for default store ID: " + storeId + " (" + defaultStore + ")");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
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
    public  void setFlowersList_c(List<Flower> flowerList)
    {
        flowersList_c = flowerList;
    }
    int add_flower_flag=0;
    String flower_name="";

    // Filter state tracking
    private double currentMinPrice = 0.0;
    private double currentMaxPrice = 300.0;
    private Set<String> currentSelectedColors = new HashSet<>();
    private Set<String> currentSelectedCategories = new HashSet<>();
    private String currentSortOption = "Name (A-Z)";

    @FXML
    void initialize() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            System.out.println("CatalogController_employee registered");
        } else {
            System.out.println("CatalogController_employee already registered");
        }
        System.out.println("CatalogController employee initialized");

        Stores.getItems().addAll("Haifa", "Krayot","Nahariyya","network");

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
        
        // Add event listener for combo box selection changes
      /*  combo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            try {
                combo_choose(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/
        
        // Add event listener for store selection changes
       /* Stores.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            updateCatalogForSelectedStore();
        });*/
    }


    @FXML
    void gotoEmployeeAcc(ActionEvent event){
        if (user == null) {
            System.out.println("No user connected");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("my_account.fxml"));
            MyAccountController controller = loader.getController();
            controller.setCurrentUser(user);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Employee Account");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setCatalogSorting(List<Flower> flowerList)
    {
        flowersList_sorting = flowerList;

        Platform.runLater(() -> {
            System.out.println("=== EMPLOYEE SET CATALOG SORTING CALLED ===");
            System.out.println("Flower list size: " + (flowerList == null ? 0 : flowerList.size()));
            System.out.println("Current store selection: " + Stores.getValue());
            
            if (catalogFlowPane == null) {
                System.out.println("catalogFlowPane is NULL! Check FXML wiring.");
                return;
            }
            
            flowersList_c = flowerList;
            System.out.println("setCatalogSorting called with " + (flowerList == null ? 0 : flowerList.size()) + " flowers");
            catalogFlowPane.getChildren().clear();
            if (flowerList == null || flowerList.isEmpty()) {
                System.out.println("No flowers to display in catalog.");
                // Optionally, add a label to the pane to indicate empty catalog
                Label emptyLabel = new Label("No flowers available for this selection.");
                emptyLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 18px;");
                catalogFlowPane.getChildren().add(emptyLabel);
                return;
            }

            // Check if we're in network view
            String selectedStore = Stores.getValue();
            boolean isNetworkView = selectedStore != null && selectedStore.equals("network");

            for (Flower f : flowersList_c) {
                VBox card = new VBox(5);
                card.setPrefWidth(160);
                card.setStyle("-fx-background-color: #fff; -fx-border-color: #c8a2c8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10;");

                ImageView imageView = new ImageView();
                imageView.setFitWidth(140);
                imageView.setFitHeight(90);
                // Load image from the flower's image field in the database
                setImageFromDatabase(imageView, f.getImage(), f.getFlowerType());

                Label nameLabel = new Label(f.getFlowerName());
                nameLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 18px;");

                Label typeLabel = new Label(f.getFlowerType());
                typeLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 14px;");

                // Add color label
                Label colorLabel = new Label("Color: " + (f.getColor() != null ? f.getColor() : "N/A"));
                colorLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 12px;");

                // Add category label
                Label categoryLabel = new Label("Category: " + (f.getCategory() != null ? f.getCategory() : "N/A"));
                categoryLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 12px;");

                TextField priceField = new TextField(String.format("%.2f", f.getFlowerPrice()));
                priceField.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 16px;");

                // Make price field editable only in network view
                priceField.setEditable(isNetworkView);
                if (isNetworkView) {
                    priceField.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 16px; -fx-background-color: #f0f0f0;");
                    priceField.setPromptText("Click to edit price");

                    // Add event handler for price updates
                    priceField.setOnAction(e -> {
                        try {
                            double newPrice = Double.parseDouble(priceField.getText());
                            if (newPrice > 0) {
                                updateFlowerPrice(f, newPrice);
                            } else {
                                // Reset to original price if invalid
                                priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                                System.err.println("Invalid price: must be greater than 0");
                            }
                        } catch (NumberFormatException ex) {
                            // Reset to original price if invalid format
                            priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                            System.err.println("Invalid price format");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                    // Add focus lost handler for better UX
                    priceField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                        if (!isNowFocused) {
                            try {
                                double newPrice = Double.parseDouble(priceField.getText());
                                if (newPrice > 0) {
                                    updateFlowerPrice(f, newPrice);
                                } else {
                                    priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                                }
                            } catch (NumberFormatException ex) {
                                priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } else {
                    priceField.setEditable(false);
                }

                Text priceBeforeSale = new Text();
                priceBeforeSale.setStyle("-fx-fill: LIGHTPINK; -fx-strikethrough: true; -fx-font-size: 14px;");
                if (f.isSale()) {
                    priceBeforeSale.setVisible(true);
                    int discount_percent = f.getDiscount();
                    double remainingPercent = 100.0 - discount_percent;
                    double originalPrice = f.getFlowerPrice() * 100.0 / remainingPercent;
                    priceBeforeSale.setText(String.format("%.2f", originalPrice));
                } else {
                    priceBeforeSale.setVisible(false);
                }

                // Add delete button for employee
                Button deleteButton = new Button("Delete");
                deleteButton.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-size: 12px;");
                deleteButton.setOnAction(e -> {
                    try {
                        // Show confirmation dialog
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Flower");
                        alert.setHeaderText("Confirm Deletion");
                        alert.setContentText("Are you sure you want to delete '" + f.getFlowerName() + "' from the selected store?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            deleteFlower(f);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                card.getChildren().addAll(imageView, nameLabel, typeLabel, colorLabel, categoryLabel, priceBeforeSale, priceField, deleteButton);
                catalogFlowPane.getChildren().add(card);
            }
            System.out.println("Catalog sorting updated with " + catalogFlowPane.getChildren().size() + " cards.");
            System.out.println("=== EMPLOYEE SET CATALOG SORTING COMPLETED ===");
        });
    }

    public void clearCatalog() {
        if (catalogFlowPane != null) {
            catalogFlowPane.getChildren().clear();
        }
        System.out.println("Catalog cleared.");
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
        System.out.println("=== EMPLOYEE CATALOG CONTROLLER: ADD FLOWER EVENT RECEIVED ===");
        System.out.println("Event catalog type: " + event.get_catalog_type());
        System.out.println("Current type: " + type);
        System.out.println("Current store selection: " + Stores.getValue());
        System.out.println("Event flowers count: " + (event.get_flowers() != null ? event.get_flowers().size() : "null"));

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
        
        // Special handling for delete events (catalog_type = -1)
        if (event.get_catalog_type() == -1) {
            System.out.println("*** NETWORK DELETE EVENT PROCESSING (EMPLOYEE) ***");
            System.out.println("Updating UI for all clients with " + event.get_flowers().size() + " flowers");
            
            // Store current store selection
            String currentStore = Stores.getValue();
            
            // Request fresh data for the current store to ensure we have the latest information
            if (currentStore != null) {
                try {
                    if (currentStore.equals("network")) {
                        // For network view, use the data from the event
                        if (event.get_flowers() != null) {
                            setCatalogData(event.get_flowers());
                        }
                    } else {
                        // For specific store, request fresh data from server
                        int currentStoreId = getCurrentStoreId(currentStore);
                        if (currentStoreId != -1) {
                            String message = "get_catalog_" + currentStoreId;
                            SimpleClient.getClient().sendToServer(message);
                            System.out.println("Requested fresh catalog for store ID: " + currentStoreId + " (" + currentStore + ") after delete event");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error requesting fresh catalog data after delete event: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("*** NETWORK DELETE EVENT PROCESSED (EMPLOYEE) ***");
            return;
        }
        
        // Only reload if this event is for our current store selection
        String selectedStore = Stores.getValue();
        if (selectedStore != null) {
            int currentStoreId = getCurrentStoreId(selectedStore);
            System.out.println("Current store ID: " + currentStoreId);
            System.out.println("Event catalog type: " + event.get_catalog_type());
            
            if (event.get_catalog_type() == currentStoreId) {
                System.out.println("Add flower event matches current store, updating UI");
                // Update the UI with the new data from the event
                setCatalogData(event.get_flowers());
            } else {
                System.out.println("Add flower event does not match current store. Event type: " + event.get_catalog_type() + ", Current store ID: " + currentStoreId);
            }
        } else {
            System.out.println("No store selected, cannot process add flower event");
        }
    }

    @Subscribe
    public void handleCatalogUpdate(discount_for_1_flower event)throws IOException
    {
        System.out.println("=== DISCOUNT EVENT RECEIVED ===");
        System.out.println("Event catalog type: " + event.get_catalog_type());
        System.out.println("Current type: " + type);
        System.out.println("Current store selection: " + Stores.getValue());
        System.out.println("Flower name: " + event.get_flower_name());
        
        // Special handling for network operations (catalog_type = -1)
        if (event.get_catalog_type() == -1) {
            System.out.println("*** NETWORK DISCOUNT EVENT PROCESSING ***");
            System.out.println("Updating UI for all clients with " + event.get_flowers().size() + " flowers");
            if (event.get_flowers() != null) {
                setCatalogData(event.get_flowers());
            }
            System.out.println("*** NETWORK DISCOUNT EVENT PROCESSED ***");
            return;
        }
        
        // For discount events (catalog_type = 4), update immediately for all clients
        if (event.get_catalog_type() == 4) {
            System.out.println("*** DISCOUNT EVENT PROCESSING - IMMEDIATE UPDATE ***");
            System.out.println("Updating catalog with " + event.get_flowers().size() + " flowers");
            
            // Store current store selection
            String currentStore = Stores.getValue();
            
            // Request fresh data for the current store to ensure we have the latest information
            if (currentStore != null) {
                try {
                    if (currentStore.equals("network")) {
                        // For network view, use the data from the event
                        if (event.get_flowers() != null) {
                            setCatalogData(event.get_flowers());
                        }
                    } else {
                        // For specific store, request fresh data from server
                        int currentStoreId = getCurrentStoreId(currentStore);
                        if (currentStoreId != -1) {
                            String message = "get_catalog_" + currentStoreId;
                            SimpleClient.getClient().sendToServer(message);
                            System.out.println("Requested fresh catalog for store ID: " + currentStoreId + " (" + currentStore + ") after discount event");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error requesting fresh catalog data after discount event: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("*** DISCOUNT EVENT PROCESSED - IMMEDIATE UPDATE ***");
            return;
        }
        
        // Only reload if this event is for our current store selection
        String selectedStore = Stores.getValue();
        if (selectedStore != null) {
            int currentStoreId = getCurrentStoreId(selectedStore);
            System.out.println("Current store ID: " + currentStoreId);
            System.out.println("Event catalog type: " + event.get_catalog_type());
            
            if (event.get_catalog_type() == currentStoreId) {
                System.out.println("Discount event matches current store, updating UI");
                // Update the UI with the new data from the event
                if (event.get_flowers() != null) {
                    System.out.println("Updating catalog with " + event.get_flowers().size() + " flowers");
                    setCatalogData(event.get_flowers());
                }
            } else {
                System.out.println("Discount event does not match current store. Event type: " + event.get_catalog_type() + ", Current store ID: " + currentStoreId);
            }
        } else {
            System.out.println("No store selected, cannot process discount event");
        }
    }

    @Subscribe
    public void handleCatalogUpdate(update_local_catalog event) {
        System.out.println("=== UPDATE LOCAL CATALOG EVENT ===");
        System.out.println("Event catalog type: " + event.get_catalog_type());
        System.out.println("Current selected store: " + Stores.getValue());

        // Special case: catalog_type = -1 means this is for store selection
        if (event.get_catalog_type() == -1) {
            handleAllFlowersForStoreSelection(event.get_flowers());
            return;
        }

        // Check if this update is for the store we're currently viewing
        String selectedStore = Stores.getValue();
        int currentStoreId = getCurrentStoreId(selectedStore);

        System.out.println("Current store ID: " + currentStoreId);
        System.out.println("Event store ID: " + event.get_catalog_type());

        // Update if it's for the store we're currently viewing
        if (currentStoreId == event.get_catalog_type()) {
            System.out.println("Updating catalog with fresh data from database");
            System.out.println("Flowers received: " + event.get_flowers().size());

            // Update the catalog with the fresh flower list from database
            flowersList_c = event.get_flowers();
            setCatalogData(event.get_flowers());
            
            // Also update the storesList to reflect the changes
            if (storesList != null) {
                Store updatedStore = storesList.stream()
                    .filter(s -> s.getId() == event.get_catalog_type())
                    .findFirst()
                    .orElse(null);
                if (updatedStore != null) {
                    updatedStore.setFlowersList(event.get_flowers());
                    System.out.println("Updated storesList for store ID " + event.get_catalog_type() + " with " + event.get_flowers().size() + " flowers");
                }
            }
        } else {
            System.out.println("Ignoring update - doesn't match current view");
        }
        System.out.println("=== END UPDATE LOCAL CATALOG EVENT ===");
    }

    /**
     * Reloads the catalog with fresh data from the database
     * This ensures all clients have the most current data after any catalog changes
     */
    private void reloadCatalogFromDatabase() {
        try {
            String selectedStore = Stores.getValue();
            if (selectedStore == null) {
                System.out.println("No store selected, cannot reload catalog");
                return;
            }
            
            System.out.println("Reloading catalog from database for store: " + selectedStore);
            
            // Request fresh data from server based on current store selection
            if (selectedStore.equals("network")) {
                // For network view, get all flowers
                SimpleClient.getClient().sendToServer("get_all_flowers");
            } else {
                // For specific store, get store-specific catalog
                int storeId = getCurrentStoreId(selectedStore);
                if (storeId > 0) {
                    SimpleClient.getClient().sendToServer("get_catalog_" + storeId);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reloading catalog from database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to apply sorting to a list of flowers
    private List<Flower> applySortingToFlowers(List<Flower> flowers, String sortType) {
        if (sortType == null || sortType.trim().isEmpty()) {
            return flowers.stream()
                .sorted((f1, f2) -> f1.getFlowerName().compareToIgnoreCase(f2.getFlowerName()))
                .toList();
        }

        String sel = sortType.trim().toLowerCase();
        if (sel.equals("price high to low")) {
            return flowers.stream()
                .sorted((f1, f2) -> Double.compare(f2.getFlowerPrice(), f1.getFlowerPrice()))
                .toList();
        } else if (sel.equals("price low to high")) {
            return flowers.stream()
                .sorted((f1, f2) -> Double.compare(f1.getFlowerPrice(), f2.getFlowerPrice()))
                .toList();
        } else {
            // Default: sort by name (A-Z)
            return flowers.stream()
                .sorted((f1, f2) -> f1.getFlowerName().compareToIgnoreCase(f2.getFlowerName()))
                .toList();
        }
    }

    // Helper method to get current store ID based on selected store
    public int getCurrentStoreId(String selectedStore) {
        if (selectedStore == null) return -1;

        switch (selectedStore) {
            case "Haifa": return 1;
            case "Krayot": return 2;
            case "Nahariyya": return 3;
            case "network": return 4;
            default: return -1;
        }
    }

    // Handle server response for successful deletion
    @Subscribe
    public void handleServerResponse(String response) {
        if (response.startsWith("Flower '") && response.contains("' removed from store '")) {
            // Server confirmed deletion, catalog will be updated via other events
            System.out.println("Server confirmed deletion: " + response);
        }
    }

    public void setCatalogData(List<Flower> flowerList) {
        System.out.println("=== EMPLOYEE SET CATALOG DATA CALLED ===");
        System.out.println("Flower list size: " + (flowerList == null ? 0 : flowerList.size()));
        System.out.println("Current store selection: " + Stores.getValue());
        
        Platform.runLater(() -> {
            System.out.println("=== PLATFORM.RUNLATER EXECUTING ===");
            if (catalogFlowPane == null) {
                System.out.println("catalogFlowPane is NULL! Check FXML wiring.");
                return;
            }
            flowersList_c = flowerList;
            System.out.println("setCatalogData called with " + (flowerList == null ? 0 : flowerList.size()) + " flowers");
            catalogFlowPane.getChildren().clear();
            if (flowerList == null || flowerList.isEmpty()) {
                System.out.println("No flowers to display in catalog.");
                // Optionally, add a label to the pane to indicate empty catalog
                Label emptyLabel = new Label("No flowers available for this selection.");
                emptyLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 18px;");
                catalogFlowPane.getChildren().add(emptyLabel);
                return;
            }

            // Check if we're in network view
            String selectedStore = Stores.getValue();
            boolean isNetworkView = selectedStore != null && selectedStore.equals("network");

            for (Flower f : flowersList_c) {
                VBox card = new VBox(5);
                card.setPrefWidth(160);
                card.setStyle("-fx-background-color: #fff; -fx-border-color: #c8a2c8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10;");

                ImageView imageView = new ImageView();
                imageView.setFitWidth(140);
                imageView.setFitHeight(90);
                // Load image from the flower's image field in the database
                setImageFromDatabase(imageView, f.getImage(), f.getFlowerType());

                Label nameLabel = new Label(f.getFlowerName());
                nameLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 18px;");

                Label typeLabel = new Label(f.getFlowerType());
                typeLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 14px;");

                // Add color label
                Label colorLabel = new Label("Color: " + (f.getColor() != null ? f.getColor() : "N/A"));
                colorLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 12px;");

                // Add category label
                Label categoryLabel = new Label("Category: " + (f.getCategory() != null ? f.getCategory() : "N/A"));
                categoryLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 12px;");

                TextField priceField = new TextField(String.format("%.2f", f.getFlowerPrice()));
                priceField.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 16px;");

                // Make price field editable only in network view
                priceField.setEditable(isNetworkView);
                if (isNetworkView) {
                    priceField.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 16px; -fx-background-color: #f0f0f0;");
                    priceField.setPromptText("Click to edit price");

                    // Add event handler for price updates
                    priceField.setOnAction(e -> {
                        try {
                            double newPrice = Double.parseDouble(priceField.getText());
                            if (newPrice > 0) {
                                updateFlowerPrice(f, newPrice);
                            } else {
                                // Reset to original price if invalid
                                priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                                System.err.println("Invalid price: must be greater than 0");
                            }
                        } catch (NumberFormatException ex) {
                            // Reset to original price if invalid format
                            priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                            System.err.println("Invalid price format");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                    // Add focus lost handler for better UX
                    priceField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                        if (!isNowFocused) {
                            try {
                                double newPrice = Double.parseDouble(priceField.getText());
                                if (newPrice > 0) {
                                    updateFlowerPrice(f, newPrice);
                                } else {
                                    priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                                }
                            } catch (NumberFormatException ex) {
                                priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } else {
                    priceField.setEditable(false);
                }

                Text priceBeforeSale = new Text();
                priceBeforeSale.setStyle("-fx-fill: LIGHTPINK; -fx-strikethrough: true; -fx-font-size: 14px;");
                if (f.isSale()) {
                    priceBeforeSale.setVisible(true);
                    int discount_percent = f.getDiscount();
                    double remainingPercent = 100.0 - discount_percent;
                    double originalPrice = f.getFlowerPrice() * 100.0 / remainingPercent;
                    priceBeforeSale.setText(String.format("%.2f", originalPrice));
                } else {
                    priceBeforeSale.setVisible(false);
                }

                // Add delete button for employee
                Button deleteButton = new Button("Delete");
                deleteButton.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-size: 12px;");
                deleteButton.setOnAction(e -> {
                    try {
                        // Show confirmation dialog
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Flower");
                        alert.setHeaderText("Confirm Deletion");
                        alert.setContentText("Are you sure you want to delete '" + f.getFlowerName() + "' from the selected store?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            deleteFlower(f);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                card.getChildren().addAll(imageView, nameLabel, typeLabel, colorLabel, categoryLabel, priceBeforeSale, priceField, deleteButton);
                catalogFlowPane.getChildren().add(card);
            }
            System.out.println("Catalog updated with " + catalogFlowPane.getChildren().size() + " cards.");
            System.out.println("=== EMPLOYEE SET CATALOG DATA COMPLETED ===");
        });
    }

    // Add method to update flower price
    private void updateFlowerPrice(Flower flower, double newPrice) throws IOException {
        System.out.println("Updating price for flower '" + flower.getFlowerName() + "' from " + flower.getFlowerPrice() + " to " + newPrice);

        // Update the flower object
        flower.setFlowerPrice(newPrice);

        // Send update request to server
        String message = "update_flower_price_" + flower.getId() + "_" + newPrice;
        SimpleClient.getClient().sendToServer(message);

        // Show success message
        Success success = new Success("Price updated for '" + flower.getFlowerName() + "' to ₪" + String.format("%.2f", newPrice));
        EventBus.getDefault().post(new SuccessEvent(success));
    }

    // Add delete flower method
    private void deleteFlower(Flower flower) throws IOException {
        // Get the selected store from the filter
        String selectedStore = Stores.getValue();

        if (selectedStore != null && selectedStore.equals("network")) {
            // For network view, delete from Flowers table and all store_flowers entries
            String message = "delete_flower_from_network_" + flower.getId();
            SimpleClient.getClient().sendToServer(message);
            System.out.println("Requested deletion of flower ID " + flower.getId() + " from network (Flowers table and all store_flowers entries)");
        } else {
            // For store view, delete from specific store only
            String storeIdentifier = "";

            // Convert store name to store identifier
            if (selectedStore != null) {
                switch (selectedStore) {
                    case "Haifa":
                        storeIdentifier = "1";
                        break;
                    case "Krayot":
                        storeIdentifier = "2";
                        break;
                    case "Nahariyya":
                        storeIdentifier = "3";
                        break;
                    case "network":
                        storeIdentifier = "4";
                        break;
                    default:
                        storeIdentifier = String.valueOf(type);
                        break;
                }
            } else {
                storeIdentifier = String.valueOf(type);
            }

            // Send delete request to server with flower ID instead of name
            System.out.println(storeIdentifier+ "," + selectedStore+ "," + flower.getId()+ "," +flower.getFlowerName());
            String message = "delete_flower_from_store_" + flower.getId() + "_" + storeIdentifier;
            SimpleClient.getClient().sendToServer(message);
        }

        // Show success message
        Success success = new Success("Flower '" + flower.getFlowerName() + "' removed from " + selectedStore + " store!");
        EventBus.getDefault().post(new SuccessEvent(success));
    }

    // Add setImageFromDatabase method to load images from database
    private void setImageFromDatabase(ImageView imageView, String databaseImagePath, String flowerType) {
        try {
            String imagePath = null;

            // If database has a valid image path, use it
            if (databaseImagePath != null && !databaseImagePath.trim().isEmpty()) {
                imagePath = databaseImagePath;
            } else {
                // Fallback to flower type if no image path in database
                imagePath = "images/" + flowerType + ".png";
            }

            // Handle different path formats
            String finalImagePath;
            if (imagePath.startsWith("images/")) {
                // Convert to resource path format
                finalImagePath = "/" + imagePath;
            } else if (imagePath.startsWith("/")) {
                // Already in resource path format
                finalImagePath = imagePath;
            } else {
                // Assume it's a relative path, add images/ prefix
                finalImagePath = "/images/" + imagePath;
            }

            System.out.println("Loading image from database path: " + imagePath + " -> " + finalImagePath);

            Image image = new Image(getClass().getResourceAsStream(finalImagePath));
            imageView.setImage(image);

        } catch (Exception e) {
            System.err.println("Failed to load image from database path: " + databaseImagePath + " for flower type: " + flowerType);
            // Try fallback to flower type
            setImageFallback(imageView, flowerType);
        }
    }

    // Fallback method for loading images by flower type
    private void setImageFallback(ImageView imageView, String flowerType) {
        try {
            String fallbackPath = "/images/" + flowerType + ".png";
            System.out.println("Trying fallback image: " + fallbackPath);
            Image image = new Image(getClass().getResourceAsStream(fallbackPath));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load fallback image for flower type: " + flowerType);
            // Load no_photo image as final fallback
            setNoPhotoImage(imageView);
        }
    }

    // Method to load no_photo image
    private void setNoPhotoImage(ImageView imageView) {
        try {
            String noPhotoPath = "/images/no_photo.png";
            System.out.println("Loading no_photo image: " + noPhotoPath);
            Image image = new Image(getClass().getResourceAsStream(noPhotoPath));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load no_photo image as well");
            // Create a placeholder image or leave empty
        }
    }

    // Legacy setImage method for backward compatibility
    private void setImage(ImageView imageView, String imagePath) {
        setImageFromDatabase(imageView, imagePath, "");
    }

    // Add storesList field and setter
    private List<Store> storesList;
    public void setStoresList(List<Store> stores) {
        this.storesList = stores;
    }

    @FXML
    void stores_choose(ActionEvent event) throws IOException
    {
        System.out.println("=== EMPLOYEE STORE SELECTION CHANGED ===");
        String selected = Stores.getValue();
        if (selected == null) {
            System.out.println("No store selected");
            return;
        }

        System.out.println("Selected store: " + selected);




        // Clear current filter state
        currentMinPrice = 0.0;
        currentMaxPrice = 300.0;
        currentSelectedColors.clear();
        currentSelectedCategories.clear();
        currentSortOption = "Name (A-Z)";

        // Request fresh data from database based on selected store
        if (selected.equals("network")) {
            // For network view, get all flowers from Flowers table
            String message = "get_all_flowers";
            SimpleClient.getClient().sendToServer(message);
            System.out.println("Requested all flowers from Flowers table for network view");
        } else {
            // Get store ID for the selected store
            int storeId = getCurrentStoreId(selected);
            if (storeId != -1) {
                // Request fresh flower list from database for this store
                String message = "get_catalog_" + storeId;
                SimpleClient.getClient().sendToServer(message);
                System.out.println("Requested fresh catalog for store ID: " + storeId + " (" + selected + ")");
            } else {
                System.err.println("Invalid store selection: " + selected);
            }
        }
    }
//mark sort
 /*   @FXML
    public void combo_choose(ActionEvent actionEvent) throws IOException {
        System.out.println("combo_choose CALLED");
        String selectedSort = combo.getValue();
        System.out.println("Selected sort option: " + selectedSort);
        
        if (selectedSort == null || selectedSort.equals("Sort")) {
            System.out.println("No sorting selected, showing original order");
            return;
        }
        
        if (flowersList_c == null || flowersList_c.isEmpty()) {
            System.out.println("No flowers to sort");
            return;
        }
        
        // Apply sorting using the existing method
        List<Flower> sortedFlowers = applySortingToFlowers(flowersList_c, selectedSort);
        
        // Update the display with sorted flowers
        setCatalogSorting(sortedFlowers);
        System.out.println("Sorting applied successfully");
    }*/
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
        // Handle new complain
    }

    boolean sale=false;

    // Remove duplicate end_sale_btn declaration since it's already declared above

    @FXML
    void end_sale_action(ActionEvent event) {
        // End sale functionality
        sale = false;
        end_sale_btn.setVisible(false);
        // Update catalog to remove sale prices
        if (flowersList_c != null) {
            setCatalogData(flowersList_c);
        }
    }

    // Add missing action methods
    @FXML
    void add_flower_action(ActionEvent event) throws IOException {
        String selectedStore = Stores.getValue();
        boolean isNetworkMode = selectedStore != null && selectedStore.equals("network");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("add_flower.fxml"));
        Parent root = loader.load();
        AddFlower_Controller controller = loader.getController();
        controller.setCatalogController(this);
        controller.setNetworkMode(isNetworkMode);

        if (!isNetworkMode) {
            fetchAvailableFlowersForStore(selectedStore, controller);
        }

        Stage stage = new Stage();
        stage.setTitle(isNetworkMode ? "Add New Item" : "Add Item to Store");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.show();
    }

    private void fetchAvailableFlowersForStore(String selectedStore, AddFlower_Controller controller) {
        try {
            Store currentStore = storesList.stream()
                .filter(s -> s.getStoreName().toLowerCase().contains(selectedStore.toLowerCase()))
                .findFirst()
                .orElse(null);
            if (currentStore == null) {
                System.err.println("Store not found: " + selectedStore);
                return;
            }
            String message = "get_all_flowers_for_store_selection";
            SimpleClient.getClient().sendToServer(message);
            this.pendingAddFlowerController = controller;
            this.pendingStoreName = selectedStore;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private AddFlower_Controller pendingAddFlowerController;
    private String pendingStoreName;
    
    public void setPendingAddFlowerController(AddFlower_Controller controller) {
        this.pendingAddFlowerController = controller;
    }
    
    public void setPendingStoreName(String storeName) {
        this.pendingStoreName = storeName;
    }

    public void handleAllFlowersForStoreSelection(List<Flower> allFlowers) {
        if (pendingAddFlowerController != null && pendingStoreName != null) {
            // Get the current store's flower list from the catalog instead of storesList
            List<Flower> currentStoreFlowers = flowersList_c;
            
            // If we don't have the current store's flowers, try to get them from storesList as fallback
            if (currentStoreFlowers == null || currentStoreFlowers.isEmpty()) {
                Store currentStore = storesList.stream()
                    .filter(s -> s.getStoreName().toLowerCase().contains(pendingStoreName.toLowerCase()))
                    .findFirst()
                    .orElse(null);
                if (currentStore != null) {
                    currentStoreFlowers = currentStore.getFlowersList();
                }
            }
            
            // Create a final reference for the lambda expression
            final List<Flower> finalCurrentStoreFlowers = currentStoreFlowers;
            
            if (finalCurrentStoreFlowers != null) {
                // Filter out flowers that are already in the current store
                List<Flower> availableFlowers = allFlowers.stream()
                    .filter(flower -> finalCurrentStoreFlowers.stream()
                        .noneMatch(storeFlower -> storeFlower.getId() == flower.getId()))
                    .toList();
                pendingAddFlowerController.setAvailableFlowers(availableFlowers);
                System.out.println("Set " + availableFlowers.size() + " available flowers for store: " + pendingStoreName);
                System.out.println("Current store has " + finalCurrentStoreFlowers.size() + " flowers");
                System.out.println("Total flowers in database: " + allFlowers.size());
            }
            pendingAddFlowerController = null;
            pendingStoreName = null;
        }
    }

    @FXML
    void discount_action(ActionEvent event) throws IOException {
        // Check if user is in network mode (type == 4)
        if (type != 4) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Access Denied");
            alert.setHeaderText("Network Access Required");
            alert.setContentText("You can only set discounts when viewing the network catalog. Please switch to 'network' view.");
            alert.showAndWait();
            return;
        }
        
        // Check if current store selection is "network"
        String selectedStore = Stores.getValue();
        if (selectedStore == null || !selectedStore.equals("network")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Access Denied");
            alert.setHeaderText("Network View Required");
            alert.setContentText("Please select 'network' from the store filter to set discounts.");
            alert.showAndWait();
            return;
        }
        
        // Open discount dialog
        FXMLLoader loader = new FXMLLoader(getClass().getResource("discount_scene.fxml"));
        Parent root = loader.load();
        discount_Controller controller = loader.getController();
        controller.setCatalogController(this);
        Stage stage = new Stage();
        stage.setTitle("Set Flower Discount");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.show();
    }

    public void receivediscount(int percentDiscount, Object o) {
    }

    public void receiveNewFlower(Flower flower) {
        // Handle new flower added to database
        System.out.println("New flower received: " + flower.getFlowerName());

        // Refresh the catalog to show the new flower
        Platform.runLater(() -> {
            String selected = Stores.getValue();
            if (selected != null && selected.equals("network")) {
                // If we're in network view, refresh the catalog
                try {
                    String message = "get_all_flowers";
                    SimpleClient.getClient().sendToServer(message);
                    System.out.println("Requested fresh flower list after adding new flower");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Add updateCatalogForSelectedStore method
   /* private void updateCatalogForSelectedStore() {
        System.out.println("updateCatalogForSelectedStore CALLED");
        Platform.runLater(() -> {
            String selected_store = Stores.getValue();
            if (selected_store == null || storesList == null){
                System.out.println("STOP");
                System.out.println(selected_store);
                System.out.println(storesList);
                return;
            }
            List<Flower> filtered;
            if (selected_store.equals("network")) {
                filtered = storesList.stream()
                    .flatMap(store -> store.getFlowersList().stream())
                    .distinct()
                    .toList();
            } else {
                Store store = storesList.stream()
                    .filter(s -> s.getStoreName().toLowerCase().contains(selected_store.toLowerCase()))
                    .findFirst()
                    .orElse(null);
                if (store == null){
                    System.out.println("STOP2");
                    return;
                }
                filtered = store.getFlowersList();
            }
            // Default: sort by name (A-Z)
            filtered = filtered.stream()
                .sorted((f1, f2) -> f1.getFlowerName().compareToIgnoreCase(f2.getFlowerName()))
                .toList();
            // If a price sort is selected, override with price sort
            String selected = combo.getValue();
            System.out.println("Sort Combo value: '" + selected + "'");
            System.out.println("Before sort: " + filtered.stream().map(Flower::getFlowerPrice).toList());
            if (selected != null) {
                String sel = selected.trim().toLowerCase();
                if (sel.equals("price high to low")) {
                    filtered = filtered.stream()
                        .sorted((f1, f2) -> Double.compare(f2.getFlowerPrice(), f1.getFlowerPrice()))
                        .toList();
                } else if (sel.equals("price low to high")) {
                    filtered = filtered.stream()
                        .sorted((f1, f2) -> Double.compare(f1.getFlowerPrice(), f2.getFlowerPrice()))
                        .toList();
                }
            }
            System.out.println("After sort: " + filtered.stream().map(Flower::getFlowerPrice).toList());
            System.out.println("Order after sort: " + filtered.stream().map(Flower::getFlowerName).toList());
            setCatalogData(filtered);
        });
    }*/

    public String getSelectedStore() {
        return Stores.getValue();
    }
    public int getStoreIdByName(String name) {
        Store store = storesList.stream()
            .filter(s -> s.getStoreName().equalsIgnoreCase(name))
            .findFirst().orElse(null);
        return store != null ? store.getId() : -1;
    }

    // New method for filtered catalog data
    public void setFilteredCatalogData(List<Flower> filteredFlowerList) {
        System.out.println("=== EMPLOYEE SET FILTERED CATALOG DATA CALLED ===");
        System.out.println("Filtered flower list size: " + (filteredFlowerList == null ? 0 : filteredFlowerList.size()));
        
        Platform.runLater(() -> {
            if (catalogFlowPane == null) {
                System.out.println("catalogFlowPane is NULL! Check FXML wiring.");
                return;
            }
            
            catalogFlowPane.getChildren().clear();
            if (filteredFlowerList == null || filteredFlowerList.isEmpty()) {
                System.out.println("No flowers match the current filters.");
                Label emptyLabel = new Label("No flowers match your current filters.\nTry adjusting your filter criteria.");
                emptyLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 16px; -fx-text-alignment: center;");
                catalogFlowPane.getChildren().add(emptyLabel);
                return;
            }
            
            // Check if we're in network view
            String selectedStore = Stores.getValue();
            boolean isNetworkView = selectedStore != null && selectedStore.equals("network");
            
            for (Flower f : filteredFlowerList) {
                VBox card = new VBox(5);
                card.setPrefWidth(160);
                card.setStyle("-fx-background-color: #fff; -fx-border-color: #c8a2c8; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 10;");

                ImageView imageView = new ImageView();
                imageView.setFitWidth(140);
                imageView.setFitHeight(90);
                setImageFromDatabase(imageView, f.getImage(), f.getFlowerType());

                Label nameLabel = new Label(f.getFlowerName());
                nameLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 18px;");

                Label typeLabel = new Label(f.getFlowerType());
                typeLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 14px;");

                // Add color label
                Label colorLabel = new Label("Color: " + (f.getColor() != null ? f.getColor() : "N/A"));
                colorLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 12px;");

                // Add category label
                Label categoryLabel = new Label("Category: " + (f.getCategory() != null ? f.getCategory() : "N/A"));
                categoryLabel.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 12px;");

                TextField priceField = new TextField(String.format("%.2f", f.getFlowerPrice()));
                priceField.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 16px;");

                // Make price field editable only in network view
                priceField.setEditable(isNetworkView);
                if (isNetworkView) {
                    priceField.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 16px; -fx-background-color: #f0f0f0;");
                    priceField.setPromptText("Click to edit price");

                    // Add event handler for price updates
                    priceField.setOnAction(e -> {
                        try {
                            double newPrice = Double.parseDouble(priceField.getText());
                            if (newPrice > 0) {
                                updateFlowerPrice(f, newPrice);
                            } else {
                                priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                                System.err.println("Invalid price: must be greater than 0");
                            }
                        } catch (NumberFormatException ex) {
                            priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                            System.err.println("Invalid price format");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });

                    // Add focus lost handler for better UX
                    priceField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                        if (!isNowFocused) {
                            try {
                                double newPrice = Double.parseDouble(priceField.getText());
                                if (newPrice > 0) {
                                    updateFlowerPrice(f, newPrice);
                                } else {
                                    priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                                }
                            } catch (NumberFormatException ex) {
                                priceField.setText(String.format("%.2f", f.getFlowerPrice()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } else {
                    priceField.setEditable(false);
                }

                Text priceBeforeSale = new Text();
                priceBeforeSale.setStyle("-fx-fill: LIGHTPINK; -fx-strikethrough: true; -fx-font-size: 14px;");
                if (f.isSale()) {
                    priceBeforeSale.setVisible(true);
                    int discount_percent = f.getDiscount();
                    double remainingPercent = 100.0 - discount_percent;
                    double originalPrice = f.getFlowerPrice() * 100.0 / remainingPercent;
                    priceBeforeSale.setText(String.format("%.2f", originalPrice));
                } else {
                    priceBeforeSale.setVisible(false);
                }

                // Add delete button for employee
                Button deleteButton = new Button("Delete");
                deleteButton.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white; -fx-font-size: 12px;");
                deleteButton.setOnAction(e -> {
                    try {
                        // Show confirmation dialog
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete Flower");
                        alert.setHeaderText("Confirm Deletion");
                        alert.setContentText("Are you sure you want to delete '" + f.getFlowerName() + "' from the selected store?");

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            deleteFlower(f);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });

                card.getChildren().addAll(imageView, nameLabel, typeLabel, colorLabel, categoryLabel, priceBeforeSale, priceField, deleteButton);
                catalogFlowPane.getChildren().add(card);
            }
            System.out.println("Filtered catalog updated with " + catalogFlowPane.getChildren().size() + " cards.");
            System.out.println("=== EMPLOYEE SET FILTERED CATALOG DATA COMPLETED ===");
        });
    }

    // Method to open filter window
    @FXML
    void openFilterWindow(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_filter.fxml"));
            Parent root = loader.load();
            
            CatalogFilterController filterController = loader.getController();
            filterController.setCatalogController(this);
            filterController.setOriginalFlowersList(flowersList_c);
            
            // Pass current filter state to restore UI
            filterController.setCurrentFilterState(
                currentMinPrice, 
                currentMaxPrice, 
                currentSelectedColors, 
                currentSelectedCategories, 
                currentSortOption
            );
            
            Stage filterStage = new Stage();
            filterStage.setTitle("Filter Catalog");
            filterStage.setScene(new Scene(root));
            filterStage.setResizable(false);
            filterController.setFilterStage(filterStage);
            
            filterStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error opening filter window: " + e.getMessage());
        }
    }
    
    /**
     * Updates the current filter state from the filter controller
     */
    public void updateCurrentFilterState(double minPrice, double maxPrice, Set<String> colors, Set<String> categories, String sortOption) {
        this.currentMinPrice = minPrice;
        this.currentMaxPrice = maxPrice;
        this.currentSelectedColors = new HashSet<>(colors);
        this.currentSelectedCategories = new HashSet<>(categories);
        this.currentSortOption = sortOption;
    }
}








