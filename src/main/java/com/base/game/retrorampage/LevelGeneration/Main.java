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

        // Initialize the LevelGenerator
        LevelGenerator levelGenerator = new LevelGenerator(7);

        // Create a Group as the root node to allow adding children
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600); // Scene size can be adjusted

        // Load the player texture and initialize the Player
        Texture playerTexture = new Texture("player.png");
        Player player = new Player(100.0f, 100.0f, 64, 64, playerTexture, 2.0f);

        // Initialize the canvas and add it to the root
        Canvas canvas = new Canvas(800, 600);
        root.getChildren().add(canvas);

        // Initialize the Camera
        Camera camera = new Camera(0, 0, 800, 600, 800, 600);

        // Start the game loop
        GameLoop gameLoop = new GameLoop(levelGenerator, camera, canvas.getGraphicsContext2D().getCanvas(), player);
        gameLoop.start();

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
