package com.example.projektinteraktionsdesign;
import static com.example.projektinteraktionsdesign.GameConstants.*;
import static com.example.projektinteraktionsdesign.CollisionUtils.checkCollision;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class GameView extends View {
    private final int screenWidth;
    private final int screenHeight;
    private final int newWidth;
    private float backgroundX = 0;
    private Bitmap background;
    private ImageView player;
    private final Bitmap shark, chestRight, chestLeft, mineBitmap;
    private final Matrix sharkMatrix = new Matrix();
    private float velocityX = 0, velocityY = 0;
    private float sharkX = -1000.0f, sharkY = 0f;
    private float chestRightX, chestLeftX;
    private boolean isGameOver = false;
    private boolean isPaused = false;
    private int coins = 0;
    private long startTime = System.currentTimeMillis();
    private long savedTime;
    private long animationTime = 0;
    private final Paint paint = new Paint();
    private float worldX = 0;
    private final List<Mine> mines = new ArrayList<>();
    private final HashSet<Integer> mineZones = new HashSet<>();
    private final Random random = new Random();
    public interface CoinUpdateListener {
        void onCoinUpdated(int newAmount);
    }
    private CoinUpdateListener coinListener;
    public void setCoinUpdateListener(CoinUpdateListener listener) {
        this.coinListener = listener;
    }
    public interface TreasureRequestListener {
        void treasureRequest();
    }
    private TreasureRequestListener  treasureListener;
    public void setTreasureListener(TreasureRequestListener  listener) {
        this.treasureListener = listener;
    }

    public GameView(Context context) {
        super(context);
        GamePrefs.setGameOver(context, isGameOver);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.combined_vatten);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        newWidth = screenHeight * (background.getWidth() / background.getHeight());
        background = Bitmap.createScaledBitmap(background, newWidth, screenHeight, false);

        Bitmap sharkBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shark);
        int SharkWidth = dpToPx(SHARK_WIDTH);
        int SharkHeight = dpToPx(SHARK_HEIGHT);
        shark = Bitmap.createScaledBitmap(sharkBitmap, SharkWidth, SharkHeight, false);

        Bitmap rawMineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bomb);
        mineBitmap = Bitmap.createScaledBitmap(rawMineBitmap, MINE_SIZE, MINE_SIZE, false);
        mineZones.add(0);

        int chestWidth = dpToPx(CHEST_WIDTH);
        int chestHeight = dpToPx(CHEST_HEIGHT);

        chestRightX = respawnDistance();
        Bitmap chestRightBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.closed_chest);
        chestRight = Bitmap.createScaledBitmap(chestRightBitmap, chestWidth, chestHeight, false);

        chestLeftX = -respawnDistance();
        Bitmap chestLeftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.closed_chest);
        chestLeft = Bitmap.createScaledBitmap(chestLeftBitmap, chestWidth, chestHeight, false);
    }

    public void setPlayer(ImageView player) {
        this.player = player;
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (isGameOver) return;
        if (player == null) return;

        int currentZone = (int) (worldX / ZONE_SIZE);
        if (!mineZones.contains(currentZone)) {
            mineZones.add(currentZone);
            maybeSpawnMine(currentZone);
        }

        backgroundX += velocityX;
        chestRightX += velocityX;
        chestLeftX += velocityX;
        sharkX += velocityX;
        worldX -= velocityX;

        for (Mine mine : mines) {
            mine.x += velocityX;
        }

        velocityX *= FRICTION;
        velocityY *= FRICTION;

        if (backgroundX < -newWidth) backgroundX += newWidth;
        if (backgroundX > 0) backgroundX -= newWidth;

        canvas.drawBitmap(background, backgroundX, 0, null);
        if (backgroundX < screenWidth - newWidth) {
            canvas.drawBitmap(background, backgroundX + newWidth, 0, null);
        }

        sharkMatrix.postTranslate(sharkX, sharkY);

        canvas.drawBitmap(shark, sharkMatrix, null);

        float chestY = screenHeight - CHEST_Y_OFFSET - (float) Math.sin(animationTime * BOBBING_FREQUENCY) * BOBBING_AMPLITUDE;

        canvas.drawBitmap(chestRight, chestRightX, chestY, paint);
        canvas.drawBitmap(chestLeft, chestLeftX, chestY, paint);

        paint.setColor(Color.BLACK);
        for (Mine mine : mines) {
            canvas.drawBitmap(mineBitmap, mine.x, mine.y, paint);
        }

        moveSharkTowardsPlayer();

        boolean mineExplosion = false;
        for (Mine mine : mines) {
            if (checkCollision(mine.x, mine.y, mineBitmap, player)) {
                mineExplosion = true;
                break;
            }
        }
        if (mineExplosion || checkCollision(sharkX, sharkY, shark, player)) {
            handleDeath();
        }
        else if (checkCollision(chestRightX, chestY, chestRight, player) ||
                checkCollision(chestLeftX, chestY, chestLeft, player)) {
            try {
                handleChestCollision();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (!isPaused && !isGameOver) {
            postInvalidateOnAnimation();
        }
        animationTime++;
    }

    private void maybeSpawnMine(int zone) {
        if (random.nextFloat() < MINE_SPAWN_CHANCE) {
            float availableHeight = screenHeight - 2 * MINE_SIZE;
            float middleStart = MINE_SIZE + availableHeight * 0.1f;
            float middleHeight = availableHeight * 0.8f;
            float mineY = middleStart + random.nextFloat() * middleHeight;
            float mineX = (zone + 1) * MINE_SIZE;
            mines.add(new Mine(mineX, mineY));
        }
    }

    private void handleDeath() {
        if (isGameOver) return;
        isGameOver = true;



        Context context = getContext();
        if (context instanceof GameActivity) {
            GamePrefs.addCoins(context, coins);
            GamePrefs.setGameOver(getContext(), true);
            Intent deathIntent = new Intent(context, DeathActivity.class);
            context.startActivity(deathIntent);
            ((GameActivity) context).finish();
        }
    }

    private void handleChestCollision() throws InterruptedException {
        if (treasureListener != null) {
            Context context = getContext();
            double coinValue = (Math.random() * 5) + 1;
            coins += (int) coinValue;

            if (coinListener != null) {
                coinListener.onCoinUpdated(coins);
            }

            if (context instanceof GameActivity) {
                if (treasureListener != null) {
                    treasureListener.treasureRequest();
                    chestRightX = player.getX() + respawnDistance();
                    chestLeftX = player.getX() - respawnDistance();
                }
            }
        }
    }

    private float respawnDistance() {
        return (float)(Math.random() * CHEST_X_OFFSET + CHEST_X_OFFSET);
    }

    private void moveSharkTowardsPlayer() {
        float playerX = player.getX();
        float playerY = player.getY();

        int secondsSurvived = (int) ((savedTime + (System.currentTimeMillis() - startTime)) / 1000);

        float speed =  min(SHARK_BASE_SPEED + secondsSurvived * SHARK_ACCELERATION, SHARK_MAX_SPEED);

        float deltaX = playerX - sharkX;
        float deltaY = playerY - sharkY;

        float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        float dirX = deltaX / distance;
        float dirY = deltaY / distance;

        if (dirX > 0) {
            sharkMatrix.setScale(1, 1);
        }
        else {
            sharkMatrix.setScale(-1, 1);
            sharkMatrix.postTranslate(shark.getWidth(), 0);
        }

        sharkX += dirX * speed;
        sharkY += dirY * speed;
    }

    public void applyTilt(float accelX, float accelY, ImageView player) {
        if (isPaused) return;
        velocityX += -accelX * PLAYER_ACCELERATION_FACTOR;
        velocityY += accelY * PLAYER_ACCELERATION_FACTOR;

        //Flytta spelaren
        float currentY = player.getY();
        float playerHeight = player.getHeight();
        float newY = currentY + velocityY;
        newY = max(0, min(newY, screenHeight - playerHeight));
        player.setY(newY);

        if (velocityX > 0) {
            player.setScaleX(-1);
        }
        else {
            player.setScaleX(1);
        }
    }

    public void pause(){
        isPaused = true;
        savedTime = System.currentTimeMillis() - startTime;
    }

    public void resume() {
        isPaused = false;
        startTime = System.currentTimeMillis();
        postInvalidateOnAnimation();
    }
}
