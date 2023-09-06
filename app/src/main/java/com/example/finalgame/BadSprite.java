package com.example.finalgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class BadSprite {
    private int padding = 10;
    private GameView gameView;
    private int imageWidth;
    private Bitmap image;
    private int x, y;
    private double distanceThreshold = 100.0;
    private boolean isCollected = false;

    private boolean isVisible = true; // Track if the image is visible or not

    public BadSprite(Bitmap bmp, int x, int y) {
        image = bmp;
        this.x = x;
        this.y = y;
        this.gameView = gameView;
        this.imageWidth = bmp.getWidth();
        this.isVisible = true;
    }
    public int getWidth() {
        return image.getWidth();
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getY1() {
        return y;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getX1() {
        return x;
    }
    public void draw(Canvas canvas) {
        if (isVisible && image != null) {
            canvas.drawBitmap(image, x, y, null);
        }

    }
    public boolean checkAndUpdateImage(CharacterSprite characterSprite) {
        if (!isCollected) {
            double distanceX = Math.abs(x - characterSprite.getX());
            double distanceY = Math.abs(y - characterSprite.getY());

            if (distanceX < (distanceThreshold) && distanceY < distanceThreshold) {
                isVisible = false; // Set the flag to hide the image
                isCollected = true;
                return true; // Indicate that the food sprite should disappear
            }
        }
        return false; // Indicate that the food sprite should not disappear
    }


    public boolean isVisible() {
        return isVisible;
    }

}
