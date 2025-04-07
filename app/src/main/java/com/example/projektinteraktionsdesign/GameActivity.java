package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

public class GameActivity extends AppCompatActivity {

    GameView gameView;
    ImageView player;
    ImageView shark;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FrameLayout rootLayout = new FrameLayout(this);
        gameView = new GameView(this); //Skapa bakgrunden
        rootLayout.addView(gameView);

        createPlayer(rootLayout); //Skapa spelaren
        createShark(rootLayout); //Skapa hejen

        setContentView(rootLayout);

        rootLayout.post(this::testRepeatedInteractions);
        shark.setOnClickListener(v -> {
            Intent sharkGameIntent = new Intent(GameActivity.this, SharkActivity.class);
            startActivity(sharkGameIntent);
        });

        new TiltSensor(this, gameView, player); //Skapa tilt sensorn
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void createPlayer(FrameLayout rootLayout) {
        player = new ImageView(this);
        player.setImageResource(R.drawable.diver);
        FrameLayout.LayoutParams playerParams = new FrameLayout.LayoutParams(
                dpToPx(64), dpToPx(200)
        );
        playerParams.gravity = Gravity.CENTER;
        player.setLayoutParams(playerParams);
        rootLayout.addView(player);
    }

    private void createShark(FrameLayout rootLayout) {
        shark = new ImageView(this);
        shark.setImageResource(R.drawable.shark);
        FrameLayout.LayoutParams sharkParams = new FrameLayout.LayoutParams(
                dpToPx(64), dpToPx(200)
        );
        sharkParams.gravity = Gravity.CENTER;
        shark.setLayoutParams(sharkParams);
        shark.setVisibility(View.INVISIBLE);
        rootLayout.addView(shark);
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