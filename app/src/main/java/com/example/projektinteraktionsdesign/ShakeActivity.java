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
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class ShakeActivity implements SensorEventListener {
    private final SensorManager sensorManager;
    private Sensor sensor;
    private float lastX;
    private float lastY;
    private float lastZ;
    private boolean notFirstTime;
    final float shakeThreshold = 16f;
    private final Vibrator vibrator;
    private Listener listener;

    public interface Listener {
        void onTranslation();
    }
    public void setListener(Listener l){
        listener = l;
    }

    ShakeActivity(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);   // permission to use the sensor
        //SensorEventListener sensorEventListener = new SensorEventListener() {
            if (sensorManager != null) {
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);   // "gets" a sensor
            }
    }

    /*@SuppressLint("CutPasteId")
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


    }*/

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float accX = event.values[0];
        float accY = event.values[1];
        float accZ = event.values[2];

        if (notFirstTime) {
            float xDiff = Math.abs(lastX - accX);
            float yDiff = Math.abs(lastY - accY);
            float zDiff = Math.abs(lastZ - accZ);

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

    protected void onResume() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause(){
        sensorManager.unregisterListener(this);
    }
}