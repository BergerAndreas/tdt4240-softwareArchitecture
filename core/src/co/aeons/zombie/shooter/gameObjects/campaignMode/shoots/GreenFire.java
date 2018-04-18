package co.aeons.zombie.shooter.gameObjects.campaignMode.shoots;

import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.enums.TypeShoot;


public class GreenFire extends Fire {

    public GreenFire(GameObject shooter, float xTarget, float yTarget) {
        super(shooter,xTarget,yTarget, AssetsManager.loadParticleEffect("green_shoot_effect"));
    }

}
