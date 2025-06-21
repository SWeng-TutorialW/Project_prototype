package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class CustomerOrdersResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<OrderSummary> orders;
    
    public CustomerOrdersResponse(List<OrderSummary> orders) {
        this.orders = orders;
    }
    
    public List<OrderSummary> getOrders() {
        return orders;
    }
    
    public void setOrders(List<OrderSummary> orders) {
        this.orders = orders;
    }
} 