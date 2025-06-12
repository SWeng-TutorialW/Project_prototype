package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;



@Entity
@Table(name = "users_tbl")
public class LoginRegCheck implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "user_pass")
    private String password;

    @Column(name = "emailAdd")
    private String email;

    private int isLogin; // 1 = login, 0 = registration
    private boolean type; // false = client, true = employee
    private int accType ;        //??

    public LoginRegCheck() {}

    public LoginRegCheck(String username, String password, String email, int isLogin, int accType) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLogin = isLogin;
        this.type = false;
        this.accType = accType;
    }

    public LoginRegCheck(String username, String password, String email, int isLogin) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLogin = isLogin;
        this.type = false;
    }

    public LoginRegCheck(String username, String password, String email, int isLogin, boolean type) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLogin = isLogin;
        this.type = type;
    }

    // Getters (optionally add setters)
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public int getIsLogin() { return isLogin; }
    public boolean isType() { return type; }
    public int getAccType() { return accType; }


    public void setAccType(int accType) {
        this.accType = accType;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}

