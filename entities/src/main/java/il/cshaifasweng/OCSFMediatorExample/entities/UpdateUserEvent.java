package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class UpdateUserEvent implements Serializable {
    private LoginRegCheck updatedUser;
    private String msg;

    public UpdateUserEvent(LoginRegCheck updatedUser) {
        this.updatedUser = updatedUser;
    }
    public UpdateUserEvent(LoginRegCheck updatedUser, String msg) {
        this.updatedUser = updatedUser;
        this.msg = msg;
    }
    public LoginRegCheck getUpdatedUser() {
        return updatedUser;
    }
    public String getMsg() {
        return msg;
    }
    public void setUpdatedUser(LoginRegCheck updatedUser) {
        this.updatedUser = updatedUser;
    }
}
