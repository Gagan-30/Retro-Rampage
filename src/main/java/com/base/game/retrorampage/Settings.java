package com.base.game.retrorampage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Settings {

    public Scene createSettingsScene(Stage mainStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings-view.fxml"));
            Parent root = fxmlLoader.load();

            SettingsController controller = fxmlLoader.getController();
            controller.setMainStage(mainStage);  // Set the mainStage in the controller

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
