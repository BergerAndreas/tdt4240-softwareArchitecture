package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies;

import com.badlogic.gdx.math.MathUtils;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.DamageManager;
import co.aeons.zombie.shooter.utils.ShootsManager;
import co.aeons.zombie.shooter.utils.enums.TypeEnemy;

public class Type3 extends Enemy {

    //Velocidad a la que se moverá el enemigo
    private static final int SPEED = 80;

    //Valor por el que se multiplicará la velocidad inicial del enemigo antes de girar
    private static final int ACCELERATION = 2;

    //Posición en la que el enemigo comenzará a girar
    private static final int INITIAL_X_TO_ROTATE = 650;

    //Indicará el diámetro del círculo que dibujará el enemigo
    private static final int DIAMETER = 150;

    //Será el valor de referencia que servirá para la frecuencia de disparo
    private static final int FREQUENCY = 100;

    //Valor inicial al que se reiniciará el timeToShoot
    private static final float INITIAL_TIME_TO_SHOOT = 150.0f;

    //Indicará el grado del movimiento que irá variando con la ondulación
    private float degrees;

    //Indica la posición de la x inicial donde empezará a girar
    private float centerOfCircle;

    //Cantidad de pixeles que quedan para que el enemigo empiece a girar
    private int pixelsToMoveSlowly;

    //Tiempo que queda hasta el disparo
    private float timeToShoot;

    //Variable auxiliar necesaria para controlar ciertos cálculos posteriores
    private float aux;

    public Type3(int x, int y) {
        super("type3", x, y, 60, AssetsManager.loadParticleEffect("basic_destroyed"));

        pixelsToMoveSlowly = 150;
        timeToShoot = INITIAL_TIME_TO_SHOOT;
        centerOfCircle = SpaceGame.width - (pixelsToMoveSlowly * ACCELERATION);
    }

    public void update(float delta){
        super.update(delta);

        if (!this.isDefeated()) {
            //Si aún no ha empezado a girar
            if (this.getX() > INITIAL_X_TO_ROTATE) {
                aux = this.getX() - SPEED * ACCELERATION * delta;
                if (aux < INITIAL_X_TO_ROTATE)
                    this.setX(INITIAL_X_TO_ROTATE);
                else
                    this.setX(aux);
            } else {
                // Si ya ha empezado a girar
                degrees += SPEED * delta;
                this.setX(centerOfCircle + (DIAMETER * MathUtils.cosDeg(degrees)));
                this.setY(this.getInitialPosition().y + (DIAMETER * MathUtils.sinDeg(degrees)));

                //Si el tiempo se ha acabado, el enemigo disparará
                if (timeToShoot <= 0) {
                    this.shoot();
                    timeToShoot = INITIAL_TIME_TO_SHOOT;
                } else {
                    timeToShoot -= FREQUENCY * delta;
                }
            }
        }
    }

    public void shoot(){
        ShootsManager.shootOneBasicWeapon(this);
    }

    public void collideWithShoot(Shoot shoot) {DamageManager.calculateDamage(shoot,this);
    }

}
