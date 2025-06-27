package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.Date;

public class OrderCancellationRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int orderId;
    private String cancellationReason;
    private String username; // User requesting cancellation
    
    public OrderCancellationRequest() {}
    
    public OrderCancellationRequest(int orderId, String cancellationReason, String username) {
        this.orderId = orderId;
        this.cancellationReason = cancellationReason;
        this.username = username;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    
    public String getCancellationReason() {
        return cancellationReason;
    }
    
    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
} 