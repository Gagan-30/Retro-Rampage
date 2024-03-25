package com.base.game.retrorampage.MainMenu;

// Import necessary JavaFX classes

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    // Instances of classes for each part of the menu to manage their respective scenes
    private final DifficultySelection difficultySelection = new DifficultySelection();
    private final Settings settings = new Settings();
    private final About about = new About();
    private final Exit exit = new Exit();
    // Variables to hold references to the main application window and various scenes
    private Stage mainStage;
    private Scene difficultySelectionScene;
    private Scene settingsScene;
    private Scene aboutScene;
    private Scene exitScene;

    // Sets the main application window for the controller
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    // Updates the title of the main application window
    private void updateTitle(String newTitle) {
        if (mainStage != null) {
            mainStage.setTitle(newTitle);
        }
    }

    // Handles clicks on the "Start Game" button, transitioning to the difficulty selection scene
    @FXML
    protected void onStartGameButtonClick() {
        boolean wasFullScreen = mainStage.isFullScreen(); // Check if the stage was in full screen

        // Initialization of the difficulty selection scene
        if (difficultySelectionScene == null) {
            difficultySelectionScene = difficultySelection.createDifficultySelectionScene(mainStage.getScene(), mainStage);
        }
        mainStage.setScene(difficultySelectionScene); // Set the scene

        // Preserve full screen mode if it was set
        if (wasFullScreen) {
            mainStage.setFullScreen(true);
        }

        updateTitle("Select Difficulty"); // Update the window's title
    }

    // Event handler for the "Settings" button
    @FXML
    protected void onSettingsButtonClick() {
        boolean wasFullScreen = mainStage.isFullScreen();// Check if the stage was in full screen

        if (settingsScene == null) {
            settingsScene = settings.createSettingsScene(mainStage.getScene(), mainStage);
        }
        mainStage.setScene(settingsScene); // Set the scene

        // Preserve full screen mode if it was set
        if (wasFullScreen) {
            mainStage.setFullScreen(true);
        }

        updateTitle("Settings");// Update the window's title
    }

    // Event handler for the "About" button
    @FXML
    protected void onAboutButtonClick() {
        boolean wasFullScreen = mainStage.isFullScreen();// Check if the stage was in full screen

        if (aboutScene == null) {
            aboutScene = about.createAboutScene(mainStage.getScene(), mainStage);
        }
        mainStage.setScene(aboutScene); // Set the scene

        // Preserve full screen mode if it was set
        if (wasFullScreen) {
            mainStage.setFullScreen(true);
        }

        updateTitle("About");// Update the window's title
    }

    // Event handler for the "Exit" button
    @FXML
    protected void onExitButtonClick() {
        boolean wasFullScreen = mainStage.isFullScreen();// Check if the stage was in full screen

        if (exitScene == null) {
            exitScene = exit.createExitScene(mainStage.getScene(), mainStage);
        }
        mainStage.setScene(exitScene); // Set the scene

        // Preserve full screen mode if it was set
        if (wasFullScreen) {
            mainStage.setFullScreen(true);
        }

        updateTitle("Exit");// Update the window's title
    }
}