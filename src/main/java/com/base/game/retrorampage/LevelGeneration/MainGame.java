package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Game;
import com.base.game.retrorampage.GameAssets.Player;
import javafx.scene.Scene;

public class MainGame extends Game {
    LevelGenerator levelGenerator;

    @Override
    public void initialize() {

        // Initialize the LevelGenerator with the desired number of cells
        levelGenerator = new LevelGenerator(stage, 5, "config.txt");

        // Generate the level and obtain the Scene object
        Scene scene = levelGenerator.generateLevel();

        // Set the scene to the primary stage
        stage.setScene(scene);

    }

    @Override
    public void update() {
        levelGenerator.update();
    }
}