package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.SensorEvent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SharkActivity extends AppCompatActivity implements ShakeActivity.Listener{
    private ShakeActivity shakeActivity;
    private SensorEvent event;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shark);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        shakeActivity = new ShakeActivity(this, this);
    }
    public void integrateWithShark(){
        shakeActivity.onSensorChanged(event);
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
        Intent movingBackgroundIntent = new Intent(SharkActivity.this, GameActivity.class);
        startActivity(movingBackgroundIntent);
    }
}
