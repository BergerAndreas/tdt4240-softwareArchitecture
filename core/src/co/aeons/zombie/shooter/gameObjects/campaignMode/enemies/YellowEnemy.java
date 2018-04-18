package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.screens.CampaignScreen;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.campaignMode.CampaignShip;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.gameObjects.campaignMode.shoots.Yellow;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.DamageManager;
import co.aeons.zombie.shooter.utils.enums.TypeEnemy;

public class YellowEnemy extends Enemy{

    // Velocidad del enemigo
    private static final int SPEED = 200;
    // A continuación se definen las variables para el efecto sinusoidal o "ola"
    // Velocidad para el cambio en grados
    private static final int INCREMENTAL_DEGREE_SPEED = 150;
    // Amplitud del efecto sinusoidal
    private static final int WAVE_AMPLITUDE = 70;

    // Tiempo que transcurre desde que ha recibido daño hasta que se para a esperar
    private final float WAIT_TO_STOPS = 1f;
    // Tiempo en el que el enemigo ni avanza ni retrocede, luego avanza al objetivo
    private final float WAIT_TO_GO_AGAIN = 1f;

    private float timeToWaitToStops;
    private float timeWaittingToGoAgain;

    // Punto de inserción de la pantalla donde ya se "tirá" hacia el punto en el que estuviese la nave
    private static final float INSERTION_POINT=0.15f;

    // Vector de movimiento del enemigo
    private Vector2 movement;

    // Cambia el movimiento cuando colisiona con un shoot
    private int movementXDirection;

    // Variable de control para saber si se ha alcanzado el punto de "inserción"
    // Con punto de inserción nos referimos a: el vector de movimiento no cambiará
    private boolean positionReached;

    // Guardamos hacía donde está girando ahora mismo, si es hacia arriba (true) o hacia abajo (false)
    private boolean waveDirection;
    // Variable para guardar cuantos grados de inclinación va teniendo
    private float waveDegree;

    private CampaignShip target;

    public YellowEnemy(int x, int y) {
        super("yellow_enemy", x, y, 30, AssetsManager.loadParticleEffect("yellow_enemy_defeated"));

        target = CampaignScreen.ship;

        movement = new Vector2();

        // Hacemos un Random entre -WAVE_AMPLITUDE y WAVE_AMPLITUDE
        // Para otorgar variedad en el inicio del efecto sinusoidal
        waveDegree = MathUtils.random(-WAVE_AMPLITUDE,WAVE_AMPLITUDE);
        // Hacemos un Random para saber en que dirección inicial se iniciará
        // el efecto
        waveDirection = MathUtils.randomBoolean();

        positionReached = false;

        movementXDirection = 1;
        timeToWaitToStops = 0;
        timeWaittingToGoAgain = 0;
    }

    public void update(float delta){
        super.update(delta);
        // Mientras la nave no haya sido derrotada
        if(!isDefeated()){
            // Mientras la posición de "incursión" no haya sido alcanzada
            if(!positionReached){
                // Situamos el punto de ataque en la punta del enemigo amarillo
                float srcx = this.getX() + this.getWidth()/2;
                float srcy = this.getY();

                // Calculamos el vector desde el punto origen al punto destino (centro de la nave)
                movement.set(target.getX()+target.getWidth()/2 - srcx, target.getY() - srcy);

                // Calculamos el vector movimiento que debe de hacer el enemigo
                // Efecto sinusoidal -> Y = speed * sen(angle+wave)
                movement.set(SPEED * delta * MathUtils.cosDeg(movement.angle()),
                        SPEED * delta * MathUtils.sinDeg(movement.angle() + waveDegree));

                // Actualizamos el efecto sinusoidal
                calculateWaveEffect(delta);

                // Actualizamos la posición del enemigo
                // Si ha recibido daño el movimiento de dirección X es el invertido y volverá hacía atrás
                if(movementXDirection == -1){
                    // Volverá hacía atrás un tiempo hasta que se pare
                    if(timeToWaitToStops<WAIT_TO_STOPS){
                        this.setX(this.getX() + (movement.x * movementXDirection));
                        timeToWaitToStops+=delta;
                    }
                }else{
                    this.setX(this.getX() + (movement.x));
                }
                this.setY(this.getY() + movement.y);

                // Comprobamos si el punto de inserción ha sido alcanzado
                if(getX()/SpaceGame.width < INSERTION_POINT)
                    positionReached=true;

                // Si ya se ha esperado el tiempo de espera para lanzarse, lo hacemos reseteando los campos de control
                if(timeToWaitToStops>=WAIT_TO_STOPS && timeWaittingToGoAgain>=WAIT_TO_GO_AGAIN){
                    timeToWaitToStops = 0;
                    timeWaittingToGoAgain = 0;
                    movementXDirection=1;
                }else
                    timeWaittingToGoAgain+=delta;
            }else{
                // Añadimos un plus del 20% de velocidad a la nave para el movimiento de inserción
                movement.set(SPEED*1.2f * delta * MathUtils.cosDeg(movement.angle()),
                        SPEED*1.2f * delta * MathUtils.sinDeg(movement.angle()));
                this.setX(this.getX() + movement.x);
                this.setY(this.getY() + movement.y);
            }
        }
    }

    private void calculateWaveEffect(float delta){
        if(waveDirection){
            waveDegree +=INCREMENTAL_DEGREE_SPEED*delta;
            if (waveDegree >= WAVE_AMPLITUDE *getX()/SpaceGame.width)
                waveDirection = false;
        }
        else{
            waveDegree -=INCREMENTAL_DEGREE_SPEED*delta;
            if (waveDegree < -WAVE_AMPLITUDE *getX()/SpaceGame.width)
                waveDirection = true;
        }
    }

    public void collideWithShip(){
        this.damage(this.getVitality());
    }

    public void collideWithShoot(Shoot shoot){
        if(shoot instanceof Yellow){
            movementXDirection=-1;
        }
        DamageManager.calculateDamage(shoot,this);
    }

    public void render(){
        if(!isDefeated())
            super.renderRotate(movement.angle());
        else
            super.render();
    }
}
