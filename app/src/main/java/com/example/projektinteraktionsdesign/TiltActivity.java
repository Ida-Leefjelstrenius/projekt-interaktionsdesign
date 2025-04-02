package com.example.projektinteraktionsdesign;

import android.hardware.SensorListener;
import android.os.Bundle;
import com.example.projektinteraktionsdesign.R;
import static java.lang.Math.*;
import android.content.Intent;
import android.media.Image;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageView;
import android.os.Vibrator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.lang.Math;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class TiltActivity extends AppCompatActivity implements SensorEventListener {
    private float accelX, accelY;
    private float velocityX = 0, velocityY = 0;
    private float friction = 0.6f;
    private float accelerationFactor = 1.4f;
    private ImageView player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tilt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SensorManager SM = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_GAME);
        player = findViewById(R.id.player);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        accelX = -event.values[0];
        accelY = event.values[1];
        movePlayer();
    }

    private void movePlayer(){
        float currentX = player.getX();
        float currentY = player.getY();

        velocityX += accelX * accelerationFactor;
        velocityY += accelY * accelerationFactor;

        velocityX *= friction;
        velocityY *= friction;

        float newX = currentX + velocityX;
        float newY = currentY + velocityY;

        int width = findViewById(R.id.main).getWidth();
        int height = findViewById(R.id.main).getHeight();

        newX = max(0, min(newX, width - player.getWidth()));
        newY = max(0, min(newY, height - player.getHeight()));

        player.setX(newX);
        player.setY(newY);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
