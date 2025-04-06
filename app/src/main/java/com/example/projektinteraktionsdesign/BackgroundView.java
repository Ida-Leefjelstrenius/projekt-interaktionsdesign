package com.example.projektinteraktionsdesign;

import static java.lang.Math.max;
import static java.lang.Math.min;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class BackgroundView extends View {
    int screenWidth, screenHeight, newWidth, newHeight;
    float backgroundX = 0;
    Bitmap background;
    Handler handler;
    Runnable runnable;

    private float velocityX = 0, velocityY = 0;

    public BackgroundView(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.big_vatten);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        newWidth = (int) (screenHeight * (background.getWidth() / background.getHeight()));
        background = Bitmap.createScaledBitmap(background, newWidth, screenHeight, false);

        handler = new Handler();
        runnable = this::invalidate;
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

        postInvalidateOnAnimation();
    }

    public void applyTilt(float accelX, float accelY, ImageView player) {
        float accelerationFactor = 1.0f;
        velocityX += -accelX * accelerationFactor;
        velocityY += accelY * accelerationFactor;

        float currentY = player.getY();
        float playerHeight = player.getHeight();
        float newY = currentY + velocityY;
        newY = max(0, min(newY, screenHeight - playerHeight));
        player.setY(newY);
    }
}
