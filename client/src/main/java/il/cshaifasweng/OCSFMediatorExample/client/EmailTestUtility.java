package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Utility class for testing email functionality
 * This class provides methods to test email configuration and send test emails
 */
public class EmailTestUtility {
    
    private static boolean isRegistered = false;
    
    /**
     * Test email configuration by sending a test request to the server
     */
    public static void testEmailConfiguration() {
        if (!isRegistered) {
            EventBus.getDefault().register(new EmailTestUtility());
            isRegistered = true;
        }
        
        try {
            SimpleClient.getClient().sendToServer("testEmail");
            System.out.println("Email test request sent to server");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Email Test Error", 
                     "Failed to send email test request", 
                     "Error: " + e.getMessage());
        }
    }
    
    /**
     * Event handler for email test results
     */
    @Subscribe
    public void handleEmailTestResult(String result) {
        Platform.runLater(() -> {
            if (result.equals("Email test successful!")) {
                showAlert(AlertType.INFORMATION, "Email Test Result", 
                         "Email Configuration Test", 
                         "Email configuration is working correctly!");
            } else if (result.equals("Email test failed!")) {
                showAlert(AlertType.ERROR, "Email Test Result", 
                         "Email Configuration Test", 
                         "Email configuration test failed. Check server logs and email settings.");
            }
        });
    }
    
    /**
     * Show alert dialog
     */
    private static void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    /**
     * Cleanup method to unregister from EventBus
     */
    public static void cleanup() {
        if (isRegistered) {
            EventBus.getDefault().unregister(new EmailTestUtility());
            isRegistered = false;
        }
    }
} 