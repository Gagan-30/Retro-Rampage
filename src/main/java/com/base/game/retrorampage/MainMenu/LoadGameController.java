package com.base.game.retrorampage.MainMenu;

// Import necessary JavaFX classes

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoadGameController {
    // Variables to store the previous scene and the current stage
    private Scene previousScene;
    private Stage stage; // Holds the main application window

    // Sets the previous scene, allowing the controller to switch back when needed
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    // Sets the main stage of the application, enabling the controller to make changes to the window
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Placeholder method for handling the event when the "Load Save File" button is clicked
    public void onLoadSaveFileButtonClick() {
        // Implementation for loading a save file would go here
    }

    // Placeholder method for handling the event when the "Create Save File" button is clicked
    public void onCreateSaveFileButtonClick() {
        // Implementation for creating a new save file would go here
    }

    // Updates the title of the main application window
    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    // Handles the event when the "Return" button is clicked, reverting to the previous scene
    @FXML
    public void onReturnButtonClick() {
        // Check if the stage is currently in fullscreen mode
        boolean wasFullScreen = stage.isFullScreen();

        // If the previous scene and stage are not null, switch back to the previous scene
        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);

            // If the stage was in fullscreen mode, re-enable this mode
            if (wasFullScreen) {
                stage.setFullScreen(true);
            }

            // Update the window's title to reflect the return to the main menu
            updateTitle("MainGame Menu");
        }
    }
}
