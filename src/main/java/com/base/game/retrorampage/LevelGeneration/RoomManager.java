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
    private final double minDisperseWidth = 60.0;
    private final double minDisperseHeight = 60.0;

    public RoomManager(int numberOfCells, Pane root) {
        this.numberOfCells = numberOfCells;
        this.root = root;
    }

    public void generateRooms() {
        cells.clear(); // Clear previous cells if any
        double centerX = root.getWidth() / 2;
        double centerY = root.getHeight() / 2;

        for (int i = 0; i < numberOfCells; i++) {
            double width = 50 + random.nextDouble() * (250 - 50); // Random width between 50 and 250
            double height = 50 + random.nextDouble() * (250 - 50); // Random height between 50 and 250

            double x = centerX - width / 2;
            double y = centerY - height / 2;

            Cell cell = new Cell(x, y, width, height);

            double roomWidthThreshold = 60.0;
            double roomHeightThreshold = 60.0;
            if (cell.getWidth() >= roomWidthThreshold && cell.getHeight() >= roomHeightThreshold) {
                if (cell.getWidth() >= 60.0 && cell.getHeight() >= 60.0) {
                    cell.setRoom(true);
                }

                cells.add(cell);
            }
        }
        disperseCells(); // Disperse cells after they are created at the center
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
        double overlapX = (cellA.getWidth() / 2 + cellB.getWidth() / 2 + minDisperseWidth * 1.15)
                - Math.abs(cellA.getCenterX() - cellB.getCenterX());
        double overlapY = (cellA.getHeight() / 2 + cellB.getHeight() / 2 + minDisperseHeight * 1.15)
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
                // Create a new Rectangle based on the cell's position and size
                Rectangle roomRectangle = new Rectangle(cell.getX(), cell.getY(), cell.getWidth(), cell.getHeight());
                roomRectangles.add(roomRectangle);
            }
        }
        return roomRectangles;
    }

    // Method to set the exit room using BFS
    public void setExitRoomUsingBFS() {
        if (cells.isEmpty() || spawnRoom == null) {
            System.out.println("Cells or spawn room not initialized.");
            return;
        }

        // Initialize visited array or map if necessary to keep track of visited rooms
        Map<Cell, Boolean> visited = new HashMap<>();
        for (Cell cell : cells) {
            visited.put(cell, false);
        }

        // Queue for BFS
        Queue<Cell> queue = new LinkedList<>();
        queue.add(spawnRoom);
        visited.put(spawnRoom, true);

        Cell currentRoom = null;

        while (!queue.isEmpty()) {
            currentRoom = queue.poll();

            // Iterate over neighbors of the current room
            // This requires a method or a way to get the neighboring rooms of a given room
            List<Cell> neighbors = getNeighbors(currentRoom);
            for (Cell neighbor : neighbors) {
                if (!visited.get(neighbor)) {
                    queue.add(neighbor);
                    visited.put(neighbor, true);
                }
            }
        }

        // After BFS, currentRoom will be the last room visited, which is the furthest from the spawn
        if (currentRoom != null && currentRoom != spawnRoom) {
            exitRoom = currentRoom;
            exitRoom.setExitRoom(true); // You'll need a method in Cell to mark it as the exit room
        }

        // Redraw rooms to visually update the exit room
        drawRooms();
    }

    // This is a placeholder for a method to get neighbors of a cell
    // You'll need to implement this based on how your cells/rooms are connected
    private List<Cell> getNeighbors(Cell cell) {
        // Implement logic to find and return neighboring cells
        return new ArrayList<>();
    }

}
