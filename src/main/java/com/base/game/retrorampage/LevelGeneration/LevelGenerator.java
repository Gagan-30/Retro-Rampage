package com.base.game.retrorampage.LevelGeneration;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.*;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
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
        List<Coordinate[]> triangleGeometries =
        List<Edge> allEdges = convertTrianglesToEdges(triangleGeometries);
        List<Edge> mstEdges = generateAndVisualizeMST(allEdges);
        List<Edge> additionalEdges = reintroduceLoops(mstEdges, triangleGeometries, 0.125); // 12.5% chance
        List<Edge> allCorridorEdges = new ArrayList<>(mstEdges);
        allCorridorEdges.addAll(additionalEdges);
        constructCorridors(allCorridorEdges);
        incorporateUnusedCells();

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
        incorporateUnusedCells();
    }

    private double getRandomDimension() {
        // Generate a normally distributed value centered around meanSize
        // meanSize and sizeStandardDeviation are used in normal distribution for cell size.
        double meanSize = 50.0;
        // Standard deviation to control the spread of sizes
        double sizeStandardDeviation = 35.0;
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
            // Calculate the center x-coordinate of the cell by adding half the width to the x-coordinate
            double centerX = cell.getX() + cell.getWidth() / 2.0;
            // Calculate the center y-coordinate of the cell by adding half the height to the y-coordinate
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

    /// This method updates the map with the new corridor, making it cheaper to go through existing corridors
    private void updateMapWithCorridor(List<Point> corridor) {
        for (Point p : corridor) {
            Cell cell = pointToCellMap.get(new Coordinate(p.x, p.y));
            if (cell != null && !cell.isObstacle()) {
                cell.setOccupied(true);
                cell.setPassageCost(0.5); // For example, make it half as costly to pass through
            }
        }
    }

    private void constructCorridors(List<Edge> edges) {
        Set<Point> obstacles = getObstacles();
        for (Edge edge : edges) {
            Point start = new Point((int) edge.start.x, (int) edge.start.y);
            Point goal = new Point((int) edge.end.x, (int) edge.end.y);
            List<Point> path = AStarPathFinder.findPath(start, goal, obstacles);
            for (Point point : path) {
                drawCorridorSegment(point);
            }
            updateMapWithCorridor(path);
        }
    }

    private Set<Point> getObstacles() {
        Set<Point> obstacles = new HashSet<>();
        for (Cell cell : cells) {
            if (cell.isObstacle()) {
                Point p = new Point((int) cell.getCenterX(), (int) cell.getCenterY());
                obstacles.add(p);
            }
        }
        return obstacles;
    }

    private void drawCorridorSegment(Point point) {
        Cell cell = pointToCellMap.get(new Coordinate(point.x, point.y));
        if (cell != null && !cell.isObstacle()) {
            Rectangle corridorSegment = new Rectangle(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
            corridorSegment.setFill(Color.DARKGRAY);
            root.getChildren().add(corridorSegment);
        }
    }

    private List<Edge> reintroduceLoops(List<Edge> mstEdges, List<Coordinate[]> triangleGeometries, double percentage) {
        List<Edge> allEdges = convertTrianglesToEdges(triangleGeometries);
        List<Edge> nonMstEdges = new ArrayList<>(allEdges);
        allEdges.removeAll(nonMstEdges); // Remove all non MST edges to get the MST edges

        // Shuffle and take a percentage of the non-MST edges
        Collections.shuffle(nonMstEdges);
        int edgesToAddBack = (int) (nonMstEdges.size() * percentage);
        List<Edge> edgesToReintroduce = nonMstEdges.subList(0, edgesToAddBack);

        // Add these edges back to create loops
        for (Edge edge : edgesToReintroduce) {
            createCorridor(edge.start, edge.end); // This will use the original corridor creation method, which uses black by default
            createWideCorridor(edge.start, edge.end, 10); // Adjust the width as needed
        }
        return allEdges;
    }

    // Overloaded method to create corridors with specified color
    private void createCorridor(Coordinate start, Coordinate end) {
        Cell startCell = pointToCellMap.get(start);
        Cell endCell = pointToCellMap.get(end);

        if (startCell != null && endCell != null) {
            // Create a line representing the hallway
            Line corridor = new Line(startCell.getCenterX(), startCell.getCenterY(), endCell.getCenterX(), endCell.getCenterY());
            corridor.setStrokeWidth(2); // Set the width of the hallway line
            corridor.setStroke(Color.TRANSPARENT); // Set the color of the hallway line

            // Add the line to the root pane
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

    // New method to incorporate unused cells
    private void incorporateUnusedCells() {
        Set<Coordinate> usedCoordinates = new HashSet<>();
        for (Cell cell : cells) {
            if (cell.isRoom()) {
                usedCoordinates.add(new Coordinate(cell.getCenterX(), cell.getCenterY()));
            }
        }

        // Any cell not in usedCoordinates is considered unused
        for (Cell cell : cells) {
            Coordinate center = new Coordinate(cell.getCenterX(), cell.getCenterY());
            if (!usedCoordinates.contains(center)) {
                // This is an unused cell; you can now decide how to incorporate it
                // For example, if you want to turn it into an obstacle:
                Rectangle obstacle = new Rectangle(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                obstacle.setFill(Color.GREY); //mark unused cells with grey
                root.getChildren().add(obstacle);
            }
        }
    }

    // Helper method to check if a point is within any cell boundaries
    private boolean isWithinAnyCell(Point point) {
        for (Cell cell : cells) {
            if (point.x > cell.getX() && point.x < cell.getX() + cell.getWidth() && point.y > cell.getY() && point.y < cell.getY() + cell.getHeight()) {
                return true; // The point is inside a cell
            }
        }
        return false; // The point is not inside any cell
    }


    private void createWideCorridor(Coordinate start, Coordinate end, double width) {
        Cell startCell = pointToCellMap.get(start);
        Cell endCell = pointToCellMap.get(end);

        if (startCell != null && endCell != null) {
            // Calculate the angle between the start and end points
            double angle = Math.atan2(endCell.getCenterY() - startCell.getCenterY(),
                    endCell.getCenterX() - startCell.getCenterX());

            // Calculate the four corners of the wide corridor
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            // Create the points for the wide corridor
            Point2D[] corridorPoints = new Point2D[4];
            corridorPoints[0] = new Point2D(startCell.getCenterX() - sin * width / 2,
                    startCell.getCenterY() + cos * width / 2);
            corridorPoints[1] = new Point2D(startCell.getCenterX() + sin * width / 2,
                    startCell.getCenterY() - cos * width / 2);
            corridorPoints[2] = new Point2D(endCell.getCenterX() + sin * width / 2,
                    endCell.getCenterY() - cos * width / 2);
            corridorPoints[3] = new Point2D(endCell.getCenterX() - sin * width / 2,
                    endCell.getCenterY() + cos * width / 2);

            // Check for intersections and adjust points
            List<Point2D> adjustedPoints = new ArrayList<>();
            for (int i = 0; i < corridorPoints.length; i++) {
                int nextIndex = (i + 1) % corridorPoints.length;
                Line corridorEdge = new Line(corridorPoints[i].getX(), corridorPoints[i].getY(),
                        corridorPoints[nextIndex].getX(), corridorPoints[nextIndex].getY());
                boolean intersectionFound = false;

                for (Cell cell : cells) {
                    Rectangle cellBounds = new Rectangle(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                    Point2D intersection = calculateIntersection(corridorEdge, cellBounds);
                    if (intersection != null) {
                        // Adjust the corridor edge to end at the intersection point
                        adjustedPoints.add(corridorPoints[i]);
                        adjustedPoints.add(intersection);
                        intersectionFound = true;
                        break;
                    }
                }

                if (!intersectionFound) {
                    adjustedPoints.add(corridorPoints[i]);
                }
            }

            // Create a polygon representing the adjusted wide corridor
            Polygon hallway = new Polygon();
            for (Point2D p : adjustedPoints) {
                hallway.getPoints().addAll(p.getX(), p.getY());
            }
            hallway.setFill(Color.LIGHTGRAY);

            // Add the polygon to the root pane
            root.getChildren().add(hallway);
        }
    }

    private Point2D calculateIntersection(Line corridor, Rectangle cell) {
        // Define the four edges of the cell
        Line topEdge = new Line(cell.getX(), cell.getY(), cell.getX() + cell.getWidth(), cell.getY());
        Line bottomEdge = new Line(cell.getX(), cell.getY() + cell.getHeight(), cell.getX() +
                cell.getWidth(), cell.getY() + cell.getHeight());
        Line leftEdge = new Line(cell.getX(), cell.getY(), cell.getX(), cell.getY() + cell.getHeight());
        Line rightEdge = new Line(cell.getX() + cell.getWidth(), cell.getY(), cell.getX() +
                cell.getWidth(), cell.getY() + cell.getHeight());

        // Check for intersection with each of the edges
        Point2D intersection = null;
        if (corridor.intersects(topEdge.getBoundsInLocal())) {
            intersection = intersectionPoint(corridor, topEdge);
        } else if (corridor.intersects(bottomEdge.getBoundsInLocal())) {
            intersection = intersectionPoint(corridor, bottomEdge);
        } else if (corridor.intersects(leftEdge.getBoundsInLocal())) {
            intersection = intersectionPoint(corridor, leftEdge);
        } else if (corridor.intersects(rightEdge.getBoundsInLocal())) {
            intersection = intersectionPoint(corridor, rightEdge);
        }

        return intersection;
    }

    private Point2D intersectionPoint(Line a, Line b) {
        // Formula to calculate intersection point
        double d = (a.getStartX() - a.getEndX()) * (b.getStartY() - b.getEndY()) - (a.getStartY() - a.getEndY()) *
                (b.getStartX() - b.getEndX());
        if (d == 0) return null; // Lines are parallel

        double xi = ((b.getStartX() - b.getEndX()) * (a.getStartX() * a.getEndY() - a.getStartY() * a.getEndX())
                - (a.getStartX() - a.getEndX()) * (b.getStartX() * b.getEndY() - b.getStartY() * b.getEndX())) / d;
        double yi = ((b.getStartY() - b.getEndY()) * (a.getStartX() * a.getEndY() - a.getStartY() * a.getEndX())
                - (a.getStartY() - a.getEndY()) * (b.getStartX() * b.getEndY() - b.getStartY() * b.getEndX())) / d;

        return new Point2D(xi, yi);
    }
}