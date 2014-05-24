package com.furry.mybubbles;
import java.util.Map;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import com.furry.mybubbles.BubbleView.BubbleType;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;

import java.util.Random;

/***
 * 
 * @author sherman1
 *
 * @class BubbleViewFactory
 * 
 * @brief This class implements the factory pattern for generating "Bubble" objects
 * 
 */
public class BubbleViewFactory extends BasePooledObjectFactory<BubbleView> {
	
	// Activity in which objects are created
	private Activity mContext;
	
	// Frame ViewGroup in which the objects will be displayed
	private RelativeLayout mFrame;
	
	// Map object "bubble" types to their loaded in-memory bitmaps
	private Map<BubbleView.BubbleType,Bitmap> mBitmaps;
	
	// Object interaction listener
	private BubbleView.OnBubbleInteractionListener mInteractionListener;
	
	// Random generator
	private Random r=new Random();
	
	public BubbleViewFactory(Activity mContext, RelativeLayout mFrame, 
			BubbleView.OnBubbleInteractionListener interactionListener,
			Map<BubbleType, Bitmap> mBitmaps) {
		super();
		this.mContext = mContext;
		this.mFrame = mFrame;
		this.mBitmaps = mBitmaps;
		this.mInteractionListener=interactionListener;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.pool2.BasePooledObjectFactory#create()
	 * Create object
	 */
	public BubbleView create(){
		BubbleView.BubbleType bubbleType=chooseBubbleType();
		BubbleView newBubbleView=new BubbleView(mContext,mFrame,mInteractionListener,mBitmaps.get(bubbleType),0,0);
		return newBubbleView;
	}
	
	/*
	 * Choose bubble type to create
	 */
	public BubbleView.BubbleType chooseBubbleType(){
		BubbleView.BubbleType[] values=BubbleView.BubbleType.values();
		int i=(int) Math.round(Math.random()*((double) values.length-1));
		String message="Bubble type choosen "+values[i].toString()+" index of "+Integer.toString(i);
		Log.d(MainActivity.LOG_TAG, message);
		return values[i];
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.commons.pool2.BasePooledObjectFactory#wrap(java.lang.Object)
	 */
	@Override
	public PooledObject<BubbleView> wrap(BubbleView bubbleView) {
		// TODO Auto-generated method stub
		return new DefaultPooledObject<BubbleView>(bubbleView);
	}

	@Override
	public void activateObject(PooledObject<BubbleView> p) throws Exception {
		// TODO Auto-generated method stub
		super.activateObject(p);
		p.getObject().setSpeedAndDirection(r);
		//p.getObject().start();
		p.getObject().setVisibility(View.VISIBLE);
	}

	@Override
	public void passivateObject(PooledObject<BubbleView> p) throws Exception {
		// TODO Auto-generated method stub
		super.passivateObject(p);
		p.getObject().stop(false);
		p.getObject().setVisibility(View.INVISIBLE);
	}
	
	
	
}
