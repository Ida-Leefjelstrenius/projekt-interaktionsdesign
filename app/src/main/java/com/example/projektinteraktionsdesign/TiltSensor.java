package com.example.projektinteraktionsdesign;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ImageView;

public class TiltSensor implements SensorEventListener {
    private final GameView gameView;
    private final ImageView player;

    public TiltSensor(Context context, GameView gameView, ImageView player) {
        this.gameView = gameView;
        this.player = player;
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float accelX = -event.values[0];
        float accelY = event.values[1];
        gameView.applyTilt(accelX, accelY, player); //Flytta spelaren och bakgrunden med tilt sensorn
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
