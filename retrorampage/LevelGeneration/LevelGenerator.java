package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Bullet;
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
    private long lastUpdateTime = System.nanoTime();

    // Managers and components
    private Pane root; // Root pane to contain the level elements
    private RoomManager roomManager; // Manages the generation and drawing of rooms
    private GraphManager graphManager; // Manages graph-related operations
    private CorridorManager corridorManager; // Manages the creation of corridors
    private VisualizationManager visualizationManager; // Manages visualization of the level
    private Input input; // Handles user input
    private Config config; // Manages configuration settings
    private Player player; // Represents the player
    private List<Bullet> bullets; // ArrayList to store bullets

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
        this.player = new Player(50, "player.png"); // Initialize the player
        this.bullets = new ArrayList<>(); // Initialize the bullets list
        this.player.setCorridorManager(corridorManager);
        scene.setOnMouseMoved(event -> input.updateMousePosition(event));
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

        // Return the scene containing the generated level
        return this.scene;
    }

    public void update() {
        double dt = calculateDeltaTime();
        updatePlayerPosition();
        updateBullets(dt);
        handleShootInput();
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
            Bullet newBullet = new Bullet(10, "bullet.png", 300.0, root);
            newBullet.shoot(player.getX(), player.getY(), player.getRotation(), input, player);
            bullets.add(newBullet);
        }
    }

    private void updateBullets(double dt) {
        for (Bullet bullet : bullets) {
            if (bullet.isActive()) {
                bullet.update(dt);
            }
        }
        bullets.removeIf(bullet -> !bullet.isActive());
    }
}
