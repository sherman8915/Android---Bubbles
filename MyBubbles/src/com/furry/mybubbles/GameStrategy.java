package com.furry.mybubbles;

import org.apache.commons.pool2.ObjectPool;

import com.furry.mybubbles.BubbleView.Action;

import android.view.ViewGroup;
import android.view.View;
import android.content.Context;
/**
 * 
 * @author sherman1
 *
 * @class GameStrategy
 * 
 * @brief Defines the game strategy interface, describes the behaviors of the game and objects interactions
 */
public abstract class GameStrategy {
	

	
	// Game initialization
	public abstract void initialize();
	
	/**
	 * defines the behavior of a single tap on the screen
	 * 
	 * @param x,y coordinates of the tap event
	 */
	public abstract void singleTapBehavior(float x,float y);
	
	/**
	 * Bubble type selection logic
	 */
	public abstract BubbleView.BubbleType chooseBubbleType();
	
	/**
	 * Stop Bubble behavior
	 */
	public abstract void stopBubble(BubbleView v);
	
	public GameStrategy(){
		
	}
	



}
