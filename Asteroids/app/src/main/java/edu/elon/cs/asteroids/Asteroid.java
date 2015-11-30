package edu.elon.cs.asteroids;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by rellingson on 11/30/2015.
 */
public class Asteroid {

    private final int STARTING_DISTANCE = 100;
    private final int MAX_HITS = 4;

    private int hits;

    private int x, y;
    private int distance;
    private int realX, realY, realZ;
    private int speed;

    private float width, height;
    private Bitmap bitmap;

    private int screenWidth, screenHeight;

    private final float SCALE = 1.0f;

    public Asteroid(Context context, int x, int y, int speed, int hits) {
        this.x = x;
        this.y = y;
        this.speed = speed;

        if(hits > MAX_HITS) {
            this.hits = MAX_HITS;
        } else {
            this.hits = hits;
        }

        //get the image
        switch (hits) {
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid1);
            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid2);
            case 3:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid3);
            case 4:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid4);
        }

        //scale
        width = bitmap.getWidth() * SCALE;
        height = bitmap.getHeight() * SCALE;

        //
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display =  wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

    }

    public  void doDraw(Canvas canvas) {
        //TODO add draw method
    }

    public void doUpdate(double elapsed) {
        //TODO add update method
    }



}
