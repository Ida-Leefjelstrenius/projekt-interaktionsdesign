package com.example.projektinteraktionsdesign;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class ShakeActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private float accX, accY, accZ, lastX, lastY, lastZ;
    private float xDiff, yDiff, zDiff;
    private boolean notFirstTime;
    final float shakeThreshold = 16f;
    private Vibrator vibrator;



    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shake);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);   // permission to use the sensor
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);   // "gets" a sensor

    }


    @Override
    public final void onSensorChanged(SensorEvent event) {
        accX = event.values[0];
        accY = event.values[1];
        accZ = event.values[2];

        if (notFirstTime) {
            xDiff = Math.abs(lastX - accX);
            yDiff = Math.abs(lastY - accY);
            zDiff = Math.abs(lastZ - accZ);

            if (xDiff > shakeThreshold && yDiff > shakeThreshold
                    || xDiff > shakeThreshold && zDiff > shakeThreshold
                    || yDiff > shakeThreshold && zDiff > shakeThreshold) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                else {
                    vibrator.vibrate(500);
                }
            }

        }

        lastX = accX;
        lastY = accY;
        lastZ = accZ;
        notFirstTime = true;

    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}