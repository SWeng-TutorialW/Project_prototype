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

    @Column(name = "type")
    private boolean type; // false = client, true = employee

    @Column(name = "accType")
    private int accType;     // 0 - Network, 1 - Chain, 2 - Subscribed

    @Column(name = "userIdNum")
    private String idNum; // only if accType == 2

    @Column(name = "creditCard")
    private String creditCard; // only if accType == 2

    @Column(name = "store")     //only if accType == 0
    private String store;

    @Column(name = "isLogin")
    private int isLogin; // 1 = login, 0 = registration

    public LoginRegCheck() {}


    //for sub' acc
    public LoginRegCheck(String username, String password, String email, int isLogin, int accType, String idNum, String creditCard) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLogin = isLogin;
        this.type = false;
        this.accType = accType;
        this.idNum = idNum;
        this.creditCard = creditCard;
    }

    //for store acc
    public LoginRegCheck(String username, String password, String email, int isLogin, int accType, String store) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLogin = isLogin;
        this.type = false;
        this.accType = accType;
        this.store = store ;
    }

    //for chain acc
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
    public String getCreditCard() {return creditCard;}
    public String getIdNum() {return idNum;}

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

