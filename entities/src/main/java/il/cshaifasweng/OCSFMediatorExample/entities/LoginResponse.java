package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class LoginResponse implements Serializable {

    private LoginRegCheck user;
    private String message;

    public LoginResponse(LoginRegCheck user, String message) {
        this.user = user;
        this.message = message;
    }
    public LoginResponse(LoginRegCheck user) {
        this.user = user;
    }

    public LoginRegCheck getUser() {
        return user;
    }
    public void setUser(LoginRegCheck user) {
        this.user = user;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
