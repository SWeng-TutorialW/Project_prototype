package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

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



    public Store() {} // ctor

    public Store(String storename,String address) {
        super();
        this.name = storename;
        Address = address;
        incomes = 0;
    }

    public double getIncomes() {
        return incomes;
    }

    public String getStoreName() {
        return name;
    }

    public String getaddress() {
        return Address;
    }

    public int getId() {
        return id;
    }
}
