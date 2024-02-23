package com.base.game.retrorampage.GameAssets;

public class Protagonist extends Sprite {
    private float speed; // Movement speed

    public Protagonist(float x, float y, int width, int height, Texture texture, float speed) {
        super(x, y, width, height, texture);
        this.speed = speed;
    }

    @Override
    public void update() {
        // Implement movement logic here
        // Example: this.x += speed;
    }

    // Additional methods specific to the protagonist, like jump, attack, etc.
    public void jump() {
        // Jump logic
    }

    public void attack() {
        // Attack logic
    }
}
