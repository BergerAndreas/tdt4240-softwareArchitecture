package co.aeons.zombie.shooter.entities.bullets;


import co.aeons.zombie.shooter.managers.ResourceManager;
import co.aeons.zombie.shooter.utils.Utils;

public class ShotgunBullet extends Bullet {

    public ShotgunBullet(float x, float y) {
        super(x, y);
        lifeTime = 0.4f;
        this.speed = Utils.randInt(400, 500);
        this.dy = Utils.randInt(-100, 100);
        this.dx = speed;
        this.bullet = ResourceManager.getShotGunBullet();
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


    public String getType(){
        return "s";
    }


}
