package com.app.dbgoo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfigurationEditController implements Initializable {
    private JSONObject connectionObject;

    @FXML
    private TextField connectionName;

    @FXML
    private TextField database;

    @FXML
    private ComboBox driver;
    private final String[] observableConnectionList = {"postgresql", "mysql"};
    private final ObservableList<String> observableConnectionListObj = FXCollections.observableArrayList(observableConnectionList);

    @FXML
    private TextField user;

    @FXML
    private PasswordField password;

    public ConfigurationEditController(JSONObject connectionObject) {
        this.connectionObject = connectionObject;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        driver.setItems(observableConnectionListObj);

        if(connectionObject != null){
            connectionName.setText(connectionObject.getString("connectionName"));
            database.setText(connectionObject.getString("database"));
            user.setText(connectionObject.getString("user"));
            password.setText(connectionObject.getString("password"));
            driver.setValue(connectionObject.getString("driver"));
        }
    }

}