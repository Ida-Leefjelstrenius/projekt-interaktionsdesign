package com.example.projektinteraktionsdesign;

import android.content.Context;
import android.content.SharedPreferences;
public class GamePrefs {
    private static final String PREFS_NAME = "game_prefs";

    public static void resetHighscore(Context context) {
        getPrefs(context).edit().putInt("highscore", 0).apply();
    }
    public static int getHighscore(Context context) {
        return getPrefs(context).getInt("highscore", 0);
    }

    public static void setHighscore(Context context, int value) {
        getPrefs(context).edit().putInt("highscore", value).apply();
    }

    public static int getTotalCoins(Context context) {
        return getPrefs(context).getInt("total_coins", 0);
    }

    public static void addCoins(Context context, int value) {
        int current = getTotalCoins(context);
        getPrefs(context).edit().putInt("total_coins", current + value).apply();
    }

    public static boolean isGameOver(Context context) {
        return getPrefs(context).getBoolean("isGameOver", false);
    }

    public static void setGameOver(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("isGameOver", value).apply();
    }

    public static boolean isMuted(Context context) {
        return getPrefs(context).getBoolean("isMuted", false);
    }

    public static void setMuted(Context context, boolean value){
        getPrefs(context).edit().putBoolean("isMuted", value).apply();
    }

    public static boolean isHitboxOn(Context context) {
        return getPrefs(context).getBoolean("isHitboxOn", false);
    }

    public static void setHitboxOn(Context context, boolean value) {
        getPrefs(context).edit().putBoolean("isHitboxOn", value).apply();
    }

    public static float getMineDifficulty(Context context) {
        return getPrefs(context).getFloat("mineDifficulty", 1.0f);
    }

    public static void setMineDifficulty(Context context, float value) {
        getPrefs(context).edit().putFloat("mineDifficulty", value).apply();
    }
    public static float getSharkDifficulty(Context context) {
        return getPrefs(context).getFloat("sharkDifficulty", 1.0f);
    }
    public static void setSharkDifficulty(Context context, float value) {
        getPrefs(context).edit().putFloat("sharkDifficulty", value).apply();
    }

    public static float getChestDifficulty(Context context) {
        return getPrefs(context).getFloat("chestDifficulty", 1.0f);
    }
    public static void setChestDifficulty(Context context, float value) {
        getPrefs(context).edit().putFloat("chestDifficulty", value).apply();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}