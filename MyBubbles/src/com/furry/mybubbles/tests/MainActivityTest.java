package com.furry.mybubbles.tests;

import com.furry.mybubbles.MainActivity;
import com.furry.mybubbles.R;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.MediumTest;
import android.widget.RelativeLayout;

/**
 * 
 * @author sherman1
 *
 * @brief testing of the MyBubbles activity
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{
	
	// The activity
	private MainActivity mActivity;
	
	//Layout frame for the main menu fragment
	private RelativeLayout mMainMenuFrame;
	
	//Layout frame for the bubbles surface
	private RelativeLayout mBubblesFrame;
	
	public MainActivityTest(){
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		//acquire handles (references)
		mActivity=getActivity();
		mMainMenuFrame=(RelativeLayout)mActivity.findViewById(R.id.main_menu_frame);
		
	}
	
	/**
	 * Test for correct setup of handles
	 */
	public void testPreconditions() {
	    assertNotNull("MainActivity is null", mActivity);
	    assertNotNull("Main menu frame is null", mMainMenuFrame);
	}
	
	@MediumTest
	public void testMainMenuClickBehavior(){
		TouchUtils.clickView(this, mMainMenuFrame);
		mBubblesFrame=(RelativeLayout)mActivity.findViewById(R.id.frame);
		assertNotNull("Bubbles frame is null", mBubblesFrame);
		TouchUtils.tapView(this, mBubblesFrame);
		//TouchUtils.clickView(this, mBubblesFrame);
		//TouchUtils.clickView(this, mBubblesFrame);
		//assertTrue(mBubblesFrame.getChildCount()>0);
	}
}
