package com.base.game.retrorampage.GameAssets;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

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
    protected ImageView imageView;
    protected double x;
    protected double y;
    private Rectangle square;
    private ImageView playerImageView;

    public Sprite() {
        position = new Vector(); // Initialize the position vector
        angle = 0;
        mirrored = false;
        flipped = false;
        opacity = 1;
        texture = new Texture();
        boundary = new Rect();
        visible = true;
        physics = null;
        animation = null;
        actionList = new ArrayList<>();
    }

    public Sprite(Image image, double x, double y) {
        this.imageView = new ImageView(image);
        this.x = x;
        this.y = y;

        this.imageView.setX(x);
        this.imageView.setY(y);

        // Initialize the boundary
        this.boundary = new Rect();
        this.boundary.setX(x);
        this.boundary.setY(y);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
        imageView.setX(x);
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        imageView.setY(y);
    }

    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    /**
     * Set the coordinates of the center of this sprite.
     *
     * @param x x-coordinate of center of sprite
     * @param y y-coordinate of center of sprite
     */
    public void setPosition(double x, double y) {
        position.setValues(x, y);
        boundary.setPosition(x, y);
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

    public void alignToSprite(Sprite other) {
        this.setPosition(other.position.x, other.position.y);
        this.setAngle(other.angle);
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
    public void setSize(double width, double height) {
        // Set the size for the boundary
        this.boundary.setWidth(width);
        this.boundary.setHeight(height);

        // Set the size for the image view
        this.imageView.setFitWidth(width);
        this.imageView.setFitHeight(height);
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
            Vector mtv = this.getBoundary().getMinimumTranslationVector(other.getBoundary());
            this.position.addVector(mtv);
        }
    }

    public void boundToScreen(int screenWidth, int screenHeight) {
        if (position.x - width / 2 < 0) position.x = width / 2;
        if (position.y - height / 2 < 0) position.y = height / 2;
        if (position.x + width / 2 > screenWidth) position.x = screenWidth - width / 2;
        if (position.y + height / 2 > screenHeight) position.y = screenHeight - height / 2;
    }

    public void wrapToScreen(int screenWidth, int screenHeight) {
        if (position.x + width / 2 < 0) position.x = screenWidth + width / 2;
        if (position.x - width / 2 > screenWidth) position.x = -width / 2;
        if (position.y + height / 2 < 0) position.y = screenHeight + height / 2;
        if (position.y - height / 2 > screenHeight) position.y = -height / 2;
    }

    public void update(double dt) {
        if (physics != null) {
            physics.position.setValues(this.position.x, this.position.y);
            physics.update(dt);
            this.position.setValues(physics.position.x, physics.position.y);
        }

        if (animation != null) {
            animation.update(dt);

            texture = animation.getCurrentTexture();
        }

        ArrayList<Action> actionListCopy = new ArrayList<Action>(actionList);
        for (Action a : actionListCopy) {
            boolean finished = a.apply(this, dt);
            if (finished) actionList.remove(a);
        }
        boundToScreen(1920, 1080);

    }

    public Vector getPosition() {
        return position;
    }

}

