package com.furry.mybubbles;

import com.furry.mybubbles.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import android.view.GestureDetector;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link MainMenuFragment.OnMainMenuInteractionListener} interface to handle
 * interaction events. Use the {@link MainMenuFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class MainMenuFragment extends Fragment{
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	//The gesture detector for the fragment
	private GestureDetector mGestureDetector;
	
	private OnMainMenuInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment MainMenuFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static MainMenuFragment newInstance(String param1, String param2) {
		MainMenuFragment fragment = new MainMenuFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public MainMenuFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		//setup gesture detector for the fragment
		setupGestureDetector();
		
		// Inflate the layout for this fragment
		View v=inflater.inflate(R.layout.fragment_main_menu, container, false);
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

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onMainMenuInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.d(MainActivity.LOG_TAG, "MainMenuFragment onAttach()");
		try {
			mListener = (OnMainMenuInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		

		
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(MainActivity.LOG_TAG, "MainMenuFragment onDetach()");
		mListener = null;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d(MainActivity.LOG_TAG, "MainMenuFragment onDestroy()");
	}
	
	// Set up GestureDetector
	private void setupGestureDetector() {

		mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
		
			// If a fling gesture starts on a BubbleView then change the
			// BubbleView's velocity

			   @Override
               public boolean onDown(MotionEvent e) {
				   //Log.d(MainActivity.LOG_TAG, "onDown in BubblesFragment Frame");
                   return true;
               }
			   



			//If a single tab confirmed then the "play" fragment will reveal
			@Override
			public boolean onSingleTapConfirmed(MotionEvent event) {

				Log.d(MainActivity.LOG_TAG, "onSingleTapConfirmed in MainMenuFragment Frame");
				mListener.onMainMenuPlayButtonClick(null);
					
				
				
				return true;
			}
		});
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
	public interface OnMainMenuInteractionListener {
		// TODO: Update argument type and name
		public void onMainMenuInteraction(Uri uri);
		public void onMainMenuPlayButtonClick(View button);
	}

}
