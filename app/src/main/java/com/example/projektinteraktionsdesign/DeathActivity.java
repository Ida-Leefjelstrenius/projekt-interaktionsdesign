package com.example.projektinteraktionsdesign;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class DeathActivity extends Activity {
    private MediaPlayer deathTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_death);

        if (!GamePrefs.isMuted(this)) {
            deathTheme = MediaPlayer.create(this, R.raw.deaththeme);
            deathTheme.start();
        }

        //deathTheme = MediaPlayer.create(this, R.raw.deaththeme);
        //deathTheme.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (deathTheme != null) {
            deathTheme.release();
            deathTheme = null;
        }
    }

    public void startGame(View view){
        stopDeathTheme();
        Intent gameIntent = new Intent(this, GameActivity.class);
        startActivity(gameIntent);
    }

    public void startHome(View view){
        stopDeathTheme();
        Intent gameIntent = new Intent(this, MainActivity.class);
        startActivity(gameIntent);
    }

    private void stopDeathTheme() {
        if (deathTheme != null && deathTheme.isPlaying()) {
            deathTheme.stop();
            deathTheme.release();
            deathTheme = null;
        }
    }
}