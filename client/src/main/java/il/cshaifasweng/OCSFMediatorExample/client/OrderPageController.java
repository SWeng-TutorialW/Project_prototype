package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CartItem;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderPageController {
    @FXML
    private ImageView flowerImage;
    @FXML private Label flowerName;
    @FXML private Label flowerType;
    @FXML private Label flowerPrice;
    @FXML private Label storeName;
    @FXML private String store;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private Label totalPrice;
    @FXML private Button addToCartButton;
    @FXML private Button viewCartButton;
    @FXML private LoginRegCheck user;
    
    private Flower selectedFlower;
    private static List<CartItem> cartItems = new ArrayList<>();
    
    public void initialize() {
        // Set up quantity spinner listener
        quantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            updateTotalPrice();
        });
    }
    
    public void setFlower(Flower flower) {
        this.selectedFlower = flower;
        updateUI();
    }

    public void setStore(String store){
        this.store = store;
    }

    public void setUser(LoginRegCheck user){
        this.user = user;
    }



    private void updateUI() {
        if (selectedFlower != null) {
            flowerName.setText(selectedFlower.getFlowerName());
            flowerType.setText("Flower Type: " + selectedFlower.getFlowerType());
            System.out.println("The store of the flower is: " + this.store);
            storeName.setText("Store: " + this.store);

            flowerPrice.setText(String.format("Price: $%.2f", selectedFlower.getFlowerPrice()));
            updateTotalPrice();
            
            // Set flower image
            try {
                String imagePath = "/images/" + selectedFlower.getFlowerType() + ".png";
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                flowerImage.setImage(image);
            } catch (Exception e) {
                System.err.println("Failed to load image for: " + selectedFlower.getFlowerType());
                try {
                    String imagePath = "/images/no_photo.png";
                    Image image = new Image(getClass().getResourceAsStream(imagePath));
                    flowerImage.setImage(image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    private void updateTotalPrice() {
        if (selectedFlower != null) {
            int quantity = quantitySpinner.getValue();
            double total = selectedFlower.getFlowerPrice() * quantity;
            totalPrice.setText(String.format("Total: $%.2f", total));
        }
    }
    
    @FXML
    private void addToCart() {

        if (!this.store.equals(this.user.getStoreName()) && user.getStore() != 4) {
            Warning warning = new Warning("You Can Only Order From The Store: " + user.getStoreName());
            EventBus.getDefault().post(new WarningEvent(warning));
            // Close current window
            Stage stage = (Stage) addToCartButton.getScene().getWindow();
            stage.close();
        }
        else if (selectedFlower != null) {
            int quantity = quantitySpinner.getValue();
            System.out.println("The store of the flower is:" + this.store);
            CartItem cartItem = new CartItem(selectedFlower, quantity, this.store);
            cartItems.add(cartItem);
            
            // Show confirmation
            Warning warning = new Warning("Item added to cart successfully!");
            EventBus.getDefault().post(new WarningEvent(warning));
            // Close current window
            Stage stage = (Stage) addToCartButton.getScene().getWindow();
            stage.close();
        }
    }
    
    @FXML
    private void viewCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cart.fxml"));
            Parent root = loader.load();
            CartController cartController = loader.getController();
            cartController.setCartItems(cartItems);
            
            Stage stage = new Stage();
            stage.setTitle("Shopping Cart");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    
    public static List<CartItem> getCartItems() {
        return cartItems;
    }
    
    public static void clearCart() {
        cartItems.clear();
    }


} 