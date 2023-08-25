package com.example.finalgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class FoodSprite {
    private Bitmap image;
    private int x, y;
    private double distanceThreshold = 200.0;
    private boolean isVisible = true; // Track if the image is visible or not

    public FoodSprite(Bitmap bmp, int x, int y) {
        image = bmp;
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas) {
        if (isVisible && image != null) {
            canvas.drawBitmap(image, x, y, null);
        }

    }
    public void checkAndUpdateImage(CharacterSprite characterSprite) {
        double distanceX = Math.abs(x - characterSprite.getX());
        double distanceY = Math.abs(y - characterSprite.getY());

        if (distanceX < distanceThreshold && distanceY < distanceThreshold) {
            isVisible = false; // Set the flag to hide the image
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

}
