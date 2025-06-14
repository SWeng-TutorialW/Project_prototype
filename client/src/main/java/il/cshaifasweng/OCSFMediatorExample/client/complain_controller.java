package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

public class complain_controller {
    private CatalogController catalogController;
    private  CatalogController_employee cc;
    public void setCatalogController(CatalogController controller) {
        this.catalogController = controller;
    }
    public void setCatalogController(CatalogController_employee controller) {
        cc = controller;
    }
    @FXML
    private Button send;
    @FXML
    private Label head;

    public void change_head(String head) {
        this.head.setText(head);
        send.setText("Send request");
    }


    @FXML
    private TextField text_box;

    @FXML
    public void send_complain(ActionEvent event)
    {
        String client_complaint = text_box.getText();
        if (client_complaint.isEmpty())
        {
            Warning warning = new Warning("Please enter a complaint before submitting.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;

        }

        Complain complain = new Complain(client_complaint);
        if (catalogController != null) {
            catalogController.receiveNewComplain(complain);
        } else {
            cc.receiveNewComplain(complain);
        }
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();


    }


}
