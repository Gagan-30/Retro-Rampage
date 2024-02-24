package com.base.game.retrorampage.GameAssets;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;

public class AnimatedTexture extends Texture {
    private Image[] frames; // Array of images for animation frames
    private int currentFrame; // Index of the current frame to display
    private long frameDuration; // Duration of each frame in nanoseconds
    private long lastFrameChangeTime = 0; // When the last frame change occurred

    public AnimatedTexture(String[] imagePaths, long frameDuration) {
        super(imagePaths[0]); // Initialize with the first frame
        this.frames = new Image[imagePaths.length];
        for (int i = 0; i < imagePaths.length; i++) {
            frames[i] = new Image(imagePaths[i]);
        }
        this.frameDuration = frameDuration;
        currentFrame = 0;
        animationTimer.start(); // Start the animation
    }

    // AnimationTimer to update the frame based on the frameDuration
    private AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if ((now - lastFrameChangeTime) >= frameDuration) {
                currentFrame = (currentFrame + 1) % frames.length;
                lastFrameChangeTime = now;
                image = frames[currentFrame]; // Update the current image
            }
        }
    };

    // Call this method to start the animation explicitly if needed
    public void startAnimation() {
        animationTimer.start();
    }

    // Method to stop the animation
    public void stopAnimation() {
        animationTimer.stop();
    }
}