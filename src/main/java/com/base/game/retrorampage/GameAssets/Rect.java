package com.base.game.retrorampage.GameAssets;

import java.util.Arrays;

/**
 * A rectangle shape, defined by its position and size,
 * particularly useful in collision detection.
 */
public class Rect {
    /**
     * x-coordinate of left edge of rectangle
     */
    public double left;

    /**
     * y-coordinate of top edge of rectangle
     */
    public double top;

    /**
     * width of rectangle
     */
    public double width;

    /**
     * height of rectangle
     */
    public double height;

    /**
     * x-coordinate of right edge of rectangle
     */
    public double right;

    /**
     * y-coordinate of bottom edge of rectangle
     */
    public double bottom;
    private double x;
    private double y;

    /**
     * Initialize rectangle with all values set to 0.
     */
    public Rect() {
        setValues(0, 0, 0, 0);
    }

    /**
     * Initialize rectangle data from coordinates of top-left corner and size.
     *
     * @param left   x-coordinate of top-left corner (left edge) of rectangle
     * @param top    y-coordinate of top-left corner (top edge) of rectangle
     * @param width  width of rectangle
     * @param height height of rectangle
     */
    public Rect(double left, double top, double width, double height) {
        setValues(left, top, width, height);
    }

    /**
     * Set rectangle data.
     * Used to update game entities that move and/or change size.
     *
     * @param left   x-coordinate of top-left corner (left edge) of rectangle
     * @param top    y-coordinate of top-left corner (top edge) of rectangle
     * @param width  width of rectangle
     * @param height height of rectangle
     */
    public void setValues(double left, double top, double width, double height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.right = left + width;
        this.bottom = top + height;
    }

    /**
     * Update rectangle data.
     * Used for game entities that move.
     *
     * @param left x-coordinate of top-left corner (left edge) of rectangle
     * @param top  y-coordinate of top-left corner (top edge) of rectangle
     */
    public void setPosition(double left, double top) {
        setValues(left, top, this.width, this.height);
    }

    /**
     * Set the size of the rectangle.
     *
     * @param width  The new width of the rectangle.
     * @param height The new height of the rectangle.
     */
    public void setSize(double width, double height) {
        setValues(this.left, this.top, width, height);
    }

    /**
     * Determine if this rectangle overlaps with another rectangle.
     *
     * @param other The rectangle to check for overlap.
     * @return True if this rectangle overlaps with the other rectangle, false otherwise.
     */
    public boolean overlaps(Rect other) {
        boolean noOverlap = (other.right <= this.left)
                || (this.right <= other.left)
                || (other.bottom <= this.top)
                || (this.bottom <= other.top);
        return !noOverlap;
    }

    /**
     * Calculate the minimum translation vector needed to separate this rectangle from another rectangle.
     *
     * @param other The other rectangle.
     * @return The minimum translation vector.
     */
    public Vector getMinimumTranslationVector(Rect other) {
        Vector[] differences = {
                new Vector(other.right - this.left, 0),
                new Vector(other.left - this.right, 0),
                new Vector(0, other.bottom - this.top),
                new Vector(0, other.top - this.bottom)
        };

        Arrays.sort(differences);

        return differences[0];
    }

    /**
     * Get the x-coordinate of the rectangle.
     *
     * @return The x-coordinate of the rectangle.
     */
    public double getX() {
        return x;
    }

    /**
     * Get the y-coordinate of the rectangle.
     *
     * @return The y-coordinate of the rectangle.
     */
    public double getY() {
        return y;
    }
}
