package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.CartItem;
import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class complain_controller implements Initializable {
    private CatalogController catalogController;
    private CatalogController_employee cc;
    private LoginRegCheck currentUser;
    private Order selectedOrder;

    @FXML private Button send;
    @FXML private Label head;
    @FXML private TextField text_box;

    @FXML private TableView<Order> ordersTable;
    //@FXML private TableColumn<Order, Integer> orderIdColumn;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, String> statusColumn;
    @FXML private TableColumn<Order, Double> priceColumn;
    @FXML private TableColumn<Order, String> deliveryColumn;
    @FXML private TableColumn<Order, String> itemsColumn;

    private ObservableList<Order> ordersList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        EventBus.getDefault().register(this);
        setupTable();
    }
    
    private void setupTable() {
        ordersList = FXCollections.observableArrayList();
        ordersTable.setItems(ordersList);

        //orderIdColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("Id"));
        orderDateColumn.setCellValueFactory(cellData -> {
            String dateStr = dateFormat.format(cellData.getValue().getOrderDate());
            return new SimpleStringProperty(dateStr);
        });
        statusColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));
        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getTotalAmount()).asObject());
        deliveryColumn.setCellValueFactory(cellData -> {
            String deliveryType = cellData.getValue().isRequiresDelivery() ? "Delivery" : "Pickup";
            return new SimpleStringProperty(deliveryType);
        });
        itemsColumn.setCellValueFactory(cellData -> {
            List<CartItem> items = cellData.getValue().getItems();
            StringBuilder itemsStr = new StringBuilder();
            if (items != null && !items.isEmpty()) {
                for (CartItem item : items) {
                    if (itemsStr.length() > 0) itemsStr.append(", ");
                    if (item.getFlower() != null) {
                        itemsStr.append(item.getFlower().getFlowerName()).append(" x").append(item.getQuantity());
                    } else {
                        itemsStr.append("Unknown item x").append(item.getQuantity());
                    }
                }
            } else {
                itemsStr.append("No items");
            }
            return new SimpleStringProperty(itemsStr.toString());
        });

        // Click-to-select functionality
        ordersTable.setOnMouseClicked(event -> {
            Order clickedOrder = ordersTable.getSelectionModel().getSelectedItem();
            if (clickedOrder != null) {
                selectedOrder = clickedOrder;
                ordersTable.getSelectionModel().select(clickedOrder);
                Warning warning = new Warning("Order #" + selectedOrder.getId() + " selected. You can now submit your complaint.");
                EventBus.getDefault().post(new WarningEvent(warning));
            }
        });
    }

    public void setCatalogController(CatalogController controller) {
        this.catalogController = controller;
        if (controller != null) {
            this.currentUser = controller.getUser();
            loadUserOrders();
        }
    }

    public void setCatalogController(CatalogController_employee controller) {
        cc = controller;
        if (controller != null) {
            this.currentUser = controller.getUser();
            loadUserOrders();
        }
    }

    private void loadUserOrders() {
        if (currentUser != null && currentUser.getUsername() != null) {
            try {
                SimpleClient.getClient().sendToServer("getOrdersForUser_" + currentUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
                showNoOrders();
            }
        } else {
            showNoOrders();
        }
    }

    private void showNoOrders() {
        ordersTable.setVisible(false);
        Warning warning = new Warning("You don't have any orders yet. Please place an order before submitting a complaint.");
        EventBus.getDefault().post(new WarningEvent(warning));
    }

    @Subscribe
    public void handleUserOrdersResponse(Object response) {
        if (response instanceof List<?>) {
            List<?> orders = (List<?>) response;
            if (!orders.isEmpty() && orders.get(0) instanceof Order) {
                @SuppressWarnings("unchecked")
                List<Order> userOrders = (List<Order>) orders;
                setOrders(userOrders);
            } else {
                showNoOrders();
            }
        } else if (response instanceof String) {
            String responseStr = (String) response;
            if (responseStr.equals("user_not_found") || responseStr.equals("error_retrieving_orders")) {
                showNoOrders();
            }
        }
    }

    private void setOrders(List<Order> orders) {
        ordersList.clear();
        if (orders != null && !orders.isEmpty()) {
            ordersList.addAll(orders);
            ordersTable.setVisible(true);
        } else {
            showNoOrders();
        }
    }

    public void change_head(String head) {
        this.head.setText(head);
        send.setText("Send request");
    }

    @FXML
    public void send_complain(ActionEvent event) {
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

        if (selectedOrder == null) {
            Warning warning = new Warning("Please click on an order from the table before submitting your complaint.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        // Create complaint with selected order ID and price
        Complain complain = new Complain(client_complaint);
        if (currentUser != null) {
            complain.setClient(currentUser.getUsername());
        }
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
