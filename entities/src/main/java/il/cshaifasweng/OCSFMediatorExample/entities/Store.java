package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
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
    @Transient
    private List<Flower> flowersList;



    public Store() {} // ctor

    public Store(String storename,String address,List<Flower> flowersList) {
        super();
        this.name = storename;
        Address = address;
        incomes = 0;
        this.flowersList = flowersList;
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
    public void setFlowersList(List<Flower> flowersList) {this.flowersList = flowersList;}

    public int getId() {
        return id;
    }
}
