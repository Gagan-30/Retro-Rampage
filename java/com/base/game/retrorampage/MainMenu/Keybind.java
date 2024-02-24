// Package declaration that specifies the location of this class within the project's structure.
package com.base.game.retrorampage.MainMenu;

// Importing necessary JavaFX classes for scene and stage manipulation, as well as exception handling.

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Declares the Keybind class, which is responsible for creating and displaying the keybinds configuration screen.
public class Keybind {

    /**
     * Creates and returns a new scene for the keybind configuration screen, based on an FXML layout.
     *
     * @param previousScene The scene that was active before transitioning to the keybinds screen.
     * @param mainStage     The primary window (stage) of the application.
     * @return A Scene object representing the keybinds screen, or null if an error occurs during creation.
     */
    public Scene createKeybindScene(Scene previousScene, Stage mainStage) {
        try {
            // Initializes an FXMLLoader with the path to the "Keybind-view.fxml" file.
            // This FXML file defines the layout of the keybind configuration screen.
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Keybind-view.fxml"));
            Parent root = fxmlLoader.load(); // Loads the FXML file and returns the root node.

            // Creates a new Scene using the root node from the FXML file.
            // The scene is set to a predefined size, which can be adjusted if necessary.
            Scene scene = new Scene(root, 640, 480);

            // Checks if the main stage was previously in fullscreen mode.
            boolean wasFullScreen = mainStage.isFullScreen();

            // Sets the newly created scene to the main stage.
            mainStage.setScene(scene);

            // If the stage was previously in fullscreen mode, re-enables fullscreen mode.
            if (wasFullScreen) {
                mainStage.setFullScreen(true);
            }

            // Retrieves the controller associated with the FXML file loaded.
            // This controller will manage user interactions within the keybinds configuration screen.
            KeybindController keybindController = fxmlLoader.getController();
            // Passes references of the previous scene and the main stage to the controller.
            // This allows the controller to perform actions like updating keybinds or returning to the previous scene.
            keybindController.setPreviousScene(previousScene);
            keybindController.setStage(mainStage);

            // Returns the newly created scene, ready to be displayed.
            return scene;
        } catch (IOException e) {
            // Catches any IOExceptions that occur during the loading of the FXML file.
            e.printStackTrace(); // Prints the stack trace of the exception to the console.
            return null; // Returns null to indicate that an error occurred and the scene could not be created.
        }
    }
}