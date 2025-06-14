package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;


public class Add_flower_event implements Serializable {

    private List<Flower> flowers;
    int type_of_catalog_that_send;



    public Add_flower_event(List<Flower> updatedItems, int sort_type) {
        this.flowers = updatedItems;
        this.type_of_catalog_that_send = sort_type;


    }





    public List<Flower> get_flowers() {
        return flowers;
    }
    public int get_catalog_type() { return this.type_of_catalog_that_send; }


}
