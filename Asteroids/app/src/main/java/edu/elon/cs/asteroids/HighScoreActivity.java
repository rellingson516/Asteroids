package edu.elon.cs.asteroids;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HighScoreActivity extends AppCompatActivity {

    private final String FILENAME = "save.txt";

    TextView title;
    TextView scoreView;

    String scoreViewText;
    String scores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        scores = "0,0,0,0,0,";
        scoreViewText = "";
        title = (TextView) findViewById(R.id.scoreTitle);
        scoreView = (TextView) findViewById(R.id.highScores);
        Typeface font = Typeface.createFromAsset(getAssets(), "pixel-font.ttf");
        title.setTypeface(font);
        scoreView.setTypeface(font);

        try {
            getPersistentData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] highScores = scores.split(",");
        for(int i = 0; i < highScores.length; i++) {
            scoreViewText += (i + 1) + ": " + highScores[i] +"\n";
        }

        scoreView.setText(scoreViewText);
    }

    private void getPersistentData() throws IOException {
        Context context = getBaseContext();
        BufferedReader reader = null;
        try {
            InputStream in = context.openFileInput(FILENAME);
            reader = new BufferedReader(new InputStreamReader(in));
            String read = reader.readLine();
            if(read != null) {
                scores = read;
            }


        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }


}
