package co.aeons.zombie.shooter.gameObjects.campaignMode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.gameObjects.campaignMode.enemies.PartOfEnemy;
import co.aeons.zombie.shooter.utils.ShapeRendererManager;
import co.aeons.zombie.shooter.utils.enums.TypeEnemy;

public class Enemy extends GameObject {

    //Indica si el enemigo se puede borrar de la pantalla, que será cuando termine el destroyEffect
    private boolean deletable;

    //Indica la posición inicial del enemigo
    private Vector2 initialPosition;

    //Indica la vitalidad del enemigo
    private int vitality;

    //Es el efecto de partículas que se mostrará al destruirse
    private ParticleEffect destroyEffect;

    private float timeToFlick;
    private float timeForInvisible;
    //Indica el rango por el que se moverá el timeForInvisible
    private static final int RANGE_INVISIBLE_TIMER = 3;

    private static final float DURATION_FLICK = 0.25f;

    //Lo usamos para activar el efecto de localización (circulo rojo alrededor del enemigo)
    //Sólo las armas que apunten a un enemigo tendrán este efecto. (Naranja)
    private boolean targettedByShip;

    public Enemy(String textureName, int x, int y, int vitality, ParticleEffect destroyEffect) {
        super(textureName, x, y);
        initialPosition = new Vector2(x,y);
        this.vitality = vitality;
        deletable = false;
        this.destroyEffect = destroyEffect;
        timeToFlick = 0;
        timeForInvisible = RANGE_INVISIBLE_TIMER;

        targettedByShip = false;

        if(this.destroyEffect != null){
            this.destroyEffect.getEmitters().first().setPosition(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
            this.destroyEffect.start();
        }
    }

    public void update(float delta) {
        //Sólo se hará si el enemigo ha sido derrotado, y se encargará de mostrar el efecto de partículas
        if (this.isDefeated()) {
            this.updateParticleEffect();
            if (destroyEffect.isComplete()) {
                this.changeToDeletable();
            }
            destroyEffect.update(delta);
        } else {
            //Si el tiempo de parpadeo no ha acabado, lo reducimos y actualizamos timeForInvisible
            if (timeToFlick > 0) {
                //timeForInvisible irá saltando de uno en uno de un valor negativo a positivo según el rango, y vuelta a empezar
                if (timeForInvisible >= RANGE_INVISIBLE_TIMER) {
                    timeForInvisible = -RANGE_INVISIBLE_TIMER;
                }
                timeForInvisible++;
                timeToFlick -= delta;
            }
        }
    }

    public void updateParticleEffect() {
        destroyEffect.getEmitters().first().setPosition(this.getX() + this.getWidth()/2, this.getY() + this.getHeight()/2);
    }

    public void render() {
        if (!this.isDefeated()) {
            //El enemigo se pintará si se ha acabado el tiempo de parpadeo o no es momento de estar invisible
            if (timeToFlick <= 0 || timeForInvisible > 0) {
                super.render();

                // Si el enemigo es objetivo de un arma, pintamos el efecto de localización
                if(targettedByShip){

                    float radius = getWidth();
                    if(getHeight() > radius)
                        radius = getHeight();

                    ShapeRendererManager.renderCircle(getX()+getWidth()/2, getY()+getHeight()/2,radius, Color.RED);
                }
            }
        } else {
            destroyEffect.draw(SpaceGame.batch);
        }
    }

    public void renderRotate(float n) {
        if (!this.isDefeated()) {
            //El enemigo se pintará si se ha acabado el tiempo de parpadeo o no es momento de estar invisible
            if (timeToFlick <= 0 || timeForInvisible > 0) {
                super.renderRotate(n);

                // Si el enemigo es objetivo de un arma, pintamos el efecto de localización
                if(targettedByShip){

                    float radius = getWidth();
                    if(getHeight() > radius)
                        radius = getHeight();

                    ShapeRendererManager.renderCircle(getCenter().x, getCenter().y,radius, Color.RED);
                }
            }
        } else {
            destroyEffect.draw(SpaceGame.batch);
        }
    }

    //Daña al enemigo con la cantidad indicada por parámetro
    public void damage(int damage) {
        vitality -= damage;
        timeToFlick = DURATION_FLICK;
    }

    //Indica si el enemigo ha sido derrotado
    public boolean isDefeated(){
        return vitality <= 0;
    }

    //Cambia el enemigo a un estado de borrable
    public void changeToDeletable() {
        deletable = true;
    }

    //Indica si el enemigo es borrable o no
    public boolean isDeletable() {
        return deletable;
    }

    //Devuelve la posición inicial del enemigo dentro de la pantalla
    public Vector2 getInitialPosition(){
        return initialPosition;
    }

    //Devuelve una lista con los PartOfEnemy en caso de tener alguno
    //Debe ser sobreescrito por los Enemy que tengan PartOfEnemy
    public Array<PartOfEnemy> getPartsOfEnemy() {
        return new Array();
    }

    public boolean canCollide() {
        return true;
    }

    public void collideWithShip() {}

    public void collideWithShoot(Shoot shoot) {
    }

    public int getVitality(){
        return vitality;
    }

    public void setVitality(int v){
        vitality = v;
    }
    //Usado para activar o desactivar el efecto de localización
    public void setTargettedByShip(boolean b){
        targettedByShip = b;
    }

    public void dispose() {
        super.dispose();
        destroyEffect.dispose();
    }
}
