package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import javafx.application.Platform;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AddFlower_Controller {

    @FXML private TextField flowerNameField;
    @FXML private TextField flowerTypeField;
    @FXML private ComboBox<String> colorComboBox;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField priceField;
    @FXML private TextField imagePathField;
    @FXML private Button addButton;
    @FXML private Button cancelButton;

    // New fields for store mode
    @FXML private ComboBox<Flower> existingFlowersComboBox;
    @FXML private VBox flowerDetailsSection;
    @FXML private ImageView flowerImageView;
    @FXML private Label flowerNameLabel;
    @FXML private Label flowerTypeLabel;
    @FXML private Label flowerColorLabel;
    @FXML private Label flowerCategoryLabel;
    @FXML private Label flowerPriceLabel;

    // Section references
    @FXML private VBox networkFormSection;
    @FXML private VBox storeModeSection;

    private CatalogController_employee catalogController;
    private boolean isNetworkMode = true;
    private List<Flower> availableFlowers;

    @FXML
    public void initialize() {
        // Register with EventBus to receive server responses
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        // Initialize category options
        categoryComboBox.getItems().addAll(
                "Flower", "Premium flower", "Flowers Wreath",
                "Vase", "Flower Crowns"
        );

        // Initialize color options
        colorComboBox.getItems().addAll(
                "Red", "Yellow", "Pink", "White", "Purple", 
                "Blue", "Orange", "Green", "Black", "Brown"
        );

        // Set up existing flowers combo box
        existingFlowersComboBox.setCellFactory(param -> new ListCell<Flower>() {
            @Override
            protected void updateItem(Flower flower, boolean empty) {
                super.updateItem(flower, empty);
                if (empty || flower == null) {
                    setText(null);
                } else {
                    setText(flower.getFlowerName() + " (" + flower.getFlowerType() + ")");
                }
            }
        });

        existingFlowersComboBox.setButtonCell(existingFlowersComboBox.getCellFactory().call(null));

        // Add listener for existing flowers selection
        existingFlowersComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                displayFlowerDetails(newVal);
                flowerDetailsSection.setVisible(true);
            } else {
                // Clear selection - hide flower details
                flowerDetailsSection.setVisible(false);
            }
        });

        // Add validation listeners
        addValidationListeners();
    }

    public void setCatalogController(CatalogController_employee controller) {
        this.catalogController = controller;
    }

    public void setNetworkMode(boolean isNetwork) {
        this.isNetworkMode = isNetwork;
        updateUIForMode();
    }

    public void setAvailableFlowers(List<Flower> flowers) {
        this.availableFlowers = flowers;
        if (!isNetworkMode && flowers != null) {
            existingFlowersComboBox.getItems().clear();
            existingFlowersComboBox.getItems().addAll(flowers);
            System.out.println("AddFlower_Controller: Updated dropdown with " + flowers.size() + " available flowers");
            for (Flower flower : flowers) {
                System.out.println("  - " + flower.getFlowerName() + " (ID: " + flower.getId() + ")");
            }
        }
    }

    private void updateUIForMode() {
        if (isNetworkMode) {
            // Network mode - show form for adding new flowers
            networkFormSection.setVisible(true);
            storeModeSection.setVisible(false);

            addButton.setText("Add New Flower");

            // Update validation for network mode
            addButton.disableProperty().unbind();
            addButton.disableProperty().bind(
                    flowerNameField.textProperty().isEmpty()
                            .or(flowerTypeField.textProperty().isEmpty())
                            .or(colorComboBox.valueProperty().isNull())
                            .or(categoryComboBox.valueProperty().isNull())
                            .or(priceField.textProperty().isEmpty())
            );
        } else {
            // Store mode - show dropdown for existing flowers
            networkFormSection.setVisible(false);
            storeModeSection.setVisible(true);

            addButton.setText("Add to Store");

            // Update validation for store mode
            addButton.disableProperty().unbind();
            addButton.disableProperty().bind(
                    existingFlowersComboBox.valueProperty().isNull()
            );
        }
    }

    private void displayFlowerDetails(Flower flower) {
        // Display flower details like in order page
        flowerNameLabel.setText("Name: " + flower.getFlowerName());
        flowerTypeLabel.setText("Type: " + flower.getFlowerType());
        flowerColorLabel.setText("Color: " + (flower.getColor() != null ? flower.getColor() : "N/A"));
        flowerCategoryLabel.setText("Category: " + (flower.getCategory() != null ? flower.getCategory() : "N/A"));
        flowerPriceLabel.setText(String.format("Price: $%.2f", flower.getFlowerPrice()));

        // Load flower image
        setFlowerImage(flower);
    }

    private void setFlowerImage(Flower flower) {
        try {
            String imagePath = null;

            // If database has a valid image path, use it
            if (flower.getImage() != null && !flower.getImage().trim().isEmpty()) {
                imagePath = flower.getImage();
            } else {
                // Fallback to flower type if no image path in database
                imagePath = "images/" + flower.getFlowerType() + ".png";
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

            InputStream inputStream = getClass().getResourceAsStream(finalImagePath);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                flowerImageView.setImage(image);
            } else {
                // Try fallback to flower type
                setImageFallback(flower);
            }

        } catch (Exception e) {
            System.err.println("Failed to load image from database path: " + flower.getImage() + " for flower type: " + flower.getFlowerType());
            setImageFallback(flower);
        }
    }

    private void setImageFallback(Flower flower) {
        try {
            // First try FlowerImages directory
            String fallbackPath = "/images/FlowerImages/" + flower.getFlowerType() + ".png";
            InputStream inputStream = getClass().getResourceAsStream(fallbackPath);

            if (inputStream == null) {
                // If not found in FlowerImages, try main images directory
                fallbackPath = "/images/" + flower.getFlowerType() + ".png";
                inputStream = getClass().getResourceAsStream(fallbackPath);
            }

            if (inputStream != null) {
                Image image = new Image(inputStream);
                flowerImageView.setImage(image);
            } else {
                // Load no_photo image as final fallback
                setNoPhotoImage();
            }
        } catch (Exception e) {
            System.err.println("Failed to load fallback image for flower type: " + flower.getFlowerType());
            setNoPhotoImage();
        }
    }

    private void setNoPhotoImage() {
        try {
            String noPhotoPath = "/images/no_photo.png";
            InputStream inputStream = getClass().getResourceAsStream(noPhotoPath);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                flowerImageView.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("Failed to load no_photo image as well");
        }
    }

    private void addValidationListeners() {
        // Real-time validation for price field
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                priceField.setText(oldValue);
            }
        });
    }

    @FXML
    private void addFlower() {
        try {
            if (isNetworkMode) {
                addNewFlower();
            } else {
                addExistingFlowerToStore();
            }
        } catch (Exception e) {
            showErrorMessage("Error adding flower: " + e.getMessage());
        }
    }

    private void addNewFlower() {
        try {
            // Validate required fields
            if (!validateNewFlowerInputs()) {
                return;
            }

            // Create new flower object
            Flower newFlower = new Flower();
            newFlower.setFlowerName(flowerNameField.getText().trim());
            newFlower.setFlowerType(flowerTypeField.getText().trim());
            newFlower.setColor(colorComboBox.getValue());
            newFlower.setCategory(categoryComboBox.getValue());
            newFlower.setFlowerPrice(Double.parseDouble(priceField.getText().trim()));

            // Set optional fields
            if (!imagePathField.getText().trim().isEmpty()) {
                newFlower.setImage(imagePathField.getText().trim());
            } else {
                // Set default image path based on flower type
                newFlower.setImage("images/FlowerImages/" + flowerTypeField.getText().trim() + ".png");
            }

            // Set default values
            newFlower.setSale(false);
            newFlower.setDiscount(0);

            // Send to server to add to database
            sendFlowerToServer(newFlower);

            // Show success message
            showSuccessMessage("Flower added successfully to database!");

            // Close the window
            closeWindow();

        } catch (NumberFormatException e) {
            showErrorMessage("Invalid price format. Please enter a valid number.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addExistingFlowerToStore() {
        Flower selectedFlower = existingFlowersComboBox.getValue();
        if (selectedFlower == null) {
            showErrorMessage("Please select a flower to add to your store.");
            return;
        }
        try {
            // Get the current store's ID from the catalog controller
            String selectedStore = catalogController.getSelectedStore();
            int storeId = catalogController.getCurrentStoreId(selectedStore);
            if (storeId == -1) {
                showErrorMessage("Could not determine store ID.");
                return;
            }
            // Send request to add flower to store with both IDs
            String message = "add_flower_to_store_" + selectedFlower.getId() + "_" + storeId;
            SimpleClient.getClient().sendToServer(message);

            // Don't close the window immediately - wait for server response
            // The window will be closed when we receive the success response
        } catch (IOException e) {
            showErrorMessage("Error adding flower to store: " + e.getMessage());
        }
    }

    // Method to handle server response for flower addition
    @Subscribe
    public void handleFlowerAdditionResponse(String response) {
        System.out.println("AddFlower_Controller: Received response: " + response);

        if (response.contains("added to store successfully")) {
            System.out.println("AddFlower_Controller: Success response detected, closing window...");

            // Wrap UI updates in Platform.runLater to ensure they run on FX thread
            Platform.runLater(() -> {
                try {
                    // Clear the ComboBox selection
                    existingFlowersComboBox.setValue(null);

                    // Close the window
                    closeWindow();
                } catch (Exception e) {
                    System.out.println("AddFlower_Controller: Error in UI update: " + e.getMessage());
                }
            });
        } else if (response.startsWith("Flower is already in this store")) {
            System.out.println("AddFlower_Controller: Flower already in store error");
            Platform.runLater(() -> showErrorMessage("This flower is already in the selected store."));
        } else if (response.startsWith("Flower or store not found")) {
            System.out.println("AddFlower_Controller: Flower or store not found error");
            Platform.runLater(() -> showErrorMessage("Flower or store not found. Please try again."));
        } else if (response.startsWith("Error adding flower to store:")) {
            System.out.println("AddFlower_Controller: General error");
            Platform.runLater(() -> showErrorMessage(response));
        } else {
            System.out.println("AddFlower_Controller: Unknown response type");
        }
    }

    // Method to refresh the available flowers dropdown
    private void refreshAvailableFlowers() {
        try {
            String selectedStore = catalogController.getSelectedStore();
            if (selectedStore != null && !selectedStore.equals("network")) {
                // Request fresh list of available flowers for this store
                String message = "get_all_flowers_for_store_selection";
                SimpleClient.getClient().sendToServer(message);

                // Set up the pending controller to refresh this dialog
                catalogController.setPendingAddFlowerController(this);
                catalogController.setPendingStoreName(selectedStore);
            }
        } catch (IOException e) {
            showErrorMessage("Error refreshing flower list: " + e.getMessage());
        }
    }

    private boolean validateNewFlowerInputs() {
        // Check required fields
        if (flowerNameField.getText().trim().isEmpty()) {
            showErrorMessage("Flower name is required.");
            return false;
        }

        if (flowerTypeField.getText().trim().isEmpty()) {
            showErrorMessage("Flower type is required.");
            return false;
        }

        if (colorComboBox.getValue() == null) {
            showErrorMessage("Color is required.");
            return false;
        }

        if (categoryComboBox.getValue() == null) {
            showErrorMessage("Please select a category.");
            return false;
        }

        if (priceField.getText().trim().isEmpty()) {
            showErrorMessage("Price is required.");
            return false;
        }

        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price <= 0) {
                showErrorMessage("Price must be greater than 0.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Invalid price format. Please enter a valid number.");
            return false;
        }

        return true;
    }

    private void sendFlowerToServer(Flower flower) throws IOException {
        // Create a wrapper object to send the flower to the server
        // This will be handled by the server to add to the Flowers table
        String message = "add_flower_to_database";
        SimpleClient.getClient().sendToServer(message);
        SimpleClient.getClient().sendToServer(flower);

        System.out.println("Sent flower to server for database addition: " + flower.getFlowerName());
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void cancel() {
        closeWindow();
    }

    private void closeWindow() {
        System.out.println("AddFlower_Controller: closeWindow() called");

        // Unregister from EventBus
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
            System.out.println("AddFlower_Controller: Unregistered from EventBus");
        }

        try {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
            System.out.println("AddFlower_Controller: Window closed successfully");
        } catch (Exception e) {
            System.err.println("AddFlower_Controller: Error closing window: " + e.getMessage());
            e.printStackTrace();
        }
    }
}