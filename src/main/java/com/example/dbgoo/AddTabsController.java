package com.example.dbgoo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AddTabsController  {
    @FXML
    private TabPane tabPane;

    public void initialize() throws IOException {
        Tab tab = new Tab();
        tab.setContent(FXMLLoader.load(getClass().getResource("InSideLayout.fxml")));
        tab.setText("Query Tab 1");
        tabPane.getTabs().add(tab);

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

    @FXML
    private void listTabs() {
        tabPane.getTabs().forEach(tab -> System.out.println(tab.getText()));
        System.out.println();
    }
}