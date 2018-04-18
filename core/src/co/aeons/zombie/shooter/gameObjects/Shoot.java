package co.aeons.zombie.shooter.gameObjects;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.campaignMode.shoots.Purple;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.RivalShip;
import co.aeons.zombie.shooter.utils.AudioManager;
import co.aeons.zombie.shooter.utils.enums.TypeShoot;

public class Shoot extends GameObject {

    //Objeto de juego que ha provocado el disparo
    private GameObject shooter;

    // Indica si el disparo ha chocado con algo
    private boolean shocked;

    // Indica si el disparo se puede borrar
    private boolean deletable;

    // Efecto de particulas de este disparo
    private ParticleEffect shootEffect;

    // Efecto de partículas cuando el disparo choca
    private ParticleEffect destroyEffect;

    private Vector2 originalShootPosition;

    private String shootFX;

    public Shoot(String nameTexture, int x, int y, GameObject shooter, ParticleEffect shootEffect, ParticleEffect destroyEffect, String shootFX) {
        super(nameTexture,x,y);
        this.shooter = shooter;
        shocked = false;

        originalShootPosition = new Vector2(x,y);

        //Creamos los efectos de partículas si no vienen nulos
        if (shootEffect != null) {
            this.shootEffect = shootEffect;
            shootEffect.start();
        }
        if (destroyEffect != null) {
            this.destroyEffect = destroyEffect;
            destroyEffect.start();
        }

        this.shootFX = shootFX;
    }

    public void playShootFX(){
        if(shootFX != null)
            AudioManager.playSound(shootFX);
    }

    public GameObject getShooter(){
        return shooter;
    }

    public boolean isShocked() { return shocked;}

    public void shock() { shocked = true; }

    public boolean isDeletable() { return deletable; }

    public void changeToDeletable() { deletable = true;}

    public void update(float delta) {
        //Si el disparo no ha chocado mostramos el efecto de salida, y si ha chocado el efecto de destrucción
        if (!this.isShocked() && shootEffect != null) {
            shootEffect.update(delta);
        } else if (destroyEffect != null) {
            destroyEffect.update(delta);
            // Restamos el tiempo que estará el efecto en pantalla, y si pasa el tiempo, marcamos el shoot como deletable
            if (destroyEffect.isComplete()) {
                this.changeToDeletable();
            }
        }
    }

    public void updateParticleEffect() {
        //Comprobamos si el disparo ha chocado
        if (!this.isShocked() && shootEffect != null) {
            //Se actuará de forma distinta si el shooter es enemigo o no
            if (this.getShooter() instanceof Enemy || this.getShooter() instanceof RivalShip) {
                shootEffect.getEmitters().first().setPosition(originalShootPosition.x, originalShootPosition.y);

                float angle = 180 + getShooter().getLogicShape().getRotation();

                // Rotamos el efecto de particulas
                shootEffect.getEmitters().first().getAngle().setHigh(angle-45, angle+45);
                shootEffect.getEmitters().first().getAngle().setLow(angle - 20, angle + 20);
            } else {
                // Lo ubicamos en el extremo derecha y mitad de altura del shooter
                shootEffect.getEmitters().first().setPosition(this.getShooter().getX() + this.getShooter().getWidth(), this.getShooter().getY() + this.getShooter().getHeight() / 2);
            }
        } else if (destroyEffect != null) {
            //Si el disparo ha chocado, el efecto a mostrar es el del shockEffect
            if (this.getShooter() instanceof Enemy || this.getShooter() instanceof RivalShip) {
                //Comprobamos si el disparo es o no un disparo morado para actuar en consecuencia
                if (this instanceof Purple==false)
                    destroyEffect.getEmitters().first().setPosition(this.getX(), this.getY());
                else
                    destroyEffect.getEmitters().first().setPosition(this.getLogicShape().getTransformedVertices()[4], this.getLogicShape().getTransformedVertices()[5]);

                //Rotamos el efecto de particulas 180º
                destroyEffect.getEmitters().first().getAngle().setHigh(135, 225);
                destroyEffect.getEmitters().first().getAngle().setLow(160, 200);
            } else {

                if(this instanceof Purple==false){
                    destroyEffect.getEmitters().first().setPosition(this.getX() + this.getWidth(), this.getY());
                }else{
                    destroyEffect.getEmitters().first().setPosition(this.getLogicShape().getTransformedVertices()[4], this.getLogicShape().getTransformedVertices()[5]);
                }
            }
        }
    }

    public void render() {
        if (!this.isShocked()) {
            //Se comprueba que no sea una instancia del arma morada para pintar la textura del arma, ya que ésta deberá tener otro tipo de render
            if (!(this instanceof Purple))
                //Si el shooter es un enemigo, giramos el arma al contrario
            if (this.getShooter() instanceof Enemy || this.getShooter() instanceof RivalShip)
                super.renderRotate(180);
            else
                super.render();

            if (shootEffect != null)
                shootEffect.draw(SpaceGame.batch);
        } else {
            if (destroyEffect != null)
                destroyEffect.draw(SpaceGame.batch);
        }
    }

    public void collideWithShip() {

    }

    public void collideWithEnemy(Enemy enemy) {}

    public void collideWithShoot(Shoot shoot) {}

    public void dispose() {
        super.dispose();
        if(shootEffect!= null)
            shootEffect.dispose();
        if(shootEffect!= null)
            destroyEffect.dispose();
    }

}