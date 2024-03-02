// Package declaration specifies where this class is located within the project's structure.
package com.base.game.retrorampage.MainMenu;

// Import statements for necessary JavaFX classes and exception handling.

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Defines the Exit class.
public class Exit {

    // Method to create and return a Scene for the exit screen.
    public Scene createExitScene(Scene previousScene, Stage mainStage) {
        try {
            // Initializes an FXMLLoader with the path to the "Exit-view.fxml" file.
            // This FXML file defines the layout of the exit screen.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Exit-view.fxml"));
            Parent root = fxmlLoader.load(); // Loads the FXML file and returns the root node.

            // Creates a new Scene using the root node from the FXML file.
            // The scene is set to a predefined size, which can be adjusted if necessary.
            Scene scene = new Scene(root, 640, 480);

            // Checks if the main stage was in fullscreen mode before changing scenes.
            boolean wasFullScreen = mainStage.isFullScreen();

            // Sets the newly created scene to the main stage.
            mainStage.setScene(scene);

            // If the stage was previously in fullscreen mode, re-enables fullscreen mode.
            if (wasFullScreen) {
                mainStage.setFullScreen(true);
            }

            // Retrieves the controller associated with the FXML file loaded.
            // This controller will manage user interactions within the exit scene.
            ExitController exitController = fxmlLoader.getController();
            // Passes references of the previous scene and the main stage to the controller.
            // This allows the controller to perform actions like returning to the previous scene.
            exitController.setPreviousScene(previousScene);
            exitController.setStage(mainStage);

            // Returns the newly created scene, ready to be displayed.
            return scene;
        } catch (IOException e) {
            // Catches any IOExceptions that occur during the loading of the FXML file.
            e.printStackTrace(); // Prints the stack trace of the exception to the console.
            return null; // Returns null to indicate that an error occurred and the scene could not be created.
        }
    }
}