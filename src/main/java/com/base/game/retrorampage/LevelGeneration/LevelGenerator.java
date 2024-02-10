package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class LevelGenerator {
    private final int numberOfCells;
    private Pane root;
    private Scene scene;
    private int WIDTH = 800;
    private int HEIGHT = 600;

    // Initialize the managers
    private RoomManager roomManager;
    private GraphManager graphManager;
    private CorridorManager corridorManager;
    private VisualizationManager visualizationManager;

    public LevelGenerator(int numberOfCells) {
        this.numberOfCells = numberOfCells;
        this.root = new Pane();
        this.scene = new Scene(root, WIDTH, HEIGHT); // Assuming fixed dimensions for simplicity

        // Initialize managers with necessary parameters
        this.roomManager = new RoomManager(numberOfCells, root);
        this.graphManager = new GraphManager();
        this.corridorManager = new CorridorManager(root);
        this.visualizationManager = new VisualizationManager(root);
    }

    public Scene generateLevel() {
        // 1. Generate and identify rooms
        roomManager.generateRooms();
        roomManager.disperseCells();

        // 2. Perform triangulation and generate MST
        var roomCenters = roomManager.getRoomCenters();
        var triangleEdges = graphManager.triangulateRooms(roomCenters);
        var mstEdges = graphManager.generateMST(triangleEdges);

        // 3. Create corridors based on MST edges and additional logic for loops
        corridorManager.createCorridors(mstEdges);

        // 4. VisualizationManager can be used to further customize the scene if needed
        roomManager.drawRooms();
        visualizationManager.drawDelaunayTriangulation(graphManager.getEdges()); // Ensure GraphManager exposes the edges

        // Return the scene containing the generated level
        return this.scene;
    }
}
