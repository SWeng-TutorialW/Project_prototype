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
    @FXML private ImageView mastercardLogo;
    @FXML private ImageView amexLogo;
    @FXML private Button payButton;
    @FXML private Button backButton;

    private Order order;
    private Stage checkoutStage;

    private enum CardType {
        VISA,
        MASTERCARD,
        AMEX,
        UNKNOWN
    }

    public void initialize() {
        // Load card logos
        try {
            Image visaImage = new Image(getClass().getResourceAsStream("/images/visa.png"));
            visaLogo.setImage(visaImage);
            visaLogo.setVisible(false);

            Image mastercardImage = new Image(getClass().getResourceAsStream("/images/mastercard.png"));
            mastercardLogo.setImage(mastercardImage);
            mastercardLogo.setVisible(false);

            Image amexImage = new Image(getClass().getResourceAsStream("/images/amex.png"));
            amexLogo.setImage(amexImage);
            amexLogo.setVisible(false);
        } catch (Exception e) {
            System.err.println("Failed to load card logos");
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
            // Limit to 16 digits for Visa/Mastercard, 15 for Amex
            if (newVal.length() > 16) {
                cardNumberField.setText(oldVal);
            }
            
            // Update card logo based on card number
            updateCardLogo(newVal);
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

    private CardType detectCardType(String cardNumber) {
        // Remove any spaces
        cardNumber = cardNumber.replaceAll("\\s", "");
        
        if (cardNumber.isEmpty()) {
            return CardType.UNKNOWN;
        }

        // Visa: starts with 4
        if (cardNumber.startsWith("4")) {
            return CardType.VISA;
        }
        
        // Mastercard: starts with 51-55 or 2221-2720
        if (cardNumber.matches("^5[1-5].*") || 
            (cardNumber.length() >= 4 && 
             Integer.parseInt(cardNumber.substring(0, 4)) >= 2221 && 
             Integer.parseInt(cardNumber.substring(0, 4)) <= 2720)) {
            return CardType.MASTERCARD;
        }
        
        // American Express: starts with 34 or 37
        if (cardNumber.matches("^3[47].*")) {
            return CardType.AMEX;
        }
        
        return CardType.UNKNOWN;
    }

    private void updateCardLogo(String cardNumber) {
        // Hide all logos first
        visaLogo.setVisible(false);
        mastercardLogo.setVisible(false);
        amexLogo.setVisible(false);
        
        // Show the appropriate logo based on card type
        CardType cardType = detectCardType(cardNumber);
        switch (cardType) {
            case VISA:
                visaLogo.setVisible(true);
                break;
            case MASTERCARD:
                mastercardLogo.setVisible(true);
                break;
            case AMEX:
                amexLogo.setVisible(true);
                break;
            default:
                // No logo shown for unknown card type
                break;
        }
    }

    private boolean validateCardNumber(String cardNumber) {
        // Remove any spaces
        cardNumber = cardNumber.replaceAll("\\s", "");
        
        // Validate based on card type
        CardType cardType = detectCardType(cardNumber);
        switch (cardType) {
            case VISA:
            case MASTERCARD:
                return cardNumber.matches("\\d{16}");
            case AMEX:
                return cardNumber.matches("\\d{15}");
            default:
                return false;
        }
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
            CardType cardType = detectCardType(cardNumberField.getText());
            String cardTypeStr = cardType == CardType.UNKNOWN ? "credit card" : cardType.toString().toLowerCase();
            showError("Please enter a valid " + cardTypeStr + " number");
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