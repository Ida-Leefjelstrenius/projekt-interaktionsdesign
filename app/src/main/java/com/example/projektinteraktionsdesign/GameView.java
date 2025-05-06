package com.example.projektinteraktionsdesign;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;

public class GameView extends View {
    private final int screenWidth;
    private final int screenHeight;
    private final int newWidth;
    private float backgroundX = 0;
    private Bitmap background;
    private ImageView player;
    private final Bitmap shark, chestRight, chestLeft;
    private final Matrix sharkMatrix = new Matrix();
    private float velocityX = 0, velocityY = 0;
    private float sharkX = -1000.0f, sharkY = 0f;
    private float chestRightX, chestLeftX;
    private boolean isGameOver = false;
    private boolean isPaused = false;
    private  int coins = 0;
    private long startTime = System.currentTimeMillis();
    private long savedTime;
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
        background = BitmapFactory.decodeResource(getResources(), R.drawable.combined_vatten);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        newWidth = screenHeight * (background.getWidth() / background.getHeight());
        background = Bitmap.createScaledBitmap(background, newWidth, screenHeight, false);

        new Handler();
        invalidate();

        Bitmap sharkBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shark);
        int SharkWidth = dpToPx(160);
        int SharkHeight = dpToPx(100);
        shark = Bitmap.createScaledBitmap(sharkBitmap, SharkWidth, SharkHeight, false);

        int chestWidth = dpToPx(100);
        int chestHeight = dpToPx(80);

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

        backgroundX += velocityX;
        chestRightX += velocityX;
        chestLeftX += velocityX;
        sharkX += velocityX;

        float friction = 0.6f;
        velocityX *= friction;
        velocityY *= friction;

        if (backgroundX < -newWidth) backgroundX += newWidth;
        if (backgroundX > 0) backgroundX -= newWidth;

        canvas.drawBitmap(background, backgroundX, 0, null);
        if (backgroundX < screenWidth - newWidth) {
            canvas.drawBitmap(background, backgroundX + newWidth, 0, null);
        }

        sharkMatrix.postTranslate(sharkX, sharkY);

        canvas.drawBitmap(shark, sharkMatrix, null);

        float chestY = screenHeight - 300;
        canvas.drawBitmap(chestRight, chestRightX, chestY, null);
        canvas.drawBitmap(chestLeft, chestLeftX, chestY, null);

        moveSharkTowardsPlayer();

        if (checkCollision(sharkX, sharkY, shark)) {
            handleDeath();
        } else if (checkCollision(chestRightX, chestY, chestRight) || checkCollision(chestLeftX, chestY, chestLeft)) {
            try {
                handleChestCollision();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (!isPaused && !isGameOver) {
            postInvalidateOnAnimation();
        }
    }

    private void handleDeath() {
        if (isGameOver) return;
        isGameOver = true;

        Context context = getContext();
        if (context instanceof GameActivity) {
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
        return (float)(Math.random() * 2000 + 2000);
    }

    private boolean checkCollision(float objectX, float objectY, Bitmap object) {
        Rect playerRect = new Rect(
                (int) player.getX(),
                (int) player.getY() + 200,
                (int) player.getX() +  player.getWidth(),
                (int) player.getY() + 350);
        Rect objectRect = new Rect(
                (int) objectX,
                (int) objectY,
                (int) (objectX + object.getWidth()),
                (int) (objectY + object.getHeight()));

        return Rect.intersects(playerRect, objectRect);
    }

    private void moveSharkTowardsPlayer() {
        float playerX = player.getX();
        float playerY = player.getY();

        int secondsSurvived = (int) ((savedTime + (System.currentTimeMillis() - startTime)) / 1000);

        float baseSpeed = 2.0f;
        float speed =  min(baseSpeed + secondsSurvived * 0.1f, 10.0f);

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
        float accelerationFactor = 1.0f;
        velocityX += -accelX * accelerationFactor;
        velocityY += accelY * accelerationFactor;

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
