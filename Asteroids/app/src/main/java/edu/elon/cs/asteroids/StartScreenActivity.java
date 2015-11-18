package edu.elon.cs.asteroids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class StartScreenActivity extends AppCompatActivity {

    TextView title;
    Button startButton;
    Button scoreButton;
    Button instructionsButton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        title = (TextView) findViewById(R.id.title);
        startButton = (Button) findViewById(R.id.startButton);
        scoreButton = (Button) findViewById(R.id.scoreButton);
        instructionsButton = (Button) findViewById(R.id.instructionButton);
        Typeface font = Typeface.createFromAsset(getAssets(), "pixel-font.ttf");
        title.setTypeface(font);
        scoreButton.setTypeface(font);
        startButton.setTypeface(font);
        instructionsButton.setTypeface(font);



    }

    public void onPlay(View view) {
        switchScreen(PlayActivity.class);
    }

    public void onScores(View view) {
        switchScreen(HighScoreActivity.class);
    }

    public void onInstructions(View view) {
        switchScreen(InstructionsActivity.class);

    }

    private void switchScreen(Class<?> cls) {
        intent = new Intent(getApplicationContext(), cls);
        startActivity(intent);
    }


}
