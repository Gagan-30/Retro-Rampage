package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.locationtech.jts.geom.Coordinate;

import java.util.*;

public class RoomManager {
    private final int numberOfCells;
    private final Pane root; // The pane on which rooms will be drawn
    private Cell spawnRoom;
    private Cell exitRoom;
    private final List<Cell> cells = new ArrayList<>();
    private final Random random = new Random();
    private Cell spawnCell;
    private Cell spawnRoomCenter;

    public RoomManager(int numberOfCells, Pane root) {
        this.numberOfCells = numberOfCells;
        this.root = root;
    }

    public void generateRooms() {
        cells.clear(); // Clear previous cells if any
        double centerX = root.getWidth() / 2;
        double centerY = root.getHeight() / 2;

        while (cells.size() < numberOfCells) {
            double width = 200 + random.nextDouble() * (400 - 200); // Random width between 50 and 250
            double height = 200 + random.nextDouble() * (400 - 200); // Random height between 50 and 250

            double x = centerX - width / 2 + (random.nextDouble() - 0.5) * (root.getWidth() - width);
            double y = centerY - height / 2 + (random.nextDouble() - 0.5) * (root.getHeight() - height);

            // Ensure that the generated room is within the bounds of the root pane
            x = clamp(x, root.getWidth() - width);
            y = clamp(y, root.getHeight() - height);

            Cell cell = new Cell(x, y, width, height);

            // Check if the cell meets the criteria to be considered a room before adding
            if (cell.getWidth() >= 60.0 && cell.getHeight() >= 60.0) {
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


    public List<Coordinate> getRoomCenters() {
        List<Coordinate> roomCenters = new ArrayList<>();
        for (Cell cell : cells) {
            if (cell.isRoom()) {
                double centerX = cell.getX() + cell.getWidth() / 2.0;
                double centerY = cell.getY() + cell.getHeight() / 2.0;
                roomCenters.add(new Coordinate(centerX, centerY));
            }
        }
        return roomCenters;
    }

    // Optional: Method to visualize rooms on the Pane for debugging or visualization purposes
    public void drawRooms() {
        for (Cell cell : cells) {
            if (cell.isRoom()) {
                Rectangle rectangle = new Rectangle(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                rectangle.setStroke(Color.BLACK); // Border color for all cells

                // Set fill color based on the room type
                if (cell == spawnRoom) {
                    rectangle.setFill(Color.GREEN); // Spawn room in green
                } else if (cell == exitRoom) {
                    rectangle.setFill(Color.RED); // Exit room in red
                } else {
                    rectangle.setFill(Color.TRANSPARENT); // Other rooms remain transparent or another color
                }

                root.getChildren().add(rectangle);
            }
        }
    }


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

    private double clamp(double value, double max) {
        return Math.max(0, Math.min(max, value));
    }

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

    public Cell getSpawnCell() {
        return spawnRoom;
    }
}
