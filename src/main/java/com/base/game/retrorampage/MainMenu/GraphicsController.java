package com.base.game.retrorampage.MainMenu;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class GraphicsController {
    private final Config config; // A Config object to manage loading and saving of graphics settings.
    private final String[] resolutions = {"640 x 480", "800 x 600", "1280 x 720", "1920 x 1080"}; // Supported resolutions.
    private Scene previousScene; // Reference to the previous scene for navigation purposes.
    private Stage stage; // The main application window.
    private int currentResolutionIndex = 0; // Index of the currently selected resolution in the resolutions array.

    @FXML
    private Button resolutionButton; // Button to toggle through resolutions.
    @FXML
    private CheckBox fullscreenCheckBox; // Checkbox to toggle fullscreen mode.

    public GraphicsController() {
        this.config = new Config("config.txt"); // Initializes the Config object with a specified configuration file.
        System.out.println("[GraphicsController] Constructor called");
    }

    @FXML
    public void initialize() {
        // Method called after all @FXML annotated members have been injected and processed.
        Platform.runLater(() -> {
            loadGraphicsConfig(); // Loads saved graphics settings from the configuration.
            applyFullScreen(); // Applies the fullscreen setting based on the loaded configuration.
            applyResolution(resolutions[currentResolutionIndex]); // Applies the resolution based on the loaded configuration.
            updateResolutionButtonText(); // Updates the resolution button text to reflect the current setting.
        });
    }

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene; // Setter for the previous scene.
    }

    public void setStage(Stage stage) {
        this.stage = stage; // Setter for the main stage.
    }

    private void updateTitle(String newTitle) {
        // Updates the title of the main stage, if available.
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    @FXML
    private void handleChangeResolution() {
        // Event handler for resolution change action.
        currentResolutionIndex = (currentResolutionIndex + 1) % resolutions.length; // Cycles through available resolutions.
        updateResolutionButtonText(); // Updates the button text to the new resolution.
        applyResolution(resolutions[currentResolutionIndex]); // Applies the new resolution setting to the stage.
    }

    private void updateResolutionButtonText() {
        // Updates the resolution button text based on the current resolution setting.
        resolutionButton.setText("Screen Resolution: " + resolutions[currentResolutionIndex]);
    }

    private void applyResolution(String resolution) {
        // Applies the specified resolution to the stage by setting its width and height.
        if (stage != null) {
            String[] parts = resolution.split(" x "); // Splits the resolution string into width and height components.
            if (parts.length == 2) {
                int width = Integer.parseInt(parts[0].trim()); // Parses the width part of the resolution string.
                int height = Integer.parseInt(parts[1].trim()); // Parses the height part of the resolution string.
                stage.setWidth(width); // Sets the stage width.
                stage.setHeight(height); // Sets the stage height.
            }
        }
    }

    public void applyFullScreen() {
        // Applies the fullscreen setting based on the state of the fullscreen checkbox.
        if (stage != null) {
            boolean isFullScreen = fullscreenCheckBox.isSelected(); // Determines if the checkbox is selected.
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH); // Disables the default fullscreen exit key combination.
            stage.setFullScreenExitHint(""); // Removes the default fullscreen exit hint.
            stage.setFullScreen(isFullScreen); // Sets the stage to fullscreen mode based on the checkbox state.
        }
    }

    private void saveGraphicsConfig() {
        // Saves the current graphics settings to the configuration file using a separate thread.
        if (config != null) {
            new Thread(() -> {
                String resolution = resolutions[currentResolutionIndex]; // Gets the current resolution setting.
                boolean isFullscreen = fullscreenCheckBox.isSelected(); // Determines if fullscreen mode is enabled.
                config.saveResolutionSetting(resolution); // Saves the resolution setting to the configuration file.
                config.saveFullScreenSetting(isFullscreen); // Saves the fullscreen setting to the configuration file.
            }).start();
        }
    }

    private void loadGraphicsConfig() {
        // Loads the graphics settings from the configuration file.
        boolean isFullscreen = config.loadFullscreenSetting(); // Loads the fullscreen setting.
        String resolution = config.loadResolutionSetting(); // Loads the resolution setting.
        if (fullscreenCheckBox != null) {
            fullscreenCheckBox.setSelected(isFullscreen); // Applies the loaded fullscreen setting to the checkbox.
        }
        currentResolutionIndex = java.util.Arrays.asList(resolutions).indexOf(resolution); // Finds the index of the loaded resolution in the supported resolutions array.
        currentResolutionIndex = currentResolutionIndex == -1 ? 0 : currentResolutionIndex; // Sets the index to 0 if the loaded resolution is not found in the array.
        updateResolutionButtonText(); // Updates the button text to reflect the loaded resolution setting.
        applyResolution(resolutions[currentResolutionIndex]); // Applies the loaded resolution setting to the stage.
    }

    @FXML
    public void onReturnButtonClick() {
        // Event handler for the return action, which saves the current graphics settings and returns to the previous scene.
        saveGraphicsConfig(); // Saves the current graphics settings before navigating away.

        if (previousScene != null && stage != null) {
            stage.setScene(previousScene); // Sets the main stage's scene back to the previous scene.
            boolean wasFullScreen = stage.isFullScreen(); // Checks if fullscreen mode was previously enabled.
            if (wasFullScreen) {
                stage.setFullScreen(true); // Re-enables fullscreen mode if it was previously set.
            }
            updateTitle("Settings"); // Updates the stage title to "Settings".
        }
    }
}