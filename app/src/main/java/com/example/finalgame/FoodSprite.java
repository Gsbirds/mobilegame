package com.example.finalgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class FoodSprite {
    private CharacterSprite characterSprite;
    private int xVelocity = 10;
    private int yVelocity = 5;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private Bitmap image;
    private int x,y;
    public FoodSprite(Bitmap bmp) {
        image = bmp;
        x=100;
        y=100;

    }
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
        if(x!= characterSprite.x && y!=characterSprite.y){
            canvas.drawBitmap(image=null, x, y, null);
        }
    }
}
