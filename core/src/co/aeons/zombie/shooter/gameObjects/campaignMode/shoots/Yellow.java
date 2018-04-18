package co.aeons.zombie.shooter.gameObjects.campaignMode.shoots;


import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.enums.TypeShoot;

public class Yellow extends Fire {

    public Yellow(GameObject shooter, float xTarget, float yTarget) {
        super(shooter,xTarget,yTarget,AssetsManager.loadParticleEffect("yellow_shoot_effect"));
    }

}