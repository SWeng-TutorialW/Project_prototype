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
    private Button backButton;
    
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
            // Hide address and delivery time sections for pickup
            addressSection.setVisible(false);
            deliveryTimeSection.setVisible(false);
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
        
        subtotalLabel.setText(String.format("₪%.2f", subtotal));
        deliveryFeeLabel.setText(String.format("₪%.2f", deliveryFee));
        totalAmountLabel.setText(String.format("₪%.2f", total));
    }
    
    @FXML
    private void handleBackButton() {
        ((Stage) backButton.getScene().getWindow()).close();
    }
} 