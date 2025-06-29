package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class PDFExporter {
    
    public static void exportToPDF(File file, ReportGeneratorController controller) throws IOException {
        // For now, we'll create a simple text-based report
        // In a real implementation, you would use a proper PDF library
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("LILACH FLOWERS - STORE REPORT\n");
            writer.write("==============================\n\n");
            
            // Add date range
            if (controller.getStartDate() != null && controller.getEndDate() != null) {
                writer.write(String.format("Report Period: %s to %s\n",
                        controller.getStartDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                        controller.getEndDate().format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))));
            }
            
            writer.write("Report Type: " + controller.getSelectedReportType() + "\n");
            writer.write("Report Scope: " + controller.getReportScope() + "\n\n");
            
            // Add summary statistics
            writer.write("SUMMARY STATISTICS\n");
            writer.write("==================\n");
            writer.write(String.format("Total Orders: %d\n", controller.getTotalOrders()));
            writer.write(String.format("Total Revenue: $%.2f\n", controller.getTotalRevenue()));
            writer.write(String.format("Total Refunds: $%.2f\n", controller.getTotalRefunds()));
            writer.write(String.format("Net Revenue: $%.2f\n", controller.getNetRevenue()));
            writer.write(String.format("Average Order Value: $%.2f\n", controller.getAvgOrderValue()));
            writer.write(String.format("Top Product: %s\n", controller.getTopProduct()));
            writer.write(String.format("Total Complaints: %d\n\n", controller.getTotalComplaints()));
            
            // Add detailed data
            writer.write("DETAILED DATA\n");
            writer.write("=============\n");
            
            // Revenue data
            writer.write("Daily Revenue:\n");
            writer.write("Date\t\tRevenue ($)\tRefunds ($)\tNet Revenue ($)\tOrders\n");
            writer.write("----\t\t-----------\t-----------\t---------------\t------\n");
            
            Map<String, Double> revenueData = controller.getRevenueData();
            Map<String, Double> refundData = controller.getRefundData();
            List<String> sortedDates = revenueData.keySet().stream().sorted().toList();
            
            for (String date : sortedDates) {
                double revenue = revenueData.get(date);
                double refunds = refundData.getOrDefault(date, 0.0);
                double netRevenue = revenue - refunds;
                int orders = (int) (revenue / controller.getAvgOrderValue());
                writer.write(String.format("%s\t\t%.2f\t\t%.2f\t\t%.2f\t\t%d\n", date, revenue, refunds, netRevenue, orders));
            }
            
            writer.write("\nProduct Sales:\n");
            writer.write("Product\t\tUnits Sold\n");
            writer.write("-------\t\t----------\n");
            
            Map<String, Integer> productData = controller.getProductData();
            for (Map.Entry<String, Integer> entry : productData.entrySet()) {
                writer.write(String.format("%s\t\t%d\n", entry.getKey(), entry.getValue()));
            }
            
            // Add complaints data
            writer.write("\nComplaints Analysis:\n");
            writer.write("Category\t\tCount\n");
            writer.write("--------\t\t-----\n");
            
            Map<String, Integer> complaintData = controller.getComplaintData();
            for (Map.Entry<String, Integer> entry : complaintData.entrySet()) {
                writer.write(String.format("%s\t\t%d\n", entry.getKey(), entry.getValue()));
            }
            
            writer.write("\nReport generated on: " + 
                    java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }
} 