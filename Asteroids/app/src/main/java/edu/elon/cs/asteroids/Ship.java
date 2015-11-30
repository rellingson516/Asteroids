package edu.elon.cs.asteroids;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by rellingson on 11/30/2015.
 */
public class Ship {

    private final int screenWidth, screenHeight;
    private int x, y;
    private float width, height;
    private Bitmap bitmap;

    private final float SCALE = 1.0f;

    public Ship(Context context) {

        //get image
        //bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);

        //set dimensions
        width = bitmap.getWidth() * SCALE;
        height = bitmap.getHeight() * SCALE;

        //figure out screen width
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        //start int bottom center
        x = screenWidth/2;
        y = (int)(screenHeight - height/2);

    }

    public void doDraw(Canvas canvas) {
        //TODO
    }

    public void doUpdate(double elapsed) {
        //TODO
    }

    public void fire() {
        //TODO
    }
}
