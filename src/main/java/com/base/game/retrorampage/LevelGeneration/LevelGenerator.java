package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.*;

import org.locationtech.jts.triangulate.DelaunayTriangulationBuilder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class LevelGenerator {
    private final int numberOfCells;// numberOfCells indicates how many cells will be generated.
    private final ArrayList<Cell> cells = new ArrayList<>();// cells is a dynamic array that will hold all the Cell objects created.
    private final Random random = new Random();// random is used to generate random numbers for various calculations.
    private final Pane root = new Pane();// root is the JavaFX pane that will hold the visual representation of the level.

    private final int sceneWidth = 800;
    private final int sceneHeight = 600;// sceneWidth and sceneHeight define the dimensions of the level.
    private final double roomWidthThreshold = 50;
    private final double roomHeightThreshold = 50;   // roomWidthThreshold and roomHeightThreshold are the size limits for a cell to be considered a room.

    private final Map<Coordinate, Cell> pointToCellMap = new HashMap<>(); // pointToCellMap maps the center coordinates of a cell to the cell object for easy lookup.
    private final Map<Coordinate, Coordinate> parent = new HashMap<>();

    public LevelGenerator(int numberOfCells) {
        this.numberOfCells = numberOfCells;
    }

    public Scene generateLevel() {
        generateCells();
        separateCells();
        identifyRooms();
        List<Coordinate> roomCenters = prepareRoomCenters();
        List<Coordinate[]> triangleGeometries = triangulateRooms(roomCenters);
        List<Edge> allEdges = convertTrianglesToEdges(triangleGeometries);
        List<Edge> mstEdges = generateAndVisualizeMST(allEdges); // This should return the MST edges
        constructCorridors(mstEdges); // Using MST edges to construct corridors
        reintroduceLoops(mstEdges, triangleGeometries, 0.15); // Introduce loops, for example, 15% of the non-MST edges
        buildGraph();
        return new Scene(root, sceneWidth, sceneHeight);
    }

    private void generateCells() {
        for (int i = 0; i < numberOfCells; i++) {
            double width = getRandomDimension();
            double height = getRandomDimension();
            // positionStandardDeviation is used in normal distribution for cell positioning.
            double positionStandardDeviation = 100.0;
            double x = getRandomPosition(positionStandardDeviation, sceneWidth - width); // Adjusted for cell width
            double y = getRandomPosition(positionStandardDeviation, sceneHeight - height); // Adjusted for cell height

            Cell cell = new Cell(x, y, width, height);
            if (!isOverlapping(cell)) {
                cells.add(cell);
                cell.draw(root);
            } else {
                i--; // Retry this cell generation if overlap detected
            }
        }
    }

    private double getRandomDimension() {
        // Generate a normally distributed value centered around meanSize
        // meanSize and sizeStandardDeviation are used in normal distribution for cell size.
        double meanSize = 50.0;
        // Standard deviation to control the spread of sizes
        double sizeStandardDeviation = 15.0;
        double dimension = meanSize + (random.nextGaussian() * sizeStandardDeviation);
        // Ensure the dimension falls within your desired range
        return Math.max(20, Math.min(100, dimension));
    }

    private double getRandomPosition(double standardDeviation, double sceneSize) {
        // Generate a normally distributed position around the center of the scene
        double position = (sceneSize / 2.0) + (random.nextGaussian() * standardDeviation);
        // Clamp the position to ensure it's within the scene bounds
        // padding is used to ensure cells are not generated too close to the scene's edges.
        int padding = 10;
        return Math.max(padding, Math.min(sceneSize - padding, position));
    }

    private boolean isOverlapping(Cell newCell) {
        for (Cell cell : cells) {
            if (newCell.overlaps(cell)) {
                return true;
            }
        }
        return false;
    }

    private void separateCells() {
        int iterations = 0;
        boolean overlapExists;
        // maxIterations is used to prevent infinite loops when adjusting overlapping cells.
        int maxIterations = 1000;
        do {
            overlapExists = adjustOverlappingCells();
            iterations++;
        } while (overlapExists && iterations < maxIterations);
    }

    private boolean adjustOverlappingCells() {
        for (int i = 0; i < cells.size(); i++) {
            for (int j = i + 1; j < cells.size(); j++) {
                if (cells.get(i).adjustIfOverlaps(cells.get(j))) {
                    return true; // Overlap found and adjustments made
                }
            }
        }
        return false; // No overlaps found
    }

    private void identifyRooms() {
        cells.forEach(cell -> {
            if (cell.getWidth() >= roomWidthThreshold && cell.getHeight() >= roomHeightThreshold) {
                cell.setRoom(true);
            }
        });
    }

    private List<Coordinate> prepareRoomCenters() {
        // Initialize an empty list to hold the center points.
        List<Coordinate> roomCenters = new ArrayList<>();

        // Iterate over each cell in the level.
        for (Cell cell : cells) {
            // Calculate the center x-coordinate of the cell by adding half the width to the x-coordinate.
            double centerX = cell.getX() + cell.getWidth() / 2.0;
            // Calculate the center y-coordinate of the cell by adding half the height to the y-coordinate.
            double centerY = cell.getY() + cell.getHeight() / 2.0;

            // Create a new Coordinate object using the center x and y values.
            Coordinate center = new Coordinate(centerX, centerY);

            // Add the center Coordinate to the list of room centers.
            roomCenters.add(center);

            // Add the center Coordinate and its corresponding cell to a map for quick lookup.
            // This will be useful later when we need to find the cell that a given center point belongs to.
            pointToCellMap.put(center, cell);
        }

        // Return the completed list of room centers.
        return roomCenters;
    }

    private List<Coordinate[]> triangulateRooms(List<Coordinate> roomCenters) {
        // Create an instance of GeometryFactory, which is a utility class to create geometry objects.
        GeometryFactory geometryFactory = new GeometryFactory();

        // Instantiate DelaunayTriangulationBuilder, a class that builds the Delaunay triangulation for a set of points.
        DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();

        // Convert the list of room center points into a MultiPoint geometry and set it as the input for triangulation.
        builder.setSites(geometryFactory.createMultiPointFromCoords(roomCenters.toArray(new Coordinate[0])));

        // Perform the triangulation and retrieve a Geometry collection representing the triangles.
        org.locationtech.jts.geom.Geometry triangles = builder.getTriangles(geometryFactory);

        // Prepare a list to hold the coordinates of each triangle's vertices.
        List<Coordinate[]> triangleCoordinates = new ArrayList<>();

        // Iterate through each geometry in the collection, which represents a triangle.
        for (int i = 0; i < triangles.getNumGeometries(); i++) {
            // Cast the geometry to a Polygon object to access its vertices (each triangle is represented as a polygon).
            org.locationtech.jts.geom.Polygon triangle = (org.locationtech.jts.geom.Polygon) triangles.getGeometryN(i);

            // Extract the coordinates of the triangle's vertices and add them to the list.
            triangleCoordinates.add(triangle.getCoordinates());
        }

        // Return the list of triangles represented by arrays of coordinates.
        return triangleCoordinates;
    }

    private void constructCorridors(List<Edge> edges) {
        for (Edge edge : edges) {
            createCorridor(edge.start, edge.end); // Use the existing method to draw corridors
        }
    }


    private void reintroduceLoops(List<Edge> mstEdges, List<Coordinate[]> triangleGeometries, double percentage) {
        List<Edge> allEdges = convertTrianglesToEdges(triangleGeometries);
        List<Edge> nonMstEdges = new ArrayList<>(allEdges);
        nonMstEdges.removeAll(mstEdges); // Remove all MST edges to get the non-MST edges

        // Shuffle and take a percentage of the non-MST edges
        Collections.shuffle(nonMstEdges);
        int edgesToAddBack = (int) (nonMstEdges.size() * percentage);
        List<Edge> edgesToReintroduce = nonMstEdges.subList(0, edgesToAddBack);

        // Add these edges back to create loops
        for (Edge edge : edgesToReintroduce) {
            createCorridor(edge.start, edge.end); // This will use the original corridor creation method, which uses black by default
        }
    }

    // Overloaded method to create corridors with specified color
    private void createCorridor(Coordinate start, Coordinate end) {
        Cell startCell = pointToCellMap.get(start);
        Cell endCell = pointToCellMap.get(end);

        if (startCell != null && endCell != null) {
            Line corridor = new Line(startCell.getCenterX(), startCell.getCenterY(), endCell.getCenterX(), endCell.getCenterY());
            corridor.setStrokeWidth(2);
            root.getChildren().add(corridor);
        }
    }


    // Find set with path compression
    private Coordinate find(Coordinate i) {
        if (!parent.get(i).equals(i)) {
            parent.put(i, find(parent.get(i)));
        }
        return parent.get(i);
    }

    // Union by rank (simplified as naive union for this example)
    private void union(Coordinate x, Coordinate y) {
        Coordinate xRoot = find(x);
        Coordinate yRoot = find(y);
        parent.put(xRoot, yRoot);
    }

    private List<Edge> convertTrianglesToEdges(List<Coordinate[]> triangleCoordinates) {
        Set<Edge> edges = new HashSet<>(); // Use a set to automatically avoid duplicate edges

        for (Coordinate[] triangle : triangleCoordinates) {
            for (int i = 0; i < triangle.length; i++) {
                Coordinate start = triangle[i];
                Coordinate end = triangle[(i + 1) % triangle.length]; // Ensure wrapping to the first coordinate
                Edge edge = new Edge(start, end);
                edges.add(edge);
            }
        }

        return new ArrayList<>(edges); // Convert the set back to a list
    }


    // Assuming `triangulateRooms` method returns a list of Edges for simplicity
    private List<Edge> generateAndVisualizeMST(List<Edge> edges) {
        // Initialize each node's parent to itself
        for (Edge edge : edges) {
            parent.putIfAbsent(edge.start, edge.start);
            parent.putIfAbsent(edge.end, edge.end);
        }

        // Kruskal's algorithm to construct MST
        for (Edge edge : edges) {
            Coordinate root1 = find(edge.start);
            Coordinate root2 = find(edge.end);

            if (!root1.equals(root2)) {
                createCorridor(edge.start, edge.end); // Visualize the corridor
                union(root1, root2);
            }
        }
        return edges;
    }

    private void buildGraph() {
    }

    private static class Edge implements Comparable<Edge> {
        Coordinate start;
        Coordinate end;
        double weight;

        public Edge(Coordinate start, Coordinate end) {
            this.start = start;
            this.end = end;
            // Euclidean distance as weight
            this.weight = Math.sqrt(Math.pow(start.x - end.x, 2) + Math.pow(start.y - end.y, 2));
        }

        @Override
        public int compareTo(Edge other) {
            return Double.compare(this.weight, other.weight);
        }
    }
}
