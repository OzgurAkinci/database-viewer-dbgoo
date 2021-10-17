package com.app.dbgoo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/RootLayout.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
        stage.setMaximized(true);
        stage.setTitle("DB Goo - Database Viewer!");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("img/icon.ico"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}