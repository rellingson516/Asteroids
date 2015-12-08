package edu.elon.cs.asteroids;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by rellingson on 11/30/2015.
 */
public class Asteroid {

    private final int STARTING_DISTANCE = 100;
    private final int MAX_HITS = 4;

    private final double MIN_LAT_LONG = -.00003;
    private final double MAX_LAT_LONG = .00003;
    private final double MIN_ALT = -30;
    private final double MAX_ALT = 30;

    protected int hits;

    protected float x, y;



    public Location getLocation() {
        return location;
    }

    private Location location;
    private Bitmap bitmap;
    private Matrix matrix;


    //private int speed;

    private float width, height;


    private int screenWidth, screenHeight;

    private final float SCALE = 1.0f;

    private Context context;

    public Asteroid(Context context, Location pLocation) {
        this.context = context;

        Random random = new Random();
        double randomLat = MIN_LAT_LONG + (MAX_LAT_LONG - MIN_LAT_LONG) * random.nextDouble();
        double randomLong = MIN_LAT_LONG + (MAX_LAT_LONG - MIN_LAT_LONG) * random.nextDouble();

        double randomAlt = MIN_ALT + (MAX_ALT - MIN_ALT) * random.nextDouble();

        location = new Location("Random Location");
        location.setLatitude(pLocation.getLatitude() - randomLat);
        location.setLongitude(pLocation.getLongitude() - randomLong);
        location.setAltitude(randomAlt);

        matrix = new Matrix();

        x = y = 0;

        this.hits = MAX_HITS;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid4);


        //scale
        width = bitmap.getWidth() * SCALE;
        height = bitmap.getHeight() * SCALE;

        /*
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display =  wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        */

    }

    public  void doDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, matrix, null);
    }

    public void doUpdate(double elapsed) {
        matrix.reset();
        matrix.postTranslate(x, y);
    }

    public void hit() {
        hits--;
        //change the image based on how many times it's been hit
        switch (hits) {
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid1);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid2);
                break;
            case 3:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid3);
                break;
        }

    }



}
