package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import javax.persistence.Entity;

@Entity
@Table(name = "catalog_items")
public class CatalogItem implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String itemName;
    private double itemPrice;
    private String itemCategory;

    public int getId() { return id; }
    public String getItemName() { return itemName; }
    public double getItemPrice() { return itemPrice; }
    public String getItemCategory() { return itemCategory; }

    public void setId(int id) { this.id = id; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setItemPrice(double itemPrice) { this.itemPrice = itemPrice; }
    public void setItemCategory(String itemCategory) { this.itemCategory = itemCategory; }
}

