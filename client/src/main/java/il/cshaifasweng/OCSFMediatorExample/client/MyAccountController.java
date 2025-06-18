package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MyAccountController {
    
    @FXML
    private Button myOrdersButton;
    
    private LoginRegCheck currentUser;
    
    public void setCurrentUser(LoginRegCheck user) {
        this.currentUser = user;
    }
    
    @FXML
    private void handleMyOrdersButton() {
        if (currentUser == null) {
            System.out.println("No user logged in");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("orders_history.fxml"));
            Parent root = loader.load();
            OrdersHistoryController ordersController = loader.getController();
            ordersController.setCurrentUser(currentUser);
            ordersController.loadUserOrders();
            
            Stage stage = new Stage();
            stage.setTitle("My Order History");
            stage.setScene(new Scene(root));
            stage.show();
            
            // Close the my account window
            ((Stage) myOrdersButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 