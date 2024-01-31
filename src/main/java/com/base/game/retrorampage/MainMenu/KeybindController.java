package com.base.game.retrorampage.MainMenu;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class KeybindController {
    private Scene previousScene;
    private Stage stage;
    private Button activeButton; // Button that is waiting for keybind

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
    public void onActionButtonClick(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            // Reset the text of the previously active button if it's still listening
            if (activeButton != null && "Listening".equals(activeButton.getText())) {
                activeButton.setText("Click to set key"); // Or whatever the default text should be
            }

            activeButton = (Button) event.getSource();
            activeButton.setText("Listening");
            setupInputListeners();
        }
    }

    private void setupInputListeners() {
        // Listen for the next key press or mouse click
        stage.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        stage.addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
    }

    private void handleKeyPress(KeyEvent event) {
        if (activeButton != null) {
            String keyName = event.getCode().toString();
            updateButtonAndRemoveListeners(keyName);
        }
    }

    private void handleMouseClick(MouseEvent event) {
        if (activeButton != null) {
            String clickType = event.getButton().toString();
            updateButtonAndRemoveListeners(clickType);
        }
    }

    private void updateButtonAndRemoveListeners(String input) {
        if (activeButton != null) {
            activeButton.setText(input);
            activeButton = null;
        }

        // Remove event listeners
        stage.removeEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);
        stage.removeEventHandler(MouseEvent.MOUSE_CLICKED, this::handleMouseClick);
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

            updateTitle("Settings");
        }
    }
}
