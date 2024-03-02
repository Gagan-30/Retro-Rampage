package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.GameAssets.Input;
import com.base.game.retrorampage.GameAssets.Player;
import com.base.game.retrorampage.MainMenu.Config;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
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

        // 3. Create hallways based on MST edges and loops
        corridorManager.createHallways(loopedEdges, 10); // 10 is the hallway width

        // 4. Create corridors based on MST edges and additional logic for loops
        corridorManager.createCorridors(mstEdges);

        // 5. Draw Rooms and Triangulation
        roomManager.drawRooms();
        visualizationManager.drawDelaunayTriangulation(graphManager.getEdges()); // Ensure GraphManager exposes the edges
        corridorManager.createHallways(loopedEdges, 75); // Draw hallways again for visualization

        // 6. Draw the player in the center of the spawn room cell
        Cell spawnRoomCenter = roomManager.getSpawnCell();
        player.drawInSpawn(spawnRoomCenter, root);

        // Return the scene containing the generated level
        return this.scene;
    }

    // Update player's position and orientation based on input
    void updatePlayerPosition() {
        player.updatePosition(input, config, 120.0);

        // Check for collisions with rooms
        for (Rectangle roomRectangle : roomManager.getRoomRectangles()) {
            player.resolveCollision(roomRectangle);
        }
    }
}