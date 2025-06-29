package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Flowers")
public class Flower implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String flowerName;
    private double flowerPrice;
    private String flowerType;
    private boolean sale;
    private int discount;
    
    // New fields
    @Column(length = 1000) // Allow longer image paths/URLs
    private String image;
    
    @Column(length = 50)
    private String color;
    
    @Column(length = 100)
    private String category;

    @OneToMany(mappedBy = "flower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    public Flower() {} // ctor

    public Flower(String flowerName, double flowerPrice, String flowerType){
        super();
        this.flowerName = flowerName;
        this.flowerPrice = flowerPrice;
        this.flowerType = flowerType;
        this.sale = false;
        this.image = "";
        this.color = "";
        this.category = "";
    }
    
    // Constructor with all fields
    public Flower(String flowerName, double flowerPrice, String flowerType, String image, String color, String category){
        super();
        this.flowerName = flowerName;
        this.flowerPrice = flowerPrice;
        this.flowerType = flowerType;
        this.sale = false;
        this.image = image;
        this.color = color;
        this.category = category;
    }

    public double getFlowerPrice() {
        return flowerPrice;
    }
    public int getDiscount() {return discount;}
    public void setDiscount(int discount) {this.discount = discount;}

    public void setSale(boolean sale) {
        this.sale = sale;
    }
    public boolean isSale() {return sale;}

    public String getFlowerName() {
        return flowerName;
    }

    public String getFlowerType() {
        return flowerType;
    }

    public void setFlowerName(String flowerName) {
        this.flowerName = flowerName;
    }

    public void setFlowerPrice(double flowerPrice) {
        this.flowerPrice = flowerPrice;
    }

    public void setFlowerType(String flowerType) {
        this.flowerType = flowerType;
    }
    public int getId() {
        return id;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }
    
    // New getters and setters
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
