package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CartItem;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.entities.UpdateUserEvent;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

public class CheckoutController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField cityField;
    @FXML private TextField streetAddressField;
    @FXML private TextField apartmentField;
    @FXML private ComboBox<String> deliveryTypeComboBox;
    @FXML private DatePicker deliveryDatePicker;
    @FXML private ComboBox<String> deliveryTimeComboBox;
    @FXML private TableView<CartItem> orderTable;
    @FXML private TableColumn<CartItem, String> nameColumn;
    @FXML private TableColumn<CartItem, String> typeColumn;
    @FXML private TableColumn<CartItem, Double> priceColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;
    @FXML private TableColumn<CartItem, Double> totalColumn;
    @FXML private Label orderTotal;
    @FXML private Label deliveryFeeLabel;
    @FXML private Button placeOrderButton;
    @FXML private Button cancelButton;
    
    // Greeting card elements
    @FXML private CheckBox includeGreetingCardCheckBox;
    @FXML private ComboBox<String> greetingCardBackgroundComboBox;
    @FXML private TextArea greetingCardMessageTextArea;
    @FXML private Label characterCountLabel;
    @FXML private Button previewGreetingCardButton;

    //check
    private CatalogController_employee catalogController;

    public void setCatalogController(CatalogController_employee controller) {
        this.catalogController = controller;
    }

    //end check
    private ObservableList<CartItem> orderItems;
    private LoginRegCheck currentUser; // Store the current user
    private static final double DELIVERY_FEE = 20.0;
    
    @FXML private Label discountLabel;
    
    private Stage previewStage = null;
    private GreetingCardPreviewController previewController = null;
    
    static Stage checkoutStage = null;
    public static boolean isCheckoutOpen() {
        return checkoutStage != null && checkoutStage.isShowing();
    }
    public static void setCheckoutStage(Stage stage) {
        checkoutStage = stage;
        if (checkoutStage != null) {
            checkoutStage.setOnHidden(e -> {
                checkoutStage = null;
                if (isGreetingCardPreviewOpen()) {
                    greetingCardPreviewStage.close();
                }
            });
        }
    }
    
    private static Stage greetingCardPreviewStage = null;
    public static boolean isGreetingCardPreviewOpen() {
        return greetingCardPreviewStage != null && greetingCardPreviewStage.isShowing();
    }
    public static void setGreetingCardPreviewStage(Stage stage) {
        greetingCardPreviewStage = stage;
        if (greetingCardPreviewStage != null) {
            greetingCardPreviewStage.setOnHidden(e -> greetingCardPreviewStage = null);
        }
    }
    
    public void initialize() {
        // Set up table columns
        nameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFlower().getFlowerName()));
        typeColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFlower().getFlowerType()));
        priceColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getFlower().getFlowerPrice()).asObject());
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());
        
        // Initialize order items list
        orderItems = FXCollections.observableArrayList();
        orderTable.setItems(orderItems);

        deliveryTypeComboBox.getItems().addAll("Delivery", "Pickup");

        
        // Set default delivery type
        deliveryTypeComboBox.setValue("Delivery"); // זהו הערך הדיפולטיבי

        
        // Set default delivery date to today
        deliveryDatePicker.setValue(LocalDate.now());
        
        // Initialize greeting card options
        initializeGreetingCardOptions();
        
        // Replace deliveryHourSpinner with deliveryTimeComboBox
        updateDeliveryTimeOptions(LocalDate.now());
        deliveryDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        deliveryDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                updateDeliveryTimeOptions(newVal);
            }
        });
        
        // Add listeners
        deliveryTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateDeliveryFee();
            // Show/hide address fields based on delivery type
            boolean isDelivery = "Delivery".equals(newVal);
            cityField.setDisable(!isDelivery);
            streetAddressField.setDisable(!isDelivery);
            apartmentField.setDisable(!isDelivery);
            if (!isDelivery) {
                cityField.clear();
                streetAddressField.clear();
                apartmentField.clear();
            }
        });
        
        // Add greeting card listeners
        includeGreetingCardCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            greetingCardBackgroundComboBox.setDisable(!newVal);
            greetingCardMessageTextArea.setDisable(!newVal);
            previewGreetingCardButton.setDisable(!newVal);
            if (!newVal) {
                greetingCardBackgroundComboBox.setValue(null);
                greetingCardMessageTextArea.clear();
            }
        });
        
        // Add character limit to greeting message
        greetingCardMessageTextArea.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > 300) {
                greetingCardMessageTextArea.setText(oldVal);
            }
            // Update character count
            if (newVal != null) {
                characterCountLabel.setText(newVal.length() + "/300 characters");
            } else {
                characterCountLabel.setText("0/300 characters");
            }
            // Live update preview if open
            if (previewController != null && isGreetingCardPreviewOpen()) {
                previewController.setGreetingCardData(
                    greetingCardBackgroundComboBox.getValue(),
                    greetingCardMessageTextArea.getText().trim()
                );
            }
        });

        // Live update preview when background changes
        greetingCardBackgroundComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (previewController != null && isGreetingCardPreviewOpen()) {
                previewController.setGreetingCardData(
                    greetingCardBackgroundComboBox.getValue(),
                    greetingCardMessageTextArea.getText().trim()
                );
            }
        });
        
        // Update total when items change
        orderItems.addListener((javafx.collections.ListChangeListener.Change<? extends CartItem> change) -> {
            updateTotal();
        });

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
    
    public void setCartItems(List<CartItem> items) {
        orderItems.clear();
        orderItems.addAll(items);
        updateTotal();
    }
    
    public void setCurrentUser(LoginRegCheck user) {
        this.currentUser = user;
        System.out.println("CheckoutController: User set to: " + (user != null ? user.getUsername() : "null"));
        // Autofill fields if user is available
        if (user != null) {
            if (user.getFullName() != null && !user.getFullName().isEmpty()) {
                nameField.setText(user.getFullName());
            }
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                emailField.setText(user.getEmail());
            }
            if (user.getPhoneNum() != null && !user.getPhoneNum().isEmpty()) {
                phoneField.setText(user.getPhoneNum());
            }
            // Optionally, autofill address if you store it in user entity
        }
    }
    
    private void updateDeliveryFee() {
        boolean requiresDelivery = "Delivery".equals(deliveryTypeComboBox.getValue());
        double fee = requiresDelivery ? DELIVERY_FEE : 0.0;
        deliveryFeeLabel.setText(String.format("Delivery Fee: ₪%.2f", fee));
        updateTotal();
    }
    
    private void updateTotal() {
        double itemsTotal = orderItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        double deliveryFee = "Delivery".equals(deliveryTypeComboBox.getValue()) ? DELIVERY_FEE : 0.0;
        double total = itemsTotal + deliveryFee;
        double discount = 0.0;
        if (currentUser != null && currentUser.is_yearly_subscription() && total >= 50.0) {
            discount = total * 0.10;
            total -= discount;
            discountLabel.setText(String.format("Yearly Subscriber Discount: -₪%.2f", discount));
            discountLabel.setVisible(true);
        } else {
            discountLabel.setVisible(false);
        }
        orderTotal.setText(String.format("Total: ₪%.2f", total));
    }
    
    private void initializeGreetingCardOptions() {
        // Add greeting card background options
        greetingCardBackgroundComboBox.getItems().addAll(
            "Background 1",
            "Background 2", 
            "Background 3",
            "Background 4"
        );
        
        // Initially disable greeting card options
        greetingCardBackgroundComboBox.setDisable(true);
        greetingCardMessageTextArea.setDisable(true);
        previewGreetingCardButton.setDisable(true);
    }
    
    @FXML
    private void placeOrder() {
        System.out.println("CheckoutController: placeOrder called");
        System.out.println("CheckoutController: Current user is: " + (currentUser != null ? currentUser.getUsername() : "null"));

        // Validate required fields
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || phoneField.getText().isEmpty()) {
            Warning warning = new Warning("Please fill in all personal fields");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        // Validate email format
        if (!emailField.getText().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            Warning warning = new Warning("Please enter a valid email address");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        // Validate phone number format
        if (!phoneField.getText().matches("^\\d{9,10}$")) {
            Warning warning = new Warning("Please enter a valid phone number (9-10 digits)");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        // Validate delivery time if delivery is selected
        if ("Delivery".equals(deliveryTypeComboBox.getValue())) {

            if (cityField.getText().isEmpty() || streetAddressField.getText().isEmpty()) {
                Warning warning = new Warning("Please fill in your address for delivery");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }

        }
        
        // Validate greeting card if selected
        if (includeGreetingCardCheckBox.isSelected()) {
            if (greetingCardBackgroundComboBox.getValue() == null || greetingCardBackgroundComboBox.getValue().isEmpty()) {
                Warning warning = new Warning("Please select a greeting card background");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }
            
            if (greetingCardMessageTextArea.getText() == null || greetingCardMessageTextArea.getText().trim().isEmpty()) {
                Warning warning = new Warning("Please enter a greeting message");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }
        }

        // Check if user is available
        if (currentUser == null) {
            System.out.println("CheckoutController: User is null, showing error");
            Warning warning = new Warning("User session not found. Please log in again.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        System.out.println("CheckoutController: Creating order with user: " + currentUser.getUsername());
        // Create and populate the order with user information
        Order order = new Order(nameField.getText(), emailField.getText(), currentUser);
        order.setCity(cityField.getText());
        order.setStreetAddress(streetAddressField.getText());
        order.setApartment(apartmentField.getText());
        order.setRequiresDelivery("Delivery".equals(deliveryTypeComboBox.getValue()));
        
        // Set greeting card information
        order.setIncludeGreetingCard(includeGreetingCardCheckBox.isSelected());
        if (includeGreetingCardCheckBox.isSelected()) {
            order.setGreetingCardBackground(greetingCardBackgroundComboBox.getValue());
            order.setGreetingCardMessage(greetingCardMessageTextArea.getText().trim());
        }
        
       
        // Convert LocalDate and time string to Date for delivery time
        String timeStr = deliveryTimeComboBox.getValue();
        int hour = 12;
        int minute = 0;
        if (timeStr != null && timeStr.contains(":")) {
            String[] parts = timeStr.split(":");
            hour = Integer.parseInt(parts[0]);
            minute = Integer.parseInt(parts[1]);
        }
        LocalDateTime deliveryDateTime = LocalDateTime.of(
            deliveryDatePicker.getValue(),
            LocalTime.of(hour, minute)
        );
        Date deliveryDate = Date.from(deliveryDateTime.atZone(ZoneId.systemDefault()).toInstant());
        order.setDeliveryTime(deliveryDate);
        

        // Add items to order
        orderItems.forEach(item -> order.addItem(item));
        // Apply discount if eligible
        double itemsTotal = orderItems.stream().mapToDouble(CartItem::getTotalPrice).sum();
        double deliveryFee = "Delivery".equals(deliveryTypeComboBox.getValue()) ? DELIVERY_FEE : 0.0;
        double total = itemsTotal + deliveryFee;
        double discount = 0.0;
        if (currentUser != null && currentUser.is_yearly_subscription() && total >= 50.0) {
            discount = total * 0.10;
            total -= discount;
        }
        order.setTotalAmount(total); // set the discounted total
        order.setDiscountAmount(discount); // set the discount amount

        // Open payment page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/il/cshaifasweng/OCSFMediatorExample/client/payment.fxml"));
            Parent root = loader.load();
            PaymentController paymentController = loader.getController();
            paymentController.setOrder(order);
            paymentController.setCheckoutStage((Stage) placeOrderButton.getScene().getWindow());

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Secure Payment");
            stage.setScene(scene);
            stage.show();

            // Hide the checkout window
            ((Stage) placeOrderButton.getScene().getWindow()).hide();
        } catch (IOException e) {
            e.printStackTrace();
            Warning warning = new Warning("Failed to open payment page");
            EventBus.getDefault().post(new WarningEvent(warning));
        }
    }
    
    @FXML
    private void cancel() {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
    
    @FXML
    private void goBackToCatalog() {
        try {
            // Load the catalog page
            App.setRoot("catalog");
        } catch (IOException e) {
            e.printStackTrace();
            Warning warning = new Warning("Error returning to catalog");
            EventBus.getDefault().post(new WarningEvent(warning));
        }
    }
    
    @FXML
    private void previewGreetingCard() {
        if (!includeGreetingCardCheckBox.isSelected()) {
            Warning warning = new Warning("Please enable greeting card first");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        if (greetingCardBackgroundComboBox.getValue() == null || greetingCardBackgroundComboBox.getValue().isEmpty()) {
            Warning warning = new Warning("Please select a greeting card background");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        if (greetingCardMessageTextArea.getText() == null || greetingCardMessageTextArea.getText().trim().isEmpty()) {
            Warning warning = new Warning("Please enter a greeting message");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        if (isGreetingCardPreviewOpen()) {
            Warning warning = new Warning("The greeting card preview window is already open.");
            EventBus.getDefault().post(new WarningEvent(warning));
            setGreetingCardPreviewStage(greetingCardPreviewStage);
            greetingCardPreviewStage.toFront();
            greetingCardPreviewStage.requestFocus();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("greeting_card_preview.fxml"));
            Parent root = loader.load();
            previewController = loader.getController();
            previewController.setGreetingCardData(
                greetingCardBackgroundComboBox.getValue(),
                greetingCardMessageTextArea.getText().trim()
            );
            Stage stage = new Stage();
            setGreetingCardPreviewStage(stage);
            stage.setTitle("Greeting Card Preview");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            stage.setOnHidden(e -> {
                setGreetingCardPreviewStage(null);
                previewController = null;
            });
        } catch (IOException e) {
            e.printStackTrace();
            Warning warning = new Warning("Failed to open greeting card preview");
            EventBus.getDefault().post(new WarningEvent(warning));
        }
    }

    @Subscribe
    public void onUserUpdated(UpdateUserEvent event) {
        if (currentUser != null && event.getUpdatedUser().getId().equals(currentUser.getId())) {
            currentUser = event.getUpdatedUser();
            updateTotal();
        }
    }

    private void updateDeliveryTimeOptions(LocalDate selectedDate) {
        deliveryTimeComboBox.getItems().clear();
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
        boolean isToday = selectedDate.equals(today);
        for (int hour = 8; hour <= 21; hour++) {
            for (int min : new int[]{0, 30}) {
                LocalTime slot = LocalTime.of(hour, min);
                if (isToday && slot.isBefore(now.plusHours(2))) continue;
                deliveryTimeComboBox.getItems().add(String.format("%02d:%02d", hour, min));
            }
        }
        // Set default value to the first available slot
        if (!deliveryTimeComboBox.getItems().isEmpty()) {
            deliveryTimeComboBox.setValue(deliveryTimeComboBox.getItems().get(0));
        }
    }
} 