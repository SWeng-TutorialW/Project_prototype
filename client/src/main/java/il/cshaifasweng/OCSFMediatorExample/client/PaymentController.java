package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Order;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.time.YearMonth;
import java.util.Calendar;

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
    @FXML private HBox cardNumberBox;
    @FXML private ImageView cardLogoInField;

    private Image visaImage;
    private Image mastercardImage;
    private Image amexImage;

    private Order order;
    private Stage checkoutStage;

    private enum CardType {
        VISA,
        MASTERCARD,
        AMEX,
        UNKNOWN
    }

    public void initialize() {
        try {
            visaImage = new Image(getClass().getResourceAsStream("/images/visa.png"));
            mastercardImage = new Image(getClass().getResourceAsStream("/images/mastercard.png"));
            amexImage = new Image(getClass().getResourceAsStream("/images/amex.png"));

            visaLogo.setImage(visaImage);
            mastercardLogo.setImage(mastercardImage);
            amexLogo.setImage(amexImage);

            visaLogo.setVisible(false);
            mastercardLogo.setVisible(false);
            amexLogo.setVisible(false);
        } catch (Exception e) {
            System.err.println("Failed to load card logos: " + e.getMessage());
        }

        for (int i = 1; i <= 12; i++) {
            expiryMonthComboBox.getItems().add(String.format("%02d", i));
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i <= currentYear + 10; i++) {
            expiryYearComboBox.getItems().add(String.valueOf(i));
        }

        cardNumberField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                cardNumberField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            if (newVal.length() > 16) {
                cardNumberField.setText(oldVal);
            }
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
        cardNumber = cardNumber.replaceAll("\\s", "");
        if (cardNumber.isEmpty()) return CardType.UNKNOWN;

        if (cardNumber.startsWith("4")) return CardType.VISA;
        if (cardNumber.matches("^5[1-5].*") ||
                (cardNumber.length() >= 4 &&
                        Integer.parseInt(cardNumber.substring(0, 4)) >= 2221 &&
                        Integer.parseInt(cardNumber.substring(0, 4)) <= 2720)) {
            return CardType.MASTERCARD;
        }
        if (cardNumber.matches("^3[47].*")) return CardType.AMEX;
        return CardType.UNKNOWN;
    }

    private void updateCardLogo(String cardNumber) {
        visaLogo.setVisible(false);
        mastercardLogo.setVisible(false);
        amexLogo.setVisible(false);
        cardLogoInField.setVisible(false);

        CardType cardType = detectCardType(cardNumber);
        switch (cardType) {
            case VISA:
                visaLogo.setVisible(true);
                cardLogoInField.setImage(visaImage);
                cardLogoInField.setVisible(true);
                break;
            case MASTERCARD:
                mastercardLogo.setVisible(true);
                cardLogoInField.setImage(mastercardImage);
                cardLogoInField.setVisible(true);
                break;
            case AMEX:
                amexLogo.setVisible(true);
                cardLogoInField.setImage(amexImage);
                cardLogoInField.setVisible(true);
                break;
            default:
                break;
        }
    }
    private boolean validateCardNumber(String cardNumber) {
        cardNumber = cardNumber.replaceAll("\\s", "");
        CardType cardType = detectCardType(cardNumber);
        return switch (cardType) {
            case VISA, MASTERCARD -> cardNumber.matches("\\d{16}");
            case AMEX -> cardNumber.matches("\\d{15}");
            default -> false;
        };
    }

    private boolean validateExpiryDate() {
        if (expiryMonthComboBox.getValue() == null || expiryYearComboBox.getValue() == null) {
            return false;
        }
        int month = Integer.parseInt(expiryMonthComboBox.getValue());
        int year = Integer.parseInt(expiryYearComboBox.getValue());
        return !YearMonth.of(year, month).isBefore(YearMonth.now());
    }

    private boolean validateCVV(String cvv) {
        return cvv != null && cvv.matches("\\d{3}");
    }

    private boolean validateCardholderName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("^[a-zA-Z ]+$");
    }

    @FXML
    private void processPayment() {
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
            Thread.sleep(1000); // simulate payment

            order.setStatus("CONFIRMED");
            SimpleClient.getClient().sendToServer(order);

            Warning warning = new Warning("Payment successful! Your order has been placed.");
            EventBus.getDefault().post(new WarningEvent(warning));

            OrderPageController.clearCart();
            ((Stage) payButton.getScene().getWindow()).close();
            if (checkoutStage != null) checkoutStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Payment processing failed. Please try again.");
        }
    }

    @FXML
    private void goBack() {
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
 