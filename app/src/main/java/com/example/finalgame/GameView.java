package com.example.finalgame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private com.example.finalgame.MainThread thread;
    private CharacterSprite characterSprite;
    private FoodSprite foodSprite;
    private FoodSprite foodSprite1;

    public GameView(Context context) {

        super(context);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new com.example.finalgame.MainThread(getHolder(), this); // Initialize the thread
        thread.setRunning(true); // Set running to true before starting
        thread.start(); // Start the thread
        setFocusable(true);
        foodSprite = new FoodSprite(BitmapFactory.decodeResource(getResources(), R.drawable.eggsmol2), 100, 100);
        foodSprite1 = new FoodSprite(BitmapFactory.decodeResource(getResources(), R.drawable.eggsmol2), 300, 1400);

        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.shih1
        ));
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Implement your logic for when the surface is changed
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }
    public class MainThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private GameView gameView;

        public MainThread(SurfaceHolder surfaceHolder, GameView gameView) {

            super();
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;

        }
    }
    public void update() {
        foodSprite.checkAndUpdateImage(characterSprite);
        foodSprite1.checkAndUpdateImage(characterSprite);

    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            characterSprite.draw(canvas);
            foodSprite.draw(canvas);
            foodSprite1.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Handle touch down event (e.g., record initial touch position)
                break;

            case MotionEvent.ACTION_MOVE:
                // Handle touch move event (e.g., update character's position based on touch)
                float x = event.getX();
                float y = event.getY();
                // Update the character's position based on touch input
                characterSprite.move(x, y);
                break;

            case MotionEvent.ACTION_UP:
                // Handle touch up event (if needed)
                break;
        }
        return true;
    }


}
