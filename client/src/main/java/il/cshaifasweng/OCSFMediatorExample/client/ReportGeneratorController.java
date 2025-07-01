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
    private RadioButton customersReport;



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
    private Button emailReportBtn;

    @FXML
    private Button refreshBtn;

    @FXML
    private Button backBtn;

    // Store selection for network admin
    @FXML
    private ComboBox<String> storeComboBox;

    @FXML
    private VBox storeSelectionContainer;

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
    private List<Complain> allComplaints = new ArrayList<>();
    private int pendingOrderRequests = 0;

    // Add flag to prevent duplicate processing
    private boolean isProcessingUsers = false;

    // Add current user field to prevent session loss
    private LoginRegCheck currentUser;

    // Store filtering
    private int selectedStoreId = -1; // -1 means all stores, 1-3 for specific stores
    private boolean isNetworkAdmin = false;
    
    // Track refunds from canceled orders
    private double totalRefundsFromOrders = 0.0;

    @FXML
    void initialize() {
        // Ensure we're only registered once
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
            System.out.println("[ReportGenerator] Registered to EventBus");
        } else {
            System.out.println("[ReportGenerator] Already registered to EventBus - skipping registration");
        }

        setupDatePickers();
        setupReportTypeGroup();
        setupCharts();
        setupStoreSelection();
        updateQuickRangeButton();
    }

    private void setupStoreSelection() {
        // Initialize store combo box with store options
        ObservableList<String> storeOptions = FXCollections.observableArrayList(
            "All Stores", "Haifa", "Krayot", "Nahariyya"
        );
        storeComboBox.setItems(storeOptions);
        storeComboBox.setValue("All Stores");

        // Add listener for store selection changes
        storeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateSelectedStoreId(newValue);
                // If we have data, regenerate the report with new store filter
                if (!allOrders.isEmpty()) {
                    processOrders(allOrders);
                }
            }
        });
    }

    private void updateSelectedStoreId(String storeName) {
        switch (storeName) {
            case "All Stores":
                selectedStoreId = -1;
                break;
            case "Haifa":
                selectedStoreId = 1;
                break;
            case "Krayot":
                selectedStoreId = 2;
                break;
            case "Nahariyya":
                selectedStoreId = 3;
                break;
            default:
                selectedStoreId = -1;
                break;
        }
        System.out.println("[ReportGenerator] Selected store ID: " + selectedStoreId);
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
        customersReport.setToggleGroup(reportTypeGroup);

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

        // Check user permissions using local currentUser field
        if (currentUser == null) {
            showAlert("User session not found. Please log in again.");
            return;
        }

        // Only allow employees and admins to access reports
        if (!currentUser.isType()) {
            showAlert("Access denied. Only employees and administrators can view reports.");
            return;
        }

        // Reset state for new report generation
        resetReportState();

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

    private void resetReportState() {
        // Clear all data and reset counters
        allOrders.clear();
        allUsers.clear();
        allComplaints.clear();
        pendingOrderRequests = 0;
        isProcessingUsers = false; // Reset processing flag
        revenueData.clear();
        productData.clear();
        customerData.clear();
        complaintData.clear();
        totalOrders = 0;
        totalRevenue = 0.0;
        avgOrderValue = 0.0;
        topProduct = "N/A";
        totalRefundsFromOrders = 0.0; // Reset refunds from orders

        System.out.println("[ReportGenerator] Reset report state for new generation");
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
            case "👥 Customer Analysis":
                updateCustomerCharts();
                break;
        }
    }

    private void updateOrdersCharts() {
        // Update revenue chart with order data
        ChartUtils.updateRevenueChart(revenueChart, revenueData);

        // Update product chart with order distribution
        ChartUtils.updateProductChart(productChart, productData);
    }

    private void updateProductCharts() {
        // Update revenue chart with product sales trend
        ChartUtils.updateRevenueChart(revenueChart, revenueData);

        // Update product chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : productData.entrySet()) {
            String name = entry.getKey();
            int count = entry.getValue();
            if (name == null || name.isEmpty() || name.equals("0") || name.equals("null")) {
                name = "Custom Item";
            }
            pieChartData.add(new PieChart.Data(name, count));
        }
        productChart.setData(pieChartData);
    }

    private void updateCustomerCharts() {
        // Update revenue chart with customer segments
        ChartUtils.updateRevenueChart(revenueChart, revenueData);

        // Update product chart with customer data - show customer names and their order counts (filtered by store)
        Map<String, Integer> customerOrderCounts = new HashMap<>();
        for (Order order : allOrders) {
            LocalDate orderDate = order.getOrderDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            LocalDate start = startDatePicker.getValue();
            LocalDate end = endDatePicker.getValue();

            // Check date range and store filter
            boolean inDateRange = (orderDate.isEqual(start) || orderDate.isAfter(start)) && 
                                 (orderDate.isEqual(end) || orderDate.isBefore(end));
            boolean inStoreRange = selectedStoreId == -1 || order.getStoreId() == selectedStoreId;

            if (inDateRange && inStoreRange) {
                String customerName = order.getCustomerName();
                customerOrderCounts.put(customerName, customerOrderCounts.getOrDefault(customerName, 0) + 1);
            }
        }

        if (customerOrderCounts.isEmpty()) {
            // If no customers, show a message
            productChart.getData().clear();
            PieChart.Data noData = new PieChart.Data("No Customers", 1);
            productChart.getData().add(noData);
        } else {
            ChartUtils.updateProductChart(productChart, customerOrderCounts);
        }
    }



    private void updateSummaryStatistics() {
        totalOrdersLbl.setText(String.valueOf(totalOrders));
        totalRevenueLbl.setText(String.format("₪%.2f", totalRevenue));
        avgOrderLbl.setText(String.format("₪%.2f", avgOrderValue));
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
            // Write header with store information
            String storeInfo = getStoreInfo();
            writer.write("Store Report: " + storeInfo + "\n");
            writer.write("Date Range: " + startDatePicker.getValue() + " to " + endDatePicker.getValue() + "\n\n");
            
            // Write revenue data
            writer.write("Revenue Data\n");
            writer.write("Date,Revenue,Orders\n");

            List<String> sortedDates = new ArrayList<>(revenueData.keySet());
            sortedDates.sort(Comparator.naturalOrder());

            for (String date : sortedDates) {
                writer.write(String.format("%s,%.2f,%d\n", date, revenueData.get(date),
                        (int)(revenueData.get(date) / avgOrderValue)));
            }

            // Write summary
            writer.write("\nSummary Statistics\n");
            writer.write(String.format("Total Orders,%d\n", totalOrders));
            writer.write(String.format("Total Revenue,%.2f\n", totalRevenue));
            writer.write(String.format("Average Order Value,%.2f\n", avgOrderValue));
            writer.write(String.format("Top Product,%s\n", topProduct));
            writer.write(String.format("Unique Customers,%d\n", customerData.getOrDefault("Unique Customers", 0)));

            // Write complaint data
            writer.write("\nComplaint Data\n");
            writer.write("Complaint Type,Count,Total Refund Amount\n");
            
            double totalComplaintRefunds = 0.0;
            for (Map.Entry<String, Integer> entry : complaintData.entrySet()) {
                String complaintType = entry.getKey();
                int count = entry.getValue();
                double refundAmount = getRefundAmountForComplaintType(complaintType);
                totalComplaintRefunds += refundAmount;
                writer.write(String.format("%s,%d,%.2f\n", complaintType, count, refundAmount));
            }
            
            writer.write(String.format("Total Complaints,%d,%.2f\n", 
                complaintData.values().stream().mapToInt(Integer::intValue).sum(), totalComplaintRefunds));

            // Write detailed complaint information (filtered by store)
            writer.write("\nDetailed Complaint Information\n");
            writer.write("Client,Complaint,Refund Amount\n");
            
            for (Complain complaint : allComplaints) {
                if (complaint != null) {
                    // Filter by store ID
                    boolean inStoreRange = selectedStoreId == -1 || complaint.getStoreId() == selectedStoreId;
                    if (inStoreRange) {
                        String clientName = complaint.getClient() != null ? complaint.getClient() : "Unknown";
                        String complaintText = complaint.getComplaint() != null ? complaint.getComplaint() : "No complaint text";
                        double refundAmount = complaint.getRefundAmount();
                        writer.write(String.format("%s,%s,%.2f\n", clientName, complaintText, refundAmount));
                    }
                }
            }
        }
    }

    public String getStoreInfo() {
        if (selectedStoreId == -1) {
            return "All Stores (Network Report)";
        } else {
            switch (selectedStoreId) {
                case 1: return "Haifa Store";
                case 2: return "Krayot Store";
                case 3: return "Nahariyya Store";
                default: return "Unknown Store";
            }
        }
    }

    private double getRefundAmountForComplaintType(String complaintType) {
        return allComplaints.stream()
                .filter(c -> c != null && c.getComplaint() != null && categorizeComplaint(c.getComplaint()).equals(complaintType))
                .filter(c -> selectedStoreId == -1 || c.getStoreId() == selectedStoreId) // Filter by store
                .mapToDouble(Complain::getRefundAmount)
                .sum();
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

    public Map<String, Integer> getComplaintData() {
        return complaintData;
    }

    public List<Complain> getAllComplaints() {
        return allComplaints;
    }

    public int getSelectedStoreId() {
        return selectedStoreId;
    }

    public double getTotalRefunds() {
        // Calculate total refunds from complaints (filtered by store)
        double complaintRefunds = allComplaints.stream()
                .filter(c -> c != null && c.getRefundAmount() > 0)
                .filter(c -> selectedStoreId == -1 || c.getStoreId() == selectedStoreId) // Filter by store
                .mapToDouble(Complain::getRefundAmount)
                .sum();
        
        // Calculate total refunds from canceled orders (filtered by store)
        double orderRefunds = allOrders.stream()
                .filter(o -> o != null && "CANCELLED".equals(o.getStatus()) && o.getRefundAmount() > 0)
                .filter(o -> selectedStoreId == -1 || o.getStoreId() == selectedStoreId) // Filter by store
                .mapToDouble(Order::getRefundAmount)
                .sum();
        
        // Return sum of both complaint and order refunds
        return complaintRefunds + orderRefunds;
    }

    // Single EventBus handler for all data types to prevent conflicts
    @Subscribe
    public void handleAllEvents(Object event) {
        try {
            if (event instanceof List<?>) {
                List<?> list = (List<?>) event;
                if (list.isEmpty()) {
                    System.out.println("[ReportGenerator] Received empty list - this might be orders for a user with no orders");
                    // Decrement pending requests for empty order lists
                    pendingOrderRequests--;
                    if (pendingOrderRequests == 0) {
                        System.out.println("[ReportGenerator] All user orders received. Total orders: " + allOrders.size());
                        processOrders(allOrders);
                    }
                    return;
                }

                Object firstElement = list.get(0);

                if (firstElement instanceof LoginRegCheck) {
                    @SuppressWarnings("unchecked")
                    List<LoginRegCheck> users = (List<LoginRegCheck>) list;
                    // Only process user list if we're not already processing and if the button is disabled (indicating we're generating a report)
                    if (!isProcessingUsers && generateReportBtn.isDisabled()) {
                        handleUsersList(users);
                    } else {
                        System.out.println("[ReportGenerator] Ignoring user list - already processing or not generating report");
                    }
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
        if (isProcessingUsers) {
            System.out.println("[ReportGenerator] Already processing users, ignoring duplicate user list");
            return;
        }

        System.out.println("[ReportGenerator] Processing user list of size: " + users.size());
        isProcessingUsers = true;
        allUsers.clear();
        allUsers.addAll(users);
        allOrders.clear();
        pendingOrderRequests = users.size();

        // Debug: Print user types to understand the mix
        System.out.println("[ReportGenerator] User types in system:");
        for (LoginRegCheck user : users) {
            System.out.println("  - " + user.getUsername() + " (type: " + user.isType() + ", store: " + user.getStore() + ")");
        }

        if (pendingOrderRequests == 0) {
            System.out.println("[ReportGenerator] No users found, skipping order fetch.");
            isProcessingUsers = false;
            processOrders(allOrders);
            return;
        }

        for (LoginRegCheck user : users) {
            System.out.println("[ReportGenerator] Requesting orders for user: " + user.getUsername() + " (type: " + user.isType() + ")");
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

        // Add some debugging for empty orders
        if (orders.isEmpty()) {
            System.out.println("[ReportGenerator] Received empty orders list - this is normal for users with no orders");
            pendingOrderRequests--;

            if (pendingOrderRequests == 0) {
                System.out.println("[ReportGenerator] All user orders received. Total orders: " + allOrders.size());
                processOrders(allOrders);
            }
            return;
        }

        // Debug: Print some order details
        for (Order order : orders) {
            System.out.println("[ReportGenerator] Order ID: " + order.getId() + ", Customer: " + order.getCustomerName() + ", Amount: " + order.getTotalAmount());
        }

        allOrders.addAll(orders);
        pendingOrderRequests--;

        System.out.println("[ReportGenerator] Pending order requests remaining: " + pendingOrderRequests);

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
        System.out.println("[ReportGenerator] Processing " + orders.size() + " total orders");

        // If there are no orders, show a message and set default date range
        if (orders.isEmpty()) {
            System.out.println("[ReportGenerator] No orders found in the system");
            // Set default date range to current month if no orders exist
            LocalDate now = LocalDate.now();
            if (startDatePicker.getValue() == null) {
                startDatePicker.setValue(now.withDayOfMonth(1));
            }
            if (endDatePicker.getValue() == null) {
                endDatePicker.setValue(now);
            }

            // Clear all data and show empty state
            revenueData.clear();
            productData.clear();
            customerData.clear();
            complaintData.clear();
            totalOrders = 0;
            totalRevenue = 0.0;
            avgOrderValue = 0.0;
            topProduct = "N/A";

            Platform.runLater(() -> {
                updateChartsForReportType();
                updateSummaryStatistics();
                generateReportBtn.setText("Generate Report");
                generateReportBtn.setDisable(false);
                showAlert("No orders found in the system for the selected date range.\n\nThis could mean:\n• No orders have been placed yet\n• All orders are outside the selected date range\n• The database is empty\n\nTry selecting a different date range or check if orders exist in the system.");
                isProcessingUsers = false; // Reset flag when done
            });
            return;
        }

        // Debug: Print all orders with their dates
        System.out.println("[ReportGenerator] All orders in system:");
        for (Order order : orders) {
            LocalDate orderDate = order.getOrderDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            System.out.println("  - Order ID: " + order.getId() + ", Customer: " + order.getCustomerName() +
                    ", Date: " + orderDate + ", Amount: " + order.getTotalAmount() + ", Store ID: " + order.getStoreId());
        }

        // If there are orders, set the default date range to the earliest and latest order dates
        LocalDate earliest = orders.stream()
                .map(order -> order.getOrderDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())
                .min(LocalDate::compareTo).orElse(startDatePicker.getValue());
        LocalDate latest = orders.stream()
                .map(order -> order.getOrderDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())
                .max(LocalDate::compareTo).orElse(endDatePicker.getValue());

        System.out.println("[ReportGenerator] Order date range: " + earliest + " to " + latest);
        System.out.println("[ReportGenerator] Selected date range: " + startDatePicker.getValue() + " to " + endDatePicker.getValue());

        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null ||
                startDatePicker.getValue().isAfter(latest) || endDatePicker.getValue().isBefore(earliest)) {
            startDatePicker.setValue(earliest);
            endDatePicker.setValue(latest);
            System.out.println("[ReportGenerator] Adjusted date range to: " + earliest + " to " + latest);
        }

        // Filter by date range and store
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        revenueData.clear();
        productData.clear();
        customerData.clear();
        Set<String> customers = new HashSet<>();
        totalOrders = 0;
        totalRevenue = 0.0;
        totalRefundsFromOrders = 0.0; // Reset refunds from canceled orders

        System.out.println("[ReportGenerator] Filtering orders between " + start + " and " + end + " for store ID: " + selectedStoreId);

        for (Order order : orders) {
            LocalDate orderDate = order.getOrderDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            
            // Check date range
            boolean inDateRange = (orderDate.isEqual(start) || orderDate.isAfter(start)) && 
                                 (orderDate.isEqual(end) || orderDate.isBefore(end));
            
            // Check store filter
            boolean inStoreRange = selectedStoreId == -1 || order.getStoreId() == selectedStoreId;
            
            if (inDateRange && inStoreRange) {
                String dateStr = orderDate.toString();
                double orderAmount = order.getTotalAmount();
                
                // Subtract refund amount if order was canceled
                if ("CANCELLED".equals(order.getStatus()) && order.getRefundAmount() > 0) {
                    totalRefundsFromOrders += order.getRefundAmount();
                    orderAmount -= order.getRefundAmount();
                    System.out.println("[ReportGenerator] Order " + order.getId() + " was canceled, refund: " + order.getRefundAmount());
                }
                
                revenueData.put(dateStr, revenueData.getOrDefault(dateStr, 0.0) + orderAmount);
                totalRevenue += orderAmount;
                totalOrders++;
                System.out.println("[ReportGenerator] Including order: " + order.getId() + " from " + orderDate + 
                                 " amount: " + orderAmount + " store: " + order.getStoreId());

                for (CartItem item : order.getItems()) {
                    String productName = item.getFlower().getFlowerName();
                    productData.put(productName, productData.getOrDefault(productName, 0) + item.getQuantity());
                }
                customers.add(order.getCustomerEmail());
            } else {
                System.out.println("[ReportGenerator] Excluding order: " + order.getId() + " from " + orderDate + 
                                 " (outside date range or store filter)");
            }
        }

        avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0.0;
        topProduct = productData.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");
        customerData.put("Unique Customers", customers.size());

        System.out.println("[ReportGenerator] Final stats - Orders: " + totalOrders + ", Revenue: " + totalRevenue +
                ", Avg: " + avgOrderValue + ", Customers: " + customers.size() + ", Refunds from orders: " + totalRefundsFromOrders);

        Platform.runLater(() -> {
            updateChartsForReportType();
            updateSummaryStatistics();
            // Update total refunds label to include canceled order refunds
            if (totalRefundsLbl != null) {
                totalRefundsLbl.setText(String.format("₪%.2f", getTotalRefunds()));
            }
            generateReportBtn.setText("Generate Report");
            generateReportBtn.setDisable(false);
            isProcessingUsers = false; // Reset flag when done
        });
    }

    private void processComplaints(List<Complain> complaints) {
        if (complaints == null) {
            System.out.println("[ReportGenerator] No complaints to process");
            return;
        }

        // Store all complaints for refund calculations (with store filtering)
        allComplaints.clear();
        allComplaints.addAll(complaints);

        complaintData.clear();

        for (Complain c : complaints) {
            if (c != null && c.getComplaint() != null) {
                // Filter by store ID
                boolean inStoreRange = selectedStoreId == -1 || c.getStoreId() == selectedStoreId;
                if (inStoreRange) {
                    String type = categorizeComplaint(c.getComplaint());
                    complaintData.put(type, complaintData.getOrDefault(type, 0) + 1);
                }
            }
        }

        // Update the total refunds label with combined refunds (complaints + canceled orders)
        Platform.runLater(() -> {
            if (totalRefundsLbl != null) {
                totalRefundsLbl.setText(String.format("₪%.2f", getTotalRefunds()));
            }
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

    // Add methods to manage current user
    public void setCurrentUser(LoginRegCheck user) {
        this.currentUser = user;
        
        // Determine user permissions and setup store filtering
        if (user != null && user.isType()) {
            // Check if user is store admin (type == true, employeetype == 0, store == 1-3)
            // or network admin (type == true, employeetype == 0, store == 4)
            if (user.getEmployeetype() == 0) {
                if (user.getStore() >= 1 && user.getStore() <= 3) {
                    // Store admin - can only see their own store
                    selectedStoreId = user.getStore();
                    isNetworkAdmin = false;
                    storeSelectionContainer.setVisible(false);
                    System.out.println("[ReportGenerator] Store admin for store: " + user.getStore());
                } else if (user.getStore() == 4) {
                    // Network admin - can see all stores with selection
                    selectedStoreId = -1; // All stores by default
                    isNetworkAdmin = true;
                    storeSelectionContainer.setVisible(true);
                    System.out.println("[ReportGenerator] Network admin - store selection enabled");
                } else {
                    // Invalid store assignment
                    generateReportBtn.setDisable(true);
                    exportPdfBtn.setDisable(true);
                    exportCsvBtn.setDisable(true);
                    emailReportBtn.setDisable(true);
                    quickRangeBtn.setDisable(true);
                    ordersReport.setDisable(true);
                    customersReport.setDisable(true);
                    showAlert("Access denied. Invalid store assignment.");
                    return;
                }
            } else {
                // Not a store admin or network admin
                generateReportBtn.setDisable(true);
                exportPdfBtn.setDisable(true);
                exportCsvBtn.setDisable(true);
                emailReportBtn.setDisable(true);
                quickRangeBtn.setDisable(true);
                ordersReport.setDisable(true);
                customersReport.setDisable(true);
                showAlert("Access denied. Only store administrators and network administrators can view reports.");
                return;
            }
        } else {
            // Not an employee
            generateReportBtn.setDisable(true);
            exportPdfBtn.setDisable(true);
            exportCsvBtn.setDisable(true);
            emailReportBtn.setDisable(true);
            quickRangeBtn.setDisable(true);
            ordersReport.setDisable(true);
            customersReport.setDisable(true);
            showAlert("Access denied. Only employees and administrators can view reports.");
            return;
        }
        
        System.out.println("[ReportGenerator] Current user set to: " + (user != null ? user.getUsername() : "null"));
    }

    public LoginRegCheck getCurrentUser() {
        return currentUser;
    }

    // Add cleanup method to be called when window is closed
    public void cleanup() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
            System.out.println("[ReportGenerator] Cleanup: Unregistered from EventBus");
        }
    }
}