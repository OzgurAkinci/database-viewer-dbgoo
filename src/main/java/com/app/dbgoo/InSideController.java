package com.app.dbgoo;

import com.app.dbgoo.constant.AppConstant;
import com.app.dbgoo.util.TableUtils;
import com.app.dbgoo.util.AppUtil;
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
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.ResourceBundle;


public class InSideController implements Initializable
{
    private TabsController addTabsController;

    public void setAddTabsController(TabsController addTabsController) {
        this.addTabsController = addTabsController;
    }

    @FXML
    private ComboBox<String> connectionsList;
    private final String[] observableConnectionList = {"None"};
    private final ObservableList<String> observableConnectionListObj = FXCollections.observableArrayList(observableConnectionList);

    @FXML
    private ComboBox<String> schemaList;
    private final String[] observableSchemaList = {"None"};
    private final ObservableList<String> observableSchemaListObj = FXCollections.observableArrayList(observableSchemaList);

    private String activeSchemaName = null;

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
        textArea.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyCodeCompination.match(keyEvent)) {
                buildData(textArea.getText());
            }
        });
        textArea.setParagraphGraphicFactory(LineNumberFactory.get(textArea));
        textArea.textProperty().addListener((obs, oldText, newText) -> {
            textArea.setStyleSpans(0, AppUtil.computeHighlighting(newText.toLowerCase()));
        });
        textArea.getStylesheets().add(InSideController.class.getResource("keywords.css").toExternalForm());

        //TableView
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        TableUtils.installCopyPasteHandler(tableView);

        //ConnectionsList
        connectionsList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldSelectedConnectionValue, selectedConnectionValue) -> {
                    try {
                        openConnection(selectedConnectionValue);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });

        //SchemaList
        schemaList.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldSelectedCurrentSchemaValue, selectedCurrentSchemaValue) -> activeSchemaName = selectedCurrentSchemaValue);

        //ExecuteQueryButton
        executeQueryButton.setOnAction(e -> {
            if(connection == null){
                consoleTextArea.setText("Connection not found.");
            }
            buildData(textArea.getText());
        });

        //ExecuteListAllTableButton
        executeListAllTablesButton.setOnAction(e -> {
            if(allTablesQuery != null)
                textArea.replaceText(allTablesQuery);
        });

        //ExecuteCodeBeautifyBtn
        executeCodeBeautifyBtn.setOnAction(e -> textArea.replaceText(new BasicFormatterImpl().format(textArea.getText())));


    }

    public void buildData(String sql) {
        tableView.getColumns().clear();
        tableView.getItems().clear();
        if(connection == null) {
            return;
        }
        data = FXCollections.observableArrayList();
        try {
            if(activeSchemaName != null && !activeSchemaName.isEmpty()){
                connection.setSchema(activeSchemaName);
            }
            ResultSet rs = connection.createStatement().executeQuery(sql);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j) == null ? null : param.getValue().get(j).toString()));
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

        try{
            addDataAndRefreshQueryHistory(sql);
        }catch (Exception e){
            consoleTextArea.setText("Error: " + e.getMessage());
        }
    }

    public void addDataAndRefreshQueryHistory(String sql) throws IOException, ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();


        JSONArray sqlHistoryDatas = AppUtil.getApplicationData("sql-history");
        while(sqlHistoryDatas.length() > 15){
            sqlHistoryDatas.remove(0);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sql", sql);
        jsonObject.put("date", df.format(cal.getTime()));
        sqlHistoryDatas.put(jsonObject);


        JSONObject obj = AppUtil.readJsonFromFile("application.json");
        obj.remove("sql-history");
        obj.put("sql-history", sqlHistoryDatas);

        PrintWriter prw= new PrintWriter("application.json");
        prw.println(obj);
        prw.close();

        addTabsController.getRootController().refreshQueryHistoryList();
    }

    private void openConnection(String connectionName) throws SQLException {
        JSONObject connectionData = getConnectionData(connectionName);
        if(connectionData != null) {
            String driver = connectionData.getString("driver");
            connection = openSqlConnection(connectionData);
            //schemaList.getItems().clear();
            //observableSchemaListObj.clear();
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
            alert.show();
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
        observableSchemaListObj.clear();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT nspname FROM pg_catalog.pg_namespace;");
        while(rs.next()) {
            observableSchemaListObj.add(rs.getString(1));
        }
        schemaList.setItems(observableSchemaListObj);
    }
    private void getSchemasDataFromMySQL(Connection connection) throws SQLException {
        observableSchemaListObj.clear();
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

