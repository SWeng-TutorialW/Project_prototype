package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CartItems")
public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "flower_id")
    private Flower flower;
    
    private int quantity;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    public CartItem() {}
    
    public CartItem(Flower flower, int quantity) {
        this.flower = flower;
        this.quantity = quantity;
    }
    
    public int getId() {
        return id;
    }
    
    public Flower getFlower() {
        return flower;
    }
    
    public void setFlower(Flower flower) {
        this.flower = flower;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public double getTotalPrice() {
        return flower.getFlowerPrice() * quantity;
    }

    public String getStore() {
        return flower.getStore().getName();
    }
} 