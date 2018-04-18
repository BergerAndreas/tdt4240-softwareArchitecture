package co.aeons.zombie.shooter.gameObjects.campaignMode.shoots;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.campaignMode.CampaignShip;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;

public class Rocket extends Shoot {

    //Velocidad de movimiento
    protected float speed;

    //Constante que se multiplicará por la velocidad cuando el misil gire
    public static final int ACCELERATION = 70;

    //Efecto de particulas del fuego de propulsión
    protected ParticleEffect propulsionEffect;

    //Indica el punto objetivo adonde se dirigirá la nave
    protected float xTarget;

    //Indica el punto central entre el sitio donde se dispara y el punto donde se pretende ir
    protected float xCenter;
    protected float yCenter;

    //Almacenará el centro del círculo que servirá como referencia para cada giro
    protected float xCenterOfCircle;

    //Radio del círculo que servirá de referencia para el giro
    protected float diameter;

    //Almacenará los grados para calcular el giro
    protected float degrees;

    //Velocidad a la que girará el misil
    protected float speed_for_circle;

    //Servirá para indicar si el giro va en el sentido de las agujas del reloj o al revés
    protected int clockwiseDirection;

    //Indicará si el misil va a la derecha (1) o a la izquierda (-1)
    protected int direction;

    //Servirá para impedir que la nave gire si el yTarget es apenas diferentemente apreciable al Y del shooter
    protected boolean canRotate;

    public Rocket(String textureName, GameObject shooter, int x, int y, float yTarget, int speed, ParticleEffect shootEffect,
                  ParticleEffect shockEffect, ParticleEffect propulsionEffect) {
        super(textureName,x,y,shooter,shootEffect,shockEffect,"shoot_rocket_FX");

        this.propulsionEffect = propulsionEffect;
        this.speed = speed;

        //Comprobamos si el punto objetivo está arriba o abajo de la posición de tiro
        //Esto servirá para saber cuántos son los grados iniciales y dónde estará el punto central
        if (yTarget >= y) {
            degrees = 270;
            yCenter = ((yTarget - y) / 2) + y;
        } else {
            degrees = 90;
            yCenter = ((y - yTarget) / 2) + yTarget;
        }

        //Sabiendo la altura del punto central, calculamos el diámetro que tendrá el círculo de referencia para el giro
        diameter = Math.abs(yCenter - y);

        //Dependiendo de si es la nave o no el shooter, el disparo vendrá de la derecha o la izquierda
        if (shooter instanceof CampaignShip) {
            direction = 1;
            xTarget = SpaceGame.width - (SpaceGame.width / 8);
            xCenter = ((xTarget - x) / 2) + x;
            xCenterOfCircle = xCenter - diameter;
        } else {
            direction = -1;
            xTarget = SpaceGame.width / 8;
            xCenter = ((x - xTarget) / 2) + xTarget;
            xCenterOfCircle = xCenter + diameter;
        }

        //La velocidad del círculo dependerá completamente del diámetro del círculo de giro
        speed_for_circle = speed * ACCELERATION / diameter;

        //Damos un valor inicial, aunque realmente no servirá para nada
        clockwiseDirection = 1;

        //Si la distancia entre la posición del shooter y el yTarget no supera el 8% de la pantalla, el disparo irá recto
        if (Math.abs(yTarget - (shooter.getY() + (shooter.getHeight()/2))) < (SpaceGame.height * 8 / 100)) {
            canRotate = false;
        } else {
            canRotate = true;
        }

        this.updateParticleEffect();
        propulsionEffect.start();
    }

    public void update(float delta) {
        if (!this.isShocked()) {

            //Comprobamos si el shooter no está dentro del rango donde debe girar o si debe rotar
            if (Math.abs(xCenter - this.getX()) > diameter || !this.canRotate) {
                //El shooter está fuera del rango de giro, así que simplemente avanzamos
                this.setX(this.getX() + (speed * delta * direction));
            } else {
                //El shooter está dentro del rango de giro

                //Si el punto central es superior a la posición del misil, el sentido de giro será el de las agujas del reloj
                if (yCenter > this.getY()) {
                    clockwiseDirection = 1;
                } else {
                    clockwiseDirection = -1;
                }

                //Actualizamos los grados a girar en base a la velocidad, dependiente de delta y en el sentido adecuado
                degrees += speed_for_circle * delta * clockwiseDirection;

                //Comprobamos si los grados sale del rango de 0 a 360, lo que indicará que el misil terminó el primer giro
                if (degrees > 360 || degrees < 0) {
                    //Ahora preparamos el inicio del segundo giro

                    //Colocamos el centro del círculo listo para el siguiente giro
                    xCenterOfCircle = xCenter + (diameter * direction);

                    //Cambiamos a 180 grados que es el primer estado del segundo giro
                    degrees = 180;

                    //Recolocamos la X para asegurarnos que en la siguiente vuelta se saltará el if del primer giro
                    this.setX(xCenter);

                    //Ajustamos para que la posición Y sobre pase el yCenter y no tengamos problemas para que clockwiseDirection se actualice
                    this.setY((float) (yCenter + 0.1 * clockwiseDirection));
                } else {
                    //En caso de no ser el comienzo del segundo giro, simplemente continuamos el giro normalmente
                    this.setX(xCenterOfCircle + (diameter * MathUtils.cosDeg(degrees)) * direction);
                    this.setY(yCenter + (diameter * MathUtils.sinDeg(degrees)));
                }
            }
        }

        //Actualizamos los efectos de partículas
        this.updateParticleEffect();
        super.update(delta);
        if (!this.isShocked()) {
            propulsionEffect.update(delta);
        }
    }

    public void updateParticleEffect() {
        super.updateParticleEffect();

        //Comprobamos si el disparo ha chocado
        if (!this.isShocked()) {
            //Se actuará de forma distinta si el shooter es enemigo o no
            if (this.getShooter() instanceof Enemy) {
                propulsionEffect.getEmitters().first().setPosition(this.getX() + this.getWidth(),this.getY() + this.getHeight()/2);

                // Rotamos el efecto de particulas 180º
                propulsionEffect.getEmitters().first().getAngle().setHigh(0);
            } else {
                // Lo centramos con la nave para que salga en la posición de su cañón
                propulsionEffect.getEmitters().first().setPosition(this.getX(),this.getY() + this.getHeight()/2);
            }
        }
    }

    public void render(){
        super.render();
        if (!this.isShocked()) {
            propulsionEffect.draw(SpaceGame.batch);
        }
    }

    public void collideWithShip() {
        super.collideWithShip();
        this.shock();
    }

    public void collideWithEnemy(Enemy enemy) {
        super.collideWithEnemy(enemy);
        this.shock();
    }

    public void collideWithShoot(Shoot shoot) {
        super.collideWithShoot(shoot);
        this.shock();
    }

    public void dispose(){
        super.dispose();
        propulsionEffect.dispose();
    }

}
