package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
    private Button  Mail_box;
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
    @FXML
    private AnchorPane custom;
    @FXML
    private Label cus_label;
    @FXML
    private ButtonBar btnPane;
    @FXML
    private ImageView mailbox_icon;
    @FXML
    private Button reportsBtn;

    boolean is_login=false;
    public void set_isLogin(boolean is_login) {
        this.is_login = is_login;
    }
    @FXML
    private ComboBox<String> Stores;
    
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
    private static LoginRegCheck user;
    public static void set_user(LoginRegCheck usr) {
        user=usr;
        System.out.println("the user is " + user.getUsername());
        System.out.println(" user send: " + user.get_send_complain());
        System.out.println(" user receive_answer: " + user.isReceive_answer());

        // Update mailbox icon when user is updated
        // Note: Since this is a static method, we need to trigger the update through EventBus
        EventBus.getDefault().post("update_mailbox_icon");
    }
    public connect_scene_Con getController() {
        return con;
    }
    public static LoginRegCheck getUser() {
        return user;
    }
    public void set_type(int value)
    {
        type=value;
        // Show reports button only for admin users (type=4)
        if (type == 4) {
            // Admin users can see reports
            if (reportsBtn != null) {
                reportsBtn.setVisible(true);
                reportsBtn.setManaged(true);
            }
        } else {
            // Hide reports button for non-admin users
            if (reportsBtn != null) {
                reportsBtn.setVisible(false);
                reportsBtn.setManaged(false);
            }
        }
    }
    public void set_sorting_type(int value)
    {
        sorting_type=value;
    }
    public void setFlowersList_c(List<Flower> flowerList)
    {
        flowersList_c = flowerList;
    }
    int add_flower_flag=0;
    String flower_name="";
    @FXML
    private ImageView cus_img;
    @FXML
    private Button bouqut_btn;
    private connect_scene_Con con;
    public void setController(connect_scene_Con controller) { con = controller; }

    private int currentIndex = 0;
    private final String[] imagePaths = {
            "/images/mix.jpg",
            "/images/lily_jaca.jpg",
            "/images/lily_daffodil.jpg",
            "/images/sunflower_rose.jpg",
            "/images/orchid_hyacinth.jpg",
            "/images/rose_tulip.jpg"
    };

    @FXML
    private FlowPane catalogFlowPane;

    // Filter state tracking
    private double currentMinPrice = 0.0;
    private double currentMaxPrice = 300.0;
    private Set<String> currentSelectedColors = new HashSet<>();
    private Set<String> currentSelectedCategories = new HashSet<>();
    private String currentSortOption = "Name (A-Z)";

    // Add flag to prevent infinite loops
    private static boolean isRefreshingUserState = false;

    // Add method to refresh user state from server
    private void refreshUserState() {
        if (user != null && !isRefreshingUserState) {
            try {
                isRefreshingUserState = true;
                System.out.println("[CatalogController] Refreshing user state for: " + user.getUsername());
                SimpleClient.getClient().sendToServer("getUserDetails_" + user.getUsername());

                // Don't send getComplaints here to avoid infinite loop
                // The complaint update event already contains the updated complaints
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Reset flag after a short delay to allow the response to be processed
                new Thread(() -> {
                    try {
                        Thread.sleep(1000); // Wait 1 second
                        isRefreshingUserState = false;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }
        }
    }

    @FXML
    void create_bouqut(ActionEvent event)
    {
        // Check if user is logged in (not a guest)
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Bouquet Creation Not Available");
            alert.setHeaderText("Login Required");
            alert.setContentText("You must be logged in to create bouquets. Please log in to your account.");
            alert.showAndWait();
            return;
        }

        // Check if user can create bouquet based on their store assignment
        int userStore = user.getStore();
        String currentCatalog = Stores.getValue();

        if (userStore != 4) {
            // User is assigned to a specific store - can only create bouquet in their own store
            String userStoreName = getStoreNameByNumber(userStore);
            if (!userStoreName.equals(currentCatalog)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Bouquet Creation Not Available");
                alert.setHeaderText("Store Access Required");
                alert.setContentText("You can only create bouquets when viewing your assigned store: " + userStoreName + ". Please switch to your store catalog.");
                alert.showAndWait();
                return;
            }
        }
        // If userStore == 4 (network), they can always create bouquet

        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bouquet.fxml"));
                Parent root = fxmlLoader.load();
                bouquet_controller regController = fxmlLoader.getController();
                regController.setCatalogController(this);
                regController.setFlowersList(flowersList_c);
                Stage stage = new Stage();
                stage.setTitle("Create New Account");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


    @FXML
    void initialize() {
        EventBus.getDefault().register(this);

        // Initialize mailbox icon after FXML injection is complete
        Platform.runLater(this::updateMailboxIcon);

        // Refresh user state from server
        refreshUserState();

        // Set up store selection
        Stores.getItems().addAll("Haifa", "Krayot", "Nahariyya", "network");
        Stores.setValue("network"); // Default to network view

        // Initialize arrays for flower display
        nameLabels = new Label[]{name_1, name_2, name_3, name_4, name_5, name_6, name_7, name_8, name_9, name_10, name_11, name_12};
        typeLabels = new Label[]{type_1, type_2, type_3, type_4, type_5, type_6, type_7, type_8, type_9, type_10, type_11, type_12};
        priceFields = new TextField[]{price_1, price_2, price_3, price_4, price_5, price_6, price_7, price_8, price_9, price_10, price_11, price_12};
        imageViews = new ImageView[]{pic_1, pic_2, pic_3, pic_4, pic_5, pic_6, pic_7, pic_8, pic_9, pic_10, pic_11, pic_12};
        price_Before_sale = new Text[]{price_1_before_sale, price_2_before_sale, price_3_before_sale, price_4_before_sale, price_5_before_sale, price_6_before_sale, price_7_before_sale, price_8_before_sale, price_9_before_sale, price_10_before_sale, price_11_before_sale, price_12_before_sale};

        // Set up cart button
        cart.setOnAction(e -> {
            try {
                openCart(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Hide reports button for non-employee users
        if ((user == null || !user.isType()) && reportsBtn != null) {
            reportsBtn.setVisible(false);
        }
    }

    // Add EventBus handler for user updates
    @Subscribe
    public void handleUserUpdate(UpdateUserEvent event) {
        System.out.println("[CatalogController] Received user update event");
        if (event.getUpdatedUser() != null && event.getUpdatedUser().getUsername().equals(user.getUsername())) {
            // Update the local user object
            CatalogController.user = event.getUpdatedUser();
            // Update mailbox icon
            Platform.runLater(this::updateMailboxIcon);
        }
    }

    // Add EventBus handler for GetUserDetails response
    @Subscribe
    public void handleGetUserDetails(GetUserDetails userDetails) {
        System.out.println("[CatalogController] Received GetUserDetails response");
        if (userDetails.getUser() != null && userDetails.getUser().getUsername().equals(user.getUsername())) {
            // Update the local user object with fresh data from server
            CatalogController.user = userDetails.getUser();
            // Update mailbox icon
            Platform.runLater(this::updateMailboxIcon);
            System.out.println("[CatalogController] Updated user state from server: " + user.getUsername() + ", isReceive_answer: " + user.isReceive_answer());
        }
    }

    // Add EventBus handler for complaint updates to refresh mailbox icon
    @Subscribe
    public void handleComplaintUpdate(ComplainUpdateEvent event) {
        System.out.println("[CatalogController] Received complaint update event");
        // Only refresh user state if we're not already refreshing and if the user might have new messages
        if (!isRefreshingUserState && user != null) {
            // Check if this user has any unread messages in the complaints
            List<Complain> complaints = event.getUpdatedItems();
            if (complaints != null) {
                boolean hasUnreadMessages = complaints.stream()
                    .anyMatch(c -> c.getClient().startsWith("answer to" + user.getUsername()));

                if (hasUnreadMessages && !user.isReceive_answer()) {
                    // User has unread messages but flag is not set, refresh user state
                    Platform.runLater(this::refreshUserState);
                } else if (!hasUnreadMessages && user.isReceive_answer()) {
                    // User has no unread messages but flag is set, refresh user state
                    Platform.runLater(this::refreshUserState);
                }
            }
        }
    }

    // TODO check can cause infinite loop
    @Subscribe
    public void handleComplaintsList(List<Complain> complaints) {
        System.out.println("[CatalogController] Received complaints list with " + (complaints != null ? complaints.size() : 0) + " complaints");
        if (user != null && complaints != null) {
            boolean hasUnreadMessages = complaints.stream()
                .anyMatch(c -> c.getClient().startsWith("answer to" + user.getUsername()));

            // Update user's receive_answer flag based on actual complaints
            if (hasUnreadMessages != user.isReceive_answer()) {
                user.set_receive_answer(hasUnreadMessages);
                Platform.runLater(this::updateMailboxIcon);
                System.out.println("[CatalogController] Updated mailbox icon based on complaints: " + hasUnreadMessages);
            }
        }
    }

    public void setCatalogSorting(List<Flower> flowerList) {
        Platform.runLater(() -> {
            flowersList_c = flowerList;
            catalogFlowPane.getChildren().clear();
            for (Flower f : flowersList_c) {
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
                priceField.setEditable(false);
                priceField.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 16px;");
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
                card.getChildren().addAll(imageView, nameLabel, typeLabel, colorLabel, categoryLabel, priceBeforeSale, priceField);
                card.setOnMouseClicked(e -> openFlowerDetails(f));
                catalogFlowPane.getChildren().add(card);
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
            imageViews[i].setImage(null); // מוחק את התמונה
        }
        nine_9.setVisible(false);
        ten_10.setVisible(false);
        eleven_11.setVisible(false);
        twelve_12.setVisible(false);
        System.out.println("Catalog cleared.");
    }
    @Subscribe
    public void handleCatalogUpdate(discount_for_1_flower event)throws IOException {
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
    public void handleCatalogUpdate(Add_flower_event event)throws IOException {
        System.out.println("=== CATALOG CONTROLLER: ADD FLOWER EVENT RECEIVED ===");
        System.out.println("Event catalog type: " + event.get_catalog_type());
        System.out.println("Current type: " + type);
        System.out.println("Current store selection: " + Stores.getValue());
        System.out.println("Event flowers count: " + (event.get_flowers() != null ? event.get_flowers().size() : "null"));

        if(event.get_flowers()==null)
        {
            System.out.println("the user is " + user.getUsername());
            System.out.println("the user that came from the event " +event.getUser().getUsername());
            if(user.getUsername().equals(event.getUser().getUsername()))
            {
                set_user(event.getUser());
                return;
            }
            return;
        }
        
        // Special handling for delete events (catalog_type = -1)
        if (event.get_catalog_type() == -1) {
            System.out.println("*** NETWORK DELETE EVENT PROCESSING ***");
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

            System.out.println("*** NETWORK DELETE EVENT PROCESSED ***");
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
                if (event.get_flowers() != null) {
                    System.out.println("Updating catalog with " + event.get_flowers().size() + " flowers");
                    setCatalogData(event.get_flowers());
                }
            } else {
                System.out.println("Add flower event does not match current store. Event type: " + event.get_catalog_type() + ", Current store ID: " + currentStoreId);
            }
        } else {
            System.out.println("No store selected, cannot process add flower event");
        }
    }
    
    @Subscribe
    public void handleCatalogUpdate(update_local_catalog event) {
        System.out.println("=== UPDATE LOCAL CATALOG EVENT ===");
        System.out.println("Event catalog type: " + event.get_catalog_type());
        System.out.println("Current selected store: " + Stores.getValue());
        System.out.println("Current type: " + type);
        
        // Check if this update is for the store we're currently viewing
        String selectedStore = Stores.getValue();
        if (selectedStore != null) {
            int currentStoreId = getCurrentStoreId(selectedStore);
            System.out.println("Current store ID: " + currentStoreId);
            System.out.println("Event store ID: " + event.get_catalog_type());
            
            if (currentStoreId == event.get_catalog_type()) {
                System.out.println("Update local catalog event matches current store, updating UI");
                // Update the UI with the new data from the event
                if (event.get_flowers() != null) {
                    flowersList_c = event.get_flowers();
                    setCatalogData(event.get_flowers());
                }
            } else {
                System.out.println("Update local catalog event does not match current store. Event type: " + event.get_catalog_type() + ", Current store ID: " + currentStoreId);
            }
        } else {
            System.out.println("No store selected, cannot process update local catalog event");
        }
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
    
    /**
     * Helper method to get current store ID based on selected store
     */
    private int getCurrentStoreId(String selectedStore) {
        if (selectedStore == null) return -1;

        switch (selectedStore) {
            case "Haifa": return 1;
            case "Krayot": return 2;
            case "Nahariyya": return 3;
            case "network": return 4;
            default: return -1;
        }
    }
    public void setCatalogData(List<Flower> flowerList) {
        System.out.println("=== SET CATALOG DATA CALLED ===");
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
            for (Flower f : flowersList_c) {
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
                priceField.setEditable(false);
                priceField.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 16px;");
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
                card.getChildren().addAll(imageView, nameLabel, typeLabel, colorLabel, categoryLabel, priceBeforeSale, priceField);
                card.setOnMouseClicked(e -> openFlowerDetails(f));
                catalogFlowPane.getChildren().add(card);
            }
            System.out.println("Catalog updated with " + catalogFlowPane.getChildren().size() + " cards.");
            System.out.println("=== SET CATALOG DATA COMPLETED ===");
        });
    }
    
    // New method for filtered catalog data
    public void setFilteredCatalogData(List<Flower> filteredFlowerList) {
        System.out.println("=== SET FILTERED CATALOG DATA CALLED ===");
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
                priceField.setEditable(false);
                priceField.setStyle("-fx-text-fill: #c8a2c8; -fx-font-size: 16px;");
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
                card.getChildren().addAll(imageView, nameLabel, typeLabel, colorLabel, categoryLabel, priceBeforeSale, priceField);
                card.setOnMouseClicked(e -> openFlowerDetails(f));
                catalogFlowPane.getChildren().add(card);
            }
            System.out.println("Filtered catalog updated with " + catalogFlowPane.getChildren().size() + " cards.");
            System.out.println("=== SET FILTERED CATALOG DATA COMPLETED ===");
        });
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
    private void openFlowerDetails(Flower flower) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order_page.fxml"));
            Parent root = loader.load();
            OrderPageController controller = loader.getController();
            if (this.user != null) {
                controller.setUser(this.user);
            }
            // Always pass the display name (Haifa, Krayot, Nahariyya, network)
            String selectedStore = Stores.getValue();
            controller.setStore(selectedStore); // Set store first
            controller.setFlower(flower);       // Then set flower (so updateUI sees the store)

            // Pass the current catalog flowers for validation
            List<Flower> currentCatalog = flowersList_sorting != null ? flowersList_sorting : flowersList_c;
            controller.setCurrentCatalogFlowers(currentCatalog);

            Stage stage = new Stage();
            stage.setTitle("Order Flower");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Warning warning = new Warning("Error opening order page");
            org.greenrobot.eventbus.EventBus.getDefault().post(new WarningEvent(warning));
        }
    }
    @FXML
    void stores_choose(ActionEvent event) throws IOException
    {
        System.out.println("=== STORE SELECTION CHANGED ===");
        String selected = Stores.getValue();
        if (selected == null) {
            System.out.println("No store selected");
            return;
        }

        System.out.println("Selected store: " + selected);

        // Clear combo selection and reset sorting


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

    //mark
    @FXML
    public void combo_choose(ActionEvent actionEvent) {
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
        
        // Create a copy of the current flower list for sorting
        List<Flower> sortedFlowers = new ArrayList<>(flowersList_c);
        
        // Apply sorting based on selection
        switch (selectedSort) {
            case "Price High to LOW":
                System.out.println("Sorting by price: High to Low");
                sortedFlowers.sort((f1, f2) -> Double.compare(f2.getFlowerPrice(), f1.getFlowerPrice()));
                break;
            case "Price Low to HIGH":
                System.out.println("Sorting by price: Low to High");
                sortedFlowers.sort((f1, f2) -> Double.compare(f1.getFlowerPrice(), f2.getFlowerPrice()));
                break;
            default:
                System.out.println("Unknown sort option: " + selectedSort);
                return;
        }
        
        // Update the display with sorted flowers
        setCatalogSorting(sortedFlowers);
        System.out.println("Sorting applied successfully");
    }

    private List<Store> storesList;
    public void setStoresList(List<Store> stores) {
        this.storesList = stores;
    }

    private void updateCatalogForSelectedStore() {
        System.out.println("=== UPDATE CATALOG FOR SELECTED STORE ===");
        String selected_store = Stores.getValue();
        if (selected_store == null) {
            System.out.println("No store selected, cannot update catalog");
            return;
        }

        System.out.println("Updating catalog for store: " + selected_store);

        // Request fresh data from database based on selected store
        try {
            if (selected_store.equals("network")) {
                // For network view, get all flowers from Flowers table
                String message = "get_all_flowers";
                SimpleClient.getClient().sendToServer(message);
                System.out.println("Requested all flowers from Flowers table for network view");
            } else {
                // Get store ID for the selected store
                int storeId = getCurrentStoreId(selected_store);
                if (storeId != -1) {
                    // Request fresh flower list from database for this store
                    String message = "get_catalog_" + storeId;
                    SimpleClient.getClient().sendToServer(message);
                    System.out.println("Requested fresh catalog for store ID: " + storeId + " (" + selected_store + ")");
                } else {
                    System.err.println("Invalid store selection: " + selected_store);
                }
            }
        } catch (IOException e) {
            System.err.println("Error requesting catalog update: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setImage(ImageView imageView, String flowerName) {
        try {
            // First try FlowerImages directory
            String imagePath = "/images/FlowerImages/" + flowerName + ".png";
            InputStream inputStream = getClass().getResourceAsStream(imagePath);
            
            if (inputStream == null) {
                // If not found in FlowerImages, try main images directory
                imagePath = "/images/" + flowerName + ".png";
                inputStream = getClass().getResourceAsStream(imagePath);
            }
            
            if (inputStream != null) {
                Image image = new Image(inputStream);
                imageView.setImage(image);
            } else {
                // Image not found in either location, use no_photo
                System.err.println("Failed to load image for: " + flowerName + " - image not found in either location");
                setNoPhotoImage(imageView);
            }
        } catch (Exception e) {
            System.err.println("Failed to load image for: " + flowerName);
            e.printStackTrace();
            // Use no_photo as fallback
            setNoPhotoImage(imageView);
        }
    }
    @FXML
    void gotoAcc(MouseEvent event) {

        if (type==0) {       //guest mode
            Platform.runLater(() -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration_screen.fxml"));
                    Parent root = fxmlLoader.load();
                    RegistrationController regController = fxmlLoader.getController();
                    regController.setCatalogController(this);
                    regController.setController(con);
                    Stage stage = new Stage();
                    stage.setTitle("Create New Account");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }
        else{       //login user mode
            if (MyAccountController.isMyAccountOpen()) {
                MyAccountController.myAccountStage.close();
            }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("my_account.fxml"));
                MyAccountController.setCurrentUser(user);
                MyAccountController.setCatalogController(this);
                Parent root = fxmlLoader.load();

                Stage stage = new Stage();
                MyAccountController.setMyAccountStage(stage);
                stage.setTitle("My Account");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
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
    public void receiveNewComplain(Complain complain)
    {
        complain.setClient(user.getUsername());
        change_sendOrRecieve_messages wrapper = new change_sendOrRecieve_messages(user, true,false);
        try {
            SimpleClient.getClient().sendToServer(wrapper);
            SimpleClient.getClient().sendToServer(complain);// try to send the complain to the DB
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void open_mail(ActionEvent event)throws IOException
    {
        if(type==0)
        {
            Warning warning = new Warning("not available for guest");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        if(user.isReceive_answer())
        {
            SimpleClient.getClient().sendToServer("I#want#to#see#my#answer_"+user.getUsername());
            System.out.println("I#want#to#see#my#answer_"+user.getUsername());
            return;
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mailbox");
            alert.setHeaderText("");
            alert.setContentText("You dont have any messages");
            alert.showAndWait();
        }

    }
    @FXML
    void complaint(ActionEvent event)throws IOException
    {
        if(type==0)
        {
            Warning warning = new Warning("guest dont allowed to send complaint");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;

        }
        if(user.get_send_complain() && user.isReceive_answer())
        {
            Warning warning = new Warning("You have a answer from the admin");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        if(user.get_send_complain() && !user.isReceive_answer())
        {
            Warning warning = new Warning("You already send a  complain.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

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
    void open_flower(ActionEvent event) {

        Button clickedButton = (Button) event.getSource();
        VBox graphicVBox = (VBox) clickedButton.getGraphic();
        String flowerName = "";

        for (Node node : graphicVBox.getChildren()) {
            if (node instanceof Label && ((Label) node).getId() != null && ((Label) node).getId().startsWith("name_")) {
                Label nameLabel = (Label) node;
                flowerName = nameLabel.getText();
                System.out.println("Flower name: " + flowerName);
                break;
            }
        }
        Flower targetFlower = null;
        if (flowersList_sorting != null) {
            for (Flower flower : flowersList_sorting) {
                if (flower.getFlowerName().equalsIgnoreCase(flowerName)) {
                    targetFlower = flower;
                    break;
                }
            }
        } else {
            for (Flower flower : flowersList_c) {
                if (flower.getFlowerName().equalsIgnoreCase(flowerName)) {
                    targetFlower = flower;
                    break;
                }
            }
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order_page.fxml"));
            Parent root = loader.load();
            OrderPageController orderController = loader.getController();
            orderController.setStore(Stores.getValue());
            orderController.setFlower(targetFlower);
            orderController.setUser(user);

            // Pass the current catalog flowers for validation
            List<Flower> currentCatalog = flowersList_sorting != null ? flowersList_sorting : flowersList_c;
            orderController.setCurrentCatalogFlowers(currentCatalog);

            Stage stage = new Stage();
            stage.setTitle("Order Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getFlowerIndex(String flowerName) {
        for (int i = 0; i < flowersList_c.size(); i++) {
            if (flowersList_c.get(i).getFlowerName().equals(flowerName)) {
                return i;
            }
        }
        return -1;
    }
    public void updateMailboxIcon() {
        System.out.println("[CatalogController] updateMailboxIcon called");
        System.out.println("[CatalogController] mailbox_icon is null: " + (mailbox_icon == null));
        System.out.println("[CatalogController] user is null: " + (user == null));

        // Check if mailbox_icon is null (FXML injection not complete yet)
        if (mailbox_icon == null) {
            System.out.println("[CatalogController] mailbox_icon is null, returning");
            return;
        }

        if (user != null) {
            System.out.println("[CatalogController] User: " + user.getUsername() + ", isReceive_answer: " + user.isReceive_answer());
            if (user.isReceive_answer()) {
                System.out.println("[CatalogController] Setting mailbox icon visible for user: " + user.getUsername());
                mailbox_icon.setVisible(true);
            } else {
                System.out.println("[CatalogController] Setting mailbox icon invisible for user: " + user.getUsername());
                mailbox_icon.setVisible(false);
            }
        } else {
            System.out.println("[CatalogController] User is null, setting mailbox icon invisible");
            mailbox_icon.setVisible(false);
        }
    }

    @FXML
    private void openCart(ActionEvent actionEvent) {
        System.out.println("CatalogController: openCart called");
        System.out.println("CatalogController: Current user is: " + (user != null ? user.getUsername() : "null"));

        if (CartController.isCartOpen()) {
            Warning warning = new Warning("The cart window is already open.");
            EventBus.getDefault().post(new WarningEvent(warning));
            CartController.setCartStage(CartController.cartStage); // bring to front
            CartController.cartStage.toFront();
            CartController.cartStage.requestFocus();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cart.fxml"));
            Parent root = loader.load();
            CartController cartController = loader.getController();
            cartController.setCartItems(OrderPageController.getCartItems());
            cartController.setCurrentUser(user);
            System.out.println("CatalogController: User passed to cart: " + (user != null ? user.getUsername() : "null"));
            cartController.setCatalogController(this);

            Stage stage = new Stage();
            CartController.setCartStage(stage);
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
    void openReports(ActionEvent event) throws IOException {
        // Only allow employees and admins to access reports
        if (user == null || !user.isType()) {
            Warning warning = new Warning("Access denied. Only employees and administrators can view reports.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("report_generator.fxml"));
            Parent root = fxmlLoader.load();

            ReportGeneratorController controller = fxmlLoader.getController();
            // Pass the current user to prevent session loss
            controller.setCurrentUser(user);

            Stage stage = new Stage();
            stage.setTitle("Report Generator");
            stage.setScene(new Scene(root));
            stage.setWidth(1200);
            stage.setHeight(900);

            // Set up cleanup when window is closed
            stage.setOnCloseRequest(e -> {
                controller.cleanup();
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    /**
     * Helper method to get store name by store number
     */
    private String getStoreNameByNumber(int storeNumber) {
        switch (storeNumber) {
            case 1: return "Haifa";
            case 2: return "Krayot";
            case 3: return "Nahariyya";
            case 4: return "network";
            default: return "Unknown";
        }
    }

    /**
     * Updates the bouquet button state based on the user's store assignment and current catalog view
     */
    private void updateBouquetButtonState(String selectedStore) {
        if (bouqut_btn != null && user != null) {
            int userStore = user.getStore();

            if (userStore == 4) {
                // Network users can always create bouquets
                bouqut_btn.setDisable(false);
                bouqut_btn.setStyle("-fx-background-color: #fdfdfd; -fx-text-fill: #000000;");
                bouqut_btn.setTooltip(null);
            } else {
                // Store-specific users can only create bouquets in their own store
                String userStoreName = getStoreNameByNumber(userStore);
                if (userStoreName.equals(selectedStore)) {
                    // User is viewing their own store - enable button
                    bouqut_btn.setDisable(false);
                    bouqut_btn.setStyle("-fx-background-color: #fdfdfd; -fx-text-fill: #000000;");
                    bouqut_btn.setTooltip(null);
                } else {
                    // User is viewing a different store - disable button
                    bouqut_btn.setDisable(true);
                    bouqut_btn.setStyle("-fx-background-color: #cccccc; -fx-text-fill: #666666;");
                    bouqut_btn.setTooltip(new Tooltip("You can only create bouquets in your assigned store: " + userStoreName));
                }
            }
        } else if (bouqut_btn != null) {
            // No user logged in - disable button
            bouqut_btn.setDisable(true);
            bouqut_btn.setStyle("-fx-background-color: #cccccc; -fx-text-fill: #666666;");
            bouqut_btn.setTooltip(new Tooltip("Please log in to create bouquets"));
        }
    }

    // Add EventBus handler for mailbox icon updates
    @Subscribe
    public void handleMailboxIconUpdate(String message) {
        if ("update_mailbox_icon".equals(message)) {
            Platform.runLater(this::updateMailboxIcon);
        }
    }
}








