package com.base.game.retrorampage.MainMenu;

// Import necessary JavaFX classes
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsController {

    // Private fields to store the previous scene, the main application stage, and scenes for each settings category
    private Scene previousScene;
    private Stage stage;
    private Scene graphicsScene;
    private Scene soundScene;
    private Scene keybindScene;

    // Instances of classes for managing specific settings categories
    private final Graphics graphics = new Graphics();
    private final Sound sound = new Sound();
    private final Keybind keybind = new Keybind();

    // Sets the previous scene, allowing for navigation back to the main menu or previous state
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    // Sets the main stage of the application, enabling modifications to the window's properties
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Updates the title of the main application window
    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    // Event handler for clicking the Graphics settings button
    @FXML
    protected void onGraphicsButtonClick() {
        // Preserve fullscreen state across scene transitions
        boolean wasFullScreen = stage.isFullScreen();

        // initialization of the graphics settings scene
        if (graphicsScene == null) {
            graphicsScene = graphics.createGraphicsScene(stage.getScene(), stage);
        }
        stage.setScene(graphicsScene);

        // Reapply fullscreen mode if it was previously enabled
        if (wasFullScreen) {
            stage.setFullScreen(true);
        }

        // Update the window title to reflect the current settings category
        updateTitle("Graphics Settings");
    }

    // These methods follow the same pattern as onGraphicsButtonClick()
    // Event handler for clicking the Sound settings button
    @FXML
    protected void onSoundButtonClick() {
        // Preserve fullscreen state across scene transitions
        boolean wasFullScreen = stage.isFullScreen();

        // initialization of the graphics settings scene
        if (soundScene == null) {
            soundScene = sound.createSoundScene(stage.getScene(), stage);
        }
        stage.setScene(soundScene);

        // Reapply fullscreen mode if it was previously enabled
        if (wasFullScreen) {
            stage.setFullScreen(true);
        }

        // Update the window title to reflect the current settings category
        updateTitle("Sound Settings");
    }

    // Event handler for clicking the Keybind settings button
    @FXML
    protected void onKeybindButtonClick() {
        // Preserve fullscreen state across scene transitions
        boolean wasFullScreen = stage.isFullScreen();

        // Initialization of the graphics settings scene
        if (keybindScene == null) {
            keybindScene = keybind.createKeybindScene(stage.getScene(), stage);
        }
        stage.setScene(keybindScene);

        // Reapply fullscreen mode if it was previously enabled
        if (wasFullScreen) {
            stage.setFullScreen(true);
        }

        // Update the window title to reflect the current settings category
        updateTitle("Keybind Settings");
    }

    // Event handler for the "Return" button, enabling navigation back to the previous scene
    @FXML
    public void onReturnButtonClick() {
        // Preserve fullscreen state when returning to the previous scene
        boolean wasFullScreen = stage.isFullScreen();

        // Set the stage's scene back to the previous scene if it's not null
        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);

            // Reapply fullscreen mode if it was previously enabled
            if (wasFullScreen) {
                stage.setFullScreen(true);
            }

            // Update the window title to "Main Menu"
            updateTitle("Main Menu");
        }
    }
}
