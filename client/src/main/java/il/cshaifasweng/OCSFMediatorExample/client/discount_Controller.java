package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.FlowerDiscountWrapper;
import il.cshaifasweng.OCSFMediatorExample.entities.update_local_catalog;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class discount_Controller implements Initializable {

    @FXML
    private ComboBox<Flower> flowerComboBox;

    @FXML
    private Slider discountSlider;

    @FXML
    private Label discountPercentageLabel;

    @FXML
    private Label selectedFlowerNameLabel;

    @FXML
    private Label selectedFlowerTypeLabel;

    @FXML
    private Label selectedFlowerPriceLabel;

    @FXML
    private Label selectedFlowerOriginalPriceLabel;

    @FXML
    private ImageView selectedFlowerImageView;

    @FXML
    private Button applyDiscountButton;

    @FXML
    private Button removeDiscountButton;

    @FXML
    private Button cancelButton;

    @FXML
    private VBox flowerDetailsSection;

    @FXML
    private Label currentDiscountLabel;

    private CatalogController_employee catalogController;
    private List<Flower> allFlowers;
    private Flower selectedFlower;
    private int currentDiscountPercentage = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Register with EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        // Initialize UI components
        initializeUI();
        
        // Request all flowers from database
        requestAllFlowers();
    }

    private void initializeUI() {
        // Set up flower combo box
        flowerComboBox.setCellFactory(param -> new ListCell<Flower>() {
            @Override
            protected void updateItem(Flower flower, boolean empty) {
                super.updateItem(flower, empty);
                if (empty || flower == null) {
                    setText(null);
                } else {
                    setText(flower.getFlowerName() + " (" + flower.getFlowerType() + ") - ₪" + String.format("%.2f", flower.getFlowerPrice()));
                }
            }
        });

        flowerComboBox.setButtonCell(flowerComboBox.getCellFactory().call(null));

        // Set up discount slider - changed from 0-50 to 0-100
        discountSlider.setMin(0);
        discountSlider.setMax(100);
        discountSlider.setValue(0);
        discountSlider.setShowTickLabels(true);
        discountSlider.setShowTickMarks(true);
        discountSlider.setMajorTickUnit(20);
        discountSlider.setMinorTickCount(5);

        // Add listeners
        flowerComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedFlower = newVal;
                updateFlowerDetails();
                flowerDetailsSection.setVisible(true);
            } else {
                selectedFlower = null;
                flowerDetailsSection.setVisible(false);
            }
        });

        discountSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentDiscountPercentage = newVal.intValue();
            discountPercentageLabel.setText(currentDiscountPercentage + "%");
            updatePriceDisplay();
        });

        // Initially hide flower details section
        flowerDetailsSection.setVisible(false);
    }

    private void requestAllFlowers() {
        try {
            SimpleClient.getClient().sendToServer("get_all_flowers");
        } catch (IOException e) {
            showError("Error requesting flowers: " + e.getMessage());
        }
    }

    @Subscribe
    public void handleAllFlowersResponse(update_local_catalog event) {
        Platform.runLater(() -> {
            this.allFlowers = event.get_flowers();
            ObservableList<Flower> flowerList = FXCollections.observableArrayList(allFlowers);
            flowerComboBox.setItems(flowerList);
        });
    }

    private void updateFlowerDetails() {
        if (selectedFlower == null) return;

        selectedFlowerNameLabel.setText(selectedFlower.getFlowerName());
        selectedFlowerTypeLabel.setText(selectedFlower.getFlowerType());
        
        // Load flower image using the same method as catalog controller
        setImageFromDatabase(selectedFlowerImageView, selectedFlower.getImage(), selectedFlower.getFlowerType());

        // Show current discount if any
        if (selectedFlower.isSale()) {
            currentDiscountLabel.setText("Current Discount: " + selectedFlower.getDiscount() + "%");
            currentDiscountLabel.setVisible(true);
            // Show remove discount button when flower has existing discount
            removeDiscountButton.setVisible(true);
        } else {
            currentDiscountLabel.setVisible(false);
            // Hide remove discount button when flower has no discount
            removeDiscountButton.setVisible(false);
        }

        updatePriceDisplay();
    }

    // Image loading method copied from CatalogController_employee
    private void setImageFromDatabase(ImageView imageView, String databaseImagePath, String flowerType) {
        if (databaseImagePath != null && !databaseImagePath.trim().isEmpty()) {
            // Try to load from the database path first
            setImage(imageView, databaseImagePath);
            if (imageView.getImage() != null) {
                return; // Successfully loaded from database path
            }
        }
        
        // Fallback to flower type-based image loading
        setImageFallback(imageView, flowerType);
    }

    private void setImageFallback(ImageView imageView, String flowerType) {
        if (flowerType != null && !flowerType.trim().isEmpty()) {
            // Try to load from FlowerImages directory
            String imagePath = "/images/FlowerImages/" + flowerType + ".png";
            setImage(imageView, imagePath);
            if (imageView.getImage() != null) {
                return; // Successfully loaded from FlowerImages
            }
        }
        
        // Final fallback to no_photo.png
        setNoPhotoImage(imageView);
    }

    private void setNoPhotoImage(ImageView imageView) {
        String imagePath = "/images/no_photo.png";
        setImage(imageView, imagePath);
    }

    private void setImage(ImageView imageView, String imagePath) {
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(image);
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imagePath);
            e.printStackTrace();
        }
    }

    private void updatePriceDisplay() {
        if (selectedFlower == null) return;

        double originalPrice = selectedFlower.getFlowerPrice();
        
        // If flower is already on sale, calculate the original price
        if (selectedFlower.isSale()) {
            int existingDiscount = selectedFlower.getDiscount();
            double remainingPercent = 100.0 - existingDiscount;
            originalPrice = selectedFlower.getFlowerPrice() * 100.0 / remainingPercent;
        }

        selectedFlowerOriginalPriceLabel.setText(String.format("Original Price: ₪%.2f", originalPrice));
        
        // Calculate new price with discount
        double newPrice = originalPrice * (1 - currentDiscountPercentage / 100.0);
        selectedFlowerPriceLabel.setText(String.format("New Price: ₪%.2f", newPrice));
        
        // Update button state
        applyDiscountButton.setDisable(currentDiscountPercentage == 0);
    }

    @FXML
    void applyDiscount(ActionEvent event) {
        if (selectedFlower == null) {
            showError("Please select a flower first.");
            return;
        }

        if (currentDiscountPercentage == 0) {
            showError("Please set a discount percentage.");
            return;
        }

        // Create discount wrapper and send to server
        FlowerDiscountWrapper wrapper = new FlowerDiscountWrapper(selectedFlower, currentDiscountPercentage);
        try {
            SimpleClient.getClient().sendToServer(wrapper);
            showSuccess("Discount applied successfully!");
            closeWindow();
        } catch (IOException e) {
            showError("Error applying discount: " + e.getMessage());
        }
    }

    @FXML
    void removeDiscount(ActionEvent event) {
        if (selectedFlower == null) {
            showError("Please select a flower first.");
            return;
        }

        if (!selectedFlower.isSale()) {
            showError("This flower doesn't have a discount to remove.");
            return;
        }

        // Create discount wrapper with -2 to remove discount (restore original price)
        FlowerDiscountWrapper wrapper = new FlowerDiscountWrapper(selectedFlower, -2);
        try {
            SimpleClient.getClient().sendToServer(wrapper);
            showSuccess("Discount removed successfully! Price restored to original.");
            closeWindow();
        } catch (IOException e) {
            showError("Error removing discount: " + e.getMessage());
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        closeWindow();
    }

    private void closeWindow() {
        // Cleanup EventBus registration
        cleanup();
        
        Stage stage = (Stage) ((Node) cancelButton).getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCatalogController(CatalogController_employee controller) {
        this.catalogController = controller;
    }

    // Cleanup when controller is destroyed
    public void cleanup() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
