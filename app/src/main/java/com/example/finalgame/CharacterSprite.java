package com.example.finalgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CharacterSprite {
    private int xVelocity = 10;
    private int yVelocity = 5;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Bitmap image;
    int x;
    int y;
    public CharacterSprite(Bitmap bmp) {
        image = bmp;
        x=300;
        y=900;

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    public void move(float touchX, float touchY) {
        // Update the character's position based on touch input
        x = (int) touchX-535;
        y = (int) touchY-200;
    }
    public int getX() {
        return x;
    }

    // Add a method to get the y-coordinate if needed
    public int getY() {
        return y;
    }

}