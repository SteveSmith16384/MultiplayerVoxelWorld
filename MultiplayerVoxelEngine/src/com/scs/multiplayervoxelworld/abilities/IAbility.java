package com.scs.multiplayervoxelworld.abilities;

public interface IAbility {

	/**
	 * Called every interval.  Returns whether the HUD needs updating
	 */
	boolean process(float tpfSecs);
	
	/**
	 * Called when activated
	 */
	boolean activate(float tpfSecs);
	
	String getHudText();
	
	boolean onlyActivateOnClick();
}
