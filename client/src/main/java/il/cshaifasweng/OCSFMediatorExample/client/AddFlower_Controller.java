package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private ComboBox<String> new_type;
    List<String> flowerTypes = Arrays.asList(
            "Rose",
            "Sunflower",
            "Tulip",
            "Jacarande",
            "Orchid",
            "Lily",
            "Daffodil",
            "Hyacinth",
            "Peony",
            "Marigold",
            "Anemone",
            "Dahlia",
            "Freesia",
            "Carnation",
            "Chrysanthemum",
            "Zinnia",
            "Ranunculus",
            "Snapdragon",
            "Camellia",
            "Cosmos",
            "Gerbera",
            "Iris",
            "Gladiolus",
            "Lavender",
            "Lisianthus",
            "Begonia",
            "Hibiscus",
            "Delphinium"
    );
    List<String> flowerNames;
    List<String> flowerTypesCopy = new ArrayList<>(flowerTypes);
    @FXML
    private Label type_lbl;
    private CatalogController_employee catalogController;
    private List<Flower> flowersList;
    void set_flowers_list(List<Flower> flowers)
    {
        this.flowersList = flowers;
        flowerNames = flowersList.stream()
                .map(Flower::getFlowerType)
                .collect(Collectors.toList());
        flowerTypesCopy.removeAll(flowerNames);
       new_type.getItems().setAll(flowerTypesCopy);



    }



    public void setCatalogController(CatalogController_employee controller) {
        this.catalogController = controller;
    }

    @FXML
    void add(ActionEvent event)
    {
        String new_type_s = new_type.getValue();

        if (isTextFieldEmpty(new_name)|| isTextFieldEmpty(new_price) || new_type==null) {
            Warning warning = new Warning("Please fill in all the fields");
            EventBus.getDefault().post(new WarningEvent(warning));
            return;
        }
        String new_name_s = new_name.getText();
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







