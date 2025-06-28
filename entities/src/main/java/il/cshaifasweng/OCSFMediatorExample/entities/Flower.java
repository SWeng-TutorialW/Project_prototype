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
    private boolean sale;
    private int discount;
    private String color;


    public Flower() {} // ctor

    public Flower(String flowerName, double flowerPrice, String flowerType){
        super();
        this.flowerName = flowerName;
        this.flowerPrice = flowerPrice;
        this.flowerType = flowerType;
        this.sale = false;
        if (flowerType.equals("Rose") ||
                flowerType.equals("Anemone") ||
                flowerType.equals("Dahlia") ||
                flowerType.equals("Gladiolus") ||
                flowerType.equals("Hibiscus"))
        {
            color = "Red";
        } else if (flowerType.equals("Sunflower") ||
                flowerType.equals("Daffodil")
                ) {
            color = "Yellow";
        } else if (flowerType.equals("Orchid") ||
                flowerType.equals("Tulip") ||
                flowerType.equals("Peony") ||
                flowerType.equals("Carnation") ||
                flowerType.equals("Snapdragon") ||
                flowerType.equals("Camellia")||
                flowerType.equals("Begonia")||
                 flowerType.equals("Ranunculus")){
            color = "Pink";
        } else if (flowerType.equals("Jacarande") ||
                flowerType.equals("Iris") ||
                flowerType.equals("Freesia") ||
                flowerType.equals("Lavender")
                ) {
            color = "Purple";
        } else if (
                flowerType.equals("Lily") ||
                flowerType.equals("Chrysanthemum") ||
                flowerType.equals("Cosmos") ||
                flowerType.equals("Lisianthus")) {
            color = "White";
        } else if (flowerType.equals("Marigold") ||
                flowerType.equals("Zinnia") ||
                flowerType.equals("Gerbera")) {
            color = "Orange";
        } else if (flowerType.equals("Hyacinth") ||
                flowerType.equals("Delphinium")) {
            color = "Blue";
        }
    }
    public String getColor()
    {
        return color;
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
}
