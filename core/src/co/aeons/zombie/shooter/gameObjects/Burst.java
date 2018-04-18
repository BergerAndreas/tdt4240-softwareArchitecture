package co.aeons.zombie.shooter.gameObjects;

import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.RivalShip;
import co.aeons.zombie.shooter.screens.CampaignScreen;
import co.aeons.zombie.shooter.utils.ScreenManager;
import co.aeons.zombie.shooter.utils.ShootsManager;
import co.aeons.zombie.shooter.utils.enums.TypeShoot;

public class Burst {

    //Será necesario para hacer una ráfaga de disparos básicos
    private int numberOfShoots;

    //Guarda el último disparo realizado en una ráfaga
    private Shoot lastShootOfBurst;

    //Guarda el punto de partida del último disparo realizado en una ráfaga
    private float startPoint;

    //Tipo de arma a hacer la ráfaga (naranja o basica)
    private TypeShoot typeToBurst;

    //Si hubiese un objetivo de la ráfaga, lo guardamos aqui
    private GameObject burstTarget;
    //

    private GameObject burstShooter;

    //Dentro del efecto ráfaga existe un factor de aparición que definiremos
    //en el método burst. A mayor número mayor tiempo entre disparos
    //Menos de 1.0 los disparos se soperponen
    private static double aparitionFactor;

    private boolean endShooting;

    public Burst(GameObject shooter, int number, float start, TypeShoot typeburst, GameObject target, double factor){

        numberOfShoots          = number;
        startPoint              = start;
        typeToBurst             = typeburst;
        burstTarget             = target;
        burstShooter            = shooter;
        aparitionFactor         = factor;

        lastShootOfBurst = null;
        endShooting = false;
    }

    //Actualiza el estado de la ráfaga ee disparo que haya en pantalla
    public void updateBurst(LandscapeShip landscapeShip) {
        //Si estamos en medio de una ráfaga de la nave, continuamos disparando si es el momento
        if (numberOfShoots > 0) {
            //Disparamos un nuevo shoot en la ráfaga si no hubo un último, o bien la distancia recorrida por el
            //último es superior a su punto de inicio más su ancho por 1.3)

            if (lastShootOfBurst == null ||
                    lastShootOfBurst.getX() > startPoint + (lastShootOfBurst.getWidth() * aparitionFactor) ||
                    (burstShooter instanceof Enemy &&
                            lastShootOfBurst.getX() < (startPoint - (lastShootOfBurst.getWidth()*aparitionFactor))) ||
                    (burstShooter instanceof RivalShip &&
                            lastShootOfBurst.getX() < (startPoint - (lastShootOfBurst.getWidth()*aparitionFactor)))) {
                if(typeToBurst.equals(TypeShoot.BASIC)){
                    if(ScreenManager.isCurrentScreenEqualsTo(CampaignScreen.class))
                        lastShootOfBurst = ShootsManager.shootOneBasicWeapon(burstShooter);
                    else
                        lastShootOfBurst = ShootsManager.shootOneBasicMultiplayerWeapon(burstShooter);
                }

                else if(typeToBurst.equals(TypeShoot.ORANGE))
                    lastShootOfBurst = ShootsManager.shootOneOrangeWeapon(landscapeShip,(int)(landscapeShip.getX() + landscapeShip.getWidth()),(int) (landscapeShip.getY() + landscapeShip.getHeight()/2), 45f ,burstTarget, numberOfShoots);
                numberOfShoots -= 1;
                startPoint = lastShootOfBurst.getX();
                //Si acabamos de lanzar el último disparo de la ráfaga, no lo guardamos
                if (numberOfShoots == 0){
                    lastShootOfBurst = null;
                    // Si el burst era de un tipo naranja, desactivamos el efecto de localización.
                    if(typeToBurst.equals(TypeShoot.ORANGE) && burstTarget instanceof Enemy){
                        Enemy enemy = (Enemy) burstTarget;
                        enemy.setTargettedByShip(false);
                    }
                    endShooting = true;
                }
            }
        }
    }

    public boolean isEndShooting(){
        return endShooting;
    }
}
