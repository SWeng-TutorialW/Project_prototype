package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReportGeneratorController {

    @FXML
    private DatePicker startDatePicker;
    
    @FXML
    private DatePicker endDatePicker;
    
    @FXML
    private Button generateReportBtn;
    
    @FXML
    private Button quickRangeBtn;
    
    @FXML
    private RadioButton ordersReport;
    
    @FXML
    private RadioButton revenueReport;
    
    @FXML
    private RadioButton productsReport;
    
    @FXML
    private RadioButton customersReport;
    
    @FXML
    private RadioButton complaintsReport;
    
    @FXML
    private LineChart<String, Number> revenueChart;
    
    @FXML
    private CategoryAxis revenueXAxis;
    
    @FXML
    private NumberAxis revenueYAxis;
    
    @FXML
    private PieChart productChart;
    
    @FXML
    private Label totalOrdersLbl;
    
    @FXML
    private Label totalRevenueLbl;
    
    @FXML
    private Label avgOrderLbl;
    
    @FXML
    private Label topProductLbl;
    
    @FXML
    private Button exportPdfBtn;
    
    @FXML
    private Button exportCsvBtn;
    
    @FXML
    private Button emailReportBtn;
    
    @FXML
    private Button refreshBtn;
    
    @FXML
    private Button backBtn;
    
    // Sample data for demonstration (will be replaced with real data)
    private Map<String, Double> revenueData = new HashMap<>();
    private Map<String, Integer> productData = new HashMap<>();
    private Map<String, Integer> customerData = new HashMap<>();
    private Map<String, Integer> complaintData = new HashMap<>();
    private Map<String, Double> branchData = new HashMap<>();
    
    private int totalOrders = 0;
    private double totalRevenue = 0.0;
    private double avgOrderValue = 0.0;
    private String topProduct = "N/A";
    
    private ToggleGroup reportTypeGroup;
    
    @FXML
    void initialize() {
        if (EventBus.getDefault().isRegistered(this)) {
            System.out.println("ReportGeneratorController already registered");
        } else {
            EventBus.getDefault().register(this);
        }
        
        setupDatePickers();
        setupReportTypeGroup();
        setupCharts();
        loadSampleData();
        updateQuickRangeButton();
    }
    
    private void setupDatePickers() {
        // Set default dates (current month)
        LocalDate now = LocalDate.now();
        startDatePicker.setValue(now.withDayOfMonth(1));
        endDatePicker.setValue(now);
        
        // Add listeners for date changes
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && endDatePicker.getValue() != null) {
                if (newValue.isAfter(endDatePicker.getValue())) {
                    endDatePicker.setValue(newValue);
                }
            }
        });
        
        endDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && startDatePicker.getValue() != null) {
                if (newValue.isBefore(startDatePicker.getValue())) {
                    startDatePicker.setValue(newValue);
                }
            }
        });
    }
    
    private void setupReportTypeGroup() {
        reportTypeGroup = new ToggleGroup();
        ordersReport.setToggleGroup(reportTypeGroup);
        revenueReport.setToggleGroup(reportTypeGroup);
        productsReport.setToggleGroup(reportTypeGroup);
        customersReport.setToggleGroup(reportTypeGroup);
        complaintsReport.setToggleGroup(reportTypeGroup);
        
        // Default selection
        ordersReport.setSelected(true);
        
        // Add listeners for report type changes
        reportTypeGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateChartsForReportType();
            }
        });
    }
    
    private void setupCharts() {
        // Setup revenue chart using ChartUtils
        ChartUtils.setupRevenueChart(revenueChart, revenueXAxis, revenueYAxis);
        
        // Setup product chart using ChartUtils
        ChartUtils.setupProductChart(productChart);
    }
    
    private void loadSampleData() {
        // Generate sample data using ChartUtils
        LocalDate startDate = startDatePicker.getValue() != null ? startDatePicker.getValue() : LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = endDatePicker.getValue() != null ? endDatePicker.getValue() : LocalDate.now();
        
        revenueData = ChartUtils.generateSampleRevenueData(startDate, endDate);
        productData = ChartUtils.generateSampleProductData();
        customerData = ChartUtils.generateCustomerData();
        complaintData = ChartUtils.generateComplaintData();
        branchData = ChartUtils.generateBranchData();
        
        // Calculate summary statistics
        totalOrders = 45;
        totalRevenue = revenueData.values().stream().mapToDouble(Double::doubleValue).sum();
        avgOrderValue = totalRevenue / totalOrders;
        topProduct = productData.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }
    
    private void updateQuickRangeButton() {
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);
        
        if (startDatePicker.getValue().equals(monthStart) && endDatePicker.getValue().equals(now)) {
            quickRangeBtn.setText("This Month");
        } else {
            quickRangeBtn.setText("Set This Month");
        }
    }
    
    @FXML
    void generateReport(ActionEvent event) {
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            showAlert("Please select both start and end dates.");
            return;
        }
        
        // Show loading indicator
        generateReportBtn.setText("Generating...");
        generateReportBtn.setDisable(true);
        
        // Simulate data loading (replace with actual data fetching)
        Platform.runLater(() -> {
            try {
                Thread.sleep(1000); // Simulate processing time
                
                // Reload data with new date range
                loadSampleData();
                updateChartsForReportType();
                updateSummaryStatistics();
                
                generateReportBtn.setText("Generate Report");
                generateReportBtn.setDisable(false);
                
                showAlert("Report generated successfully!");
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
    
    @FXML
    void setQuickRange(ActionEvent event) {
        LocalDate now = LocalDate.now();
        LocalDate monthStart = now.withDayOfMonth(1);
        
        startDatePicker.setValue(monthStart);
        endDatePicker.setValue(now);
        
        updateQuickRangeButton();
    }
    
    private void updateChartsForReportType() {
        RadioButton selected = (RadioButton) reportTypeGroup.getSelectedToggle();
        if (selected == null) return;
        
        String reportType = selected.getText();
        
        switch (reportType) {
            case "📦 Orders Summary":
                updateOrdersCharts();
                break;
            case "💰 Revenue Analysis":
                updateRevenueCharts();
                break;
            case "🌹 Popular Products":
                updateProductCharts();
                break;
            case "👥 Customer Analysis":
                updateCustomerCharts();
                break;
            case "⚠️ Complaints & Refunds":
                updateComplaintCharts();
                break;
        }
    }
    
    private void updateOrdersCharts() {
        // Update revenue chart with order data
        ChartUtils.updateRevenueChart(revenueChart, revenueData);
        
        // Update product chart with order distribution
        ChartUtils.updateProductChart(productChart, productData);
    }
    
    private void updateRevenueCharts() {
        // Update revenue chart
        ChartUtils.updateRevenueChart(revenueChart, revenueData);
        
        // Update product chart with revenue contribution
        Map<String, Integer> revenueContribution = new HashMap<>();
        for (Map.Entry<String, Integer> entry : productData.entrySet()) {
            revenueContribution.put(entry.getKey(), (int)(entry.getValue() * avgOrderValue));
        }
        ChartUtils.updateProductChart(productChart, revenueContribution);
    }
    
    private void updateProductCharts() {
        // Update revenue chart with product sales trend
        ChartUtils.updateRevenueChart(revenueChart, revenueData);
        
        // Update product chart
        ChartUtils.updateProductChart(productChart, productData);
    }
    
    private void updateCustomerCharts() {
        // Update revenue chart with customer segments
        ChartUtils.updateRevenueChart(revenueChart, revenueData);
        
        // Update product chart with customer data
        ChartUtils.updateProductChart(productChart, customerData);
    }
    
    private void updateComplaintCharts() {
        // Update revenue chart with complaint impact
        ChartUtils.updateRevenueChart(revenueChart, revenueData);
        
        // Update product chart with complaint data
        ChartUtils.updateProductChart(productChart, complaintData);
    }
    
    private void updateSummaryStatistics() {
        totalOrdersLbl.setText(String.valueOf(totalOrders));
        totalRevenueLbl.setText(String.format("$%.2f", totalRevenue));
        avgOrderLbl.setText(String.format("$%.2f", avgOrderValue));
        topProductLbl.setText(topProduct);
    }
    
    @FXML
    void exportToPDF(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Report");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
            );
            fileChooser.setInitialFileName("flower_store_report.txt");
            
            File file = fileChooser.showSaveDialog(App.getStage());
            if (file != null) {
                PDFExporter.exportToPDF(file, this);
                showAlert("Report exported successfully to: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error exporting report: " + e.getMessage());
        }
    }
    
    @FXML
    void exportToCSV(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV Report");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );
            fileChooser.setInitialFileName("flower_store_report.csv");
            
            File file = fileChooser.showSaveDialog(App.getStage());
            if (file != null) {
                exportCSVData(file);
                showAlert("CSV exported successfully to: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error exporting CSV: " + e.getMessage());
        }
    }
    
    private void exportCSVData(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            // Write header
            writer.write("Date,Revenue,Orders\n");
            
            // Write revenue data
            List<String> sortedDates = new ArrayList<>(revenueData.keySet());
            sortedDates.sort(Comparator.naturalOrder());
            
            for (String date : sortedDates) {
                writer.write(String.format("%s,%.2f,%d\n", date, revenueData.get(date), 
                    (int)(revenueData.get(date) / avgOrderValue)));
            }
            
            // Write summary
            writer.write("\nSummary\n");
            writer.write(String.format("Total Orders,%d\n", totalOrders));
            writer.write(String.format("Total Revenue,%.2f\n", totalRevenue));
            writer.write(String.format("Average Order Value,%.2f\n", avgOrderValue));
            writer.write(String.format("Top Product,%s\n", topProduct));
        }
    }
    
    @FXML
    void emailReport(ActionEvent event) {
        // This would integrate with the existing email service
        showAlert("Email functionality will be implemented soon!");
    }
    
    @FXML
    void refreshData(ActionEvent event) {
        loadSampleData();
        updateChartsForReportType();
        updateSummaryStatistics();
        showAlert("Data refreshed successfully!");
    }
    
    @FXML
    void goBack(ActionEvent event) {
        try {
            // Go back to employee catalog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("catalog_employee.fxml"));
            Parent root = loader.load();
            App.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error navigating back: " + e.getMessage());
        }
    }
    
    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Report Generator");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    // Getters for PDF export
    public LineChart<String, Number> getRevenueChart() {
        return revenueChart;
    }
    
    public PieChart getProductChart() {
        return productChart;
    }
    
    public Map<String, Double> getRevenueData() {
        return revenueData;
    }
    
    public Map<String, Integer> getProductData() {
        return productData;
    }
    
    public int getTotalOrders() {
        return totalOrders;
    }
    
    public double getTotalRevenue() {
        return totalRevenue;
    }
    
    public double getAvgOrderValue() {
        return avgOrderValue;
    }
    
    public String getTopProduct() {
        return topProduct;
    }
    
    public LocalDate getStartDate() {
        return startDatePicker.getValue();
    }
    
    public LocalDate getEndDate() {
        return endDatePicker.getValue();
    }
    
    public String getSelectedReportType() {
        RadioButton selected = (RadioButton) reportTypeGroup.getSelectedToggle();
        return selected != null ? selected.getText() : "Orders Summary";
    }
} 