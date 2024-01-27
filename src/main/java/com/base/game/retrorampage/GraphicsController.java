package com.base.game.retrorampage;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GraphicsController {
    private Scene previousScene;
    private Stage stage;

    // Assuming these are the resolutions you want to toggle between.
    private String[] resolutions = { "640 x 480", "800 x 600", "1280 x 720", "1920 x 1080" };
    private int currentResolutionIndex = 3; // Start with the last resolution as default.

    @FXML
    private Button resolutionButton; // The button to display and change the resolution.

    @FXML
    private Button returnButton; // The return button to go back to the previous scene.

    // Initialize method is called after all @FXML annotated members have been injected.
    @FXML
    public void initialize() {
        updateResolutionButtonText();
    }

    // Method to set the previous scene
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    // Method to set the main stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Method called when the resolution button is clicked.
    @FXML
    private void handleChangeResolution() {
        // Cycle through the resolutions array.
        currentResolutionIndex = (currentResolutionIndex + 1) % resolutions.length;
        updateResolutionButtonText();
        // Here you can also call a method to actually change the screen resolution if needed.
    }

    // Method to update the text on the resolution button.
    private void updateResolutionButtonText() {
        resolutionButton.setText("Screen Resolution: " + resolutions[currentResolutionIndex]);
    }

    // Method called when the return button is clicked.
    @FXML
    private void onReturnButtonClick() {
        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);
            // Optionally, you can update the stage title or do other actions here.
        }
    }
}