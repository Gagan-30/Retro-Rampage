package com.base.game.retrorampage.MainMenu;

// Import necessary JavaFX classes and IOException for error handling

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.io.IOException;

public class RetroRampage extends Application {

    // Static method to apply settings from the Config object to the Stage
    private static void applySettingsToStage(Stage stage, Config config) {
        // Load the resolution setting from the config and split it into width and height
        String resolution = config.loadResolutionSetting();
        String[] parts = resolution.split(" x ");
        if (parts.length == 2) {
            // Parse and apply width and height to the stage
            stage.setWidth(Integer.parseInt(parts[0]));
            stage.setHeight(Integer.parseInt(parts[1]));
        }
        // Set the full screen exit hint to an empty string (no hint displayed)
        stage.setFullScreenExitHint("");
        // Disable the default full screen exit key combination
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        // Apply the fullscreen setting from the config
        stage.setFullScreen(config.loadFullscreenSetting());
    }

    // The main method that launches the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }

    // Entry point for the JavaFX application. This method is called after the application starts.
    @Override
    public void start(Stage stage) throws IOException {
        // Create a Config object to read settings from a file named "config.txt"
        Config config = new Config("config.txt");
        // Apply settings from the config file to the primary stage of the application
        applySettingsToStage(stage, config);

        // Instantiate the MainMenu class and create the main menu scene
        MainMenu mainMenu = new MainMenu();
        Scene mainMenuScene = mainMenu.createMainMenuScene(stage, config);

        // Display the main menu scene on the stage
        showMainMenu(stage, mainMenuScene);
    }

    // Helper method to set the scene to the stage and make the stage visible
    private void showMainMenu(Stage stage, Scene scene) {
        stage.setScene(scene);
        stage.show();
    }
}