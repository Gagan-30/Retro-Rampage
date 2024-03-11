package com.base.game.retrorampage.LevelGeneration;

public class Cell {
    private double x, y; // Coordinates of the top-left corner of the cell
    private double width;
    private final double height; // Size dimensions of the cell
    private boolean isRoom = false; // Flag indicating whether the cell is considered a room
    private boolean exitRoom;
    private boolean isSpawnRoom = false;
    private boolean isExitRoom = false;

    // Constructor initializes the cell with its position and size
    public Cell(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Checks if this cell overlaps with another cell (cellB)
    public boolean overlaps(Cell cellB) {
        // Determines horizontal and vertical separations between the cells
        boolean horizontalSeparation = this.x + this.width < cellB.x || cellB.x + cellB.width < this.x;
        boolean verticalSeparation = this.y + this.height < cellB.y || cellB.y + cellB.height < this.y;

        // If there's no separation on either axis, the cells overlap
        return !(horizontalSeparation || verticalSeparation);
    }

    // Accessor and mutator methods for the cell's properties
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    // Method to get the center X coordinate of the cell
    public double getCenterX() {
        return this.x + this.width / 2.0;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // Method to get the center Y coordinate of the cell
    public double getCenterY() {
        return this.y + this.height / 2.0;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    // Determines if the cell is marked as a room
    public boolean isRoom() {
        return isRoom;
    }

    public void setRoom(boolean isRoom) {
        this.isRoom = isRoom;
    }
    public void setSpawnRoom(boolean isSpawnRoom) {
        this.isSpawnRoom = isSpawnRoom;
    }

    public void setExitRoom(boolean isExitRoom) {
        this.isExitRoom = isExitRoom;
    }
}