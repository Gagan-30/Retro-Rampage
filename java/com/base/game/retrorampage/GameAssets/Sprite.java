package com.base.game.retrorampage.GameAssets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Represents a sprite in the game.
 */
public class Sprite {
    /**
     * The position of the sprite in the game world.
     */
    public Vector position;

    /**
     * The angle of rotation (in degrees) of the texture.
     */
    public double angle;

    /**
     * Determines whether the texture is reversed along the x direction.
     */
    public boolean mirrored;

    /**
     * Determines whether the texture is reversed along the y direction.
     */
    public boolean flipped;

    /**
     * The amount of transparency; value from 0.0 (fully transparent) to 1.0 (fully opaque).
     */
    public double opacity;

    /**
     * The shape used for collision.
     */
    public Rect boundary;

    /**
     * The width of the sprite.
     */
    public double width;

    /**
     * The height of the sprite.
     */
    public double height;

    /**
     * Determines if the sprite will be visible.
     */
    public boolean visible;

    public Pane root;
    ImageView imageView;
    private double x;
    private double y;
    private double rotation;

    /**
     * Constructs a Sprite object with default values.
     */
    public Sprite() {
        position = new Vector();
        angle = 0;
        mirrored = false;
        flipped = false;
        opacity = 1;
        boundary = new Rect();
        visible = true;
    }

    /**
     * Constructs a Sprite object with the given image path and size.
     *
     * @param imagePath The path of the image.
     * @param size      The size of the sprite.
     */
    public Sprite(String imagePath, double size) {
        Image image = new Image(imagePath);
        this.imageView = new ImageView(image);
        this.imageView.setFitWidth(size);
        this.imageView.setFitHeight(size);

        // Initialize the position vector
        this.position = new Vector();
    }

    /**
     * Set the coordinates of the center of this sprite.
     *
     * @param x The x-coordinate of the center of the sprite.
     * @param y The y-coordinate of the center of the sprite.
     */
    public void setPosition(double x, double y) {
        // Ensure the position vector is not null before invoking setValues
        if (this.position != null) {
            this.position.setValues(x, y);
            this.imageView.relocate(x, y);
        } else {
            // Log an error or throw an exception if needed
            System.err.println("Error: Position vector is null.");
        }
    }

    /**
     * Get the x-coordinate of the sprite.
     *
     * @return The x-coordinate of the sprite.
     */
    public double getX() {
        return this.imageView.getLayoutX();
    }

    /**
     * Set the x-coordinate of the sprite.
     *
     * @param x The x-coordinate to set.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Get the y-coordinate of the sprite.
     *
     * @return The y-coordinate of the sprite.
     */
    public double getY() {
        return this.imageView.getLayoutY();
    }

    /**
     * Set the y-coordinate of the sprite.
     *
     * @param y The y-coordinate to set.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Get the position vector of the sprite.
     *
     * @return The position vector of the sprite.
     */
    public Vector getPosition() {
        return position;
    }

    /**
     * Set the root pane for the sprite.
     *
     * @param root The root pane to set.
     */
    public void setRoot(Pane root) {
        this.root = root;
    }

    /**
     * Get the rotation angle of the sprite.
     *
     * @return The rotation angle of the sprite.
     */
    public double getRotation() {
        return rotation;
    }

    /**
     * Set the rotation angle of the sprite.
     *
     * @param angle The rotation angle to set.
     */
    public void setRotation(double angle) {
        this.imageView.setRotate(angle);
    }

    /**
     * Add the sprite to the specified pane.
     *
     * @param root The pane to add the sprite to.
     */
    public void addToPane(Pane root) {
        if (!root.getChildren().contains(imageView)) {
            root.getChildren().add(imageView);
        } else {
            // If imageView is already a child, remove it and add it again
            root.getChildren().remove(imageView);
            root.getChildren().add(imageView);
        }
        updateImageViewPosition();  // Update the position of the imageView
    }

    /**
     * Remove the sprite from its pane.
     */
    public void removeFromPane() {
        if (root != null) {
            root.getChildren().remove(imageView);
        }
    }

    /**
     * Update the position of the ImageView.
     */
    protected void updateImageViewPosition() {
        imageView.setX(x);
        imageView.setY(y);
        imageView.setRotate(rotation);
    }

    /**
     * Get the width of the sprite.
     *
     * @return The width of the sprite.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Get the height of the sprite.
     *
     * @return The height of the sprite.
     */
    public double getHeight() {
        return height;
    }
}
