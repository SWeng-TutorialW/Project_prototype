package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;


public class CatalogUpdateEvent implements Serializable {

    private List<Flower> updatedItems;
    private List<LoginRegCheck> users;


    public CatalogUpdateEvent(List<Flower> updatedItems, List<LoginRegCheck> USERS) {
        this.updatedItems = updatedItems;
        users = USERS;
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
}
