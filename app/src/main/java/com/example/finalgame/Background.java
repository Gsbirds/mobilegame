package com.example.finalgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {
    private Bitmap backgroundImage;
    private int xVelocity = 10;
    private int yVelocity = 5;
    private Bitmap image;
    int x;
    int y;
    public Background(Bitmap bmp) {
        image = bmp;
//        x=-400;
//        y=-200;
        x=-800;
        y=-1080;
    }

    public void setBackgroundImage(Bitmap newBackground) {
        backgroundImage = newBackground;
    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public int getX() {
        return x;
    }

    // Add a method to get the y-coordinate if needed
    public int getY() {
        return y;
    }

}