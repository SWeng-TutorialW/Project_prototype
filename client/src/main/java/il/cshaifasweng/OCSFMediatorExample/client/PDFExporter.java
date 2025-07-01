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
            
            // Add store information
            String storeInfo = controller.getStoreInfo();
            writer.write("Store: " + storeInfo + "\n");
            
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
            writer.write(String.format("Total Revenue: ₪%.2f\n", controller.getTotalRevenue()));
            writer.write(String.format("Average Order Value: ₪%.2f\n", controller.getAvgOrderValue()));
            writer.write(String.format("Top Product: %s\n", controller.getTopProduct()));
            
            // Add complaints and refunds data if available
            if (controller.getComplaintData() != null && !controller.getComplaintData().isEmpty()) {
                writer.write(String.format("Total Refunds: ₪%.2f\n", controller.getTotalRefunds()));
            }
            writer.write("\n");
            
            // Add detailed data
            writer.write("DETAILED DATA\n");
            writer.write("=============\n");
            
            // Revenue data
            writer.write("Daily Revenue:\n");
            writer.write("Date\t\tRevenue (₪)\tOrders\n");
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
                writer.write("Type\t\tCount\t\tTotal Refund Amount\n");
                writer.write("----\t\t-----\t\t------------------\n");
                
                double totalComplaintRefunds = 0.0;
                for (Map.Entry<String, Integer> entry : complaintData.entrySet()) {
                    String complaintType = entry.getKey();
                    int count = entry.getValue();
                    double refundAmount = getRefundAmountForComplaintType(complaintType, controller.getAllComplaints());
                    totalComplaintRefunds += refundAmount;
                    writer.write(String.format("%s\t\t%d\t\t%.2f\n", complaintType, count, refundAmount));
                }
                
                writer.write(String.format("Total Complaints:\t%d\t\t%.2f\n", 
                    complaintData.values().stream().mapToInt(Integer::intValue).sum(), totalComplaintRefunds));
                
                // Add detailed complaints information
                writer.write("\nDetailed Complaints Information:\n");
                writer.write("Client\t\tComplaint\t\tRefund Amount\n");
                writer.write("------\t\t---------\t\t-------------\n");
                
                List<Complain> allComplaints = controller.getAllComplaints();
                if (allComplaints != null) {
                    for (Complain complaint : allComplaints) {
                        if (complaint != null) {
                            String clientName = complaint.getClient() != null ? complaint.getClient() : "Unknown";
                            String complaintText = complaint.getComplaint() != null ? complaint.getComplaint() : "No complaint text";
                            double refundAmount = complaint.getRefundAmount();
                            
                            // Truncate long complaint text for better formatting
                            if (complaintText.length() > 50) {
                                complaintText = complaintText.substring(0, 47) + "...";
                            }
                            
                            writer.write(String.format("%s\t\t%s\t\t%.2f\n", clientName, complaintText, refundAmount));
                        }
                    }
                }
            }
            
            writer.write("\nReport generated on: " + 
                    java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }
    
    private static double getRefundAmountForComplaintType(String complaintType, List<Complain> complaints) {
        if (complaints == null) return 0.0;
        
        return complaints.stream()
                .filter(c -> c != null && c.getComplaint() != null && categorizeComplaint(c.getComplaint()).equals(complaintType))
                .mapToDouble(Complain::getRefundAmount)
                .sum();
    }
    
    private static String categorizeComplaint(String complaintText) {
        if (complaintText == null) return "Other";
        String lower = complaintText.toLowerCase();
        if (lower.contains("delivery")) return "Delivery Issues";
        if (lower.contains("refund")) return "Refunds";
        if (lower.contains("quality")) return "Quality Issues";
        if (lower.contains("wrong")) return "Wrong Items";
        if (lower.contains("service")) return "Customer Service";
        return "Other";
    }
} 