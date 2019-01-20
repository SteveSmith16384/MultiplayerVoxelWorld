package com.scs.samescreentowerdefence.weapons;

import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.abilities.IAbility;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.modules.AbstractGameModule;

public abstract class AbstractMagazineGun implements IAbility {

	protected MultiplayerVoxelWorldMain game;
	protected AbstractGameModule module;
	protected ICanShoot shooter;
	protected String name;

	protected float timeUntilShoot = 0;
	protected int magazineSize;
	protected int bulletsLeftInMag;
	protected float shotInterval, reloadInterval; 

	public AbstractMagazineGun(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, String _name, ICanShoot _shooter, float shotInt, float reloadInt, int magSize) {
		super();

		game = _game;
		module = _module;
		name = _name;
		shooter = _shooter;
		this.shotInterval = shotInt;
		this.reloadInterval = reloadInt;
		this.magazineSize = magSize;

		this.bulletsLeftInMag = this.magazineSize;
	}


	public abstract void launchBullet(MultiplayerVoxelWorldMain _game, AbstractGameModule _module, ICanShoot _shooter);


	@Override
	public final boolean activate(float interpol) {
		if (this.timeUntilShoot <= 0 && bulletsLeftInMag > 0) {
			this.launchBullet(game, module, shooter);
			timeUntilShoot = this.shotInterval;
			bulletsLeftInMag--;
			return true;
		}
		return false;
	}


	@Override
	public boolean process(float interpol) {
		if (this.bulletsLeftInMag <= 0) {
			// Reload
			this.bulletsLeftInMag = this.magazineSize;
			this.timeUntilShoot += this.reloadInterval;
		}
		timeUntilShoot -= interpol;
		return false;
	}


	@Override
	public String getHudText() {
		if (this.bulletsLeftInMag == this.magazineSize && this.timeUntilShoot > shotInterval) {
			return name + " RELOADING";
		} else {
			return name + " (" + this.bulletsLeftInMag + "/" + this.magazineSize  +")";
		}
	}

}
