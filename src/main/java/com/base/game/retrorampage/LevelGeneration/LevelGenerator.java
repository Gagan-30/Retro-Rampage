package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.*;
import com.base.game.retrorampage.MainMenu.Config;
import com.base.game.retrorampage.MainMenu.GameOver;
import com.base.game.retrorampage.MainMenu.NextLevel;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

/**
 * LevelGenerator class responsible for generating and managing game levels.
 */
public class LevelGenerator {
    /**
     * Global enemy health variable.
     */
    private static int globalEnemyHealth;

    /**
     * JavaFX Scene object representing the game scene.
     */
    private final Scene scene;

    /**
     * Width of the game window.
     */
    private final int WIDTH;

    /**
     * Height of the game window.
     */
    private final int HEIGHT;

    // Managers and components
    /**
     * Root pane to contain the level elements.
     */
    private final Pane root;

    /**
     * Manages the generation and drawing of rooms.
     */
    private final RoomManager roomManager;

    /**
     * Manages graph-related operations.
     */
    private final GraphManager graphManager;

    /**
     * Manages the creation of corridors.
     */
    private final CorridorManager corridorManager;

    /**
     * Handles user input.
     */
    private final Input input;

    /**
     * Manages configuration settings.
     */
    private Config config;

    /**
     * Represents the player.
     */
    private final Player player;

    /**
     * ArrayList to store bullets.
     */
    private final List<Bullet> bullets;

    /**
     * Map to store enemy cooldowns.
     */
    private final Map<Enemy, Long> enemyCooldowns = new HashMap<>();

    /**
     * Camera object for managing the view.
     */
    private final Camera camera;

    /**
     * Initial health value.
     */
    private final int health;

    /**
     * Flag indicating if the right mouse button is pressed.
     */
    private final boolean isRightMousePressed = false;

    /**
     * Flag indicating if the middle mouse button is pressed.
     */
    private final boolean isMiddleMousePressed = false;

    /**
     * Reference to the primary stage.
     */
    private final Stage primaryStage;

    /**
     * Reference to the game instance.
     */
    private final Game game;

    /**
     * AudioClip object for playing audio.
     */
    private AudioClip audioClip;

    /**
     * URL for gunshot sound.
     */
    private URL gunshotPath;

    /**
     * URL for enemy damage sound.
     */
    private URL enemyDamagePath;

    /**
     * URL for key pickup sound.
     */
    private URL keyPickUpPath;

    /**
     * URL for heal-up sound.
     */
    private URL healUpPath;

    /**
     * Flag indicating if the player is shooting.
     */
    private boolean isShooting = false;

    /**
     * Time of the last update.
     */
    private long lastUpdateTime = System.nanoTime();

    /**
     * List of enemies.
     */
    private List<Enemy> enemies;

    /**
     * Health item object.
     */
    private HealthItem healthItem;

    /**
     * Exit key object.
     */
    private ExitKey exitKey;

    /**
     * Key for shooting action 1.
     */
    private String shootKey1;

    /**
     * Key for shooting action 2.
     */
    private String shootKey2;

    /**
     * Flag indicating if the left mouse button is pressed.
     */
    private boolean isLeftMousePressed = false;


    /**
     * Constructor for LevelGenerator.
     * @param primaryStage The primary stage of the application.
     * @param game The game instance.
     * @param numberOfCells The number of cells.
     * @param configFilePath The file path for the configuration.
     */
    public LevelGenerator(Stage primaryStage, Game game, int numberOfCells, String configFilePath) {
        boolean wasFullScreen = primaryStage.isFullScreen();
        // If the stage was in full screen mode before, re-enable full screen mode.
        if (wasFullScreen) {
            primaryStage.setFullScreen(true);
        }
        this.primaryStage = primaryStage;
        this.root = new Pane(); // Create a new pane to hold the level elements
        this.config = new Config(configFilePath); // Load configuration settings
        this.WIDTH = config.getWidth(); // Set WIDTH using config.getWidth()
        this.HEIGHT = config.getHeight(); // Set HEIGHT using config.getHeight()
        this.scene = new Scene(root, WIDTH, HEIGHT); // Set scene dimensions
        this.game = game;

        // Initialize managers with necessary parameters
        this.roomManager = new RoomManager(numberOfCells, root);
        this.graphManager = new GraphManager();
        this.corridorManager = new CorridorManager(root);
        this.input = new Input(scene); // Set up input handling
        this.camera = new Camera(1.0, 0.1);
        this.player = new Player(25, "player.png", 100, root, camera, roomManager); // Initialize the player
        this.bullets = new ArrayList<>(); // Initialize the bullets list
        this.player.setCorridorManager(corridorManager);
        scene.setOnMouseMoved(event -> input.updateMousePosition(event));
        this.health = 100;
        this.healthItem = new HealthItem(20, "health.png", player, root, roomManager);
        this.exitKey = new ExitKey(20, "key.png", player, root, roomManager);
        this.enemies = new ArrayList<>();
        handleMousePress();
    }

    /**
     * Setter method to set the global enemy health.
     * @param health The global enemy health value to set.
     */
    public static void setGlobalEnemyHealth(int health) {
        globalEnemyHealth = health;
    }

    /**
     * Generate the game level.
     * @return The generated Scene object.
     */
    public Scene generateLevel() {
        // 1. Generate and identify rooms
        roomManager.generateRooms();
        List<Rectangle> roomRectangles = roomManager.getRoomRectangles();
        roomManager.disperseCells();
        corridorManager.setRooms(roomRectangles);

        // 2. Perform triangulation and generate MST
        var roomCenters = roomManager.getRoomCenters();
        var triangleEdges = graphManager.triangulateRooms(roomCenters);
        var mstEdges = graphManager.generateMST(triangleEdges);
        var loopedEdges = graphManager.reintroduceLoops(mstEdges, triangleEdges, 0.3);

        // 3. Draw Rooms and Triangulation
        roomManager.drawRooms();
        corridorManager.createHallways(loopedEdges, 40); // Draw hallways again for visualization

        // 4. Draw the player in the center of the spawn room cell
        Cell spawnRoomCenter = roomManager.getSpawnCell();
        player.drawInSpawn(spawnRoomCenter, root);

        // 5. Spawn enemies and set the corridor manager for each enemy
        enemies = Enemy.spawnEnemies(roomManager.getCells().size(), 60, "enemy.png",
                globalEnemyHealth, roomManager.getCells(), roomManager.getSpawnCell(), root);
        for (Enemy enemy : enemies) {
            enemy.setCorridorManager(corridorManager);
        }

        // 6. Spawn health item
        healthItem = new HealthItem(20, "health.png", player, root, roomManager);
        healthItem.spawnRandomly();

        // 7. Spawn exitKey item
        exitKey = new ExitKey(20, "key.png", player, root, roomManager);
        exitKey.spawnRandomly();

        // 8. Set the camera to follow the player
        camera.follow(player, WIDTH, HEIGHT);

        // 9. Apply transformations to the root
        root.getTransforms().setAll(camera.getTranslation(), camera.getZoom());

        // Return the scene containing the generated level
        return this.scene;
    }


    /**
     * Update method called in the game loop to update game logic.
     */
    public void update() {
        double dt = calculateDeltaTime();
        updateCamera();
        updatePlayerPosition();
        updateBullets(dt);
        handleShootInput();
        updateEnemies();
        updateHealthItem();
        updateExitKey();
        checkPlayerHealth();
        checkEndLevel();
    }
    /**
     * Calculate delta time.
     * @return The delta time.
     */
    private double calculateDeltaTime() {
        long now = System.nanoTime();
        double dt = (now - lastUpdateTime) / 1e9; // Convert nanoseconds to seconds
        lastUpdateTime = now;
        return dt;
    }
    /**
     * Update the camera position and zoom.
     */
    private void updateCamera() {
        // Update the camera to follow the player
        camera.follow(player, WIDTH, HEIGHT);

        // Apply transformations to the root
        root.getTransforms().setAll(camera.getTranslation(), camera.getZoom());
    }
    /**
     * Update the player's position based on user input.
     */
    private void updatePlayerPosition() {
        player.updatePosition(input, config, 120.0);
    }

    /**
     * Handles shooting input, either from keyboard or left mouse button.
     */
    private void handleShootInput() {
        // Retrieve key bindings for shooting
        shootKey1 = config.getKeybind("Shoot1");
        shootKey2 = config.getKeybind("Shoot2");

        // Check if the shoot key is pressed or any mouse button is pressed
        if (input.isKeyPressed(shootKey1) || input.isKeyPressed(shootKey2) || isLeftMousePressed || isRightMousePressed || isMiddleMousePressed) {
            // If not shooting already, create a new bullet and add it to the list
            if (!isShooting) {
                Bullet newBullet = new Bullet(10, "bullet.png", root, player, input);
                newBullet.shoot(player.getX(), player.getY(), player.getRotation());
                bullets.add(newBullet);
                isShooting = true;
                // Load gunshot sound and play it
                gunshotPath = getClass().getResource("/gunshot.wav");
                audioClip = new AudioClip(gunshotPath.toString());
                audioClip.setVolume(config.getVolume());
                audioClip.play();
            }
        } else {
            isShooting = false; // Stop shooting if no shoot key is pressed
        }
    }

    /**
     * Handle mouse press and release events to track the left mouse button state.
     */
    private void handleMousePress() {
        shootKey1 = config.getKeybind("Shoot1");
        shootKey2 = config.getKeybind("Shoot2");

        handlePrimaryClick(shootKey1, shootKey2);
        handleSecondaryClick(shootKey1, shootKey2);
        handleMiddleClick(shootKey1, shootKey2);
    }

    private void handlePrimaryClick(String shootKey1, String shootKey2) {
        if (shootKey1 == "Left Click" || shootKey2 == "Left Click") {
            scene.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    isLeftMousePressed = true;
                }
            });

            scene.setOnMouseReleased(event -> {
                if (event.getButton() == MouseButton.PRIMARY) {
                    isLeftMousePressed = false;
                }
            });
        }
    }

    private void handleSecondaryClick(String shootKey1, String shootKey2) {
        if (shootKey1 == "Right Click" || shootKey2 == "Right Click") {
            scene.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    isLeftMousePressed = true;
                }
            });

            scene.setOnMouseReleased(event -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    isLeftMousePressed = false;
                }
            });
        }
    }

    private void handleMiddleClick(String shootKey1, String shootKey2) {
        if (shootKey1 == "Middle Click" || shootKey2 == "Middle Click") {
            scene.setOnMousePressed(event -> {
                if (event.getButton() == MouseButton.MIDDLE) {
                    isLeftMousePressed = true;
                }
            });

            scene.setOnMouseReleased(event -> {
                if (event.getButton() == MouseButton.MIDDLE) {
                    isLeftMousePressed = false;
                }
            });
        }
    }

    /**
     * Update the list of bullets and check for collisions.
     * @param dt The time step.
     */
    private void updateBullets(double dt) {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            if (bullet.isActive()) {
                bullet.update(dt);
                checkBulletCollisions(bullet);
            } else {
                iterator.remove(); // Remove the bullet using the iterator
            }
        }
    }
    /**
     * Check for collisions between bullets and enemies.
     * @param bullet The bullet to check collisions for.
     */
    private void checkBulletCollisions(Bullet bullet) {
        boolean bulletOutsideRooms = true;

        // Check if the bullet intersects with any room rectangle
        for (Rectangle roomRectangle : roomManager.getRoomRectangles()) {
            if (roomRectangle.getBoundsInParent().contains(bullet.getX(), bullet.getY())) {
                bulletOutsideRooms = false;
                break;
            }
        }

        if (bulletOutsideRooms) {
            // If the bullet is outside all rooms, remove it
            bullet.setActive(false);
            return; // Exit the method after deactivating the bullet
        }

        // Check for collisions with enemies
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            if (bullet.isActive() && enemy.getSquare().getBoundsInParent().intersects(bullet.getBoundsInParent())) {
                // Handle bullet collision with enemy
                enemy.reduceHealth(25);
                enemyDamagePath = getClass().getResource("/enemydamage.wav");
                audioClip = new AudioClip(enemyDamagePath.toString());
                audioClip.setVolume(config.getVolume());
                audioClip.play(); // Play heal up audio
                enemy.updateDamageLabel(25);
                bullet.setActive(false);

                // Check if the enemy has been defeated
                if (enemy.getHealth() <= 0) {
                    iterator.remove(); // Remove the defeated enemy
                }
            }
        }
    }

    /**
     * Update the list of enemies and their behavior.
     */
    private void updateEnemies() {
        List<Enemy> enemiesToRemove = new ArrayList<>();

        for (Enemy enemy : enemies) {
            // Check if the enemy is on cooldown
            if (enemyCooldowns.containsKey(enemy)) {
                long cooldownEndTime = enemyCooldowns.get(enemy);
                long currentTime = System.currentTimeMillis();

                if (currentTime < cooldownEndTime) {
                    // Enemy is still on cooldown, skip this iteration
                    continue;
                } else {
                    // Cooldown has expired, remove the enemy from cooldowns
                    enemyCooldowns.remove(enemy);
                }
            }

            enemy.setPlayer(player);

            // Save the previous position
            double prevX = enemy.getX();
            double prevY = enemy.getY();

            enemy.updatePosition(player.getX(), player.getY(), 50);

            if (enemy.isCollidingPlayer(player)) {
                if (!enemyCooldowns.containsKey(enemy)) {
                    // Set cooldown for the enemy (2 seconds in milliseconds)
                    long cooldownDuration = 2000;
                    enemyCooldowns.put(enemy, System.currentTimeMillis() + cooldownDuration);
                    enemy.updateDamageLabel(20);

                    // Optionally, handle player-enemy collision without health reduction here
                    System.out.println("Enemy hit player, cooldown started!");
                }
            } else {
                for (Rectangle roomRectangle : roomManager.getRoomRectangles()) {
                    if (enemy.isCollidingRoom(roomRectangle)) {
                        // Reset the position to the previous location before the collision
                        enemy.setPosition(prevX, prevY);

                        // Handle enemy collision with walls, e.g., change enemy direction, etc.
                        System.out.println("Enemy collided with room!");
                        break;
                    }
                }
            }
        }

        enemies.removeAll(enemiesToRemove);
    }

    /**
     * Update the health item's position and check for player collision.
     */
    private void updateHealthItem() {
        if (healthItem != null) {
            healthItem.update();
            checkPlayerHealthItemCollision();
        }
    }
    /**
     * Check for collision between player and health item.
     */
    private void checkPlayerHealthItemCollision() {
        if (healthItem != null && healthItem.isActive() && player.getSquare().getBoundsInParent().intersects(healthItem.getBoundsInParent())) {
            // Player collided with the health item
            player.heal(60);
            player.updateHealthLabel(60);
            
            healUpPath = getClass().getResource("/healup.wav");
            audioClip = new AudioClip(healUpPath.toString());
            audioClip.setVolume(config.getVolume());
            audioClip.play(); // Play heal up audio
            player.updateHealthLabel(healthItem.getHealthIncrease());
            healthItem.removeFromPane(); // Remove the health item from the pane
            healthItem = null; // Set healthItem to null
        }
    }
    /**
     * Check the player's health and handle game over if health is zero.
     */
    private void checkPlayerHealth() {
        if (player.getHealth() == 0) {
            Platform.runLater(this::gameOver);
        }
    }
    /**
     * Update the exit key's position and check for player collision.
     */
    private void updateExitKey() {
        if (exitKey != null && exitKey.isActive()) {
            exitKey.update();
            checkPlayerExitKeyCollision();
        }
    }
    /**
     * Check for collision between player and exit key.
     */
    private void checkPlayerExitKeyCollision() {
        if (exitKey != null && exitKey.isActive() && player.getSquare().getBoundsInParent().intersects(exitKey.getBoundsInParent())) {
            if (!exitKey.hasKey()) {
                System.out.println("Player collected the exit key!");
                // Load key pick audio
                keyPickUpPath = getClass().getResource("/keypickup.wav");
                audioClip = new AudioClip(keyPickUpPath.toString());
                audioClip.setVolume(config.getVolume());
                audioClip.play(); // Play key pick audio
                player.setHasKey(true); // Update the hasKey status
            }

            // Deactivate and remove the exit key item
            exitKey.removeFromPane();
            exitKey.setActive(false); // Optionally, set active to false if necessary
            exitKey = null; // Move this line after you've performed any necessary logic
        }
    }
    /**
     * Check if the level should end and trigger next level if conditions are met.
     */
    private void checkEndLevel() {
        // Check if the player has the key and is in the dark red room
        if (player.hasKey() && player.isInRedRoom()) {
            Platform.runLater(this::nextLevel);
        }
    }
    /**
     * Clear the level by removing all elements and resetting variables.
     */
    public void clearLevel() {
        // Remove all elements from the root pane
        root.getChildren().clear();

        // Clear lists and maps
        bullets.clear();
        enemies.clear();
        enemyCooldowns.clear();
        player.setHasKey(false);
        input.resetInput();
        game.stopGameLoop();

        // Reset player health
        player.setHealth(100);
    }
    /**
     * Handle the game over state.
     */
    public void gameOver() {
        // Create an instance of the NextLevel class
        GameOver gameOver = new GameOver();

        // Call the createNextLevelScene method to get the next level scene
        Scene gameOverScene = gameOver.createGameOverScene(scene, primaryStage, roomManager.getCells().size());

        if (gameOverScene != null) {
            boolean wasFullScreen = primaryStage.isFullScreen();
            // Set the next level scene to be displayed on the primary stage
            primaryStage.setScene(gameOverScene);

            // If the stage was in full screen mode before, re-enable full screen mode.
            if (wasFullScreen) {
                primaryStage.setFullScreen(true);
            }
            // Call clearLevel method to clean up the level
            clearLevel();
        } else {
            // Handle the case where the next level scene couldn't be created
            System.err.println("Failed to create the next level scene.");
        }
    }
    /**
     * Move to the next level.
     */
    private void nextLevel() {
        // Create an instance of the NextLevel class
        NextLevel nextLevel = new NextLevel();
        boolean wasFullScreen = primaryStage.isFullScreen();
        // Call the createNextLevelScene method to get the next level scene
        Scene nextLevelScene = nextLevel.createNextLevelScene(scene, primaryStage);

        if (nextLevelScene != null) {
            // Set the next level scene to be displayed on the primary stage
            primaryStage.setScene(nextLevelScene);
            if (wasFullScreen) {
                primaryStage.setFullScreen(true);
            }

            // Call clearLevel method to clean up the level
            clearLevel();
        } else {
            // Handle the case where the next level scene couldn't be created
            System.err.println("Failed to create the next level scene.");
            game.startGameLoop();
        }
    }


}