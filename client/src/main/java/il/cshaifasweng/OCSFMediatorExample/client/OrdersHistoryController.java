package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.OrderCancellationRequest;
import il.cshaifasweng.OCSFMediatorExample.entities.OrderCancellationResponse;
import javafx.application.Platform;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class OrdersHistoryController {
    
    @FXML
    private TableView<Order> ordersTable;
    
    @FXML
    private TableColumn<Order, Integer> orderIdColumn;
    
    @FXML
    private TableColumn<Order, String> orderDateColumn;
    
    @FXML
    private TableColumn<Order, String> statusColumn;
    
    @FXML
    private TableColumn<Order, String> storeColumn;
    
    @FXML
    private TableColumn<Order, Double> totalAmountColumn;
    
    @FXML
    private TableColumn<Order, String> deliveryColumn;
    
    @FXML
    private TableColumn<Order, String> addressColumn;
    
    @FXML
    private TableColumn<Order, Void> detailsColumn;
    
    @FXML
    private TableColumn<Order, Void> cancelColumn;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Label noOrdersLabel;
    
    @FXML
    private Label titleLabel;
    
    private LoginRegCheck currentUser;
    private List<Order> ordersList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private OrderDetailsController openOrderDetailsController; // Track open order details window
    
    public void initialize() {
        ordersList = FXCollections.observableArrayList();
        ordersTable.setItems((ObservableList<Order>) ordersList);
        
        // Set up table columns
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        orderDateColumn.setCellValueFactory(cellData -> {
            String dateStr = dateFormat.format(cellData.getValue().getOrderDate());
            return new SimpleStringProperty(dateStr);
        });
        
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        storeColumn.setCellValueFactory(cellData -> {
            String storeName = getStoreNameFromId(cellData.getValue().getStoreId());
            return new SimpleStringProperty(storeName);
        });
        
        totalAmountColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getTotalAmount()).asObject());
        
        deliveryColumn.setCellValueFactory(cellData -> {
            String deliveryType = cellData.getValue().isRequiresDelivery() ? "Delivery" : "Pickup";
            return new SimpleStringProperty(deliveryType);
        });
        
        addressColumn.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            String address = "";
            if (order.isRequiresDelivery()) {
                address = order.getStreetAddress() + ", " + order.getCity();
                if (order.getApartment() != null && !order.getApartment().isEmpty()) {
                    address += ", Apt " + order.getApartment();
                }
            } else {
                address = "Store Pickup";
            }
            return new SimpleStringProperty(address);
        });
        
        // Set up details column with buttons
        detailsColumn.setCellFactory(param -> new TableCell<Order, Void>() {
            private final Button viewDetailsButton = new Button("View Details");
            
            {
                viewDetailsButton.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    openOrderDetails(order);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewDetailsButton);
                }
            }
        });
        
        // Set up cancel column with buttons
        cancelColumn.setCellFactory(param -> new TableCell<Order, Void>() {
            private final Button cancelButton = new Button("Cancel Order");
            
            {
                cancelButton.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    handleCancelOrder(order);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Order order = getTableView().getItems().get(getIndex());
                    boolean isSubscription = false;
                    if (order.getItems() != null) {
                        for (var cartItem : order.getItems()) {
                            if (cartItem.getFlower() != null && "Yearly Subscription".equals(cartItem.getFlower().getFlowerName())) {
                                isSubscription = true;
                                break;
                            }
                        }
                    }
                    // Only show cancel button for orders that can be cancelled and are not subscription
                    if (canCancelOrder(order) && !isSubscription) {
                        cancelButton.setVisible(true);
                        cancelButton.setDisable(false);
                        setGraphic(cancelButton);
                    } else {
                        cancelButton.setVisible(false);
                        cancelButton.setDisable(true);
                        setGraphic(null); // Remove the button completely
                    }
                }
            }
        });
        
        // Register with EventBus to receive server responses
        EventBus.getDefault().register(this);
    }
    
    public void setCurrentUser(LoginRegCheck user) {
        this.currentUser = user;
    }
    
    public void loadUserOrders() {
        if (currentUser == null) {
            showNoOrders();
            return;
        }
        
        try {
            // Send request to server to get user orders
            SimpleClient.getClient().sendToServer("getOrdersForUser_" + currentUser.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
            showNoOrders();
        }
    }
    
    public void setOrders(List<Order> orders) {
        ordersList.clear();
        if (orders != null && !orders.isEmpty()) {
            ordersList.addAll(orders);
            ordersTable.setVisible(true);
            noOrdersLabel.setVisible(false);
        } else {
            showNoOrders();
        }
    }
    
    private void showNoOrders() {
        ordersTable.setVisible(false);
        noOrdersLabel.setVisible(true);
    }
    
    private void openOrderDetails(Order order) {
        try {
            // Ensure we have the most up-to-date order information
            Order currentOrder = getCurrentOrderFromList(order.getId());
            if (currentOrder == null) {
                currentOrder = order; // Fallback to the passed order
            }
            
            // Debug: Print order details to verify data
            debugOrderDetails(currentOrder);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order_details.fxml"));
            Parent root = loader.load();
            openOrderDetailsController = loader.getController();
            openOrderDetailsController.setOrder(currentOrder);
            
            Stage stage = new Stage();
            stage.setTitle("Order Details - Order #" + currentOrder.getId());
            stage.setScene(new Scene(root));
            
            // Clear reference when window is closed
            stage.setOnCloseRequest(event -> {
                openOrderDetailsController = null;
            });
            
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get the current order from the list by ID
     */
    private Order getCurrentOrderFromList(int orderId) {
        for (Order order : ordersList) {
            if (order.getId() == orderId) {
                return order;
            }
        }
        return null;
    }
    
    /**
     * Debug method to print order details
     */
    private void debugOrderDetails(Order order) {
        System.out.println("=== Order Details Debug ===");
        System.out.println("Order ID: " + order.getId());
        System.out.println("Status: " + order.getStatus());
        System.out.println("Order Date: " + (order.getOrderDate() != null ? order.getOrderDate() : "null"));
        System.out.println("Cancellation Date: " + (order.getCancellationDate() != null ? order.getCancellationDate() : "null"));
        System.out.println("Refund Amount: " + order.getRefundAmount());
        System.out.println("Cancellation Reason: " + (order.getCancellationReason() != null ? order.getCancellationReason() : "null"));
        System.out.println("Total Amount: " + order.getTotalAmount());
        System.out.println("Delivery Time: " + (order.getDeliveryTime() != null ? order.getDeliveryTime() : "null"));
        System.out.println("==========================");
    }
    
    @FXML
    private void handleBackButton() {
        // Unregister from EventBus before closing
        EventBus.getDefault().unregister(this);
        ((Stage) backButton.getScene().getWindow()).close();
    }
    
    // Handle server response for user orders
    @org.greenrobot.eventbus.Subscribe
    public void handleUserOrdersResponse(Object response) {
        if (response instanceof List<?>) {
            List<?> orders = (List<?>) response;
            if (!orders.isEmpty() && orders.get(0) instanceof Order) {
                @SuppressWarnings("unchecked")
                List<Order> userOrders = (List<Order>) orders;
                setOrders(userOrders);
            }
        } else if (response instanceof String) {
            String responseStr = (String) response;
            if (responseStr.equals("user_not_found") || responseStr.equals("error_retrieving_orders")) {
                setOrders(null);
            }
        }
    }
    
    // Handle order cancellation response
    @org.greenrobot.eventbus.Subscribe
    public void handleOrderCancellationResponse(OrderCancellationResponse response) {
        if (response.isSuccess()) {
            showAlert(Alert.AlertType.INFORMATION, "Order Cancelled", 
                     "Order #" + response.getOrderId() + " has been cancelled successfully",
                     "Refund Amount: ₪" + String.format("%.2f", response.getRefundAmount()) + 
                     "\nRefund Policy: " + response.getRefundPolicy() +
                     "\n\nYou will receive a confirmation email shortly.");
            
            // Update the specific order in the list with cancellation details
            // Note: We'll use the server's calculated refund amount to ensure accuracy
            updateOrderWithServerCancellationData(response);
            
            // Refresh order details if they're currently open
            if (openOrderDetailsController != null) {
                Order updatedOrder = getCurrentOrderFromList(response.getOrderId());
                if (updatedOrder != null) {
                    // Add a small delay to ensure the UI updates properly
                    Platform.runLater(() -> {
                        openOrderDetailsController.setOrder(updatedOrder);
                    });
                }
            }
            
            // Force complete refresh of the table to update button visibility
            forceTableRefresh();
        } else {
            showAlert(Alert.AlertType.ERROR, "Cancellation Failed", 
                     "Failed to cancel order #" + response.getOrderId(),
                     "Error: " + response.getMessage());
        }
    }
    
    /**
     * Update order with server-provided cancellation data
     */
    private void updateOrderWithServerCancellationData(OrderCancellationResponse response) {
        for (Order order : ordersList) {
            if (order.getId() == response.getOrderId()) {
                order.setStatus("CANCELLED");
                order.setCancellationDate(new Date());
                order.setRefundAmount(response.getRefundAmount());
                // Set a default cancellation reason if none exists
                if (order.getCancellationReason() == null || order.getCancellationReason().isEmpty()) {
                    order.setCancellationReason("Customer requested cancellation");
                }
                break;
            }
        }
    }
    
    /**
     * Check if an order can be cancelled
     */
    private boolean canCancelOrder(Order order) {
        if (order == null) return false;
        
        String status = order.getStatus();
        return !("CANCELLED".equals(status) || "DELIVERED".equals(status) || "PICKED_UP".equals(status));
    }
    
    /**
     * Handle order cancellation
     */
    private void handleCancelOrder(Order order) {
        if (!canCancelOrder(order)) {
            showAlert(Alert.AlertType.WARNING, "Cannot Cancel Order", 
                     "Order #" + order.getId() + " cannot be cancelled",
                     "This order has already been " + order.getStatus().toLowerCase() + ".");
            return;
        }
        
        // Show refund policy information
        String refundPolicy = order.getRefundPolicyDescription();
        String timeUntilDelivery = calculateTimeUntilDelivery(order);
        
        String message = "Are you sure you want to cancel order #" + order.getId() + "?\n\n" +
                        "Refund Policy: " + refundPolicy + "\n" +
                        "Time until delivery: " + timeUntilDelivery + "\n\n" +
                        "This action cannot be undone.";
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Order");
        alert.setHeaderText("Confirm Order Cancellation");
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Get cancellation reason
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Cancellation Reason");
            dialog.setHeaderText("Please provide a reason for cancellation");
            dialog.setContentText("Reason (optional):");
            
            Optional<String> reason = dialog.showAndWait();
            String cancellationReason = reason.orElse("Customer requested cancellation");
            
            // Send cancellation request
            try {
                OrderCancellationRequest request = new OrderCancellationRequest(
                    order.getId(), cancellationReason, currentUser.getUsername());
                SimpleClient.getClient().sendToServer(request);
                
                // Temporarily update the local order with cancellation reason
                // The full update will happen when we get the server response
                order.setCancellationReason(cancellationReason);
                
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", 
                         "Failed to send cancellation request",
                         "Error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Calculate time until delivery in a readable format
     */
    private String calculateTimeUntilDelivery(Order order) {
        if (order.getDeliveryTime() == null) {
            return "No delivery time set";
        }
        
        long currentTime = System.currentTimeMillis();
        long deliveryTime = order.getDeliveryTime().getTime();
        long timeUntilDelivery = deliveryTime - currentTime;
        
        if (timeUntilDelivery <= 0) {
            return "Delivery time has passed";
        }
        
        long hours = timeUntilDelivery / (1000 * 60 * 60);
        long minutes = (timeUntilDelivery % (1000 * 60 * 60)) / (1000 * 60);
        
        if (hours > 0) {
            return hours + " hours " + minutes + " minutes";
        } else {
            return minutes + " minutes";
        }
    }
    
    /**
     * Show alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
    
    /**
     * Force refresh the entire table to update all cells
     */
    private void forceTableRefresh() {
        // Temporarily clear and re-add items to force cell factory to recreate
        ObservableList<Order> tempList = FXCollections.observableArrayList(ordersList);
        ordersTable.setItems(null);
        ordersTable.setItems(tempList);
        
        // Also refresh the table
        ordersTable.refresh();
    }
    
    /**
     * Adds an order for a user. This is a static helper for MyAccountController to add a subscription order.
     * This implementation is minimal and only works if an OrdersHistoryController instance is open and accessible.
     * In a real app, this should send the order to the server for persistence.
     */
    public static void addOrderForUser(LoginRegCheck user, Order order) {
        // This is a placeholder. In a real app, you would send the order to the server.
        try {
            // Send the order to the server for persistence
            SimpleClient.getClient().sendToServer(order);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @org.greenrobot.eventbus.Subscribe
    public void handleOrderSuccessMessage(String message) {
        if (message.equals("Order created successfully")) {
            Platform.runLater(() -> {
                loadUserOrders(); // Refresh the orders list
            });
        }
    }
    
    /**
     * Convert store ID to store name
     * @param storeId The store ID
     * @return The store name
     */
    private String getStoreNameFromId(int storeId) {
        switch (storeId) {
            case 1: return "Haifa";
            case 2: return "Krayot";
            case 3: return "Nahariyya";
            case 4: return "Network";
            default: return "Unknown Store";
        }
    }
} 