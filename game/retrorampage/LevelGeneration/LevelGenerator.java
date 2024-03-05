package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Bullet;
import com.base.game.retrorampage.GameAssets.Enemy;
import com.base.game.retrorampage.GameAssets.Input;
import com.base.game.retrorampage.GameAssets.Player;
import com.base.game.retrorampage.MainMenu.Config;
import javafx.scene.Scene;
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

    private void handleShootInput() {
        String shootKey1 = config.getKeybind("Shoot1");
        String shootKey2 = config.getKeybind("Shoot2");

        if (input.isKeyPressed(shootKey1) || input.isKeyPressed(shootKey2)) {
            if (!isShooting) {
                if (isMouseClick(shootKey1) || isMouseClick(shootKey2)) {
                    Bullet newBullet = new Bullet(10, "bullet.png", root, player, input);
                    newBullet.shoot(player.getX(), player.getY(), player.getRotation());
                    bullets.add(newBullet);
                    isShooting = true;
                } else {
                    // Handle keyboard input
                    Bullet newBullet = new Bullet(10, "bullet.png", root, player, input);
                    newBullet.shoot(player.getX(), player.getY(), player.getRotation());
                    bullets.add(newBullet);
                    isShooting = true;
                }
            }
        } else {
            isShooting = false;
        }
    }

    // Helper method to check if the input corresponds to a mouse click

    private boolean isMouseClick(String inputKey) {
        return inputKey.equals("Right Click") || inputKey.equals("Left Click") || inputKey.equals("MIDDLE");
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
        boolean bulletHitRoom = false;

        for (Rectangle roomRectangle : roomManager.getRoomRectangles()) {
            double bulletX = bullet.getX();
            double bulletY = bullet.getY();
            double roomX = roomRectangle.getX();
            double roomY = roomRectangle.getY();
            double roomWidth = roomRectangle.getWidth();
            double roomHeight = roomRectangle.getHeight();

            // Check if the bullet is within the bounds of the room
            if (bulletX + bullet.getWidth() >= roomX && bulletX <= roomX + roomWidth &&
                    bulletY + bullet.getHeight() >= roomY && bulletY <= roomY + roomHeight) {
                // Bullet collided with the room boundary, handle collision logic if needed
                System.out.println("Bullet hit room boundary!");
                bulletHitRoom = true;
            }
        }

        if (bulletHitRoom) {
            bullet.setActive(false);
        }

        List<Enemy> enemiesToRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (bullet.isActive() && enemy.getSquare().getBoundsInParent().intersects(bullet.getBoundsInParent())) {
                System.out.println("Bullet hit enemy!");
                // Handle enemy hit logic, e.g., reduce enemy health
                enemy.reduceHealth(25); // Adjust the amount by which the health is reduced
                bullet.setActive(false);

                if (enemy.getHealth() <= 0) {
                    System.out.println("Enemy defeated!");
                    // Optionally, you can remove the enemy if its health is zero or below
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