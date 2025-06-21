package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

public class update_user_values implements Serializable
{
    private LoginRegCheck user;
    private String column;
    private String new_value;

    public update_user_values(LoginRegCheck user, String column, String new_value)
    {
        this.user = user;
        this.column = column;
        this.new_value = new_value;
    }
    public LoginRegCheck getUser()
    {
        return user;
    }
    public String getColumn()
    {
        return column;
    }
    public  String getNew_value()
    {
        return new_value;
    }


}
