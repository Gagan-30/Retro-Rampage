package com.base.game.retrorampage.MainMenu;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenuController {

    private Stage mainStage;
    private Scene difficultySelectionScene;
    private Scene loadGameScene;
    private Scene settingsScene;
    private Scene aboutScene;
    private Scene exitScene;

    // Instances of various scenes and functionalities for the main menu
    private final DifficultySelection difficultySelection = new DifficultySelection();
    private final LoadGame loadGame = new LoadGame();
    private final Settings settings = new Settings();
    private final About about = new About();
    private final Exit exit = new Exit();

    // Method to set the main stage for the controller
    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void updateTitle(String newTitle) {
        if (mainStage != null) {
            mainStage.setTitle(newTitle);
        }
    }

    // Event handler for the "Start Game" button
    @FXML
    protected void onStartGameButtonClick() {
        boolean wasFullScreen = mainStage.isFullScreen();

        if (difficultySelectionScene == null) {
            difficultySelectionScene = difficultySelection.createDifficultySelectionScene(mainStage.getScene(), mainStage);
        }
        mainStage.setScene(difficultySelectionScene);

        if (wasFullScreen) {
            mainStage.setFullScreen(true);
        }

        updateTitle("Select Difficulty");
    }


    // Event handler for the "Load Game" button
    @FXML
    protected void onLoadGameButtonClick() {
        boolean wasFullScreen = mainStage.isFullScreen();

        if (loadGameScene == null) {
            loadGameScene = loadGame.createLoadGameScene(mainStage.getScene(), mainStage);
        }
        mainStage.setScene(loadGameScene);

        if (wasFullScreen) {
            mainStage.setFullScreen(true);
        }

        updateTitle("Select Game Save");
    }

    // Event handler for the "Settings" button
    @FXML
    protected void onSettingsButtonClick() {
        boolean wasFullScreen = mainStage.isFullScreen();

        if (settingsScene == null) {
            settingsScene = settings.createSettingsScene(mainStage.getScene(), mainStage);
        }
        mainStage.setScene(settingsScene);

        if (wasFullScreen) {
            mainStage.setFullScreen(true);
        }

        updateTitle("Settings");
    }

    // Event handler for the "About" button
    @FXML
    protected void onAboutButtonClick() {
        boolean wasFullScreen = mainStage.isFullScreen();

        if (aboutScene == null) {
            aboutScene = about.createAboutScene(mainStage.getScene(), mainStage);
        }
        mainStage.setScene(aboutScene);

        if (wasFullScreen) {
            mainStage.setFullScreen(true);
        }

        updateTitle("About");
    }

    // Event handler for the "Exit" button
    @FXML
    protected void onExitButtonClick() {
        boolean wasFullScreen = mainStage.isFullScreen();

        if (exitScene == null) {
            exitScene = exit.createExitScene(mainStage.getScene(), mainStage);
        }
        mainStage.setScene(exitScene);

        if (wasFullScreen) {
            mainStage.setFullScreen(true);
        }

        updateTitle("Exit");
    }
}