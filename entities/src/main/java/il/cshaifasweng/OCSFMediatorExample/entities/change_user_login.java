package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class change_user_login implements Serializable {
    private LoginRegCheck USER;
    int the_new_state = 0;


    public change_user_login( LoginRegCheck user ,int the_new_state) {
       USER=user;
       this.the_new_state=the_new_state;
    }

    public  LoginRegCheck get_user() {
        return USER;
    }
    public int get_the_new_state() {
        return the_new_state;
    }


}