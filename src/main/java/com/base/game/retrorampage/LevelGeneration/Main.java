package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Camera;
import com.base.game.retrorampage.GameAssets.Player;
import com.base.game.retrorampage.GameAssets.Texture;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Level Generator");

        // Initialize the LevelGenerator with the desired number of cells
        LevelGenerator levelGenerator = new LevelGenerator(7);

        // Generate the level and obtain the Scene object
        Scene scene = levelGenerator.generateLevel();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
