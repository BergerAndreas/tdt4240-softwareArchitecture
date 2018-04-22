package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.managers.Jukebox;
import co.aeons.zombie.shooter.managers.ResourceManager;

public class Trump extends Zombie {

    public Trump(float x, float y, int difficulty) {
        super(x, y, difficulty);
        this.dx = -25;
        this.health = 50;

        setType("t");

        this.score = 35;
        width = height = 60;
        bounds = new Rectangle(0, 0, width, height);
    }

    @Override
    protected void createIdleAnimation() {
        this.runningAnimation = ResourceManager.getTrumpRunningAnimation();
        stateTimeRunning = 0f;
    }

    @Override
    protected void createAttackAnimation() {
        this.attackAnimation = ResourceManager.getTrumpAttackAnimation();
        stateTimeRunning = 0f;
    }

    @Override
    public void deathSound() {
        Jukebox.play("china");
    }

}
