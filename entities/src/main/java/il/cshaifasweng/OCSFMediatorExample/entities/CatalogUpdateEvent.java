package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;

public class CatalogUpdateEvent {


    private List<CatalogItem> updatedItems;

    public CatalogUpdateEvent(List<CatalogItem> updatedItems) {
        this.updatedItems = updatedItems;
    }
    public List<CatalogItem> getUpdatedItems() {
        return updatedItems;
    }

}
