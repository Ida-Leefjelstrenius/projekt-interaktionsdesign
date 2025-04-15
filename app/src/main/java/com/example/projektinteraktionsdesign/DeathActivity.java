package com.example.projektinteraktionsdesign;

import android.os.Bundle;


import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.TextView;

public class DeathActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_death);
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(DeathActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }, 3000);
    }
}