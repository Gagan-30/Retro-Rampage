package com.base.game.retrorampage;

import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    private Stage mainStage;
    private Scene difficultySelectionScene;
    private Scene loadGameScene;
    private Scene settingsScene;
    private Scene aboutScene;
    private Scene exitScene;

    // Instances of various scenes and functionalities for the main menu
    private DifficultySelection difficultySelection = new DifficultySelection();
    private LoadGame loadGame = new LoadGame();
    private Settings settings = new Settings();
    private About about = new About();
    private Exit exit = new Exit();

    // Method to set the main stage for the controller
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    // Method to update the title of the main stage
    private void updateTitle(String newTitle) {
        if (mainStage != null) {
            mainStage.setTitle(newTitle);
        }
    }

    // Event handler for the "Start Game" button
    @FXML
    protected void onStartGameButtonClick() {
        if (difficultySelectionScene == null) {
            // Load the difficulty selection scene if not loaded
            difficultySelectionScene = difficultySelection.createDifficultySelectionScene(mainStage.getScene(), mainStage);
        }
        updateTitle("Select Difficulty");
        mainStage.setScene(difficultySelectionScene);
    }

    // Event handler for the "Load Game" button
    @FXML
    protected void onLoadGameButtonClick() {
        if (loadGameScene == null) {
            // Load the Load Game scene if not loaded
            loadGameScene = loadGame.createLoadGameScene(mainStage.getScene(), mainStage);
        }
        updateTitle("Select Game Save");
        mainStage.setScene(loadGameScene);
    }

    // Event handler for the "Settings" button
    @FXML
    protected void onSettingsButtonClick() {
        if (settingsScene == null) {
            // Load the Settings scene if not loaded
            settingsScene = settings.createSettingsScene(mainStage);
        }
        updateTitle("Settings");
        mainStage.setScene(settingsScene);
    }

    // Event handler for the "About" button
    @FXML
    protected void onAboutButtonClick() {
        if (aboutScene == null) {
            // Pass the current scene as the previous scene and the stage
            aboutScene = about.createAboutScene(mainStage.getScene(), mainStage);
        }
        updateTitle("About");
        mainStage.setScene(aboutScene);
    }

    // Event handler for the "Exit" button
    @FXML
    protected void onExitButtonClick() {
        if (exitScene == null) {
            // Pass the current scene as the previous scene and the stage
            exitScene = exit.createExitScene(mainStage.getScene(), mainStage);
        }
        updateTitle("Exit");
        mainStage.setScene(exitScene);
    }
}