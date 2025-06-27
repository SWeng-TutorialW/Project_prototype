package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class OrderCancellationResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean success;
    private String message;
    private double refundAmount;
    private String refundPolicy;
    private int orderId;
    
    public OrderCancellationResponse() {}
    
    public OrderCancellationResponse(boolean success, String message, double refundAmount, String refundPolicy, int orderId) {
        this.success = success;
        this.message = message;
        this.refundAmount = refundAmount;
        this.refundPolicy = refundPolicy;
        this.orderId = orderId;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public double getRefundAmount() {
        return refundAmount;
    }
    
    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }
    
    public String getRefundPolicy() {
        return refundPolicy;
    }
    
    public void setRefundPolicy(String refundPolicy) {
        this.refundPolicy = refundPolicy;
    }
    
    public int getOrderId() {
        return orderId;
    }
    
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
} 