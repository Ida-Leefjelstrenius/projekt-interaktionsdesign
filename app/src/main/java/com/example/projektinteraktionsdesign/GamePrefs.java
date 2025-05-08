package com.example.projektinteraktionsdesign;

import android.content.Context;
import android.content.SharedPreferences;
public class GamePrefs {
    private static final String PREFS_NAME = "game_prefs";

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
    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
}
