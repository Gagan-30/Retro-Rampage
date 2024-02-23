package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Sound {

    // Method to create a new scene for sound settings
    public Scene createSoundScene(Scene previousScene, Stage mainStage) {
        try {
            // Load the FXML for the Sound-view screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Sound-view.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new scene with the loaded FXML root
            Scene scene = new Scene(root, 640, 480); // You might want to adjust the size

            // Check if the stage was in fullscreen mode before changing the scene
            boolean wasFullScreen = mainStage.isFullScreen();

            // Set the newly created scene as the main stage's scene
            mainStage.setScene(scene);

            // Reapply fullscreen mode if it was previously set
            if (wasFullScreen) {
                mainStage.setFullScreen(true);
            }

            // Set up the controller for the Sound-view screen
            SoundController soundController = fxmlLoader.getController();
            soundController.setPreviousScene(previousScene);
            soundController.setStage(mainStage);

            // Return the newly created scene
            return scene;
        } catch (IOException e) {
            // Handle any potential IO exception
            e.printStackTrace();
            return null; // Return null in case of an exception
        }
    }
}
