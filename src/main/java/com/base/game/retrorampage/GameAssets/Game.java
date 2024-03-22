package com.base.game.retrorampage.GameAssets;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * MainGame class to be extended for game projects.
 * Creates the window and {@link Group} objects,
 * and manages the life cycle of the game (initialization and game loop).
 */
public abstract class Game extends Application implements Screen {

    /**
     * The window containing the game.
     */
    protected Stage stage;

    /**
     * Canvas where game graphics are displayed.
     */
    protected Canvas canvas;

    /**
     * Graphics context to draw on the canvas.
     */
    protected GraphicsContext context;

    /**
     * The group containing game entities.
     */
    protected Group group;

    /**
     * Handles user input.
     */
    protected Input input;

    /**
     * Animation timer for the game loop.
     */
    protected AnimationTimer gameLoop;
    private Scene previousScene;

    /**
     * Initializes the window and game objects,
     * and manages the life cycle of the game (initialization and game loop).
     */
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Game");
        stage.setResizable(false);
        boolean wasFullScreen = stage.isFullScreen();
        // If the stage was in full screen mode before, re-enable full screen mode.
        if (wasFullScreen) {
            stage.setFullScreen(true);
        }

        Pane root = new Pane();
        Scene mainScene = new Scene(root);
        stage.setScene(mainScene);

        canvas = new Canvas(512, 512);
        context = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        group = new Group();
        input = new Input(mainScene);

        gameLoop = new AnimationTimer() {
            long lastTime = System.nanoTime();
            int frameCount = 0;

            @Override
            public void handle(long currentTime) {
                // Calculate FPS
                frameCount++;
                if (currentTime - lastTime >= 1e9) {
                    double fps = frameCount / ((currentTime - lastTime) / 1e9);
                    setTitle("Game - FPS: " + String.format("%.2f", fps));
                    frameCount = 0;
                    lastTime = currentTime;
                }

                // Update user input
                input.update();

                // Update game state
                update();
                group.update(1 / 60.0);

                // Clear the canvas
                context.setFill(Color.GRAY);
                context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
        };

        stage.show();
        initialize();
        startGameLoop();
    }

    /**
     * Starts the game loop.
     */
    public void startGameLoop() {
        if (gameLoop != null) { // Ensure gameLoop is not null before starting
            gameLoop.start();
        } else {
            System.out.println("Game loop is null. Make sure to initialize it.");
        }
    }

    /**
     * Stops the game loop.
     */
    public void stopGameLoop() {
        if (gameLoop != null) { // Ensure gameLoop is not null before stopping
            gameLoop.stop();
        }
    }

    /**
     * Set the text that appears in the window title bar.
     *
     * @param title window title
     */
    public void setTitle(String title) {
        stage.setTitle(title);
    }

    /**
     * Override this method to initialize the game state.
     */
    public abstract void initialize();

    /**
     * Override this method to update the game state.
     */
    public abstract void update();

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }
}