package com.base.game.retrorampage.MainMenu;

import com.base.game.retrorampage.LevelGeneration.Level;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Defines the DifficultySelectionController class.
public class DifficultySelectionController {

    private Scene previousScene; // A private field to store the scene that was displayed before the current scene.
    private Stage stage; // A field to reference the main application window (stage).
    private Level level;

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

    // Setter method to set the Level instance
    public void setMainGame(Level level) {
        this.level = level;
    }

    @FXML
    private void onEasyButtonClick() {
        boolean wasFullScreen = stage.isFullScreen();
        level.start(stage);
        // If the stage was in full screen mode before, re-enable full screen mode.
        if (wasFullScreen) {
            stage.setFullScreen(true);
        }
        level.setGlobalEnemyHealth(100);
        level.initialize();
        level.update();
        level.getLevelGenerator().gameOver();
    }

    // Event handler for the "Medium" button click. It should contain logic for handling what happens when the Medium difficulty is selected.
    @FXML
    private void onMediumButtonClick() {
        boolean wasFullScreen = stage.isFullScreen();
        level.start(stage);
        // If the stage was in full screen mode before, re-enable full screen mode.
        if (wasFullScreen) {
            stage.setFullScreen(true);
        }
        level.setGlobalEnemyHealth(150);
        level.initialize();
        level.update();
        level.getLevelGenerator().gameOver();
    }

    // Event handler for the "Hard" button click. It should contain logic for handling what happens when the Hard difficulty is selected.
    @FXML
    private void onHardButtonClick() {
        boolean wasFullScreen = stage.isFullScreen();
        level.start(stage);
        // If the stage was in full screen mode before, re-enable full screen mode.
        if (wasFullScreen) {
            stage.setFullScreen(true);
        }
        level.setGlobalEnemyHealth(200);
        level.initialize();
        level.update();
        level.getLevelGenerator().gameOver();
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

            updateTitle("Main Menu"); // Updates the stage's title to "Level Menu".
        }
    }
}