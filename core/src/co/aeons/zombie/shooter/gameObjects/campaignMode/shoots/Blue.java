package co.aeons.zombie.shooter.gameObjects.campaignMode.shoots;

import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.enums.TypeShoot;

public class Blue extends Rocket {

    public Blue(GameObject shooter, int x, int y, float yTarget) {
        super("blue_shoot", shooter, x , y, yTarget, 350,
                AssetsManager.loadParticleEffect("blue_shoot_effect_shoot"),
                AssetsManager.loadParticleEffect("blue_shoot_effect_shock"),
                AssetsManager.loadParticleEffect("blue_propulsion_effect"));

    }

}
