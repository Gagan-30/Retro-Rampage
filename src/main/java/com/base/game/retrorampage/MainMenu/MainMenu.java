package com.base.game.retrorampage.MainMenu;

// Necessary imports for JavaFX functionality and exception handling

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenu {

    // Method to create and return the main menu scene for the provided stage, configured according to the given config object
    public Scene createMainMenuScene(Stage stage, Config config) throws IOException {
        // Load the main menu's layout from an FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainMenu-view.fxml"));
        Parent root = fxmlLoader.load();

        // Obtain the controller for the main menu and set the main stage on it
        MainMenuController mainMenuController = fxmlLoader.getController();
        mainMenuController.setMainStage(stage);

        // Apply settings from the config object to the stage
        applySettingsFromConfig(stage, config);

        // Create a new scene with the loaded layout
        Scene scene = new Scene(root);
        // Set the title of the window (stage) to "Main Menu"
        stage.setTitle("Main Menu");
        // Make the window resizable
        stage.setResizable(true);

        // Add a stylesheet to the scene for styling the UI
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());
        // Return the constructed scene
        return scene;
    }

    // Helper method to apply configuration settings to the stage, such as resolution and fullscreen mode
    private void applySettingsFromConfig(Stage stage, Config config) {
        // Parse the resolution setting from the config
        String resolution = config.loadResolutionSetting();
        String[] parts = resolution.split(" x ");
        // If the resolution setting is correctly formatted, apply the width and height to the stage
        if (parts.length == 2) {
            stage.setWidth(Integer.parseInt(parts[0]));
            stage.setHeight(Integer.parseInt(parts[1]));
        }
        // Set the full screen exit hint to an empty string (disabling it)
        stage.setFullScreenExitHint("");
        // Disable the default key combination for exiting full screen mode
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        // Apply the fullscreen setting from the config
        stage.setFullScreen(config.loadFullscreenSetting());
    }
}
