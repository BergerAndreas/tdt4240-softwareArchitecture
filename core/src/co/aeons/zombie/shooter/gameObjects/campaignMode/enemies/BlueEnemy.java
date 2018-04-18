package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies;


import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.DamageManager;
import co.aeons.zombie.shooter.utils.ShootsManager;
import co.aeons.zombie.shooter.utils.enums.TypeEnemy;

public class BlueEnemy extends Enemy {

    //Indica la velocidad a la que se moverá el enemigo
    public static final int SPEED = 200;

    //Valor inicial que tendrá el contador de disparo cada vez que se reinicie
    public static final int INITIAL_COUNTER = 300;

    //Indica la dirección de de movimiento
    private int direction;

    //Indicará cuándo a partir de cuándo puede disparar el enemigo
    private boolean isReady;

    //Contador que usaremos para saber cuándo disparar
    private float counter;

    public BlueEnemy(int x, int y) {
        super("blue_enemy", x, y, 200, AssetsManager.loadParticleEffect("blue_destroyed"));

        //Inicialmente lo pondremos que va hacia arriba, sólo por darle un valor válido
        direction = 1;

        isReady = false;
        counter = INITIAL_COUNTER;
    }

    public void update(float delta){
        super.update(delta);

        if (!this.isDefeated()) {

            // Evitamos que el enemigo se salga de la pantalla tanto por arriba como por abajo
            if(this.getY() + this.getHeight() > SpaceGame.height){
                direction = -1;
            } else if(this.getY() < 0){
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

            //Movemos el enemigo
            this.setY(this.getY() + (SPEED * delta * direction));
        }
    }

    public void shoot(){
        ShootsManager.shootBlueWeapon(this, 200);
    }

    public void collideWithShoot(Shoot shoot) {
        DamageManager.calculateDamage(shoot,this);
    }

}
