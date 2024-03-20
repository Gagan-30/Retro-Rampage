package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Game;
import javafx.scene.Scene;

import java.util.Random;

public class MainGame extends Game {
    LevelGenerator levelGenerator;
    private int numberOfCells;

    @Override
    public void initialize() {
        // Initialize the LevelGenerator with the desired number of cells
        levelGenerator = new LevelGenerator(stage, this, 3, "config.txt");

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


    public int getRandomCells() {
        // Create an instance of the Random class
        Random random = new Random();

        // Generate a random integer between 3 and 10 (inclusive)
        int randomNumberOfCells = random.nextInt(8) + 3;

        // Return the random number of cells
        return randomNumberOfCells;
    }

    public int getNumberOfCells() {
        return numberOfCells;
    }
}