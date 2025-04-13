package com.example.projektinteraktionsdesign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PauseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

        Button resumeButton = findViewById(R.id.resumeButton);

        Button homeButton = findViewById(R.id.homeButton);
    }

    public void openGame(View view){
        Intent resumeGame = new Intent(this, GameActivity.class);
        startActivity(resumeGame);
    }

    public void openHomePage(View view){
        Intent backHome = new Intent(this, MainActivity.class);
        startActivity(backHome);
    }
}
