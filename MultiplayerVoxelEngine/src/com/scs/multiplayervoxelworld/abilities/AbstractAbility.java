package com.scs.multiplayervoxelworld.abilities;

import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.entities.AbstractPlayersAvatar;
import com.scs.multiplayervoxelworld.modules.GameModule;

public abstract class AbstractAbility implements IAbility {
	
	protected AbstractPlayersAvatar player;
	protected MultiplayerVoxelWorldMain game;
	protected GameModule module;

	public AbstractAbility(MultiplayerVoxelWorldMain _game, GameModule _module, AbstractPlayersAvatar p) {
		super();
		
		game = _game;
		module = _module;
		player = p;
	}
	
	
	/**
	 * Override if required
	 */
	@Override
	public boolean onlyActivateOnClick() {
		return false;
	}

}