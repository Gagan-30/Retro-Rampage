package com.base.game.retrorampage.LevelGeneration;

import com.base.game.retrorampage.MainMenu.Config;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.locationtech.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages the generation and layout of rooms in the game level.
 */
public class RoomManager {
    /**
     * The desired number of cells/rooms
     */
    private final int numberOfCells;
    /**
     * The pane on which rooms will be drawn
     */
    private final Pane root;
    /**
     * List to store generated cells/rooms
     */
    private final List<Cell> cells = new ArrayList<>();
    /**
     * Random object for generating random values
     */
    private final Random random = new Random();
    /**
     * The room designated as the spawn room
     */
    private Cell spawnRoom;
    /**
     * The room designated as the exit room
     */
    private Cell exitRoom;

    /**
     * Constructor for the RoomManager class.
     *
     * @param numberOfCells The number of cells/rooms to generate.
     * @param root          The root pane where rooms will be drawn.
     */
    public RoomManager(int numberOfCells, Pane root) {
        this.numberOfCells = numberOfCells;
        this.root = root;
    }

    /**
     * Generates random rooms based on the specified criteria.
     */
    public void generateRooms() {
        cells.clear(); // Clear previous cells if any
        double centerX = root.getWidth() / 2;
        double centerY = root.getHeight() / 2;

        Config config = new Config("config.txt");
        double widthScale;
        double heightScale;

        String resolution = config.loadResolutionSetting();
        if (resolution.equals("800 x 600")) {
            widthScale = 0.5;
            heightScale = 0.5;
        } else if (resolution.equals("1280 x 720")) {
            widthScale = 0.75;
            heightScale = 0.75;
        } else {
            widthScale = 1;
            heightScale = 1;
        }

        while (cells.size() < numberOfCells) {
            double width = (200 + random.nextDouble() * (400 - 200)) * widthScale; // Random width between 50 and 250
            double height = (200 + random.nextDouble() * (400 - 200)) * heightScale; // Random height between 50 and 250

            double x = centerX - width / 2 + (random.nextDouble() - 0.5) * (root.getWidth() - width);
            double y = centerY - height / 2 + (random.nextDouble() - 0.5) * (root.getHeight() - height);

            // Ensure that the generated room is within the bounds of the root pane
            x = clamp(x, root.getWidth() - width);
            y = clamp(y, root.getHeight() - height);

            Cell cell = new Cell(x, y, width, height);

            // Check if the cell meets the criteria to be considered a room before adding
            if (cell.getWidth() >= (60.0 * widthScale) && cell.getHeight() >= (60.0 * widthScale)) {
                cell.setRoom(true);
                cells.add(cell);
            }
        }

        disperseCells(); // Disperse cells after they are created

        // Set the first room as the spawn room if it exists
        if (!cells.isEmpty()) {
            spawnRoom = cells.get(0); // Assuming the first cell is a room
            spawnRoom.setSpawnRoom(true);

            // Set the last generated room as the exit room
            exitRoom = cells.get(cells.size() - 1); // Get the last room in the list
            exitRoom.setExitRoom(true);
        }
    }

    /**
     * Retrieves the coordinates of the centers of all the generated rooms.
     *
     * @return A list containing the coordinates of the centers of all the rooms.
     */
    public List<Coordinate> getRoomCenters() {
        // Create a list to store the room centers
        List<Coordinate> roomCenters = new ArrayList<>();

        // Iterate through each cell/room in the list
        for (Cell cell : cells) {
            // Check if the cell is a room
            if (cell.isRoom()) {
                // Calculate the center coordinates of the room
                double centerX = cell.getX() + cell.getWidth() / 2.0;
                double centerY = cell.getY() + cell.getHeight() / 2.0;

                // Create a new Coordinate object with the calculated center coordinates and add it to the list
                roomCenters.add(new Coordinate(centerX, centerY));
            }
        }

        // Return the list of room centers
        return roomCenters;
    }


    /**
     * Disperses the cells/rooms to avoid overlap.
     */
    public void disperseCells() {
        boolean moved;
        do {
            moved = false;
            for (int i = 0; i < cells.size(); i++) {
                for (int j = i + 1; j < cells.size(); j++) {
                    Cell cellA = cells.get(i);
                    Cell cellB = cells.get(j);
                    if (cellA.overlaps(cellB)) {
                        // Adjust positions to reduce overlap
                        adjustPositions(cellA, cellB);
                        moved = true;
                    }
                }
            }
        } while (moved);
    }

    /**
     * Adjusts the positions of overlapping cells/rooms to reduce overlap.
     *
     * @param cellA The first cell/room.
     * @param cellB The second cell/room.
     */
    private void adjustPositions(Cell cellA, Cell cellB) {
        // Calculate the overlap offsets for X and Y axes
        double minDisperseWidth = 80.0;
        double overlapX = (cellA.getWidth() / 2 + cellB.getWidth() / 2 + minDisperseWidth * 1.1)
                - Math.abs(cellA.getCenterX() - cellB.getCenterX());
        double minDisperseHeight = 70.0;
        double overlapY = (cellA.getHeight() / 2 + cellB.getHeight() / 2 + minDisperseHeight * 1.1)
                - Math.abs(cellA.getCenterY() - cellB.getCenterY());

        // Ensure there's a minimum separation distance
        if (overlapX < minDisperseWidth) {
            overlapX += minDisperseWidth - overlapX;
        }
        if (overlapY < minDisperseHeight) {
            overlapY += minDisperseHeight - overlapY;
        }

        // Calculate the direction to move each cell
        double moveAX = overlapX * (cellA.getCenterX() < cellB.getCenterX() ? -1 : 1);
        double moveAY = overlapY * (cellA.getCenterY() < cellB.getCenterY() ? -1 : 1);
        double moveBX = -moveAX;
        double moveBY = -moveAY;

        // Apply the calculated positions, ensuring cells stay within the pane's bounds and maintain minimum separation
        cellA.setX(clamp(cellA.getX() + moveAX, root.getWidth() - cellA.getWidth()));
        cellA.setY(clamp(cellA.getY() + moveAY, root.getHeight() - cellA.getHeight()));
        cellB.setX(clamp(cellB.getX() + moveBX, root.getWidth() - cellB.getWidth()));
        cellB.setY(clamp(cellB.getY() + moveBY, root.getHeight() - cellB.getHeight()));
    }

    /**
     * Clamps a value within a specified range.
     *
     * @param value The value to clamp.
     * @param max   The maximum value.
     * @return The clamped value.
     */
    private double clamp(double value, double max) {
        return Math.max(0, Math.min(max, value));
    }

    /**
     * Visualizes the rooms on the root pane for debugging or visualization purposes.
     */
    public void drawRooms() {
        for (Cell cell : cells) {
            if (cell.isRoom()) {
                Rectangle rectangle = new Rectangle(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                rectangle.setStroke(Color.BLACK); // Border color for all cells

                // Set fill color based on the room type
                if (cell == exitRoom) {
                    rectangle.setFill(Color.DARKRED); // Spawn room in green
                } else {
                    rectangle.setFill(Color.GREY); // Other rooms remain transparent or another color
                }

                root.getChildren().add(rectangle);
            }
        }
    }

    /**
     * Retrieves the list of room rectangles for visualization.
     *
     * @return The list of room rectangles.
     */
    public List<Rectangle> getRoomRectangles() {
        List<Rectangle> roomRectangles = new ArrayList<>();
        for (Cell cell : cells) {
            if (cell.isRoom()) {
                // Create a new Rect based on the cell's position and size
                Rectangle roomRectangle = new Rectangle(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                roomRectangles.add(roomRectangle);
            }
        }
        return roomRectangles;
    }

    /**
     * Retrieves the spawn cell where the player spawns.
     *
     * @return The spawn cell.
     */
    public Cell getSpawnCell() {
        return spawnRoom;
    }

    /**
     * Retrieves the list of generated cells/rooms.
     *
     * @return The list of cells/rooms.
     */
    public List<Cell> getCells() {
        return cells;
    }
}
