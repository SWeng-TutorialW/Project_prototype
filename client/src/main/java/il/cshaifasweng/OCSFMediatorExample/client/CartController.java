package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CartItem;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
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
import java.util.List;

public class CartController {
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> nameColumn;
    @FXML private TableColumn<CartItem, String> storeColumn;
    @FXML private TableColumn<CartItem, String> typeColumn;
    @FXML private TableColumn<CartItem, Double> priceColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;
    @FXML private TableColumn<CartItem, Double> totalColumn;
    @FXML private TableColumn<CartItem, Void> actionColumn;
    @FXML private Label cartTotal;
    @FXML private Button checkoutButton;
    @FXML private Button continueShoppingButton;
    private LoginRegCheck currentUser; // Store the current user

    private ObservableList<CartItem> cartItems;


    private CatalogController catalogController;
    public void setCatalogController(CatalogController catalogController) {
        this.catalogController = catalogController;
    }

    public void initialize() {
        // Set up table columns
        nameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getFlower().getFlowerName()));
        storeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStore()));
        typeColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getFlower().getFlowerType()));
        priceColumn.setCellValueFactory(cellData ->
            new SimpleDoubleProperty(cellData.getValue().getFlower().getFlowerPrice()).asObject());
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(cellData ->
            new SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());

        // Add remove button to action column
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                removeButton.setOnAction(event -> {
                    CartItem item = getTableView().getItems().get(getIndex());
                    removeItem(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : removeButton);
            }
        });

        // Initialize cart items list
        cartItems = FXCollections.observableArrayList();
        cartTable.setItems(cartItems);

        // Update total when items change
        cartItems.addListener((javafx.collections.ListChangeListener.Change<? extends CartItem> change) -> {
            updateTotal();
        });
    }

    public void setCartItems(List<CartItem> items) {
        cartItems.clear();
        cartItems.addAll(items);
        updateTotal();
    }

    public void setCurrentUser(LoginRegCheck user) {
        this.currentUser = user;
    }

    private void updateTotal() {
        double total = cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cartTotal.setText(String.format("Total: $%.2f", total));
    }

    private void removeItem(CartItem item) {
        cartItems.remove(item);
        OrderPageController.getCartItems().remove(item);
        System.out.println("Item Removed:" + item);
        System.out.println("cart items" + cartItems);
        updateTotal();

        // Show confirmation
        Warning warning = new Warning("Item removed from cart");
        EventBus.getDefault().post(new WarningEvent(warning));
    }

    @FXML
    private void checkout() {
        System.out.println("Checkout button clicked");
        System.out.println("Current user in cart: " + (currentUser != null ? currentUser.getUsername() : "null"));
        System.out.println("Cart items size: " + cartItems.size());

        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty");
            Warning warning = new Warning("Your cart is empty!");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        // Check if user is logged in
        if (currentUser.getIsLogin() == 0) {
            System.out.println("User not logged in, opening login window");
            Warning warning = new Warning("Please log in to proceed with checkout");
            EventBus.getDefault().post(new WarningEvent(warning));

            // Open login window
            try {
                System.out.println("Attempting to load login screen");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("login_screen.fxml"));
                Parent root = loader.load();
                LoginController loginController = loader.getController();
                loginController.setCatalogController(catalogController);
                System.out.println("Login screen loaded successfully");
                Stage stage = new Stage();
                stage.setTitle("Login Required");
                stage.setScene(new Scene(root));
                stage.show();
                System.out.println("Login window shown");
            } catch (IOException e) {
                System.err.println("Error opening login window: " + e.getMessage());
                e.printStackTrace();
                Warning errorWarning = new Warning("Error opening login window");
                EventBus.getDefault().post(new WarningEvent(errorWarning));
            }
            return;
        }

        // Check if current user is available, use SimpleClient as fallback
     /*   if (currentUser == null) {
            currentUser = SimpleClient.getCurrentUser();
            System.out.println("Using SimpleClient current user: " + (currentUser != null ? currentUser.getUsername() : "null"));
        }

        if (currentUser == null) {
            System.out.println("Current user is null despite being logged in");
            Warning warning = new Warning("User session not found. Please log in again.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }*/

        System.out.println("User is logged in, proceeding to checkout");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("checkout.fxml"));
            Parent root = loader.load();
            CheckoutController checkoutController = loader.getController();
            checkoutController.setCartItems(cartItems);
            checkoutController.setCurrentUser(currentUser); // Pass the user to checkout

            Stage stage = new Stage();
            stage.setTitle("Checkout");
            stage.setScene(new Scene(root));
            stage.show();

            // Close cart window
            ((Stage) cartTable.getScene().getWindow()).close();
        } catch (IOException e) {
            System.err.println("Error loading checkout.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void continueShopping() {
        // Close cart window
        ((Stage) cartTable.getScene().getWindow()).close();
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