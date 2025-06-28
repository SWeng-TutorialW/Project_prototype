package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.util.*;
import java.util.stream.Collectors;

public class CatalogFilterController {
    
    @FXML private Slider priceRangeSlider;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private Label minPriceLabel;
    @FXML private Label maxPriceLabel;
    @FXML private Label resultsLabel;
    
    // Color checkboxes
    @FXML private CheckBox redCheckBox;
    @FXML private CheckBox yellowCheckBox;
    @FXML private CheckBox pinkCheckBox;
    @FXML private CheckBox whiteCheckBox;
    @FXML private CheckBox purpleCheckBox;
    @FXML private CheckBox blueCheckBox;
    @FXML private CheckBox orangeCheckBox;
    @FXML private CheckBox greenCheckBox;
    @FXML private CheckBox blackCheckBox;
    @FXML private CheckBox brownCheckBox;
    
    // Category checkboxes
    @FXML private CheckBox flowerCheckBox;
    @FXML private CheckBox bouquetCheckBox;
    @FXML private CheckBox plantCheckBox;
    @FXML private CheckBox giftCheckBox;
    @FXML private CheckBox seasonalCheckBox;
    
    @FXML private ComboBox<String> sortComboBox;
    @FXML private Button applyFilterButton;
    @FXML private Button clearFilterButton;
    
    private List<Flower> originalFlowersList;
    private Object catalogController; // Use Object to handle both controller types
    private Stage filterStage;
    
    // Filter criteria
    private double minPrice = 0.0;
    private double maxPrice = 300.0;
    private Set<String> selectedColors = new HashSet<>();
    private Set<String> selectedCategories = new HashSet<>();
    private String sortOption = "";
    
    @FXML
    void initialize() {
        setupPriceSlider();
        setupPriceFields();
        setupSortComboBox();
        setupEventListeners();
    }
    
    private void setupPriceSlider() {
        priceRangeSlider.setMin(0.0);
        priceRangeSlider.setMax(300.0);
        priceRangeSlider.setValue(300.0);
        maxPriceLabel.setText("300");
        maxPriceField.setText("300");
        
        priceRangeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            maxPrice = newVal.doubleValue();
            maxPriceLabel.setText(String.format("%.0f", maxPrice));
            maxPriceField.setText(String.format("%.0f", maxPrice));
        });
    }
    
    private void setupPriceFields() {
        minPriceField.setText("0");
        maxPriceField.setText("300");
        
        minPriceField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                if (!newVal.isEmpty()) {
                    minPrice = Double.parseDouble(newVal);
                    if (minPrice > maxPrice) {
                        minPrice = maxPrice;
                        minPriceField.setText(String.format("%.0f", minPrice));
                    }
                    minPriceLabel.setText(String.format("%.0f", minPrice));
                }
            } catch (NumberFormatException e) {
                // Invalid input, ignore
            }
        });
        
        maxPriceField.textProperty().addListener((obs, oldVal, newVal) -> {
            try {
                if (!newVal.isEmpty()) {
                    maxPrice = Double.parseDouble(newVal);
                    if (maxPrice < minPrice) {
                        maxPrice = minPrice;
                        maxPriceField.setText(String.format("%.0f", maxPrice));
                    }
                    maxPriceLabel.setText(String.format("%.0f", maxPrice));
                    priceRangeSlider.setValue(maxPrice);
                }
            } catch (NumberFormatException e) {
                // Invalid input, ignore
            }
        });
    }
    
    private void setupSortComboBox() {
        sortComboBox.setItems(FXCollections.observableArrayList(
            "Name (A-Z)",
            "Name (Z-A)", 
            "Price (Low to High)",
            "Price (High to Low)",
            "Category",
            "Color"
        ));
        sortComboBox.setValue("Name (A-Z)");
    }
    
    private void setupEventListeners() {
        // Add listeners to all checkboxes to update results count
        CheckBox[] colorCheckBoxes = {redCheckBox, yellowCheckBox, pinkCheckBox, whiteCheckBox, 
                                    purpleCheckBox, blueCheckBox, orangeCheckBox, greenCheckBox, 
                                    blackCheckBox, brownCheckBox};
        
        CheckBox[] categoryCheckBoxes = {flowerCheckBox, bouquetCheckBox, plantCheckBox, 
                                       giftCheckBox, seasonalCheckBox};
        
        for (CheckBox cb : colorCheckBoxes) {
            cb.selectedProperty().addListener((obs, oldVal, newVal) -> updateResultsLabel());
        }
        
        for (CheckBox cb : categoryCheckBoxes) {
            cb.selectedProperty().addListener((obs, oldVal, newVal) -> updateResultsLabel());
        }
    }
    
    public void setCatalogController(Object controller) {
        this.catalogController = controller;
    }
    
    public void setFilterStage(Stage stage) {
        this.filterStage = stage;
    }
    
    public void setOriginalFlowersList(List<Flower> flowers) {
        this.originalFlowersList = new ArrayList<>(flowers);
        updateResultsLabel();
    }
    
    @FXML
    void applyFilters() {
        if (originalFlowersList == null || originalFlowersList.isEmpty()) {
            showAlert("No Data", "No flowers available to filter.");
            return;
        }
        
        List<Flower> filteredList = filterFlowers();
        List<Flower> sortedList = sortFlowers(filteredList);
        
        // Apply filtered data to the appropriate controller
        if (catalogController instanceof CatalogController) {
            ((CatalogController) catalogController).setFilteredCatalogData(sortedList);
        } else if (catalogController instanceof CatalogController_employee) {
            ((CatalogController_employee) catalogController).setFilteredCatalogData(sortedList);
        }
        
        updateResultsLabel();
        
        // Close the filter window
        if (filterStage != null) {
            filterStage.close();
        }
    }
    
    @FXML
    void clearFilters() {
        // Reset all checkboxes
        redCheckBox.setSelected(false);
        yellowCheckBox.setSelected(false);
        pinkCheckBox.setSelected(false);
        whiteCheckBox.setSelected(false);
        purpleCheckBox.setSelected(false);
        blueCheckBox.setSelected(false);
        orangeCheckBox.setSelected(false);
        greenCheckBox.setSelected(false);
        blackCheckBox.setSelected(false);
        brownCheckBox.setSelected(false);
        
        flowerCheckBox.setSelected(false);
        bouquetCheckBox.setSelected(false);
        plantCheckBox.setSelected(false);
        giftCheckBox.setSelected(false);
        seasonalCheckBox.setSelected(false);
        
        // Reset price range
        priceRangeSlider.setMin(0);
        priceRangeSlider.setMax(300);
        priceRangeSlider.setValue(300);
        minPriceField.setText("0");
        maxPriceField.setText("300");
        
        // Reset sort
        sortComboBox.setValue("Name (A-Z)");
        
        // Show all flowers
        if (catalogController instanceof CatalogController) {
            ((CatalogController) catalogController).setCatalogData(originalFlowersList);
        } else if (catalogController instanceof CatalogController_employee) {
            ((CatalogController_employee) catalogController).setCatalogData(originalFlowersList);
        }
        
        updateResultsLabel();
    }
    
    private List<Flower> filterFlowers() {
        // Collect selected colors
        selectedColors.clear();
        if (redCheckBox.isSelected()) selectedColors.add("Red");
        if (yellowCheckBox.isSelected()) selectedColors.add("Yellow");
        if (pinkCheckBox.isSelected()) selectedColors.add("Pink");
        if (whiteCheckBox.isSelected()) selectedColors.add("White");
        if (purpleCheckBox.isSelected()) selectedColors.add("Purple");
        if (blueCheckBox.isSelected()) selectedColors.add("Blue");
        if (orangeCheckBox.isSelected()) selectedColors.add("Orange");
        if (greenCheckBox.isSelected()) selectedColors.add("Green");
        if (blackCheckBox.isSelected()) selectedColors.add("Black");
        if (brownCheckBox.isSelected()) selectedColors.add("Brown");
        
        // Collect selected categories
        selectedCategories.clear();
        if (flowerCheckBox.isSelected()) selectedCategories.add("Flower");
        if (bouquetCheckBox.isSelected()) selectedCategories.add("Bouquet");
        if (plantCheckBox.isSelected()) selectedCategories.add("Plant");
        if (giftCheckBox.isSelected()) selectedCategories.add("Gift");
        if (seasonalCheckBox.isSelected()) selectedCategories.add("Seasonal");
        
        // Get sort option
        sortOption = sortComboBox.getValue();
        
        // Apply filters
        List<Flower> filteredList = originalFlowersList.stream()
            .filter(this::matchesPriceFilter)
            .filter(this::matchesColorFilter)
            .filter(this::matchesCategoryFilter)
            .collect(Collectors.toList());
        
        return filteredList;
    }
    
    private boolean matchesPriceFilter(Flower flower) {
        return flower.getFlowerPrice() >= minPrice && flower.getFlowerPrice() <= maxPrice;
    }
    
    private boolean matchesColorFilter(Flower flower) {
        if (selectedColors.isEmpty()) {
            return true; // No color filter selected, show all
        }
        
        String flowerColor = flower.getColor();
        if (flowerColor == null || flowerColor.isEmpty()) {
            return false; // Flowers without color don't match any color filter
        }
        
        return selectedColors.contains(flowerColor);
    }
    
    private boolean matchesCategoryFilter(Flower flower) {
        if (selectedCategories.isEmpty()) {
            return true; // No category filter selected, show all
        }
        
        String flowerCategory = flower.getCategory();
        if (flowerCategory == null || flowerCategory.isEmpty()) {
            return false; // Flowers without category don't match any category filter
        }
        
        return selectedCategories.contains(flowerCategory);
    }
    
    private List<Flower> sortFlowers(List<Flower> flowers) {
        switch (sortOption) {
            case "Name (A-Z)":
                flowers.sort(Comparator.comparing(Flower::getFlowerName));
                break;
            case "Name (Z-A)":
                flowers.sort(Comparator.comparing(Flower::getFlowerName).reversed());
                break;
            case "Price (Low to High)":
                flowers.sort(Comparator.comparing(Flower::getFlowerPrice));
                break;
            case "Price (High to Low)":
                flowers.sort(Comparator.comparing(Flower::getFlowerPrice).reversed());
                break;
            case "Category":
                flowers.sort(Comparator.comparing(f -> 
                    f.getCategory() != null ? f.getCategory() : ""));
                break;
            case "Color":
                flowers.sort(Comparator.comparing(f -> 
                    f.getColor() != null ? f.getColor() : ""));
                break;
        }
        return flowers;
    }
    
    private void updateResultsLabel() {
        if (originalFlowersList == null) {
            resultsLabel.setText("No flowers available");
            return;
        }
        
        // Apply current filters to get count
        long filteredCount = originalFlowersList.stream()
            .filter(this::matchesPriceFilter)
            .filter(this::matchesColorFilter)
            .filter(this::matchesCategoryFilter)
            .count();
        
        resultsLabel.setText(String.format("Showing %d of %d items", filteredCount, originalFlowersList.size()));
    }
    
    private void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    public boolean hasActiveFilters() {
        return minPrice > 0.0 || maxPrice < 300.0 ||
               !selectedColors.isEmpty() || !selectedCategories.isEmpty();
    }
} 