package com.base.game.retrorampage.MainMenu;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsController {

    private Scene previousScene;
    private Stage stage;
    private Scene graphicsScene;
    private Scene soundScene;
    private Scene keybindScene;

    private Graphics graphics = new Graphics();
    private Sound sound = new Sound();
    private Keybind keybind = new Keybind();

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
    protected void onGraphicsButtonClick() {
        boolean wasFullScreen = stage.isFullScreen();

        if (graphicsScene == null) {
            graphicsScene = graphics.createGraphicsScene(stage.getScene(), stage);
        }
        stage.setScene(graphicsScene);

        if (wasFullScreen) {
            stage.setFullScreen(true);
        }

        updateTitle("Graphics Settings");
    }

    @FXML
    protected void onSoundButtonClick() {
        boolean wasFullScreen = stage.isFullScreen();

        if (soundScene == null) {
            soundScene = sound.createSoundScene(stage.getScene(), stage);
        }
        stage.setScene(soundScene);

        if (wasFullScreen) {
            stage.setFullScreen(true);
        }

        updateTitle("Sound Settings");
    }

    @FXML
    protected void onKeybindButtonClick() {
        boolean wasFullScreen = stage.isFullScreen();

        if (keybindScene == null) {
            keybindScene = keybind.createKeybindScene(stage.getScene(), stage);
        }
        stage.setScene(keybindScene);

        if (wasFullScreen) {
            stage.setFullScreen(true);
        }

        updateTitle("Keybind Settings");
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
