package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Complain;
import il.cshaifasweng.OCSFMediatorExample.entities.ComplainUpdateEvent;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyComplaintsController {

    @FXML
    private TableView<ComplaintRow> complaintsTable;

    @FXML
    private TableColumn<ComplaintRow, String> dateCol;

    @FXML
    private TableColumn<ComplaintRow, String> typeCol;

    @FXML
    private TableColumn<ComplaintRow, String> complaintCol;

    @FXML
    private TableColumn<ComplaintRow, String> responseCol;

    @FXML
    private TableColumn<ComplaintRow, String> refundCol;

    @FXML
    private Label statusLabel;

    private LoginRegCheck currentUser;
    private ObservableList<ComplaintRow> complaintsData = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        // Register to EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        // Setup table columns
        setupTableColumns();
        
        // Load complaints
        loadComplaints();
    }

    private void setupTableColumns() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        complaintCol.setCellValueFactory(new PropertyValueFactory<>("complaint"));
        responseCol.setCellValueFactory(new PropertyValueFactory<>("response"));
        refundCol.setCellValueFactory(new PropertyValueFactory<>("refund"));

        // Set column widths
        dateCol.setPrefWidth(120);
        typeCol.setPrefWidth(100);
        complaintCol.setPrefWidth(200);
        responseCol.setPrefWidth(200);
        refundCol.setPrefWidth(100);

        // Enable text wrapping for complaint and response columns
        complaintCol.setStyle("-fx-alignment: TOP_LEFT;");
        responseCol.setStyle("-fx-alignment: TOP_LEFT;");
    }

    private void loadComplaints() {
        if (currentUser == null) {
            statusLabel.setText("No user logged in");
            return;
        }

        statusLabel.setText("Loading complaints...");
        
        try {
            SimpleClient.getClient().sendToServer("getComplaints");
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Error loading complaints: " + e.getMessage());
        }
    }

    @Subscribe
    public void handleComplaintUpdate(ComplainUpdateEvent event) {
        if (event.getUpdatedItems() == null) {
            Platform.runLater(() -> statusLabel.setText("No complaints found"));
            return;
        }

        // Filter complaints for current user and answers to them
        List<Complain> userComplaints = event.getUpdatedItems().stream()
            .filter(c -> c != null && (currentUser.getUsername().equals(c.getClient()) ||
                (c.getComplaint() != null && c.getComplaint().startsWith("answer to" + currentUser.getUsername())))) //TODO: maybe add a space
            .collect(Collectors.toList());

        // Create complaint rows
        List<ComplaintRow> rows = new ArrayList<>();
        
        for (Complain complaint : userComplaints) {
            String date = complaint.getTimestamp() != null ? 
                complaint.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A";
            
            String type = categorizeComplaint(complaint.getComplaint());
            
            String complaintText = complaint.getComplaint() != null ? complaint.getComplaint() : "";
            
            // Check if this is a response (starts with "answer to <username>")
            String response = "";
            if (complaintText.startsWith("answer to")) {
                response = complaintText.substring(("answer to"+currentUser.getUsername()).length()).trim();
                complaintText = "Response received";
            }
            
            String refund = complaint.getRefundAmount() > 0 ? 
                String.format("₪%.2f", complaint.getRefundAmount()) : "";
            
            rows.add(new ComplaintRow(date, type, complaintText, response, refund));
        }

        Platform.runLater(() -> {
            complaintsData.clear();
            complaintsData.addAll(rows);
            complaintsTable.setItems(complaintsData);
            
            if (rows.isEmpty()) {
                statusLabel.setText("No complaints found");
            } else {
                statusLabel.setText(rows.size() + " complaint(s) found");
            }
        });
    }

    private String categorizeComplaint(String complaintText) {
        if (complaintText == null) return "Other";
        String lower = complaintText.toLowerCase();
        if (lower.contains("delivery")) return "Delivery";
        if (lower.contains("refund")) return "Refund";
        if (lower.contains("quality")) return "Quality";
        if (lower.contains("wrong")) return "Wrong Item";
        if (lower.contains("service")) return "Service";
        if (lower.startsWith("answer to")) return "Response";
        return "Other";
    }

    public void setCurrentUser(LoginRegCheck user) {
        this.currentUser = user;
        loadComplaints();
    }

    public void cleanup() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    // Inner class to represent a complaint row in the table
    public static class ComplaintRow {
        private final String date;
        private final String type;
        private final String complaint;
        private final String response;
        private final String refund;

        public ComplaintRow(String date, String type, String complaint, String response, String refund) {
            this.date = date;
            this.type = type;
            this.complaint = complaint;
            this.response = response;
            this.refund = refund;
        }

        public String getDate() { return date; }
        public String getType() { return type; }
        public String getComplaint() { return complaint; }
        public String getResponse() { return response; }
        public String getRefund() { return refund; }
    }
} 