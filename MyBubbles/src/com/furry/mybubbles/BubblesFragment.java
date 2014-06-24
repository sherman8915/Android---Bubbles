package com.furry.mybubbles;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.widget.Toast;

import java.util.Map;
import java.util.Hashtable;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

import com.furry.mybubbles.R;



/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link BubblesFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link BubblesFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */

/**
 * 
 * @author sherman8915
 *
 * This fragment class serves 2 purposes:
 * 1. It is a template pattern implementation that forms a bridge pattern to wrap around the game strategy.
 * 2. Responsible for handling the low level details of initializing and releasing resources (e.g. bitmaps and sounds)
 * 
 * 
 */
public class BubblesFragment extends Fragment{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "gameStrategy";
	
	
	//Game strategy to be used in the fragment game interaction calls
	private GameStrategy mGameStrategy;
	
	// TODO: Rename and change types of parameters
	private String mParamGameStrategy;
	

	
	private RelativeLayout mFrame;
	private Activity mContext;
	// Gesture Detector
	private GestureDetector mGestureDetector;
	
	// AudioManager
	private AudioManager mAudioManager;
	// SoundPool
	private SoundPool mSoundPool;
	// ID for the bubble popping sound
	private int mSoundID;
	// Audio volume
	private float mStreamVolume;
	
	private OnBubblesFragmentInteractionListener mListener;
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment BubblesFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static BubblesFragment newInstance(String gameStrategy) {
		BubblesFragment fragment = new BubblesFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, gameStrategy);
		fragment.setArguments(args);
		return fragment;
	}

	public BubblesFragment() {
		// Required empty public constructor
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		mFrame = (RelativeLayout) this.getActivity().findViewById(R.id.frame);
		mContext=this.getActivity();

	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParamGameStrategy = getArguments().getString(ARG_PARAM1);
			
			
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//setup gesture detector for the fragment
		setupGestureDetector();
		
		// Inflate the layout for this fragment
		View v=inflater.inflate(R.layout.fragment_bubbles, container, false);
		v.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//Log.d(MainActivity.LOG_TAG, "onTouch in BubblesFragment Frame");
				return mGestureDetector.onTouchEvent(event);
			}
		});

		return v;
	}



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(MainActivity.LOG_TAG, "BubblesFragment onAttach()");
		try {
			mListener = (OnBubblesFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		// Manage bubble popping sound
		// Use AudioManager.STREAM_MUSIC as stream type
		BubblesFragment.this.mFrame.setVisibility(ViewGroup.INVISIBLE);
		mAudioManager = (AudioManager) this.getActivity().getSystemService(Activity.AUDIO_SERVICE);

		mStreamVolume = (float) mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC)
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		// TODO - make a new SoundPool, allowing up to 10 streams 
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

		// TODO - set a SoundPool OnLoadCompletedListener that setup the game strategy with the initialized sound pool
		mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,int status) {
				if (0 == status) {
					//setupGestureDetector();
					BubblesFragment.this.updateGameStrategy();
					BubblesFragment.this.mGameStrategy.initialize();
					BubblesFragment.this.mFrame.setVisibility(ViewGroup.VISIBLE);

				}
			}
		});
				
		// TODO - load the sound from res/raw/bubble_pop.wav
		mSoundID = mSoundPool.load(this.getActivity(), R.raw.bubble_pop, 1);
		

				
	}
	

	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(MainActivity.LOG_TAG, "BubblesFragment onDetach()");
		mListener = null;
	}

	@Override
	public void onPause() {
		
		// TODO - Release all SoundPool resources
		if (null != mSoundPool) {
			mSoundPool.unload(mSoundID);
			mSoundPool.release();
			mSoundPool = null;
		}
		mAudioManager.setSpeakerphoneOn(false);
		mAudioManager.unloadSoundEffects();

		
		super.onPause();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d(MainActivity.LOG_TAG, this.toString()+" onDestroy()");
	}
	

	

	
	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnBubblesFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onBubblesFragmentInteraction(Uri uri);
	}
	
	/**
	 * Updates the game strategy
	 */
	private void updateGameStrategy(){
		//Setup the game strategy
		if (BubblesFragment.this.mParamGameStrategy.equals(GameStrategyBase.strategyName))
			mGameStrategy=new GameStrategyBase(mContext,mFrame,mSoundPool,mStreamVolume,mSoundID);
		else if (BubblesFragment.this.mParamGameStrategy.equals(GameStrategyCountdown.strategyName))
			mGameStrategy=new GameStrategyCountdown(mContext,mFrame,mSoundPool,mStreamVolume,mSoundID);
		else
			mGameStrategy=new GameStrategyBase(mContext,mFrame,mSoundPool,mStreamVolume,mSoundID);
	}
	
	/**
	 * Setup the gestures detector for the fragments frame
	 */
	private void setupGestureDetector() {

		mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
		
			// If a fling gesture starts on a BubbleView then change the
			// BubbleView's velocity

			   @Override
               public boolean onDown(MotionEvent e) {
				   //Log.d(MainActivity.LOG_TAG, "onDown in BubblesFragment Frame");
                   return true;
               }
			   
			@Override
			public boolean onFling(MotionEvent event1, MotionEvent event2,float velocityX, float velocityY) {
				
				Log.d(MainActivity.LOG_TAG, "onFling in BubblesFragment Frame");
				// TODO - Implement onFling actions.
				// You can get all Views in mFrame using the
				// ViewGroup.getChildCount() method
				Boolean intersected=false;
				for (int i=0;i<mFrame.getChildCount();i++){
					BubbleView childBubbleView= (BubbleView) mFrame.getChildAt(i);
					if (childBubbleView.intersects(event1.getX(), event1.getY())){
						childBubbleView.deflect(velocityX, velocityY);
						intersected=true;
					}
				}
				
				
				if (intersected)
					return true;
				else
					return false;
				
			}

			// If a single tap intersects a BubbleView, then pop the BubbleView
			// Otherwise, create a new BubbleView at the tap's location and add
			// it to mFrame. You can get all views from mFrame with ViewGroup.getChildAt()

			@Override
			public boolean onSingleTapConfirmed(MotionEvent event) {
				//Call delegated to the game strategy assigned to the fragment
				BubblesFragment.this.mGameStrategy.singleTapBehavior(event.getX(),event.getY());
				
				return true;
			}
		});
	}
	

		
		
}
