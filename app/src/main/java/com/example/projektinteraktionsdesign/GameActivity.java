package com.example.projektinteraktionsdesign;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    String  chooseMiniGame() {
        Random rand = new Random();
        int miniGame = (rand.nextInt(3) + 1);

        switch (miniGame) {
            case 1:
                return "vrida";
            case 2:
                return "hacka";
            default:
                return "luta";
        }
    }

}

