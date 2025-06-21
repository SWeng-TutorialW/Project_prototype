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
    @FXML private TableColumn<LoginRegCheck, Boolean> subscriptionCol;
    @FXML private TableColumn<LoginRegCheck, String> isLoginCol;

    private ObservableList<LoginRegCheck> users_date;
    List<LoginRegCheck> users ;
    private final ObservableList<String> storeOptions = FXCollections.observableArrayList("Haifa", "Krayot", "Nahariyya", "Network");




    @FXML
    public void initialize() throws IOException {
        usernameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));
        emailCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));


        typeCol.setCellValueFactory(cellData -> {
            String typeString = cellData.getValue().isType() ? "employee" : "client";
            return new ReadOnlyStringWrapper(typeString);
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

            user.setUsername(newUsername);
            saveChanges(user, "username", newUsername);

            System.out.println("Updated username for user ID " + user.getId() + " to: " + newUsername);
        });
        emailCol.setOnEditCommit(event -> {
            LoginRegCheck user = event.getRowValue();
            String newEmail = event.getNewValue();
            String oldEmail = event.getOldValue();

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

        subscriptionCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().is_yearly_subscription()).asObject());



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


        //subscriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());

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
