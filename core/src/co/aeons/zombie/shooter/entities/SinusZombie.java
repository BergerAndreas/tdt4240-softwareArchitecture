package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import co.aeons.zombie.shooter.managers.Jukebox;
import co.aeons.zombie.shooter.managers.ResourceManager;

public class SinusZombie extends Zombie {

    private int d;

    public SinusZombie(float x, float y, int difficulty) {
        super(x, y, difficulty);
        this.health = 5;
        this.dx = -60;
        this.dy = 4;
        this.d = (int) y;
        setType("s");

    }

    private float f(float  y) {
        return (float) ((d/2)*Math.sin(y*0.5) + d);
    }

    @Override
    public void update(float dt) {
        if(!isStopped){
            x += dx * dt;
            y += dy*dt;
        }

        stateTimeRunning += dt;
        stateTimeAttacking += dt;

        bounds.setPosition(x, f(y));

        attackTimer += dt;
    }

    @Override
    protected void createIdleAnimation() {
        this.runningAnimation = ResourceManager.getSanicRunningAnimation();
        stateTimeRunning = 0f;
    }

    @Override
    protected void createAttackAnimation() {
        this.attackAnimation = ResourceManager.getSanicAttackAnimation();
        stateTimeRunning = 0f;
    }

//    Allow running in y-direction
    @Override
    public void draw(SpriteBatch batch) {
        batch.begin();
        if (!isStopped) {
            TextureRegion currentRunningFrame = runningAnimation.getKeyFrame(stateTimeRunning, true);
            batch.draw(currentRunningFrame, x, f(y), width, height);
        } else {
            TextureRegion currentAttackFrame = attackAnimation.getKeyFrame(stateTimeAttacking, true);
            batch.draw(currentAttackFrame, x, f(y), width, height);
        }
        batch.end();
    }

    @Override
    public void deathSound() {
        Jukebox.play("elite");
    }

}