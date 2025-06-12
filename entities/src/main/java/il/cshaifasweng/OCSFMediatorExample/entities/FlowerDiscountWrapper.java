package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class FlowerDiscountWrapper implements Serializable {
    private Flower flower;
    private int discount;

    public FlowerDiscountWrapper(Flower flower, int discount) {
        this.flower = flower;
        this.discount = discount;
    }

    public Flower getFlower() {
        return flower;
    }

    public int getDiscount() {
        return discount;
    }
}
