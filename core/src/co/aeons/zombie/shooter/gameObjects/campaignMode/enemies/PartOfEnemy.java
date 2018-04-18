package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;

public class PartOfEnemy extends Enemy {

    //Enemigo al que pertece la parte
    private Enemy father;

    //Indica si esta parte daña al enemigo padre
    public boolean damageable;
    public boolean collide;

    public PartOfEnemy(String texture, int x, int y, int vitality, ParticleEffect particleEffect, Enemy father, boolean damageable, boolean collisionable) {
        super(texture, x, y, vitality, particleEffect);
        this.father = father;
        this.damageable = damageable;
        this.collide = collisionable;
    }

    public void render() {
        if (!father.isDefeated()) {
            super.render();
        }
    }

    public boolean canCollide() {
        return collide;
    }

    //Si choca con un disparo y es dañable, se resta vitalidad al enemigo padre
    public void collideWithShoot(Shoot shoot) {
        if (damageable)
            father.damage(1);
    }

}
