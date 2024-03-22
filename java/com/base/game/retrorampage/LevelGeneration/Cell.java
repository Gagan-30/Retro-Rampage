package com.base.game.retrorampage.LevelGeneration;

/**
 * A class representing a cell in level generation, defined by its position and size.
 */
public class Cell {
    private final double height; // Size dimensions of the cell
    private double x, y; // Coordinates of the top-left corner of the cell
    private double width;
    private boolean isRoom = false; // Flag indicating whether the cell is considered a room
    private boolean isSpawnRoom = false; // Flag indicating whether the cell is a spawn room
    private boolean isExitRoom = false; // Flag indicating whether the cell is an exit room

    /**
     * Constructs a cell with the specified position and size.
     *
     * @param x      The x-coordinate of the top-left corner of the cell.
     * @param y      The y-coordinate of the top-left corner of the cell.
     * @param width  The width of the cell.
     * @param height The height of the cell.
     */
    public Cell(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Checks if this cell overlaps with another cell.
     *
     * @param cellB The other cell to check for overlap.
     * @return true if the cells overlap, false otherwise.
     */
    public boolean overlaps(Cell cellB) {
        // Determines horizontal and vertical separations between the cells
        boolean horizontalSeparation = this.x + this.width < cellB.x || cellB.x + cellB.width < this.x;
        boolean verticalSeparation = this.y + this.height < cellB.y || cellB.y + cellB.height < this.y;

        // If there's no separation on either axis, the cells overlap
        return !(horizontalSeparation || verticalSeparation);
    }

    /**
     * Gets the x-coordinate of the top-left corner of the cell.
     *
     * @return The x-coordinate of the top-left corner of the cell.
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x-coordinate of the top-left corner of the cell.
     *
     * @param x The x-coordinate to set.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Gets the y-coordinate of the top-left corner of the cell.
     *
     * @return The y-coordinate of the top-left corner of the cell.
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y-coordinate of the top-left corner of the cell.
     *
     * @param y The y-coordinate to set.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Gets the width of the cell.
     *
     * @return The width of the cell.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Sets the width of the cell.
     *
     * @param width The width to set.
     */
    public void setWidth(double width) {
        this.width = width;
    }

    /**
     * Gets the height of the cell.
     *
     * @return The height of the cell.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Determines if the cell is marked as a room.
     *
     * @return true if the cell is marked as a room, false otherwise.
     */
    public boolean isRoom() {
        return isRoom;
    }

    /**
     * Sets whether the cell is marked as a room.
     *
     * @param room true if the cell should be marked as a room, false otherwise.
     */
    public void setRoom(boolean room) {
        isRoom = room;
    }

    /**
     * Sets whether the cell is a spawn room.
     *
     * @param spawnRoom true if the cell is a spawn room, false otherwise.
     */
    public void setSpawnRoom(boolean spawnRoom) {
        isSpawnRoom = spawnRoom;
    }

    /**
     * Determines whether the cell is a spawn room.
     *
     * @return true if the cell is a spawn room, false otherwise.
     */
    public boolean isSpawnRoom() {
        return isSpawnRoom;
    }

    /**
     * Determines whether the cell is an exit room.
     *
     * @return true if the cell is an exit room, false otherwise.
     */
    public boolean isExitRoom() {
        return isExitRoom;
    }

    /**
     * Sets whether the cell is an exit room.
     *
     * @param exitRoom true if the cell is an exit room, false otherwise.
     */
    public void setExitRoom(boolean exitRoom) {
        isExitRoom = exitRoom;
    }

    /**
     * Checks if a point (x, y) is contained within the cell.
     *
     * @param pointX The x-coordinate of the point.
     * @param pointY The y-coordinate of the point.
     * @return true if the point is contained within the cell, false otherwise.
     */
    public boolean contains(double pointX, double pointY) {
        return pointX >= x && pointX <= x + width && pointY >= y && pointY <= y + height;
    }

    /**
     * Gets the center x-coordinate of the cell.
     *
     * @return The center x-coordinate of the cell.
     */
    public double getCenterX() {
        return this.x + this.width / 2.0;
    }

    /**
     * Gets the center y-coordinate of the cell.
     *
     * @return The center y-coordinate of the cell.
     */
    public double getCenterY() {
        return this.y + this.height / 2.0;
    }
}