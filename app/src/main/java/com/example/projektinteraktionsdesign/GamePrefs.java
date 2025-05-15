package com.example.projektinteraktionsdesign;
import static com.example.projektinteraktionsdesign.GameConstants.*;

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

    public static void resetCoins(Context context) {
        getPrefs(context).edit().putInt("total_coins", 0).apply();
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
    public static String getDifficulty(Context context, String type) {
        return getPrefs(context).getString(type, NORMAL);
    }

    public static void setDifficulty(Context context, String type, String value) {
        getPrefs(context).edit().putString(type, value).apply();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}