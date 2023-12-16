package com.base.game.retrorampage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SoundController {
    private Scene previousScene;
    private Stage stage;
    private int previousVolume = 50;
    @FXML
    private Slider volumeSlider;

    @FXML
    private TextField volumeTextField;

    @FXML
    private Label audioStatusLabel;

    @FXML
    private Button muteButton;

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

    // Initialize the controller after FXML file is loaded
    @FXML
    private void initialize() {
        // Set up a listener for the volume slider
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Update the volume text field when the slider is moved
            volumeTextField.setText(String.valueOf(newValue.intValue()));
        });

        // Add a listener for the volume text field focus property
        volumeTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // When focus is lost, update the slider with the typed volume
                updateVolumeFromTextField();
            }
        });

        // Set default values
        volumeSlider.setValue(previousVolume);
        volumeTextField.setText(String.valueOf(previousVolume));
    }

    // Method to handle volume change from the text field
    public void onVolumeTextFieldChanged(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            updateVolumeFromTextField();
        }
    }

    private void updateVolumeFromTextField() {
        try {
            // Parse the text field value to an integer
            int volume = Integer.parseInt(volumeTextField.getText());

            // Ensure the volume is within the valid range (0-100)
            volume = Math.max(0, Math.min(100, volume));

            // Update the slider value
            volumeSlider.setValue(volume);
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid integer
            e.printStackTrace();
        }
    }

    @FXML
    public void onMuteButtonClick() {
        if (volumeSlider.getValue() > 0) {
            // Store the current volume in a variable
            previousVolume = (int) volumeSlider.getValue();

            // Mute the audio by setting the volume to zero
            volumeSlider.setValue(0);

            // Update the button text to indicate the mute state
            muteButton.setText("Audio: Muted");
        } else {
            // Unmute the audio by setting the volume back to the previous state
            volumeSlider.setValue(previousVolume);

            // Update the button text to indicate the unmute state
            muteButton.setText("Audio: Unmuted");
        }
    }

    // Event handler for the "Return" button
    @FXML
    public void onReturnButtonClick() {
        // Print the previous scene and stage for debugging purposes
        System.out.println("Previous Scene: " + previousScene);
        System.out.println("Stage: " + stage);

        // Check if both previous scene and stage are not null
        if (previousScene != null && stage != null) {
            // Set the main stage's scene back to the previous scene
            stage.setScene(previousScene);
            updateTitle("Main Menu");
        }
    }

}
