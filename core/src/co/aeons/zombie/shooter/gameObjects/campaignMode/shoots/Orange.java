package co.aeons.zombie.shooter.gameObjects.campaignMode.shoots;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.gameObjects.campaignMode.CampaignShip;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.enums.TypeShoot;


public class Orange extends Shoot{

    // Constantes de velocidad de movimiento del arma naranja
    private final int SPEEDX;
    private final int SPEEDY;

    // Constantes de velocidad angular en el arma naranja
    private final float SPEEDANGLE;
    private final float HIGHSPEEDANGLE;

    // Máximo tiempo en el que irá rapidamente el arma naranja
    private final float MAX_TIME_GOING_HIGH_SPEED;

    // Velocidad angular actual
    private float actualSpeedAngle;

    // Tiempo actual durante el cual el movimiento angular será rápido
    private float timeGoingFast;

    // Efecto de particulas de este disparo
    private ParticleEffect shoot;

    // Angulo objetivo
    private float targetAngle;

    // Angulo actual
    private float actualAngle;

    // Vector Origen-Destino
    private Vector2 ODVector;

    // GameObject objetivo
    private GameObject target;

    public Orange(GameObject shooter, int x, int y, float angle, GameObject target) {
        super("orange_shoot", x, y, shooter,
                AssetsManager.loadParticleEffect("orange_shoot_effect_shoot"),
                AssetsManager.loadParticleEffect("orange_shoot_effect_shock"),
                "shoot_orange_FX");

        this.setY(this.getY() - this.getHeight()/2);

        shoot = AssetsManager.loadParticleEffect("orange_shoot_effect");
        shoot.getEmitters().first().setPosition(this.getX()+this.getWidth()/2,this.getY()+this.getHeight()/2);

        this.target = target;

        ODVector = new Vector2();

        super.updateParticleEffect();

        actualAngle = angle;

        timeGoingFast = 0;

        // Dependiendo si es el LandscapeShip o si es el Enemy
        // tendrá una configuración distinta
        // ya que el enemy tendrá que girar mas rápidamente al inicio.
        if(getShooter() instanceof CampaignShip){
            MAX_TIME_GOING_HIGH_SPEED = 0.4f;
            SPEEDX=150;
            SPEEDY=150;
            SPEEDANGLE = 0.7f;
            HIGHSPEEDANGLE = 0.0f;
        }else{
            MAX_TIME_GOING_HIGH_SPEED = 1.3f;
            SPEEDANGLE = 0.8f;
            HIGHSPEEDANGLE = 2.3f;
            SPEEDX=250;
            SPEEDY=150;
        }

        // Al principio la velocidad angular es la rápida
        actualSpeedAngle = HIGHSPEEDANGLE;
    }
    public void update(float delta){
        super.updateParticleEffect();
        super.update(delta);

        shoot.update(delta);

        shoot.getEmitters().first().setPosition(this.getX()+this.getWidth()/2,this.getY()+this.getHeight()/2);

        if(!isShocked()){
            // Si ha pasado el tiempo en el que debemos girar rapidamente y la velocidad angular
            // no es la velocidad angular normal
            if(timeGoingFast >= MAX_TIME_GOING_HIGH_SPEED && actualSpeedAngle != SPEEDANGLE){
                // Vamos modificando la velocidad angular dependiendo de si tenemos que sumar o restar
                if(actualSpeedAngle < SPEEDANGLE){
                    actualSpeedAngle +=0.1f;
                }else{
                    actualSpeedAngle -=0.1f;
                }

            }else{
                timeGoingFast+=delta;
            }

            // Calculamos la diferencia de angulo entre el objetivo y nosotros
            // y dependiendo de si es mayor o menor sumamos
            if(calculateDiffAngle() < 0 )
                actualAngle-=actualSpeedAngle;
            else
                actualAngle+=actualSpeedAngle;

            // Movemos el arma naranja
            this.setX(this.getX()+ SPEEDX * delta * MathUtils.cosDeg(actualAngle) );
            this.setY(this.getY()+ SPEEDY * delta * MathUtils.sinDeg(actualAngle) );
        }


    }

    private float calculateDiffAngle(){
        // Posicion actual del arma naranja
        float x1 = this.getX() + this.getWidth()/2;
        float y1 = this.getY() + this.getHeight()/2;

        // Posición objetivo
        float x2 = target.getX() + target.getWidth()/2;
        float y2 = target.getY() + target.getHeight()/2;

        // Calculo del vector origen-destino
        ODVector.set( x2-x1 , y2-y1 );

        targetAngle = ODVector.angle();

        if(getShooter() instanceof CampaignShip && targetAngle >=180)
            targetAngle-=360;

        return targetAngle - actualAngle;
    }

    public void render() {
        super.render();
        if(!isShocked())
            shoot.draw(SpaceGame.batch);
    }

    public void collideWithEnemy(Enemy enemy) {
        super.collideWithEnemy(enemy);
        this.shock();
    }

    public void collideWithShoot(Shoot shoot) {
        super.collideWithShoot(shoot);
        this.shock();
    }

    public void collideWithShip() {
        super.collideWithShip();
        this.shock();
    }
}
