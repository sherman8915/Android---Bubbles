package com.furry.mybubbles;

import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.SoundPool;



/**
 * 
 * @author sherman1
 *
 * @brief The basic game strategy to be used in the game
 *        Starting with an empty board and user need to start tapping to fill bubble object
 *        No end conditions to the game
 */
public class GameStrategyCountdown extends GameStrategy{
	
	public static final String strategyName="Countdown Strategy";
	
	private SoundPool mSoundPool;
	
	// Application context
	private Activity mContext;
	
	//Game frame 
	private RelativeLayout mFrame;
	
	//hash map that maps Bubble types to their instantiated bitmaps
	private Map<BubbleView.BubbleType,Bitmap> mBitmaps=new Hashtable<BubbleView.BubbleType,Bitmap>();
	
	//Game objects pool
	private ObjectPool<BubbleView> mBubbleViewPool;
	
	// ID for the bubble popping sound
	private int mSoundID;
	// Audio volume
	private float mStreamVolume;
	
	// Number of starting bubbles on the screen
	private static final int STARTING_BUBBLES_NUM = 5;
	
	private static final CharSequence WIN_MESSAGE = "You Win!!!";
	/**
	 * @brief This game class implements game that requires the user to pop all bubbles from screen.
	 *        When successful the game ends. If a blank spot on the screen is clicked accidently, new bubble is added
	 *        
	 * @param context
	 * @param frame
	 * @param soundPool
	 * @param streamVolume
	 * @param soundID
	 */
	public GameStrategyCountdown(
			Activity context,
			RelativeLayout frame,
			SoundPool soundPool,
			float streamVolume,
			int soundID){

		mContext=context;
		mFrame=frame;
		mSoundPool=soundPool;
		mSoundID=soundID;
		mStreamVolume=streamVolume;
		
		Bitmap mBitmap;
		for (BubbleView.BubbleType t: BubbleView.BubbleType.values()){
			mBitmap = BitmapFactory.decodeResource(mContext.getResources(), t.getDrawHandle());
			mBitmaps.put(t, mBitmap);
		}

		BubbleViewFactory mFactory=new BubbleViewFactory(mContext,mFrame,this,mBitmaps);
		new GenericObjectPool<BubbleView>(mFactory);
		mBubbleViewPool=new GenericObjectPool<BubbleView>(mFactory);

	}
	
	@Override
	public void initialize(){
		try {
			for (int i=0;i<STARTING_BUBBLES_NUM;i++)
				mBubbleViewPool.addObject();

		} catch (Exception e){
			throw new RuntimeException("Unable to borrow bubble from pool" + e.toString());
		}
		distributeBubbles();

	}
	
	@Override
	public void singleTapBehavior(float x,float y){
		Log.d(MainActivity.LOG_TAG, "onSingleTapConfirmed in BubblesFragment Frame");
		// TODO - Implement onSingleTapConfirmed actions.
		// You can get all Views in mFrame using the
		// ViewGroup.getChildCount() method
		Boolean popped=false;
		for (int i=0;i<mFrame.getChildCount();i++){
			BubbleView childBubbleView= (BubbleView) mFrame.getChildAt(i);
			if (childBubbleView.isShown() && childBubbleView.intersects(x, y)){
				popped=true;
				mSoundPool.play(mSoundID, mStreamVolume, mStreamVolume, 1, 0, 1.0f);
				childBubbleView.stop(popped);

			}
		}
		if (!popped){
			addBubbleOnFrame(x,y);
		}
	}
	
	@Override
	public BubbleView.BubbleType chooseBubbleType(){
		BubbleView.BubbleType[] values=BubbleView.BubbleType.values();
		int i=(int) Math.round(Math.random()*((double) values.length-1));
		String message="Bubble type choosen "+values[i].toString()+" index of "+Integer.toString(i);
		Log.d(MainActivity.LOG_TAG, message);
		return values[i];
	}
	
	@Override
	public void stopBubble(BubbleView v){
		mFrame.post(new MyPostRunnable(v,BubbleView.Action.STOP));
		
	}
	
	/**
	 * Add new bubble object on frame
	 * @param x - x coordinate
	 * 		  y - y coordinate
	 */
	private void addBubbleOnFrame(float x,float y){
		BubbleView newBubble=null;
		try{
			newBubble=mBubbleViewPool.borrowObject();
			
		} catch (Exception e){
			throw new RuntimeException("Unable to borrow buffer from pool" + e.toString());
		}
		newBubble.setPosition(x, y);
		mFrame.addView(newBubble);
		newBubble.start();
	}
	
	/**
	 * Tests for victory conditions
	 */
	private boolean testVictory(){
		//Log.d(MainActivity.LOG_TAG, "Bubbles remaining: "+(mFrame.getChildCount()));
		if (mFrame.getChildCount()==0){
			//Post victory message for UI
			Log.d(MainActivity.LOG_TAG, "Victory achieved!!!");
			return true;

		}
		return false;
	}
	
	@Override
	public void updateSoundPool(SoundPool soundPool){
		this.mSoundPool=soundPool;
	}
	
	/**
	 * Distributes the bubbles on screen
	 */
	private void distributeBubbles(){
		Random r=new Random();
		for (int i=0;i<STARTING_BUBBLES_NUM;i++){
			this.addBubbleOnFrame(
					mFrame.getWidth()/8+r.nextFloat()*mFrame.getWidth()*5/8,
					mFrame.getHeight()/8+r.nextFloat()*mFrame.getHeight()*5/8
					);
		}

	}
	/**
	 * 
	 * @brief Runnable class that posts code to UI to act on a specific object
	 *
	 */
	private class MyPostRunnable implements Runnable{
		
		BubbleView mBubble;
		BubbleView.Action mAction;
		
		//construct runnable with the bubble to remove
		public MyPostRunnable(BubbleView bubble,BubbleView.Action action){
			mBubble=bubble;
			mAction=action;
		}
		
		@Override
		public void run() {
			
			switch (mAction) {
			case STOP:
				// Remove the BubbleView from mFrame					
				mFrame.removeView(mBubble);
				try {
					mBubbleViewPool.returnObject(mBubble);
				} catch (Exception e){
					throw new RuntimeException("Unable to return bubble to pool" + e.toString());
				}
				if (GameStrategyCountdown.this.testVictory()){
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(GameStrategyCountdown.this.mContext, WIN_MESSAGE, duration);
					toast.show();
					GameStrategyCountdown.this.mContext.onBackPressed();
				}
				
			}
		
		}
	}
}
