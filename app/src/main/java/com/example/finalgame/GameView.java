package com.example.finalgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
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

    private int foodSpriteSpeed = 15; // Initial speed
    private long lastFoodSpriteTime = 0; // To keep track of the time of the last FoodSprite creation
    private long foodSpriteCreationInterval = 1000;
    private com.example.finalgame.MainThread thread;
    private int screenWidth =Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private boolean isPlaying = false;

    private Random random = new Random();
    private Background background;
    private CharacterSprite characterSprite;
    private FoodSprite foodSprite;
    private int score = 0;
    private ArrayList<FoodSprite> foodSprites = new ArrayList<>();

//    private int foodSpriteSpeed = 15; // Adjust the speed as needed

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
        background = new Background(BitmapFactory.decodeResource(getResources(),R.drawable.red_shroom4));
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
                long currentTime = System.currentTimeMillis();

                // Calculate the time elapsed since the last FoodSprite creation
                long elapsedTimeSinceLastFoodSprite = currentTime - lastFoodSpriteTime;

                for (int i = 0; i < foodSprites.size(); i++) {
                    FoodSprite foodSprite = foodSprites.get(i);

                    // Update the y-coordinate of each FoodSprite with speed
                    foodSprite.setY(foodSprite.getY1() + foodSpriteSpeed);

                    // Check and update if it should disappear
                    if (foodSprite.checkAndUpdateImage(characterSprite)) {
                        // FoodSprite was collected, increase the score
                        score += 1; // Adjust the score increment as needed
                    }
                }

                // Check if it's time to create a new FoodSprite based on elapsed time
                if (elapsedTimeSinceLastFoodSprite >= foodSpriteCreationInterval) {
                    int randomX = -500 + random.nextInt(851);
                    int randomY = -1000 + random.nextInt(1051);

                    // Create a new FoodSprite and update the last creation time
                    FoodSprite foodSprite = new FoodSprite(BitmapFactory.decodeResource(getResources(), R.drawable.eggsmol3), randomX, randomY);
                    foodSprites.add(foodSprite);
                    lastFoodSpriteTime = currentTime;

                    // Increment the speed for the next FoodSprite
                    foodSpriteSpeed += 1; // Increase speed by 1 for each FoodSprite
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
                background.draw(canvas);

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
