package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.paint.Color;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChartUtils {
    
    private static final Color[] CHART_COLORS = {
        Color.rgb(200, 162, 200), // Lilac (project theme)
        Color.rgb(255, 193, 7),   // Yellow
        Color.rgb(220, 53, 69),   // Red
        Color.rgb(40, 167, 69),   // Green
        Color.rgb(23, 162, 184),  // Cyan
        Color.rgb(111, 66, 193),  // Purple
        Color.rgb(253, 126, 20),  // Orange
        Color.rgb(108, 117, 125)  // Gray
    };
    
    public static void setupRevenueChart(LineChart<String, Number> chart, CategoryAxis xAxis, NumberAxis yAxis) {
        chart.setTitle("Revenue Trend");
        chart.setLegendVisible(false);
        chart.setAnimated(true);
        
        xAxis.setLabel("Date");
        yAxis.setLabel("Revenue ($)");
        
        // Style the chart
        chart.setStyle("-fx-background-color: white; -fx-border-color: #C8A2C8; -fx-border-width: 1;");
    }
    
    public static void setupProductChart(PieChart chart) {
        chart.setTitle("Product Distribution");
        chart.setLegendVisible(false);
        chart.setAnimated(true);
        
        // Style the chart
        chart.setStyle("-fx-background-color: white; -fx-border-color: #C8A2C8; -fx-border-width: 1;");
    }
    
    public static void updateRevenueChart(LineChart<String, Number> chart, Map<String, Double> revenueData) {
        chart.getData().clear();
        
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Daily Revenue");
        
        // Sort dates for proper chart display
        List<String> sortedDates = new ArrayList<>(revenueData.keySet());
        sortedDates.sort(Comparator.naturalOrder());
        
        for (String date : sortedDates) {
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(date, revenueData.get(date));
            revenueSeries.getData().add(dataPoint);
        }
        
        chart.getData().add(revenueSeries);
        
        // Apply custom styling to the series
        if (!chart.getData().isEmpty()) {
            XYChart.Series<String, Number> series = chart.getData().get(0);
            for (XYChart.Data<String, Number> data : series.getData()) {
                if (data.getNode() != null) {
                    data.getNode().setStyle("-fx-background-color: #C8A2C8;");
                }
            }
        }
    }
    
    public static void updateProductChart(PieChart chart, Map<String, Integer> productData) {
        chart.getData().clear();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : productData.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChartData.add(data);
        }

        chart.setData(pieChartData);

        // Now apply custom colors after nodes are created
        int colorIndex = 0;
        for (PieChart.Data data : chart.getData()) {
            final int idx = colorIndex;
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-pie-color: " + toHexString(CHART_COLORS[idx % CHART_COLORS.length]) + ";");
                }
            });
            // If node already exists (sometimes it does), style it immediately
            if (data.getNode() != null) {
                data.getNode().setStyle("-fx-pie-color: " + toHexString(CHART_COLORS[colorIndex % CHART_COLORS.length]) + ";");
            }
            colorIndex++;
        }
    }
    
    public static Map<String, Integer> generateCustomerData() {
        Map<String, Integer> customerData = new HashMap<>();
        
        customerData.put("New Customers", 25 + (int)(Math.random() * 15));
        customerData.put("Returning Customers", 80 + (int)(Math.random() * 30));
        customerData.put("VIP Customers", 15 + (int)(Math.random() * 10));
        customerData.put("Corporate Clients", 10 + (int)(Math.random() * 8));
        
        return customerData;
    }
    
    public static Map<String, Integer> generateComplaintData() {
        Map<String, Integer> complaintData = new HashMap<>();
        
        complaintData.put("Delivery Issues", 5 + (int)(Math.random() * 3));
        complaintData.put("Quality Issues", 3 + (int)(Math.random() * 2));
        complaintData.put("Wrong Items", 2 + (int)(Math.random() * 2));
        complaintData.put("Refunds", 4 + (int)(Math.random() * 3));
        complaintData.put("Customer Service", 1 + (int)(Math.random() * 2));
        
        return complaintData;
    }
    
    public static Map<String, Double> generateBranchData() {
        Map<String, Double> branchData = new HashMap<>();
        
        branchData.put("Store 1", 2500.0 + Math.random() * 1000);
        branchData.put("Store 2", 2200.0 + Math.random() * 800);
        branchData.put("Store 3", 1800.0 + Math.random() * 600);
        branchData.put("Online", 3200.0 + Math.random() * 1200);
        
        return branchData;
    }
    
    private static String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
    
    public static void applyChartTheme(Chart chart) {
        // Apply consistent styling to charts
        chart.setStyle("-fx-background-color: white; -fx-border-color: #C8A2C8; -fx-border-width: 1; -fx-border-radius: 5;");
    }
} 