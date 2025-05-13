package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class TreasureActivity extends AppCompatActivity {
    private ImageView closedChest, diver, openChest, coin;
    private TextView coinDialog;
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;
    private Vibrator vibrator;
    MediaPlayer mediaPlayerCoin;
    private int coinValue;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure);

        closedChest = findViewById(R.id.imageClosedChest);
        openChest = findViewById(R.id.imageOpenChest);
        diver = findViewById(R.id.imageDiver);
        coin = findViewById(R.id.imageCoin);
        coinDialog = findViewById(R.id.coinDialog);
        coinDialog.setVisibility(View.INVISIBLE);

        mediaPlayerCoin = MediaPlayer.create(TreasureActivity.this, R.raw.coin_sound);

        floatingAnimation();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        vibrator = (Vibrator) getSystemService(Vibrator.class);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] rotationMatrix = new float[9];
                float[] orientationAngles = new float[3];

                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                SensorManager.getOrientation(rotationMatrix, orientationAngles);

                //float azimuth = orientationAngles[0]; //compass
                float pitch = orientationAngles[1];   // tilt up + down
                float roll = orientationAngles[2];    // left right

                if (Math.abs(roll) > 1.2 && Math.abs(pitch) < 0.5) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(500);
                    }
                    if (!GamePrefs.isMuted(TreasureActivity.this)) {
                        mediaPlayerCoin.start();
                    }
                    keyMovementDetected();
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        if (rotationSensor != null) {
            sensorManager.registerListener(sensorEventListener, rotationSensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            Toast.makeText(this, "Rotation sensor not available", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    private void keyMovementDetected() {
        onPause();
        openChestAnimation();

        coinValue = (int) (Math.random() * 5) + 1;
        coinDialog.setVisibility(View.VISIBLE);
        coinDialog.setText("You got " + coinValue + " coins!");

        Button goBackBottom = findViewById(R.id.btn_back_to_game);
        goBackBottom.setVisibility(View.VISIBLE);
        goBackBottom.setOnClickListener(this::goBackToGame);
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
        Animation movingCoin = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -190);
        movingCoin.setDuration(2000);
        movingCoin.setRepeatCount(Animation.ABSOLUTE);
        movingCoin.setFillAfter(true);

        closedChest.setVisibility(View.INVISIBLE);
        openChest.setVisibility(View.VISIBLE);
        coin.startAnimation(movingCoin);
    }

    public void goBackToGame(View view) {
        Intent intent = new Intent();
        intent.putExtra("result", coinValue);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showHint(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_hint, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);

        AlertDialog dialog = builder.create();

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }

}
