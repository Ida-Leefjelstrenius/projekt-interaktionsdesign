package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity implements ShakeActivity.Listener{
    private ShakeActivity shakeActivity;
    private SensorEvent event;
    FrameLayout frameSharkDialog;
    ImageView imageShark;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        shakeActivity = new ShakeActivity(this, this);

        imageShark = findViewById(R.id.imageShark);
        frameSharkDialog = findViewById(R.id.frameSharkDialog);
        imageShark.setVisibility(View.INVISIBLE);
        testRepeatedInteractions();

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

    public void testRepeatedInteractions(){ //every ten seconds for an hour
        Runnable test = () -> runOnUiThread(() -> imageShark.setVisibility(View.VISIBLE));
        ScheduledFuture<?> timer =
                scheduler.scheduleWithFixedDelay(test, 10, 10, TimeUnit.SECONDS);
        Runnable canceller = () -> timer.cancel(false);
        scheduler.schedule(canceller, 1, TimeUnit.HOURS);
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
        imageShark.setVisibility(View.INVISIBLE);

    }
}

