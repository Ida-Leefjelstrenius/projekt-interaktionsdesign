package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.SensorEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TreasureActivity extends AppCompatActivity{
    private ShakeActivity shakeActivity;
    private ImageView closedChest, diver, openChest, coin;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        closedChest = findViewById(R.id.imageClosedChest);
        openChest = findViewById(R.id.imageOpenChest);
        diver = findViewById(R.id.imageDiver);
        coin = findViewById(R.id.imageCoin);

        floatingAnimation();

        shakeActivity = new ShakeActivity(this, new ShakeActivity.SensorHandler() {
            private float lastX, lastY, lastZ;
            private boolean notFirstTime = false;
            private final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

            @Override
            public void handleSensorEvent(SensorEvent event) {
                float accX = event.values[0];
                float accY = event.values[1];
                float accZ = event.values[2];

                if (notFirstTime) {
                    float xDiff = Math.abs(lastX - accX);
                    float yDiff = Math.abs(lastY - accY);
                    float zDiff = Math.abs(lastZ - accZ);

                    float shakeThreshold = 16f;
                    if ((xDiff > shakeThreshold && yDiff > shakeThreshold)
                            || (xDiff > shakeThreshold && zDiff > shakeThreshold)
                            || (yDiff > shakeThreshold && zDiff > shakeThreshold)) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrator.vibrate(500);
                        }


                        onShakeDetected();
                    }
                }

                lastX = accX;
                lastY = accY;
                lastZ = accZ;
                notFirstTime = true;
            }
        });
    }

    private void onShakeDetected() {
        openChestAnimation();
        Button goBackBottom = findViewById(R.id.btn_back_to_game);
        goBackBottom.setVisibility(View.VISIBLE);
        goBackBottom.setOnClickListener(this::goBackToTest);
    }

    public void floatingAnimation() {
        Animation movingDiver = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 10, Animation.ABSOLUTE, -10);
        movingDiver.setDuration(3000);
        movingDiver.setRepeatCount(Animation.INFINITE);
        movingDiver.setRepeatMode(Animation.RESTART);
        movingDiver.setFillAfter(true);
        movingDiver.setInterpolator(new CycleInterpolator(1));

        diver.startAnimation(movingDiver);
    }

    public void openChestAnimation() {
        Animation movingCoin = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -140);
        movingCoin.setDuration(2000);
        movingCoin.setRepeatCount(Animation.ABSOLUTE);
        movingCoin.setFillAfter(true);

        closedChest.setVisibility(View.INVISIBLE);
        openChest.setVisibility(View.VISIBLE);
        coin.startAnimation(movingCoin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shakeActivity.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeActivity.unregister();
    }
    /*
    @Override
    public void onTranslation() { // Här ska vi sätta det som händer vid skakning
        openChestAnimation();
        Button goBackBottom = findViewById(R.id.btn_back_to_game);
        goBackBottom.setVisibility(View.VISIBLE);
        goBackBottom.setOnClickListener(this::goBackToTest);
    }

     */

    public void goBackToTest(View view) {
        //getOnBackPressedDispatcher().onBackPressed();
        Intent testIntent = new Intent(this, AbTestActivity.class);
        startActivity(testIntent);
        finish();
    }
}
