package com.scs.splitscreentowerdefence.entities;

import com.jme3.asset.TextureKey;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import com.scs.splitscreenfpsengine.SplitScreenFpsEngine;
import com.scs.splitscreenfpsengine.components.ICanShoot;
import com.scs.splitscreenfpsengine.components.IProcessable;
import com.scs.splitscreenfpsengine.entities.AbstractPhysicalEntity;
import com.scs.splitscreenfpsengine.modules.AbstractGameModule;

public class Grenade extends AbstractPhysicalEntity implements IProcessable  {//implements ICausesHarm {

	public ICanShoot shooter;
	private float timeLeft = 2f;

	public Grenade(SplitScreenFpsEngine _game, AbstractGameModule _module, ICanShoot _shooter) {
		super(_game, _module, "Grenade");

		this.shooter = _shooter;

		Sphere sphere = new Sphere(8, 8, 0.1f, true, false);
		sphere.setTextureMode(TextureMode.Projected);
		Geometry ball_geo = new Geometry("cannon ball", sphere);

		TextureKey key3 = new TextureKey( "Textures/grenade.png");
		Texture tex3 = game.getAssetManager().loadTexture(key3);
		Material mat = new Material(game.getAssetManager(),"Common/MatDefs/Light/Lighting.j3md");  // create a simple material
		mat.setTexture("DiffuseMap", tex3);
		ball_geo.setMaterial(mat);

		this.mainNode.attachChild(ball_geo);
		game.getRootNode().attachChild(this.mainNode);
		/** Position the cannon ball  */
		ball_geo.setLocalTranslation(shooter.getBulletStartPosition().add(shooter.getShootDir().multLocal(shooter.getRadius())));
		/** Make the ball physical with a mass > 0.0f */
		rigidBodyControl = new RigidBodyControl(.2f);
		/** Add physical ball to physics space. */
		ball_geo.addControl(rigidBodyControl);
		/** Accelerate the physical ball to shoot it. */
		rigidBodyControl.setLinearVelocity(shooter.getShootDir().mult(15));

		rigidBodyControl.setUserObject(this);
		module.addEntity(this);

	}


	@Override
	public void process(float tpf) {
		this.timeLeft -= tpf;
		if (this.timeLeft < 0) {
			module.doExplosion(this.getLocation(), this);//, 3, 10);
			this.markForRemoval();
		}

	}


}
