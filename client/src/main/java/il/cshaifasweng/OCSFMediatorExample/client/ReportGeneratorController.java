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

    // Track order fetching state
    private List<Order> allOrders = new ArrayList<>();
    private List<LoginRegCheck> allUsers = new ArrayList<>();
    private int pendingOrderRequests = 0;

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
        System.out.println("[ReportGenerator] Generating report: requesting user list...");
        generateReportBtn.setText("Generating...");
        generateReportBtn.setDisable(true);
        // Fetch all users first
        try {
            SimpleClient.getClient().sendToServer("asks_for_users");
            SimpleClient.getClient().sendToServer("getComplaints");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error fetching data: " + e.getMessage());
            generateReportBtn.setText("Generate Report");
            generateReportBtn.setDisable(false);
        }
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
        generateReport(null); // Just re-fetch real data
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

    // Single EventBus handler for all data types to prevent conflicts
    @Subscribe
    public void handleAllEvents(Object event) {
        try {
            if (event instanceof List<?>) {
                List<?> list = (List<?>) event;
                if (list.isEmpty()) {
                    System.out.println("[ReportGenerator] Received empty list");
                    return;
                }
                
                Object firstElement = list.get(0);
                
                if (firstElement instanceof LoginRegCheck) {
                    @SuppressWarnings("unchecked")
                    List<LoginRegCheck> users = (List<LoginRegCheck>) list;
                    handleUsersList(users);
                } else if (firstElement instanceof Order) {
                    @SuppressWarnings("unchecked")
                    List<Order> orders = (List<Order>) list;
                    handleOrdersList(orders);
                } else if (firstElement instanceof Complain) {
                    @SuppressWarnings("unchecked")
                    List<Complain> complaints = (List<Complain>) list;
                    handleComplaintsList(complaints);
                } else {
                    System.out.println("[ReportGenerator] Received unknown list type: " + firstElement.getClass().getName());
                }
            } else if (event instanceof ComplainUpdateEvent) {
                ComplainUpdateEvent complainEvent = (ComplainUpdateEvent) event;
                System.out.println("[ReportGenerator] Received ComplainUpdateEvent with size: " + 
                                 (complainEvent.getUpdatedItems() != null ? complainEvent.getUpdatedItems().size() : 0));
                handleComplaintsList(complainEvent.getUpdatedItems());
            } else {
                System.out.println("[ReportGenerator] Received unknown event: " + event.getClass().getName());
            }
        } catch (Exception e) {
            System.err.println("[ReportGenerator] Error in handleAllEvents: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Internal methods for processing different data types
    private void handleUsersList(List<LoginRegCheck> users) {
        System.out.println("[ReportGenerator] Processing user list of size: " + users.size());
        allUsers.clear();
        allUsers.addAll(users);
        allOrders.clear();
        pendingOrderRequests = users.size();
        
        if (pendingOrderRequests == 0) {
            System.out.println("[ReportGenerator] No users found, skipping order fetch.");
            processOrders(allOrders);
            return;
        }
        
        for (LoginRegCheck user : users) {
            System.out.println("[ReportGenerator] Requesting orders for user: " + user.getUsername());
            try {
                SimpleClient.getClient().sendToServer("getOrdersForUser_" + user.getUsername());
            } catch (IOException e) {
                e.printStackTrace();
                pendingOrderRequests--;
            }
        }
    }

    private void handleOrdersList(List<Order> orders) {
        System.out.println("[ReportGenerator] Processing orders list of size: " + orders.size());
        allOrders.addAll(orders);
        pendingOrderRequests--;
        
        if (pendingOrderRequests == 0) {
            System.out.println("[ReportGenerator] All user orders received. Total orders: " + allOrders.size());
            processOrders(allOrders);
        }
    }

    private void handleComplaintsList(List<Complain> complaints) {
        System.out.println("[ReportGenerator] Processing complaints list of size: " + (complaints != null ? complaints.size() : 0));
        processComplaints(complaints);
    }

    // Remove all other EventBus handlers to prevent conflicts
    // @Subscribe methods removed to prevent multiple handlers for the same data

    private void processOrders(List<Order> orders) {
        // If there are orders, set the default date range to the earliest and latest order dates
        if (!orders.isEmpty()) {
            LocalDate earliest = orders.stream()
                .map(order -> order.getOrderDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())
                .min(LocalDate::compareTo).orElse(startDatePicker.getValue());
            LocalDate latest = orders.stream()
                .map(order -> order.getOrderDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())
                .max(LocalDate::compareTo).orElse(endDatePicker.getValue());
            if (startDatePicker.getValue() == null || endDatePicker.getValue() == null ||
                startDatePicker.getValue().isAfter(latest) || endDatePicker.getValue().isBefore(earliest)) {
                startDatePicker.setValue(earliest);
                endDatePicker.setValue(latest);
            }
        }
        // Filter by date range
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        revenueData.clear();
        productData.clear();
        customerData.clear();
        Set<String> customers = new HashSet<>();
        totalOrders = 0;
        totalRevenue = 0.0;
        for (Order order : orders) {
            LocalDate orderDate = order.getOrderDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            if ((orderDate.isEqual(start) || orderDate.isAfter(start)) && (orderDate.isEqual(end) || orderDate.isBefore(end))) {
                String dateStr = orderDate.toString();
                revenueData.put(dateStr, revenueData.getOrDefault(dateStr, 0.0) + order.getTotalAmount());
                totalRevenue += order.getTotalAmount();
                totalOrders++;
                for (CartItem item : order.getItems()) {
                    String productName = item.getFlower().getFlowerName();
                    productData.put(productName, productData.getOrDefault(productName, 0) + item.getQuantity());
                }
                customers.add(order.getCustomerEmail());
            }
        }
        avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0.0;
        topProduct = productData.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");
        customerData.put("Unique Customers", customers.size());
        Platform.runLater(() -> {
            updateChartsForReportType();
            updateSummaryStatistics();
            generateReportBtn.setText("Generate Report");
            generateReportBtn.setDisable(false);
        });
    }

    private void processComplaints(List<Complain> complaints) {
        if (complaints == null) {
            System.out.println("[ReportGenerator] No complaints to process");
            return;
        }
        
        complaintData.clear();
        for (Complain c : complaints) {
            if (c != null && c.getComplaint() != null) {
                String type = categorizeComplaint(c.getComplaint());
                complaintData.put(type, complaintData.getOrDefault(type, 0) + 1);
            }
        }
        Platform.runLater(() -> {
            updateChartsForReportType();
            updateSummaryStatistics();
        });
    }

    private String categorizeComplaint(String complaintText) {
        if (complaintText == null) return "Other";
        String lower = complaintText.toLowerCase();
        if (lower.contains("delivery")) return "Delivery Issues";
        if (lower.contains("refund")) return "Refunds";
        if (lower.contains("quality")) return "Quality Issues";
        if (lower.contains("wrong")) return "Wrong Items";
        if (lower.contains("service")) return "Customer Service";
        return "Other";
    }

    // Listen for order updates from server
    @Subscribe
    public void handleOrderUpdate(String message) {
        if ("update_catalog_after_change".equals(message)) {
            System.out.println("[ReportGenerator] Received order update notification, refreshing data...");
            // Automatically refresh data when new orders are created
            Platform.runLater(() -> {
                if (startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
                    generateReport(null);
                }
            });
        }
    }
} 