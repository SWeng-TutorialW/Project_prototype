package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Add_flower_event;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.hibernate.SessionFactory;

public class AddFlower_Controller {

    @FXML
    private Button add_F;

    @FXML
    private Label name_labl;

    @FXML
    private Label name_labl1;

    @FXML
    private TextField new_name;

    @FXML
    private TextField new_price;

    @FXML
    private TextField new_type;

    @FXML
    private Label type_lbl;
    
    @FXML
    private ImageView mailbox_icon;
    
    private CatalogController_employee catalogController;
    private LoginRegCheck user;

    public void setCatalogController(CatalogController_employee controller) {
        this.catalogController = controller;
        if (controller != null) {
            this.user = controller.getUser();
            // Register with EventBus to receive user updates
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            updateMailboxIcon();
        }
    }
    
    public void updateMailboxIcon() {
        if (mailbox_icon == null) {
            return;
        }
        
        if (user != null && user.isReceive_answer()) {
            mailbox_icon.setVisible(true);
        } else {
            mailbox_icon.setVisible(false);
        }
    }
    
    @Subscribe
    public void handleUserUpdate(Add_flower_event event) {
        if (event.getUser() != null && user != null && 
            event.getUser().getUsername().equals(user.getUsername())) {
            
            // Update the user object with new data from server
            this.user = event.getUser();
            
            // Update the mailbox icon
            Platform.runLater(() -> {
                updateMailboxIcon();
            });
        }
    }

    @FXML
    void add(ActionEvent event) {
        if (isTextFieldEmpty(new_name)|| isTextFieldEmpty(new_price) || isTextFieldEmpty(new_type)) {
            Warning warning = new Warning("Please fill in all the fields");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        String new_name_s = new_name.getText();
        String new_type_s = new_type.getText();
        String text = new_price.getText();
        double value;
        try {
             value = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            Warning warning = new Warning("Please Enter a valid number");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        Flower flower = new Flower(new_name_s, value, new_type_s);
        catalogController.receiveNewFlower(flower);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
    private boolean isTextFieldEmpty(TextField tf) {
        return tf.getText() == null || tf.getText().trim().isEmpty();
    }
}







