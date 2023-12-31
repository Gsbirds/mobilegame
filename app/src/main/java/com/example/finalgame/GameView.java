package com.example.finalgame;

import android.content.Context;
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
    private Bitmap redShroomBackground;
    private Bitmap blueMeanieBackground;
    private Bitmap pinkShroomBackground;
    private int foodSpriteSpeed = 15; // Initial speed
    private long lastBadSpriteTime = 0;
    private long lastFoodSpriteTime = 0; // To keep track of the time of the last FoodSprite creation
    private long foodSpriteCreationInterval = 1000;
    private long badSpriteCreationInterval = 3000;
    private com.example.finalgame.MainThread thread;
    private boolean isPlaying = false;
    private boolean restart = false;
    private boolean levelUp = false;
    private boolean levelUp2 = false;
    private long gameStartTime = 0;
    private Random random = new Random();
    private Background background;
    private CharacterSprite characterSprite;
    private FoodSprite foodSprite;
    private int score = 0;
    private ArrayList<FoodSprite> foodSprites = new ArrayList<>();
    private ArrayList<BadSprite> badsprites = new ArrayList<>();

//    private int foodSpriteSpeed = 15; // Adjust the speed as needed

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameStartTime = System.currentTimeMillis();
        thread = new com.example.finalgame.MainThread(getHolder(), this); // Initialize the thread
        thread.setRunning(true); // Set running to true before starting
        thread.start(); // Start the thread
        setFocusable(true);
        redShroomBackground = BitmapFactory.decodeResource(getResources(), R.drawable.red_shroom4);
        blueMeanieBackground = BitmapFactory.decodeResource(getResources(), R.drawable.blue_meanie);
        pinkShroomBackground = BitmapFactory.decodeResource(getResources(), R.drawable.pink_shroom3);
        background = new Background(redShroomBackground);

//        background = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.red_shroom4));
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
        long currentTime = System.currentTimeMillis();

        if (!isPlaying) {
            // If the game is not playing, there's nothing to update
            return;
        }
        // Calculate the time elapsed since the game started
        long elapsedTime = currentTime - gameStartTime;
        // Check if 10 seconds have passed
        if (elapsedTime >= 50000) {
            // Stop the game after 10 seconds
            if (score > 70) {
                levelUp2=true;
                levelUp=false;
            } else if (score>20 && score<70) {
                levelUp = true;
            }
            restart= true;
            return;
        }
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

            if (levelUp) {
                foodSpriteSpeed += 2; // Increase speed by 2 for each FoodSprite when levelUp is true
            } else if (levelUp2){
                foodSpriteSpeed+=2.5;
            } else {
                foodSpriteSpeed += 1; // Increase speed by 1 for each FoodSprite when levelUp is false
            }
        }

        long elapsedTimeSinceLastBadSprite = currentTime - lastBadSpriteTime;

        for (int i = 0; i < badsprites.size(); i++) {
        BadSprite badSprite = badsprites.get(i);

        // Update the y-coordinate of each FoodSprite with speed
        badSprite.setY(badSprite.getY1() + foodSpriteSpeed);

        // Check and update if it should disappear
        if (badSprite.checkAndUpdateImage(characterSprite)) {
            // FoodSprite was collected, increase the score
            score -= 1; // Adjust the score increment as needed
        }
    }

    // Check if it's time to create a new FoodSprite based on elapsed time
        if (elapsedTimeSinceLastBadSprite >= badSpriteCreationInterval) {
        int randomX = -500 + random.nextInt(851);
        int randomY = -1000 + random.nextInt(1051);

        // Create a new FoodSprite and update the last creation time
        BadSprite badSprite = new BadSprite(BitmapFactory.decodeResource(getResources(), R.drawable.badsprite4), randomX, randomY);
        badsprites.add(badSprite);
        lastBadSpriteTime = currentTime;

        if (levelUp) {
            foodSpriteSpeed += 2; // Increase speed by 2 for each FoodSprite when levelUp is true
        } else if (levelUp2){
            foodSpriteSpeed+=2.5;
        } else {
            foodSpriteSpeed += 1; // Increase speed by 1 for each FoodSprite when levelUp is false
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
            } else if (restart) {
                characterSprite.draw(canvas);
                Paint buttonPaint = new Paint();
                buttonPaint.setColor(Color.rgb(255, 105, 180));
                buttonPaint.setStyle(Paint.Style.FILL);

                canvas.drawRect(600, 40, 970, 150, buttonPaint);

                Paint textPaint = new Paint();
                textPaint.setColor(Color.WHITE);
                textPaint.setTextSize(48);

                canvas.drawText("Restart", 700, 108, textPaint);
//                background.draw(canvas);
                characterSprite.draw(canvas);
                Paint paint = new Paint();
                paint.setColor(Color.rgb(255, 105, 180));
                paint.setTextSize(48);
                canvas.drawText("Score: " + score, 50, 50, paint);

            } else if (isPlaying) {

                if (levelUp) {
                    // Set the background to blue_meanie if levelUp is true
                    canvas.drawBitmap(blueMeanieBackground, -300, -50, null);
                } else if (levelUp2){
                    canvas.drawBitmap(pinkShroomBackground, -1630, -3600, null);
                }
                else {
                    canvas.drawColor(Color.BLACK);
                    // Set the background to red_shroom4 if levelUp is false
                    canvas.drawBitmap(redShroomBackground, -800, -1200, null);
                }
                characterSprite.draw(canvas);
                for (BadSprite badSprite : badsprites) {
                    badSprite.draw(canvas);
                }
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
                } else if (restart && event.getX() >= 600 && event.getX() <= 970
                        && event.getY() >= 40 && event.getY() <= 150) {
                    // The user tapped inside the "Restart" button area
                    restart = false; // Reset the restart flag
                    isPlaying = true; // Start the game again
//                    score = 0; // Reset the score
                    gameStartTime = System.currentTimeMillis(); // Reset the game start time
                    foodSprites.clear(); // Clear the food sprites
                    badsprites.clear();
                    foodSpriteSpeed = 15;
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
