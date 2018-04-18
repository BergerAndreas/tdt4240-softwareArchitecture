package co.aeons.zombie.shooter.gameObjects.campaignMode.shoots;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.screens.CampaignScreen;
import co.aeons.zombie.shooter.utils.TouchManager;

public class Fire extends Shoot {

    // Amplitud del efecto de disparo (el efecto de particulas)
    private static final int AMPLITUDE_OF_FIRE = 10;
    private static final int SHOOT_EFFECT_LIFE = 160;
    private final float FULL_WIDTH;
    private static final float CHANGE_IN_SCALLING = 0.04f;
    private float actualReasonOfScaling;

    // Lo usaremos para hacer el calculo del ángulo
    private Vector2 vector;

    // Almacena el punto objetivo del arma
    private Vector3 targetVector;

    // El efecto de disparo
    private ParticleEffect shoot;

    public Fire(GameObject shooter, float xTarget, float yTarget, ParticleEffect shoot) {
        super("yellow_shoot", 0, 0, shooter,null,null,null);

        if (!this.isFromShootOfEnemy()) {
            this.setX((int)(shooter.getX()+shooter.getWidth()+this.getHeight()));
        } else {
            this.setX((int) shooter.getX());
        }
        this.setY(getShooter().getY() + this.getHeight());
        this.setOrigin(0,this.getHeight()/2);

        FULL_WIDTH = getWidth();
        actualReasonOfScaling = 0.0f;

        this.shoot = shoot;
        shoot.getEmitters().first().setPosition(this.getX() - this.getHeight()/2, this.getY()+this.getHeight()/2);

        vector = new Vector2();

        //Si es la nave el shooter, o bien el shooter del shoot que ejecutó el fuego, usamos el touchManager
        if (!this.isFromShootOfEnemy()) {
            targetVector = TouchManager.getTouchFromPosition(xTarget, yTarget);
        } else {
            targetVector = new Vector3();
            targetVector.x = xTarget;
            targetVector.y = yTarget;
        }

        recalculateLifeShootEffect();

        shoot.start();
    }

    private void recalculateLifeShootEffect(){
        this.setScale( actualReasonOfScaling , 1.0f);
        shoot.getEmitters().first().getLife().setHigh(SHOOT_EFFECT_LIFE * actualReasonOfScaling);
    }

    public void update(float delta){

        float newX;
        float newY = this.getShooter().getY() + this.getShooter().getHeight()/2 - this.getHeight() / 2;

        if (this.isFromShootOfEnemy()) {
            targetVector.x = CampaignScreen.ship.getX() + CampaignScreen.ship.getWidth() / 2;
            targetVector.y = CampaignScreen.ship.getY() + CampaignScreen.ship.getHeight() / 2;
            newX = this.getShooter().getX() - this.getShooter().getHeight()/2;
        } else {
            newX = this.getShooter().getX() + this.getShooter().getWidth() - this.getShooter().getHeight()/4;
        }

        this.setX(newX);
        this.setY(newY);

        shoot.update(delta);
        shoot.getEmitters().first().setPosition(newX + this.getHeight(), newY + this.getHeight() / 2);
        // Controlamos si el jugador sigue queriendo disparar
        // En caso de ser un enemigo el shooter, lo dejamos que dispare constantemente
        // En caso de ser un shoot el shooter, comprobamos si no ha chocado, no es borrable o todavía existe
        if ((TouchManager.isTouchActive(targetVector) || this.isFromShootOfEnemy()) &&
                !(this.getShooter() instanceof Shoot &&
                        (((Shoot) this.getShooter()).isShocked() || ((Shoot) this.getShooter()).isDeletable()))) {

            // Obtenemos el angulo a donde tenemos que girar
            vector.set(targetVector.x - this.getX(), targetVector.y - this.getY());

            // Basta con llamar al vector.angle para tener el angulo a girar
            float angle = vector.angle();

            // Rotamos el rectangulo de colisión y el efecto de particulas
            this.setRotation(angle);

            // Gestionamos el escalado del logicShape dependiendo si ha colisionado o no con un enemigo
            // Si la razón de escalado es menor que 1.0f es que
            if(actualReasonOfScaling < 1.0f){
                actualReasonOfScaling+= CHANGE_IN_SCALLING/2;
            }else{
                actualReasonOfScaling = 1.0f;
            }

            recalculateLifeShootEffect();

            ParticleEmitter.ScaledNumericValue angles = this.shoot.getEmitters().first().getAngle();
            angles.setLow(angle);
            angles.setHigh(angle-AMPLITUDE_OF_FIRE,angle+AMPLITUDE_OF_FIRE);
        }else{
            this.shock();
            shoot.allowCompletion();
            if (shoot.isComplete()) {
                this.changeToDeletable();
            }
        }
    }

    public boolean isFromShootOfEnemy() {
        return (this.getShooter() instanceof Shoot && ((Shoot) this.getShooter()).getShooter() instanceof Enemy);
    }

    public void render(){
        shoot.draw(SpaceGame.batch);
    }

    public void collideWithEnemy(Enemy enemy) {
        // Como ha colisionado con un enemigo, tenemos que reducir el ancho del logicShape
        // además de reducir el efecto de particulas
        // Esto lo hacemos reduciendo su factor de escalado
        actualReasonOfScaling -= CHANGE_IN_SCALLING;
    }

    public void dispose(){
        super.dispose();
        shoot.dispose();
    }
}
