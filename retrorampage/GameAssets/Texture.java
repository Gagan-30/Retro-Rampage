package com.base.game.retrorampage.GameAssets;

import javafx.scene.image.Image;

/**
 * Image data used for drawing a BAGEL.Sprite.
 * BAGEL.Texture objects are typically created using the {@link #load(String)} method.
 * Multiple instances of a BAGEL.Sprite may share a single BAGEL.Texture reference.
 *
 */
public class Texture
{
    /**
     *  The image to be drawn.
     */
    public Image image;

    /**
     *  A rectangular sub-area of the image to be drawn.
     */
    public Rect region;

    /**
     *  Create an empty texture.
     */
    public Texture()
    {

    }

    public Texture(String imageFileName)
    {
        image = new Image(imageFileName);
        System.out.println("Player image path: " + imageFileName);
        region = new Rect();
        double width = image.getWidth();
        double height = image.getHeight();
        region.setValues(0,0, width,height);
        System.out.println("Player image dimensions: " + region.width + "x" + region.height);
    }

        /**
         * Create a BAGEL.Texture from the image file with the given file name.
         * Sets {@link #region} to the original image dimensions.
         * @param imageFileName name of the image file
         * @return A BAGEL.Texture object that displays the image file with the given file name.
         */
        public static Texture load(String imageFileName)
        {
            Texture tex = new Texture();
            // String fileName = new File(imageFileName).toURI().toString();
            tex.image = new Image( imageFileName );
            tex.region = new Rect();
            double width = tex.image.getWidth();
            double height = tex.image.getHeight();
            tex.region.setValues(0,0, width,height);
            return tex;
    }
}
