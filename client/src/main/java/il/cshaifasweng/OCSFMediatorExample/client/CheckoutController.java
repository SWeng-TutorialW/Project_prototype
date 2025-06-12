package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CartItem;
import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

public class CheckoutController {
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TableView<CartItem> orderTable;
    @FXML private TableColumn<CartItem, String> nameColumn;
    @FXML private TableColumn<CartItem, String> typeColumn;
    @FXML private TableColumn<CartItem, Double> priceColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;
    @FXML private TableColumn<CartItem, Double> totalColumn;
    @FXML private Label orderTotal;
    @FXML private Button placeOrderButton;
    @FXML private Button cancelButton;

    private ObservableList<CartItem> orderItems;

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

        // Update total when items change
        orderItems.addListener((javafx.collections.ListChangeListener.Change<? extends CartItem> change) -> {
            updateTotal();
        });
    }

    public void setCartItems(List<CartItem> items) {
        orderItems.clear();
        orderItems.addAll(items);
        updateTotal();
    }

    private void updateTotal() {
        double total = orderItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        orderTotal.setText(String.format("Total: $%.2f", total));
    }

    @FXML
    private void placeOrder() {
        // Validate input
        if (nameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty()) {
            Warning warning = new Warning("Please fill in all fields");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        // Create order
        Order order = new Order(nameField.getText().trim(), emailField.getText().trim());
        orderItems.forEach(item -> order.addItem(item));

        try {
            // Send order to server
            SimpleClient.getClient().sendToServer(order);

            // Show success message
            Warning warning = new Warning("Order placed successfully!");
            EventBus.getDefault().post(new WarningEvent(warning));

            // Clear cart and close window
            OrderPageController.clearCart();
            ((Stage) orderTable.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            Warning warning = new Warning("Error placing order. Please try again.");
            EventBus.getDefault().post(new WarningEvent(warning));
        }
    }

    @FXML
    private void cancel() {
        // Close checkout window
        ((Stage) orderTable.getScene().getWindow()).close();
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
}