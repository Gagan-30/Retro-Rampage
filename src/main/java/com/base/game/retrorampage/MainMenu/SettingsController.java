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
        if (graphicsScene == null) {
            // Load the graphics scene if not loaded
            graphicsScene = graphics.createGraphicsScene(stage.getScene(), stage);
        }
        updateTitle("Graphics Settings");
        stage.setScene(graphicsScene);
    }

    @FXML
    public void onSoundButtonClick() {
        if (soundScene == null) {
            // Load the sound scene if not loaded
            System.out.println("[Sound] Testing scene transition at " + java.time.LocalDateTime.now());

            soundScene = sound.createSoundScene(stage.getScene(), stage);
        }
        updateTitle("Sound Settings");
        stage.setScene(soundScene);
    }

    @FXML
    public void onKeybindButtonClick() {
        if (keybindScene == null) {
            // Load the keybind scene if not loaded
            keybindScene = keybind.createKeybindScene(stage.getScene(), stage);
        }
        updateTitle("Keybind Settings");
        stage.setScene(keybindScene);
    }

    // Event handler for the "Return" button
    @FXML
    public void onReturnButtonClick() {

        // Check if both previous scene and stage are not null
        if (previousScene != null && stage != null) {
            // Set the main stage's scene back to the previous scene
            System.out.println("[Sound] Testing scene transition at " + java.time.LocalDateTime.now());
            stage.setScene(previousScene);
            updateTitle("Main Menu");
        }
    }
}
