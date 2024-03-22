// Package declaration specifies the location within the project structure.
package com.base.game.retrorampage.MainMenu;

// Import statements for required JavaFX classes and exception handling.

import com.base.game.retrorampage.LevelGeneration.MainGame;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Defines the DifficultySelection class.
public class DifficultySelection {

    // Method to create and return a Scene for the difficulty selection screen.
    public Scene createDifficultySelectionScene(Scene previousScene, Stage mainStage) {
        try {

            // Stop the game loop of the previous scene if it's running
            if (previousScene != null && previousScene.getUserData() instanceof MainGame) {
                MainGame previousMainGame = (MainGame) previousScene.getUserData();
                previousMainGame.stopGameLoop();
            }

            // Initializes an FXMLLoader to load the FXML file for the difficulty selection screen.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DifficultySelection-view.fxml"));
            Parent root = fxmlLoader.load(); // Loads the FXML file and returns the root node.

            // Creates a new Scene with the loaded root node. The scene's size is set to 640x480.
            Scene scene = new Scene(root, 640, 480);

            // Checks if the mainStage was previously set to fullscreen.
            boolean wasFullScreen = mainStage.isFullScreen();

            // Sets the newly created scene to the mainStage.
            mainStage.setScene(scene);

            // If the stage was in fullscreen mode before, re-enable fullscreen mode.
            if (wasFullScreen) {
                mainStage.setFullScreen(true);
            }

            // Retrieve the controller associated with the FXML file
            DifficultySelectionController difficultySelectionController = fxmlLoader.getController();
            difficultySelectionController.setPreviousScene(previousScene);
            difficultySelectionController.setStage(mainStage);

            // Instantiate MainGame and pass the stage to it
            MainGame mainGame = new MainGame(mainStage);
            difficultySelectionController.setMainGame(mainGame); // Pass MainGame instance

            // Returns the newly created scene.
            return scene;
        } catch (IOException e) { // Catches any IOExceptions that occur during FXML loading.
            e.printStackTrace(); // Prints the stack trace of the exception to the console.
            return null; // Returns null to indicate that an error occurred and the scene could not be created.
        }
    }
}