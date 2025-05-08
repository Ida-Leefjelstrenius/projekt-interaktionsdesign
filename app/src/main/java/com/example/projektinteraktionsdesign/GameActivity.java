package com.example.projektinteraktionsdesign;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    GameView gameView;
    ImageView player, playPauseButton;
    TextView timer, coinCounter;

    long startTime = 0;
    long elapsedBeforePause = 0;
    boolean isPaused;
    Runnable timerRunnable;
    Handler timerHandler;
    private ActivityResultLauncher<Intent> treasureResultLauncher;
    private Handler feetAnimationHandler;
    private int feetFrame = 0;
    private final Bitmap[] feetPictures = new Bitmap[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        treasureResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        gameView.resume();
                        isPaused = false;
                        startTime = System.currentTimeMillis();
                        timerHandler.post(timerRunnable);
                    }
                }
        );

        FrameLayout rootLayout = new FrameLayout(this);

        gameView = new GameView(this); //Skapa bakgrunden

        gameView.setTreasureListener(() -> {
            isPaused = true;
            gameView.pause();
            elapsedBeforePause += System.currentTimeMillis() - startTime;
            timerHandler.removeCallbacksAndMessages(null);
            playPauseButton.setVisibility(View.VISIBLE);

            Intent chestIntent = new Intent(GameActivity.this, TreasureActivity.class);
            treasureResultLauncher.launch(chestIntent);
        });

        rootLayout.addView(gameView);

        createPlayer(rootLayout); //Skapa spelaren

        feetPictures[0] = BitmapFactory.decodeResource(getResources(), R.drawable.diver_feet_down);
        feetPictures[1] = BitmapFactory.decodeResource(getResources(), R.drawable.diver);

        feetAnimationHandler = new Handler();
        Runnable feetAnimationRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isPaused) {
                    feetFrame = (feetFrame + 1) % 2;
                    player.setImageBitmap(feetPictures[feetFrame]);
                }
                feetAnimationHandler.postDelayed(this, 200);
            }
        };
        feetAnimationHandler.post(feetAnimationRunnable);

        Typeface customFont = ResourcesCompat.getFont(this, R.font.moldiedemo);

        timer = new TextView(this);
        timer.setTypeface(customFont);
        timer.setTextSize(25);
        timer.setTextColor(Color.WHITE);
        timer.setPadding(20,20,20,20);
        coinCounter = new TextView(this);
        coinCounter.setTypeface(customFont);
        coinCounter.setTextSize(25);
        coinCounter.setTextColor(Color.WHITE);
        coinCounter.setPadding(20, 80, 20, 20);
        coinCounter.setText(getString(R.string.coin_label, 0));
        gameView.setPlayer(player);
        rootLayout.addView(timer);
        rootLayout.addView(coinCounter);
        setContentView(rootLayout);
        timerHandler = new Handler();
        isPaused = false;
        new TiltSensor(this, gameView, player); //Skapa tilt sensorn
        startTime = System.currentTimeMillis();

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
                boolean isGameOver = prefs.getBoolean("isGameOver", false);
                if (isGameOver) {
                    return;
                }
                long millis = elapsedBeforePause + (System.currentTimeMillis() - startTime);
                int seconds = (int) (millis / 1000);
                updateHighscoreIfNeeded(seconds);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                timer.setText(getString(R.string.time_alive, minutes, seconds));
                timerHandler.postDelayed(this, 500);

                gameView.setCoinUpdateListener(newAmount -> runOnUiThread(() -> coinCounter.setText(getString(R.string.coin_label, newAmount))));

            }
        };
        timerHandler.post(timerRunnable);

        playPauseButton = new ImageView(this);
        playPauseButton.setImageResource(R.drawable.pause);
        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                dpToPx(50), dpToPx(50)
        );

        playPauseButton.setOnClickListener(a -> showPausePopUp());

        buttonParams.gravity = Gravity.TOP | Gravity.END;
        buttonParams.setMargins(20, 20, 20, 20);
        playPauseButton.setLayoutParams(buttonParams);

        rootLayout.addView(playPauseButton);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void createPlayer(FrameLayout rootLayout) {
        player = new ImageView(this);
        player.setImageResource(R.drawable.diver);
        player.setBackgroundColor(Color.TRANSPARENT);
        FrameLayout.LayoutParams playerParams = new FrameLayout.LayoutParams(
                dpToPx(64), dpToPx(200));
        playerParams.gravity = Gravity.CENTER;
        player.setLayoutParams(playerParams);
        rootLayout.addView(player);
    }

    private void showPausePopUp() {
        isPaused = true;
        gameView.pause();
        elapsedBeforePause += System.currentTimeMillis() - startTime;
        timerHandler.removeCallbacksAndMessages(null);
        playPauseButton.setVisibility(View.INVISIBLE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_pause, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(popupView);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false); // block outside taps
        dialog.setCancelable(false); // (optional) block back button

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button resumeButton = popupView.findViewById(R.id.resumeButton);
        resumeButton.setOnClickListener(v -> {
            dialog.dismiss();
            isPaused = false;
            gameView.resume();
            startTime = System.currentTimeMillis();
            timerHandler.post(timerRunnable);
            playPauseButton.setVisibility(View.VISIBLE);
        });

        Button backToStart = popupView.findViewById(R.id.homeButton);
        backToStart.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


    }
    private void updateHighscoreIfNeeded(int seconds) {
        SharedPreferences prefs = getSharedPreferences("game_prefs", MODE_PRIVATE);
        int highscore = prefs.getInt("highscore", 0);

        if (seconds > highscore) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", seconds);
            editor.apply();
        }
    }

}