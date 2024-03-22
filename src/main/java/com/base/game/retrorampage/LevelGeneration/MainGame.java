package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Game;
import com.base.game.retrorampage.GameAssets.Player;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Random;

public class MainGame extends Game {
    private LevelGenerator levelGenerator;
    private Scene scene;
    private Scene previousScene;

    private int numberOfCells;
    private Stage stage;
    private int levelCounter; // Counter for the number of times a level is generated

    public MainGame(Stage stage) {
        this.stage = stage;
        this.levelCounter = 0; // Initialize the counter
    }

    @Override
    public void initialize() {
        boolean wasFullScreen = stage.isFullScreen();
        // If the stage was in full screen mode before, re-enable full screen mode.
        if (wasFullScreen) {
            stage.setFullScreen(true);
        }
        // Initialize the LevelGenerator with the desired number of cells
        levelGenerator = new LevelGenerator(stage, this, getRandomCells(), "config.txt");

        // Generate the level and obtain the Scene object
        Scene scene = levelGenerator.generateLevel();

        // Set the MainGame instance as the user data of the scene
        scene.setUserData(this);

        // Set the scene to the primary stage
        stage.setScene(scene);
    }

    @Override
    public void update() {
        levelGenerator.update();
    }

    public LevelGenerator getLevelGenerator() {
        return levelGenerator;
    }

    public void setLevelGenerator(LevelGenerator levelGenerator) {
        this.levelGenerator = levelGenerator;
    }

    public void getPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    public int getRandomCells() {
        // If the level counter is 0, return 3, else return a random number between 3 and 10
        if (levelCounter == 0) {
            return 3;
        } else {
            Random random = new Random();
            return random.nextInt(8) + 3;
        }
    }

    public void setGlobalEnemyHealth(int health) {
        LevelGenerator.setGlobalEnemyHealth(health);
    }

    public int getNumberOfCells() {
        return numberOfCells;
    }
}
