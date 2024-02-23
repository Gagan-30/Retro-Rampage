// Package declaration aligns with the application's structure, indicating this class is part of the MainMenu package.
package com.base.game.retrorampage.MainMenu;

// Import statements for necessary JavaFX classes.

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Defines the ExitController class.
public class ExitController {

    private Scene previousScene; // A private field to store the scene displayed before the exit screen.
    private Stage stage; // A field to reference the main application window (stage).

    /**
     * Sets the previous scene.
     * This method allows the controller to remember which scene was displayed before transitioning to the exit screen,
     * enabling the application to return to that scene if necessary.
     *
     * @param previousScene The Scene object representing the previous scene.
     */
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    /**
     * Sets the main application window (stage).
     * This is necessary for changing scenes or updating stage properties, such as the title.
     *
     * @param stage The Stage object representing the main application window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Updates the title of the main application window (stage).
     * This private method is used internally to change the window's title when returning to the main menu.
     *
     * @param newTitle The new title to be set for the window.
     */
    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    /**
     * Event handler for the "Return" button click.
     * When invoked, it checks if the stage was in fullscreen mode before switching scenes,
     * returns to the previous scene, and re-enables fullscreen mode if it was previously active.
     */
    @FXML
    public void onReturnButtonClick() {
        if (previousScene != null && stage != null) {
            boolean wasFullScreen = stage.isFullScreen(); // Check if fullscreen is active before switching scenes
            stage.setScene(previousScene);
            if (wasFullScreen) {
                stage.setFullScreen(true); // Re-enable fullscreen if it was previously active
            }
            updateTitle("Main Menu");
        }
    }

    /**
     * Event handler for the "Quit" button click.
     * This method exits the application when the quit button is clicked, effectively closing the game.
     */
    @FXML
    public void onQuitButtonClick() {
        System.exit(0); // Terminates the currently running JVM.
    }
}
