package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TreasureActivity3 extends AppCompatActivity {

    private ImageView closedChest, diver, openChest, coin;
    private SensorManager sensorManager;
    SensorEventListener sensorEventListener;
    private float lastX, lastY, lastZ;
    private boolean notFirstTime = false;
    private Vibrator vibrator;
    MediaPlayer mediaPlayerCoin;

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

        mediaPlayerCoin = MediaPlayer.create(TreasureActivity3.this, R.raw.coin);

        floatingAnimation();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                if (notFirstTime) {
                    float xDiff = Math.abs(lastX - x);
                    float yDiff = Math.abs(lastY - y);
                    float zDiff = Math.abs(lastZ - z);

                    float threshold = 1.0f;
                    if (xDiff > threshold) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrator.vibrate(500);
                        }
                        mediaPlayerCoin.start();
                        gestureDetection();
                    }
                }
                lastX = x;
                lastY = y;
                lastZ = z;
                notFirstTime = true;
            }



            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        if (sensor != null) {
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            Toast.makeText(this, "Rotation sensor not available", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
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

    public void goBackToTest(View view) {
        //getOnBackPressedDispatcher().onBackPressed();
        Intent testIntent = new Intent(this, AbTestActivity.class);
        startActivity(testIntent);
        finish();
    }

    private void gestureDetection(){
        onPause();
        openChestAnimation();
        Button goBackBottom = findViewById(R.id.btn_back_to_game);
        goBackBottom.setVisibility(View.VISIBLE);
        goBackBottom.setOnClickListener(this::goBackToTest);
    }
}
