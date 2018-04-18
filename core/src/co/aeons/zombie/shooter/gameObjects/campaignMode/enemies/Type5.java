package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.DamageManager;
import co.aeons.zombie.shooter.utils.ShootsManager;
import co.aeons.zombie.shooter.utils.enums.TypeEnemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;

public class Type5 extends Enemy{

    //Indica la velocidad a la que se moverá el enemigo
    public static final int SPEED = 50;

    //Frencuencia de disparo se refiere a cada cuantos segundos va a disparar
    private static final float FREQUENCY_OF_SHOOTING = 3f;

    //Tiempo necesario para que dispare
    private float timeToShoot;

    //Variable de control para saber si ha disparado o no
    private boolean hasShooted;

    //Variable para el efecto de partículas que indica si el enemigo esta preparado para disparar
    private ParticleEffect shootEffectWarning;

    public Type5(int x, int y) {
        super("type5", x, y, 350, AssetsManager.loadParticleEffect("basic_type5_destroyed"));
        timeToShoot = FREQUENCY_OF_SHOOTING;
        hasShooted = false;

        //Creamos el efecto de particulas
        shootEffectWarning = AssetsManager.loadParticleEffect("warning_shoot_type5_effect");
        this.updateParticleEffect();

        //Iniciamos el efecto de partículas
        shootEffectWarning.start();
    }

    public void update(float delta){
        super.update(delta);

        if (!this.isDefeated()) {
            //Mientras el enemigo tenga distancia que recorrer hasta un punto fijo dado, vamos actualizando su posición
            if (this.getX() >= 450) {
                this.setX(this.getX() - SPEED * delta);
            } else {
                //Si hemos sobrepasado el tiempo para disparar
                if (timeToShoot < 0) {
                    //Si no ha disparado aún el enemigo
                    if (!hasShooted) {
                        //El enemigo dispara y lo hacemos saber a la variable de control
                        this.shoot();
                        hasShooted = true;
                    } else {
                        //Reseteamos todos los tiempos y controles
                        hasShooted = false;
                        timeToShoot = FREQUENCY_OF_SHOOTING;
                    }
                } else {
                    //Actualizamos el tiempo para disparar
                    timeToShoot -= delta;

                    //Actualizamos el efecto de partículas
                    this.updateParticleEffect();
                    shootEffectWarning.update(delta);
                }
            }
        }
    }

    public void updateParticleEffect() {
        if (this.isDefeated()){
            super.updateParticleEffect();
        }else {
            shootEffectWarning.getEmitters().first().setPosition(this.getX(), this.getY() + this.getHeight() / 2);        }
    }

    public void shoot(){
        ShootsManager.shootOneType5Weapon(this);
    }

    public void render(){
        //Si el enemigo no ha disparado pintamos el efecto de partículas
        if(!this.hasShooted && !this.isDefeated())
            shootEffectWarning.draw(SpaceGame.batch);
        super.render();
    }

    public void dispose() {
        super.dispose();
        shootEffectWarning.dispose();
    }

    public void collideWithShoot(Shoot shoot) {DamageManager.calculateDamage(shoot,this);
    }

}
