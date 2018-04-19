package co.aeons.zombie.shooter.entities.weapons;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.LinkedList;

import co.aeons.zombie.shooter.entities.bullets.Bullet;

public class Pistol extends Weapon {

    private static final float RELOAD_TIME = 2.0f;

    public Pistol(float x, float y) {
        super(x, y);
        clipSize = 12;
        fireRate = 0.3f;
        texturePath = "weapons/pistol1.png";
        weaponTexture = new Texture(texturePath);
        bullets = new LinkedList<Bullet>();
        reloadTime = 0.7f;
        reload();
        isReloading = false;
    }

    @Override
    public ArrayList<Bullet> shoot() {
        ArrayList<Bullet> output = new ArrayList<Bullet>();
        if(!isReloading && !isFired){
            if(!bullets.isEmpty()) {
                output.add(bullets.poll());
                isFired = true;
                fireRate = 0.3f;
            }else reload();
        }
        return output;
    }

    @Override
    public void reload() {
        bullets.clear();
        isReloading = true;
        reloadTime = RELOAD_TIME;
        for (int i = 0; i < clipSize; i++) {
            bullets.add(getNewBullet());
        }
    }

    @Override
    public Bullet getNewBullet() {
       return new Bullet(this.x, this.y);
    }

}
