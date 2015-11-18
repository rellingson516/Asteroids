package edu.elon.cs.asteroids;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.awt.font.TextAttribute;

public class InstructionsActivity extends AppCompatActivity {

    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        title = (TextView) findViewById(R.id.instTitle);
        Typeface font = Typeface.createFromAsset(getAssets(), "pixel-font.ttf");
        title.setTypeface(font);
    }

}
