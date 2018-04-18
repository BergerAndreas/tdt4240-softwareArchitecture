package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies;

import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.DamageManager;
import co.aeons.zombie.shooter.utils.enums.TypeEnemy;

public class Type2 extends Enemy{

    // En el principio este enemigo viajará a una velocidad que
    // llamaremos "normal" para luego lanzarse hacia el jugador
    // con una velocidad "alta"
    private static final int NORMAL_SPEED = 150;
    private static final int HIGH_SPEED = 500;

    // Píxel al que el enemigo se moverá antes de acelerar
    private static final int LIMIT_TO_MOVE_SLOW = 600;

    // Este es el tiempo en el que el enemigo esperará sin
    // moverse hasta que se lance hacía el jugador
    private float timeToGoFast;

    public Type2(int x, int y) {
        super("type2", x, y, 10, AssetsManager.loadParticleEffect("basic_destroyed"));

        timeToGoFast = 2f;
    }

    public void update(float delta){
        super.update(delta);

        if (!this.isDefeated()) {
            // Mientras tengamos que seguir moviendonos lentamente
            if (this.getX() > LIMIT_TO_MOVE_SLOW) {
                this.setX(this.getX() - NORMAL_SPEED * delta);
            } else {
                // Si el tiempo ya ha acabado, podemos movernos rapidamente
                if (timeToGoFast <= 0) {
                    this.setX(this.getX() - HIGH_SPEED * delta);
                } else {
                    timeToGoFast -= delta;
                }
            }
        }
    }

    public void collideWithShoot(Shoot shoot) {
        DamageManager.calculateDamage(shoot,this);
    }

}
