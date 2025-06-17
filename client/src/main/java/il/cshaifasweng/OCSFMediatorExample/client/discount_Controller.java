
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

import java.util.List;

public class discount_Controller {
    private CatalogController_employee catalogController;
    public void setCatalogController(CatalogController_employee controller) {
        this.catalogController = controller;
    }

    @FXML
    private Button discount_to_ALL;

    @FXML
    private Button discount_to_specip_flower;

    @FXML
    private TextField flower_name;

    @FXML
    private Label name_labl;

    @FXML
    private Label name_labl1;

    @FXML
    private Label name_labl11;

    @FXML
    private Label name_labl2;

    @FXML
    private Label name_labl21;

    @FXML
    private TextField new_price_box;

    @FXML
    private TextField percent_for_all;

    @FXML
    private TextField percent_for_one;

    @FXML
    private Button specip_price;
    private List<Flower> flowersList_c;
    public void setFlowersList_c(List<Flower> arr) {
        flowersList_c = arr;
    }

    @FXML
    void all(ActionEvent event)
    {
        String text = percent_for_all.getText().trim();
        if (text.isEmpty()) {
            Warning warning = new Warning("Please fill in the discount field");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        try {
            int percent = Integer.parseInt(text);
            if (percent < 1 || percent > 99) {
                Warning warning = new Warning("Discount must be a number between 1 and 99");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }
        } catch (NumberFormatException e) {
            Warning warning = new Warning("Discount must be a valid number");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        String discountStr = percent_for_all.getText();
        int percent_discount = Integer.parseInt(discountStr);
        catalogController.receivediscount(percent_discount,null);
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

    }

    @FXML
    void new_price(ActionEvent event) {
        String text = new_price_box.getText().trim();
        String name=flower_name.getText().trim();
        if (name.isEmpty()) {
            Warning warning = new Warning("Please fill in the name field");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        if (text.isEmpty()) {
            Warning warning = new Warning("Please fill in the price field");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }

        try {
            double price = Double.parseDouble(text);
            if (price <= 0) {
                Warning warning = new Warning("Price must be a positive number");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }
            System.out.println("Valid price: " + price);
        } catch (NumberFormatException e) {
            Warning warning = new Warning("Price must be a valid number");
            EventBus.getDefault().post(new WarningEvent(warning));
        }
        String str_flower = flower_name.getText().trim();
        boolean found = false;
        Flower targetFlower = null;
        for (Flower flower : flowersList_c) {
            if (flower.getFlowerName().equalsIgnoreCase(str_flower)) {
                found = true;
                targetFlower = flower;
                break;
            }
        }
        if (!found)
        {
            Warning warning = new Warning("Flower not found");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        else
        {
            double new_price = Double.parseDouble(text);
            System.out.println(new_price);
            targetFlower.setFlowerPrice(new_price);
            int percent_discount = -2;
            catalogController.receivediscount(percent_discount,targetFlower);
        }
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

    }

    @FXML
    void one_flower(ActionEvent event)
    {
        String text = percent_for_one.getText().trim();
        if (text.isEmpty()) {
            Warning warning = new Warning("Please fill in the discount field");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        try {
            int percent = Integer.parseInt(text);
            if (percent < 1 || percent > 99) {
                Warning warning = new Warning("Discount must be a number between 1 and 99");
                EventBus.getDefault().post(new WarningEvent(warning));
                return;
            }
        } catch (NumberFormatException e) {
            Warning warning = new Warning("Discount must be a valid number");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        String str_flower = flower_name.getText().trim();
        boolean found = false;
        Flower targetFlower = null;
        for (Flower flower : flowersList_c) {
            if (flower.getFlowerName().equalsIgnoreCase(str_flower)) {
                found = true;
                targetFlower = flower;
                break;
            }
        }
        if (!found)
        {
            Warning warning = new Warning("Flower not found");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        else
        {
            String discountStr = percent_for_one.getText();
            int percent_discount = Integer.parseInt(discountStr);
            catalogController.receivediscount(percent_discount,targetFlower);
        }
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();


    }
    private boolean isTextFieldEmpty(TextField tf) {
        return tf.getText() == null || tf.getText().trim().isEmpty();
    }

}
