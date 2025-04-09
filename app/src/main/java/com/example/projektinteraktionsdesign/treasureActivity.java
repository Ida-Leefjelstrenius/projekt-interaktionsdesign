package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class treasureActivity extends AppCompatActivity implements ShakeActivity.Listener{
    private ShakeActivity shakeActivity;
    private SensorEvent event;
    private ImageView closedChest, diver, openChest, heart;
    private Button goBackBottom;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        shakeActivity = new ShakeActivity(this, this);
        closedChest = (ImageView) findViewById(R.id.imageClosedChest);
        openChest = (ImageView) findViewById(R.id.imageOpenChest);
        diver = (ImageView) findViewById(R.id.imageDiver);
        heart = (ImageView) findViewById(R.id.imageHeart);

        floatingAnimation();
    }
    public void integrateWithShark(){
        shakeActivity.onSensorChanged(event);
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
        Animation movingHeart = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, -140);
        movingHeart.setDuration(2000);
        movingHeart.setRepeatCount(Animation.ABSOLUTE);
        movingHeart.setFillAfter(true);

        closedChest.setVisibility(View.INVISIBLE);
        openChest.setVisibility(View.VISIBLE);
        heart.startAnimation(movingHeart);
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
        goBackBottom = findViewById(R.id.btn_back_to_game);
        goBackBottom.setVisibility(View.VISIBLE);
        goBackBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToGame();
            }
        });
    }

    public void goBackToGame() {
        //getOnBackPressedDispatcher().onBackPressed();
        Intent gameIntent = new Intent(this, GameActivity.class);
        startActivity(gameIntent);
        finish();
    }
}
