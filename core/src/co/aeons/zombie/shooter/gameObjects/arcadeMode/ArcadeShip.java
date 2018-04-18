package co.aeons.zombie.shooter.gameObjects.arcadeMode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.utils.AssetsManager;

public class ArcadeShip extends GameObject {

    private final static float SPEED = 200;

    //Efecto de particulas del fuego de propulsión
    private ParticleEffect fireEffectLeft;
    private ParticleEffect fireEffectRight;
    private float fireEffectScale;
    private float fireEffectLife;

    //Efecto de destrucción de la nave
    private ParticleEffect destroyEffect;
    private float destroyEffectScale;
    private float destroyEffectLife;

    //Rango de sensibilidad del movimiento horizontal
    private float sensitiveRange;

    //Indica si la nave está derrotada
    private boolean defeated;

    public ArcadeShip() {
        super("arcadeShip", 0, 25);

        //Colocamos la nave en la mitad de la pantalla
        this.setX((SpaceGame.width / 2) - this.getWidth());

        //Preparamos los efectos de partículas con sus respectivos reescalados
        fireEffectLeft = AssetsManager.loadParticleEffect("propulsion_ship_effect");
        fireEffectRight = AssetsManager.loadParticleEffect("propulsion_ship_effect");
        fireEffectScale = 300;
        fireEffectLife = fireEffectLeft.getEmitters().first().getLife().getHighMax();

        destroyEffect = AssetsManager.loadParticleEffect("ship_shock_effect");
        destroyEffectScale = 300;
        destroyEffectLife = destroyEffect.getEmitters().first().getLife().getHighMax();

        sensitiveRange = 0.3f;
        defeated = false;

        this.updateParticleEffect();
        fireEffectLeft.start();
        fireEffectRight.start();
        destroyEffect.start();
    }

    @Override
    public void render(){
        if (!defeated) {
            super.render();
            fireEffectLeft.draw(SpaceGame.batch);
            fireEffectRight.draw(SpaceGame.batch);
        } else {
            destroyEffect.draw(SpaceGame.batch);
        }
    }

    public void update(float delta) {
        if (!defeated) {

            //Nos movemos si hemos salido del rango de sensibilidad
            if (Gdx.input.getAccelerometerX() > sensitiveRange)
                this.setX(this.getX() - (Gdx.input.getAccelerometerX() * SPEED * delta * this.getLogicShape().getScaleX()));
            else if (Gdx.input.getAccelerometerX() < -sensitiveRange)
                this.setX(this.getX() - (Gdx.input.getAccelerometerX() * SPEED * delta * this.getLogicShape().getScaleX()));

            //Controlamos que no nos salgamos de la pantalla
            checkInsideScreen();

            //Actualizamos los efectos de partículas
            this.updateParticleEffect();
            fireEffectLeft.update(delta);
            fireEffectRight.update(delta);
        } else {
            this.updateParticleEffect();
            destroyEffect.update(delta);
        }
    }

    //Controla que la nave no se salga de la pantalla
    public void checkInsideScreen() {

        //Será necesario para que los valores sean independientes del escalado de la nave
        float aditive = ((1 - this.getLogicShape().getScaleX()) / 2) * this.getWidth();

        if (this.getX() < -aditive) {
            this.setX(-aditive);
        } else if (this.getX() > SpaceGame.width - this.getWidth() + aditive) {
            this.setX(SpaceGame.width - this.getWidth() + aditive);
        }


    }

    public void updateParticleEffect() {
        if (!defeated) {
            //Colocamos el efecto según la posición de la nave, y ajustamos el ángulo, ya que originalmente es horizontal
            fireEffectLeft.getEmitters().first().setPosition(
                    this.getX() + this.getWidth()/2 - (this.getWidth()/3 * this.getLogicShape().getScaleX()),
                    this.getY() + (this.getHeight() * ((1 - this.getLogicShape().getScaleY()) / 2)));

            fireEffectRight.getEmitters().first().setPosition(
                    this.getX() + this.getWidth()/2 + (this.getWidth()/3 * this.getLogicShape().getScaleX()),
                    this.getY() + (this.getHeight() * ((1 - this.getLogicShape().getScaleY()) / 2)));

            fireEffectLeft.getEmitters().first().getAngle().setHigh(270);
            fireEffectRight.getEmitters().first().getAngle().setHigh(270);
        } else {
            destroyEffect.getEmitters().first().setPosition(this.getCenter().x, this.getCenter().y);
        }
    }

    //Indica que la nave está derrotada
    public void defeat() {
        defeated = true;
    }

    //Además de escalar el propio objeto, se encarga de escalar el efecto de partículas
    public void setScale(float x, float y) {
        super.setScale(x, y);

        fireEffectLeft.getEmitters().first().getLife().setHigh(fireEffectLife * y);

        fireEffectRight.getEmitters().first().getLife().setHigh(fireEffectLife * y);

        destroyEffect.getEmitters().first().getVelocity().setHigh(destroyEffectScale * y);
        destroyEffect.getEmitters().first().getLife().setHigh(destroyEffectLife * y);
    }

    @Override
    public void dispose() {
        super.dispose();
        fireEffectLeft.dispose();
        fireEffectRight.dispose();
        destroyEffect.dispose();
    }

}
