package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.util.ResourceBundle;

public class complain_controller implements Initializable {
    private CatalogController catalogController;
    private CatalogController_employee cc;
    private LoginRegCheck currentUser;
    private OrderSummary selectedOrder;
    
    @FXML private Button send;
    @FXML private Label head;
    @FXML private TextField text_box;
    
    // Table components
    @FXML private TableView<OrderSummary> ordersTable;
    @FXML private TableColumn<OrderSummary, String> orderDateColumn;
    @FXML private TableColumn<OrderSummary, Double> priceColumn;
    @FXML private TableColumn<OrderSummary, String> statusColumn;
    @FXML private TableColumn<OrderSummary, String> deliveryColumn;
    @FXML private TableColumn<OrderSummary, String> itemsColumn;
    
    private ObservableList<OrderSummary> ordersList;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBus.getDefault().register(this);
        setupTable();
        // Only load orders if this is a customer (not a worker)
        if (currentUser != null && !currentUser.isType()) {
            loadCustomerOrders();
        }
    }
    
    private void setupTable() {
        ordersList = FXCollections.observableArrayList();
        ordersTable.setItems(ordersList);
        
        // Set up table columns
        orderDateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFormattedDate()));
        
        priceColumn.setCellValueFactory(cellData -> 
            new SimpleDoubleProperty(cellData.getValue().getTotalAmount()).asObject());
        
        statusColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getStatus()));
        
        deliveryColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().isRequiresDelivery() ? "Yes" : "No"));
        
        itemsColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getItemsSummary()));
        
        // Add click listener for order selection
        ordersTable.setOnMouseClicked(event -> {
            OrderSummary clickedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (clickedOrder != null) {
                selectedOrder = clickedOrder;
                System.out.println("Selected order ID: " + selectedOrder.getId());
                
                // Visual feedback - highlight selected row
                ordersTable.getSelectionModel().select(clickedOrder);
                
                // Show confirmation
                Warning warning = new Warning("Order #" + selectedOrder.getId() + " selected. You can now submit your complaint.");
                EventBus.getDefault().post(new WarningEvent(warning));
            }
        });
    }
    
    private void loadCustomerOrders() {
        if (currentUser != null && currentUser.getUsername() != null) {
            System.out.println("Loading orders for customer: " + currentUser.getUsername());
            CustomerOrdersRequest request = new CustomerOrdersRequest(currentUser.getUsername());
            try {
                SimpleClient.getClient().sendToServer(request);
            } catch (Exception e) {
                e.printStackTrace();
                Warning warning = new Warning("Error loading orders: " + e.getMessage());
                EventBus.getDefault().post(new WarningEvent(warning));
            }
        } else {
            System.out.println("No current user or username available");
        }
    }
    
    @Subscribe
    public void handleCustomerOrdersResponse(CustomerOrdersResponse response) {
        System.out.println("Received " + response.getOrders().size() + " orders from server");
        ordersList.clear();
        ordersList.addAll(response.getOrders());
        
        if (response.getOrders().isEmpty()) {
            Warning warning = new Warning("You don't have any orders yet. Please place an order before submitting a complaint.");
            EventBus.getDefault().post(new WarningEvent(warning));
        }
    }
    
    @Subscribe
    public void handleErrorResponse(String errorMsg) {
        if ("error_fetching_orders".equals(errorMsg)) {
            Warning warning = new Warning("Error loading your orders. Please try again later.");
            EventBus.getDefault().post(new WarningEvent(warning));
        }
    }
    
    public void setCatalogController(CatalogController controller) {
        this.catalogController = controller;
        if (controller != null) {
            this.currentUser = controller.getUser();
            // Only load orders for customers
            if (currentUser != null && !currentUser.isType()) {
                loadCustomerOrders();
            }
        }
    }
    
    public void setCatalogController(CatalogController_employee controller) {
        cc = controller;
        if (controller != null) {
            this.currentUser = controller.getUser();
            // Only load orders for customers (workers should use worker_request_controller)
            if (currentUser != null && !currentUser.isType()) {
                loadCustomerOrders();
            }
        }
    }

    public void change_head(String head) {
        this.head.setText(head);
        send.setText("Send request");
    }

    @FXML
    public void send_complain(ActionEvent event) {
        // Ensure this is only used by customers
        if (currentUser != null && currentUser.isType()) {
            Warning warning = new Warning("Workers should use the request feature instead of complaints.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        
        String client_complaint = text_box.getText();
        if (client_complaint.isEmpty()) {
            Warning warning = new Warning("Please enter a complaint before submitting.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        // Check if an order is selected
        if (selectedOrder == null) {
            Warning warning = new Warning("Please click on an order from the table before submitting your complaint.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        // Create complaint with selected order ID
        Complain complain = new Complain(client_complaint);
        if (currentUser != null) {
            complain.setClient(currentUser.getUsername());
        }
        
        // Add order information to the complaint text
        String complaintWithOrder = "Order #" + selectedOrder.getId() + " - Price: $" + String.format("%.2f", selectedOrder.getTotalAmount()) + " - " + client_complaint;
        complain.setComplaint(complaintWithOrder);
        
        if (catalogController != null) {
            catalogController.receiveNewComplain(complain);
        } else if (cc != null) {
            cc.receiveNewComplain(complain);
        }
        
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
