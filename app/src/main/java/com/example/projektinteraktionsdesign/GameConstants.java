package com.example.projektinteraktionsdesign;

public class GameConstants {
    //Mine
    public static final int ZONE_SIZE = 250;
    public static final int MINE_WIDTH = 50;
    public static final int MINE_HEIGHT = 70;
    public static final float MINE_SPAWN_CHANCE = 0.3f;
    public static final float MINE_EASY_MULTIPLIER = 0.75f;
    public static final float MINE_HARD_MULTIPLIER = 1.4f;

    //Shark
    public static final int SHARK_WIDTH = 160;
    public static final int SHARK_HEIGHT = 100;
    public static final float SHARK_BASE_SPEED = 5.0f;
    public static final float SHARK_MAX_SPEED = 15.0f;
    public static final float SHARK_ACCELERATION = 0.3f;
    public static final float SHARK_EASY_MULTIPLIER = 0.75f;
    public static final float SHARK_HARD_MULTIPLIER = 1.4f;

    //Player
    public static final float FRICTION = 0.6f;
    public static final int PLAYER_HIT_BOX_A = 225;
    public static final int PLAYER_HIT_BOX_B = 325;
    public static final float PLAYER_ACCELERATION_FACTOR = 1.0f;

    //Chest
    public static final int CHEST_WIDTH = 100;
    public static final int CHEST_HEIGHT = 80;
    public static final float BOBBING_FREQUENCY = 0.02f;
    public static final float BOBBING_AMPLITUDE = 150.0f;
    public static final int CHEST_Y_OFFSET = 350;
    public static final float CHEST_SPAWN_CHANCE = 0.20f;
    public static final float CHEST_EASY_MULTIPLIER = 1.6f;
    public static final float CHEST_HARD_MULTIPLIER = 0.5f;

    //Other
    public static final String EASY = "easy";
    public static final String NORMAL = "normal";
    public static final String HARD = "hard";
    public static final String SHARK_DIFFICULTY = "sharkDifficulty";
    public static final String MINE_DIFFICULTY = "mineDifficulty";
    public static final String CHEST_DIFFICULTY = "chestDifficulty";
}