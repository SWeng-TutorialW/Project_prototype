package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CartItem;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;

public class OrderDetailsController {
    
    @FXML
    private Label orderIdLabel;
    
    @FXML
    private Label orderDateLabel;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private Label customerNameLabel;
    
    @FXML
    private Label customerEmailLabel;
    
    @FXML
    private Label deliveryTypeLabel;
    
    @FXML
    private VBox addressSection;
    
    @FXML
    private Label addressLabel;
    
    @FXML
    private VBox deliveryTimeSection;
    
    @FXML
    private Label deliveryTimeLabel;
    
    @FXML
    private TableView<CartItem> itemsTable;
    
    @FXML
    private TableColumn<CartItem, String> itemNameColumn;
    
    @FXML
    private TableColumn<CartItem, String> itemTypeColumn;
    
    @FXML
    private TableColumn<CartItem, String> itemColorColumn;
    
    @FXML
    private TableColumn<CartItem, String> itemCategoryColumn;
    
    @FXML
    private TableColumn<CartItem, String> itemStoreColumn;
    
    @FXML
    private TableColumn<CartItem, Double> itemPriceColumn;
    
    @FXML
    private TableColumn<CartItem, Integer> itemQuantityColumn;
    
    @FXML
    private TableColumn<CartItem, Double> itemTotalColumn;
    
    @FXML
    private Label subtotalLabel;
    
    @FXML
    private Label deliveryFeeLabel;
    
    @FXML
    private Label totalAmountLabel;
    
    @FXML
    private VBox cancellationSection;
    
    @FXML
    private Label cancellationDateLabel;
    
    @FXML
    private Label refundAmountLabel;
    
    @FXML
    private Label cancellationReasonLabel;
    
    @FXML
    private VBox greetingCardSection;
    
    @FXML
    private Label greetingCardBackgroundLabel;
    
    @FXML
    private Label greetingCardMessageLabel;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Label discountLabel;
    
    private Order currentOrder;
    private ObservableList<CartItem> itemsList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    public void initialize() {
        itemsList = FXCollections.observableArrayList();
        itemsTable.setItems(itemsList);
        
        // Set up table columns
        itemNameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFlower().getFlowerName()));
        
        itemTypeColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFlower().getFlowerType()));
        
        itemColorColumn.setCellValueFactory(cellData -> {
            String color = cellData.getValue().getFlower().getColor();
            return new SimpleStringProperty(color != null ? color : "N/A");
        });
        
        itemCategoryColumn.setCellValueFactory(cellData -> {
            String category = cellData.getValue().getFlower().getCategory();
            return new SimpleStringProperty(category != null ? category : "N/A");
        });
        
        itemStoreColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStore()));
        
        itemPriceColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getFlower().getFlowerPrice()).asObject());
        
        itemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        itemTotalColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());
    }
    
    public void setOrder(Order order) {
        this.currentOrder = order;
        populateOrderDetails();
    }
    
    private void populateOrderDetails() {
        if (currentOrder == null) return;
        
        // Set basic order information
        orderIdLabel.setText(String.valueOf(currentOrder.getId()));
        orderDateLabel.setText(dateFormat.format(currentOrder.getOrderDate()));
        statusLabel.setText(currentOrder.getStatus());
        customerNameLabel.setText(currentOrder.getCustomerName());
        customerEmailLabel.setText(currentOrder.getCustomerEmail());
        
        // Set delivery information
        String deliveryType = currentOrder.isRequiresDelivery() ? "Delivery" : "Pickup";
        deliveryTypeLabel.setText(deliveryType);
        
        if (currentOrder.isRequiresDelivery()) {
            // Show address section
            addressSection.setVisible(true);
            String address = currentOrder.getStreetAddress() + ", " + currentOrder.getCity();
            if (currentOrder.getApartment() != null && !currentOrder.getApartment().isEmpty()) {
                address += ", Apt " + currentOrder.getApartment();
            }
            addressLabel.setText(address);
            
            // Show delivery time section
            deliveryTimeSection.setVisible(true);
            if (currentOrder.getDeliveryTime() != null) {
                deliveryTimeLabel.setText(dateFormat.format(currentOrder.getDeliveryTime()));
            } else {
                deliveryTimeLabel.setText("Not specified");
            }
        } else {
            // Hide address section for pickup
            addressSection.setVisible(false);
            
            // Show pickup time section for pickup orders
            deliveryTimeSection.setVisible(true);
            if (currentOrder.getDeliveryTime() != null) {
                deliveryTimeLabel.setText("Pickup Time: " + dateFormat.format(currentOrder.getDeliveryTime()));
            } else {
                deliveryTimeLabel.setText("Pickup Time: Not specified");
            }
        }
        
        // Populate items table
        itemsList.clear();
        if (currentOrder.getItems() != null) {
            itemsList.addAll(currentOrder.getItems());
        }
        
        // Calculate and display totals
        double subtotal = 0.0;
        if (currentOrder.getItems() != null) {
            subtotal = currentOrder.getItems().stream()
                    .mapToDouble(CartItem::getTotalPrice)
                    .sum();
        }
        double deliveryFee = currentOrder.getDeliveryFee();
        double total = currentOrder.getTotalAmount();
        double discount = currentOrder.getDiscountAmount();
        if (discount > 0.0) {
            discountLabel.setText(String.format("Yearly Subscriber Discount: -₪%.2f", discount));
            discountLabel.setVisible(true);
        } else {
            discountLabel.setVisible(false);
        }
        subtotalLabel.setText(String.format("₪%.2f", subtotal));
        deliveryFeeLabel.setText(String.format("₪%.2f", deliveryFee));
        totalAmountLabel.setText(String.format("₪%.2f", total));
        
        // Show cancellation information if order is cancelled
        if ("CANCELLED".equals(currentOrder.getStatus())) {
            cancellationSection.setVisible(true);
            if (currentOrder.getCancellationDate() != null) {
                cancellationDateLabel.setText(dateFormat.format(currentOrder.getCancellationDate()));
            } else {
                cancellationDateLabel.setText("Not specified");
            }
            refundAmountLabel.setText(String.format("₪%.2f", currentOrder.getRefundAmount()));
            if (currentOrder.getCancellationReason() != null && !currentOrder.getCancellationReason().isEmpty()) {
                cancellationReasonLabel.setText(currentOrder.getCancellationReason());
            } else {
                cancellationReasonLabel.setText("No reason provided");
            }
        } else {
            cancellationSection.setVisible(false);
        }
        
        // Show greeting card information if included
        if (currentOrder.isIncludeGreetingCard()) {
            greetingCardSection.setVisible(true);
            if (currentOrder.getGreetingCardBackground() != null && !currentOrder.getGreetingCardBackground().isEmpty()) {
                greetingCardBackgroundLabel.setText(currentOrder.getGreetingCardBackground());
            } else {
                greetingCardBackgroundLabel.setText("Not specified");
            }
            if (currentOrder.getGreetingCardMessage() != null && !currentOrder.getGreetingCardMessage().isEmpty()) {
                greetingCardMessageLabel.setText(currentOrder.getGreetingCardMessage());
            } else {
                greetingCardMessageLabel.setText("No message provided");
            }
        } else {
            greetingCardSection.setVisible(false);
        }
    }
    
    @FXML
    private void handleBackButton() {
        ((Stage) backButton.getScene().getWindow()).close();
    }
} 