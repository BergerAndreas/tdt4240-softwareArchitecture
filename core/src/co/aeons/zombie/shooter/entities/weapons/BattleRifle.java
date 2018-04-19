package co.aeons.zombie.shooter.entities.weapons;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.LinkedList;

import co.aeons.zombie.shooter.entities.bullets.BRBullet;
import co.aeons.zombie.shooter.entities.bullets.Bullet;

public class BattleRifle extends Weapon {

    public static final float RELOAD_TIME = 2.5f;
    private int bulletDelay;

    public BattleRifle(float x, float y) {
        super(x, y);

        clipSize = 36;
        fireRate = 0.5f;
        texturePath = "weapons/assault1.png";
        weaponTexture = new Texture(texturePath);
        bullets = new LinkedList<Bullet>();
        reloadTime = 1.25f;
        reload();
        isReloading = false;
        bulletDelay = 0;
    }

    @Override
    public ArrayList<Bullet> shoot() {
        ArrayList<Bullet> output = new ArrayList<Bullet>();
        if(!isReloading && !isFired){
            if(!bullets.isEmpty()) {
                for (int i = 0; i < 3; i++) {
                    output.add(bullets.poll());
                }

                if(bullets.isEmpty()) {
                    reload();
                }

                isFired = true;
                fireRate = 0.5f;
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
            setBulletDelay(i % 3);
            bullets.add(getNewBullet());
        }
    }

    @Override
    public Bullet getNewBullet() {
        Bullet bill = new BRBullet(this.x, this.y, this.bulletDelay);
        return bill;
    }

    public void setBulletDelay(int bulletDelay) {
        this.bulletDelay = bulletDelay;
    }

}
