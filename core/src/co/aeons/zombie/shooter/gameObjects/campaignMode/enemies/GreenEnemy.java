package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies;

import com.badlogic.gdx.utils.Array;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.gameObjects.campaignMode.shoots.Basic;
import co.aeons.zombie.shooter.gameObjects.campaignMode.shoots.Green;
import co.aeons.zombie.shooter.gameObjects.campaignMode.shoots.GreenFire;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.ShootsManager;
import co.aeons.zombie.shooter.utils.enums.TypeEnemy;

public class GreenEnemy extends Enemy {

    //Indica la velocidad a la que se moverá el enemigo
    private static final int SPEED = 50;

    //Valor inicial que tendrá el contador de disparo cada vez que se reinicie
    public static final int INITIAL_COUNTER = 250;

    // El escudo del enemigo
    private PartOfEnemy shield;

    // Direccion de movimiento
    private int direction;

    //Indicará cuándo a partir de cuándo puede disparar el enemigo
    private boolean isReady;

    //Contador que usaremos para saber cuándo disparar
    private float counter;

    public GreenEnemy(int x, int y) {
        super("green_body", x, y, 600, AssetsManager.loadParticleEffect("green_destroyed"));
        shield = new PartOfEnemy("green_shield", x - 40 ,y - 33, 15,
                                    AssetsManager.loadParticleEffect("green_destroyed"), this, false, true);

        direction = 1;
        isReady = false;
        counter = INITIAL_COUNTER;
    }

    public void update(float delta) {
        super.update(delta);

        if (!this.isDefeated()) {
            // Evitamos que el enemigo se salga de la pantalla tanto por arriba como por abajo
            if(this.getY() + this.getHeight() > SpaceGame.height){
                direction = -1;
            } else if (this.getY() < 0){
                direction = 1;
            } else {
                //Si el enemigo estaba fuera de la pantalla, cambiamos por tanto su estado para que pueda disparar
                isReady = true;
            }

            //Si el enemigo está listo y ha terminado el contador, disparará y lo reiniciamos
            if (isReady && counter <= 0) {
                this.shoot();
                counter = INITIAL_COUNTER;
            } else {
                counter -= delta * SPEED;
            }

            // Movemos las tres partes del enemigo
            this.setY(this.getY() + SPEED * delta * direction);
            shield.setY(shield.getY() + SPEED * delta * direction);
        }
    }

    public Array<PartOfEnemy> getPartsOfEnemy() {
        Array<PartOfEnemy> partsOfEnemy = new Array<PartOfEnemy>();
        partsOfEnemy.add(shield);
        return partsOfEnemy;
    }

    public void render(){
        super.render();
    }

    public void shoot(){
        ShootsManager.shootGreenWeapon(this.shield, 0.0f);
    }

    public void collideWithShoot(Shoot shoot) {
        if (this.canCollide()) {
            if (shoot instanceof Basic){
                //Si el enemigo es alcanzado por un disparo de tipo básico, sólo recibirá un punto de daño
                this.damage(1);
            }else if (shoot instanceof Green || shoot instanceof GreenFire){
                //Si por el contrario, es alcanzado por un disparo de tipo verde, perderá tres puntos de vida
                this.damage(3);
            }
        }
    }

    public void changeToDeletable() {
        super.changeToDeletable();
        shield.changeToDeletable();
    }

    public void dispose(){
        super.dispose();
        shield.dispose();
    }

}
