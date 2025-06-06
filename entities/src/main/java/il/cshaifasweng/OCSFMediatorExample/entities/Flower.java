package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

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

    public Flower() {} // ctor

    public Flower(String flowerName, double flowerPrice, String flowerType){
        super();
        this.flowerName = flowerName;
        this.flowerPrice = flowerPrice;
        this.flowerType = flowerType;
    }

    public double getFlowerPrice() {
        return flowerPrice;
    }

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
}
