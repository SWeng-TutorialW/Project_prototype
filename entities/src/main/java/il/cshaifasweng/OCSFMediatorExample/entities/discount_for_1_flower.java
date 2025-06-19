package il.cshaifasweng.OCSFMediatorExample.entities;


import java.io.Serializable;
import java.util.List;


public class  discount_for_1_flower implements Serializable {

    private List<Flower> flowers;
    int type_of_catalog_that_send;
    String flower_name;




    public  discount_for_1_flower(List<Flower> updatedItems, int sort_type,String flower_name) {
        this.flowers = updatedItems;
        this.type_of_catalog_that_send = sort_type;
        this.flower_name = flower_name;


    }


    public List<Flower> get_flowers() {
        return flowers;
    }
    public int get_catalog_type() { return this.type_of_catalog_that_send; }
    public String get_flower_name() { return this.flower_name; }


}


