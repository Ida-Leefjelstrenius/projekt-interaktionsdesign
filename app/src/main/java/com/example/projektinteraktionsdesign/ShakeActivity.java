package com.example.projektinteraktionsdesign;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;


public class ShakeActivity implements SensorEventListener {
    private final SensorManager sensorManager;
    private Sensor sensor;
    private float lastX;
    private float lastY;
    private float lastZ;
    private boolean notFirstTime;
    final float shakeThreshold = 16f;
    private final Vibrator vibrator;

    private final SensorHandler handler;

    public interface SensorHandler {
        void handleSensorEvent(SensorEvent event);
    }

    ShakeActivity(Context context, SensorHandler handler) {
        this.handler = handler;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (handler != null) {
            handler.handleSensorEvent(event);
        }
    }
    /*
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
                if (listener != null) {
                    listener.onTranslation();
                }
            }
        }

        lastX = accX;
        lastY = accY;
        lastZ = accZ;
        notFirstTime = true;

    }

     */

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    protected void register() {
        if (sensorManager != null && sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    protected void unregister(){
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }
}