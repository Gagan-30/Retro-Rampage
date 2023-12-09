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



    private DifficultySelection difficultySelection = new DifficultySelection();
    private LoadGame loadGame = new LoadGame();
    private Settings settings = new Settings();
    private About about = new About();
    private Exit exit = new Exit();


    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void updateTitle(String newTitle) {
        if (mainStage != null) {
            mainStage.setTitle(newTitle);
        }
    }

    @FXML
    protected void onStartGameButtonClick() {
        if (difficultySelectionScene == null) {
            // Load the difficulty selection scene if not loaded
            difficultySelectionScene = difficultySelection.createDifficultySelectionScene();
        }
        updateTitle("Select Difficulty");
        mainStage.setScene(difficultySelectionScene);
    }

    @FXML
    protected void onLoadGameButtonClick() {
        if (loadGameScene == null) {
            // Load the Load Game scene if not loaded
            loadGameScene = loadGame.createLoadGameScene();
        }
        updateTitle("Select Game Save");
        mainStage.setScene(loadGameScene);
    }

    @FXML
    protected void onSettingsButtonClick() {
        if (settingsScene == null) {
            // Load the Settings scene if not loaded
            settingsScene = settings.createSettingsScene();
        }
        updateTitle("Settings");
        mainStage.setScene(settingsScene);
    }

    @FXML
    protected void onAboutButtonClick() {
        if (aboutScene == null) {
            // Load the About scene if not loaded
            aboutScene = about.createAboutScene();
        }
        updateTitle("About");
        mainStage.setScene(aboutScene);
    }

    @FXML
    protected void onExitButtonClick() {
        if (exitScene == null) {
            // Load the Exit scene if not loaded
            exitScene = exit.createExitScene();
        }
        updateTitle("Exit");
        mainStage.setScene(exitScene);
    }
}