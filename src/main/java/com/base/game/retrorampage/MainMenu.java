package com.base.game.retrorampage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {

    public void show(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainMenu-view.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 640, 480);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("Retro Rampage - Main Menu");
        stage.setResizable(true);
        stage.setMinWidth(640);
        stage.setMinHeight(480);
        stage.setScene(scene);

        MainMenuController mainMenuController = fxmlLoader.getController();

        stage.show();
    }
}
