package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Game;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Random;

/**
 * The main class for managing the Retro Rampage game.
 */
public class MainGame extends Game {
    private LevelGenerator levelGenerator;
    private Scene scene;
    private Scene previousScene;

    private int numberOfCells;
    private Stage stage;
    private int levelCounter; // Counter for the number of times a level is generated

    /**
     * Constructs a MainGame object with the given stage.
     *
     * @param stage The primary stage of the game.
     */
    public MainGame(Stage stage) {
        this.stage = stage;
        this.levelCounter = 0; // Initialize the counter
    }

    /**
     * Initializes the game by setting up the level generator and generating the level.
     */
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

    /**
     * Updates the game by calling the update method of the level generator.
     */
    @Override
    public void update() {
        levelGenerator.update();
    }

    /**
     * Retrieves the level generator.
     *
     * @return The level generator object.
     */
    public LevelGenerator getLevelGenerator() {
        return levelGenerator;
    }


    /**
     * Generates a random number of cells for the level.
     *
     * @return The number of cells for the level.
     */
    public int getRandomCells() {
        // If the level counter is 0, return 3, else return a random number between 3 and 10 (inclusive)
        if (levelCounter == 0) {
            return 3;
        } else {
            Random random = new Random();
            return random.nextInt(8) + 3;
        }
    }

    /**
     * Sets the global enemy health for the level generator.
     *
     * @param health The global enemy health to set.
     */
    public void setGlobalEnemyHealth(int health) {
        LevelGenerator.setGlobalEnemyHealth(health);
    }

}
