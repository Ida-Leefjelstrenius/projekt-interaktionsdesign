package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class treasureActivity extends AppCompatActivity implements ShakeActivity.Listener{
    private ShakeActivity shakeActivity;
    private SensorEvent event;
    private ImageView closedChest, diver;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        shakeActivity = new ShakeActivity(this, this);
        closedChest = (ImageView) findViewById(R.id.imageClosedChest);
        diver = (ImageView) findViewById(R.id.imageDiver);

        moveImages();
    }
    public void integrateWithShark(){
        shakeActivity.onSensorChanged(event);
    }

    public void moveImages() {
        Animation movingDiver = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 10, Animation.ABSOLUTE, -10);
        movingDiver.setDuration(3000);
        movingDiver.setRepeatCount(Animation.INFINITE);
        movingDiver.setRepeatMode(Animation.RESTART);
        movingDiver.setFillAfter(true);
        movingDiver.setInterpolator(new CycleInterpolator(1));

        diver.startAnimation(movingDiver);
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
        getOnBackPressedDispatcher().onBackPressed();
        //remove shark from screen
    }
}
