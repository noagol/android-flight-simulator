package com.example.ebng.flightsimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback {
    private Integer xInCircle;
    private Integer yInCircle;
    private Integer xOutCircle;
    private Integer yOutCircle;
    private Integer radiusOut;
    private Integer radiusIn;
    private Paint inCirclePaint;
    private Paint outCirclePaint;

    public JoystickView(Context context)
    {
        super(context);
        getHolder().addCallback(this);
    }

    public JoystickView(Context context, AttributeSet attributes, int style)
    {
        super(context, attributes, style);
        getHolder().addCallback(this);
    }

    public JoystickView (Context context, AttributeSet attributes)
    {
        super(context, attributes);
        getHolder().addCallback(this);
    }

    /**
     * Handle touch events
     * @param event the motion event
     * @return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int x = (int) event.getX();
                int y = (int) event.getY();
                int dist = calcDistance(x, y, xOutCircle, yOutCircle);

                if (dist <= radiusOut){
                    // Movement in the circle
                    xInCircle = x;
                    yInCircle = y;
                } else {
                    // Movement out of the circle
                    double angle = getAngle(x, y);
                    int x1 = (int) (radiusOut * Math.cos(angle));
                    int y1 = (int) (radiusOut *  Math.sin(angle));

                    xInCircle = xOutCircle - x1;

                    if (y < yOutCircle){
                        yInCircle = yOutCircle - y1;

                    } else {
                        yInCircle = yOutCircle + y1;
                    }

                }
                drawJoystick();
                break;
            }
            case MotionEvent.ACTION_UP:
                xInCircle = xOutCircle;
                yInCircle = yOutCircle;
                drawJoystick();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    /**
     * Get the elevator value
     * @return value
     */
    public double getElevator(){
        return (yOutCircle - yInCircle) / (double) radiusOut;
    }

    /**
     * Get aileron value
     * @return value
     */
    public double getAileron(){
        return (xInCircle - xOutCircle) / (double) radiusOut;
    }

    /**
     * On view created
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // Set up values
        initialize();

        // Draw the joystick
        drawJoystick();
    }

    /**
     * Set up dimensions
     */
    public void initialize(){
        // Set up centers
        xOutCircle =  getWidth() / 2;;
        yOutCircle = getHeight() / 2;

        xInCircle = xOutCircle;
        yInCircle = yOutCircle;

        // Radius
        radiusOut = Math.min(getWidth(), getHeight()) / 3;;
        radiusIn = Math.min(getWidth(), getHeight()) / 5;

        // Colors
        inCirclePaint = new Paint();
        inCirclePaint.setAntiAlias(true);
        inCirclePaint.setColor(getResources().getColor(R.color.joystickIn));
        inCirclePaint.setStyle(Paint.Style.FILL);

        outCirclePaint = new Paint();
        outCirclePaint.setAntiAlias(true);
        outCirclePaint.setColor(getResources().getColor(R.color.joystickOut));
        outCirclePaint.setStyle(Paint.Style.FILL);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) { }

    /**
     * Draw the joystick
     */
    private void drawJoystick()
    {
        if(getHolder().getSurface().isValid())
        {
            // Get canvas
            Canvas canvas = this.getHolder().lockCanvas();

            // Clear canvas
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            // Draw out circle
            canvas.drawCircle(xOutCircle, yOutCircle, radiusOut, outCirclePaint);

            // Draw in circle
            canvas.drawCircle(xInCircle, yInCircle, radiusIn, inCirclePaint);

            // Draw to surface
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Calculate euclidean distance
     * @param x1 x 1
     * @param y1 y 1
     * @param x2 x 2
     * @param y2 y 2
     * @return the distance
     */
    private int calcDistance(
            int x1,
            int y1,
            int x2,
            int y2) {
        return (int) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    /**
     * Get the move angle
     * @param x current x
     * @param y current y
     * @return the angle in radians
     */
    private double getAngle(int x, int y){
        double dist = calcDistance(xOutCircle, yOutCircle, x, y);
        return Math.acos((xOutCircle - x) / dist);
    }
}
