package co.aeons.zombie.shooter.entities.weapons;

import java.util.ArrayList;
import java.util.LinkedList;

import co.aeons.zombie.shooter.entities.bullets.Bullet;
import co.aeons.zombie.shooter.entities.bullets.ShotgunBullet;
import co.aeons.zombie.shooter.managers.Jukebox;
import co.aeons.zombie.shooter.managers.ResourceManager;

public class Shotgun extends Weapon{

    private int pelletCount;
    private static final float RELOAD_TIME = 1.0f;

    public Shotgun(float x, float y) {
        super(x, y);
        clipSize = 4;
        fireRate = 0.3f;
        bullets = new LinkedList<Bullet>();
        pelletCount = 12;
        reload();
        isReloading = false;
        texturePath = "weapons/shotgun1.png";
        weaponTexture = ResourceManager.getShotgunTexture();
    }

    @Override
    public ArrayList<Bullet> shoot() {
        ArrayList<Bullet> output = new ArrayList<Bullet>();
        if(!isReloading && !isFired) {
            if(!bullets.isEmpty()){
                for (int i = 0; i < pelletCount; i++) {
                    output.add(getNewBullet());
                }
                playSound();
                bullets.poll();
                if (bullets.isEmpty()) {
                    reload();
                    Jukebox.play("shotgunReload");
                }
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
    public void playSound() {
        Jukebox.play("skrra");
    }

    @Override
    public Bullet getNewBullet() {
        return new ShotgunBullet(x, y);
    }


    public String getType(){
        return "s";
    }
}
