package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Game;
import com.base.game.retrorampage.GameAssets.Player;
import javafx.scene.Scene;

public class Main extends Game {
    private LevelGenerator levelGenerator;
    private Player player;

    @Override
    public void initialize() {
        // Set the title of the primary stage
        setTitle("Level Generator");

        // Initialize the LevelGenerator with the desired number of cells
        levelGenerator = new LevelGenerator(5, "config.txt");

        // Generate the level and obtain the Scene object
        Scene scene = levelGenerator.generateLevel();

        // Set the scene to the primary stage
        stage.setScene(scene);


    }

    @Override
    public void update() {
        if (player != null) {
            player.handleMovementInput();
        }
    }
}