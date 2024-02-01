package com.base.game.retrorampage.MainMenu;

// Import necessary JavaFX classes and IOException for handling input/output exceptions

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadGame {
    // Method to create and return a Scene for the Load Game screen, given the previous Scene and the main Stage
    public Scene createLoadGameScene(Scene previousScene, Stage mainStage) {
        try {
            // Initialize an FXMLLoader to load the FXML file for the Difficulty Selection screen
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoadGame-view.fxml"));
            // Load the FXML file into a Parent root node for the scene
            Parent root = fxmlLoader.load();

            // Create a new Scene with the loaded FXML root, specifying its size (width and height)
            Scene scene = new Scene(root, 640, 480); // Adjust the size as necessary

            // Check if the main stage was previously in fullscreen mode
            boolean wasFullScreen = mainStage.isFullScreen();

            // Set the newly created scene to the main stage
            mainStage.setScene(scene);

            // If the stage was previously in fullscreen mode, reapply this mode
            if (wasFullScreen) {
                mainStage.setFullScreen(true);
            }

            // Get the controller associated with the loaded FXML file
            LoadGameController loadGameController = fxmlLoader.getController();
            // Pass the previous Scene and the main Stage to the controller for further operations
            loadGameController.setPreviousScene(previousScene);
            loadGameController.setStage(mainStage);

            // Return the newly created scene
            return scene;
        } catch (IOException e) {
            // Print the stack trace for the IOException, typically due to FXML loading issues
            e.printStackTrace();
            // Return null if there was an issue loading the FXML, indicating failure to create the scene
            return null;
        }
    }
}
