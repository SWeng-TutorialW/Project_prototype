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
    @FXML private CheckBox premiumFlowerCheckBox;
    @FXML private CheckBox flowersWreathCheckBox;
    @FXML private CheckBox flowerCrownsCheckBox;
    @FXML private CheckBox vaseCheckBox;
    
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
        
        CheckBox[] categoryCheckBoxes = {flowerCheckBox, premiumFlowerCheckBox, flowersWreathCheckBox, 
                                       flowerCrownsCheckBox, vaseCheckBox};
        

    }
    
    public void setCatalogController(Object controller) {
        this.catalogController = controller;
    }
    
    public void setFilterStage(Stage stage) {
        this.filterStage = stage;
    }
    
    public void setOriginalFlowersList(List<Flower> flowers) {
        this.originalFlowersList = new ArrayList<>(flowers);
        restoreCurrentFilterState();
    }
    
    /**
     * Restores the current filter state to the UI checkboxes and fields
     */
    private void restoreCurrentFilterState() {
        // Restore price range
        minPriceField.setText(String.format("%.0f", minPrice));
        maxPriceField.setText(String.format("%.0f", maxPrice));
        priceRangeSlider.setValue(maxPrice);
        minPriceLabel.setText(String.format("%.0f", minPrice));
        maxPriceLabel.setText(String.format("%.0f", maxPrice));
        
        // Restore color checkboxes
        redCheckBox.setSelected(selectedColors.contains("Red"));
        yellowCheckBox.setSelected(selectedColors.contains("Yellow"));
        pinkCheckBox.setSelected(selectedColors.contains("Pink"));
        whiteCheckBox.setSelected(selectedColors.contains("White"));
        purpleCheckBox.setSelected(selectedColors.contains("Purple"));
        blueCheckBox.setSelected(selectedColors.contains("Blue"));
        orangeCheckBox.setSelected(selectedColors.contains("Orange"));
        greenCheckBox.setSelected(selectedColors.contains("Green"));
        blackCheckBox.setSelected(selectedColors.contains("Black"));
        brownCheckBox.setSelected(selectedColors.contains("Brown"));
        
        // Restore category checkboxes
        flowerCheckBox.setSelected(selectedCategories.contains("Flower"));
        premiumFlowerCheckBox.setSelected(selectedCategories.contains("Premium Flowers"));
        flowersWreathCheckBox.setSelected(selectedCategories.contains("Flowers Wreath"));
        flowerCrownsCheckBox.setSelected(selectedCategories.contains("Flower Crowns"));
        vaseCheckBox.setSelected(selectedCategories.contains("Vase"));
        
        // Restore sort option
        if (sortOption != null && !sortOption.isEmpty()) {
            sortComboBox.setValue(sortOption);
        }
    }
    
    @FXML
    void applyFilters() {
        if (originalFlowersList == null || originalFlowersList.isEmpty()) {
            showAlert("No Data", "No flowers available to filter.");
            return;
        }
        
        List<Flower> filteredList = filterFlowers();
        List<Flower> sortedList = sortFlowers(filteredList);
        
        // Update the catalog controller's filter state
        if (catalogController instanceof CatalogController) {
            ((CatalogController) catalogController).updateCurrentFilterState(
                minPrice, maxPrice, selectedColors, selectedCategories, sortOption
            );
            ((CatalogController) catalogController).setFilteredCatalogData(sortedList);
        } else if (catalogController instanceof CatalogController_employee) {
            ((CatalogController_employee) catalogController).updateCurrentFilterState(
                minPrice, maxPrice, selectedColors, selectedCategories, sortOption
            );
            ((CatalogController_employee) catalogController).setFilteredCatalogData(sortedList);
        }
        

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
        premiumFlowerCheckBox.setSelected(false);
        flowersWreathCheckBox.setSelected(false);
        flowerCrownsCheckBox.setSelected(false);
        vaseCheckBox.setSelected(false);
        
        // Reset price range
        priceRangeSlider.setMin(0);
        priceRangeSlider.setMax(300);
        priceRangeSlider.setValue(300);
        minPriceField.setText("0");
        maxPriceField.setText("300");
        
        // Reset sort
        sortComboBox.setValue("Name (A-Z)");
        
        // Reset filter state
        minPrice = 0.0;
        maxPrice = 300.0;
        selectedColors.clear();
        selectedCategories.clear();
        sortOption = "Name (A-Z)";
        
        // Update the catalog controller's filter state
        if (catalogController instanceof CatalogController) {
            ((CatalogController) catalogController).updateCurrentFilterState(
                minPrice, maxPrice, selectedColors, selectedCategories, sortOption
            );
            ((CatalogController) catalogController).setCatalogData(originalFlowersList);
        } else if (catalogController instanceof CatalogController_employee) {
            ((CatalogController_employee) catalogController).updateCurrentFilterState(
                minPrice, maxPrice, selectedColors, selectedCategories, sortOption
            );
            ((CatalogController_employee) catalogController).setCatalogData(originalFlowersList);
        }

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
        if (premiumFlowerCheckBox.isSelected()) selectedCategories.add("Premium Flowers");
        if (flowersWreathCheckBox.isSelected()) selectedCategories.add("Flowers Wreath");
        if (flowerCrownsCheckBox.isSelected()) selectedCategories.add("Flower Crowns");
        if (vaseCheckBox.isSelected()) selectedCategories.add("Vase");
        
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
    
    /**
     * Sets the current filter state from external source
     */
    public void setCurrentFilterState(double minPrice, double maxPrice, Set<String> colors, Set<String> categories, String sortOption) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.selectedColors = new HashSet<>(colors);
        this.selectedCategories = new HashSet<>(categories);
        this.sortOption = sortOption;
        
        // Update UI if originalFlowersList is already set
        if (originalFlowersList != null) {
            restoreCurrentFilterState();
        }
    }
} 