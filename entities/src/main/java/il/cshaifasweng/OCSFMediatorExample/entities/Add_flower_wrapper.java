package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class Add_flower_wrapper implements Serializable {
    private Flower flower;
    private int type;

    public Add_flower_wrapper(Flower flower, int discount) {
        this.flower = flower;
        this.type = discount;
    }

    public Flower getFlower() {
        return flower;
    }

    public int gettype() {
        return type;
    }
}