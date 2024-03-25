package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Game;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Random;

/**
 * The main class for managing the Retro Rampage game.
 */
public class Level extends Game {
    private LevelGenerator levelGenerator;
    private Stage stage;

    /**
     * Constructs a Level object with the given stage.
     *
     * @param stage The primary stage of the game.
     */
    public Level(Stage stage) {
        this.stage = stage;
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

        // Set the Level instance as the user data of the scene
        scene.setUserData(this);

        // Set the scene to the primary stage
        stage.setScene(scene);
    }

    /**
     * Updates the game by calling the update method of the level generator.
     */
    @Override
    public void update() {
        Platform.runLater(() -> {
            levelGenerator.update();
        });
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
            Random random = new Random();
            return random.nextInt(8) + 3;
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
