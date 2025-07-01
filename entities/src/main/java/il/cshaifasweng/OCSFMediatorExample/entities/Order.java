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
    private String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    
    // Cancellation fields
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancellationDate;
    private double refundAmount;
    private String cancellationReason;
    
    // Greeting card fields
    private boolean includeGreetingCard;
    private String greetingCardBackground;
    private String greetingCardMessage;
    
    // New fields for address
    private String city;
    private String streetAddress;
    private String apartment;
    
    // Delivery fields
    private boolean requiresDelivery;
    private double deliveryFee;
    
    // Delivery or pickup time (used for both delivery and pickup orders)
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryTime;
    
    // Link to user who placed the order
    @ManyToOne
    @JoinColumn(name = "user_id")
    private LoginRegCheck user;
    
    // Store ID where the order was created
    private int storeId;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<CartItem> items = new ArrayList<>();
    
    private double totalAmount;
    private double discountAmount = 0.0;
    
    // Track if a complaint has been sent for this order
    @Column(nullable = false)
    private boolean sentComplaint = false;
    
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
    
    public Order(String customerName, String customerEmail, LoginRegCheck user) {
        this();
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.user = user;
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
    
    public LoginRegCheck getUser() {
        return user;
    }
    
    public void setUser(LoginRegCheck user) {
        this.user = user;
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

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    // Cancellation methods
    public Date getCancellationDate() {
        return cancellationDate;
    }
    
    public void setCancellationDate(Date cancellationDate) {
        this.cancellationDate = cancellationDate;
    }
    
    public double getRefundAmount() {
        return refundAmount;
    }
    
    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }
    
    public String getCancellationReason() {
        return cancellationReason;
    }
    
    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
    
    /**
     * Calculate refund amount based on delivery time
     * @return refund amount (0.0 for no refund, 0.5 for 50% refund, 1.0 for full refund)
     */
    public double calculateRefundPercentage() {
        if (this.deliveryTime == null || this.orderDate == null) {
            return 0.0; // No delivery time set, no refund
        }
        
        long currentTime = System.currentTimeMillis();
        long deliveryTimeMillis = this.deliveryTime.getTime();
        long orderTimeMillis = this.orderDate.getTime();
        
        // Calculate time until delivery in hours
        long timeUntilDelivery = deliveryTimeMillis - currentTime;
        long timeSinceOrder = currentTime - orderTimeMillis;
        
        // If delivery time has passed, no refund
        if (timeUntilDelivery <= 0) {
            return 0.0;
        }
        
        double hoursUntilDelivery = timeUntilDelivery / (1000.0 * 60 * 60);
        
        // Refund policy:
        // More than 3 hours: 100% refund
        // Between 1-3 hours: 50% refund
        // Less than 1 hour: 0% refund
        if (hoursUntilDelivery > 3.0) {
            return 1.0; // Full refund
        } else if (hoursUntilDelivery > 1.0) {
            return 0.5; // 50% refund
        } else {
            return 0.0; // No refund
        }
    }
    
    /**
     * Cancel the order and calculate refund
     * @param reason cancellation reason
     * @return true if cancellation was successful
     */
    public boolean cancelOrder(String reason) {
        if ("CANCELLED".equals(this.status) || "DELIVERED".equals(this.status)) {
            return false; // Cannot cancel already cancelled or delivered orders
        }
        
        this.status = "CANCELLED";
        this.cancellationDate = new Date();
        this.cancellationReason = reason;
        
        double refundPercentage = calculateRefundPercentage();
        this.refundAmount = this.getTotalAmount() * refundPercentage;
        
        return true;
    }
    
    /**
     * Get refund policy description
     * @return description of refund policy
     */
    public String getRefundPolicyDescription() {
        double refundPercentage = calculateRefundPercentage();
        if (refundPercentage == 1.0) {
            return "Full refund (100%)";
        } else if (refundPercentage == 0.5) {
            return "Partial refund (50%)";
        } else {
            return "No refund";
        }
    }
    
    // Greeting card methods
    public boolean isIncludeGreetingCard() {
        return includeGreetingCard;
    }
    
    public void setIncludeGreetingCard(boolean includeGreetingCard) {
        this.includeGreetingCard = includeGreetingCard;
    }
    
    public String getGreetingCardBackground() {
        return greetingCardBackground;
    }
    
    public void setGreetingCardBackground(String greetingCardBackground) {
        this.greetingCardBackground = greetingCardBackground;
    }
    
    public String getGreetingCardMessage() {
        return greetingCardMessage;
    }
    
    public void setGreetingCardMessage(String greetingCardMessage) {
        this.greetingCardMessage = greetingCardMessage;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    // Getter and setter for sentComplaint
    public boolean isSentComplaint() {
        return sentComplaint;
    }

    public void setSentComplaint(boolean sentComplaint) {
        this.sentComplaint = sentComplaint;
    }
} 