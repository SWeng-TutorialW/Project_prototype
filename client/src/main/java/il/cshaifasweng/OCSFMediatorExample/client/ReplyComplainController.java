package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.entities.change_sendOrRecieve_messages;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class ReplyComplainController {

        @FXML
        private TextArea replyArea;

        @FXML
        private CheckBox refundCheckBox;

        @FXML
        private TextField moneyTf;
        
        @FXML
        private Label titleLabel;

    private Complain originalComplain;
    private boolean isWorkerRequest = false;


        public void setComplain(Complain complain) {
            this.originalComplain = complain;
            
            // Check if this is a worker request (no order ID in complaint text)
            String complaintText = complain.getComplaint();
            if (complaintText != null && !complaintText.contains("Order #")) {
                isWorkerRequest = true;
                titleLabel.setText("Reply to Request:");
                refundCheckBox.setVisible(false);
                refundCheckBox.setManaged(false);
                moneyTf.setVisible(false);
                moneyTf.setManaged(false);
            } else {
                titleLabel.setText("Reply to Complaint:");
                refundCheckBox.setVisible(true);
                refundCheckBox.setManaged(true);
                moneyTf.setVisible(true);
                moneyTf.setManaged(true);
            }
        }


    @FXML
    public void initialize() {
        moneyTf.setDisable(true);
        moneyTf.setManaged(false);
        refundCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            moneyTf.setDisable(!newValue);
            moneyTf.setManaged(newValue);
            if(!newValue){
                moneyTf.setText("");
            }
            else{
                moneyTf.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue,
                                        String newValue) {
                        if (!newValue.matches("\\d*")) {
                            moneyTf.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                    }
                });
            }
        });
    }


    @FXML
    void sendReply(ActionEvent event) {
        String replyText = replyArea.getText();
        String refundAmount = moneyTf.getText();
    
        if (replyText == null || replyText.isBlank()) return;
    
        try {
            // Create a new Complain object instead of updating the old one
            Complain newComplain = new Complain();
            newComplain.setTimestamp(java.time.LocalDateTime.now());
    
            String userToBeAnswered = originalComplain.getClient();
            newComplain.setClient("answer to" + userToBeAnswered);
    
            if (!isWorkerRequest && refundCheckBox.isSelected()) {
                if (refundAmount == null || refundAmount.isBlank()) {
                    Warning warning = new Warning("Enter money amount to refund or disable option");
                    EventBus.getDefault().post(new WarningEvent(warning));
                    return;
                }
    
                double refundValue = Double.parseDouble(refundAmount);
                double orderPrice = extractOrderPrice(originalComplain.getComplaint());
    
                if (orderPrice > 0 && refundValue > orderPrice) {
                    Warning warning = new Warning("Cannot refund more than order price ($" + String.format("%.2f", orderPrice) + ")");
                    EventBus.getDefault().post(new WarningEvent(warning));
                    return;
                }
    
                replyText = replyText.concat("\nWe have refunded your account with ").concat(refundAmount).concat(" $");
                newComplain.setRefundAmount(refundValue);
            }
    
            newComplain.setComplaint(replyText);
            newComplain.setOrder(originalComplain.getOrder()); // Optional, keep order context if needed
    
            // Send new complaint + wrapper to server
            change_sendOrRecieve_messages wrapper = new change_sendOrRecieve_messages(userToBeAnswered, true, true);
            SimpleClient.getClient().sendToServer(newComplain);
            SimpleClient.getClient().sendToServer(wrapper);
            SimpleClient.getClient().sendToServer("getComplaints");
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
    
        
        private double extractOrderPrice(String complaintText) {
            if (complaintText == null) return 0.0;
            
            // Look for "Price: $123.45" pattern
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("Price: \\$([\\d.]+)");
            java.util.regex.Matcher matcher = pattern.matcher(complaintText);
            
            if (matcher.find()) {
                try {
                    return Double.parseDouble(matcher.group(1));
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
            
            return 0.0;
        }
    }


