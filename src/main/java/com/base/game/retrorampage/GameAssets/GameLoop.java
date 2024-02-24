package com.base.game.retrorampage.GameAssets;

import com.base.game.retrorampage.LevelGeneration.LevelGenerator;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class GameLoop implements Runnable {
    private final int FPS = 10000;
    private boolean running;
    private final LevelGenerator levelGenerator;
    private final Camera camera;
    private Canvas canvas;
    private Player player;

    // Update the constructor to include Canvas and Player
    public GameLoop(LevelGenerator levelGenerator, Camera camera, Canvas canvas, Player player) {
        this.levelGenerator = levelGenerator;
        this.camera = camera;
        this.canvas = canvas; // Initialize the canvas
        this.player = player; // Initialize the player
    }


    @Override
    public void run() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / FPS;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        running = true;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = false;

            while (delta >= 1) {
                update();
                delta -= 1;
                shouldRender = true;
            }

            if (shouldRender) {
                render(gc);
                frames++;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
    }

    public void update() {
        // Update game objects and camera
        // levelGenerator.update(); // Implement this method in LevelGenerator to update game objects
        // camera.update(protagonist); // Assuming protagonist is accessible and updated in LevelGenerator
    }

    public void render(GraphicsContext gc) {
        Platform.runLater(() -> {
            // Clear previous frame
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // Draw the player at its current position
            gc.drawImage(player.getTexture().getImage(), player.getX(), player.getY());
        });
    }


    public void start() {
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }
}
