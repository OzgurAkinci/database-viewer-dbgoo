package com.app.dbgoo;

import java.util.Objects;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MenuController implements Initializable
{
    @FXML
    private MenuBar menuBar;

    @FXML
    private void handleMenuAction(final ActionEvent event)
    {
        MenuItem menuItem = ((MenuItem) event.getSource());
        if(menuItem.getId().equals("confMenu")) {
            provideConfMenuFunctionality();
        }else if(menuItem.getId().equals("exitProgramMenu")) {
            Platform.exit();
            System.exit(0);
        }
    }

    private void provideConfMenuFunctionality()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/Configuration.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("DB Goo - Database Configuration");
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.png"))));
            stage.setScene(scene);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(java.net.URL arg0, ResourceBundle arg1) {
        menuBar.setFocusTraversable(true);
        menuBar.setCursor(Cursor.HAND);
    }
}

