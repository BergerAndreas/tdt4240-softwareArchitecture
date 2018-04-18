package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies;

import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.DamageManager;
import co.aeons.zombie.shooter.utils.ShootsManager;
import co.aeons.zombie.shooter.utils.enums.TypeEnemy;

public class RedEnemy extends Enemy{

    //Indica la velocidad a la que se moverá el enemigo
    public static final int SPEED = 200;

    //Direccion de movimiento para el eje X
    private int directionX;

    //Direccion de movimiento para el eje Y
    private int directionY;

    //Punto de inicio para el movimiento romboide
    private static final float ENTRY_POINT = SpaceGame.width / 1.2f;

    //Punto de referencia sobre el eje X para el movimiento romboide
    private static final float X_REFERENCE = 500;

    //Punto de referencia sobre el eje Y para el movimiento romboide
    private static final float Y_REFERENCE = 200;

    public RedEnemy(int x, int y) {
        super("red_enemy", x, y, 40, AssetsManager.loadParticleEffect("red_destroyed"));
    }

    public void update(float delta){
        super.update(delta);

        if (!this.isDefeated()) {
            //Comprobamos que el enemigo ha llegado al punto donde comenzará con el patrón romboide desde fuera de la pantalla, sólo la primera vez que comienza a moverse
            if (this.getX() >= ENTRY_POINT && directionY == 0) {
                directionX = -1;
            }else {
                //Para el resto de veces, se aplicarán los cambios posteriormente descritos
                if (directionY==0){
                    //Para evitar fallos la primera vez que se ejecuta el patrón imponemos que suba en el eje Y, sólo la primera vez
                    directionY = 1;
                }

                //Comprobamos que la posición Y del enemigo sea menor que un umbral desigando y que se estuviera movimendo negativamente sobre el eje X
                if (this.getY() < Y_REFERENCE && directionX == -1) {
                    //Cambiamos su movimiento sobre el eje X para hacerlo positivo
                    directionX = 1;
                    //Dispara en esta posición
                    this.shoot();
                    //Recolocamos su posición Y para que siempre tenga el mismo tamaño su movimiento romboide
                    this.setY(Y_REFERENCE);
                } else
                //Comprobamos que se estuviera movimiendo positivamente sobre el eje X, además de que se encuentre en el umbral para cambiar de dirección
                if ((this.getY() > Y_REFERENCE || this.getX() >= ENTRY_POINT) && directionX == 1) {
                    //Cambiamos su movimiento sobre el eje X para hacerlo negativo
                    directionX = -1;
                    //Dispara en esta posición
                    this.shoot();
                    //Recolocamos su posición Y para que siempre tenga el mismo tamaño su movimiento romboide
                    this.setY(Y_REFERENCE);
                }

                //Comprobamos que la posicion X del enemigo sea mayor que un umbral designado y que se estuviera moviendo negativamente sobre el eje Y
                if (this.getX() > X_REFERENCE && directionY == -1) {
                    //Cambiamos su movimiento sobre el eje Y para hacerlo positivo
                    directionY = 1;
                    //Dispara en esta posición
                    this.shoot();
                    //Recolocamos su posición X para que siempre tenga el mismo tamaño su movimiento romboide
                    this.setX(X_REFERENCE);
                } else
                //Comprobamos que que se estuviera moviendo positivamente sobre el eje Y y que su posición X fuera menor que un umbral designado
                if (this.getX() < X_REFERENCE && directionY == 1) {
                    //Cambiamos su movimiento sobre el eje Y para hacerlo negativo
                    directionY = -1;
                    //Dispara en esta posición
                    this.shoot();
                    //Recolocamos su posición X para que siempre tenga el mismo tamaño su movimiento romboide
                    this.setX(X_REFERENCE);
                }
            }

            //Movemos al enemigo describiendo un patrón romboide, gracias a las direcciones positivas o negativas tanto en el eje X como en el eje Y
            this.setY(this.getY() + SPEED * delta * directionY);
            this.setX(this.getX() + SPEED * delta * directionX);
        }
    }

    public void shoot(){
        ShootsManager.shootRedWeapon(this);
    }

    public void collideWithShoot(Shoot shoot) {
        DamageManager.calculateDamage(shoot,this);
    }
}
