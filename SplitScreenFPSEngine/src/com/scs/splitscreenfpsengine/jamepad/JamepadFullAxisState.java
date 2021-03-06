package com.scs.splitscreenfpsengine.jamepad;

import java.util.HashMap;

import com.studiohartman.jamepad.ControllerAxis;
import com.studiohartman.jamepad.ControllerIndex;
import com.studiohartman.jamepad.ControllerUnpluggedException;

public class JamepadFullAxisState {

	private ControllerIndex cont;
	public HashMap<ControllerAxis, Float> states = new HashMap<ControllerAxis, Float>();
	
	public JamepadFullAxisState(ControllerIndex c) {
		cont = c;
		
		try {
			states.put(ControllerAxis.LEFTX, cont.getAxisState(ControllerAxis.LEFTX));
			states.put(ControllerAxis.LEFTY, cont.getAxisState(ControllerAxis.LEFTY));
			states.put(ControllerAxis.RIGHTX, cont.getAxisState(ControllerAxis.RIGHTX));
			states.put(ControllerAxis.RIGHTY, cont.getAxisState(ControllerAxis.RIGHTY));
			states.put(ControllerAxis.TRIGGERLEFT, cont.getAxisState(ControllerAxis.TRIGGERLEFT));
			states.put(ControllerAxis.TRIGGERRIGHT, cont.getAxisState(ControllerAxis.TRIGGERRIGHT));
		} catch (ControllerUnpluggedException e) {
			e.printStackTrace();
		}
	}

}
