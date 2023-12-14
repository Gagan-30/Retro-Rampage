package com.base.game.retrorampage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Sound class
public class Sound {
    public Scene createSoundScene(Scene previousScene, Stage mainStage) {
        try {
            // Load the Sound-view.fxml file using FXMLLoader
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sound-view.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new stage for the exit scene
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 640, 480));
            stage.setResizable(true);
            stage.setMinWidth(640);
            stage.setMinHeight(480);

            // Get the controller associated with the SoundController-view.fxml
            SoundController soundController = fxmlLoader.getController();

            // Set the previous scene in the SoundController
            soundController.setPreviousScene(previousScene);

            // Set the main stage in the SoundController (not GraphicsController)
            soundController.setStage(mainStage);

            // Return the scene associated with the new stage
            return stage.getScene();
        } catch (IOException e) {
            // Handle IOException by printing the stack trace
            e.printStackTrace();
            return null;
        }
    }
}
