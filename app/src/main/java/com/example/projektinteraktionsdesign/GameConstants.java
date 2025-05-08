package com.example.projektinteraktionsdesign;

public class GameConstants {
    //Mine
    public static final int ZONE_SIZE = 250;
    public  static final int MINE_SIZE = 200;
    public static final float MINE_SPAWN_CHANCE = 0.25f;

    //Shark
    public static final int SHARK_WIDTH = 160;
    public static final int SHARK_HEIGHT = 100;
    public static final float SHARK_BASE_SPEED = 4.0f;
    public static final float SHARK_MAX_SPEED = 15.0f;
    public static final float SHARK_ACCELERATION = 0.2f;

    //Player
    public static final float FRICTION = 0.6f;
    public static final int PLAYER_HIT_BOX_A = 200;
    public static final int PLAYER_HIT_BOX_B = 350;
    public static final float PLAYER_ACCELERATION_FACTOR = 1.0f;

    //Chest
    public static final int CHEST_WIDTH = 100;
    public static final int CHEST_HEIGHT = 80;
    public static final float BOBBING_FREQUENCY = 0.02f;
    public static final float BOBBING_AMPLITUDE = 150.0f;
    public static final int CHEST_Y_OFFSET = 350;
    public static final int CHEST_X_OFFSET = 2000;
}