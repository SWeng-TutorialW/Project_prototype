package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.LoginRegCheck;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
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

    private ObservableList<LoginRegCheck> users_date;
    List<LoginRegCheck> users ;

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

        subscriptionCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().is_yearly_subscription()).asObject());

        usernameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        storeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        //subscriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());

        usersTable.setEditable(true);

        EventBus.getDefault().register(this);
        System.out.println("Registered to EventBus - waiting for reg result");

        SimpleClient.getClient().sendToServer("asks_for_users");
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

    @FXML
    void saveChanges()
    {

    }
}
