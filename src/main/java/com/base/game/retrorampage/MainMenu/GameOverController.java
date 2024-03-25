package com.base.game.retrorampage.MainMenu;

import com.base.game.retrorampage.GameAssets.Enemy;
import com.base.game.retrorampage.LevelGeneration.LevelGenerator;
import com.base.game.retrorampage.LevelGeneration.Level;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class GameOverController {

    private final MainMenu mainMenu = new MainMenu();
    private final Config config = new Config("config.txt");
    @FXML
    private Label mainMenuLabel;
    @FXML
    private Label totalEnemiesLabel;
    private Scene previousScene;
    private Stage stage;
    private Scene mainMenuScene;
    private Enemy enemy; // Instance of the Enemy class
    private int numberOfCells; // Store the number of cells

    public void setPreviousScene(Scene previousScene, int numberOfCells) {
        this.previousScene = previousScene;
        this.numberOfCells = numberOfCells; // Set the number of cells
    }

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }


    public void initialize() {
        mainMenuLabel.setText("Game Over");
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
    public void onRetryButtonClick() {
        boolean wasFullScreen = stage.isFullScreen();
        Level level = (Level) previousScene.getUserData();
        if (level != null) {

            // Retrieve the levelGenerator from the Level instance
            LevelGenerator levelGenerator = level.getLevelGenerator();

            // Reinitialize the LevelGenerator to regenerate the level
            Scene scene = levelGenerator.generateLevel();

            // Set the scene to the primary stage
            stage.setScene(scene);

            boolean isFullscreen = config.loadFullscreenSetting();
            if (isFullscreen == true) {
                stage.setFullScreen(true);
            }

            // Start the game loop again
            level.startGameLoop();
        } else {
            // Handle the case where level is null
            System.out.println("Level object is null.");
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
