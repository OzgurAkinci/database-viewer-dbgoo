package com.app.dbgoo.util;


import com.app.dbgoo.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * Created by Johannes on 23.05.16.
 *
 */

public class CustomListViewSkin extends ListCell<String> {
    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(String text, boolean empty) {
        super.updateItem(text, empty);
        setText(text);
        setHeight(40);
    }
}