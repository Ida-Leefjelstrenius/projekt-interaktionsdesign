package com.example.projektinteraktionsdesign;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void openSensorActivity(View view) {
        Intent intent = new Intent(this, SensorActivity.class);
        startActivity(intent);
    }

    public void openGameActivity(View view) {
        Intent gintent = new Intent(this, GameActivity.class);
        startActivity(gintent);
    }

    public void openMovingActivity(View view){
        Intent movingBackgroundIntent = new Intent(this, MovingActivity.class);
        startActivity(movingBackgroundIntent);
    }

    public void openProfilePage(View view){
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        startActivity(profileIntent);
    }

    public void openInventoryPage(View view){
        Intent inventoryIntent = new Intent(this, InventoryActivity.class);
        startActivity(inventoryIntent);
    }

    public void openTiltActivity(View view){
        Intent tiltIntent = new Intent(this, TiltActivity.class);
        startActivity(tiltIntent);
    }

    public void openSettingsPage(View view){
        Intent settingsIntent = new Intent(this,SettingsActivity.class);
        startActivity(settingsIntent);
    }
}