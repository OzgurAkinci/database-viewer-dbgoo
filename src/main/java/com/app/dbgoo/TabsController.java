package com.app.dbgoo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TabsController implements Initializable {
    @FXML
    public TabPane tabPane;

    private RootController rootController;
    public RootController getRootController() {
        return rootController;
    }
    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Tab addTabBtn = new Tab();
        addTabBtn.setText("+");
        addTabBtn.setId("addBtn");
        addTabBtn.setClosable(false);
        tabPane.getTabs().add(tabPane.getTabs().size(), addTabBtn);

        try {
            addDynamicTab();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, t, t1) -> {
                    if(t1.getId() != null &&t1.getId().equals("addBtn")){
                        try {
                            addDynamicTab();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    @FXML
    private void addDynamicTab() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/InSideLayout.fxml"));
        InSideController inSideController = new InSideController();
        inSideController.setAddTabsController(this);
        fxmlLoader.setController(inSideController);

        int numTabs = tabPane.getTabs().size()-1;
        Tab tab = new Tab("Query Tab "+(numTabs+1));
        tab.setContent(fxmlLoader.load());
        tab.getStyleClass().addAll("global-bg", "tab-font-color");
        tabPane.getTabs().add(tabPane.getTabs().size()-1, tab);
        tabPane.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        tabPane.getSelectionModel().select(numTabs);
    }
}