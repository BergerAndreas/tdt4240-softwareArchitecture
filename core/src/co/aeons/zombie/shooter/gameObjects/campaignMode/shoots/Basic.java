package co.aeons.zombie.shooter.gameObjects.campaignMode.shoots;

import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;

public class Basic extends Shoot {

    // Velocidad de movimiento
    public static final int SPEED = 600;

    public Basic(GameObject shooter, int x, int y) {
        // Situamos el disparo en el sitio correcto
        // X - Extremo derecha del shooter
        // Y - La mitad del alto del shooter - la mitad del alto del disparo
        super("basic_shoot",x,y,shooter,
                AssetsManager.loadParticleEffect("basic_effect_shoot"),
                AssetsManager.loadParticleEffect("basic_effect_shoot"),"shoot_basic_FX");
        this.updateParticleEffect();
    }

    public void updateParticleEffect() {
        super.updateParticleEffect();
    }

    public void update(float delta) {
        if (!this.isShocked()) {
            // Actualizamos el movimiento del disparo
            if (getShooter() instanceof Enemy) {
                this.setX(this.getX() - (SPEED * delta));
            } else {
                this.setX(this.getX() + (SPEED * delta));
            }
        }

        this.updateParticleEffect();

        //Actualizamos los efectos de partículas
        super.update(delta);
    }

    @Override
    public void render(){
        super.render();
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
    }
}
