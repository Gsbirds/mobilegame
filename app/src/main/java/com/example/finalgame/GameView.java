package com.example.finalgame;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private com.example.finalgame.MainThread thread;
    private boolean isPlaying = false;

    private Random random = new Random();
    private CharacterSprite characterSprite;
    private FoodSprite foodSprite;
    private int score = 0;
    private ArrayList<FoodSprite> foodSprites = new ArrayList<>();

    private int foodSpriteSpeed = 10; // Adjust the speed as needed

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
        foodSprite = new FoodSprite(BitmapFactory.decodeResource(getResources(), R.drawable.eggsmol2), 10, 10);

        for (int i = 0; i < 25; i++) {
            int randomX = random.nextInt(getWidth()); // Adjust the range as needed
            int randomY = random.nextInt(getHeight()); // Adjust the range as needed

            // Create a new FoodSprite and add it to the list
            FoodSprite foodSprite = new FoodSprite(BitmapFactory.decodeResource(getResources(), R.drawable.eggsmol2), randomX, randomY);
            foodSprites.add(foodSprite);
        }
        characterSprite = new CharacterSprite(BitmapFactory.decodeResource(getResources(),R.drawable.l19));
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

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
        if (isPlaying) {
            for (FoodSprite foodSprite : foodSprites) {
                // Update the y-coordinate of each FoodSprite
                foodSprite.setY(foodSprite.getY1() + foodSpriteSpeed);

                // Check and update if it should disappear
                foodSprite.checkAndUpdateImage(characterSprite);

            }
            for (FoodSprite foodSprite : foodSprites) {
                if (foodSprite.checkAndUpdateImage(characterSprite)) {
                    // FoodSprite was collected, increase the score
                    score += 1; // Adjust the score increment as needed
                }
            }
        }
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {
            if (!isPlaying) {
                // Draw a "Play" button
                Paint buttonPaint = new Paint();
                buttonPaint.setColor(Color.rgb(255, 105, 180));
                buttonPaint.setStyle(Paint.Style.FILL);

                canvas.drawRect(200, 200, 400, 300, buttonPaint);

                Paint textPaint = new Paint();
                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(48);

                canvas.drawText("Play", 260, 260, textPaint);
            } else {
                // Draw game elements when playing
                characterSprite.draw(canvas);

                // Loop through the foodSprites list and draw each FoodSprite
                for (FoodSprite foodSprite : foodSprites) {
                    foodSprite.draw(canvas);
                }
                // Draw the score
                Paint paint = new Paint();
                paint.setColor(Color.rgb(255, 105, 180));
                paint.setTextSize(48);
                canvas.drawText("Score: " + score, 50, 50, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!isPlaying && event.getX() >= 200 && event.getX() <= 400
                        && event.getY() >= 200 && event.getY() <= 300) {
                    // The user tapped inside the button area
                    isPlaying = true;
                    score = 0; // Reset the score
//                    foodSprites.clear();
                } else if (isPlaying) {
                    // Handle touch events when the game is playing
                    float x = event.getX();
                    float y = event.getY();
                    // Update the character's position based on touch input
                    characterSprite.move(x, y = 1700);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                // Handle touch move event (e.g., update character's position based on touch)
                float x = event.getX();
                float y = event.getY();
                // Update the character's position based on touch input
                characterSprite.move(x, y=1700);
                break;

            case MotionEvent.ACTION_UP:
                // Handle touch up event (if needed)
                break;
        }
        return true;
    }

}
