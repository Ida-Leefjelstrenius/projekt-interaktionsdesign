package com.example.projektinteraktionsdesign;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class DeathActivity extends Activity {
    private MediaPlayer deathTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_death);

        deathTheme = MediaPlayer.create(this, R.raw.deaththeme);
        deathTheme.setOnCompletionListener(mp -> {
            mp.release();
            Intent intent = new Intent(DeathActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        deathTheme.start();
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
        Intent gameIntent = new Intent(this, GameActivity.class);
        startActivity(gameIntent);
    }
}