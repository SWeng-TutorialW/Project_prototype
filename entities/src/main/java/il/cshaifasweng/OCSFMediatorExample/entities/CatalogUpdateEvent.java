package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;


public class CatalogUpdateEvent implements Serializable {

    private List<Flower> updatedItems;
    private List<LoginRegCheck> users;
    private List<Store> stores;



    public CatalogUpdateEvent(List<Flower> updatedItems, List<LoginRegCheck> USERS, List<Store> STORES) {
        this.updatedItems = updatedItems;
        users = USERS;
        stores = STORES;

    }
    public CatalogUpdateEvent(List<Flower> updatedItems) {
        this.updatedItems = updatedItems;

    }





    public List<Flower> getUpdatedItems() {
        return updatedItems;
    }
    public List<LoginRegCheck> getUsers() {
        return users;
    }
    public List<Store> getStores() {return stores;}

}
