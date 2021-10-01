package com.app.dbgoo;

import com.app.dbgoo.entity.ConnectionObj;
import com.app.dbgoo.util.AppUtil;
import com.app.dbgoo.util.TableUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
        TableColumn password = new TableColumn("Password");
        password.setCellValueFactory(new PropertyValueFactory<ConnectionObj, String>("password"));
        connectionListTableView.getColumns().addAll(connectionName, driver, database, password);         
        loadConnectionData();
    }

    @FXML
    private MenuBar configurationMenuBar;

    @FXML
    private void handleNewConnectionMenuAction(final ActionEvent event)
    {
        MenuItem menuItem = ((MenuItem) event.getSource());
        if(menuItem.getId().equals("newConnectionMenu")) {
            provideNewConnectionMenuFunctionality();
        }else if(menuItem.getId().equals("closeConnectionsScreen")){
            Stage stage = (Stage) configurationMenuBar.getScene().getWindow();
            stage.close();
        }
    }

    private void provideNewConnectionMenuFunctionality() {

    }

    private void loadConnectionData(){
        JSONArray connectionsData = AppUtil.getApplicationData("connections");
        data = FXCollections.observableArrayList();
        for(int i=0; i<connectionsData.length(); i++) {
            JSONObject connectionObject = connectionsData.getJSONObject(i);
            ConnectionObj connectionObj = new ConnectionObj(connectionObject.getString("connection-name"),
                    connectionObject.getString("driver"),
                    connectionObject.getString("database"),
                    connectionObject.getString("password"),
                    connectionObject.getString("test-query"));
            data.add(connectionObj);
        }

        connectionListTableView.setItems(data);
    }

}