package com.example.projektinteraktionsdesign;

import android.graphics.Color;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    GameView gameView;
    ImageView player, playPauseButton;
    TextView timer;

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
        timer = new TextView(this);
        timer.setTextSize(25);
        timer.setTextColor(Color.WHITE);
        timer.setPadding(20,20,20,20);
        gameView.setPlayer(player);
        rootLayout.addView(timer);
        setContentView(rootLayout);
        long startTime;
        Handler timerHandler = new Handler();
        Runnable timerRunnable;
        new TiltSensor(this, gameView, player); //Skapa tilt sensorn
        startTime = System.currentTimeMillis();

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - startTime;
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                timer.setText("Time alive: " + String.format("%d:%02d", minutes, seconds));
                timerHandler.postDelayed(this, 500);
            }
        };
        timerHandler.post(timerRunnable);

        playPauseButton = new ImageView(this);
        playPauseButton.setImageResource(R.drawable.pause);
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                dpToPx(50), dpToPx(50)
        );

        buttonParams.gravity = Gravity.TOP | Gravity.END;
        buttonParams.setMargins(20, 20, 20, 20);
        playPauseButton.setLayoutParams(buttonParams);

        rootLayout.addView(playPauseButton);
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
}