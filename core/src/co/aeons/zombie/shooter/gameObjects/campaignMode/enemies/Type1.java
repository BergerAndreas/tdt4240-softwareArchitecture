package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies;

import com.badlogic.gdx.math.MathUtils;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.DamageManager;
import co.aeons.zombie.shooter.utils.enums.TypeEnemy;

public class Type1 extends Enemy{

    public static final int SPEED = 200;

    //Indicará el rango de ondulación
    private static final int AMPLITUDE = 80;

    //Indicará el grado del movimiento que irá variando con la ondulación
    private float degrees;

    //Este es el tiempo requerido para el comienzo de movimiento de un enemigo
    //Usaremos esta propiedad para definir el escuadrón
    private float delay;

    public Type1(int x, int y, float delay) {
        super("type1", x, y, 1, AssetsManager.loadParticleEffect("basic_destroyed"));

        this.delay = delay;
        degrees = 0;
    }

    public void update(float delta){
        super.update(delta);
        if (!this.isDefeated()) {
            //Si se ha terminado el tiempo requerido, el enemigo se moverá
            if (delay <= 0) {
                this.setX(this.getX() - SPEED * delta);
                degrees += delta * SPEED;
                this.setY(this.getInitialPosition().y + (AMPLITUDE * MathUtils.sinDeg(degrees)));
            } else {
                delay -= delta;
            }
        }
    }

    public void collideWithShoot(Shoot shoot) {
        DamageManager.calculateDamage(shoot,this);
    }

}
