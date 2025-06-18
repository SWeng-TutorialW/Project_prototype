package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
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
import java.util.List;

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
    private TableColumn<Order, Double> totalAmountColumn;
    
    @FXML
    private TableColumn<Order, String> deliveryColumn;
    
    @FXML
    private TableColumn<Order, String> addressColumn;
    
    @FXML
    private TableColumn<Order, Void> detailsColumn;
    
    @FXML
    private Button backButton;
    
    @FXML
    private Label noOrdersLabel;
    
    @FXML
    private Label titleLabel;
    
    private LoginRegCheck currentUser;
    private ObservableList<Order> ordersList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    public void initialize() {
        ordersList = FXCollections.observableArrayList();
        ordersTable.setItems(ordersList);
        
        // Set up table columns
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        orderDateColumn.setCellValueFactory(cellData -> {
            String dateStr = dateFormat.format(cellData.getValue().getOrderDate());
            return new SimpleStringProperty(dateStr);
        });
        
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order_details.fxml"));
            Parent root = loader.load();
            OrderDetailsController detailsController = loader.getController();
            detailsController.setOrder(order);
            
            Stage stage = new Stage();
            stage.setTitle("Order Details - Order #" + order.getId());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
} 