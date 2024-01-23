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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class SoundController {
    private static final String CONFIG_FILE_PATH = "config.json";
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
        // Extract volume value
        int volume = (int) volumeSlider.getValue();

        // Create a Map to represent the configuration
        Map<String, Object> configMap = new HashMap<>();
        configMap.put("volume", volume);

        // Write the configuration to a JSON file
        saveConfigToJson(configMap);
    }

    private void saveConfigToJson(Map<String, Object> configMap) {
        System.out.println("Saving config to JSON: " + configMap);

        try (FileWriter fileWriter = new FileWriter(CONFIG_FILE_PATH)) {
            StringBuilder json = new StringBuilder("{\n");
            for (Map.Entry<String, Object> entry : configMap.entrySet()) {
                json.append(String.format("  \"%s\": %s,\n", entry.getKey(), entry.getValue()));
            }
            // Remove the trailing comma and newline
            json.deleteCharAt(json.length() - 2);
            json.append("}");

            fileWriter.write(json.toString());

            System.out.println("Config saved. Volume: " + configMap.get("volume"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error saving config: " + e.getMessage());
        }
    }

    private void loadConfig() {
        // Load the configuration from the JSON file
        Map<String, Object> configMap = loadConfigFromJson();

        // Update UI or other logic based on the loaded configuration
        if (configMap != null) {
            if (configMap.containsKey("volume")) {
                int volume = (int) configMap.get("volume");
                previousVolume = volume;
            }
        }
    }

    private Map<String, Object> loadConfigFromJson() {
        try (FileReader fileReader = new FileReader(CONFIG_FILE_PATH)) {
            StringBuilder json = new StringBuilder();
            int data;
            while ((data = fileReader.read()) != -1) {
                json.append((char) data);
            }

            if (json.length() == 0) {
                // Empty file, return an empty map
                return new HashMap<>();
            }

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json.toString(), new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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