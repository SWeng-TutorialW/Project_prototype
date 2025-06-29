package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Stores")
public class Store implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String Address;
    private double incomes;
    
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "store_flowers",
        joinColumns = @JoinColumn(name = "store_id"),
        inverseJoinColumns = @JoinColumn(name = "flower_id")
    )
    private List<Flower> flowersList = new ArrayList<>();

    public Store() {} // ctor

    public Store(String storename,String address,List<Flower> flowersList) {
        super();
        this.name = storename;
        Address = address;
        incomes = 0;
        if (flowersList != null) {
            this.flowersList = new ArrayList<>(flowersList);
        }
    }
    
    public List<Flower> getFlowersList() {return flowersList;}

    public double getIncomes() {
        return incomes;
    }

    public String getStoreName() {
        return name;
    }

    public String getaddress() {
        return Address;
    }
    
    public void setFlowersList(List<Flower> flowersList) {
        this.flowersList = flowersList != null ? new ArrayList<>(flowersList) : new ArrayList<>();
    }
    
    public void addFlower(Flower flower) {
        if (flower != null && !this.flowersList.contains(flower)) {
            this.flowersList.add(flower);
        }
    }
    
    public void removeFlower(Flower flower) {
        if (flower != null) {
            this.flowersList.remove(flower);
        }
    }

    public int getId() {
        return id;
    }
}
