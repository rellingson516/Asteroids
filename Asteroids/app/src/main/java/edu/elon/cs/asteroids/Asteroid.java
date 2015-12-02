package edu.elon.cs.asteroids;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by rellingson on 11/30/2015.
 */
public class Asteroid {

    private final int STARTING_DISTANCE = 100;
    private final int MAX_HITS = 4;

    public int hits;

    private int x, y;
    private int distance;
    private int realX, realY, realZ;
    private int speed;

    private float width, height;
    private Bitmap bitmap;

    private int screenWidth, screenHeight;

    private final float SCALE = 1.0f;

    private Context context;

    public Asteroid(Context context, int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.context = context;

        this.hits = MAX_HITS;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid1);


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
        canvas.drawBitmap(bitmap,
                null,
                new Rect((int) (x - width/2), (int) (y - height/2),
                        (int) (x + width/2), (int) (y + height/2)),
                null);
    }

    public void doUpdate(double elapsed) {
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
            case 4:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid4);
                break;
        }
    }

    public void hit() {
        hits--;
    }



}
