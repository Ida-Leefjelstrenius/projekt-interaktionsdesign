package com.example.projektinteraktionsdesign;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LightSensor extends AppCompatActivity implements SensorEventListener {
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
            setLightMode(layout, R.color.white, R.color.black, R.string.lightMode);
        } else {
            setLightMode(layout, R.color.black, R.color.white, R.string.darkMode);
        }
    }

    private void setLightMode(ConstraintLayout layout, int backgroundColor, int textColor, int string) {
        layout.setBackgroundColor(getResources().getColor(backgroundColor, getTheme()));
        luxTextView.setTextColor(getResources().getColor(textColor, getTheme()));
        lightModeTextView.setTextColor(getResources().getColor(textColor, getTheme()));
        lightModeTextView.setText(getString(string));
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