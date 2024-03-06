package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Bullet;
import com.base.game.retrorampage.GameAssets.Enemy;
import com.base.game.retrorampage.GameAssets.Input;
import com.base.game.retrorampage.GameAssets.Player;
import com.base.game.retrorampage.MainMenu.Config;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class LevelGenerator {
    private final Scene scene;
    private final int WIDTH = 1920;
    private final int HEIGHT = 1080;
    private boolean isShooting = false; // Add this variable
    private long lastUpdateTime = System.nanoTime();

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
    private List<Enemy> enemies;
    private int health;
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
        this.player = new Player(50, "player.png", 100, root); // Initialize the player
        this.bullets = new ArrayList<>(); // Initialize the bullets list
        this.player.setCorridorManager(corridorManager);
        scene.setOnMouseMoved(event -> input.updateMousePosition(event));
        this.health = 100;
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
        visualizationManager.drawDelaunayTriangulation(graphManager.getEdges()); // Ensure GraphManager exposes the edges
        corridorManager.createHallways(loopedEdges, 75); // Draw hallways again for visualization

        // 5. Draw the player in the center of the spawn room cell
        Cell spawnRoomCenter = roomManager.getSpawnCell();
        player.drawInSpawn(spawnRoomCenter, root);

// Spawn enemies and set the corridor manager for each enemy
        enemies = Enemy.spawnEnemies(5, 75, "enemy.png", health, roomManager.getCells(), roomManager.getSpawnCell(), root);
        for (Enemy enemy : enemies) {
            enemy.setCorridorManager(corridorManager);
        }

        // Return the scene containing the generated level
        return this.scene;
    }

    public void update() {
        double dt = calculateDeltaTime();
        updatePlayerPosition();
        updateBullets(dt);
        handleShootInput();
        updateEnemies();
    }

    private double calculateDeltaTime() {
        long now = System.nanoTime();
        double dt = (now - lastUpdateTime) / 1e9; // Convert nanoseconds to seconds
        lastUpdateTime = now;
        return dt;
    }

    private void updatePlayerPosition() {
        player.updatePosition(input, config, 120.0);
        for (Rectangle roomRectangle : roomManager.getRoomRectangles()) {
            player.resolveCollision(roomRectangle);
        }
    }

    // Handle shooting input, either from keyboard or left mouse button
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
            enemy.setPlayer(player);
            enemy.updatePosition(player.getX(), player.getY(), 100);

            if (enemy.isCollidingPlayer(player)) {
                System.out.println("Player-enemy collision!");
                // Handle player-enemy collision, e.g., reduce player health, etc.
                // You may also want to remove the enemy in this case
                enemiesToRemove.add(enemy);
            }

            for (Rectangle roomRectangle : roomManager.getRoomRectangles()) {
                if (enemy.isCollidingRoom(roomRectangle)) {
                    // Handle enemy collision with walls, e.g., change enemy direction, etc.
                }
            }
        }

        enemies.removeAll(enemiesToRemove);
    }

}