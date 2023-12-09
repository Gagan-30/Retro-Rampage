package com.base.game.retrorampage;

import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {


    private Stage mainStage;
    private Scene difficultySelectionScene;

    private DifficultySelection difficultySelection = new DifficultySelection();

    private LoadGame loadGame = new LoadGame();

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
        // Handle loading save game
    }

    @FXML
    protected void onSettingsButtonClick() {
        // Add logic to switch to the settings scene
    }

    @FXML
    protected void onAboutButtonClick() {
        // Add logic to show information about the game
    }

    @FXML
    protected void onExitButtonClick() {
        // Add logic to exit the game
    }
}