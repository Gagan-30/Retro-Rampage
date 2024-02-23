package com.base.game.retrorampage.GameAssets;

public class Camera {
    private float x, y;

    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(float targetX, float targetY) {
        //this.x = targetX - Main.WIDTH / 2;
        //this.y = targetY - Game.HEIGHT / 2;
    }

    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
}