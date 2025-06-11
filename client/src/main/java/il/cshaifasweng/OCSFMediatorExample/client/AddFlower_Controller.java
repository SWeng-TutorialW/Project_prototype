package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
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
    private CatalogController_employee catalogController;

    public void setCatalogController(CatalogController_employee controller) {
        this.catalogController = controller;
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







