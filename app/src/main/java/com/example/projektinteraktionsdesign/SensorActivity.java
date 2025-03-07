package com.example.projektinteraktionsdesign;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class SensorActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor light;
    private TextView luxTextView;
    private TextView lightModeTextView;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        luxTextView = findViewById(R.id.lux_value);
        lightModeTextView = findViewById(R.id.lux_label);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float lux = event.values[0];

        String luxText = getString(R.string.luxString, lux);
        luxTextView.setText(luxText);

        ConstraintLayout layout = findViewById(R.id.sensor);
        if (lux > 20) {
            layout.setBackgroundColor(getResources().getColor(R.color.white, getTheme()));
            luxTextView.setTextColor(getResources().getColor(R.color.black, getTheme()));
            lightModeTextView.setTextColor(getResources().getColor(R.color.black, getTheme()));
            lightModeTextView.setText(getString(R.string.lightMode));
        } else {
            layout.setBackgroundColor(getResources().getColor(R.color.black, getTheme()));
            luxTextView.setTextColor(getResources().getColor(R.color.white, getTheme()));
            lightModeTextView.setTextColor(getResources().getColor(R.color.white, getTheme()));
            lightModeTextView.setText(getString(R.string.darkMode));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}