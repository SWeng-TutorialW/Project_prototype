package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;
    
    private String customerName;
    private String customerEmail;
    private String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED
    
    // New fields for address
    private String city;
    private String streetAddress;
    private String apartment;
    
    // Delivery fields
    private boolean requiresDelivery;
    private double deliveryFee;
    
    // Delivery time
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryTime;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<CartItem> items = new ArrayList<>();
    
    private double totalAmount;
    
    public Order() {
        this.orderDate = new Date();
        this.status = "PENDING";
        this.deliveryFee = 0.0;
    }
    
    public Order(String customerName, String customerEmail) {
        this();
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }
    
    public int getId() {
        return id;
    }
    
    public Date getOrderDate() {
        return orderDate;
    }
    
    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public List<CartItem> getItems() {
        return items;
    }
    
    public void setItems(List<CartItem> items) {
        this.items = items;
    }
    
    public void addItem(CartItem item) {
        items.add(item);
        item.setOrder(this);
        updateTotalAmount();
    }
    
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setOrder(null);
        updateTotalAmount();
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getStreetAddress() {
        return streetAddress;
    }
    
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    
    public String getApartment() {
        return apartment;
    }
    
    public void setApartment(String apartment) {
        this.apartment = apartment;
    }
    
    public boolean isRequiresDelivery() {
        return requiresDelivery;
    }
    
    public void setRequiresDelivery(boolean requiresDelivery) {
        this.requiresDelivery = requiresDelivery;
        if (requiresDelivery) {
            this.deliveryFee = 20.0; // 20 ILS delivery fee
        } else {
            this.deliveryFee = 0.0;
        }
        updateTotalAmount(); // Update total to include delivery fee
    }
    
    public double getDeliveryFee() {
        return deliveryFee;
    }
    
    public Date getDeliveryTime() {
        return deliveryTime;
    }
    
    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    
    public double getTotalAmount() {
        return this.totalAmount + this.deliveryFee;
    }
    
    private void updateTotalAmount() {
        this.totalAmount = items.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
    }
} 