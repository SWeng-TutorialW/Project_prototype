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
import java.util.stream.Collectors;

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
    private Label totalRefundsLbl;
    
    @FXML
    private Button exportPdfBtn;
    
    @FXML
    private Button exportCsvBtn;
    
    @FXML
    private Button refreshBtn;
    
    @FXML
    private VBox storeSelectionContainer;
    
    @FXML
    private ComboBox<String> storeComboBox;
    
    // Sample data for demonstration (will be replaced with real data)
    private Map<String, Double> revenueData = new HashMap<>();
    private Map<String, Double> refundData = new HashMap<>();
    private Map<String, Integer> productData = new HashMap<>();
    private Map<String, Integer> customerData = new HashMap<>();
    private Map<String, Integer> complaintData = new HashMap<>();
    private Map<String, Double> branchData = new HashMap<>();
    
    private int totalOrders = 0;
    private double totalRevenue = 0.0;
    private double totalRefunds = 0.0;
    private double avgOrderValue = 0.0;
    private String topProduct = "N/A";
    private int totalComplaints = 0;
    
    private ToggleGroup reportTypeGroup;

    // Track order fetching state
    private List<Order> allOrders = new ArrayList<>();
    private List<LoginRegCheck> allUsers = new ArrayList<>();
    private int pendingOrderRequests = 0;
    private boolean isProcessingData = false; // Flag to prevent duplicate processing

    @FXML
    void initialize() {
        try {
            if (EventBus.getDefault().isRegistered(this)) {
                System.out.println("ReportGeneratorController already registered");
            } else {
                EventBus.getDefault().register(this);
            }
            
            setupDatePickers();
            setupReportTypeGroup();
            setupCharts();
            setupStoreSelection();
            updateQuickRangeButton();
        } catch (Exception e) {
            System.err.println("[ReportGenerator] Error in initialize: " + e.getMessage());
            e.printStackTrace();
        }
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
    
    private void setupStoreSelection() {
        LoginRegCheck currentUser = SimpleClient.getCurrentUser();
        if (currentUser != null && currentUser.getStore() == 4) {
            // Admin can select stores
            storeSelectionContainer.setVisible(true);
            storeComboBox.getItems().addAll("All Stores", "Haifa (Store 1)", "Krayot (Store 2)", "Nahariyya (Store 3)");
            storeComboBox.setValue("All Stores");
            
            // Add listener to refresh report when store selection changes
            storeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null && !oldValue.equals(newValue) && 
                    startDatePicker.getValue() != null && endDatePicker.getValue() != null) {
                    System.out.println("[ReportGenerator] Store selection changed from " + oldValue + " to " + newValue);
                    generateReport(null);
                }
            });
        } else {
            // Employee can only see their store
            storeSelectionContainer.setVisible(false);
        }
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
        
        // Check user permissions
        LoginRegCheck currentUser = SimpleClient.getCurrentUser();
        if (currentUser == null) {
            showAlert("User session not found. Please log in again.");
            return;
        }
        
        // Only allow employees and admins to access reports
        if (!currentUser.isType()) {
            showAlert("Access denied. Only employees and administrators can view reports.");
            return;
        }
        
        System.out.println("[ReportGenerator] Generating report: requesting user list...");
        generateReportBtn.setText("Generating...");
        generateReportBtn.setDisable(true);
        
        // Reset processing state
        isProcessingData = false;
        allOrders.clear();
        allUsers.clear();
        pendingOrderRequests = 0;
        
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
        totalRevenueLbl.setText(String.format("$%.2f", totalRevenue - totalRefunds));
        avgOrderLbl.setText(String.format("$%.2f", avgOrderValue));
        topProductLbl.setText(topProduct);
        totalRefundsLbl.setText(String.format("$%.2f", totalRefunds));
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
            writer.write("Date,Revenue,Refunds,Net Revenue,Orders\n");
            
            // Write revenue data
            List<String> sortedDates = new ArrayList<>(revenueData.keySet());
            sortedDates.sort(Comparator.naturalOrder());
            
            for (String date : sortedDates) {
                double revenue = revenueData.get(date);
                double refunds = refundData.getOrDefault(date, 0.0);
                double netRevenue = revenue - refunds;
                writer.write(String.format("%s,%.2f,%.2f,%.2f,%d\n", date, revenue, refunds, netRevenue, 
                    (int)(revenue / (avgOrderValue + (totalRefunds / totalOrders)))));
            }
            
            // Write summary
            writer.write("\nSummary\n");
            writer.write(String.format("Total Orders,%d\n", totalOrders));
            writer.write(String.format("Total Revenue,%.2f\n", totalRevenue));
            writer.write(String.format("Total Refunds,%.2f\n", totalRefunds));
            writer.write(String.format("Net Revenue,%.2f\n", totalRevenue - totalRefunds));
            writer.write(String.format("Average Order Value,%.2f\n", avgOrderValue));
            writer.write(String.format("Top Product,%s\n", topProduct));
            writer.write(String.format("Total Complaints,%d\n", totalComplaints));
            
            // Write complaints data
            writer.write("\nComplaints by Category\n");
            writer.write("Category,Count\n");
            for (Map.Entry<String, Integer> entry : complaintData.entrySet()) {
                writer.write(String.format("%s,%d\n", entry.getKey(), entry.getValue()));
            }
        }
    }
    
    @FXML
    void refreshData(ActionEvent event) {
        generateReport(null); // Just re-fetch real data
        showAlert("Data refreshed successfully!");
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
    
    // New getter methods for PDF export
    public String getReportScope() {
        LoginRegCheck currentUser = SimpleClient.getCurrentUser();
        if (currentUser == null) return "Unknown";
        
        if (currentUser.getStore() == 4) {
            // Admin - show selected store
            String selectedStoreText = storeComboBox.getValue();
            if (selectedStoreText != null && !selectedStoreText.equals("All Stores")) {
                return selectedStoreText;
            } else {
                return "Network-wide (All Stores)";
            }
        } else {
            return "Store " + currentUser.getStore() + " (" + currentUser.getStoreName() + ")";
        }
    }
    
    public double getTotalRefunds() {
        return totalRefunds;
    }
    
    public double getNetRevenue() {
        return totalRevenue - totalRefunds;
    }
    
    public int getTotalComplaints() {
        return totalComplaints;
    }
    
    public Map<String, Double> getRefundData() {
        return refundData;
    }
    
    public Map<String, Integer> getComplaintData() {
        return complaintData;
    }

    // Add EventBus handlers for real data
    @Subscribe
    public void handleReportUsersList(List<LoginRegCheck> users) {
        try {
            // Prevent duplicate processing
            if (isProcessingData) {
                System.out.println("[ReportGenerator] Already processing data, ignoring duplicate user list");
                return;
            }
            
            // Check if the list actually contains LoginRegCheck objects
            if (users == null || users.isEmpty()) {
                System.out.println("[ReportGenerator] Received empty or null user list");
                return;
            }
            
            // Verify the first element is actually a LoginRegCheck
            if (!(users.get(0) instanceof LoginRegCheck)) {
                System.out.println("[ReportGenerator] Received list with wrong type: " + users.get(0).getClass().getName());
                return;
            }
            
            System.out.println("[ReportGenerator] Received user list of size: " + users.size());
            isProcessingData = true;
            allUsers.clear();
            allUsers.addAll(users);
            allOrders.clear();
            
            // Only fetch orders for customers (not employees)
            List<LoginRegCheck> customers = users.stream()
                .filter(user -> !user.isType()) // Only customers (type = false)
                .collect(Collectors.toList());
            
            pendingOrderRequests = customers.size();
            
            // Debug: Print user information
            for (LoginRegCheck user : users) {
                System.out.println("[ReportGenerator] User: " + user.getUsername() + ", Store: " + user.getStore() + 
                                 ", Type: " + user.isType() + " (" + (user.isType() ? "Employee" : "Customer") + ")");
            }
            
            System.out.println("[ReportGenerator] Found " + customers.size() + " customers out of " + users.size() + " total users");
            
            if (pendingOrderRequests == 0) {
                System.out.println("[ReportGenerator] No customers found, skipping order fetch.");
                processOrders(allOrders);
                return;
            }
            
            for (LoginRegCheck customer : customers) {
                System.out.println("[ReportGenerator] Requesting orders for customer: " + customer.getUsername());
                try {
                    SimpleClient.getClient().sendToServer("getOrdersForUser_" + customer.getUsername());
                } catch (IOException e) {
                    e.printStackTrace();
                    pendingOrderRequests--;
                }
            }
        } catch (Exception e) {
            System.err.println("[ReportGenerator] Error in handleReportUsersList: " + e.getMessage());
            e.printStackTrace();
            isProcessingData = false;
        }
    }

    @Subscribe
    public void handleReportOrdersResponse(List<Order> userOrders) {
        try {
            // Check if the list actually contains Order objects
            if (userOrders == null || userOrders.isEmpty()) {
                System.out.println("[ReportGenerator] Received empty order list");
                pendingOrderRequests--;
                if (pendingOrderRequests == 0) {
                    System.out.println("[ReportGenerator] All user orders received. Total orders: " + allOrders.size());
                    processOrders(allOrders);
                }
                return;
            }
            
            // Verify the first element is actually an Order
            if (!(userOrders.get(0) instanceof Order)) {
                System.out.println("[ReportGenerator] Received list with wrong type: " + userOrders.get(0).getClass().getName());
                pendingOrderRequests--;
                if (pendingOrderRequests == 0) {
                    System.out.println("[ReportGenerator] All user orders received. Total orders: " + allOrders.size());
                    processOrders(allOrders);
                }
                return;
            }
            
            System.out.println("[ReportGenerator] Received " + userOrders.size() + " orders for a user.");
            allOrders.addAll(userOrders);
            pendingOrderRequests--;
            if (pendingOrderRequests == 0) {
                System.out.println("[ReportGenerator] All user orders received. Total orders: " + allOrders.size());
                processOrders(allOrders);
            }
        } catch (Exception e) {
            System.err.println("[ReportGenerator] Error in handleReportOrdersResponse: " + e.getMessage());
            e.printStackTrace();
            pendingOrderRequests--;
            if (pendingOrderRequests == 0) {
                processOrders(allOrders);
            }
        }
    }

    // Fallback handler for empty order lists
    @Subscribe
    public void handleReportEmptyOrderResponse(Object response) {
        try {
            if (response instanceof String) {
                String responseStr = (String) response;
                if (responseStr.equals("user_not_found") || responseStr.equals("error_retrieving_orders")) {
                    System.out.println("[ReportGenerator] Received error response for user orders: " + responseStr);
                    pendingOrderRequests--;
                    if (pendingOrderRequests == 0) {
                        System.out.println("[ReportGenerator] All user orders processed. Total orders: " + allOrders.size());
                        processOrders(allOrders);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[ReportGenerator] Error in handleReportEmptyOrderResponse: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Subscribe
    public void handleReportComplaintsResponse(List<Complain> complaints) {
        try {
            // Check if the list actually contains Complain objects
            if (complaints == null || complaints.isEmpty()) {
                System.out.println("[ReportGenerator] Received empty complaints list");
                return;
            }
            
            // Verify the first element is actually a Complain
            if (!(complaints.get(0) instanceof Complain)) {
                System.out.println("[ReportGenerator] Received complaints list with wrong type: " + complaints.get(0).getClass().getName());
                return;
            }
            
            System.out.println("[ReportGenerator] Received List<Complain> with size: " + complaints.size());
            processComplaints(complaints);
        } catch (Exception e) {
            System.err.println("[ReportGenerator] Error in handleReportComplaintsResponse: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Subscribe
    public void handleReportComplainUpdateEvent(ComplainUpdateEvent event) {
        try {
            System.out.println("[ReportGenerator] Received ComplainUpdateEvent with size: " + (event.getUpdatedItems() != null ? event.getUpdatedItems().size() : 0));
            processComplaints(event.getUpdatedItems());
        } catch (Exception e) {
            System.err.println("[ReportGenerator] Error in handleReportComplainUpdateEvent: " + e.getMessage());
            e.printStackTrace();
        }
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

    private void processOrders(List<Order> orders) {
        try {
            System.out.println("[ReportGenerator] Processing " + orders.size() + " orders");
            
            // Get current user for role-based filtering
            LoginRegCheck currentUser = SimpleClient.getCurrentUser();
            if (currentUser == null) {
                System.out.println("[ReportGenerator] No current user found");
                isProcessingData = false;
                return;
            }
            
            // Filter orders based on user role and store selection
            List<Order> filteredOrders = new ArrayList<>();
            int selectedStore = -1;
            
            if (currentUser.getStore() == 4) {
                // Admin - check store selection
                String selectedStoreText = storeComboBox.getValue();
                if (selectedStoreText != null) {
                    if (selectedStoreText.contains("Haifa")) selectedStore = 1;
                    else if (selectedStoreText.contains("Krayot")) selectedStore = 2;
                    else if (selectedStoreText.contains("Nahariyya")) selectedStore = 3;
                    // If "All Stores" is selected, selectedStore remains -1
                }
            } else {
                // Employee - only their store
                selectedStore = currentUser.getStore();
            }
            
            for (Order order : orders) {
                boolean includeOrder = false;
                
                if (selectedStore == -1) {
                    // Admin viewing all stores
                    includeOrder = true;
                } else {
                    // Filter by store - check if the order's user belongs to the selected store
                    if (order.getUser() != null) {
                        includeOrder = (order.getUser().getStore() == selectedStore);
                        System.out.println("[ReportGenerator] Order " + order.getId() + " user store: " + 
                                         order.getUser().getStore() + ", selected store: " + selectedStore + 
                                         ", include: " + includeOrder);
                    } else {
                        // If order has no user, include it for now (fallback)
                        includeOrder = true;
                        System.out.println("[ReportGenerator] Order " + order.getId() + " has no user, including by default");
                    }
                }
                
                if (includeOrder) {
                    filteredOrders.add(order);
                }
            }
            
            System.out.println("[ReportGenerator] Filtered to " + filteredOrders.size() + " orders for store " + 
                             (selectedStore == -1 ? "All Stores" : selectedStore));
            
            // If there are orders, set the default date range to the earliest and latest order dates
            if (!filteredOrders.isEmpty()) {
                LocalDate earliest = filteredOrders.stream()
                    .map(order -> order.getOrderDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())
                    .min(LocalDate::compareTo).orElse(startDatePicker.getValue());
                LocalDate latest = filteredOrders.stream()
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
            refundData.clear();
            productData.clear();
            customerData.clear();
            Set<String> customers = new HashSet<>();
            totalOrders = 0;
            totalRevenue = 0.0;
            totalRefunds = 0.0;
            
            for (Order order : filteredOrders) {
                LocalDate orderDate = order.getOrderDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                System.out.println("[ReportGenerator] Processing order " + order.getId() + " with date " + orderDate + 
                                 " (start: " + start + ", end: " + end + ")");
                
                if ((orderDate.isEqual(start) || orderDate.isAfter(start)) && (orderDate.isEqual(end) || orderDate.isBefore(end))) {
                    String dateStr = orderDate.toString();
                    double orderAmount = order.getTotalAmount();
                    double refundAmount = order.getRefundAmount();
                    
                    revenueData.put(dateStr, revenueData.getOrDefault(dateStr, 0.0) + orderAmount);
                    refundData.put(dateStr, refundData.getOrDefault(dateStr, 0.0) + refundAmount);
                    
                    totalRevenue += orderAmount;
                    totalRefunds += refundAmount;
                    totalOrders++;
                    
                    if (order.getItems() != null) {
                        for (CartItem item : order.getItems()) {
                            if (item.getFlower() != null) {
                                String productName = item.getFlower().getFlowerName();
                                productData.put(productName, productData.getOrDefault(productName, 0) + item.getQuantity());
                            }
                        }
                    }
                    
                    if (order.getCustomerEmail() != null) {
                        customers.add(order.getCustomerEmail());
                    }
                }
            }
            
            avgOrderValue = totalOrders > 0 ? (totalRevenue - totalRefunds) / totalOrders : 0.0;
            topProduct = productData.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");
            customerData.put("Unique Customers", customers.size());
            
            System.out.println("[ReportGenerator] Processed data - Orders: " + totalOrders + 
                             ", Revenue: " + totalRevenue + ", Refunds: " + totalRefunds + 
                             ", Products: " + productData.size() + ", Customers: " + customers.size());
            
            Platform.runLater(() -> {
                try {
                    updateChartsForReportType();
                    updateSummaryStatistics();
                    generateReportBtn.setText("Generate Report");
                    generateReportBtn.setDisable(false);
                    isProcessingData = false; // Reset processing flag
                } catch (Exception e) {
                    System.err.println("[ReportGenerator] Error updating UI: " + e.getMessage());
                    e.printStackTrace();
                    generateReportBtn.setText("Generate Report");
                    generateReportBtn.setDisable(false);
                    isProcessingData = false;
                }
            });
        } catch (Exception e) {
            System.err.println("[ReportGenerator] Error in processOrders: " + e.getMessage());
            e.printStackTrace();
            Platform.runLater(() -> {
                generateReportBtn.setText("Generate Report");
                generateReportBtn.setDisable(false);
                isProcessingData = false;
            });
        }
    }

    private void processComplaints(List<Complain> complaints) {
        try {
            if (complaints == null) {
                System.out.println("[ReportGenerator] No complaints to process");
                return;
            }
            
            // Get current user for role-based filtering
            LoginRegCheck currentUser = SimpleClient.getCurrentUser();
            if (currentUser == null) {
                System.out.println("[ReportGenerator] No current user found for complaints processing");
                return;
            }
            
            // Filter complaints based on user role and store selection
            List<Complain> filteredComplaints = new ArrayList<>();
            int selectedStore = -1;
            
            if (currentUser.getStore() == 4) {
                // Admin - check store selection
                String selectedStoreText = storeComboBox.getValue();
                if (selectedStoreText != null) {
                    if (selectedStoreText.contains("Haifa")) selectedStore = 1;
                    else if (selectedStoreText.contains("Krayot")) selectedStore = 2;
                    else if (selectedStoreText.contains("Nahariyya")) selectedStore = 3;
                    // If "All Stores" is selected, selectedStore remains -1
                }
            } else {
                // Employee - only their store
                selectedStore = currentUser.getStore();
            }
            
            for (Complain complaint : complaints) {
                boolean includeComplaint = false;
                
                if (selectedStore == -1) {
                    // Admin viewing all stores
                    includeComplaint = true;
                } else {
                    // Filter by store - check if the complaint's client belongs to the selected store
                    // For now, include all complaints since store information might not be in complaints
                    // This can be enhanced later when store information is added to complaints
                    includeComplaint = true;
                }
                
                if (includeComplaint) {
                    filteredComplaints.add(complaint);
                }
            }
            
            complaintData.clear();
            totalComplaints = filteredComplaints.size();
            
            for (Complain c : filteredComplaints) {
                String type = categorizeComplaint(c.getComplaint());
                complaintData.put(type, complaintData.getOrDefault(type, 0) + 1);
            }
            
            System.out.println("[ReportGenerator] Processed " + filteredComplaints.size() + " complaints into " + complaintData.size() + " categories");
            
            Platform.runLater(() -> {
                try {
                    updateChartsForReportType();
                    updateSummaryStatistics();
                } catch (Exception e) {
                    System.err.println("[ReportGenerator] Error updating UI for complaints: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.err.println("[ReportGenerator] Error in processComplaints: " + e.getMessage());
            e.printStackTrace();
        }
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
    
    // Method to properly clean up EventBus registration
    public void cleanup() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
            System.out.println("[ReportGenerator] Unregistered from EventBus");
        }
    }
} 