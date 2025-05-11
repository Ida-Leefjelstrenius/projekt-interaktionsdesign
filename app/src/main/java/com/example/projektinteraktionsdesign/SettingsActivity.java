package com.example.projektinteraktionsdesign;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
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

        switchMute.setOnCheckedChangeListener(this::onCheckedChanged);

        Button resetHighscoreButton = findViewById(R.id.btn_resetHighScore);

        resetHighscoreButton.setOnClickListener(v -> {
            GamePrefs.resetHighscore(this);
            Toast.makeText(this, "Highscore reset!", Toast.LENGTH_SHORT).show();
        });
    }

    private void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        GamePrefs.setMuted(this, isChecked);
    }
}