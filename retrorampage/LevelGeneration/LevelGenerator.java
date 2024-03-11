package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.*;
import com.base.game.retrorampage.MainMenu.Config;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelGenerator {
    private final Scene scene;
    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;
    // Managers and components
    private final Pane root; // Root pane to contain the level elements
    private final RoomManager roomManager; // Manages the generation and drawing of rooms
    private final GraphManager graphManager; // Manages graph-related operations
    private final CorridorManager corridorManager; // Manages the creation of corridors
    private final VisualizationManager visualizationManager; // Manages visualization of the level
    private final Input input; // Handles user input
    private final Config config; // Manages configuration settings
    private final Player player; // Represents the player
    private final List<Bullet> bullets; // ArrayList to store bullets
    private boolean isShooting = false; // Add this variable
    private long lastUpdateTime = System.nanoTime();
    private Map<Enemy, Long> enemyCooldowns = new HashMap<>(); // Map to store enemy cooldowns
    private List<Enemy> enemies;
    private Camera camera;
    private int health;
    private HealthItem healthItem;
    private ExitKey exitKey;
    private String shootKey1;
    private String shootKey2;
    private boolean isLeftMousePressed = false;
    private boolean isRightMousePressed = false;
    private boolean isMiddleMousePressed = false;

    // Constructor
    public LevelGenerator(int numberOfCells, String configFilePath) {
        this.root = new Pane(); // Create a new pane to hold the level elements
        this.scene = new Scene(root, WIDTH, HEIGHT); // Set scene dimensions

        // Initialize managers with necessary parameters
        this.roomManager = new RoomManager(numberOfCells, root);
        this.graphManager = new GraphManager();
        this.corridorManager = new CorridorManager(root);
        this.visualizationManager = new VisualizationManager(root);
        this.input = new Input(scene); // Set up input handling
        this.config = new Config(configFilePath); // Load configuration settings
        this.camera = new Camera(1.0, 0.1);
        this.player = new Player(25, "player.png", 100, root, camera); // Initialize the player
        this.bullets = new ArrayList<>(); // Initialize the bullets list
        this.player.setCorridorManager(corridorManager);
        scene.setOnMouseMoved(event -> input.updateMousePosition(event));
        this.health = 100;
        this.healthItem = new HealthItem(20, "health.png", player, root, roomManager);
        this.exitKey = new ExitKey(20, "key.png", player, root, roomManager);
        this.enemies = new ArrayList<>();
        handleMousePress();
    }

    // Generate the level
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

        // 3. Create corridors based on MST edges and additional logic for loops
        corridorManager.createCorridors(mstEdges);

        // 4. Draw Rooms and Triangulation
        roomManager.drawRooms();
        corridorManager.createHallways(loopedEdges, 40); // Draw hallways again for visualization

        // 5. Draw the player in the center of the spawn room cell
        Cell spawnRoomCenter = roomManager.getSpawnCell();
        player.drawInSpawn(spawnRoomCenter, root);

        // Spawn enemies and set the corridor manager for each enemy
        enemies = Enemy.spawnEnemies(5, 60, "enemy.png", health, roomManager.getCells(), roomManager.getSpawnCell(), root);
        for (Enemy enemy : enemies) {
            enemy.setCorridorManager(corridorManager);
        }

        // 6. Spawn health item
        healthItem = new HealthItem(20, "health.png", player, root, roomManager);
        healthItem.spawnRandomly();

        // 7. Spawn exitKey item
        exitKey = new ExitKey(20, "key.png", player, root, roomManager);
        exitKey.spawnRandomly();

        // Set the camera to follow the player
        camera.follow(player, WIDTH, HEIGHT);
        // Apply transformations to the root
        root.getTransforms().setAll(camera.getTranslation(), camera.getZoom());

        // Return the scene containing the generated level
        return this.scene;
    }


    public void update() {
        double dt = calculateDeltaTime();
        updatePlayerPosition();
        updateBullets(dt);
        handleShootInput();
        updateEnemies();
        updateHealthItem();
        updateExitKey();
        updateCamera();

//        // Debug prints
//        System.out.println("Checking exit key collision");
//        System.out.println("exitKey: " + exitKey);
//        System.out.println("player: " + player);
//        System.out.println("exitKey.isActive(): " + (exitKey != null ? exitKey.isActive() : "null"));
//        System.out.println("player.getSquare().getBoundsInParent(): " + (player != null ? player.getSquare().getBoundsInParent() : "null"));
//        System.out.println("exitKey.getBoundsInParent(): " + (exitKey != null ? exitKey.getBoundsInParent() : "null"));

        checkPlayerExitKeyCollision();

//        System.out.println("Checked exit key collision");

        // Check if the player has the key and is in the red room
        if (exitKey != null && exitKey.hasKey() && player.isInRedRoom()) {
            endLevel();  // Implement your logic for ending the level
        }
    }


    private void endLevel() {
        // Implement your logic for ending the level
        // For example, display a message, transition to the next level, etc.
        System.out.println("Level completed! You have the key and are in the red room.");
    }

    private void updateCamera() {
        // Update the camera to follow the player
        camera.follow(player, WIDTH, HEIGHT);

        // Apply transformations to the root
        root.getTransforms().setAll(camera.getTranslation(), camera.getZoom());
    }

    private double calculateDeltaTime() {
        long now = System.nanoTime();
        double dt = (now - lastUpdateTime) / 1e9; // Convert nanoseconds to seconds
        lastUpdateTime = now;
        return dt;
    }

    private void updatePlayerPosition() {
        player.updatePosition(input, config, 120.0);
    }

    // Handle shsdooting input, either from keyboard or left mouse button
    private void handleShootInput() {
        shootKey1 = config.getKeybind("Shoot1");
        shootKey2 = config.getKeybind("Shoot2");

        // Check if the shoot key is pressed or left mouse button is pressed
        if (input.isKeyPressed(shootKey1) || input.isKeyPressed(shootKey2) || isLeftMousePressed || isRightMousePressed || isMiddleMousePressed) {
            if (!isShooting) {
                // Create a new bullet and add it to the list
                Bullet newBullet = new Bullet(10, "bullet.png", root, player, input);
                newBullet.shoot(player.getX(), player.getY(), player.getRotation());
                bullets.add(newBullet);
                isShooting = true;
            }
        } else {
            isShooting = false;
        }
    }

    // Handle mouse press and release events to track the left mouse button state
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

    private void updateBullets(double dt) {
        List<Bullet> bulletsToRemove = new ArrayList<>();

        for (Bullet bullet : bullets) {
            if (bullet.isActive()) {
                bullet.update(dt);
                checkBulletCollisions(bullet);
            } else {
                bulletsToRemove.add(bullet);
            }
        }

        bullets.removeAll(bulletsToRemove);
    }

    // Update the checkBulletCollisions method in LevelGenerator
    private void checkBulletCollisions(Bullet bullet) {
        boolean bulletOutsideRooms = true;

        for (Rectangle roomRectangle : roomManager.getRoomRectangles()) {
            double roomX = roomRectangle.getX();
            double roomY = roomRectangle.getY();
            double roomWidth = roomRectangle.getWidth();
            double roomHeight = roomRectangle.getHeight();

            // Check if the bullet is inside the bounds of the room
            if (bullet.getX() >= roomX && bullet.getX() + bullet.getWidth() <= roomX + roomWidth &&
                    bullet.getY() >= roomY && bullet.getY() + bullet.getHeight() <= roomY + roomHeight) {
                bulletOutsideRooms = false;
                break;
            }
        }

        if (bulletOutsideRooms) {
            // Bullet is outside all rooms, consider it colliding with the room edges
            bullet.setActive(false);
        }

        List<Enemy> enemiesToRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (bullet.isActive() && enemy.getSquare().getBoundsInParent().intersects(bullet.getBoundsInParent())) {
                System.out.println("Bullet hit enemy!");
                enemy.reduceHealth(25);
                bullet.setActive(false);

                if (enemy.getHealth() <= 0) {
                    System.out.println("Enemy defeated!");
                    enemiesToRemove.add(enemy);
                }
            }
        }

        enemies.removeAll(enemiesToRemove);
    }

    // In the updateEnemies method of LevelGenerator, print a debug statement
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
//                System.out.println("Player-enemy collision!");
//                System.out.println("Player health: " + player.getHealth());
//                System.out.println("Player position: (" + player.getX() + ", " + player.getY() + ")");
//                System.out.println("Enemy position: (" + enemy.getX() + ", " + enemy.getY() + ")");
//                System.out.println("Player bounds: " + player.getSquare().getBoundsInParent());
//                System.out.println("Enemy bounds: " + enemy.getSquare().getBoundsInParent());

                if (!enemyCooldowns.containsKey(enemy)) {
                    // Set cooldown for the enemy (2 seconds in milliseconds)
                    long cooldownDuration = 2000;
                    enemyCooldowns.put(enemy, System.currentTimeMillis() + cooldownDuration);

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

    private void updateHealthItem() {
        if (healthItem != null) {
            healthItem.update();
            checkPlayerHealthItemCollision();
        }
    }

    private void checkPlayerHealthItemCollision() {
        if (healthItem != null && healthItem.isActive() && player.getSquare().getBoundsInParent().intersects(healthItem.getBoundsInParent())) {
            // Player collided with the health item
            player.heal(healthItem.getHealthIncrease()); // Assuming you have a getHealthIncrease() method in HealthItem
            healthItem.removeFromPane(); // Remove the health item from the pane
            healthItem = null; // Set healthItem to null
        }
    }

    private void updateExitKey() {
        if (exitKey != null && exitKey.isActive()) {
            exitKey.update();
            checkPlayerExitKeyCollision();
        }
    }

    private void checkPlayerExitKeyCollision() {
        if (exitKey != null && exitKey.isActive() && player.getSquare().getBoundsInParent().intersects(exitKey.getBoundsInParent())) {
            // Player collided with the exit key item
//            System.out.println("Player collided with the exit key item");
//            System.out.println("exitKey.isActive(): " + exitKey.isActive());
//            System.out.println("exitKey.hasKey(): " + exitKey.hasKey());
//            System.out.println("player.getSquare().getBoundsInParent(): " + player.getSquare().getBoundsInParent());
//            System.out.println("exitKey.getBoundsInParent(): " + exitKey.getBoundsInParent());

            // Inside checkPlayerExitKeyCollision()
            if (exitKey.hasKey()) {
                // Player has the key, perform exit logic (e.g., end the level)
                System.out.println("Player collected the exit key!");
                // Implement your exit logic here, such as transitioning to the next level or ending the game.
            } else {
                // Player does not have the key yet
                System.out.println("Player needs the key to exit!");
            }

            // Deactivate and remove the exit key item
            exitKey.removeFromPane();
            exitKey.setActive(false); // Optionally, set active to false if necessary
            exitKey = null; // Move this line after you've performed any necessary logic

            // Additional check for null exitKey and player in red room
            if (exitKey == null && player.isInRedRoom()) {
                System.out.println("Player is in a red room without the exit key!");
                // Add your logic here, such as displaying a message or taking appropriate action
            }
        }
    }


}