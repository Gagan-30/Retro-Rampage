package com.base.game.retrorampage.MainMenu;

// Import statements for handling JavaFX components and exceptions
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameOver {

    // Method to create and return a JavaFX Scene for the NextLevel screen. This method takes the previous scene and the main application stage as parameters.
    public Scene createGameOverScene(Scene previousScene, Stage mainStage) {
        try {
            // Initialize an FXMLLoader to load the FXML file associated with the NextLevel screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameOver-view.fxml"));
            // Load the root node from the FXML file into a Parent object
            Parent root = fxmlLoader.load();

            // Create a new Scene instance with the loaded root node, specifying its width and height
            Scene scene = new Scene(root, 640, 480); // The size can be adjusted as needed

            // Check if the main application stage was previously set to fullscreen mode
            boolean wasFullScreen = mainStage.isFullScreen();

            // Set the newly created scene to the main application stage
            mainStage.setScene(scene);

            // If the stage was in fullscreen mode before, reapply this mode to the stage
            if (wasFullScreen) {
                mainStage.setFullScreen(true);
            }

            // Retrieve the controller associated with the FXML file loaded for the settings screen
            GameOverController gameOverController = fxmlLoader.getController();
            // Pass the previous scene and the main stage to the controller for further use
            gameOverController.setStage(mainStage);
            gameOverController.setPreviousScene(previousScene);


            // Return the newly created scene to wherever this method was called from
            return scene;
        } catch (IOException e) {
            // If there's an IOException (e.g., file not found), print the stack trace for debugging
            e.printStackTrace();
            // Return null indicating that the scene could not be created due to an error
            return null;
        }
    }
}