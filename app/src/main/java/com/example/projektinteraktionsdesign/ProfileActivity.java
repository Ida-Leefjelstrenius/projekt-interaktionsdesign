package com.example.projektinteraktionsdesign;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        TextView coinText = findViewById(R.id.totalCoins);

        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        int totalCoins = prefs.getInt("total_coins", 0);

        coinText.setText(getString(R.string.total_coin_label, totalCoins));
    }
}