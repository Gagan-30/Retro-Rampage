package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    private Stage mainStage;
    private final DifficultySelection difficultySelection = new DifficultySelection();
    private final LoadGame loadGame = new LoadGame();
    private final Settings settings = new Settings();
    private final About about = new About();
    private final Exit exit = new Exit();

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @FXML
    protected void onStartGameButtonClick() {
        Scene difficultySelectionScene = difficultySelection.createDifficultySelectionScene(mainStage);
        switchScene(difficultySelectionScene, "Select Difficulty");
    }

    @FXML
    protected void onLoadGameButtonClick() {
        Scene loadGameScene = loadGame.createLoadGameScene(mainStage);
        switchScene(loadGameScene, "Select Game Save");
    }

    @FXML
    protected void onSettingsButtonClick() {
        Scene settingsScene = settings.createSettingsScene(mainStage);
        switchScene(settingsScene, "Settings");
    }

    @FXML
    protected void onAboutButtonClick() {
        Scene aboutScene = about.createAboutScene(mainStage);
        switchScene(aboutScene, "About");
    }

    @FXML
    protected void onExitButtonClick() {
        Scene exitScene = exit.createExitScene(mainStage);
        switchScene(exitScene, "Exit");
    }

    private void switchScene(Scene newScene, String title) {
        if (newScene != null) {
            mainStage.setScene(newScene);
            updateTitle(title);
        }
    }

    private void updateTitle(String newTitle) {
        if (mainStage != null) {
            mainStage.setTitle(newTitle);
        }
    }
}
