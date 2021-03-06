package com.app.dbgoo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import org.fxmisc.richtext.CodeArea;

import java.net.URL;
import java.util.ResourceBundle;


public class RootController  implements Initializable {
    @FXML
    private LeftSideController leftSideLayoutController;

    @FXML
    private TabsController addTabsLayoutController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leftSideLayoutController.setRootController(this);
        addTabsLayoutController.setRootController(this);
    }

    public void refreshQueryHistoryList () {
        this.leftSideLayoutController.loadSqlHistoryData();
    }

    public void setTxt(String txt) {
        try {
            VBox n = (VBox) this.addTabsLayoutController.tabPane.getSelectionModel().getSelectedItem().getContent();
            //Index = 1 =>  CodeArea element.
            CodeArea codeArea = (CodeArea) n.getChildren().get(1);
            codeArea.replaceText(txt);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}