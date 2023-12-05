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

        Scene scene = new Scene(root, 1280, 720);
        stage.setTitle("Retro Rampage - Main Menu");
        stage.setScene(scene);

        MainMenuController mainMenuController = fxmlLoader.getController();
        mainMenuController.initialize();

        stage.show();
    }
}
