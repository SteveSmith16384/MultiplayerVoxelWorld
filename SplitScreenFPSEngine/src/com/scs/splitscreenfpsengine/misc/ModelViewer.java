package com.scs.splitscreenfpsengine.misc;

import java.io.File;
import java.io.IOException;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.scs.splitscreenfpsengine.Settings;
import com.scs.splitscreenfpsengine.jme.JMEModelFunctions;
import com.studiohartman.jamepad.ControllerManager;

public class ModelViewer extends SimpleApplication implements AnimEventListener {

	private AnimControl control;
	private FilterPostProcessor fpp;

	public static void main(String[] args) {
		ModelViewer app = new ModelViewer();
		app.showSettings = false;

		app.start();
	}


	public Spatial getModel() {
		Spatial model = JMEModelFunctions.loadModel(assetManager, "Models/spider/Spider.blend", false);
		JMEModelFunctions.setTextureOnSpatial(assetManager, model, "Models/spider/Spider.png");

		//JMEModelFunctions.scaleModelToHeight(model, 2f);
		//JMEModelFunctions.setTextureOnSpatial(assetManager, model, "Models/young_lightskinned_male_diffuse.png");
		return model;
	}
	
	
	public String getAnimNode() {
		return "tshirt02 (Node)";
	}
	

	public String getAnimToShow() {
		return "WalkBaked";
	}
	

	@Override
	public void simpleInitApp() {
		cam.setFrustumPerspective(60, settings.getWidth() / settings.getHeight(), .1f, 100);

		super.getViewPort().setBackgroundColor(ColorRGBA.Black);
		
		Spatial model = this.getModel();

		if (model instanceof Node) {
			Settings.p("Listing anims:");
			JMEModelFunctions.listAllAnimations((Node)model);
			Settings.p("Finished listing anims");
			
			control = JMEModelFunctions.getNodeWithControls(getAnimNode(), (Node)model);
			if (control != null) {
				control.addListener(this);
				//Globals.p("Control Animations: " + control.getAnimationNames());
				AnimChannel channel = control.createChannel();
				try {
					channel.setAnim(getAnimToShow());
					Settings.p("Running anim '" + getAnimToShow() + "'");
				} catch (IllegalArgumentException ex) {
					Settings.pe("Try running the right anim code!");
				}
			} else {
				Settings.p("No animation control on selected node '" + getAnimNode() + "'");
			}
		}

		rootNode.attachChild(model);

		this.rootNode.attachChild(JMEModelFunctions.getGrid(assetManager, 10));

		rootNode.updateGeometricState();

		model.updateModelBound();
		BoundingBox bb = (BoundingBox)model.getWorldBound();
		Settings.p("Model w/h/d: " + (bb.getXExtent()*2) + "/" + (bb.getYExtent()*2) + "/" + (bb.getZExtent()*2));
		Settings.p("Centre at : " + bb.getCenter());

		this.flyCam.setMoveSpeed(12f);

		fpp = new FilterPostProcessor(assetManager);
		viewPort.addProcessor(fpp);
		
		//setupLight();

		//this.showOutlineEffect(model, 3, ColorRGBA.Red);

	}


	private void setupLight() {
		// Remove existing lights
		this.rootNode.getWorldLightList().clear();
		LightList list = this.rootNode.getWorldLightList();
		for (Light it : list) {
			this.rootNode.removeLight(it);
		}

		// We add light so we see the scene
		AmbientLight al = new AmbientLight();
		al.setColor(ColorRGBA.White.mult(1));
		rootNode.addLight(al);

		DirectionalLight dirlight = new DirectionalLight(); // FSR need this for textures to show
		dirlight.setColor(ColorRGBA.White.mult(1f));
		rootNode.addLight(dirlight);

	}


	@Override
	public void simpleUpdate(float tpf) {
		//System.out.println("Pos: " + this.cam.getLocation());
		//this.rootNode.rotate(0,  tpf,  tpf);

		//Globals.p("Model w/h/d: " + (bb.getXExtent()*2) + "/" + (bb.getYExtent()*2) + "/" + (bb.getZExtent()*2));
	}


	@Override
	public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {

	}


	@Override
	public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

	}

}