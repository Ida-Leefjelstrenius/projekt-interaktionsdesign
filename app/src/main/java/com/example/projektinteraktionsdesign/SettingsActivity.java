package com.example.projektinteraktionsdesign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);

        SwitchCompat switchMute = findViewById(R.id.muteSwitch);
        boolean isMuted = GamePrefs.isMuted(this);
        switchMute.setChecked(isMuted);

        switchMute.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GamePrefs.setMuted(this, isChecked);
        });
    }

    public void openSensorActivity(View view) {
        Intent intent = new Intent(this, LightSensor.class);
        startActivity(intent);
    }


}