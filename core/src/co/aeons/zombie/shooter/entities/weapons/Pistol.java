package co.aeons.zombie.shooter.entities.weapons;

import java.util.ArrayList;
import java.util.LinkedList;

import co.aeons.zombie.shooter.entities.bullets.Bullet;
import co.aeons.zombie.shooter.managers.Jukebox;
import co.aeons.zombie.shooter.managers.ResourceManager;

public class Pistol extends Weapon {

    private static final float RELOAD_TIME = 0.3f;
    private static final float FIRE_RATE = 0.01f;

    public Pistol(float x, float y) {
        super(x, y);
        clipSize = 20;
        fireRate = FIRE_RATE;

        texturePath = "weapons/pistol1.png";
        weaponTexture = ResourceManager.getPistolTexture();
        bullets = new LinkedList<Bullet>();
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
                fireRate = FIRE_RATE;
                playSound();
                if (bullets.isEmpty()) {
                    reload();
                    Jukebox.play("pistolReload");
                }
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
        Jukebox.play("pom");
    }

    @Override
    public Bullet getNewBullet() {
       return new Bullet(this.x, this.y);
    }


    public String getType(){
        return "p";
    }
}
