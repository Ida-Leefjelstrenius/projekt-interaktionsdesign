package com.example.projektinteraktionsdesign;

import static com.example.projektinteraktionsdesign.GameConstants.*;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;

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

        setupDifficulty(SHARK_DIFFICULTY, R.id.shark_radio_easy, R.id.shark_radio_normal, R.id.shark_radio_hard);
        setupDifficulty(MINE_DIFFICULTY, R.id.bomb_radio_easy, R.id.bomb_radio_normal, R.id.bomb_radio_hard);
        setupDifficulty(CHEST_DIFFICULTY, R.id.chest_radio_easy, R.id.chest_radio_normal, R.id.chest_radio_hard);
    }

    private void setupDifficulty(String difficultyType, int radioEasy, int radioNormal, int radioHard) {
        switch (GamePrefs.getDifficulty(this, difficultyType)) {
            case EASY:
                ((RadioButton) findViewById(radioEasy)).setChecked(true);
                break;
            case HARD:
                ((RadioButton) findViewById(radioHard)).setChecked(true);
                break;
            default:
                ((RadioButton) findViewById(radioNormal)).setChecked(true);
                break;
        }

        RadioButton easyButton = findViewById(radioEasy);
        easyButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                GamePrefs.setDifficulty(this, difficultyType, EASY);
            }
        });

        RadioButton normalButton = findViewById(radioNormal);
        normalButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                GamePrefs.setDifficulty(this, difficultyType, NORMAL);
            }
        });

        RadioButton hardButton = findViewById(radioHard);
        hardButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                GamePrefs.setDifficulty(this, difficultyType, HARD);
            }
        });
    }

    private void onMutedChanged(CompoundButton buttonView, boolean isChecked) {
        GamePrefs.setMuted(this, isChecked);
    }
}