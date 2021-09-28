package com.example.dbgoo;

import com.example.dbgoo.constant.AppConstant;
import com.example.dbgoo.util.AppUtil;
import com.example.dbgoo.util.TableUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.sql.*;

import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;


public class InSideController implements Initializable
{
    @FXML
    private ComboBox<String> connectionsList;
    private String[] observableConnectionList = {"None"};
    private ObservableList<String> observableConnectionListObj = FXCollections.observableArrayList(observableConnectionList);

    @FXML
    private ComboBox<String> schemaList;
    private String[] observableSchemaList = {"None"};
    private ObservableList<String> observableSchemaListObj = FXCollections.observableArrayList(observableSchemaList);

    @FXML
    private Button executeQueryButton;

    @FXML
    private Button executeListAllTablesButton;
    private String allTablesQuery = null;

    @FXML
    private Button executeCodeBeautifyBtn;

    @FXML
    private CodeArea textArea;

    @FXML
    private TextArea consoleTextArea;

    @FXML
    private TableView tableView;
    private ObservableList<ObservableList> data;
    private Connection connection = null;

    @Override
    public void initialize(java.net.URL arg0, ResourceBundle arg1) {
        loadConnectionData();
        //TextArea
        KeyCodeCombination keyCodeCompination = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);
        textArea.addEventFilter(KeyEvent.ANY, keyEvent -> {
            if (keyCodeCompination.match(keyEvent)) {
                buildData(textArea.getText());
            }
        });
        textArea.setParagraphGraphicFactory(LineNumberFactory.get(textArea));
        textArea.textProperty().addListener((obs, oldText, newText) -> {
            textArea.setStyleSpans(0, AppUtil.computeHighlighting(newText.toLowerCase()));
        });
        textArea.getStylesheets().add(InSideController.class.getResource("java-keywords.css").toExternalForm());

        //TableView
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        TableUtils.installCopyPasteHandler(tableView);

        //ConnectionsList
        connectionsList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> observable,
                                        String oldSelectedConnectionValue, String selectedConnectionValue) {
                        try {
                            openConnection(selectedConnectionValue);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

        //ExecuteQueryButton
        executeQueryButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                if(connection == null){
                    consoleTextArea.setText("Connection not found.");
                }
                buildData(textArea.getText());
            }
        });

        //ExecuteListAllTableButton
        executeListAllTablesButton.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                if(allTablesQuery != null)
                    textArea.replaceText(allTablesQuery);
            }
        });

        //ExecuteCodeBeautifyBtn
        executeCodeBeautifyBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e)
            {
                textArea.replaceText(new BasicFormatterImpl().format(textArea.getText()));
            }
        });


    }

    public void buildData(String sql) {
        tableView.getColumns().clear();
        tableView.getItems().clear();
        if(connection == null) {
            return;
        }
        data = FXCollections.observableArrayList();
        try {
            ResultSet rs = connection.createStatement().executeQuery(sql);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j) == null ? null : param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
            }
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                data.add(row);

            }
            tableView.setItems(data);
            consoleTextArea.setText("Query successfully executed.");
        } catch (Exception e) {
            consoleTextArea.setText(e.getMessage());
        }
    }

    private void openConnection(String connectionName) throws SQLException {
        JSONObject connectionData = getConnectionData(connectionName);
        if(connectionData != null) {
            String driver = connectionData.getString("driver");
            connection = openSqlConnection(connectionData);
            schemaList.getItems().clear();
            if(driver.equals(AppConstant.CONNECTION_DRIVER_POSTGRESQL)){
                getSchemasDataFromPostgreSQL(connection);
            }else if(driver.equals(AppConstant.CONNECTION_DRIVER_MYSQL)) {
                getSchemasDataFromMySQL(connection);
            }
            setListAllTablesQuery(driver);
        }else{
            connection = null;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Connection not found!");
            alert.setContentText("Connection not found. Please see application.json file.");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
    }

    private void setListAllTablesQuery(String driver) {
        if(driver.equals(AppConstant.CONNECTION_DRIVER_POSTGRESQL)) {
            allTablesQuery = "SELECT * FROM pg_catalog.pg_tables;";
        }else if(driver.equals(AppConstant.CONNECTION_DRIVER_MYSQL)){
            allTablesQuery = "show tables;";
        }else {
            allTablesQuery = null;
        }
    }

    private void getSchemasDataFromPostgreSQL(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nspname FROM pg_catalog.pg_namespace;");
        while(rs.next()) {
            observableSchemaListObj.add(rs.getString(1));
        }
        schemaList.setItems(observableSchemaListObj);
    }
    private void getSchemasDataFromMySQL(Connection connection) throws SQLException {
        ResultSet rs = connection.getMetaData().getCatalogs();
        while(rs.next()) {
            observableSchemaListObj.add(rs.getString("TABLE_CAT"));
        }
        schemaList.setItems(observableSchemaListObj);
    }

    private Connection openSqlConnection(JSONObject connectionData) throws SQLException {
        String databaseServer = connectionData.getString("database-server");
        String databasePort = connectionData.getString("database-port");
        String database = connectionData.getString("database");
        String ssl = connectionData.getString("ssl");
        String driver = connectionData.getString("driver");

        String url = "jdbc:"+ driver +"://" + databaseServer + ":" + databasePort + "/" + database;
        Properties props = new Properties();
        props.setProperty("user", connectionData.getString("user"));
        props.setProperty("password", connectionData.getString("password"));
        props.setProperty("ssl", ssl);
        return DriverManager.getConnection(url, props);
    }

    private void loadConnectionData(){
        JSONArray connectionsData = AppUtil.getApplicationData("connections");
        for(int i=0; i<connectionsData.length(); i++) {
            JSONObject connectionObject = connectionsData.getJSONObject(i);
            observableConnectionListObj.add(connectionObject.getString("connection-name"));
        }
        connectionsList.setItems(observableConnectionListObj);
    }

    private JSONObject getConnectionData(String connectionName) {
        JSONArray connectionsData = AppUtil.getApplicationData("connections");
        for(int i=0; i<connectionsData.length(); i++) {
            JSONObject connectionObject = connectionsData.getJSONObject(i);
            if(connectionObject.get("connection-name").toString().equals(connectionName)){
                return connectionObject;
            }
        }
        return null;
    }
}
