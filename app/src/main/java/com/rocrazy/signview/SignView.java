package com.rocrazy.signview;

import java.io.OutputStream;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.roscrazy.signview.R;

public class SignView extends View {

    private static final String TAG = "SignView";

    //The stroke width (default will be 7 pixels).
    private int STROKE_WIDTH = 7;

    // A value is used in a lowpass filter to calculate the velocity between two points.
    private float VELOCITY_FILTER_WEIGHT = 0.2f;

    // Those of values present for : ---startPoint---previousPoint----currentPoint
    private Point previousPoint;
    private Point startPoint;
    private Point currentPoint;

    // contain the last velocity. Will be used to calculate the Stroke Width
    private float lastVelocity;

    // contain the last stroke width. Will be used to calculate the Stroke Width
    private float lastWidth;

    // The paint will be used to drawing the line
    private Paint paint;

    // We 'll draw lines to this bitmap.
    private Bitmap bmp;

    // This paint will be used to draw the bitmap @bmp to view's canva
    private Paint paintBm;

    // The Canvas which is used to draw line and contain data to the bitmap @bmp
    private Canvas canvasBmp;


    public SignView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
        this.setDrawingCacheEnabled(true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SignView);

        // Get the STROKE_WIDTH and VELOCITY_FILTER_WEIGHT from xml
        if (array != null) {
            STROKE_WIDTH = array.getDimensionPixelSize(R.styleable.SignView_strokeSize, STROKE_WIDTH);
            VELOCITY_FILTER_WEIGHT = array.getFloat(R.styleable.SignView_filterWeight, VELOCITY_FILTER_WEIGHT);
        }
        array.recycle();
        init();
    }

    public SignView(Context context) {
        super(context);
        this.setWillNotDraw(false);
        this.setDrawingCacheEnabled(true);
        init();

    }

    /**
     * This method is used to init the paints.
     */
    public void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBm = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(Color.BLACK);


        paintBm.setAntiAlias(true);
        paintBm.setStyle(Paint.Style.STROKE);
        paintBm.setStrokeJoin(Paint.Join.ROUND);
        paintBm.setStrokeCap(Paint.Cap.ROUND);
        paintBm.setStrokeWidth(STROKE_WIDTH);
        paintBm.setColor(Color.BLACK);

        paintBm.setAlpha(100);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        /**
         * Recreate the bitmap when the layout has changed.
         * Note : after recreate the bitmap, all drawing will be gone.
         */
        if (bmp == null) {
            bmp = Bitmap.createBitmap(right - left, bottom - top, Bitmap.Config.ARGB_8888);
            canvasBmp = new Canvas(bmp);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // In Action down  currentPoint, previousPoint, startPoint will be set at the same point.
                currentPoint = new Point(event.getX(), event.getY(), System.currentTimeMillis());
                previousPoint = currentPoint;
                startPoint = previousPoint;
                break;

            case MotionEvent.ACTION_MOVE:
                // Those of values present for : ---startPoint---previousPoint----currentPoint-----
                startPoint = previousPoint;
                previousPoint = currentPoint;
                currentPoint = new Point(event.getX(), event.getY(), System.currentTimeMillis());

                // Calculate the velocity between the current point to the previous point
                float velocity = currentPoint.velocityFrom(previousPoint);

                // A simple lowpass filter to mitigate velocity aberrations.
                velocity = VELOCITY_FILTER_WEIGHT * velocity + (1 - VELOCITY_FILTER_WEIGHT) * lastVelocity;

                // Caculate the stroke width based on the velocity
                float strokeWidth = getStrokeWidth(velocity);


                // Draw line to the canvasBmp canvas.
                drawLine(canvasBmp, paint, lastWidth, strokeWidth);

                // Tracker the velocity and the stroke width
                lastVelocity = velocity;
                lastWidth = strokeWidth;

                break;
            case MotionEvent.ACTION_UP:
                startPoint = previousPoint;
                previousPoint = currentPoint;
                currentPoint = new Point(event.getX(), event.getY(), System.currentTimeMillis());
                drawLine(canvasBmp, paint, lastWidth, 0);
                break;
            default:
                break;
        }

        invalidate();
        return true;
    }


    private float getStrokeWidth(float velocity) {
        return STROKE_WIDTH - velocity;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bmp, 0, 0, paintBm);
    }


    // Generate mid point values
    private Point midPoint(Point p1, Point p2) {
        return new Point((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2, (p1.time + p2.time) / 2);
    }

    private void drawLine(Canvas canvas, Paint paint, float lastWidth, float currentWidth) {
        Point mid1 = midPoint(previousPoint, startPoint);
        Point mid2 = midPoint(currentPoint, previousPoint);
        draw(canvas, mid1, previousPoint, mid2, paint, lastWidth, currentWidth);
    }


    /**
     * This method is used to draw a smooth line. It follow "Bézier Curve" algorithm (it's Quadratic curves).
     * </br> For reference, you can see more detail here: <a href="http://en.wikipedia.org/wiki/B%C3%A9zier_curve">Wiki</a>
     * </br> We 'll draw a  smooth curves from three points. And the stroke size will be changed depend on the start width and the end width
     *
     * @param canvas : we 'll draw on this canvas
     * @param p0 the start point
     * @param p1 mid point
     * @param p2 end point
     * @param paint the paint is used to draw the points.
     * @param lastWidth start stroke width
     * @param currentWidth end stroke width
     */
    private void draw(Canvas canvas, Point p0, Point p1, Point p2, Paint paint, float lastWidth, float currentWidth) {
        float xa, xb, ya, yb, x, y;
        float different = (currentWidth - lastWidth);

        for (float i = 0; i < 1; i += 0.01) {


            // This block of code is used to calculate next point to draw on the curves
            xa = getPt(p0.x, p1.x, i);
            ya = getPt(p0.y, p1.y, i);
            xb = getPt(p1.x, p2.x, i);
            yb = getPt(p1.y, p2.y, i);

            x = getPt(xa, xb, i);
            y = getPt(ya, yb, i);
            //

            // reset strokeWidth
            paint.setStrokeWidth(lastWidth + different * (i));
            canvas.drawPoint(x, y, paint);
        }
    }


    // This method is used to calculate the next point cordinate.
    private float getPt(float n1, float n2, float perc) {
        float diff = n2 - n1;
        return n1 + (diff * perc);
    }


    /**
     * This method is used to save the bitmap to an output stream
     * @param outputStream
     */
    public void save(OutputStream outputStream) {
        Bitmap bitmap = getDrawingCache();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
        }
    }

}
