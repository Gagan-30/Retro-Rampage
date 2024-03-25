package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controller class for the About screen.
 */
public class AboutController {

    private Scene previousScene; // Variable to store the previously displayed scene.
    private Stage stage; // Variable to store the main application stage.

    /**
     * Sets the previous scene that was displayed before the current scene.
     *
     * @param previousScene The Scene object representing the previous scene.
     */
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    /**
     * Sets the main application window (stage) for this controller.
     *
     * @param stage The Stage object representing the main application window.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Updates the title of the main application window (stage).
     *
     * @param newTitle The new title to set for the window.
     */
    private void updateTitle(String newTitle) {
        // Check if the stage reference is not null before attempting to update the title.
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    /**
     * Handles the return button click event to navigate back to the previous scene.
     */
    @FXML
    public void onReturnButtonClick() {
        // Check if the main application window (stage) is currently in full screen mode.
        boolean wasFullScreen = stage.isFullScreen();

        // Check if there is a previous scene and a valid stage before attempting to switch scenes.
        if (previousScene != null && stage != null) {
            // Set the stage's scene to the previous scene to go back.
            stage.setScene(previousScene);

            // If the stage was in full screen mode before, re-enable full screen mode.
            if (wasFullScreen) {
                stage.setFullScreen(true);
            }

            // Update the window's title to "Level Menu".
            updateTitle("Level Menu");
        }
    }
}
