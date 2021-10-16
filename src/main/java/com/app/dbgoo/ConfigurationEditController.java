package com.app.dbgoo;

import com.app.dbgoo.constant.AppConstant;
import com.app.dbgoo.util.AppUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ConfigurationEditController implements Initializable {
    private JSONObject connectionObject;

    @FXML
    private TextField connectionName;

    @FXML
    private TextField databaseTxt;

    @FXML
    private TextField server;

    @FXML
    private TextField port;

    @FXML
    private ComboBox driver;
    private final String[] observableConnectionList = {"postgresql", "mysql"};
    private final ObservableList<String> observableConnectionListObj = FXCollections.observableArrayList(observableConnectionList);

    @FXML
    private TextField user;

    @FXML
    private PasswordField password;

    @FXML
    private ComboBox ssl;
    private final String[] sslValueList = {"false", "true"};
    private final ObservableList<String> observableSslValueListObj = FXCollections.observableArrayList(sslValueList);

    @FXML
    private Button executeQueryTestButton;

    @FXML
    private Button executeQuerySaveButton;

    private ConfigurationController configurationController;

    public ConfigurationEditController(JSONObject connectionObject, ConfigurationController configurationController) {
        this.connectionObject = connectionObject;
        this.configurationController = configurationController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        driver.setItems(observableConnectionListObj);
        ssl.setItems(observableSslValueListObj);

        if(connectionObject != null){
            connectionName.setText(connectionObject.getString("connection-name"));
            databaseTxt.setText(connectionObject.getString("database"));
            server.setText(connectionObject.getString("database-server"));
            port.setText(connectionObject.getString("database-port"));
            user.setText(connectionObject.getString("user"));
            password.setText(connectionObject.getString("password"));
            driver.setValue(connectionObject.getString("driver"));
            ssl.setValue(connectionObject.getString("ssl"));
        }

        executeQueryTestButton.setOnMouseClicked((MouseEvent event) -> {
            try {
                testConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        executeQuerySaveButton.setOnMouseClicked((MouseEvent event) -> {
             JSONObject newJsonObj = new JSONObject();
             newJsonObj.put("connection-name", connectionName.getText());
             newJsonObj.put("database", databaseTxt.getText());
             newJsonObj.put("password", password.getText());
             newJsonObj.put("driver", driver.getValue() == null ? "" : driver.getValue());
             newJsonObj.put("database-server", server.getText());
             newJsonObj.put("user", user.getText());
             newJsonObj.put("ssl", ssl.getValue() == null ? "" : ssl.getValue());
             newJsonObj.put("database-port", port.getText());

             try {
                 saveNewObj(newJsonObj);
             }catch (Exception e){
                 Alert alert = new Alert(Alert.AlertType.INFORMATION);
                 alert.setTitle("Error");
                 alert.setHeaderText("Save object error");
                 alert.setContentText(e.getMessage());
                 alert.show();
             }
        });
    }

    private void saveNewObj(JSONObject newJsonObj) throws IOException, ParseException {
        JSONArray connections = AppUtil.getApplicationData("connections");

        if(newJsonObj.getString("connection-name").isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Save object error");
            alert.setContentText("Connection name is required.");
            alert.show();
            return;
        }

        boolean isUpdate= false;
        int i=0;
        if(this.connectionObject != null){
            while(i<connections.length()){
                JSONObject jsonObjectTemp = connections.getJSONObject(i);
                if(this.connectionObject.getString("connection-name").equals(jsonObjectTemp.getString("connection-name"))){
                    isUpdate = true;
                    break;
                }
                i++;
            }
        }

        if(isUpdate) {
            connections.put(i, newJsonObj);
        }else{
            connections.put(newJsonObj);
        }

        JSONObject obj = AppUtil.readJsonFromFile();
        obj.remove("connections");
        obj.put("connections", connections);
        String path = AppConstant.homePath + File.separator + AppConstant.propertiesFolderName + File.separator + AppConstant.propertiesFileName;
        PrintWriter prw= new PrintWriter(path);
        prw.println(obj);
        prw.close();

        /** **/
        //this.connectionObject.put("connection-name", newJsonObj.getString("connection-name"));
        this.connectionObject = newJsonObj;
        /** **/
        configurationController.loadConnectionData();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Successfully saved.");
        alert.setContentText("The connection record has been successfully saved.");
        alert.show();
    }

    private void testConnection() throws SQLException {
        String databaseServer = server.getText();
        String databasePort = port.getText();
        String database = databaseTxt.getText();
        String ssl_ = ssl.getValue().toString();
        String driver_ = driver.getValue().toString();

        String url = "jdbc:"+ driver_ +"://" + databaseServer + ":" + databasePort + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", user.getText());
        props.setProperty("password", password.getText());
        props.setProperty("ssl", ssl_);
        try {
            Connection connection = DriverManager.getConnection(url, props);
            if(connection !=null){
                connection.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Connection success");
                alert.setContentText("Successfully created the connection.");
                alert.show();
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Connection error");
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

}