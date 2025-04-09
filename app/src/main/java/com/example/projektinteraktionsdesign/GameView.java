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
    private final Bitmap shark;
    private final Matrix sharkMatrix = new Matrix();
    private float velocityX = 0, velocityY = 0;
    private float sharkX = 0f, sharkY = 0f;

    public GameView(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.big_vatten);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        newWidth = screenHeight * (background.getWidth() / background.getHeight());
        background = Bitmap.createScaledBitmap(background, newWidth, screenHeight, false);

        new Handler();
        Runnable runnable = this::invalidate;

        Bitmap sharkBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shark);
        int SharkWidth = dpToPx(160);
        int SharkHeight = dpToPx(100);
        shark = Bitmap.createScaledBitmap(sharkBitmap, SharkWidth, SharkHeight, false);
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

        backgroundX += velocityX;

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
        moveSharkTowardsPlayer();

        if (checkCollision()) {
            handleCollision();
        }

        postInvalidateOnAnimation();
    }

    private void handleCollision() {
        Context context = getContext();

        if (context instanceof GameActivity) {
            Intent sharkGameIntent = new Intent(context, treasureActivity.class);
            context.startActivity(sharkGameIntent);
            sharkX = 0;
            sharkY = 0;
        }
    }

    private boolean checkCollision() {
        Rect playerRect = new Rect(
                (int) player.getX(),
                (int) player.getY() + 200,
                (int) player.getX() +  player.getWidth(),
                (int) player.getY() + 350);
        Rect sharkRect = new Rect(
                (int) sharkX,
                (int) sharkY,
                (int) (sharkX + shark.getWidth()),
                (int) (sharkY + shark.getHeight()));

        return Rect.intersects(playerRect, sharkRect);
    }

    private void moveSharkTowardsPlayer() {
        float playerX = player.getX();
        float playerY = player.getY();

        float speed = 5.0f;

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

        sharkX += velocityX;
    }
}
