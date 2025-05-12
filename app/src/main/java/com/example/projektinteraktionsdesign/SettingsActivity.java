package com.example.projektinteraktionsdesign;

import static com.example.projektinteraktionsdesign.GameConstants.*;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
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

        switchMute.setOnCheckedChangeListener(this::onMutedChanged);

        SwitchCompat switchHitbox = findViewById(R.id.hitboxSwitch);
        boolean isHitboxOn = GamePrefs.isHitboxOn(this);
        switchHitbox.setChecked(isHitboxOn);

        switchHitbox.setOnCheckedChangeListener(this::onHitboxChanged);


        Button resetHighscoreButton = findViewById(R.id.btn_resetHighScore);

        resetHighscoreButton.setOnClickListener(v -> {
            GamePrefs.resetHighscore(this);
            Toast.makeText(this, "Highscore reset!", Toast.LENGTH_SHORT).show();
        });

        RadioButton easyShark = findViewById(R.id.shark_radio_easy);
        easyShark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GamePrefs.setSharkDifficulty(this, SHARK_EASY_MULTIPLIER);
            Toast.makeText(this, "Easy Shark!", Toast.LENGTH_SHORT).show();
        });

        RadioButton normalShark = findViewById(R.id.shark_radio_normal);
        normalShark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GamePrefs.setSharkDifficulty(this, 1);
            Toast.makeText(this, "Normal Shark!", Toast.LENGTH_SHORT).show();
        });

        RadioButton hardShark = findViewById(R.id.shark_radio_hard);
        hardShark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GamePrefs.setSharkDifficulty(this, SHARK_HARD_MULTIPLIER);
            Toast.makeText(this, "Hard Shark!", Toast.LENGTH_SHORT).show();
        });

        RadioButton easyBomb = findViewById(R.id.bomb_radio_easy);
        easyBomb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GamePrefs.setMineDifficulty(this, MINE_EASY_MULTIPLIER);
            Toast.makeText(this, "Easy Bombs!", Toast.LENGTH_SHORT).show();
        });

        RadioButton normalBomb = findViewById(R.id.bomb_radio_normal);
        normalBomb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GamePrefs.setMineDifficulty(this, 1);
            Toast.makeText(this, "Normal Bombs!", Toast.LENGTH_SHORT).show();
        });

        RadioButton hardBomb = findViewById(R.id.bomb_radio_hard);
        hardBomb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            GamePrefs.setMineDifficulty(this, MINE_HARD_MULTIPLIER);
            Toast.makeText(this, "Hard Bombs!", Toast.LENGTH_SHORT).show();
        });
    }

    private void onMutedChanged(CompoundButton buttonView, boolean isChecked) {
        GamePrefs.setMuted(this, isChecked);
    }

    private void onHitboxChanged(CompoundButton buttonView, boolean isChecked) {
        GamePrefs.setHitboxOn(this, isChecked);
    }
}