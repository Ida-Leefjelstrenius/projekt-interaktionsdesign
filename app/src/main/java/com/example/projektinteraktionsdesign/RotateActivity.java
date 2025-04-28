package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class RotateActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mRotationSensor;
    private ImageView closedChest, diver, openChest, coin;

    private static final int SENSOR_DELAY = 500 * 1000;
    private static final int FROM_RADS_TO_DEGS = -57;
    private boolean chestOpened = false;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        closedChest = findViewById(R.id.imageClosedChest);
        openChest = findViewById(R.id.imageOpenChest);
        diver = findViewById(R.id.imageDiver);
        coin = findViewById(R.id.imageCoin);

        floatingAnimation();

        try {
            mSensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
            mRotationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            mSensorManager.registerListener(this, mRotationSensor, SENSOR_DELAY);
        } catch (Exception e) {
            Toast.makeText(this, "Hardware compatibility issue", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == mRotationSensor) {
            if (event.values.length > 4) {
                float[] truncatedRotationVector = new float[4];
                System.arraycopy(event.values, 0, truncatedRotationVector, 0, 4);
                update(truncatedRotationVector);
            } else {
                update(event.values);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void update(float[] vectors) {
        float[] rotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, vectors);
        int worldAxisX = SensorManager.AXIS_X;
        int worldAxisZ = SensorManager.AXIS_Z;
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(rotationMatrix, worldAxisX, worldAxisZ, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);

        float pitch = orientation[1] * FROM_RADS_TO_DEGS;
        float roll = orientation[2] * FROM_RADS_TO_DEGS;

        Log.d("SensorDebug", "Roll: " + roll);

        if (Math.abs(roll) > 80 && !chestOpened) {
            onRotateDetected();
        }
    }

    private void onRotateDetected() {
        chestOpened = true;
        openChestAnimation();
        Button goBackBottom = findViewById(R.id.btn_back_to_game2);
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
        coin.setVisibility(View.VISIBLE);
        coin.startAnimation(movingCoin);
    }

    public void goBackToTest(View view) {
        //getOnBackPressedDispatcher().onBackPressed();
        Intent testIntent = new Intent(this, AbTestActivity.class);
        startActivity(testIntent);
        finish();
    }
}