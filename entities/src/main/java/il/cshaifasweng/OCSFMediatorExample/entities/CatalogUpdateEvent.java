package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;


public class CatalogUpdateEvent implements Serializable {

    private List<Flower> updatedItems;

    public CatalogUpdateEvent(List<Flower> updatedItems) {
        this.updatedItems = updatedItems;
    }

    public List<Flower> getUpdatedItems() {
        return updatedItems;
    }
}
