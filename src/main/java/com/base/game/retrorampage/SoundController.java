package com.base.game.retrorampage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class SoundController {
    private Scene previousScene;
    private Stage stage;
    private Config config;

    @FXML
    private Slider volumeSlider;
    @FXML
    private TextField volumeTextField;
    @FXML
    private Button muteButton;

    public SoundController() {
        this.config = new Config("config.txt");
    }

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        loadConfig();
        setupListeners();
    }

    private void setupListeners() {
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> syncSliderWithTextField());
        volumeTextField.textProperty().bindBidirectional(volumeSlider.valueProperty(), new NumberStringConverter());
        volumeTextField.setOnKeyReleased(this::onVolumeTextFieldChanged);
    }

    private void syncSliderWithTextField() {
        volumeTextField.setText(String.valueOf((int) volumeSlider.getValue()));
        updateMuteButtonState();
    }

    public void onVolumeTextFieldChanged(KeyEvent event) {
        if (event.getCode().isDigitKey() || event.getCode().isArrowKey()) {
            syncSliderWithTextField();
        }
    }

    @FXML
    public void onMuteButtonClick() {
        if (volumeSlider.getValue() > 0) {
            volumeSlider.setValue(0);
            muteButton.setText("Audio: Muted");
        } else {
            volumeSlider.setValue(config.getVolume());
            muteButton.setText("Audio: Unmuted");
        }
    }

    private void saveConfig() {
        int currentVolume = (int) volumeSlider.getValue();
        new Thread(() -> {
            config.setVolume(currentVolume);
            System.out.println("Volume saved: " + currentVolume);
        }).start();
    }

    private void loadConfig() {
        int volume = config.getVolume();
        volumeSlider.setValue(volume);
        volumeTextField.setText(String.valueOf(volume));
        updateMuteButtonState();
    }

    private void updateMuteButtonState() {
        muteButton.setText(volumeSlider.getValue() > 0 ? "Audio: Unmuted" : "Audio: Muted");
    }

    @FXML
    public void onReturnButtonClick() {
        saveConfig();

        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);
            stage.setTitle("Main Menu");
        }
    }
}