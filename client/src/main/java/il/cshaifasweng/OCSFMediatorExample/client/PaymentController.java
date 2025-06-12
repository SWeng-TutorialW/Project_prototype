package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView; 
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.regex.Pattern;

public class PaymentController {
    @FXML private TextField cardNumberField;
    @FXML private TextField cardholderNameField;
    @FXML private ComboBox<String> expiryMonthComboBox;
    @FXML private ComboBox<String> expiryYearComboBox;
    @FXML private PasswordField cvvField;
    @FXML private Label totalAmountLabel;
    @FXML private ImageView visaLogo;
    @FXML private Button payButton;
    @FXML private Button backButton;

    private Order order;
    private Stage checkoutStage;

    public void initialize() {
        // Load Visa logo
        try {
            Image visaImage = new Image(getClass().getResourceAsStream("/images/visa.png"));
            visaLogo.setImage(visaImage);
        } catch (Exception e) {
            System.err.println("Failed to load Visa logo");
        }

        // Initialize expiry month combo box
        for (int i = 1; i <= 12; i++) {
            expiryMonthComboBox.getItems().add(String.format("%02d", i));
        }

        // Initialize expiry year combo box (current year + 10 years)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i <= currentYear + 10; i++) {
            expiryYearComboBox.getItems().add(String.valueOf(i));
        }

        // Add input validation listener for card number
        cardNumberField.textProperty().addListener((obs, oldVal, newVal) -> {
            // Only allow digits
            if (!newVal.matches("\\d*")) {
                cardNumberField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            // Limit to 16 digits
            if (newVal.length() > 16) {
                cardNumberField.setText(oldVal);
            }
        });

        cvvField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                cvvField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            if (newVal.length() > 3) {
                cvvField.setText(oldVal);
            }
        });
    }

    public void setOrder(Order order) {
        this.order = order;
        updateTotalAmount();
    }

    public void setCheckoutStage(Stage stage) {
        this.checkoutStage = stage;
    }

    private void updateTotalAmount() {
        if (order != null) {
            totalAmountLabel.setText(String.format("Total Amount: ₪%.2f", order.getTotalAmount()));
        }
    }

    private boolean validateCardNumber(String cardNumber) {
        // Remove any spaces
        cardNumber = cardNumber.replaceAll("\\s", "");
        // Check if it's exactly 16 digits
        return cardNumber.matches("\\d{16}");
    }

    private boolean validateExpiryDate() {
        if (expiryMonthComboBox.getValue() == null || expiryYearComboBox.getValue() == null) {
            return false;
        }

        int month = Integer.parseInt(expiryMonthComboBox.getValue());
        int year = Integer.parseInt(expiryYearComboBox.getValue());
        YearMonth expiry = YearMonth.of(year, month);
        return !expiry.isBefore(YearMonth.now());
    }

    private boolean validateCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }

    private boolean validateCardholderName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("^[a-zA-Z ]+$");
    }

    @FXML
    private void processPayment() {
        // Validate all fields
        if (!validateCardholderName(cardholderNameField.getText())) {
            showError("Please enter a valid cardholder name");
            return;
        }

        if (!validateCardNumber(cardNumberField.getText())) {
            showError("Please enter a valid Visa card number");
            return;
        }

        if (!validateExpiryDate()) {
            showError("Please enter a valid expiry date");
            return;
        }

        if (!validateCVV(cvvField.getText())) {
            showError("Please enter a valid CVV");
            return;
        }


        try {
            // Simulate payment processing
            Thread.sleep(1000);

            // Set order status to CONFIRMED
            order.setStatus("CONFIRMED");

            // Send order to server
            SimpleClient.getClient().sendToServer(order);

            // Show success message
            Warning warning = new Warning("Payment successful! Your order has been placed.");
            EventBus.getDefault().post(new WarningEvent(warning));

            // Clear cart and close windows
            OrderPageController.clearCart();
            ((Stage) payButton.getScene().getWindow()).close();
            if (checkoutStage != null) {
                checkoutStage.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Payment processing failed. Please try again.");
        }
    }

    @FXML
    private void goBack() {
        // Close payment window and show checkout window
        ((Stage) backButton.getScene().getWindow()).close();
        if (checkoutStage != null) {
            checkoutStage.show();
        }
    }

    private void showError(String message) {
        Warning warning = new Warning(message);
        EventBus.getDefault().post(new WarningEvent(warning));
    }
} 