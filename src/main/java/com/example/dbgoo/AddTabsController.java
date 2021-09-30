package com.example.dbgoo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddTabsController  implements Initializable {
    @FXML
    private TabPane tabPane;

    private RootController rootController;

    public RootController getRootController() {
        return rootController;
    }

    @FXML
    private InSideController inSideLayoutController;

    public void setRootController(RootController rootController) {
        this.rootController = rootController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inSideLayoutController.setAddTabsController(this);
        /*Tab tab = new Tab();
        try {
            tab.setContent(FXMLLoader.load(getClass().getResource("/com/example/dbgoo/InSideLayout.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tab.setText("Query Tab 1");
        tabPane.getTabs().add(tab);*/

        Tab tab2 = new Tab();
        tab2.setText("+");
        tab2.setId("addBtn");
        tab2.setClosable(false);
        tabPane.getTabs().add(tabPane.getTabs().size(),tab2);

        tabPane.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        if(t1.getId() != null &&t1.getId().equals("addBtn")){
                            try {
                                addTab();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
    }

    @FXML
    private void addTab() throws IOException {
        int numTabs = tabPane.getTabs().size()-1;
        Tab tab = new Tab("Query Tab "+(numTabs+1));
        tab.setContent(FXMLLoader.load(getClass().getResource("InSideLayout.fxml")));
        tabPane.getTabs().add(tabPane.getTabs().size()-1, tab);
        tabPane.getSelectionModel().select(numTabs);
    }
}