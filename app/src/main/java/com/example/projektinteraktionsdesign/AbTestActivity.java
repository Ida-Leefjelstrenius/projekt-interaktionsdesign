package com.example.projektinteraktionsdesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class AbTestActivity extends AppCompatActivity {
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test);
    }

    public void startOption1(View view){
        Intent option1Intent = new Intent(this, TreasureActivity.class);
        startActivity(option1Intent);
    }

    public void startOption2(View view){
        Intent option2Intent = new Intent(this, RotateActivity.class);
        startActivity(option2Intent);
    }

}
