package com.base.game.retrorampage;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SoundController {
    private Scene previousScene;
    private Stage stage;
    private Config config = new Config("config.txt");

    @FXML
    private Slider volumeSlider;
    @FXML
    private TextField volumeTextField;
    @FXML
    private Button muteButton;

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void updateTitle() {
        if (stage != null) {
            stage.setTitle("Main Menu");
        }
    }

    @FXML
    private void initialize() {
        loadConfig();

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            volumeTextField.setText(String.valueOf(newValue.intValue()));
            updateVolumeFromTextField();
        });

        volumeTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                updateVolumeFromTextField();
            }
        });

        TextFormatter<Integer> textFormatter = getIntegerTextFormatter();
        volumeTextField.setTextFormatter(textFormatter);
        updateMuteButtonState();
    }

    private void updateMuteButtonState() {
        if (volumeSlider.getValue() > 0) {
            muteButton.setText("Audio: Unmuted");
        } else {
            muteButton.setText("Audio: Muted");
        }
    }

    private TextFormatter<Integer> getIntegerTextFormatter() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (Pattern.matches("\\d{0,3}", newText)) {
                return change;
            }
            return null;
        };

        StringConverter<Integer> converter = new StringConverter<>() {
            @Override
            public Integer fromString(String string) {
                return Integer.parseInt(string);
            }

            @Override
            public String toString(Integer object) {
                return object.toString();
            }
        };

        return new TextFormatter<>(converter, config.getVolume(), filter);
    }

    public void onVolumeTextFieldChanged(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            updateVolumeFromTextField();
        }
    }

    private void updateVolumeFromTextField() {
        String input = volumeTextField.getText();
        String regex = "^(100|[1-9]?[0-9])$";

        if (input.matches(regex)) {
            int volume = Integer.parseInt(input);
            volume = Math.max(0, Math.min(100, volume));
            volumeSlider.setValue(volume);
            saveConfig();
            updateMuteButtonState();
        } else {
            System.out.println("Invalid input. Please enter a valid integer between 0 and 100.");
            volumeTextField.setText(String.valueOf((int) volumeSlider.getValue()));
            saveConfig();
        }
    }

    @FXML
    public void onMuteButtonClick() {
        if (volumeSlider.getValue() > 0) {
            volumeSlider.setValue(0);
            muteButton.setText("Audio: Muted");
            saveConfig();

        } else {
            volumeSlider.setValue(config.getVolume());
            muteButton.setText("Audio: Unmuted");
            saveConfig();
        }
        saveConfig();
    }

    private void saveConfig() {
         int currentVolume = (int) volumeSlider.getValue();
         config.setVolume(currentVolume);
         System.out.println("Volume = " + currentVolume);
    }

    private void loadConfig() {
        int volume = config.getVolume();
        volumeSlider.setValue(volume);
        volumeTextField.setText(String.valueOf(volume));
    }

    @FXML
    public void onReturnButtonClick() {
        saveConfig();
        if (previousScene != null && stage != null) {
            stage.setScene(previousScene);
            updateTitle();
        }
    }
}