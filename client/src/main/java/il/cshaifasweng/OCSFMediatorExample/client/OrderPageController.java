package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.CartItem;
import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.entities.update_local_catalog;
import il.cshaifasweng.OCSFMediatorExample.entities.Add_flower_event;
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
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class OrderPageController {
    @FXML
    private ImageView flowerImage;
    @FXML private Label flowerName;
    @FXML private Label flowerType;
    @FXML private Label flowerColor;
    @FXML private Label flowerCategory;
    @FXML private Label flowerPrice;
    @FXML private Label storeName;
    private String store;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private Label totalPrice;
    @FXML private Button addToCartButton;
    @FXML private Button viewCartButton;
    @FXML private LoginRegCheck user;
    
    private Flower selectedFlower;
    private static List<CartItem> cartItems = new ArrayList<>();
    private List<Flower> currentCatalogFlowers; // Store the current catalog flowers for validation
    private boolean catalogRefreshRequested = false; // Flag to track if catalog refresh was requested


    public void initialize() {
        // Set up quantity spinner listener
        quantitySpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            updateTotalPrice();
        });
        
        // Register with EventBus to receive catalog updates
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        
        // Unregister from EventBus when window closes
        addToCartButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
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

    /**
     * Set the current catalog flowers for validation
     * @param flowers The list of flowers currently in the catalog
     */
    public void setCurrentCatalogFlowers(List<Flower> flowers) {
        this.currentCatalogFlowers = flowers;
    }

    private void updateUI() {
        if (selectedFlower != null) {
            flowerName.setText(selectedFlower.getFlowerName());
            flowerType.setText("Flower Type: " + selectedFlower.getFlowerType());
            flowerColor.setText("Color: " + (selectedFlower.getColor() != null ? selectedFlower.getColor() : "N/A"));
            flowerCategory.setText("Category: " + (selectedFlower.getCategory() != null ? selectedFlower.getCategory() : "N/A"));
            System.out.println("The store of the flower is: " + this.store);
            storeName.setText("Store: " + this.store);

            flowerPrice.setText(String.format("Price: ₪%.2f", selectedFlower.getFlowerPrice()));
            updateTotalPrice();
            
            // Set flower image using database path with fallback
            setFlowerImage();
        }
    }
    
    private void setFlowerImage() {
        try {
            String imagePath = null;
            
            // If database has a valid image path, use it
            if (selectedFlower.getImage() != null && !selectedFlower.getImage().trim().isEmpty()) {
                imagePath = selectedFlower.getImage();
            } else {
                // Fallback to flower type if no image path in database
                imagePath = "images/" + selectedFlower.getFlowerType() + ".png";
            }
            
            // Handle different path formats
            String finalImagePath;
            if (imagePath.startsWith("images/")) {
                // Convert to resource path format
                finalImagePath = "/" + imagePath;
            } else if (imagePath.startsWith("/")) {
                // Already in resource path format
                finalImagePath = imagePath;
            } else {
                // Assume it's a relative path, add images/ prefix
                finalImagePath = "/images/" + imagePath;
            }
            
            System.out.println("Loading flower image from path: " + imagePath + " -> " + finalImagePath);
            
            InputStream inputStream = getClass().getResourceAsStream(finalImagePath);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                flowerImage.setImage(image);
            } else {
                // Try fallback to flower type
                setImageFallback();
            }
            
        } catch (Exception e) {
            System.err.println("Failed to load image from database path: " + selectedFlower.getImage() + " for flower type: " + selectedFlower.getFlowerType());
            setImageFallback();
        }
    }
    
    private void setImageFallback() {
        try {
            // First try FlowerImages directory
            String fallbackPath = "/images/FlowerImages/" + selectedFlower.getFlowerType() + ".png";
            InputStream inputStream = getClass().getResourceAsStream(fallbackPath);
            
            if (inputStream == null) {
                // If not found in FlowerImages, try main images directory
                fallbackPath = "/images/" + selectedFlower.getFlowerType() + ".png";
                inputStream = getClass().getResourceAsStream(fallbackPath);
            }
            
            if (inputStream != null) {
                System.out.println("Using fallback image: " + fallbackPath);
                Image image = new Image(inputStream);
                flowerImage.setImage(image);
            } else {
                // Load no_photo image as final fallback
                setNoPhotoImage();
            }
        } catch (Exception e) {
            System.err.println("Failed to load fallback image for flower type: " + selectedFlower.getFlowerType());
            setNoPhotoImage();
        }
    }
    
    private void setNoPhotoImage() {
        try {
            String noPhotoPath = "/images/no_photo.png";
            System.out.println("Loading no_photo image: " + noPhotoPath);
            InputStream inputStream = getClass().getResourceAsStream(noPhotoPath);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                flowerImage.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("Failed to load no_photo image as well");
        }
    }
    
    private void updateTotalPrice() {
        if (selectedFlower != null) {
            int quantity = quantitySpinner.getValue();
            double total = selectedFlower.getFlowerPrice() * quantity;
            totalPrice.setText(String.format("Total: ₪%.2f", total));
        }
    }

    @FXML
    private void addToCart() {
        if (user == null || user.getIsLogin() == 0 || SimpleClient.isGuest) {
            System.out.println("User not logged in");
            Warning warning = new Warning("Please log in to add items to cart");
            EventBus.getDefault().post(new WarningEvent(warning));
            closeWindow();
            return;
        }

        // אם המשתמש לא מנהל רשת – ניתן להזמין רק מהחנות שלו
        if (user.getStore() != 4 && !this.store.equals(user.getStoreName())) {
            Warning warning = new Warning("You can only order from your own store: " + user.getStoreName());
            EventBus.getDefault().post(new WarningEvent(warning));
            closeWindow();
            return;
        }

        int quantity = quantitySpinner.getValue();

        // הוספה לעגלה לפי store השמור בקונטרולר
        boolean found = false;
        for (CartItem item : cartItems) {
            if (item.getFlower().getId() == selectedFlower.getId() && item.getStore().equals(this.store)) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }

        if (!found) {
            CartItem cartItem = new CartItem(selectedFlower, quantity, this.store);
            cartItems.add(cartItem);
            System.out.println("✅ Added to cart: " + selectedFlower.getFlowerName() + " x" + quantity);
        }

        EventBus.getDefault().post(new SuccessEvent(new Success("Item added to cart successfully!")));
        EventBus.getDefault().post(new CartUpdatedEvent());
        closeWindow();
    }



    /**
     * Handle catalog updates from server
     */
    @Subscribe
    public void handleCatalogUpdate(update_local_catalog event) {
        if (catalogRefreshRequested) {
            // Update the current catalog flowers with fresh data
            this.currentCatalogFlowers = event.get_flowers();
            catalogRefreshRequested = false;
            
            // Now perform the validation with fresh data
            performAddToCartValidation();
        }
    }
    
    /**
     * Handle network catalog updates from server
     */
    @Subscribe
    public void handleNetworkCatalogUpdate(Add_flower_event event) {
        if (catalogRefreshRequested && this.store.equals("network")) {
            // Update the current catalog flowers with fresh data
            this.currentCatalogFlowers = event.get_flowers();
            catalogRefreshRequested = false;
            
            // Now perform the validation with fresh data
            performAddToCartValidation();
        }
    }
    
    /**
     * Perform the actual add to cart validation and operation
     */
    private void performAddToCartValidation() {
        // Check if the flower is available in the current store's catalog
        System.out.println("performAddToCartValidation CALLED");
        if (!isFlowerAvailableInStore(selectedFlower, this.store)) {
            Warning warning = new Warning("This flower is not available in the current store catalog. Please refresh the catalog or try a different store.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        if (selectedFlower != null) {
            int quantity = quantitySpinner.getValue();
            boolean found = false;
            for (CartItem item : cartItems) {
                if (item.getFlower().getId() == selectedFlower.getId() && item.getStore().equals(this.store)) {
                    item.setQuantity(item.getQuantity() + quantity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                CartItem cartItem = new CartItem(selectedFlower, quantity, this.store);
                cartItems.add(cartItem);
            }
            // Show confirmation
            Success success = new Success("Item added to cart successfully!");
            EventBus.getDefault().post(new SuccessEvent(success));
            // Notify cart window to refresh
            EventBus.getDefault().post(new CartUpdatedEvent());
        }
        
        // Note: Window is already closed, so no need to close it again
    }
    
    @FXML
    private void viewCart() {
        if (CartController.isCartOpen()) {
            Warning warning = new Warning("The cart window is already open.");
            EventBus.getDefault().post(new WarningEvent(warning));
            CartController.setCartStage(CartController.cartStage); // bring to front
            CartController.cartStage.toFront();
            CartController.cartStage.requestFocus();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("cart.fxml"));
            Parent root = loader.load();
            CartController cartController = loader.getController();
            cartController.setCartItems(cartItems);

            Stage stage = new Stage();
            CartController.setCartStage(stage);
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

    /**
     * Check if a flower is available in the current store's catalog
     * @param flower The flower to check
     * @param storeName The store name to check against
     * @return true if the flower is available in the store catalog, false otherwise
     */
    private boolean isFlowerAvailableInStore(Flower flower, String storeName) {
        if (flower == null || storeName == null) {
            return false;
        }
        
        // If we have the current catalog flowers, check if the flower exists in it
        if (currentCatalogFlowers != null && !currentCatalogFlowers.isEmpty()) {
            return currentCatalogFlowers.stream()
                .anyMatch(catalogFlower -> catalogFlower.getId() == flower.getId());
        }
        
        // If we don't have catalog data, assume it's available to avoid blocking valid operations
        // This could happen if the order page is opened directly without going through catalog
        return true;
    }

    /**
     * Refresh the store's flower list from database before validation
     */
    private void refreshStoreCatalog() {
        try {
            if (this.store != null) {
                if (this.store.equals("network")) {
                    // For network view, get all flowers
                    SimpleClient.getClient().sendToServer("get_all_flowers");
                } else {
                    // For specific store, get store-specific catalog
                    int storeId = getStoreIdFromName(this.store);
                    if (storeId != -1) {
                        SimpleClient.getClient().sendToServer("get_catalog_" + storeId);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error refreshing store catalog: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Helper method to get store ID from store name
     */
    private int getStoreIdFromName(String storeName) {
        switch (storeName) {
            case "Haifa": return 1;
            case "Krayot": return 2;
            case "Nahariyya": return 3;
            case "network": return 4;
            default: return -1;
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) addToCartButton.getScene().getWindow();
        stage.close();
    }
} 