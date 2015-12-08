package edu.elon.cs.asteroids;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {

    private GameLoopView gameLoopView;
    private SensorManager sensorManager;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Typeface font = Typeface.createFromAsset(getAssets(), "pixel-font.ttf");

        TextView scoreView = new TextView(this);
        TextView timeView = new TextView(this);
        scoreView.setTextColor(Color.argb(255,50,205,50));
        timeView.setTextColor(Color.argb(255,50,205,50));
        scoreView.setTypeface(font);
        scoreView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        scoreView.setGravity(Gravity.RIGHT);
        timeView.setTypeface(font);
        timeView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        gameLoopView = new GameLoopView(this);
        gameLoopView.setTextViews(scoreView, timeView);

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        frameLayout.addView(gameLoopView);
        frameLayout.addView(scoreView);
        frameLayout.addView(timeView);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(gameLoopView, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(gameLoopView, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

}
