// Import necessary JavaFX libraries
package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

// Define the SoundController class
public class SoundController {
    private final Config config; // Configuration object
    private Scene previousScene;
    private Stage stage;
    @FXML
    private Slider volumeSlider; // Slider for volume control
    @FXML
    private TextField volumeTextField; // Text field for displaying volume
    @FXML
    private CheckBox muteCheckBox; // Checkbox for muting the sound

    // Constructor for SoundController
    public SoundController() {
        this.config = new Config("config.txt"); // Initialize the configuration
    }

    // Setter for the previous scene
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    // Setter for the stage
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Initialization method called by JavaFX
    @FXML
    private void initialize() {
        loadConfig(); // Load configuration settings
        setupListeners(); // Setup event listeners
    }

    // Setup event listeners for UI components
    private void setupListeners() {
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> syncSliderWithTextField());
        volumeTextField.textProperty().bindBidirectional(volumeSlider.valueProperty(), new NumberStringConverter());
        volumeTextField.setOnKeyReleased(this::onVolumeTextFieldChanged);
    }

    // Synchronize slider value with text field
    private void syncSliderWithTextField() {
        volumeTextField.setText(String.valueOf((int) volumeSlider.getValue()));
        updateMuteCheckBoxState();
    }

    // Handle changes in the volume text field
    public void onVolumeTextFieldChanged(KeyEvent event) {
        // Check if the key released is a digit, arrow, backspace, or delete key
        if (event.getCode().isDigitKey() || event.getCode().isArrowKey() || event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
            try {
                int value = Integer.parseInt(volumeTextField.getText()); // Convert the text field content to integer
                if (value < 0) {
                    volumeSlider.setValue(0); // Set slider to minimum if value is below range
                } else if (value > 100) {
                    volumeSlider.setValue(100); // Set slider to maximum if value is above range
                } else {
                    volumeSlider.setValue(value); // Set slider to entered value if within range
                }
            } catch (NumberFormatException e) {
                volumeTextField.setText(String.valueOf((int) volumeSlider.getValue())); // Reset to slider's value on invalid input
            }
        }
    }

    // Handle click event for the mute checkbox
    @FXML
    public void onMuteBoxClick() {
        if (muteCheckBox.isSelected()) {
            volumeSlider.setValue(0); // Mute the sound
        } else {
            volumeSlider.setValue(config.loadVolumeSetting()); // Set volume to saved value
        }
    }

    // Save configuration settings
    private void saveConfig() {
        int currentVolume = (int) volumeSlider.getValue(); // Get current volume value
        new Thread(() -> {
            config.saveVolumeSetting(currentVolume); // Save the volume setting in a separate thread
            System.out.println("Volume saved: " + currentVolume);
        }).start();
    }

    // Load configuration settings
    private void loadConfig() {
        int volume = config.loadVolumeSetting(); // Load the saved volume setting
        volumeSlider.setValue(volume); // Set the slider to the loaded volume
        volumeTextField.setText(String.valueOf(volume)); // Set the text field to display the loaded volume
        updateMuteCheckBoxState(); // Update the mute checkbox state based on the volume
    }

    // Update the state of the mute checkbox
    private void updateMuteCheckBoxState() {
        muteCheckBox.setSelected(volumeSlider.getValue() == 0); // Set checkbox state based on volume
    }

    // Update the title of the stage
    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle); // Set the stage title to the specified new title
        }
    }

    // Handle the return button click event
    @FXML
    public void onReturnButtonClick() {
        saveConfig(); // Save the configuration settings

        // Check if the stage is in fullscreen mode
        boolean wasFullScreen = stage.isFullScreen();

        // Set the main stage's scene back to the previous scene
        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);

            // Re-enable fullscreen if it was previously set
            if (wasFullScreen) {
                stage.setFullScreen(true);
            }

            updateTitle("Settings"); // Update the stage title
        }
    }
}