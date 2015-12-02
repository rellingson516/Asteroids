package edu.elon.cs.asteroids;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by rellingson on 11/30/2015.
 */
public class GameLoopView extends SurfaceView implements SurfaceHolder.Callback{

    private GameLoopThread thread;
    private SurfaceHolder surfaceHolder;
    private Context context;

    private float touchX, touchY;

    public GameLoopView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // remember the context for finding resources
        this.context = context;

        // want to know when the surface changes
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        // game loop thread
        thread = new GameLoopThread();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new GameLoopThread();
        }

        // start the game loop
        thread.setIsRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //Does nothing?
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setIsRunning(false);

        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            thread.screenTouched();
        }
        return true;
    }

    private class GameLoopThread extends Thread {
        private boolean isRunning = false;
        private long lastTime;

        private int level = 1;

        //ship
        private Ship ship;

        //Asteroids
        private int MAX_ASTEROIDS = 5;
        private ArrayList<Asteroid> asteroids;

        public GameLoopThread() {
           // ship = new Ship(context);

            asteroids = new ArrayList<>();
            asteroids.add(new Asteroid(context, 540,360, 5));
        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        @Override
        public void run() {
            lastTime = System.currentTimeMillis();

            while (isRunning) {

                // grab hold of the canvas
                Canvas canvas = surfaceHolder.lockCanvas();
                if (canvas == null) {
                    // trouble -- exit nicely
                    isRunning = false;
                    continue;
                }

                synchronized (surfaceHolder) {

                    // compute how much time since last time around
                    long now = System.currentTimeMillis();
                    double elapsed = (now - lastTime) / 1000.0;
                    lastTime = now;

                    // update/draw
                    doUpdate(elapsed);
                    doDraw(canvas);

                }

                // release the canvas
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

        }

        public void doUpdate(double elapsed) {
            for(Asteroid a: asteroids) {
                if(a.hits <= 0) {
                    asteroids.remove(a);
                } else {
                    a.doUpdate(elapsed);
                }
            }

        }

        public void doDraw(Canvas canvas) {

            canvas.drawColor(Color.BLACK);

            for(Asteroid a: asteroids) {
                a.doDraw(canvas);
            }
        }

        public void screenTouched() {
            //proof of concept
            for(Asteroid a : asteroids) {
                a.hit();
            }
        }

    }
}
