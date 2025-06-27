package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class bouquet_controller {

    @FXML
    private ComboBox<String> flower_num;
    private CatalogController catalogController;

    public void setCatalogController(CatalogController controller) {
        this.catalogController = controller;
    }
    private List<Flower> flowersList_c;
    public void setFlowersList(List<Flower> flowersList)
    {
        this.flowersList_c = flowersList;
        List<String> flowerNames = flowersList.stream()
                .map(Flower::getFlowerName)
                .collect(Collectors.toList());



        flower_num1.getItems().setAll(flowerNames);
        flower_num2.getItems().setAll(flowerNames);
        flower_num3.getItems().setAll(flowerNames);
        flower_num4.getItems().setAll(flowerNames);
        flower_num5.getItems().setAll(flowerNames);
    }
    @FXML
    private Label f_1;

    @FXML
    private Label f_2;

    @FXML
    private Label f_3;

    @FXML
    private Label f_4;

    @FXML
    private Label f_5;


    @FXML
    private ComboBox<String> flower_num1;

    @FXML
    private ComboBox<String> flower_num2;

    @FXML
    private ComboBox<String> flower_num3;

    @FXML
    private ComboBox<String> flower_num4;

    @FXML
    private ComboBox<String> flower_num5;

    @FXML
    private Label name_labl;
    @FXML
    private TextField calc_price;

    @FXML
    private Label price;
    @FXML
    void initialize() {

        flower_num.getItems().addAll("2", "3","4","5");
    }
    @FXML
    void flowers_num(ActionEvent event)
    {
        String selected = flower_num.getValue();

        if (selected.equals("2"))
        {
            f_1.setVisible(true);
            f_2.setVisible(true);
            f_3.setVisible(false);
            f_4.setVisible(false);
            f_5.setVisible(false);
            flower_num1.setVisible(true);
            flower_num2.setVisible(true);
            flower_num3.setVisible(false);
            flower_num4.setVisible(false);
            flower_num5.setVisible(false);

        }
        else if (selected.equals("3"))
        {
            f_1.setVisible(true);
            f_2.setVisible(true);
            f_3.setVisible(true);
            f_4.setVisible(false);
            f_5.setVisible(false);
            flower_num1.setVisible(true);
            flower_num2.setVisible(true);
            flower_num3.setVisible(true);
            flower_num4.setVisible(false);
            flower_num5.setVisible(false);


        }
        else if (selected.equals("4"))
        {
            f_1.setVisible(true);
            f_2.setVisible(true);
            f_3.setVisible(true);
            f_4.setVisible(true);
            f_5.setVisible(false);
            flower_num1.setVisible(true);
            flower_num2.setVisible(true);
            flower_num3.setVisible(true);
            flower_num4.setVisible(true);
            flower_num5.setVisible(false);

        }
        else if (selected.equals("5"))
        {
            f_1.setVisible(true);
            f_2.setVisible(true);
            f_3.setVisible(true);
            f_4.setVisible(true);
            f_5.setVisible(true);
            flower_num1.setVisible(true);
            flower_num2.setVisible(true);
            flower_num3.setVisible(true);
            flower_num4.setVisible(true);
            flower_num5.setVisible(true);

        }

    }
    @FXML
    void calc(ActionEvent event) {
        String selectedNumStr = flower_num.getValue();
        int selectedNum = Integer.parseInt(selectedNumStr);
        double totalPrice = 0.0;


        ComboBox<String>[] boxes = new ComboBox[]{flower_num1, flower_num2, flower_num3, flower_num4, flower_num5};

        for (int i = 0; i < selectedNum; i++) {
            String flowerName = boxes[i].getValue();

            if (flowerName == null) {

                calc_price.setText("");
                return;
            }

            Flower flower = flowersList_c.stream()
                    .filter(f -> f.getFlowerName().equals(flowerName))
                    .findFirst()
                    .orElse(null);

            if (flower == null) {
                calc_price.setText("Flower " + flowerName + " not found.");
                return;
            }

            totalPrice += flower.getFlowerPrice() / selectedNum;
        }

        calc_price.setText(String.format("%.2f", totalPrice));
    }


}

