package com.base.game.retrorampage.GameAssets;

import javafx.geometry.Bounds;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Represents a bullet fired in the game.
 * Handles bullet movement, shooting, and collision detection.
 */
public class Bullet extends Sprite{
    /** Speed of the bullet in pixels per second. Adjust as needed. */
    private static final double BULLET_SPEED = 350.0;

    /** The root pane of the game. */
    private final Pane root;

    /** ImageView representing the bullet. */
    private final ImageView imageView;

    /** Reference to the player. */
    private final Player player;

    /** Handles user input. */
    private final Input input;

    /** Flag indicating if the bullet is active. */
    private boolean active;

    /** Directional velocity in the x-axis. */
    private double dx;

    /** Directional velocity in the y-axis. */
    private double dy;

    /** Bounds of the bullet in the parent pane. */
    private Bounds boundsInParent;

    /** x-coordinate of the bullet. */
    private double x;

    /** y-coordinate of the bullet. */
    private double y;

    /** Width of the bullet. */
    private double width;

    /** Height of the bullet. */
    private double height;


    /**
     * Constructs a new Bullet.
     *
     * @param size   The size of the bullet.
     * @param image  The image representing the bullet.
     * @param root   The root pane of the game.
     * @param player The player object.
     * @param input  The input handler.
     */
    public Bullet(double size, String image, Pane root, Player player, Input input) {
        this.active = false;
        this.root = root;
        this.player = player;
        this.input = input;
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(size);
        this.imageView.setFitHeight(size);
        this.imageView.setPreserveRatio(true);
    }

    /**
     * Updates the bullet's position and behavior.
     *
     * @param dt The time passed since the last update.
     */
    public void update(double dt) {
        if (isActive()) {
            // Move the bullet in the specified direction
            imageView.setTranslateX(imageView.getTranslateX() + dx * BULLET_SPEED * dt);
            imageView.setTranslateY(imageView.getTranslateY() + dy * BULLET_SPEED * dt);

            // Set the rotation of the ImageView based on the direction
            imageView.setRotate(Math.toDegrees(Math.atan2(dy, dx)));

            // Check if the bullet is out of bounds
            if (isOutOfBounds()) {
                setActive(false); // Set the bullet as inactive when out of bounds
            }
        }
    }

    /**
     * Fires the bullet from the player's position with a given angle.
     *
     * @param playerX      The x-coordinate of the player.
     * @param playerY      The y-coordinate of the player.
     * @param playerAngle  The angle of the player's orientation.
     */
    public void shoot(double playerX, double playerY, double playerAngle) {
        // Set the bullet position and activate it
        imageView.setTranslateX(playerX);
        imageView.setTranslateY(playerY);

        playerAngle = player.getMouseLookingDirection(input);
        // Calculate the direction based on the player's looking direction
        double radians = Math.toRadians(playerAngle); // Convert to radians
        dx = Math.cos(radians);
        dy = Math.sin(radians);

        setActive(true);

        // Draw the bullet
        draw(playerAngle);
    }

    /**
     * Draws the bullet with the given angle.
     *
     * @param angleToMouse The angle towards the mouse cursor.
     */
    public void draw(double angleToMouse) {
        // Set the rotation of the ImageView based on the calculated angle
        imageView.setRotate(angleToMouse);

        // Add the bullet to the root if it's not already a child
        if (!root.getChildren().contains(imageView)) {
            root.getChildren().add(this.imageView);
        }
    }

    /**
     * Checks if the bullet is out of bounds.
     *
     * @return True if the bullet is out of bounds, false otherwise.
     */
    private boolean isOutOfBounds() {
        // Check if the bullet is out of bounds based on your game's screen size
        // Adjust the conditions based on your requirements
        return imageView.getTranslateX() < 0 || imageView.getTranslateY() < 0 ||
                imageView.getTranslateX() > root.getWidth() || imageView.getTranslateY() > root.getHeight();
    }

    /**
     * Checks if the bullet is active.
     *
     * @return True if the bullet is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the bullet's active state.
     *
     * @param active The active state to set.
     */
    public void setActive(boolean active) {
        this.active = active;
        if (!active && root.getChildren().contains(imageView)) {
            root.getChildren().remove(this.imageView); // Remove the bullet from the root when inactive
        }
    }

    /**
     * Gets the bounds of the bullet in the parent pane.
     *
     * @return The bounds of the bullet.
     */
    public Bounds getBoundsInParent() {
        return imageView.getBoundsInParent();
    }

    /**
     * Gets the x-coordinate of the bullet.
     *
     * @return The x-coordinate of the bullet.
     */
    public double getX() {
        return imageView.getBoundsInParent().getMinX();
    }

    /**
     * Gets the y-coordinate of the bullet.
     *
     * @return The y-coordinate of the bullet.
     */
    public double getY() {
        return imageView.getBoundsInParent().getMinY();
    }

    /**
     * Gets the width of the bullet.
     *
     * @return The width of the bullet.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the bullet.
     *
     * @return The height of the bullet.
     */
    public double getHeight() {
        return height;
    }
}