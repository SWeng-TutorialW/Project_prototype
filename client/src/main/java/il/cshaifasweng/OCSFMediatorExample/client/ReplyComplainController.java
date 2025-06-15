package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.change_sendOrRecieve_messages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class ReplyComplainController {

        @FXML
        private TextArea replyArea;

        private Complain originalComplain;

        public void setComplain(Complain complain) {
            this.originalComplain = complain;
        }


        @FXML
        void sendReply(ActionEvent event)
        {
            String replyText = replyArea.getText();
            if (replyText == null || replyText.isBlank()) return;

            try {
                originalComplain.setComplaint(replyText);
                String user_to_be_answerd= originalComplain.getClient();
                String new_name="answer to"+user_to_be_answerd;
                originalComplain.setClient(new_name);
                change_sendOrRecieve_messages wrapper = new change_sendOrRecieve_messages(user_to_be_answerd, true,true);
                SimpleClient.getClient().sendToServer(originalComplain);
                SimpleClient.getClient().sendToServer(wrapper);
                SimpleClient.getClient().sendToServer("getComplaints");
            } catch (IOException e) {
                e.printStackTrace();
            }


            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        }
    }


