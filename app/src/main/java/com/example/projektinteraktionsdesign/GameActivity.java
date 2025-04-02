package com.example.projektinteraktionsdesign;

import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements ShakeActivity.Listener{
    private ShakeActivity shakeActivity;
    private SensorEvent event;
    FrameLayout frameSharkDialog;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        shakeActivity = new ShakeActivity(this, this);

        ImageView imageShark = findViewById(R.id.imageShark);
        frameSharkDialog = findViewById(R.id.frameSharkDialog);
        imageShark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                shakeActivity.register();
                CharSequence text = "Nu kan du skaka!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(GameActivity.this, text, duration);
                toast.show();
                frameSharkDialog.setVisibility(View.VISIBLE);
            }
        });
    }

    public void integrateWithShark(){
        shakeActivity.onSensorChanged(event);
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

    @Override
    protected void onResume() {
        super.onResume();
        shakeActivity.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeActivity.unregister();
    }

    @Override
    public void onTranslation() {
        // Här ska vi sätta det som händer vid skakning
        frameSharkDialog.setVisibility(View.INVISIBLE);

    }
}

