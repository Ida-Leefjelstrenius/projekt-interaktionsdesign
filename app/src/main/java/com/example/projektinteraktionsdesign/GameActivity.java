package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    GameView gameView;
    ImageView player;

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

        gameView.setPlayer(player);
        setContentView(rootLayout);

        //shark.setOnClickListener(v -> {
        //    Intent sharkGameIntent = new Intent(GameActivity.this, SharkActivity.class);
        //    startActivity(sharkGameIntent);
        //});

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
}