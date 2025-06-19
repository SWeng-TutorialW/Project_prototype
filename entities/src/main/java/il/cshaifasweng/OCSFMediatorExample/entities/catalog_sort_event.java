package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;


public class catalog_sort_event implements Serializable {

    private List<Flower> updatedItems;
    int sort_type;
    private List<Flower> original_catalog;


    public catalog_sort_event(List<Flower> updatedItems, int sort_type,List<Flower> original_catalog) {
        this.updatedItems = updatedItems;
        this.sort_type = sort_type;
        this.original_catalog = original_catalog;

    }





    public List<Flower> get_Sorted_flowers() {
        return updatedItems;
    }
    public List<Flower> get_original_catalog() {return original_catalog;}
    public int getSort_type() { return sort_type; }


}
