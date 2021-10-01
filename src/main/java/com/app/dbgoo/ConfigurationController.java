package com.app.dbgoo;

import com.app.dbgoo.entity.ConnectionObj;
import com.app.dbgoo.util.AppUtil;
import com.app.dbgoo.util.TableUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ConfigurationController implements Initializable {
    @FXML
    private TableView connectionListTableView;
    private ObservableList<ConnectionObj> data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableUtils.installCopyPasteHandler(connectionListTableView);
        TableColumn connectionName = new TableColumn("Connection Name");
        connectionName.setCellValueFactory(new PropertyValueFactory<ConnectionObj, String>("connectionName"));
        TableColumn driver = new TableColumn("Driver");
        driver.setCellValueFactory(new PropertyValueFactory<ConnectionObj, String>("driver"));
        TableColumn database = new TableColumn("Database");
        database.setCellValueFactory(new PropertyValueFactory<ConnectionObj, String>("database"));
        TableColumn user = new TableColumn("User");
        user.setCellValueFactory(new PropertyValueFactory<ConnectionObj, String>("user"));
        TableColumn password = new TableColumn("Password");
        password.setCellValueFactory(new PropertyValueFactory<ConnectionObj, String>("password"));
        TableColumn databaseServer = new TableColumn("Database Server");
        databaseServer.setCellValueFactory(new PropertyValueFactory<ConnectionObj, String>("databaseServer"));
        TableColumn databasePort = new TableColumn("Database Port");
        databasePort.setCellValueFactory(new PropertyValueFactory<ConnectionObj, String>("databasePort"));
        TableColumn ssl = new TableColumn("SSL");
        ssl.setCellValueFactory(new PropertyValueFactory<ConnectionObj, String>("ssl"));
        connectionListTableView.getColumns().addAll(connectionName, driver, database, user, password, databaseServer, databasePort, ssl);
        loadConnectionData();


        connectionListTableView.setRowFactory( tv -> {
            TableRow<ConnectionObj> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    ConnectionObj rowData = row.getItem();
                    provideNewConnectionMenuFunctionality(rowData);
                }
            });
            return row ;
        });
    }

    @FXML
    private MenuBar configurationMenuBar;

    @FXML
    private void handleNewConnectionMenuAction(final ActionEvent event)
    {
        MenuItem menuItem = ((MenuItem) event.getSource());
        if(menuItem.getId().equals("newConnectionMenu")) {
            provideNewConnectionMenuFunctionality(null);
        }else if(menuItem.getId().equals("closeConnectionsScreen")){
            Stage stage = (Stage) configurationMenuBar.getScene().getWindow();
            stage.close();
        }
    }

    private void provideNewConnectionMenuFunctionality(ConnectionObj connectionObj_) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/ConfigurationEdit.fxml"));
            if(connectionObj_ != null) {
                JSONObject connectionObj = new JSONObject();
                connectionObj.put("connection-name", connectionObj_.getConnectionName());
                connectionObj.put("database", connectionObj_.getDatabase());
                connectionObj.put("password", connectionObj_.getPassword());
                connectionObj.put("driver", connectionObj_.getDriver());
                connectionObj.put("database-server", connectionObj_.getDatabaseServer());
                connectionObj.put("user", connectionObj_.getUser());
                connectionObj.put("ssl", connectionObj_.getSsl());
                connectionObj.put("database-port", connectionObj_.getDatabasePort());

                ConfigurationEditController configurationEditController = new ConfigurationEditController(connectionObj, this);
                fxmlLoader.setController(configurationEditController);
            }else{
                ConfigurationEditController configurationEditController = new ConfigurationEditController(null, this);
                fxmlLoader.setController(configurationEditController);
            }
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("DB Goo - Database Configuration Edit");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png"))));
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadConnectionData(){
        JSONArray connectionsData = AppUtil.getApplicationData("connections");
        data = FXCollections.observableArrayList();
        for(int i=0; i<connectionsData.length(); i++) {
            JSONObject connectionObject = connectionsData.getJSONObject(i);
            ConnectionObj connectionObj = new ConnectionObj(connectionObject.getString("connection-name"),
                    connectionObject.getString("driver"),
                    connectionObject.getString("database"),
                    connectionObject.getString("user"),
                    connectionObject.getString("password"),
                    connectionObject.getString("database-server"),
                    connectionObject.getString("database-port"),
                    connectionObject.getString("ssl"));
            data.add(connectionObj);
        }
        connectionListTableView.setItems(data);
    }

}