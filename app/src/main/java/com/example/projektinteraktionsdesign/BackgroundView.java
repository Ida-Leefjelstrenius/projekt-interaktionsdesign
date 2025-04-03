package com.example.projektinteraktionsdesign;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.View;

public class BackgroundView extends View {
    int screenWidth, screenHeight, newWidth, newHeight;
    int backgroundX = 0;
    Bitmap background;
    Handler handler;
    Runnable runnable;
    final long UPDATE_MILLIS=30;

    public BackgroundView(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.big_vatten);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        float height = background.getHeight();
        float width = background.getWidth();
        float ratio = width / height;
        newHeight = screenHeight;
        newWidth = (int) (ratio * screenHeight);
        background = Bitmap.createScaledBitmap(background, newWidth, newHeight, false);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        backgroundX -= 3;
        if (backgroundX < -newWidth) {
            backgroundX = 0;
        }
        canvas.drawBitmap(background, backgroundX, 0, null);
        if (backgroundX < screenWidth - newWidth) {
            canvas.drawBitmap(background, backgroundX+newWidth, 0, null);
        }
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }
}
