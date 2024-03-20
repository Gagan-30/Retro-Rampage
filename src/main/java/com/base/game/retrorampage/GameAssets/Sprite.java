package com.base.game.retrorampage.GameAssets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Sprite {
    /**
     * sprite location in game world
     */
    public Vector position;

    /**
     * angle of rotation (in degrees) of the texture
     */
    public double angle;

    /**
     * determines whether texture is reversed along the x direction
     */
    public boolean mirrored;

    /**
     * determines whether texture is reversed along the y direction
     */
    public boolean flipped;

    /**
     * amount of transparency; value from 0.0 (fully transparent) to 1.0 (fully opaque)
     */
    public double opacity;

    /**
     * image displayed when rendering this sprite
     */
    public Texture texture;

    /**
     * shape used for collision
     */
    public Rect boundary;

    /**
     * width of sprite
     */
    public double width;

    /**
     * height of sprite
     */
    public double height;

    /**
     * determines if sprite will be visible
     */
    public boolean visible;

    public Physics physics;

    public Animation animation;

    public ArrayList<Action> actionList;
    public Pane root;
    ImageView imageView;
    private double x;
    private double y;
    private double rotation;

    public Sprite() {
        position = new Vector();
        angle = 0;
        mirrored = false;
        flipped = false;
        opacity = 1;
        texture = new Texture();
        boundary = new Rect();
        visible = true;
        physics = null;
        animation = null;
        actionList = new ArrayList<Action>();
    }


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
     * @param x x-coordinate of center of sprite
     * @param y y-coordinate of center of sprite
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

    public double getX() {
        return this.imageView.getLayoutX();
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.imageView.getLayoutY();
    }

    public void setY(double y) {
        this.y = y;
    }

    /**
     * Move this sprite by the specified amounts.
     *
     * @param dx amount to move sprite along x direction
     * @param dy amount to move sprite along y direction
     */
    public void moveBy(double dx, double dy) {
        position.addValues(dx, dy);
    }

    public void setAngle(double a) {
        angle = a;
    }

    /**
     * Rotate sprite by the specified angle.
     *
     * @param da the angle (in degrees) to rotate this sprite
     */
    public void rotateBy(double da) {
        angle += da;
    }

    // Inside the Bullet class
    void alignToSprite(Sprite sprite) {
        this.setX(sprite.getX());
        this.setY(sprite.getY());
    }

    /**
     * Move sprite by the specified distance at the specified angle.
     *
     * @param dist the distance to move this sprite
     * @param a    the angle (in degrees) along which to move this sprite
     */
    public void moveAtAngle(double dist, double a) {
        double A = Math.toRadians(a);
        double dx = dist * Math.cos(A);
        double dy = dist * Math.sin(A);
        moveBy(dx, dy);
    }

    /**
     * Move sprite forward by the specified distance at current angle.
     *
     * @param dist the distance to move this sprite
     */
    public void moveForward(double dist) {
        moveAtAngle(dist, angle);
    }

    /**
     * set the texture data used when drawing this sprite;
     * also sets width and height of sprite
     *
     * @param tex texture data
     */
    public void setTexture(Texture tex) {
        texture = tex;
        width = texture.region.width;
        height = texture.region.height;
        boundary.setSize(width, height);
    }

    /**
     * set the width and height of this sprite;
     * used for drawing texture and collision rectangle
     *
     * @param width  sprite width
     * @param height sprite height
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        boundary.setSize(width, height);
    }

    public void setPhysics(Physics phys) {
        physics = phys;
    }

    public void setAnimation(Animation anim) {
        animation = anim;
        width = anim.getCurrentTexture().region.width;
        height = anim.getCurrentTexture().region.height;
        boundary.setSize(width, height);
    }

    public void addAction(Action a) {
        actionList.add(a);
    }

    /**
     * Get boundary shape for this sprite, adjusted according to current position.
     * Angle of rotation has no effect on the boundary.
     *
     * @return boundary shape for this sprite
     */
    public Rect getBoundary() {
        boundary.setValues(position.x - width / 2, position.y - height / 2, width, height);
        return boundary;
    }

    /**
     * Check if this sprite is overlapping another sprite.
     *
     * @param other sprite to check for overlap with
     * @return true if this sprite overlaps other sprite
     */
    public boolean overlaps(Sprite other) {
        return this.getBoundary().overlaps(other.getBoundary());
    }

    /**
     * Prevent this sprite from overlapping another sprite
     * by adjusting the position of this sprite.
     *
     * @param other sprite to prevent overlap with
     */
    public void preventOverlap(Sprite other) {
        if (this.overlaps(other)) {
            Vector mtv = this.getBoundary()
                    .getMinimumTranslationVector(other.getBoundary());
            this.position.addVector(mtv);
        }
    }

    public void boundToScreen(int screenWidth, int screenHeight) {
        if (getX() - width / 2 < 0)
            setX(width / 2);
        if (getY() - height / 2 < 0)
            setY(height / 2);
        if (getX() + width / 2 > screenWidth)
            setX(screenWidth - width / 2);
        if (getY() + height / 2 > screenHeight)
            setY(screenHeight - height / 2);
    }

    public void wrapToScreen(int screenWidth, int screenHeight) {
        if (position.x + width / 2 < 0)
            position.x = screenWidth + width / 2;
        if (position.x - width / 2 > screenWidth)
            position.x = -width / 2;
        if (position.y + height / 2 < 0)
            position.y = screenHeight + height / 2;
        if (position.y - height / 2 > screenHeight)
            position.y = -height / 2;
    }

    public void update(double dt) {
        if (physics != null) {
            physics.position.setValues(
                    this.position.x, this.position.y);
            physics.update(dt);
            this.position.setValues(
                    physics.position.x, physics.position.y);
        }

        if (animation != null) {
            animation.update(dt);

            texture = animation.getCurrentTexture();
        }

        ArrayList<Action> actionListCopy = new ArrayList<Action>(actionList);
        for (Action a : actionListCopy) {
            boolean finished = a.apply(this, dt);
            if (finished)
                actionList.remove(a);
        }
    }

    public Vector getPosition() {
        return position;
    }

    public void setRoot(Pane root) {
        this.root = root;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double angle) {
        this.imageView.setRotate(angle);
    }

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

    public void removeFromPane() {
        if (root != null) {
            root.getChildren().remove(imageView);
        }
    }

    // Add this method to update the position of the ImageView
    protected void updateImageViewPosition() {
        imageView.setX(x);
        imageView.setY(y);
        imageView.setRotate(rotation);
    }

    public boolean isColliding(Bullet bullet) {
        return false;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }
}

