package edu.elon.cs.asteroids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by rellingson on 11/30/2015.
 */
public class GameLoopView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener, LocationListener{

    private final double MAX_TIME = 60.0;

    private GameLoopThread thread;
    private SurfaceHolder surfaceHolder;
    private Context context;

    private int score;
    private double timeRemaining;

    private Location location;

    private TextView scoreView;
    private TextView timeView;

    private final float X_ANGLE_WIDTH = 29.0f;
    private final float Y_ANGLE_WIDTH = 19.0f;

    private float direction = 0.0f;
    private float rollingZ = 0.0f;
    private float rollingX = 0.0f;
    private float inclination = 0.0f;

    private int screenWidth, screenHeight;

    public GameLoopView(Context context) {
        super(context);

        // remember the context for finding resources
        this.context = context;

        score = 0;

        // want to know when the surface changes
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        //set a fixed location

        location = new Location("FIXED POSITION");
        location.setLatitude(0);
        location.setAltitude(0);
        location.setLongitude(0);



        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        screenWidth = p.x;
        screenHeight = p.y;

        // game loop thread
        thread = new GameLoopThread();
    }

    public void setTextViews(TextView scoreView, TextView timeView) {
        this.scoreView = scoreView;
        this.timeView = timeView;
        scoreView.setText("Score: ");
        timeView.setText("Time Remaining: ");
    }

    //SensorEventListener

    private float[] magnetic_field_values = new float[3];
    private float[] accelerometer_values = new float[3];
    private static final int MATRIX_SIZE = 16;
    private static final float KFILTER = 0.1f;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetic_field_values[0] = (float) (magnetic_field_values[0] * (1.0 - KFILTER) + event.values[0] *KFILTER);
            magnetic_field_values[1] = (float) (magnetic_field_values[1] * (1.0 - KFILTER) + event.values[1] *KFILTER);
            magnetic_field_values[2] = (float) (magnetic_field_values[2] * (1.0 - KFILTER) + event.values[2] *KFILTER);
        }
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometer_values[0] = (float) (accelerometer_values[0] * (1.0 - KFILTER) + event.values[0] * KFILTER);
            accelerometer_values[1] = (float) (accelerometer_values[1] * (1.0 - KFILTER) + event.values[1] * KFILTER);
            accelerometer_values[2] = (float) (accelerometer_values[2] * (1.0 - KFILTER) + event.values[2] * KFILTER);

            rollingZ = (float) ((accelerometer_values[2] * KFILTER) + rollingZ * (1.0 - KFILTER));
            rollingX = (float) ((accelerometer_values[0] * KFILTER) + rollingX * (1.0 - KFILTER));

            if(rollingZ != 0.0f) {
                inclination = (float) Math.atan(rollingX/rollingZ);
            } else if(rollingX < 0) {
                inclination = (float)(Math.PI/2.0);
            } else if(rollingX >= 0) {
                inclination = (float) (3 * Math.PI/2.0);
            }

            inclination = (float)(inclination * (360/(2*Math.PI)));
            if(inclination < 0) inclination += 90;
            else inclination -= 90;

        }

        if(magnetic_field_values != null && accelerometer_values != null) {

            float[] R = new float[MATRIX_SIZE];
            float[] I = new float[MATRIX_SIZE];

            if(SensorManager.getRotationMatrix(R,I,accelerometer_values,magnetic_field_values)) {

                float[] orientation_values = new float[3];
                SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Z, R);
                SensorManager.getOrientation(R, orientation_values);

                //radians
                float azimuth = (float) Math.toDegrees(orientation_values[0]);
                azimuth = (azimuth + 360) % 360;

                direction = (float) ((azimuth * KFILTER) + direction * (1.0 - KFILTER));
            }
        }

        scoreView.setText("Score: " + score);
        timeView.setText(String.format("Time Remaining: %.2f", timeRemaining));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

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
        //Does nothing
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

        //ship
        private CrossHair crossHair;

        //Asteroids
        private int MAX_ASTEROIDS = 10;
        private ArrayList<Asteroid> asteroids;

        public GameLoopThread() {
           timeRemaining = MAX_TIME;

           asteroids = new ArrayList<>();
           for(int i = 0; i < MAX_ASTEROIDS; i++) {
               asteroids.add(new Asteroid(context, location));
           }

            crossHair = new CrossHair(context, asteroids);


        }

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        private float computeX(float leftArm, float rightArm, float azi) {
            float offset = azi - leftArm;
            if(leftArm > rightArm && azi <= rightArm) {
                offset = 360 - leftArm + azi;
            }
            return (offset/X_ANGLE_WIDTH) * screenWidth;
        }

        private float computeY(float lowerArm, float upperArm, float inc) {
            float offset = ((upperArm - Y_ANGLE_WIDTH) - inc) * -1;
            return screenHeight - ((offset/Y_ANGLE_WIDTH) * screenHeight);
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

            //viewport
            float leftArm = direction - (X_ANGLE_WIDTH/2);
            if(leftArm < 0) leftArm += 360;
            float rightArm = direction + (X_ANGLE_WIDTH/2);
            if(rightArm > 360) rightArm -= 360;
            float upperArm = inclination + (Y_ANGLE_WIDTH/2);
            float lowerArm = inclination - (Y_ANGLE_WIDTH/2);

            ArrayList<Asteroid> toRemove = new ArrayList<>();

            for(Asteroid asteroid: asteroids) {

                float azi = location.bearingTo(asteroid.getLocation());
                if (azi < 0) azi += 360;
                float inc = (float) Math.atan(asteroid.getLocation().getAltitude() / location.distanceTo(asteroid.getLocation()));

                asteroid.x = computeX(leftArm, rightArm, azi);
                asteroid.y = computeY(lowerArm, upperArm, inc);

                asteroid.doUpdate(elapsed);

                if(asteroid.hits <= 0) {
                    toRemove.add(asteroid);
                    score += 100;
                }

            }

            timeRemaining -= elapsed;

            asteroids.removeAll(toRemove);
            toRemove.clear();

            if(asteroids.size() < MAX_ASTEROIDS) {
                asteroids.add(new Asteroid(context, location));
            }

            if(timeRemaining <= 0) {
                switchScreen();
            }



        }

        public void doDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
            for(Asteroid asteroid: asteroids) {
                asteroid.doDraw(canvas);
            }
            crossHair.doDraw(canvas);

        }

        public void screenTouched() {
            crossHair.fire();
        }

        private void switchScreen() {
            Intent intent = new Intent(context, GameOverActivity.class);
            intent.putExtra("score", score);
            context.startActivity(intent);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
