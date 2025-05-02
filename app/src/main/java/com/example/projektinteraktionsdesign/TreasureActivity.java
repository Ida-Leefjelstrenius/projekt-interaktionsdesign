package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TreasureActivity extends AppCompatActivity implements ShakeActivity.Listener{
    private ShakeActivity shakeActivity;
    private ImageView closedChest, diver, openChest, coin;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        shakeActivity = new ShakeActivity(this, this);
        closedChest = findViewById(R.id.imageClosedChest);
        openChest = findViewById(R.id.imageOpenChest);
        diver = findViewById(R.id.imageDiver);
        coin = findViewById(R.id.imageCoin);

        floatingAnimation();
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
        Animation movingCoin = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -190);
        movingCoin.setDuration(2000);
        movingCoin.setRepeatCount(Animation.ABSOLUTE);
        movingCoin.setFillAfter(true);

        closedChest.setVisibility(View.INVISIBLE);
        openChest.setVisibility(View.VISIBLE);
        coin.startAnimation(movingCoin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shakeActivity.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeActivity.unregister();
    }


    @Override
    public void onTranslation() { // Här ska vi sätta det som händer vid skakning
        openChestAnimation();
        Button goBackBottom = findViewById(R.id.btn_back_to_game);
        goBackBottom.setVisibility(View.VISIBLE);
        goBackBottom.setOnClickListener(this::goBackToGame);
    }


    public void goBackToGame(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
