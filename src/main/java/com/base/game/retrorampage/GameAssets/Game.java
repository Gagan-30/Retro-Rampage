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
     * area where game graphics are displayed
     */
    public Canvas canvas;

    /**
     * object with methods to draw game entities on canvas
     */
    public GraphicsContext context;

    public Group group;

    /**
     * The window containing the game.
     */
    public Stage stage;

    public Input input;

    private AnimationTimer gameLoop;

    /**
     * Initializes the window and game objects,
     * and manages the life cycle of the game (initialization and game loop).
     */
    public void start(Stage mainStage) {
        mainStage.setTitle("Game");
        mainStage.setResizable(false);

        Pane root = new Pane();
        Scene mainScene = new Scene(root);
        mainStage.setScene(mainScene);
        mainStage.sizeToScene();

        canvas = new Canvas(512, 512);
        context = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        group = new Group();

        input = new Input(mainScene);

        // to clarify class containing update method
        Game self = this;

        gameLoop = new AnimationTimer() {
            long lastTime = System.nanoTime();
            int frameCount = 0;

            public void handle(long currentTime) {
                // Calculate FPS
                frameCount++;
                if (currentTime - lastTime >= 1e9) {
                    double fps = frameCount / ((currentTime - lastTime) / 1e9);
                    setTitle("Game - FPS: " + String.format("%.2f", fps));
                    frameCount = 0;
                    lastTime = currentTime;
                }

                // update user input
                input.update();

                // update game state
                self.update();
                self.group.update(1 / 60.0);

                // clear the canvas
                self.context.setFill(Color.GRAY);
                self.context.fillRect(0, 0,
                        self.canvas.getWidth(),
                        self.canvas.getHeight());
            }
        };

        mainStage.show();

        // reference required for set methods
        stage = mainStage;

        initialize();
        startGameLoop();
    }

    /**
     * Starts the game loop.
     */
    protected void startGameLoop() {
        gameLoop.start();
    }

    /**
     * Stops the game loop.
     */
    protected void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    /**
     * Set the text that appears in the window title bar
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

}
