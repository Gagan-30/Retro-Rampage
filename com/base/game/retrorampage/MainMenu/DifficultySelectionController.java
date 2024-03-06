// Package declaration aligns with the application's structure, indicating this class is part of the MainMenu package.
package com.base.game.retrorampage.MainMenu;

// Import statements for required JavaFX classes.

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Defines the DifficultySelectionController class.
public class DifficultySelectionController {

    private Scene previousScene; // A private field to store the scene that was displayed before the current scene.
    private Stage stage; // A field to reference the main application window (stage).

    // Sets the previous scene. This method allows for navigation back to the previous scene when needed.
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    // Sets the main application window (stage). This is necessary for changing scenes or updating the stage properties.
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Updates the title of the main application window (stage). This private method is used internally to change the window title.
    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    // Event handler for the "Easy" button click. It should contain logic for handling what happens when the Easy difficulty is selected.
    @FXML
    private void onEasyButtonClick() {
        // Implementation of Easy difficulty selection logic.
    }

    // Event handler for the "Medium" button click. It should contain logic for handling what happens when the Medium difficulty is selected.
    @FXML
    private void onMediumButtonClick() {
        // Implementation of Medium difficulty selection logic.
    }

    // Event handler for the "Hard" button click. It should contain logic for handling what happens when the Hard difficulty is selected.
    @FXML
    private void onHardButtonClick() {
        // Implementation of Hard difficulty selection logic.
    }

    // Event handler for the "Survivor" button click. It should contain logic for handling what happens when the Survivor difficulty is selected.
    @FXML
    public void onSurvivorButtonClick() {
        // Implementation of Survivor difficulty selection logic.
    }

    // Event handler for the "Grounded" button click. It should contain logic for handling what happens when the Grounded difficulty is selected.
    @FXML
    public void onGroundedButtonClick() {
        // Implementation of Grounded difficulty selection logic.
    }

    // Event handler for the "Return" button click. It manages the transition back to the previous scene, potentially re-enabling fullscreen mode if it was active before.
    @FXML
    public void onReturnButtonClick() {
        if (previousScene != null && stage != null) {
            stage.setScene(previousScene); // Switches back to the previous scene.

            boolean wasFullScreen = stage.isFullScreen(); // Checks if fullscreen mode was active.
            if (wasFullScreen) {
                stage.setFullScreen(true); // Re-enables fullscreen mode if it was set before.
            }

            updateTitle("Main Menu"); // Updates the stage's title to "Main Menu".
        }
    }
}