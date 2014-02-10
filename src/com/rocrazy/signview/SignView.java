package com.rocrazy.signview;
import java.io.OutputStream;

import com.hanbiro.signview.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class SignView extends LinearLayout{

	private static final String TAG = "SignView";
	private int STROKE_WIDTH = 7;
	private float VELOCITY_FILTER_WEIGHT = 0.2f;
	
	private Point previousPoint;
	private Point startPoint;
	private Point currentPoint;
	private float lastVelocity;
	private float lastWidth;
	private Paint paint;
	private Paint paintBm;
	private Bitmap bmp;
	private Canvas canvasBmp;

	public SignView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setWillNotDraw(false);
		this.setDrawingCacheEnabled(true);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SignView);
		if(array != null){
			STROKE_WIDTH = array.getDimensionPixelSize(R.styleable.SignView_strokeSize, STROKE_WIDTH);
			VELOCITY_FILTER_WEIGHT = array.getFloat(R.styleable.SignView_filterWeight, 0.2f);
		}
		init();
	}

	public SignView(Context context) {
		super(context);
		this.setWillNotDraw(false);
		this.setDrawingCacheEnabled(true);
		init();
		
	}

	public void init(){
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintBm = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(STROKE_WIDTH);
		paint.setColor(Color.BLACK);
		//paint.setAlpha(100);

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
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		if(bmp == null){
			bmp = Bitmap.createBitmap(right-left,bottom-top,Bitmap.Config.ARGB_8888);
			canvasBmp = new Canvas(bmp);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//printSamples(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			currentPoint = new Point(event.getX(), event.getY(), System.currentTimeMillis());
			previousPoint = currentPoint;
			startPoint = previousPoint;
			break;

		case MotionEvent.ACTION_MOVE:
			startPoint = previousPoint;
			previousPoint = currentPoint;
			currentPoint = new Point(event.getX(), event.getY(), System.currentTimeMillis());

			float velocity = currentPoint.velocityFrom(previousPoint);

			// A simple lowpass filter to mitigate velocity aberrations.
			velocity = VELOCITY_FILTER_WEIGHT * velocity + (1 - VELOCITY_FILTER_WEIGHT) * lastVelocity;

			float strokeWidth = getStrokeWidth(velocity);
			drawLine(canvasBmp, paint, lastWidth, strokeWidth);
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
		return true;// super.onTouchEvent(event);
	}


	private float getStrokeWidth(float velocity){
		return STROKE_WIDTH - velocity;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(bmp, 0, 0, paintBm);
	}


	private void drawLine(Canvas canvas, Paint paint, float lastWidth, float currentWidth){
		Point mid1 = midPoint(previousPoint, startPoint);
		Point mid2 = midPoint(currentPoint, previousPoint);
		draw(canvas, mid1, previousPoint, mid2, paint, lastWidth, currentWidth);
	}

	private float getPt( float n1 , float n2 , float perc ){
		float diff = n2 - n1;
		return n1 + ( diff * perc );
	}    


	private void draw(Canvas canvas, Point p0, Point p1, Point p2, Paint paint, float lastWidth, float currentWidth){
		float xa, xb, ya, yb, x, y;
		float different = (currentWidth - lastWidth);

		for( float i = 0 ; i < 1 ; i += 0.01 )
		{

			xa = getPt( p0.x , p1.x , i );
			ya = getPt( p0.y , p1.y , i );
			xb = getPt( p1.x , p2.x , i );
			yb = getPt( p1.y , p2.y , i );

			x = getPt( xa , xb , i );
			y = getPt( ya , yb , i );

			paint.setStrokeWidth(lastWidth + different * (i));
			canvas.drawPoint(x , y , paint );
		}
	}



	private Point  midPoint(Point p1, Point p2)
	{
		return new Point((p1.x + p2.x) / 2.0f ,  (p1.y + p2.y) * 0.5f, (p1.time + p2.time) / 2);
	}
	
	public void save(OutputStream outputStream){
		Bitmap bitmap = getDrawingCache();
		if(bitmap != null){
			bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
		}
	}

}