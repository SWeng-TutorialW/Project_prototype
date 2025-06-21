package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GreetingCardPreviewController {

    @FXML
    private StackPane greetingCardContainer; // שונה מ־VBox ל־StackPane

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private Label messageLabel;

    @FXML
    private Button closeButton;

    @FXML
    private Label backgroundThemeLabel;

    private String backgroundTheme;
    private String message;

    public void initialize() {
        // סוגר את התצוגה
        closeButton.setOnAction(event -> {
            ((Stage) closeButton.getScene().getWindow()).close();
        });
    }

    public void setGreetingCardData(String backgroundTheme, String message) {
        this.backgroundTheme = backgroundTheme;
        this.message = message;
        updatePreview();
    }

    private void updatePreview() {
        // עדכון כותרת
        backgroundThemeLabel.setText("Theme: " + backgroundTheme);

        // קביעת תמונת רקע
        String imagePath = getImagePathForTheme(backgroundTheme);
        try {
            Image backgroundImage = new Image(getClass().getResourceAsStream(imagePath));
            if (backgroundImage.isError()) {
                throw new Exception("Image failed to load");
            }
            backgroundImageView.setImage(backgroundImage);
            backgroundImageView.setFitWidth(400);
            backgroundImageView.setFitHeight(300);
            backgroundImageView.setPreserveRatio(false);
            backgroundImageView.setVisible(true);
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + imagePath);
            System.err.println("Error: " + e.getMessage());
            greetingCardContainer.setStyle("-fx-background-color: linear-gradient(to bottom right, #f0f0f0, #e0e0e0); -fx-border-color: #B79DD9; -fx-border-width: 2;");
            backgroundImageView.setVisible(false);
        }

        // עדכון טקסט
        messageLabel.setText(message);
        messageLabel.setWrapText(true);
        messageLabel.setFont(Font.font("Arial", 16));
    }

    private String getImagePathForTheme(String theme) {
        // התאמה בין שמות הרקעים לקבצים
        return switch (theme) {
            case "Background 1" -> "/images/greeting_cards/bg1.jpg";
            case "Background 2" -> "/images/greeting_cards/bg2.jpg";
            case "Background 3" -> "/images/greeting_cards/bg3.jpg";
            case "Background 4" -> "/images/greeting_cards/bg4.jpg";
            default -> "/images/greeting_cards/bg1.jpg";
        };
    }
}
