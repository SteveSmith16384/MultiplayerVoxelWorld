package com.scs.splitscreenfpsengine;

import java.util.Iterator;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.scs.splitscreenfpsengine.entities.AbstractPhysicalEntity;
import com.scs.splitscreenfpsengine.entities.AbstractPlayersAvatar;

public class CameraSystem {

	public enum View {First, Third, TopDown, Cinema };

	private SplitScreenFpsEngine game;
	private AbstractPlayersAvatar avatar;
	private Camera cam;
	
	private float followDist = 1f;
	private float shoulderAngleRads = 0f;
	private float fixedHeight = -1;
	private float heightOffset1stPerson;
	private float heightOffset3rdPerson;
	private boolean camInCharge;
	private View currentView = View.Third;
	
	// Temp vars
	private Vector3f dirTmp = new Vector3f();
	private Vector3f lookAtTmp = new Vector3f();
	private Vector3f avatarPosTmp = new Vector3f();

	public CameraSystem(SplitScreenFpsEngine _game, Camera _cam, AbstractPlayersAvatar _avatar) {
		game = _game;
		cam = _cam;
		avatar = _avatar;
	}


	public void setupCam(float dist, float angleRads, boolean _camInCharge, float _heightOffset1stPerson, float _heightOffset3rdPerson) { 
		this.followDist = dist;
		shoulderAngleRads = angleRads;
		camInCharge = _camInCharge;
		heightOffset1stPerson = _heightOffset1stPerson;
		heightOffset3rdPerson = _heightOffset3rdPerson; // todo - warn if setup() not called
	}


	public void process(float tpfSecs) {
		if (this.currentView == View.First) {
			// Position camera at node
			Vector3f vec = avatar.getMainNode().getWorldTranslation();
			cam.getLocation().x = vec.x;
			cam.getLocation().y = vec.y + heightOffset1stPerson;
			cam.getLocation().z = vec.z;

			if (!camInCharge) { // Need for Stock Car
				Vector3f dir = avatar.getMainNode().getWorldRotation().getRotationColumn(2, dirTmp);
				dir.y = cam.getLocation().y;
				lookAtTmp.set(avatar.getMainNode().getWorldTranslation());
				lookAtTmp.addLocal(dir);
				cam.lookAt(lookAtTmp, Vector3f.UNIT_Y);
			}
		
		} else if (this.currentView == View.Cinema) {
			Vector3f pos = avatar.getMainNode().getWorldTranslation();
			cam.lookAt(pos, Vector3f.UNIT_Y);
			cam.getLocation().x = (int)(pos.x / 25) * 25;
			cam.getLocation().y = (int)(pos.y / 20) * 20 + 5;
			cam.getLocation().z = (int)(pos.z / 25) * 25;
		} else {
			avatarPosTmp.set(avatar.getMainNode().getWorldTranslation()); 
			avatarPosTmp.y += heightOffset3rdPerson;
			if (this.currentView == View.Third) {
				if (camInCharge) {
					dirTmp = cam.getDirection().mult(-1, dirTmp);
				} else {
					dirTmp = avatar.getMainNode().getWorldRotation().getRotationColumn(2).mult(-1, dirTmp);
				}
				if (shoulderAngleRads != 0) {
					Quaternion rotQ = new Quaternion();
					rotQ.fromAngleAxis(shoulderAngleRads, Vector3f.UNIT_Y);
					rotQ.multLocal(dirTmp).normalizeLocal();
				}
			} else if (this.currentView == View.TopDown) {
				//dirTmp = avatar.getMainNode().getWorldRotation().getRotationColumn(2).mult(-1, dirTmp);
				dirTmp.set(0, 1, 0);
			}

			Ray r = new Ray(avatarPosTmp, dirTmp);
			if (this.currentView == View.Third) {
				r.setLimit(followDist);
			} else if (this.currentView == View.TopDown) {
				r.setLimit(10f);
			}
			CollisionResults res = new CollisionResults();
			int c = game.getRootNode().collideWith(r, res);
			boolean found = false;
			if (c > 0) {
				Iterator<CollisionResult> it = res.iterator();
				while (it.hasNext()) {
					CollisionResult col = it.next();
					if (col.getDistance() > r.getLimit()) { // Keep this in! collideWith() seems to ignore it.
						break;
					}
					Spatial s = col.getGeometry();
					while (s.getUserData(Settings.ENTITY) == null) {
						s = s.getParent();
						if (s == null) {
							break;
						}
					}
					if (s != null && s.getUserData(Settings.ENTITY) != null) {
						AbstractPhysicalEntity pe = (AbstractPhysicalEntity)s.getUserData(Settings.ENTITY);
						if (pe != avatar) {
							float dist = col.getDistance();
							if (dist > 0.1f) { // Move cam forward slightly
								dist -= 0.1f;
							}
							Vector3f add = dirTmp.multLocal(dist);
							cam.setLocation(avatarPosTmp.addLocal(add));
							found = true;
							break;
						}
					}
				}
			}

			if (!found) {
				Vector3f add = dirTmp.multLocal(r.limit);
				cam.setLocation(avatarPosTmp.addLocal(add));
			}

			if (fixedHeight > 0) {
				cam.getLocation().y = fixedHeight;
			}

			if (!camInCharge || this.currentView == View.TopDown) {
				cam.lookAt(avatar.getMainNode().getWorldTranslation(), Vector3f.UNIT_Y);
			}

		}

		cam.update();
	}


	public void setView(View v) {
		currentView = v;
	}

}
