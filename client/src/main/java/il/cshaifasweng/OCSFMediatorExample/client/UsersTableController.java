package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Add_flower_wrapper;
import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import il.cshaifasweng.OCSFMediatorExample.entities.update_user_values;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.util.List;

public class UsersTableController {

    @FXML private TableView<LoginRegCheck> usersTable;
    @FXML private TableColumn<LoginRegCheck, String> usernameCol;
    @FXML private TableColumn<LoginRegCheck, String> emailCol;
    @FXML private TableColumn<LoginRegCheck, String> typeCol;
    @FXML private TableColumn<LoginRegCheck, String> storeCol;
    @FXML private TableColumn<LoginRegCheck, String> subscriptionCol;
    @FXML private TableColumn<LoginRegCheck, String> employeeTypeCol;
    @FXML private TableColumn<LoginRegCheck, String> isLoginCol;
    @FXML private TableColumn<LoginRegCheck, String> phoneCol;
    @FXML private TableColumn<LoginRegCheck, String> fullNameCol;
    @FXML private TableColumn<LoginRegCheck, String> idNumCol;

    private ObservableList<LoginRegCheck> users_date;
    List<LoginRegCheck> users ;
    private final ObservableList<String> storeOptions = FXCollections.observableArrayList("Haifa", "Krayot", "Nahariyya", "Network");
    private final ObservableList<String> subscriptionOptions = FXCollections.observableArrayList("0", "1");
    private final ObservableList<String> typeOptions = FXCollections.observableArrayList("0", "1");
    private final ObservableList<String> employeeTypeOptions = FXCollections.observableArrayList("0", "1", "2", "3");




    @FXML
    public void initialize() throws IOException {
        usernameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));
        emailCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        phoneCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPhoneNum()));
        fullNameCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getFullName()));
        idNumCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getIdNum()));



        typeCol.setCellValueFactory(cellData -> {
            String typeValue = cellData.getValue().isType() ? "1" : "0";
            return new javafx.beans.property.SimpleStringProperty(typeValue);
        });
        
        typeCol.setOnEditCommit(event -> {
            LoginRegCheck user = event.getRowValue();
            String newTypeValue = event.getNewValue();
            String oldTypeValue = event.getOldValue();
            
            if (oldTypeValue.equals(newTypeValue)) {
                System.out.println("type not changed");
                return;
            }
            
            boolean newType = "1".equals(newTypeValue);
            user.setType(newType);
            saveChanges(user, "type", newTypeValue);
            
            System.out.println("Updated type for user " + user.getUsername() + " to: " + newTypeValue);
        });
        phoneCol.setCellFactory(col -> new TextFieldTableCell<>(new DefaultStringConverter()));
        phoneCol.setOnEditCommit(event -> {
            LoginRegCheck user = event.getRowValue();
            String newPhone = event.getNewValue();
            if (!newPhone.equals(user.getPhoneNum())) {
                user.setPhoneNum(newPhone);
                saveChanges(user, "phoneNum", newPhone);
            }
        });

        fullNameCol.setCellFactory(col -> new TextFieldTableCell<>(new DefaultStringConverter()));
        fullNameCol.setOnEditCommit(event -> {
            LoginRegCheck user = event.getRowValue();
            String newName = event.getNewValue();
            if (!newName.equals(user.getFullName())) {
                user.setFullName(newName);
                saveChanges(user, "fullName", newName);
            }
        });

        idNumCol.setCellFactory(col -> new TextFieldTableCell<>(new DefaultStringConverter()));
        idNumCol.setOnEditCommit(event -> {
            LoginRegCheck user = event.getRowValue();
            String newId = event.getNewValue();
            if (!newId.equals(user.getIdNum())) {
                user.setIdNum(newId);
                saveChanges(user, "idNum", newId);
            }
        });



        storeCol.setCellValueFactory(cellData -> {
            String storeName = cellData.getValue().getStoreName();
            return new ReadOnlyStringWrapper(storeName);
        });
        storeCol.setOnEditCommit(event -> {

            LoginRegCheck user = event.getRowValue();
            String newStoreName = event.getNewValue();
            String oldStoreName = event.getOldValue();


            if(oldStoreName.equals(newStoreName))
            {
                System.out.println("nothing changed");
                return;
            }

            int newStoreIndex = storeOptions.indexOf(newStoreName) + 1;
            user.setStore(newStoreIndex);
            saveChanges(user,"store",newStoreName);

            System.out.println("Updated store for user " + user.getUsername() + " to " + newStoreName + " (store=" + newStoreIndex + ")");
        });
        usernameCol.setOnEditCommit(event -> {
            LoginRegCheck user = event.getRowValue();
            String newUsername = event.getNewValue();
            String oldUsername = event.getOldValue();

            if (oldUsername.equals(newUsername)) {
                System.out.println("username not changed");
                return;
            }
            boolean usernameExists = users.stream()
                    .anyMatch(u -> u.getUsername().equals(newUsername) && !u.getId().equals(user.getId()));

            if (usernameExists) {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Username");
                alert.setHeaderText(null);
                alert.setContentText("The username '" + newUsername + "' is already taken. Please choose another one.");
                alert.showAndWait();
                usersTable.refresh();
                return;
            }

            user.setUsername(newUsername);
            saveChanges(user, "username", newUsername);

            System.out.println("Updated username for user ID " + user.getId() + " to: " + newUsername);
        });
        emailCol.setOnEditCommit(event -> {
            LoginRegCheck user = event.getRowValue();
            String newEmail = event.getNewValue();
            String oldEmail = event.getOldValue();
            if (!newEmail.contains("@") || !newEmail.contains(".")) {
                System.out.println("Invalid email format: " + newEmail);
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Email");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a valid email address containing '@' and '.'");
                alert.showAndWait();
                usersTable.refresh(); // מחזיר את הערך הישן בתצוגה
                return;
            }

            if (oldEmail.equals(newEmail)) {
                System.out.println("email not changed");
                return;
            }

            user.setEmail(newEmail);
            saveChanges(user, "email", newEmail);

            System.out.println("Updated email for user ID " + user.getId() + " to: " + newEmail);
        });




        isLoginCol.setCellValueFactory(cellData -> {
            String value = (cellData.getValue().getIsLogin() == 1) ? "Inside the system" : "out";
            return new ReadOnlyStringWrapper(value);
        });

        subscriptionCol.setCellValueFactory(cellData -> {
            String subscriptionValue = cellData.getValue().is_yearly_subscription() ? "1" : "0";
            return new javafx.beans.property.SimpleStringProperty(subscriptionValue);
        });
        
        subscriptionCol.setOnEditCommit(event -> {
            LoginRegCheck user = event.getRowValue();
            String newSubscriptionValue = event.getNewValue();
            String oldSubscriptionValue = event.getOldValue();
            
            if (oldSubscriptionValue.equals(newSubscriptionValue)) {
                System.out.println("subscription not changed");
                return;
            }
            
            boolean newSubscription = "1".equals(newSubscriptionValue);
            user.set_yearly_subscription(newSubscription);
            
            // Handle subscription date
            if (newSubscription) {
                user.setSubscriptionStartDate(java.time.LocalDate.now());
            } else {
                user.setSubscriptionStartDate(null);
            }
            
            saveChanges(user, "yearly_subscription", newSubscriptionValue);
            
            System.out.println("Updated yearly subscription for user " + user.getUsername() + " to: " + newSubscriptionValue);
        });

        employeeTypeCol.setCellValueFactory(cellData -> {
            String employeeTypeValue = String.valueOf(cellData.getValue().getEmployeetype());
            return new javafx.beans.property.SimpleStringProperty(employeeTypeValue);
        });
        
        employeeTypeCol.setOnEditCommit(event -> {
            LoginRegCheck user = event.getRowValue();
            String newEmployeeTypeValue = event.getNewValue();
            String oldEmployeeTypeValue = event.getOldValue();
            
            if (oldEmployeeTypeValue.equals(newEmployeeTypeValue)) {
                System.out.println("employee type not changed");
                return;
            }
            
            int newEmployeeType = Integer.parseInt(newEmployeeTypeValue);
            user.setEmployeetype(newEmployeeType);
            saveChanges(user, "Employeetype", newEmployeeTypeValue);
            
            System.out.println("Updated employee type for user " + user.getUsername() + " to: " + newEmployeeTypeValue);
        });



        usernameCol.setCellFactory(col -> new TextFieldTableCell<LoginRegCheck, String>(new DefaultStringConverter()) {
            @Override
            public void startEdit() {
                LoginRegCheck user = getTableView().getItems().get(getIndex());
                if (user.getIsLogin() == 1) {
                    System.out.println("Blocked username edit for: " + user.getUsername());
                    return;
                }
                super.startEdit();
            }
        });


        emailCol.setCellFactory(col -> new TextFieldTableCell<LoginRegCheck, String>(new DefaultStringConverter()) {
            @Override
            public void startEdit() {
                LoginRegCheck user = getTableView().getItems().get(getIndex());
                if (user.getIsLogin() == 1) {
                    System.out.println("Blocked email edit for: " + user.getUsername());
                    return;
                }
                super.startEdit();
            }
        });



        storeCol.setCellFactory(col -> new ComboBoxTableCell<LoginRegCheck, String>(storeOptions) {
            @Override
            public void startEdit() {
                LoginRegCheck user = getTableView().getItems().get(getIndex());
                if (user.getIsLogin() == 1) {
                    System.out.println("Blocked editing for logged-in user: " + user.getUsername());
                    return;
                }
                super.startEdit();
            }
        });


        subscriptionCol.setCellFactory(col -> new ComboBoxTableCell<LoginRegCheck, String>(subscriptionOptions) {
            @Override
            public void startEdit() {
                LoginRegCheck user = getTableView().getItems().get(getIndex());
                if (user.getIsLogin() == 1) {
                    System.out.println("Blocked subscription edit for logged-in user: " + user.getUsername());
                    return;
                }
                super.startEdit();
            }
        });

        typeCol.setCellFactory(col -> new ComboBoxTableCell<LoginRegCheck, String>(typeOptions) {
            @Override
            public void startEdit() {
                LoginRegCheck user = getTableView().getItems().get(getIndex());
                if (user.getIsLogin() == 1) {
                    System.out.println("Blocked type edit for logged-in user: " + user.getUsername());
                    return;
                }
                super.startEdit();
            }
        });

        employeeTypeCol.setCellFactory(col -> new ComboBoxTableCell<LoginRegCheck, String>(employeeTypeOptions) {
            @Override
            public void startEdit() {
                LoginRegCheck user = getTableView().getItems().get(getIndex());
                if (user.getIsLogin() == 1) {
                    System.out.println("Blocked employee type edit for logged-in user: " + user.getUsername());
                    return;
                }
                super.startEdit();
            }
        });

        usersTable.setEditable(true);

        EventBus.getDefault().register(this);
        System.out.println("Registered to EventBus - waiting for reg result");

        if (!SimpleClient.getClient().isConnected()) {
            try {
                SimpleClient.getClient().openConnection();
            } catch (IOException e) {
                System.out.println("Failed to open connection to server.");
                e.printStackTrace();
                return;
            }
        }

        try {
            SimpleClient.getClient().sendToServer("asks_for_users");
        } catch (IOException e) {
            System.out.println("Failed to send request for users.");
            e.printStackTrace();
        }
    }

    void saveChanges(LoginRegCheck user,String column,String new_value)
    {
            System.out.println("inside store");
            update_user_values wrapper = new update_user_values(user, column, new_value);
            try {
                SimpleClient.getClient().sendToServer(wrapper);
                System.out.println(" send request to update");
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    public String getStoreName(int index)
    {
        String[] stores = { "Haifa", "Krayot", "Nahariyya", "Network" };
        return stores[index];

    }


    @Subscribe
    public void get_users(List<LoginRegCheck> allUsers)
    {
        users=allUsers;
        loadUsersFromDB();
    }

    private void loadUsersFromDB()
    {
        users_date = FXCollections.observableArrayList(users);
        usersTable.setItems(users_date);

    }



}
