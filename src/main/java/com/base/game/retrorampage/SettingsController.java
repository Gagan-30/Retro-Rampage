package com.base.game.retrorampage;


import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsController {

    private Stage mainStage;
    private Scene graphicsScene;
    private Scene soundScene;
    private Scene keybindScene;

    private Graphics graphics = new Graphics();
    private Sound sound = new Sound();
    private Keybind keybind = new Keybind();

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void updateTitle(String newTitle) {
        if (mainStage != null) {
            mainStage.setTitle(newTitle);
        }
    }

    @FXML
    protected void onGraphicsButtonClick() {
        if (graphicsScene == null) {
            // Load the graphics scene if not loaded
            graphicsScene = graphics.createGraphicsScene();
        }
        updateTitle("Graphics Settings");
        mainStage.setScene(graphicsScene);
    }

    @FXML
    public void onSoundButtonClick() {
        if (soundScene == null) {
            // Load the sound scene if not loaded
            soundScene = sound.createSoundScene();
        }
        updateTitle("Sound Settings");
        mainStage.setScene(soundScene);
    }

    @FXML
    public void onKeybindButtonClick() {
        if (keybindScene == null) {
            // Load the keybind scene if not loaded
            keybindScene = keybind.createKeybindScene();
        }
        updateTitle("Keybind Settings");
        mainStage.setScene(keybindScene);
    }

    @FXML
    public void onReturnButtonClick() {

    }
}
