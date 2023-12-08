package com.base.game.retrorampage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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


    private DifficultySelection difficultySelection = new DifficultySelection();

    @FXML
    protected void onStartGameButtonClick() {
        difficultySelection.loadDifficultySelectionScene();
    }


    @FXML
    protected void onLoadGameButtonClick() {
        // Add logic to switch to the settings scene
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
