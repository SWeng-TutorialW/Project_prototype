package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import il.cshaifasweng.OCSFMediatorExample.entities.Complain;

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
            
            writer.write("Report Type: " + controller.getSelectedReportType() + "\n\n");
            
            // Add summary statistics
            writer.write("SUMMARY STATISTICS\n");
            writer.write("==================\n");
            writer.write(String.format("Total Orders: %d\n", controller.getTotalOrders()));
            writer.write(String.format("Total Revenue: $%.2f\n", controller.getTotalRevenue()));
            writer.write(String.format("Average Order Value: $%.2f\n", controller.getAvgOrderValue()));
            writer.write(String.format("Top Product: %s\n", controller.getTopProduct()));
            
            // Add complaints and refunds data if available
            if (controller.getComplaintData() != null && !controller.getComplaintData().isEmpty()) {
                writer.write(String.format("Total Refunds: $%.2f\n", controller.getTotalRefunds()));
            }
            writer.write("\n");
            
            // Add detailed data
            writer.write("DETAILED DATA\n");
            writer.write("=============\n");
            
            // Revenue data
            writer.write("Daily Revenue:\n");
            writer.write("Date\t\tRevenue ($)\tOrders\n");
            writer.write("----\t\t-----------\t------\n");
            
            Map<String, Double> revenueData = controller.getRevenueData();
            List<String> sortedDates = revenueData.keySet().stream().sorted().toList();
            
            for (String date : sortedDates) {
                double revenue = revenueData.get(date);
                int orders = (int) (revenue / controller.getAvgOrderValue());
                writer.write(String.format("%s\t\t%.2f\t\t%d\n", date, revenue, orders));
            }
            
            writer.write("\nProduct Sales:\n");
            writer.write("Product\t\tUnits Sold\n");
            writer.write("-------\t\t----------\n");
            
            Map<String, Integer> productData = controller.getProductData();
            for (Map.Entry<String, Integer> entry : productData.entrySet()) {
                writer.write(String.format("%s\t\t%d\n", entry.getKey(), entry.getValue()));
            }
            
            // Add complaints data if available
            Map<String, Integer> complaintData = controller.getComplaintData();
            if (complaintData != null && !complaintData.isEmpty()) {
                writer.write("\nComplaints by Type:\n");
                writer.write("Type\t\tCount\n");
                writer.write("----\t\t-----\n");
                
                for (Map.Entry<String, Integer> entry : complaintData.entrySet()) {
                    writer.write(String.format("%s\t\t%d\n", entry.getKey(), entry.getValue()));
                }
                
                // Add detailed complaints information
                writer.write("\nDetailed Complaints:\n");
                writer.write("Date\t\tCustomer\t\tComplaint\t\tResponse\t\tRefund\n");
                writer.write("----\t\t--------\t\t---------\t\t--------\t\t------\n");
                
                List<Complain> allComplaints = controller.getAllComplaints();
                if (allComplaints != null) {
                    for (Complain complaint : allComplaints) {
                        if (complaint != null) {
                            String date = complaint.getTimestamp() != null ? 
                                complaint.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A";
                            String customer = complaint.getClient() != null ? complaint.getClient() : "N/A";
                            String complaintText = complaint.getComplaint() != null ? complaint.getComplaint() : "";
                            
                            // Check if this is a response
                            String response = "";
                            if (complaintText.startsWith("answer to")) {
                                response = complaintText.substring("answer to".length()).trim();
                                complaintText = "Response received";
                            }
                            
                            String refund = complaint.getRefundAmount() > 0 ? 
                                String.format("$%.2f", complaint.getRefundAmount()) : "";
                            
                            writer.write(String.format("%s\t\t%s\t\t%s\t\t%s\t\t%s\n", 
                                date, customer, complaintText, response, refund));
                        }
                    }
                }
            }
            
            writer.write("\nReport generated on: " + 
                    java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }
} 