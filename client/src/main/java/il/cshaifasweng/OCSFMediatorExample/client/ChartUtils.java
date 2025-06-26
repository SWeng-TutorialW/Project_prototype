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
        chart.setLegendVisible(true);
        chart.setAnimated(true);
        
        xAxis.setLabel("Date");
        yAxis.setLabel("Revenue ($)");
        
        // Style the chart
        chart.setStyle("-fx-background-color: white; -fx-border-color: #C8A2C8; -fx-border-width: 1;");
    }
    
    public static void setupProductChart(PieChart chart) {
        chart.setTitle("Product Distribution");
        chart.setLegendVisible(true);
        chart.setAnimated(true);
        
        // Style the chart
        chart.setStyle("-fx-background-color: white; -fx-border-color: #C8A2C8; -fx-border-width: 1;");
    }
    
    public static Map<String, Double> generateSampleRevenueData(LocalDate startDate, LocalDate endDate) {
        Map<String, Double> revenueData = new HashMap<>();
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            String dateStr = currentDate.format(DateTimeFormatter.ofPattern("MM/dd"));
            
            // Generate realistic revenue data with some variation
            double baseRevenue = 1500.0;
            double variation = Math.sin(currentDate.getDayOfYear() * 0.1) * 500; // Seasonal variation
            double randomFactor = (Math.random() - 0.5) * 300; // Random variation
            double revenue = Math.max(500, baseRevenue + variation + randomFactor); // Minimum $500
            
            revenueData.put(dateStr, revenue);
            currentDate = currentDate.plusDays(1);
        }
        
        return revenueData;
    }
    
    public static Map<String, Integer> generateSampleProductData() {
        Map<String, Integer> productData = new HashMap<>();
        
        // Sample flower products with realistic sales numbers
        productData.put("Roses", 150 + (int)(Math.random() * 50));
        productData.put("Tulips", 120 + (int)(Math.random() * 40));
        productData.put("Lilies", 90 + (int)(Math.random() * 30));
        productData.put("Orchids", 80 + (int)(Math.random() * 25));
        productData.put("Sunflowers", 60 + (int)(Math.random() * 20));
        productData.put("Daffodils", 70 + (int)(Math.random() * 25));
        productData.put("Hyacinths", 50 + (int)(Math.random() * 20));
        productData.put("Carnations", 100 + (int)(Math.random() * 30));
        
        return productData;
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
        
        int colorIndex = 0;
        for (Map.Entry<String, Integer> entry : productData.entrySet()) {
            PieChart.Data data = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChartData.add(data);
            
            // Apply custom colors
            data.getNode().setStyle("-fx-pie-color: " + toHexString(CHART_COLORS[colorIndex % CHART_COLORS.length]) + ";");
            colorIndex++;
        }
        
        chart.setData(pieChartData);
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