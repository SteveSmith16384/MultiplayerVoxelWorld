package com.scs.splitscreenfpsengine;

public class Settings {

	public static final boolean RELEASE_MODE = false;
	public static final boolean RECORD_VID = false;
	public static final boolean LOAD_J3OModels = false;

	// DEBUG

	// Unused debug
	public static final boolean SHOW_BULLET_SPHERE = false;
	public static final boolean DEBUG_BULLET_COLLISION = false;
	public static final boolean DEBUG_ROTATING_CAM = false;
	public static final boolean ALWAYS_SHOW_4_CAMS = false;
	//public static final boolean USE_MODEL_FOR_PLAYERS = true;
	//public static final boolean DEBUG_GAMEPAD_DIV_TPF = false;
	//public static final boolean DEBUG_GAMEPAD_MULT_VALUE = false;	
	//public static final boolean GAMEPAD_USE_AVG = false;
	//public static final boolean DEBUG_GAMEPAD_TURNING = false;
	//public static final boolean DEBUG_HUD = false;
	//public static final boolean DEBUG_TARGETTER = false;
	public static final boolean USE_TERRAIN = true;

	public enum GameMode {Skirmish, KingOfTheHill } // todo - delete these
	
	// Game settings
	public static GameMode GAME_MODE;
	
	// Our movement speed
	public static final float PLAYER_MOVE_SPEED = 3f;
	public static final float JUMP_FORCE = 4f;
	public static final float FRUSTUM_ANGLE = 55;
	public static final float CAM_DIST = 100f;
	
	// User Data
	public static final String ENTITY = "Entity";
	
	public static void p(String s) {
		System.out.println(System.currentTimeMillis() + ": " + s);
	}

	
	public static void pe(String s) {
		System.err.println(System.currentTimeMillis() + ": " + s);
	}

	
}
