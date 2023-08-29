package com.example.finalgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class FoodSprite {
    private GameView gameView;
    private int imageWidth;
    private Bitmap image;
    private int x, y;
    private double distanceThreshold = 200.0;
    private boolean isVisible = true; // Track if the image is visible or not

    public FoodSprite(Bitmap bmp, int x, int y) {
        image = bmp;
        this.x = x;
        this.y = y;
        this.gameView = gameView;
        this.imageWidth = bmp.getWidth();
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
    public void draw(Canvas canvas) {
        if (isVisible && image != null) {
            canvas.drawBitmap(image, x, y, null);
        }

    }
    public boolean checkAndUpdateImage(CharacterSprite characterSprite) {
        double distanceX = Math.abs(x - characterSprite.getX());
        double distanceY = Math.abs(y - characterSprite.getY());

        if (distanceX < (distanceThreshold + image.getWidth() / 2) && distanceY < distanceThreshold){
            isVisible = false; // Set the flag to hide the image
            return true; // Indicate that the food sprite should disappear
        }

        return false; // Indicate that the food sprite should not disappear
    }


    public boolean isVisible() {
        return isVisible;
    }

}
