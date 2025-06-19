package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;
import java.util.List;


public class ComplainUpdateEvent implements Serializable {

    private List<Complain> updatedItems;
    private List<LoginRegCheck> users;

    public ComplainUpdateEvent(List<Complain> updatedItems, List<LoginRegCheck> USERS) {
        this.updatedItems = updatedItems;
        users = USERS;
    }
    public ComplainUpdateEvent(List<Complain> updatedItems) {
        this.updatedItems = updatedItems;

    }


    public List<Complain> getUpdatedItems() {
        return updatedItems;
    }
    public List<LoginRegCheck> getUsers() {
        return users;
    }
}
