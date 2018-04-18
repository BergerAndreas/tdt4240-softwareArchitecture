package co.aeons.zombie.shooter.gameObjects.campaignMode.shoots;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.ShapeRendererManager;
import co.aeons.zombie.shooter.utils.enums.TypeShoot;

public class Purple extends Shoot{

    //Velocidad de movimiento
    public static final int SPEED = 10;

    //Efecto de particulas de este disparo
    private ParticleEffect shoot;

    private float angle;

    public Purple(GameObject shooter, int x, int y, float xTarget, float yTarget) {
        super("purple_shoot", x, y, shooter,
                AssetsManager.loadParticleEffect("purple_shoot_effect_shoot"),
                AssetsManager.loadParticleEffect("purple_effect_shock"),"shoot_purple_FX");

        this.setY(this.getY()-this.getHeight()/2);
        this.getLogicShape().setOrigin(0,this.getHeight()/2);

        //Creamos el efecto de particulas
        shoot = AssetsManager.loadParticleEffect("purple_shoot_effect");

        Vector2 aux = new Vector2((xTarget - (shooter.getX() + shooter.getWidth())),(yTarget - (shooter.getY() +
                shooter.getHeight()/2)));
        angle = aux.angle();

        //Cambiamos el ángulo
        this.getLogicShape().setRotation(angle);

        //Actualizamos el efecot de partículas
        this.updateParticleEffect();

        //Los iniciamos, pero aunque los iniciemos si no haya un update no avanzarán
        shoot.start();
    }

    public void updateParticleEffect() {
        super.updateParticleEffect();
        //Comprobamos si el disparo ha chocado
        if (!this.isShocked()) {
            shoot.getEmitters().first().setPosition(this.getX() + SPEED*MathUtils.cosDeg(angle) , this.getY()+ this.getHeight()/2 + SPEED*MathUtils.sinDeg(angle));
            //Rotamos el efecto de partículas para hacerlo coincidir con el ángulo del disparo
            shoot.getEmitters().first().getAngle().setHigh(angle);
        }
    }

    public void update(float delta){
        if (!this.isShocked()) {
            //Actualizamos el movimiento del disparo
            this.setX(this.getX() + SPEED*MathUtils.cosDeg(angle));
            this.setY(this.getY() + SPEED*MathUtils.sinDeg(angle));

            //Actualizamos el ángulo del disparo por si acaso tendría que modificarse
            this.getLogicShape().setRotation(angle);
            shoot.update(delta);
        }
        this.updateParticleEffect();
        super.update(delta);
    }


    public void render(){
        super.render();
        if (!this.isShocked()) {
            shoot.draw(SpaceGame.batch);
        }
    }

    public void collideWithShip() {
        this.shock();
    }

    public void collideWithEnemy(Enemy enemy) {
        this.shock();
    }

    public void collideWithShoot(Shoot shoot) {
        this.shock();
    }

    public void dispose(){
        super.dispose();
        shoot.dispose();
    }
}
