package com.base.game.retrorampage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SoundController {
    private static final String CONFIG_FILE_PATH = "config.properties";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Scene previousScene;
    private Stage stage;
    private int previousVolume = 50;
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
        // Load configuration file on initialization
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

        // Set the initial volume value from the config file
        volumeSlider.setValue(previousVolume);
        volumeTextField.setText(String.valueOf(previousVolume));

        TextFormatter<Integer> textFormatter = getIntegerTextFormatter();
        volumeTextField.setTextFormatter(textFormatter);

        // Check if the initial volume is 0 and update mute button
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

        TextFormatter<Integer> textFormatter = new TextFormatter<>(converter, previousVolume, filter);
        return textFormatter;
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

            // Update mute button state after setting the volume
            updateMuteButtonState();
        } else {
            System.out.println("Invalid input. Please enter a valid integer between 0 and 100.");
            volumeTextField.setText(String.valueOf((int) volumeSlider.getValue()));
        }

        System.out.println("Input: " + volumeTextField.getText());
    }

    @FXML
    public void onMuteButtonClick() {
        if (volumeSlider.getValue() > 0) {
            previousVolume = (int) volumeSlider.getValue();
            volumeSlider.setValue(0);
            muteButton.setText("Audio: Muted");
        } else {
            volumeSlider.setValue(previousVolume);
            muteButton.setText("Audio: Unmuted");
        }
    }

    private void saveConfig() {
        try (FileOutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
            Properties prop = new Properties();
            prop.setProperty("volume", String.valueOf((int) volumeSlider.getValue()));
            prop.store(output, null);

            System.out.println("Config saved. Volume: " + (int) volumeSlider.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            Properties prop = new Properties();
            prop.load(input);

            String volumeStr = prop.getProperty("volume");

            if (volumeStr != null) {
                int volume = Integer.parseInt(volumeStr);
                previousVolume = volume;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
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