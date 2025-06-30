package il.cshaifasweng.OCSFMediatorExample.client;



import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.change_sendOrRecieve_messages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class AnswerScene {
    private Complain complain;
    private LoginRegCheck user;
    public void set_user(LoginRegCheck user) {
        this.user = user;
    }

    public void setComplain(Complain complain) {
        this.complain = complain;
        answer_box.setText(complain.getComplaint());
    }


    @FXML
    private TextArea answer_box;

    @FXML
    private Button btn;

    @FXML
    private Label head;

    @FXML
    void finish_request(ActionEvent event)throws IOException
    {
        change_sendOrRecieve_messages wrapper = new change_sendOrRecieve_messages(user, false,false);
        SimpleClient.getClient().sendToServer(wrapper);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

    }

}
