package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import static java.lang.Math.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.ImageView;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class TiltActivity extends AppCompatActivity implements SensorEventListener {
    private float accelX, accelY;
    private float velocityX = 0, velocityY = 0;
    private ImageView player;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tilt);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        float accelerationFactor = 1.4f;
        velocityX += accelX * accelerationFactor;
        velocityY += accelY * accelerationFactor;

        float friction = 0.6f;
        velocityX *= friction;
        velocityY *= friction;

        float newX = currentX + velocityX;
        float newY = currentY + velocityY;

        View mainView = findViewById(R.id.main);
        int width = mainView.getWidth();
        int height = mainView.getHeight();

        newX = max(0, min(newX, width - player.getWidth()));
        newY = max(0, min(newY, height - player.getHeight()));

        player.setX(newX);
        player.setY(newY);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
