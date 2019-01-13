package com.scs.multiplayervoxelworld.entities;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.scs.multiplayervoxelworld.MultiplayerVoxelWorldMain;
import com.scs.multiplayervoxelworld.Settings;
import com.scs.multiplayervoxelworld.components.IBullet;
import com.scs.multiplayervoxelworld.components.ICanShoot;
import com.scs.multiplayervoxelworld.components.ICollideable;
import com.scs.multiplayervoxelworld.components.IProcessable;
import com.scs.multiplayervoxelworld.models.BeamLaserModel;
import com.scs.multiplayervoxelworld.modules.GameModule;

public class LaserBullet extends AbstractPhysicalEntity implements IBullet, IProcessable, PhysicsTickListener {

	public ICanShoot shooter;
	private float timeLeft = 3;

	public LaserBullet(MultiplayerVoxelWorldMain _game, GameModule _module, ICanShoot _shooter) {
		super(_game, _module, "LaserBullet");

		this.shooter = _shooter;

		Vector3f origin = shooter.getLocation().clone();

		Node ball_geo = BeamLaserModel.Factory(game.getAssetManager(), origin, origin.add(shooter.getShootDir().multLocal(1)), ColorRGBA.Pink);

		this.mainNode.attachChild(ball_geo);
		game.getRootNode().attachChild(this.mainNode);
		ball_geo.setLocalTranslation(shooter.getLocation().add(shooter.getShootDir().multLocal(PlayersAvatar.PLAYER_RAD*3)));
		ball_geo.getLocalTranslation().y -= 0.1f; // Drop bullets slightly
		rigidBodyControl = new RigidBodyControl(.1f);
		ball_geo.addControl(rigidBodyControl);
		//module.bulletAppState.getPhysicsSpace().add(rigidBodyControl);
		/** Accelerate the physical ball to shoot it. */
		rigidBodyControl.setLinearVelocity(shooter.getShootDir().mult(40));
		rigidBodyControl.setGravity(Vector3f.ZERO);

		this.getMainNode().setUserData(Settings.ENTITY, this);
		ball_geo.setUserData(Settings.ENTITY, this);
		rigidBodyControl.setUserObject(this);
		module.addEntity(this);

		AudioNode audio_gun = new AudioNode(game.getAssetManager(), "Sound/laser3.wav", false);
		audio_gun.setPositional(false);
		audio_gun.setLooping(false);
		audio_gun.setVolume(2);
		this.getMainNode().attachChild(audio_gun);
		audio_gun.play();

	}


	@Override
	public void process(float tpf) {
		this.timeLeft -= tpf;
		if (this.timeLeft < 0) {
			this.markForRemoval();
		}
	}


	@Override
	public ICanShoot getShooter() {
		return shooter;
	}


	@Override
	public void collidedWith(ICollideable other) {
		if (other != this.shooter) {
			//Settings.p("Laser collided with " + other);

			CubeExplosionShard.Factory(game, module, this.getLocation(), 3);

			module.audioSmallExplode.play();

			this.markForRemoval(); // Don't bounce
		}
	}


	@Override
	public float getDamageCaused() {
		return 10;
	}


	@Override
	public void prePhysicsTick(PhysicsSpace space, float tpf) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void physicsTick(PhysicsSpace space, float tpf) {
		// TODO Auto-generated method stub
		
	}



}
