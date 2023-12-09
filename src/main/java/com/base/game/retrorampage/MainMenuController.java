package com.base.game.retrorampage;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Label mainMenuLabel;

    @FXML
    private VBox mainMenu;

    @FXML
    private Button startGameButton;

    @FXML
    private Button loadGameButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button aboutButton;

    @FXML
    private Button exitButton;

    private LoadGame loadGame = new LoadGame();

    private DifficultySelection difficultySelection = new DifficultySelection();

    private Stage mainStage;
    private Scene difficultySelectionScene;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    protected void onStartGameButtonClick() {
        if (difficultySelectionScene == null) {
            // Load the difficulty selection scene if not loaded
            difficultySelectionScene = difficultySelection.createDifficultySelectionScene();
        }

        mainStage.setScene(difficultySelectionScene);
    }

    @FXML
    protected void onLoadGameButtonClick() {
        // Handle loading game
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
