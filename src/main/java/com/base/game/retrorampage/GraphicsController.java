package com.base.game.retrorampage;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class GraphicsController {
    private Scene previousScene;
    private Stage stage;
    private Config config;

    private String[] resolutions = {"640 x 480", "800 x 600", "1280 x 720", "1920 x 1080"};
    private int currentResolutionIndex = 0;

    @FXML
    private Button resolutionButton;
    @FXML
    private CheckBox fullscreenCheckBox;

    public GraphicsController() {
        this.config = new Config("config.txt");
        // Don't call loadGraphicsConfig here, move it to initialize
    }

    @FXML
    public void initialize() {
        loadGraphicsConfig();
        updateResolutionButtonText();
        applyResolution(resolutions[currentResolutionIndex]);
        updateFullscreenState();
    }

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleChangeResolution() {
        currentResolutionIndex = (currentResolutionIndex + 1) % resolutions.length;
        updateResolutionButtonText();
        applyResolution(resolutions[currentResolutionIndex]);
    }

    private void updateResolutionButtonText() {
        resolutionButton.setText("Screen Resolution: " + resolutions[currentResolutionIndex]);
    }

    private void applyResolution(String resolution) {
        if (stage != null) {
            String[] parts = resolution.split(" x ");
            if (parts.length == 2) {
                int width = Integer.parseInt(parts[0]);
                int height = Integer.parseInt(parts[1]);
                stage.setWidth(width);
                stage.setHeight(height);
            }
        }
    }

    public void handleFullScreen() {
        if (stage != null) {
            boolean isFullScreen = stage.isFullScreen();
            stage.setFullScreen(!isFullScreen);
            if (fullscreenCheckBox != null) {
                fullscreenCheckBox.setSelected(!isFullScreen);
            }
        }
    }

    private void updateFullscreenState() {
        if (stage != null && fullscreenCheckBox != null) {
            stage.setFullScreen(fullscreenCheckBox.isSelected());
        }
    }

    private void saveGraphicsConfig() {
        if (fullscreenCheckBox != null) {
            String resolution = resolutions[currentResolutionIndex];
            boolean isFullscreen = fullscreenCheckBox.isSelected();
            config.saveResolutionSetting(resolution);
            config.saveFullScreenSetting(isFullscreen);
        }
    }

    private void loadGraphicsConfig() {
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
    private void onReturnButtonClick() {
        saveGraphicsConfig();
        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);
        }
    }
}
