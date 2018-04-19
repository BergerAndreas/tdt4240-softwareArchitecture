package co.aeons.zombie.shooter.entities.bullets;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import co.aeons.zombie.shooter.utils.utils;

public class ShotgunBullet extends Bullet {

    public ShotgunBullet(float x, float y) {
        super(x, y);
        lifeTime = 0.4f;
        this.speed = utils.randInt(400, 500);
        this.dy = utils.randInt(-100, 100);
        this.dx = speed;
        this.bullet = new Sprite(new Texture("weapons/shotgun1.png"));
        this.damage = 5;
    }

    @Override
    public void update(float dt) {

        x += dx*dt;
        y += dy*dt;

        bulletBounds.setPosition(x, y);
        lifeTimer += dt;
        if(lifeTimer > lifeTime) {
            remove = true;
        }
    }


}
