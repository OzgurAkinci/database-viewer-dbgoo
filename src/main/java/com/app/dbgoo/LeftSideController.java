package com.app.dbgoo;
import com.app.dbgoo.util.AppUtil;
import com.app.dbgoo.util.CustomListViewSkin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
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
    private final ObservableList<JSONObject> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ListView
        listView.setCellFactory(customListViewSkin -> new CustomListViewSkin());
        listView.setOnKeyPressed(e -> {
            KeyCodeCombination copyKeyCodeCompination = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
            if (copyKeyCodeCompination.match(e)) {
                JSONObject currentItemSelected = (JSONObject) listView.getSelectionModel().getSelectedItem();
                String sql = currentItemSelected.getString("sql");

                ClipboardContent content = new ClipboardContent();
                content.putString(sql);
                Clipboard.getSystemClipboard().setContent(content);
            }
        });
        loadSqlHistoryData();
    }

    public void loadSqlHistoryData() {
        listView.getItems().clear();
        JSONArray sqlHistoryDatas = AppUtil.getApplicationData("sql-history");
        for(int i=0; i<sqlHistoryDatas.length(); i++) {
            JSONObject sqlHistoryObject = sqlHistoryDatas.getJSONObject(i);
            String sql = sqlHistoryObject.getString("sql");
            String date = sqlHistoryObject.getString("date");
            JSONObject jo = new JSONObject();
            jo.put("sql", sql);
            jo.put("date", date);
            observableList.add(jo);
        }
        listView.setItems(observableList);
    }


}