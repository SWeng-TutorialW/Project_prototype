package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderSummary implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private Date orderDate;
    private double totalAmount;
    private String status;
    private boolean requiresDelivery;
    private String itemsSummary;
    
    public OrderSummary(int id, Date orderDate, double totalAmount, String status, boolean requiresDelivery, String itemsSummary) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.requiresDelivery = requiresDelivery;
        this.itemsSummary = itemsSummary;
    }
    
    // Getters
    public int getId() { return id; }
    public Date getOrderDate() { return orderDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public boolean isRequiresDelivery() { return requiresDelivery; }
    public String getItemsSummary() { return itemsSummary; }
    
    // Utility method for date formatting
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(orderDate);
    }
} 