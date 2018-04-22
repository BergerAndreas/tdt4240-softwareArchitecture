package co.aeons.zombie.shooter.entities.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Queue;

import co.aeons.zombie.shooter.entities.bullets.Bullet;

public abstract class Weapon {

    protected Texture weaponTexture;
    protected String texturePath;

    protected int clipSize;
    protected Queue<Bullet> bullets;

    // Player cannot shoot while true
    protected boolean isReloading;
    protected boolean isFired;

    // Time it take to reload
    protected float reloadTime;

    // Time it take to fire next bullet after shooting bullet
    protected float fireRate;

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    public Weapon(float x, float y) {
        this.x = x;
        this.y = y;
        isReloading = false;
        isFired = false;
    }

    public abstract ArrayList<Bullet> shoot();
    public abstract void reload();
    public abstract void playSound();

    public abstract Bullet getNewBullet();

    // Update fireRate and reloadTime
    public void update(float dt) {
        reloadTime -= dt;
        fireRate -= dt;
        if(reloadTime <= 0) {
            isReloading = false;
        }
        if(fireRate <= 0) {
            isFired = false;
        }

    }

    public void draw(SpriteBatch sb){
        sb.begin();
        sb.draw(weaponTexture, x, y, width, height);
        sb.end();
    }

    public float getReloadTime() {
        return reloadTime;
    }

    public int getRemainingBullets() {
        return bullets.size();
    }

    public int getClipSize() {
        return clipSize;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public abstract String getType();
}
