package com.example.projektinteraktionsdesign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.media.MediaPlayer;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer theme_music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        int highscore = GamePrefs.getHighscore(this);

        TextView highscoreText = findViewById(R.id.highScore);
        int minutes = highscore / 60;
        int seconds = highscore % 60;
        highscoreText.setText(getString(R.string.highscore_label, minutes, seconds));

        if (!GamePrefs.isMuted(this)) {
            theme_music = MediaPlayer.create(this, R.raw.theme);
            theme_music.setLooping(true);
            theme_music.start();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (theme_music != null && theme_music.isPlaying()) {
            theme_music.pause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        int highscore = GamePrefs.getHighscore(this);
        TextView highscoreText = findViewById(R.id.highScore);
        int minutes = highscore / 60;
        int seconds = highscore % 60;
        highscoreText.setText(getString(R.string.highscore_label, minutes, seconds));

        boolean isMuted = GamePrefs.isMuted(this);

        if (isMuted) {
            if (theme_music != null) {
                if (theme_music.isPlaying()) {
                    theme_music.pause();
                }
            }
        } else {
            if (theme_music == null) {
                theme_music = MediaPlayer.create(this, R.raw.theme);
                theme_music.setLooping(true);
                theme_music.start();
            } else if (!theme_music.isPlaying()) {
                theme_music.start();
            }
        }
    }

    public void startGame(View view){
        Intent gameIntent = new Intent(this, GameActivity.class);
        startActivity(gameIntent);
    }

    public void openProfilePage(View view){
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    public void openInventoryPage(View view){
        Intent inventoryIntent = new Intent(this, InventoryActivity.class);
        startActivity(inventoryIntent);
    }

    public void openSettingsPage(View view){
        Intent settingsIntent = new Intent(this,SettingsActivity.class);
        startActivity(settingsIntent);
    }
    public void openInfoPage(View view){
        Intent infoIntent = new Intent(this,InfoActivity.class);
        startActivity(infoIntent);
    }
}