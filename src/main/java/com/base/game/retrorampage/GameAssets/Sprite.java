package com.base.game.retrorampage.GameAssets;

import javafx.scene.canvas.GraphicsContext;

public abstract class Sprite {
    protected float x, y; // Position
    protected int width, height; // Dimensions
    protected Texture texture; // For graphical representation

    public Sprite(float x, float y, int width, int height, Texture texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    // Method to update the sprite's state. To be overridden by subclasses.
    public abstract void update();

    // Method to render the sprite on the screen. Implementation may vary based on the graphics library.
    public void render(GraphicsContext gc) {
        gc.drawImage(texture.getImage(), x, y, width, height);
    }

    // Getters and setters
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
