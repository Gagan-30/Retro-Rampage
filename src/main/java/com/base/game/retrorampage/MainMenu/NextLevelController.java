package com.base.game.retrorampage.MainMenu;

import com.base.game.retrorampage.GameAssets.Enemy;
import com.base.game.retrorampage.LevelGeneration.LevelGenerator;
import com.base.game.retrorampage.LevelGeneration.MainGame;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class NextLevelController {

    private final MainMenu mainMenu = new MainMenu();
    private final Config config = new Config("config.txt");
    @FXML
    private Label mainMenuLabel;
    @FXML
    private Label totalEnemiesLabel;
    private Scene previousScene;
    private Stage stage;
    private Scene mainMenuScene;

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        mainMenuLabel.setText("Level Complete");
        updateTotalEnemiesLabel();
    }

    @FXML
    public void onMainMenuButtonClick() throws IOException {
        boolean wasFullScreen = stage.isFullScreen();

        if (mainMenuScene == null) {
            mainMenuScene = mainMenu.createMainMenuScene(stage, config);
        }
        stage.setScene(mainMenuScene);

        if (wasFullScreen) {
            stage.setFullScreen(true);
        }

        updateTitle("RetroRampage Menu");
    }

    @FXML
    public void onNextLevelButtonClick() {
        MainGame mainGame = (MainGame) previousScene.getUserData();
        if (mainGame != null) {
            // Retrieve the levelGenerator from the MainGame instance
            LevelGenerator levelGenerator = mainGame.getLevelGenerator();

            // Reinitialize the LevelGenerator to regenerate the level
            Scene scene = levelGenerator.generateLevel();

            // Set the scene to the primary stage
            stage.setScene(scene);

            // Start the game loop again
            mainGame.startGameLoop();
        } else {
            // Handle the case where mainGame is null
            System.out.println("MainGame object is null.");
        }
    }

    private void updateTitle(String newTitle) {
        if (stage != null) {
            stage.setTitle(newTitle);
        }
    }

    private void updateTotalEnemiesLabel() {
        int totalEnemiesKilled = getTotalEnemiesKilled(); // Retrieve or calculate total enemies killed
        totalEnemiesLabel.setText("Enemies Killed: " + totalEnemiesKilled);
    }

    private int getTotalEnemiesKilled() {
        return Enemy.getTotalEnemiesKilled(); // Replace with actual logic
    }

}
