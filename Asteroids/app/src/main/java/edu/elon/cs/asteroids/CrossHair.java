package edu.elon.cs.asteroids;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by rellingson on 11/30/2015.
 */
public class CrossHair {

    private final int screenWidth, screenHeight;
    private int x, y;
    private float width, height;
    private Bitmap bitmap;

    private ArrayList<Asteroid> asteroids;

    private final float SCALE = 1.0f;

    public CrossHair(Context context, ArrayList<Asteroid> asteroids) {

        this.asteroids = asteroids;

        //get image
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.crosshair);

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

        //start in middle
        x = screenWidth/2;
        y = screenHeight/2;

    }

    public void doDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, null,
                new Rect((int) (x - width/2), (int) (y - height/2),
                        (int) (x + width/2), (int) (y + height/2)), null);
    }

    public void doUpdate(double elapsed) {
        //empty?
    }

    public void fire() {
        for(Asteroid asteroid: asteroids) {
            if(asteroid.x > (x - width/2) && asteroid.x < (x + width/2) &&
                    asteroid.y > (y - height/2) && asteroid.y < (y + height/2)) {
                asteroid.hit();
            }
        }
    }
}
