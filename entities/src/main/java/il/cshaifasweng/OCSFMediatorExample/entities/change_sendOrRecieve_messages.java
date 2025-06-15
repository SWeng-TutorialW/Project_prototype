package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class change_sendOrRecieve_messages implements Serializable
{   private LoginRegCheck USER;
    boolean the_new_state_for_send = false;
    boolean the_new_state_for_recieve = false;
    String user_name=null;


    public change_sendOrRecieve_messages( LoginRegCheck user ,boolean the_new_state_for_send,boolean the_new_state_for_recieve) {
        USER=user;
        this.the_new_state_for_send=the_new_state_for_send;
        this.the_new_state_for_recieve=the_new_state_for_recieve;
    }
    public change_sendOrRecieve_messages( String user ,boolean the_new_state_for_send,boolean the_new_state_for_recieve) {
        user_name=user;
        this.the_new_state_for_send=the_new_state_for_send;
        this.the_new_state_for_recieve=the_new_state_for_recieve;
    }
    public String get_user_name() {
        return user_name;
    }


    public  LoginRegCheck get_user() {
        return USER;
    }
    public boolean get_the_new_state_for_send() {
        return the_new_state_for_send;
    }
    public boolean get_the_new_state_for_recieve() {
        return the_new_state_for_recieve;
    }

}
