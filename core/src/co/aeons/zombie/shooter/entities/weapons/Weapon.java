package co.aeons.zombie.shooter.entities.weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Queue;

import co.aeons.zombie.shooter.entities.Bullet;

public abstract class Weapon {

    protected Texture weaponTexture;

    protected int fireRate;
    protected int clipSize;
    protected Queue<Bullet> bullets;

    protected boolean isReloading;
    protected float reloadTime;

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    public Weapon(float x, float y) {
        this.x = x;
        this.y = y;
        isReloading = false;
    }

    public abstract ArrayList<Bullet> shoot();

    public abstract void reload();

    public abstract Bullet getNewBullet();

    public void update(float dt) {
        reloadTime -= dt;
        if(reloadTime <= 0) {
            isReloading = false;
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
}
