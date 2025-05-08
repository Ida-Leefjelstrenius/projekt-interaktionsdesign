package com.example.projektinteraktionsdesign;
import static com.example.projektinteraktionsdesign.GameConstants.*;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.widget.ImageView;

public class CollisionUtils {
    public static boolean checkCollision(float objectX, float objectY, Bitmap object, ImageView player) {
        Rect playerRect = new Rect(
                (int) player.getX(),
                (int) player.getY() + PLAYER_HIT_BOX_A,
                (int) player.getX() +  player.getWidth(),
                (int) player.getY() + PLAYER_HIT_BOX_B);
        Rect objectRect = new Rect(
                (int) objectX,
                (int) objectY,
                (int) (objectX + object.getWidth()),
                (int) (objectY + object.getHeight()));

        return Rect.intersects(playerRect, objectRect);
    }
}
