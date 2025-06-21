package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class EmployeeAccountController {

    @FXML
    private Label accountTypeEmptyLbl , accountTypeLbl ,emailEmptyLbl , emailLbl, myAccountLbl, usernameEmptyLbl,  usernameLbl;
    @FXML
    private AnchorPane myAccUsers, my_account_data;


    private LoginRegCheck currentUser;
    public void setCurrentUser(LoginRegCheck user) {
        this.currentUser = user;
        loadUserInfo();
    }

    public void loadUserInfo() {
        if (currentUser == null) return;

        usernameEmptyLbl.setText(currentUser.getUsername());
        emailEmptyLbl.setText(currentUser.getEmail());
        int store = currentUser.getStore();
        if(store <= 3 ){
            accountTypeEmptyLbl.setText("Store Worker - " + currentUser.getStoreName());
        }
        if(store == 4){
            accountTypeEmptyLbl.setText("Manager");
        }


    }

}
