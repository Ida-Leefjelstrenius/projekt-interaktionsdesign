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
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class GameView extends View {
    private final int screenWidth;
    private final int screenHeight;
    private final int newWidth;
    private float backgroundX = 0;
    private ImageView player;
    private Bitmap backgroundBitmap;
    private final Bitmap sharkBitmap, mineBitmap, chestBitmap;
    private final Matrix sharkMatrix = new Matrix();
    private float velocityX = 0, velocityY = 0;
    private float sharkX = -1000.0f, sharkY = 0f;
    private boolean isGameOver = false;
    private boolean isPaused = false;
    private final Vibrator vibrator;
    private long startTime = System.currentTimeMillis();
    private long savedTime;
    private long animationTime = 0;
    private float worldX = 0;
    private final MediaPlayer mediaPlayerMine, mediaPlayerShark;
    private final List<Mine> mines = new ArrayList<>();
    private final List<Chest> chests = new ArrayList<>();
    private final HashSet<Integer> zones = new HashSet<>(Arrays.asList(-2, -1, 0, 1, 2));
    private final Random random = new Random();
    private float mineDifficulty, sharkDifficulty, chestDifficulty;
    public interface TreasureRequestListener {
        void onChestCollected(Chest chest);
    }
    private TreasureRequestListener  treasureListener;
    public void setTreasureListener(TreasureRequestListener  listener) {
        this.treasureListener = listener;
    }

    public GameView(Context context) {
        super(context);

        checkSettings(context);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        mediaPlayerMine = MediaPlayer.create(context, R.raw.mine_sound);
        mediaPlayerShark = MediaPlayer.create(context, R.raw.shark_sound);
        vibrator = ContextCompat.getSystemService(context, Vibrator.class);

        Bitmap rawBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.combined_vatten);
        newWidth = screenHeight * (rawBackgroundBitmap.getWidth() / rawBackgroundBitmap.getHeight());
        backgroundBitmap = Bitmap.createScaledBitmap(rawBackgroundBitmap, newWidth, screenHeight, false);

        Bitmap rawSharkBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shark);
        sharkBitmap = Bitmap.createScaledBitmap(rawSharkBitmap, dpToPx(SHARK_WIDTH), dpToPx(SHARK_HEIGHT), false);

        Bitmap rawMineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mine);
        mineBitmap = Bitmap.createScaledBitmap(rawMineBitmap, dpToPx((int) (MINE_WIDTH * mineDifficulty)), dpToPx((int) (MINE_HEIGHT * mineDifficulty)), false);

        Bitmap rawChestBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.closed_chest);
        chestBitmap = Bitmap.createScaledBitmap(rawChestBitmap, dpToPx(CHEST_WIDTH), dpToPx(CHEST_HEIGHT), false);
    }

    public void setPlayer(ImageView player) {
        this.player = player;
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void setDarkMode(boolean isDark) {
        Bitmap rawBackgroundBitmap;
        if (isDark) {
            rawBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.combined_vatten_dark);
        }
        else {
            rawBackgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.combined_vatten);
        }
        backgroundBitmap = Bitmap.createScaledBitmap(rawBackgroundBitmap, newWidth, screenHeight, false);
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (isGameOver) return;

        int currentZone = (int) (worldX / ZONE_SIZE);
        if (!zones.contains(currentZone)) {
            zones.add(currentZone);
            maybeSpawnMine(currentZone);
            maybeSpawnChest(currentZone);
        }

        backgroundX += velocityX;
        sharkX += velocityX;
        worldX -= velocityX;

        for (Mine mine : mines) {
            mine.x += velocityX;
        }
        for (Chest chest : chests) {
            chest.x += velocityX;
        }

        velocityX *= FRICTION;
        velocityY *= FRICTION;

        if (backgroundX < -newWidth) backgroundX += newWidth;
        if (backgroundX > 0) backgroundX -= newWidth;

        canvas.drawBitmap(backgroundBitmap, backgroundX, 0, null);
        if (backgroundX < screenWidth - newWidth) {
            canvas.drawBitmap(backgroundBitmap, backgroundX + newWidth, 0, null);
        }

        sharkMatrix.postTranslate(sharkX, sharkY);

        canvas.drawBitmap(sharkBitmap, sharkMatrix, null);

        float chestY = screenHeight - CHEST_Y_OFFSET - (float) Math.sin(animationTime * BOBBING_FREQUENCY) * BOBBING_AMPLITUDE;

        for (Mine mine : mines) {
            canvas.drawBitmap(mineBitmap, mine.x, mine.y, null);
        }
        for (Chest chest : chests) {
            canvas.drawBitmap(chestBitmap, chest.x, chestY, null);
        }

        moveSharkTowardsPlayer();

        for (Mine mine : mines) {
            if (checkCollision(mine.x, mine.y, mineBitmap, player)) {
                if (!GamePrefs.isMuted(getContext())) {
                    mediaPlayerMine.start();
                }
                vibrator.vibrate(500);
                handleDeath();
                break;
            }
        }
        if (checkCollision(sharkX, sharkY, sharkBitmap, player)) {
            if (!GamePrefs.isMuted(getContext())) {
                mediaPlayerShark.start();
            }
            vibrator.vibrate(500);
            handleDeath();
        }
        for (Chest chest : chests) {
            if (checkCollision(chest.x, chestY, chestBitmap, player)) {
                try {
                    handleChestCollision(chest);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }

        if (!isPaused && !isGameOver) {
            postInvalidateOnAnimation();
        }
        animationTime++;
    }

    private void maybeSpawnMine(int zone) {
        if (random.nextFloat() < (MINE_SPAWN_CHANCE * mineDifficulty)) {
            float availableHeight = screenHeight - 2 * MINE_HEIGHT * mineDifficulty;
            float middleStart = MINE_HEIGHT * mineDifficulty + availableHeight * 0.1f;
            float middleHeight = availableHeight * 0.8f;
            float mineY = middleStart + random.nextFloat() * middleHeight;
            float mineX = zone * screenWidth / 2.5f;
            mines.add(new Mine(mineX, mineY));
        }
    }

    private void maybeSpawnChest(int zone) {
        if (random.nextFloat() < CHEST_SPAWN_CHANCE * chestDifficulty) {
            float chestX = zone * screenWidth / 3.5f;
            chests.add(new Chest(chestX));
        }
    }

    private void handleDeath() {
        if (isGameOver) return;
        isGameOver = true;

        Context context = getContext();
        GamePrefs.setGameOver(getContext(), true);
        Intent deathIntent = new Intent(context, DeathActivity.class);
        context.startActivity(deathIntent);
        ((GameActivity) context).finish();
    }

    private void handleChestCollision(Chest chest) throws InterruptedException {
        if (treasureListener != null) {
            chests.remove(chest);
            treasureListener.onChestCollected(chest);
        }
    }

    private void moveSharkTowardsPlayer() {
        float playerX = player.getX();
        float playerY = player.getY();

        int secondsSurvived = (int) ((savedTime + (System.currentTimeMillis() - startTime)) / 1000);

        float speed =  min(SHARK_BASE_SPEED * sharkDifficulty + secondsSurvived * SHARK_ACCELERATION * sharkDifficulty, SHARK_MAX_SPEED);

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
            sharkMatrix.postTranslate(sharkBitmap.getWidth(), 0);
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
    private void checkSettings(Context context) {
        GamePrefs.setGameOver(context, isGameOver);

        switch (GamePrefs.getDifficulty(context, MINE_DIFFICULTY)) {
            case EASY:
                mineDifficulty = MINE_EASY_MULTIPLIER;
                break;
            case HARD:
                mineDifficulty = MINE_HARD_MULTIPLIER;
                break;
            default:
                mineDifficulty = 1.0f;
                break;
        }

        switch (GamePrefs.getDifficulty(context, SHARK_DIFFICULTY)) {
            case EASY:
                sharkDifficulty = SHARK_EASY_MULTIPLIER;
                break;
            case HARD:
                sharkDifficulty = SHARK_HARD_MULTIPLIER;
                break;
            default:
                sharkDifficulty = 1.0f;
                break;
        }

        switch (GamePrefs.getDifficulty(context, CHEST_DIFFICULTY)) {
            case EASY:
                chestDifficulty = CHEST_EASY_MULTIPLIER;
                break;
            case HARD:
                chestDifficulty = CHEST_HARD_MULTIPLIER;
                break;
            default:
                chestDifficulty = 1.0f;
                break;
        }
    }
}