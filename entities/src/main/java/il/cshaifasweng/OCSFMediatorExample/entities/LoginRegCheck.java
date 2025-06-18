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
    private boolean type;// false = client, true = employee
    private int store;// 1 for store number 1 ,until 3 ,
    // if the value is 4 is for all the stores -workes must be between 1-2-3
    // worker with value 4 is the מנהל רשת
    //5 is only for users and mean that the user is מנוי שנתי

    @Column(name = "userIdNum")
    private String idNum; // only if accType == 2

    @Column(name = "creditCard")
    private String creditCard; // only if accType == 2

    @Column(name = "phoneNum")
    private String phoneNum;

    @Column(name = "fullName")
    private String fullName;


    boolean is_yearly_subscription=false;
    boolean send_complain=false;
    boolean receive_answer=false;

    public LoginRegCheck() {}

    public LoginRegCheck(String username, String password, String email, int isLogin, boolean type, int store) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLogin = isLogin;
        this.type = type;
        this.store = store;
    }
    public LoginRegCheck(String username, String password, String email, int isLogin, boolean type, int store, String phoneNum, String fullName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLogin = isLogin;
        this.type = type;
        this.store = store;
        this.phoneNum = phoneNum;
        this.fullName = fullName;
    }
    public LoginRegCheck(String username, String password, String email, int isLogin, boolean type, int store, String phoneNum, String fullName, String idNum, String creditCard, boolean is_yearly_subscription) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLogin = isLogin;
        this.type = type;
        this.store = store;
        this.phoneNum = phoneNum;
        this.fullName = fullName;
        this.idNum = idNum;
        this.creditCard = creditCard;
        this.is_yearly_subscription = is_yearly_subscription;
    }
    public LoginRegCheck(String username, String password, String email, int isLogin, boolean type, int store,  boolean is_yearly_subscription) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isLogin = isLogin;
        this.type = type;
        this.store = store;
        this.phoneNum = phoneNum;
        this.fullName = fullName;
        this.idNum = idNum;
        this.creditCard = creditCard;
        this.is_yearly_subscription = is_yearly_subscription;
    }
    public Integer getStore() {return store;}

    // Getters (optionally add setters)
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public int getIsLogin() { return isLogin; }
    public boolean isType() { return type; }

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
    public void setIsLogin(int isLogin) {
        this.isLogin = isLogin;
    }
    public boolean is_yearly_subscription() {
        return is_yearly_subscription;
    }
    public void set_send(boolean send_complain)
    {
        this.send_complain=send_complain;
    }
    public boolean get_send_complain()
    {
        return send_complain;
    }
    public void set_receive_answer(boolean receive_answer)
    {
        this.receive_answer=receive_answer;
    }
    public boolean isReceive_answer()
    {
        return receive_answer;
    }
}