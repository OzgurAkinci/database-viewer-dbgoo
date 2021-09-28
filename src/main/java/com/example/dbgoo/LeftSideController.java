package com.example.dbgoo;
import com.example.dbgoo.util.AppUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

public class LeftSideController {
    @FXML
    private ListView listView;

    private ObservableList<String> observableList = FXCollections.observableArrayList();


    public void initialize(){
        loadSqlHistoryData();
    }

    private void loadSqlHistoryData() {
        JSONArray sqlHistoryDatas = AppUtil.getApplicationData("sql-history");
        for(int i=0; i<sqlHistoryDatas.length(); i++) {
            JSONObject sqlHistoryObject = sqlHistoryDatas.getJSONObject(i);
            String sql = sqlHistoryObject.getString("sql");
            observableList.add(sql);
        }
        listView.setItems(observableList);
    }
}