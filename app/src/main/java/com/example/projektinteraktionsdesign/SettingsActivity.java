package com.example.projektinteraktionsdesign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

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

        Button resetHighscoreButton = findViewById(R.id.btn_resetHighScore);

        resetHighscoreButton.setOnClickListener(v -> {
            GamePrefs.resetHighscore(this);
            Toast.makeText(this, "Highscore reset!", Toast.LENGTH_SHORT).show();
        });
    }

    public void openSensorActivity(View view) {
        Intent intent = new Intent(this, LightSensor.class);
        startActivity(intent);
    }


}