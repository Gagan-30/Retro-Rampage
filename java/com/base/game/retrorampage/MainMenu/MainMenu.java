package com.base.game.retrorampage.MainMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class MainMenu extends Application {

    // Entry point for the JavaFX application. This method is called after the application starts.
    @Override
    public void start(Stage stage) throws IOException {
        // Create a Config object to read settings from a file named "config.txt"
        Config config = new Config("config.txt");
        // Apply settings from the config file to the primary stage of the application
        applySettingsToStage(stage, config);

        // Load the main menu's layout from an FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainMenu-view.fxml"));
        Parent root = fxmlLoader.load();

        // Obtain the controller for the main menu and set the main stage on it
        MainMenuController mainMenuController = fxmlLoader.getController();
        mainMenuController.setMainStage(stage);

        // Create a new scene with the loaded layout
        Scene scene = new Scene(root);
        // Set the title of the window (stage) to "Main Menu"
        stage.setTitle("Main Menu");
        // Make the window resizable
        stage.setResizable(true);

        // Add a stylesheet to the scene for styling the UI
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css")).toExternalForm());

        // Display the main menu scene on the stage
        stage.setScene(scene);
        stage.show();
    }

    // Helper method to apply configuration settings to the stage, such as resolution and fullscreen mode
    private void applySettingsToStage(Stage stage, Config config) {
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

    // The main method that launches the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}
