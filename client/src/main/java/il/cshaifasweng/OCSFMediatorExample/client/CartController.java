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
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class CartController {
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> nameColumn;
    @FXML private TableColumn<CartItem, String> storeColumn;
    @FXML private TableColumn<CartItem, String> typeColumn;
    @FXML private TableColumn<CartItem, String> colorColumn;
    @FXML private TableColumn<CartItem, String> categoryColumn;
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

    static Stage cartStage = null;

    public static boolean isCartOpen() {
        return cartStage != null && cartStage.isShowing();
    }

    public static void setCartStage(Stage stage) {
        cartStage = stage;
        if (cartStage != null) {
            cartStage.setOnHidden(e -> cartStage = null);
        }
    }

    @FXML
    public void initialize() {
        // Set up table columns
        nameColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getFlower().getFlowerName()));
        storeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStore()));
        typeColumn.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getFlower().getFlowerType()));
        colorColumn.setCellValueFactory(cellData -> {
            String color = cellData.getValue().getFlower().getColor();
            return new SimpleStringProperty(color != null ? color : "N/A");
        });
        categoryColumn.setCellValueFactory(cellData -> {
            String category = cellData.getValue().getFlower().getCategory();
            return new SimpleStringProperty(category != null ? category : "N/A");
        });
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

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        // Unregister from EventBus when window closes
        cartTable.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                if (newScene.getWindow() != null) {
                    newScene.getWindow().setOnHidden(e -> {
                        if (EventBus.getDefault().isRegistered(this)) {
                            EventBus.getDefault().unregister(this);
                        }
                    });
                } else {
                    // Listen for the window property to become non-null
                    newScene.windowProperty().addListener((obsWin, oldWin, newWin) -> {
                        if (newWin != null) {
                            newWin.setOnHidden(e -> {
                                if (EventBus.getDefault().isRegistered(this)) {
                                    EventBus.getDefault().unregister(this);
                                }
                            });
                        }
                    });
                }
            }
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
        cartTotal.setText(String.format("Total: ₪%.2f", total));
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

        if (CheckoutController.isCheckoutOpen()) {
            CheckoutController.checkoutStage.close();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("checkout.fxml"));
            Parent root = loader.load();
            CheckoutController checkoutController = loader.getController();
            checkoutController.setCartItems(cartItems);
            checkoutController.setCurrentUser(currentUser);

            Stage stage = new Stage();
            CheckoutController.setCheckoutStage(stage);
            stage.setTitle("Checkout");
            stage.setScene(new Scene(root));
            stage.show();

            // Close cart window
            Stage cartStage = (Stage) checkoutButton.getScene().getWindow();
            cartStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            Warning warning = new Warning("Error opening checkout");
            EventBus.getDefault().post(new WarningEvent(warning));
        }
    }

    @FXML
    private void continueShopping() {
        Stage stage = (Stage) continueShoppingButton.getScene().getWindow();
        stage.close();
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
    private void openCartWarningIfOpen() {
        if (isCartOpen()) {
            Warning warning = new Warning("The cart window is already open.");
            EventBus.getDefault().post(new WarningEvent(warning));
            cartStage.toFront();
            cartStage.requestFocus();
        }
    }

    @Subscribe
    public void onCartUpdated(CartUpdatedEvent event) {
        // Refresh the cart display with the latest items from the shared cart
        cartItems.clear();
        cartItems.addAll(OrderPageController.getCartItems());
        updateTotal();
    }
}