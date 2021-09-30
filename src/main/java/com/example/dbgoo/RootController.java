package com.example.dbgoo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;


public class RootController  implements Initializable {
    @FXML
    private LeftSideController leftSideLayoutController;

    @FXML
    private AddTabsController addTabsLayoutController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leftSideLayoutController.setRootController(this);
        addTabsLayoutController.setRootController(this);
    }

    public void refreshQueryHistoryList () {
        this.leftSideLayoutController.loadSqlHistoryData();
    }
}