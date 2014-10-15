package com.cb.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressBarView extends View {
	private int progress = 100;
	private int max = 200;
	private Paint paint;
	private RectF oval;
	private int radius = 150;
	private int circleWidth = 10; 
	
	public int getCircleWidth() {
		return circleWidth;
	}

	public void setCircleWidth(int circleWidth) {
		this.circleWidth = circleWidth;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		invalidate();
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public CircleProgressBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		oval = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float width = (float)this.getWidth()/2;
		float height = (float)this.getHeight()/2;
		paint.setAntiAlias(true);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.parseColor("#99CCCCFF"));
		paint.setStrokeWidth(circleWidth);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawCircle(width,height,radius,paint);
		paint.setColor(Color.WHITE);
		oval.set(width - radius, height - radius, width + radius, height + radius);
		canvas.drawArc(oval, -90, ((float)progress/max)*360, false, paint);
		
	}
}
