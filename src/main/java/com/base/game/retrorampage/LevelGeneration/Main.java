package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Camera;
import com.base.game.retrorampage.GameAssets.GameLoop;
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

        // Create a new Group that will hold the Canvas
        Group root = new Group();
        scene.setRoot(root); // Set this new group as the root of the scene

        Canvas canvas = new Canvas(800, 600);
        root.getChildren().add(canvas); // Add the canvas to the group

        // Initialize other game components
        Texture playerTexture = new Texture("player.png", 0.2); // Adjust path and scale factor as necessary
        Player player = new Player(100.0f, 100.0f, 64, 64, playerTexture, 2.0f);

        Camera camera = new Camera(0, 0, 800, 600, 800, 600);

        // Start the game loop
        GameLoop gameLoop = new GameLoop(levelGenerator, camera, canvas, player);
        gameLoop.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
