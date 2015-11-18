package edu.elon.cs.asteroids;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HighScoreActivity extends AppCompatActivity {

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        title = (TextView) findViewById(R.id.scoreTitle);
        Typeface font = Typeface.createFromAsset(getAssets(), "pixel-font.ttf");
        title.setTypeface(font);
    }


}
