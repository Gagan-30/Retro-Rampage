package com.base.game.retrorampage.LevelGeneration;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * Manages the creation of hallways between rooms in the game level.
 */
public class CorridorManager {
    private final Pane root; // The JavaFX pane on which corridors will be drawn
    private List<Rectangle> rooms; // List of rectangles representing rooms
    private Rectangle[] hallwayRectangles; // Array to store hallway rectangles
    private Rectangle[] hallwayBounds; // Array to store hallway bounds

    /**
     * Constructs a CorridorManager with the specified root pane.
     *
     * @param root The JavaFX pane on which corridors will be drawn.
     */
    public CorridorManager(Pane root) {
        this.root = root;
    }

    /**
     * Creates hallways between rooms based on the provided edges and hallway width.
     *
     * @param edges        The list of edges connecting room centers.
     * @param hallwayWidth The width of the hallways.
     */
    public void createHallways(List<GraphManager.Edge> edges, double hallwayWidth) {

        for (GraphManager.Edge edge : edges) {
            // Calculate the direction and length of the hallway
            double dx = edge.end.x - edge.start.x;
            double dy = edge.end.y - edge.start.y;

            // First segment is horizontal
            double x1 = edge.start.x;
            double y1 = edge.start.y - hallwayWidth / 2; // Offset by half the hallway width
            double w1 = dx;
            double h1 = hallwayWidth;

            // Second segment is vertical
            double x2 = edge.end.x - hallwayWidth / 2; // Offset by half the hallway width
            double y2 = edge.start.y;
            double w2 = hallwayWidth;
            double h2 = dy;

            // Adjust the length and position if dx or dy is negative
            if (dx < 0) {
                x1 = edge.end.x;
                w1 = -dx;
            }
            if (dy < 0) {
                y2 = edge.end.y;
                h2 = -dy;
            }

            // Create rectangles for the hallway segments
            Rectangle hallwayHorizontal = new Rectangle(x1, y1, w1, h1);
            Rectangle hallwayVertical = new Rectangle(x2, y2, w2, h2);

            adjustSegmentIfOverlapping(hallwayHorizontal);
            adjustSegmentIfOverlapping(hallwayVertical);

            // Set the fill color to transparent and the stroke to a visible color
            hallwayHorizontal.setFill(Color.BLACK);
            hallwayHorizontal.setStroke(Color.BLACK);
            hallwayVertical.setFill(Color.BLACK);
            hallwayVertical.setStroke(Color.BLACK);

            // Add the hallway segments to the root pane
            root.getChildren().addAll(hallwayHorizontal, hallwayVertical);
        }
    }
    /**
     * Adjusts a hallway segment if it overlaps with a room, ensuring no overlap occurs.
     *
     * @param hallwaySegment The hallway segment to adjust.
     */
    private void adjustSegmentIfOverlapping(Rectangle hallwaySegment) {
        for (Rectangle room : rooms) {
            if (hallwaySegment.getBoundsInParent().intersects(room.getBoundsInParent())) {
                // Calculate intersection details
                double intersectionMinX = Math.max(hallwaySegment.getX(), room.getX());
                double intersectionMaxX = Math.min(hallwaySegment.getX() + hallwaySegment.getWidth(), room.getX() + room.getWidth());
                double intersectionMinY = Math.max(hallwaySegment.getY(), room.getY());
                double intersectionMaxY = Math.min(hallwaySegment.getY() + hallwaySegment.getHeight(), room.getY() + room.getHeight());

                // Determine if hallway segment is horizontal or vertical by comparing width and height
                boolean isHorizontal = hallwaySegment.getWidth() > hallwaySegment.getHeight();

                if (isHorizontal) { // Horizontal hallway segment
                    if (hallwaySegment.getX() < room.getX()) {
                        // Hallway is to the left of the room, shorten it to end at the room's left edge
                        hallwaySegment.setWidth(intersectionMinX - hallwaySegment.getX());
                    } else {
                        // Hallway is to the right of the room, move it to start at the room's right edge and adjust the width
                        double newWidth = hallwaySegment.getWidth() - (intersectionMaxX - hallwaySegment.getX());
                        hallwaySegment.setX(intersectionMaxX);
                        hallwaySegment.setWidth(newWidth);
                    }
                } else { // Vertical hallway segment
                    if (hallwaySegment.getY() < room.getY()) {
                        // Hallway is above the room, shorten it to end at the room's top edge
                        hallwaySegment.setHeight(intersectionMinY - hallwaySegment.getY());
                    } else {
                        // Hallway is below the room, move it to start at the room's bottom edge and adjust the height
                        double newHeight = hallwaySegment.getHeight() - (intersectionMaxY - hallwaySegment.getY());
                        hallwaySegment.setY(intersectionMaxY);
                        hallwaySegment.setHeight(newHeight);
                    }
                }

                // If the adjustment makes the width or height zero or negative, remove this segment
                if (hallwaySegment.getWidth() <= 0 || hallwaySegment.getHeight() <= 0) {
                    root.getChildren().remove(hallwaySegment);
                    return;
                }
            }
        }
    }

    /**
     * Sets the list of rooms managed by this CorridorManager.
     *
     * @param rooms The list of rooms represented as rectangles.
     */
    public void setRooms(List<Rectangle> rooms) {
        this.rooms = rooms;
    }

    /**
     * Gets the array of hallway rectangles.
     *
     * @return The array of hallway rectangles.
     */
    public Rectangle[] getHallwayRectangles() {
        return hallwayRectangles;
    }

    /**
     * Gets the array of hallway bounds.
     *
     * @return The array of hallway bounds.
     */
    public Rectangle[] getHallwayBounds() {
        return hallwayBounds;
    }

    /**
     * Checks if a position (x, y) is within any of the rooms.
     *
     * @param x      The x-coordinate of the position.
     * @param y      The y-coordinate of the position.
     * @param width  The width of the position.
     * @param height The height of the position.
     * @return true if the position is within any of the rooms, false otherwise.
     */
    public boolean isPositionWithinCell(double x, double y, double width, double height) {
        for (Rectangle roomRectangle : rooms) {
            if (roomRectangle.getBoundsInParent().contains(x, y, width, height)) {
                return true;
            }
        }
        return false;
    }
}
