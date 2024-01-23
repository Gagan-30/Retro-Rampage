package com.base.game.retrorampage;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class GraphicsController {

    private static final String CONFIG_FILE = "config.json";
    private Scene previousScene;
    private Stage stage;

    @FXML
    private Button screenTypeButton;

    @FXML
    private SplitMenuButton resolutionMenu;

    // Method to set the previous scene
    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    // Method to set the main stage
    public void setStage(Stage stage) {
        this.stage = stage;
        // Apply fullscreen setting when the stage is set
        applyFullscreenSetting(loadFullscreenSetting());
    }

    // Method to update the title of the main stage
    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    // Method to initialize the controller after FXML file is loaded
    @FXML
    private void initialize() {
        // Set the initial fullscreen setting when the controller is initialized
        // Avoid executing the change screen type logic during initialization
        applyFullscreenSetting(loadFullscreenSetting());

        // Update the button text based on the initial fullscreen setting
        if (stage != null) {
            updateButtonText();
        }
    }

    @FXML
    private void handleResolutionChange(javafx.event.ActionEvent event) {
        MenuItem selectedMenuItem = (MenuItem) event.getSource();
        resolutionMenu.setText(selectedMenuItem.getText());
    }

    @FXML
    public void onChangeScreenTypeButtonClick() {
        boolean fullscreen = !stage.isFullScreen();
        System.out.println("Changing fullscreen to: " + fullscreen);

        // Set full-screen mode directly on the main stage
        stage.setFullScreen(fullscreen);

        // Set resizable property based on full-screen mode
        stage.setResizable(!fullscreen);

        // Save the full-screen setting to the config file immediately
        saveFullscreenSetting(fullscreen);

        // Update the button text after changing the fullscreen mode
        if (stage != null) {
            updateButtonText();
        }
    }

    private void updateButtonText() {
        boolean fullscreen = stage.isFullScreen();
        screenTypeButton.setText("FullScreen: " + (fullscreen ? "Enabled" : "Disabled"));
    }



    private void saveFullscreenSetting(boolean fullscreen) {
        Properties properties = new Properties();
        properties.setProperty("fullscreen", String.valueOf(fullscreen));

        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, "Fullscreen Settings");
            output.flush(); // Flush the stream to ensure immediate write
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean loadFullscreenSetting() {
        Properties properties = new Properties();
        boolean defaultFullscreen = false;

        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            String fullscreenString = properties.getProperty("fullscreen", String.valueOf(defaultFullscreen));
            return Boolean.parseBoolean(fullscreenString);
        } catch (IOException e) {
            e.printStackTrace();
            return defaultFullscreen;
        }
    }

    private void applyFullscreenSetting(boolean fullscreen) {
        if (stage != null) {
            stage.setFullScreen(fullscreen);
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
