package co.aeons.zombie.shooter.entities.weapons;

import java.util.ArrayList;
import java.util.LinkedList;

import co.aeons.zombie.shooter.entities.bullets.Bullet;
import co.aeons.zombie.shooter.entities.bullets.ShotgunBullet;

public class Shotgun extends Weapon{

    private int pelletCount;
    private static final float RELOAD_TIME = 4.0f;

    public Shotgun(float x, float y) {
        super(x, y);
        clipSize = 4;
        reloadTime = 3;
        bullets = new LinkedList<Bullet>();
        pelletCount = 24;
        reload();
        isReloading = false;
    }

    @Override
    public ArrayList<Bullet> shoot() {
        ArrayList<Bullet> output = new ArrayList<Bullet>();
        if(!isReloading) {
            if(!bullets.isEmpty()){
                for (int i = 0; i < pelletCount; i++) {
                    output.add(getNewBullet());
                }
                bullets.poll();
            }else reload();
        }else System.out.println("Reloading!");
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
        return new ShotgunBullet(x, y);
    }
}
