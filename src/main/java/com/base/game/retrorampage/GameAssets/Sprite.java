package com.base.game.retrorampage.GameAssets;

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


    // Getters and setters
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public Texture getTexture() {
        return texture;
    }
}
