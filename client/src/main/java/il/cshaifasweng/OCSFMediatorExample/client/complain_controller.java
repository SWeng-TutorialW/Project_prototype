package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class complain_controller {
    private CatalogController catalogController;

    public void setCatalogController(CatalogController controller) {
        this.catalogController = controller;
    }

    @FXML
    private Button send;


    @FXML
    private TextField text_box;

    @FXML
    public void send_complain(ActionEvent event) throws IOException {
        String client_complaint = text_box.getText();
        if (client_complaint.isEmpty())
        {
            Warning warning = new Warning("Please enter a complaint before submitting.");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;

        }


        Complain complain = new Complain(client_complaint, SimpleClient.userData.getId()); // send the complaint with the client's ID
        SimpleClient.getClient().sendToServer(complain);
    }


    @Subscribe
    public void onComplaintSent(String msg) {
        if(msg.startsWith("#complaint_sent")) {

            // open a dialog or alert to inform the user that the complaint was sent successfully
            Platform.runLater(() -> {Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Your complaint was sent successfully.");
                alert.showAndWait();});

        }

       // catalogController.receiveNewComplain(complain); // irrelevant i think
      //  ((Stage) ((Node) event.getSource()).getScene().getWindow()).close(); // we n
    }

    @FXML
    void initialize() {

        EventBus.getDefault().register(this); // register to receive events
    }

}
