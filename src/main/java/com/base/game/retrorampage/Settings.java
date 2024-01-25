package com.base.game.retrorampage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Settings {

    public Scene createSettingsScene(Scene previousScene, Stage mainStage) {
        try {
            System.out.println("[Settings] Creating settings scene at " + java.time.LocalDateTime.now());

            // Load the Settings-view.fxml file using FXMLLoader
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings-view.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new stage for the exit scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 640, 480));
            stage.setResizable(true);
            stage.setMinWidth(640);
            stage.setMinHeight(480);

            // Get the controller associated with the Settings-view.fxml
            SettingsController settingsController = fxmlLoader.getController();

            // Set the previous scene in the SettingsController
            settingsController.setPreviousScene(previousScene);

            // Set the main stage in the SettingsController
            settingsController.setStage(mainStage);

            // Return the scene associated with the new stage
            return stage.getScene();
        } catch (IOException e) {
            // Handle IOException by printing the stack trace
            e.printStackTrace();
            return null;
        }
    }
}
