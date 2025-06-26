package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

public class bouquet_controller {

    @FXML
    private ComboBox<String> flower_num;
    private CatalogController catalogController;

    public void setCatalogController(CatalogController controller) {
        this.catalogController = controller;
    }
    private List<Flower> flowersList_c;
    List<String> flowerNames;
    public void setFlowersList(List<Flower> flowersList)
    {
        this.flowersList_c = flowersList;
         flowerNames = flowersList.stream()
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
    private ComboBox<String>[] flowerBoxes;

    @FXML
    private Label price;
    int flowers_number=-1;

    @FXML
    void initialize() {

        flower_num.getItems().addAll("2", "3","4","5");
        flowerBoxes = new ComboBox[]{flower_num1, flower_num2, flower_num3, flower_num4, flower_num5};


    }





    @FXML
    void flowers_num(ActionEvent event)
    {
        String selected = flower_num.getValue();
        flowers_number=Integer.parseInt(selected);



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
    boolean[] used = new boolean[5];
    String[] names = new String[5];
    List<String> choosen_flowers = new ArrayList<>();
    private boolean isUpdating = false;


    @FXML
    void calc(ActionEvent event) {
        if (isUpdating) return;

        String selectedNumStr = flower_num.getValue();
        int selectedNum = Integer.parseInt(selectedNumStr);
        double totalPrice = 0.0;

        ComboBox<String> source = (ComboBox<String>) event.getSource();
        String sourceId = source.getId();
        String selectedFlower = source.getValue();
        if (selectedFlower == null) return;

        ComboBox<String>[] boxes = new ComboBox[]{flower_num1, flower_num2, flower_num3, flower_num4, flower_num5};


        int sourceIndex = -1;
        for (int i = 0; i < boxes.length; i++)
        {
            if (boxes[i].getId().equals(sourceId))
            {
                if(used[i])
                {
                    choosen_flowers.remove(names[i]);
                }
                used[i]=true;
                names[i]=selectedFlower;
                choosen_flowers.add(selectedFlower);
                sourceIndex = i;
                break;
            }
        }
        System.out.println("used array:");
        for (int i = 0; i < used.length; i++) {
            System.out.println("used[" + i + "] = " + used[i]);
        }

        System.out.println("choosen");
        for (String flower : choosen_flowers) {
            System.out.println(flower);
        }
        for (int i = 0; i < 5; i++)
        {
            isUpdating = true;
            if (i != sourceIndex )
            {
                if(used[i])
                {
                    System.out.println("inside used box "+i);


                    String previousSelection = boxes[i].getValue();
                    List<String> flowerNames_modifi = new ArrayList<>(flowerNames);
                    flowerNames_modifi.removeAll(choosen_flowers);
                    flowerNames_modifi.add(previousSelection);
                    System.out.println("modified");
                    for (String flower :flowerNames_modifi) {
                        System.out.println(flower);
                    }
                    boxes[i].getItems().setAll(flowerNames_modifi);
                    boxes[i].setValue(previousSelection);
                    System.out.println("setvalueto"+previousSelection);
                    if(i==1)
                    {
                        System.out.println("modified");
                    }
                    continue;
                }
                List<String> flowerNames_modifi = new ArrayList<>(flowerNames);
                flowerNames_modifi.removeAll(choosen_flowers);
                boxes[i].getItems().setAll(flowerNames_modifi);
                System.out.println("modified");
                for (String flower :flowerNames_modifi) {
                    System.out.println(flower);
                }

            }

        }
        isUpdating = false;


        for (int i = 0; i < selectedNum; i++)
        {
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

