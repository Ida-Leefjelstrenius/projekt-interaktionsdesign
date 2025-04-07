package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity implements SensorEventListener  {

    GameView gameView;
    ImageView player;
    ImageView shark;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FrameLayout rootLayout = new FrameLayout(this);
        gameView = new GameView(this); //Skapa bakgrunden
        rootLayout.addView(gameView);

        player = new ImageView(this); //Skapa spelaren
        player.setImageResource(R.drawable.diver);
        FrameLayout.LayoutParams playerParams = new FrameLayout.LayoutParams(
                dpToPx(64), dpToPx(200)
        );
        playerParams.gravity = Gravity.CENTER;
        player.setLayoutParams(playerParams);
        rootLayout.addView(player);

        shark = new ImageView(this);
        shark.setImageResource(R.drawable.shark);
        FrameLayout.LayoutParams sharkParams = new FrameLayout.LayoutParams(
                dpToPx(64), dpToPx(200)
        );
        sharkParams.gravity = Gravity.CENTER;
        shark.setLayoutParams(sharkParams);
        shark.setVisibility(View.INVISIBLE);
        rootLayout.addView(shark);

        setContentView(rootLayout);

        rootLayout.post(this::testRepeatedInteractions);
        shark.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sharkGameIntent = new Intent(GameActivity.this, SharkActivity.class);
                startActivity(sharkGameIntent);
            }
        });

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SensorManager SM = (SensorManager) getSystemService(SENSOR_SERVICE); //Skapa sensorn
        Sensor mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float accelX = -event.values[0];
        float accelY = event.values[1];
        gameView.applyTilt(accelX, accelY, player); //Flyta spelaren och bakgrunden med tilt sensorn

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public void testRepeatedInteractions(){ //every ten seconds for an hour
        Runnable test = () -> runOnUiThread(() -> {
            if (shark != null && shark.getParent() != null) {
                shark.setVisibility(View.VISIBLE);
            }
        });
        ScheduledFuture<?> timer =
                scheduler.scheduleWithFixedDelay(test, 10, 10, TimeUnit.SECONDS);
        Runnable canceller = () -> timer.cancel(false);
        scheduler.schedule(canceller, 1, TimeUnit.HOURS);
    }

}
