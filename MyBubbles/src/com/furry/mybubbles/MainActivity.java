package com.furry.mybubbles;

import com.furry.mybubbles.R;

import android.support.v7.app.ActionBarActivity;


import android.app.ActionBar;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.os.Build;
import android.util.Log;


public class MainActivity extends ActionBarActivity implements MainMenuFragment.OnMainMenuInteractionListener,
	BubblesFragment.OnBubblesFragmentInteractionListener{
	private MainMenuFragment mainMenuFragment;
	private BubblesFragment bubblesFragment;
	private FragmentManager fragmentManager;
	private Integer backStackValue=0;
	public static String LOG_TAG="myBubbles";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(LOG_TAG, "Entrering on create for MainActiviy");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragmentManager=this.getFragmentManager();
		mainMenuFragment=MainMenuFragment.newInstance("empty", "empty");
		bubblesFragment=BubblesFragment.newInstance("empty", "empty");
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, mainMenuFragment).commit();
		}


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	public void onMainMenuInteraction(Uri uri){
		Log.d(LOG_TAG, "Interaction propagated from MainMenuFragment");
	}
	
	public void onMainMenuPlayButtonClick(View button){
		Log.d(LOG_TAG, "play button click propagated from MainMenuFragment");
		fragmentManager.beginTransaction()
			.setCustomAnimations(R.animator.main_menu_flip_right_in, R.animator.main_menu_flip_right_out,R.animator.main_menu_flip_left_in,R.animator.main_menu_flip_left_out)
			//.setCustomAnimations(R.animator.main_menu_flip_left_in, R.animator.main_menu_flip_left_in)
			.replace(R.id.container, bubblesFragment)
			.addToBackStack(null)
			.commit();
		
	}
	
	public void onBubblesFragmentInteraction(Uri uri){
		Log.d(LOG_TAG, "Interaction propagated from BubblesFragment");
	}
	
	@Override
    public void onBackPressed() {
		Log.d(LOG_TAG, "BackStack value: "+fragmentManager.getBackStackEntryCount());
		if (fragmentManager.getBackStackEntryCount()>0) {
			fragmentManager.popBackStack();
		}
		else
			super.onBackPressed();
			
		
    }

}
