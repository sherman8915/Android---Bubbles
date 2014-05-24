package com.furry.mybubbles;

import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RelativeLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;

import com.furry.mybubbles.R;

import android.util.Log;

/**
 * TODO: document your custom view class.
 */
public class BubbleView extends View {
	public final static int RANDOM = 0;
	public final static int SINGLE = 1;
	public final static int STILL = 2;
	public final static int FIXED_SIZE = 3;
	public final static int RANDOM_SIZE = 4;
	public static int speedMode = RANDOM;
	public static int sizeMode = FIXED_SIZE;
	
	private static final int BITMAP_SIZE = 64;
	private static final int REFRESH_RATE = 40;
	
	//The types of "bubbles" 
	public static enum BubbleType{
		RED (R.drawable.red),
		BLUE (R.drawable.blue),
		YELLOWTAIL (R.drawable.yellowtail),
		GREEN (R.drawable.green),
		BLACK (R.drawable.black),
		ORANGE (R.drawable.orange),
		PURPLE (R.drawable.purple),
		WHITE (R.drawable.white),
		YELLOWBIRD (R.drawable.yellowbird),
		WHITEPANDA (R.drawable.whitepanda),
		;
		
		private final int mDrawHandle;
		BubbleType(int drawHandle){
			this.mDrawHandle=drawHandle;
		}
		public int getDrawHandle() {return this.mDrawHandle;}
		
	};
	
	private final Paint mPainter = new Paint();
	private ScheduledFuture<?> mMoverFuture;
	private int mScaledBitmapWidth;
	private Bitmap mScaledBitmap;
	private RelativeLayout mFrame;
	private Bitmap mBitmap;
	

	
	//defines the the bubble type of this instance
	private BubbleType mBubbleType;
	
	// location, speed and direction of the bubble
	private float mXPos, mYPos, mDx, mDy;
	private long mRotate, mDRotate;
	// Display dimensions
	private int mDisplayWidth, mDisplayHeight;
	private OnBubbleInteractionListener mInteractionListener;
	
	public BubbleView(Context context,RelativeLayout frame,OnBubbleInteractionListener interactionListener,Bitmap bitmap,float x, float y) {
		super(context);
		//log("Creating Bubble at: x:" + x + " y:" + y);
		
		
		mBitmap=bitmap;
		mFrame=frame;
		mInteractionListener=interactionListener;
		
		// Create a new random number generator to
		// randomize size, rotation, speed and direction
		Random r = new Random();

		// Creates the bubble bitmap for this BubbleView
		createScaledBitmap(r);

		// Adjust position to center the bubble under user's finger
		setPosition(x,y);

		// Set the BubbleView's speed and direction
		setSpeedAndDirection(r);
		
		// Set the BubbleView's rotation
		setRotation(r);

		mPainter.setAntiAlias(true);
		
	}
	
	public void setPosition(float x, float y){
		// Adjust position to center the bubble under user's finger
		mXPos = x - mScaledBitmapWidth / 2;
		mYPos = y - mScaledBitmapWidth / 2;
		mDisplayWidth = mFrame.getWidth();
		mDisplayHeight = mFrame.getHeight();
	}

	private void setRotation(Random r) {

		if (speedMode == RANDOM) {
			
			// TODO - set rotation in range [1..3]
			mDRotate = (int) (Math.random()*3+1);

			
		} else {
		
			mDRotate = 0;
		
		}
	}

	public void setSpeedAndDirection(Random r) {

		// Used by test cases
		switch (speedMode) {

		case SINGLE:

			// Fixed speed
			mDx = 10;
			mDy = 10;
			break;

		case STILL:

			// No speed
			mDx = 0;
			mDy = 0;
			break;

		default:

			// TODO - Set movement direction and speed
			// Limit movement speed in the x and y
			// direction to [-3..3].
			double randY,randX;
			randX=(1+Math.random() * 3) - (Math.random()*3+1);
			randY=(1+Math.random() * 3) - (Math.random()*3+1);
			
			mDx = (int) (Math.round(randX));
			mDy = (int) (Math.round(randY));
		
		
		
		
		
		}
	}

	private void createScaledBitmap(Random r) {

		if (sizeMode == FIXED_SIZE) {

			mScaledBitmapWidth = BITMAP_SIZE * 3;
		
		} else {
		
			//TODO - set scaled bitmap size in range [1..3] * BITMAP_SIZE
			mScaledBitmapWidth = (int) (BITMAP_SIZE * 3 * Math.random() + BITMAP_SIZE);
		
		}

		// TODO - create the scaled bitmap using size set above
		mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, mScaledBitmapWidth, mScaledBitmapWidth, false);
	}

	// Start moving the BubbleView & updating the display
	public void start() {

		// Creates a WorkerThread
		ScheduledExecutorService executor = Executors
				.newScheduledThreadPool(1);

		// Execute the run() in Worker Thread every REFRESH_RATE
		// milliseconds
		// Save reference to this job in mMoverFuture
		mMoverFuture = executor.scheduleWithFixedDelay(new Runnable() {
			Boolean out;
			@Override
			public void run() {
				// TODO - implement movement logic.
				// Each time this method is run the BubbleView should
				// move one step. If the BubbleView exits the display, 
				// stop the BubbleView's Worker Thread. 
				// Otherwise, request that the BubbleView be redrawn.
				out=BubbleView.this.moveWhileOnScreen();
				if (out){
					log("Bubble out of screen");
					BubbleView.this.stop(false);
				}
				else {
					BubbleView.this.postInvalidate();
				}
				
				
				
			}
		}, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
	}

	public synchronized boolean intersects(float x, float y) {
		
		// TODO - Return true if the BubbleView intersects position (x,y)
		float xMin=mXPos;
		float yMin=mYPos;
		float xMax=mXPos + mScaledBitmapWidth;
		float yMax=mYPos + mScaledBitmapWidth;
		if (x<xMax && x>xMin && y<yMax && y>yMin){
			return true;
		}
		return false;
	}

	// Cancel the Bubble's movement
	// Remove Bubble from mFrame
	// Play pop sound if the BubbleView was popped
	
	public void stop(final boolean popped) {
		
		// This work will be performed on the UI Thread
		if (null != mMoverFuture) {
			if (mMoverFuture.cancel(true)) {
				this.mInteractionListener.stopBubble(this);
			}
		}
			


	}

	// Change the Bubble's speed and direction
	public synchronized void deflect(float velocityX, float velocityY) {
		//log("velocity X:" + velocityX + " velocity Y:" + velocityY);

		//TODO - set mDx and mDy to be the new velocities divided by the REFRESH_RATE
		
		mDx = velocityX/REFRESH_RATE;
		mDy = velocityY/REFRESH_RATE;

	}

	// Draw the Bubble at its current location
	@Override
	protected synchronized void onDraw(Canvas canvas) {

		// TODO - save the canvas
		canvas.save();

		// TODO - increase the rotation of the original image by mDRotate
		mRotate=mRotate+mDRotate;
		

		
		// TODO Rotate the canvas by current rotation
		canvas.rotate(mRotate,mXPos + mScaledBitmapWidth/2, mYPos + mScaledBitmapWidth/2);
		
		
		
		// TODO - draw the bitmap at it's new location
		canvas.drawBitmap(mScaledBitmap, mXPos, mYPos, mPainter);

		
		// TODO - restore the canvas
		canvas.restore();

		
	}


	private synchronized boolean moveWhileOnScreen() {

		// TODO - Move the BubbleView
		// Returns true if the BubbleView has exited the screen
		this.mXPos=this.mXPos+this.mDx;
		this.mYPos=this.mYPos+this.mDy;

		
		
		return isOutOfView();

	}

	private boolean isOutOfView() {

		// TODO - Return true if the BubbleView has exited the screen
		
		if ((this.mXPos < 0) ||  
			(this.mXPos > mDisplayWidth) ||
			(this.mYPos < 0) ||  
			(this.mYPos > mDisplayHeight) 
				) {
			return true;
		}
		
		return false;
	}
	
	private static void log (String message) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Log.i(MainActivity.LOG_TAG,message);
	}
	
	public interface OnBubbleInteractionListener{
		//Implements program level handling of bubble stop (e.g. resource reallocation etc..)
		public void stopBubble(BubbleView bubble);
	}
}

