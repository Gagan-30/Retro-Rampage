package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DifficultySelectionController {


    private Scene previousScene;
    private Stage stage; // Add a stage variable

    // Method to set the previous scene
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    // Method to set the main stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Method to update the title of the main stage
    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    @FXML
    private void onEasyButtonClick() {
        // Handle logic for selecting Easy difficulty
    }

    @FXML
    private void onMediumButtonClick() {
        // Handle logic for selecting Medium difficulty
    }

    @FXML
    private void onHardButtonClick() {
        // Handle logic for selecting Hard difficulty
    }

    @FXML
    public void onSurvivorButtonClick() {
        // Handle logic for selecting Survivor difficulty
    }

    @FXML
    public void onGroundedButtonClick() {
        // Handle logic for selecting Grounded difficulty
    }


    // Event handler for the "Return" button
    @FXML
    public void onReturnButtonClick() {
        // Check if the stage is in fullscreen mode
        boolean wasFullScreen = stage.isFullScreen();

        // Set the main stage's scene back to the previous scene
        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);

            // Re-enable fullscreen if it was previously set
            if (wasFullScreen) {
                stage.setFullScreen(true);
            }

            updateTitle("Main Menu");
        }
    }
}
