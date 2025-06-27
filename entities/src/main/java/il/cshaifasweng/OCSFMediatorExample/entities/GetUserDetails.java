package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class GetUserDetails implements Serializable {

    private LoginRegCheck requestedUser;

    public GetUserDetails(LoginRegCheck loginRegCheck) {
        this.requestedUser = loginRegCheck;

    }
    public LoginRegCheck getUser() {
        return requestedUser;
    }



}
