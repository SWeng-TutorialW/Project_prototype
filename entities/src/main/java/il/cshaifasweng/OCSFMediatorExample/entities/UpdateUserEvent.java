package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class UpdateUserEvent implements Serializable {
    private LoginRegCheck updatedUser;

    public UpdateUserEvent(LoginRegCheck updatedUser) {
        this.updatedUser = updatedUser;
    }

    public LoginRegCheck getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(LoginRegCheck updatedUser) {
        this.updatedUser = updatedUser;
    }
}
