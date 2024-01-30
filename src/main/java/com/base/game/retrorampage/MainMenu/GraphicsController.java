package com.base.game.retrorampage.MainMenu;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

public class GraphicsController {
    private final Config config;
    private final String[] resolutions = {"640 x 480", "800 x 600", "1280 x 720", "1920 x 1080"};
    private Scene previousScene;
    private Stage stage;
    private int currentResolutionIndex = 0;

    @FXML
    private Button resolutionButton;
    @FXML
    private CheckBox fullscreenCheckBox;

    public GraphicsController() {
        this.config = new Config("config.txt");
        System.out.println("[GraphicsController] Constructor called");
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            loadGraphicsConfig();
            applyFullScreen();
            applyResolution(resolutions[currentResolutionIndex]);
            updateResolutionButtonText();
        });
    }

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    @FXML
    private void handleChangeResolution() {
        System.out.println("[GraphicsController] handleChangeResolution called");
        currentResolutionIndex = (currentResolutionIndex + 1) % resolutions.length;
        updateResolutionButtonText();
        applyResolution(resolutions[currentResolutionIndex]);
    }

    private void updateResolutionButtonText() {
        resolutionButton.setText("Screen Resolution: " + resolutions[currentResolutionIndex]);
    }

    private void applyResolution(String resolution) {
        System.out.println("[GraphicsController] applyResolution called with resolution: " + resolution);
        if (stage != null) {
            String[] parts = resolution.split(" x ");
            if (parts.length == 2) {
                int width = Integer.parseInt(parts[0].trim());
                int height = Integer.parseInt(parts[1].trim());
                stage.setWidth(width);
                stage.setHeight(height);
            }
        }
    }

    public void applyFullScreen() {
        System.out.println("[GraphicsController] applyFullScreen called");
        if (stage != null) {
            boolean isFullScreen = fullscreenCheckBox.isSelected(); // Use the checkbox state directly
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setFullScreenExitHint("");
            stage.setFullScreen(isFullScreen);
        }
    }

    private void saveGraphicsConfig() {
        if (config != null) {
            new Thread(() -> {
                System.out.println("[GraphicsController] config is not null, saving settings");
                String resolution = resolutions[currentResolutionIndex];
                boolean isFullscreen = fullscreenCheckBox.isSelected(); // Get the actual fullscreen state
                config.saveResolutionSetting(resolution);
                config.saveFullScreenSetting(isFullscreen);
            }).start();
        } else {
            System.out.println("[GraphicsController] config is null");
        }
    }

    private void loadGraphicsConfig() {
        System.out.println("[GraphicsController] loadGraphicsConfig called");
        boolean isFullscreen = config.loadFullscreenSetting();
        String resolution = config.loadResolutionSetting();
        if (fullscreenCheckBox != null) {
            fullscreenCheckBox.setSelected(isFullscreen);
        }
        currentResolutionIndex = java.util.Arrays.asList(resolutions).indexOf(resolution);
        currentResolutionIndex = currentResolutionIndex == -1 ? 0 : currentResolutionIndex;
        updateResolutionButtonText();
        applyResolution(resolutions[currentResolutionIndex]);
    }

    @FXML
    public void onReturnButtonClick() {
        saveGraphicsConfig();

        // Check if the stage is in fullscreen mode
        boolean wasFullScreen = stage.isFullScreen();

        // Set the main stage's scene back to the previous scene
        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);

            // Re-enable fullscreen if it was previously set
            if (wasFullScreen) {
                stage.setFullScreen(true);
            }

            updateTitle("Settings");
        }
    }
}