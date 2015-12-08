package edu.elon.cs.asteroids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.Buffer;
import java.util.ArrayList;

public class GameOverActivity extends AppCompatActivity {

    private final int NUM_SCORES = 5;

    private ArrayList<Integer> highScores;

    private final String FILENAME = "save.txt";

    TextView gameOver;
    TextView scoreView;
    Button nextPage;

    int myScore;

    String scores;

    String titleText;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        scores = "";
        titleText = "Game Over";
        highScores = new ArrayList<>();
        for(int i = 0; i < NUM_SCORES; i++) {
            highScores.add(0);
        }

        intent = getIntent();
        myScore = intent.getIntExtra("score", 0);

        Typeface font = Typeface.createFromAsset(getAssets(), "pixel-font.ttf");
        gameOver = (TextView) findViewById(R.id.gameOver);
        scoreView = (TextView) findViewById(R.id.score);
        nextPage = (Button) findViewById(R.id.menuButton);

        gameOver.setTypeface(font);
        scoreView.setTypeface(font);
        nextPage.setTypeface(font);

        scoreView.setText("Score: " + myScore);

        try {
            getPersistentData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!scores.equals("")) {
            String[] separated = scores.split(",");
            for(int i = 0; i < NUM_SCORES; i++) {
                highScores.set(i, Integer.parseInt(separated[i]));
            }
        }

        for (int i = 0; i < NUM_SCORES; i++) {
            if(myScore > highScores.get(i)) {
                highScores.add(i, myScore);
                highScores.remove(NUM_SCORES);
                titleText = "New High Score";
                break;
            }
        }

        gameOver.setText(titleText);



    }

    public void onNext(View view) {
        try {
            putPersistentData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent = new Intent(getBaseContext(), StartScreenActivity.class);
        startActivity(intent);

    }

    private void putPersistentData() throws IOException {
        Context context = getBaseContext();
        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            String newScores = "";
            for(int i = 0; i < NUM_SCORES; i++) {
                newScores += highScores.get(i) + ",";
            }
            writer.write(newScores);

        } finally {
            if(writer != null) {
                writer.close();
            }
        }
    }

    private void getPersistentData() throws IOException {
        Context context = getBaseContext();
        BufferedReader reader = null;
        try {
            InputStream in = context.openFileInput(FILENAME);
            reader = new BufferedReader(new InputStreamReader(in));
            String read = reader.readLine();
            if(read != null) {
                scores += read;
            }


        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }


}
