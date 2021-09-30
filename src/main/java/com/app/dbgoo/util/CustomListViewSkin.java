package com.app.dbgoo.util;


import com.app.dbgoo.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Johannes on 23.05.16.
 *
 */

public class CustomListViewSkin extends ListCell<JSONObject> {
    @Override
    protected void updateItem(JSONObject jObject, boolean empty) {
        super.updateItem(jObject, empty);

        if (empty) {
            setGraphic(null);
        } else {
            final Tooltip tooltip = new Tooltip(jObject.getString("sql"));
            setAlignment(Pos.CENTER_LEFT);
            setTextAlignment(TextAlignment.JUSTIFY);
            setTooltip(tooltip);

            VBox hBox = new VBox();
            hBox.setMaxWidth(250);
            hBox.setFillWidth(true);
            hBox.setAlignment(Pos.CENTER_LEFT);

            // Create centered Label
            HBox hBoxLabel1 = new HBox();
            Label label = new Label(jObject.getString("sql"));
            label.setAlignment(Pos.CENTER_LEFT);
            hBoxLabel1.getChildren().add(label);

            HBox hBoxLabel2 = new HBox();
            Label label2 = new Label(jObject.getString("date"));
            label2.setTextFill(Color.GRAY);
            label2.setAlignment(Pos.CENTER_LEFT);
            hBoxLabel2.getChildren().add(label2);

            hBox.getChildren().addAll(hBoxLabel1, hBoxLabel2);
            setGraphic(hBox);
            setCursor(Cursor.HAND);
        }
    }
}