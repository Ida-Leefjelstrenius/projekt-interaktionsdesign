package com.example.projektinteraktionsdesign;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class InventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inventory);


        Button resetCoinsButton = findViewById(R.id.btn_reset_coins);
        resetCoinsButton.setOnClickListener(v -> {
            GamePrefs.resetCoins(this);
            Toast.makeText(this, "Coins reset!", Toast.LENGTH_SHORT).show();
        });

        Button resetHighscoreButton = findViewById(R.id.btn_resetHighScore);
        resetHighscoreButton.setOnClickListener(v -> {
            GamePrefs.resetHighscore(this);
            Toast.makeText(this, "Highscore reset!", Toast.LENGTH_SHORT).show();
        });
    }
}