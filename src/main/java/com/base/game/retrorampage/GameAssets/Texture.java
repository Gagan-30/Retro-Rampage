package com.base.game.retrorampage.GameAssets;

import javafx.scene.image.Image;

public class Texture {

    protected Image image;

    public Texture(String imagePath) {
        this.image = new Image(imagePath);
    }

    public Image getImage() {
        return image;
    }
}

