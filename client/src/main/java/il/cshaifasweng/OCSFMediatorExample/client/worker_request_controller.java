package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

public class worker_request_controller {
    private CatalogController_employee cc;
    private LoginRegCheck currentUser;
    
    @FXML private Button send;
    @FXML private Label head;
    @FXML private TextField text_box;
    
    public void setCatalogController(CatalogController_employee controller) {
        cc = controller;
        if (controller != null) {
            this.currentUser = controller.getUser();
        }
    }

    public void change_head(String head) {
        this.head.setText(head);
        send.setText("Send request");
    }

    @FXML
    public void send_request(ActionEvent event) {
        String request_text = text_box.getText();
        if (request_text.isEmpty()) {
            Warning warning = new Warning("Please enter a request before submitting.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        // Create complaint without order (for worker requests)
        Complain complain = new Complain(request_text);
        if (currentUser != null) {
            complain.setClient(currentUser.getUsername());
            // Set store ID for report filtering
            complain.setStoreId(currentUser.getStore());
        }
        
        if (cc != null) {
            cc.receiveNewComplain(complain);
        }
        
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
} 