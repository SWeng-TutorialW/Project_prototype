package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class CustomerOrdersRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String customerName;
    
    public CustomerOrdersRequest(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
} 