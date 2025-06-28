package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import il.cshaifasweng.OCSFMediatorExample.entities.FlowerDiscountWrapper;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class delete_controller  {

    @FXML
    private Button deleteButton;

    @FXML
    private ImageView flowerImage;

    @FXML
    private Label flowerName;

    @FXML
    private Label flowerPrice;
    public int  type=0;
    public void set_type(int type){this.type=type;}

    @FXML
    private Label flowerType;
    private Flower selectedFlower;
    public void setFlower(Flower flower) {
        this.selectedFlower = flower;
        updateUI();
    }

    private void updateUI() {
        if (selectedFlower != null) {
            flowerName.setText(selectedFlower.getFlowerName());
            flowerType.setText(selectedFlower.getFlowerType());
            flowerPrice.setText(String.format("Price: $%.2f", selectedFlower.getFlowerPrice()));

            // Set flower image
            try {
                String imagePath = "/images/" + selectedFlower.getFlowerType() + ".png";
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                flowerImage.setImage(image);
            } catch (Exception e) {
                System.err.println("Failed to load image for: " + selectedFlower.getFlowerType());
                try {
                    String imagePath = "/images/no_photo.png";
                    Image image = new Image(getClass().getResourceAsStream(imagePath));
                    flowerImage.setImage(image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @FXML
    void Delete(ActionEvent event)
    {
        System.out.println(type);
        if(type!=4)
        {
            Warning warning = new Warning("You are not allowed to delete flower please send request to  the administrator.");
            EventBus.getDefault().post(new WarningEvent(warning));
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            return;
        }

        FlowerDiscountWrapper wrapper = new FlowerDiscountWrapper(selectedFlower, -1);
        try {
            SimpleClient.getClient().sendToServer(wrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

    }
}
