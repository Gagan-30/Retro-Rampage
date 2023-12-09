package com.base.game.retrorampage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Keybind {

    public Scene createKeybindScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Keybind-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 640, 480));
            stage.setResizable(true);
            stage.setMinWidth(640);
            stage.setMinHeight(480);

            return stage.getScene();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
