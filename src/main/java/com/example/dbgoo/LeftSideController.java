package com.example.dbgoo;
import com.example.dbgoo.util.AppUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class LeftSideController implements Initializable {
    private RootController rootController;

    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }

    @FXML
    private ListView listView;
    private ObservableList<String> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //rootController.refreshQueryHistoryList();
        loadSqlHistoryData();
    }

    public void loadSqlHistoryData() {
        listView.getItems().clear();
        JSONArray sqlHistoryDatas = AppUtil.getApplicationData("sql-history");
        for(int i=0; i<sqlHistoryDatas.length(); i++) {
            JSONObject sqlHistoryObject = sqlHistoryDatas.getJSONObject(i);
            String sql = sqlHistoryObject.getString("sql");
            observableList.add(sql);
        }
        listView.setItems(observableList);
    }


}