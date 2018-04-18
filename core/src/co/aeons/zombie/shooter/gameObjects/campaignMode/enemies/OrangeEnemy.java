package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.campaignMode.shoots.Orange;
import co.aeons.zombie.shooter.gameObjects.campaignMode.enemies.partsOfEnemy.Cannon;
import co.aeons.zombie.shooter.screens.CampaignScreen;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.DamageManager;
import co.aeons.zombie.shooter.utils.ShootsManager;
import co.aeons.zombie.shooter.utils.enums.TypeEnemy;


public class OrangeEnemy extends Enemy {

    // Enumerado para mayor comprensión de los estados por los que pasa el enemigo naranaj
    private enum OrangeEnemyState{
        OPENING_SHIELD, SHIELD_OPENED, CLOSING_SHIELD, READY, APPEAR
    }

    // Frecuencia de inicio de las ráfagas de los cañones secundarios
    private static final float  FREQUENCY_OF_BURST = 4f;
    // Número de ráfagas de cañon secundario antes de iniciar el cañon principal
    private static final int NUMBER_OF_SECONDARY_BURSTS = 2;
    // Frecuencia de disparo de cada ráfaga
    private static final float  FREQUENCY_OF_SHOOTING = 1.4f;
    // Número de disparos máximo con los que contaremos cada ráfaga
    private static final int    MAX_OF_SHOOTS_SECUNDARY_CANNON = 4;
    // Frecuencia de tiempo de carga para el disparo del cañon principal
    private static final float  FREQUENCY_OF_SHOOT_MAIN_CANNON = 6f;
    // Maxima velocidad del efecto de particulas que se encarga del efecto de carga
    private static final int    MAXIMUM_VELOCITY_CHARGING_EFFECT = 50;
    // Posicion relativa del escudo con respecto al main Body
    private final float         SHIELD_OFFSET_X;
    // Número de disparos máximo con los que contaremos en la ráfaga del cañon principal
    private static final int    MAX_OF_SHOOTS_MAIN_CANNON = 30;
    // Cantidad a restar al tiempo de carga cuando un disparo golpee al enemigo
    private static final double AMOUNT_TO_SUBSTRACT_TIME_CHARGING_MAIN_CANNON = 0.35;
    // Velocidad de aparición
    private static final int    APPEAR_SPEED = 60;
    // Posición limite de aparición relativa a la posición del cañon principal
    private static final int    APPEAR_POSITION = 530;
    private Array<Integer> aviableSecondaryCannons;

    // Tiempo de carga del cañon principal
    private float   timeChargingMainCannon;
    // Tiempo de ráfaga de disparo
    private float   timeToBurst;
    // Tiempo de disparo dentro de cada ráfaga
    private float   timeToShoot;

    // Número de bolas de fuego disparadas
    private int     shootsFired;
    // Número de ráfagas producidas
    private int     burstsFired;
    // Cañon seleccionado para iniciar la ráfaga de disparo
    private int     selectedCannonToFire;

    // Variable de control para saber si disparamos o no el cañon principal
    private boolean shootMainCannon;
    // Variable de control para saber en que estado actual se encuentra el enemigo
    private OrangeEnemyState orangeEnemyState;

    // Su escudo
    private PartOfEnemy shield;
    // Su cuerpo (parte derecha y centro)
    private PartOfEnemy body;

    // Cañon superior izquierda
    private Cannon cannonUpperLeft;
    // Cañon superior derecha
    private Cannon cannonUpperRight;

    // Cañon inferior izquierda
    private Cannon cannonLowerLeft;
    // Cañon inferior derecha
    private Cannon cannonLowerRight;

    // Efecto de partículas encargado del efecto de carga y de disparo del cañon principal
    private ParticleEffect chargeMainCannonEffect;

    private PartOfEnemy auxTopPart;
    private PartOfEnemy auxBottomPart;

    public OrangeEnemy(int x, int y) {
        super("orange_enemy_main_cannon", x, y, 400, AssetsManager.loadParticleEffect("orange_enemy_defeated"));

        aviableSecondaryCannons = new Array<Integer>();

        chargeMainCannonEffect = AssetsManager.loadParticleEffect("orange_main_cannon_charging");

        // Creación y posicionamiento del cuerpo
        body = new PartOfEnemy("orange_enemy_body",x,y,7,AssetsManager.loadParticleEffect("basic_destroyed"),this, false, true);
        body.setX(x - 10);
        body.setY( (y + getHeight()/2) - body.getHeight()/2 ) ;

        // Creación y posicionamiento del escudo
        shield = new PartOfEnemy("orange_enemy_shield",x,y,7,AssetsManager.loadParticleEffect("basic_destroyed"),this, false, true);
        SHIELD_OFFSET_X = 22;
        shield.setX(body.getX() - SHIELD_OFFSET_X);

        shield.setY((y + getHeight()/2) - shield.getHeight()/2);

        // Creación y posicionamiento de los cañones
        float xPosition = x;
        float yPosition = (y + getHeight()/2) + body.getHeight()/3 + 10;

        cannonUpperLeft = new Cannon(xPosition, yPosition , this, xPosition, yPosition+30, -50, 150);

        xPosition = body.getCenter().x - cannonUpperLeft.getWidth()/2 - 12;
        yPosition = body.getCenter().y + body.getHeight()/2 - 5;

        cannonUpperRight = new Cannon(xPosition, yPosition, this, xPosition+15, yPosition+30, -90 , 90);

        xPosition = x;
        yPosition = (y + getHeight()/2) - body.getHeight()/2 - 7 ;

        cannonLowerLeft = new Cannon(xPosition, yPosition, this, xPosition, yPosition+10, 50, 230);

        xPosition = body.getCenter().x - cannonUpperLeft.getWidth()/2 - 12;
        yPosition = body.getCenter().y - body.getHeight()/2 - cannonUpperRight.getHeight() + 5;

        cannonLowerRight = new Cannon(xPosition, yPosition, this, xPosition, yPosition, 90 , 270);

        resetStates();
        orangeEnemyState = OrangeEnemyState.APPEAR;

        aviableSecondaryCannons = new Array<Integer>();

        super.updateParticleEffect();
        chargeMainCannonEffect.getEmitters().first().setPosition(this.getX()-10,this.getY()+this.getHeight()/2);
        chargeMainCannonEffect.start();

        createAuxiliaryParts(x,y);

    }

    private void createAuxiliaryParts(int x, int y){
        auxBottomPart   = new PartOfEnemy(null,0,0,1,null,this,false,true);
        auxTopPart      = new PartOfEnemy(null,0,0,1,null,this,false,true);


        float[] bodyVertices = body.getLogicShape().getVertices();

        // Vertices para la parte superior
        float[] topVertices = new float[]{
                bodyVertices[4],
                bodyVertices[5],
                bodyVertices[6],
                bodyVertices[7],
                bodyVertices[8],
                bodyVertices[9],
        };

        // Vertices para la parte inferior
        float[] bottomVertices = new float[]{
                bodyVertices[0],
                bodyVertices[1],
                bodyVertices[2],
                bodyVertices[3],
                bodyVertices[bodyVertices.length-2],
                bodyVertices[bodyVertices.length-1],
        };

        // Aplicar esos vertices a cada parte
        auxBottomPart.getLogicShape().setVertices(bottomVertices);
        auxTopPart.getLogicShape().setVertices(topVertices);

        // Crear los nuevos vertices del body
        float[] newBodyVertices = new float[18];
        int j=0;
        for(int i=8; i < bodyVertices.length ;i++){
            newBodyVertices[j] = bodyVertices[i];
            j++;
        }

        // Posicionar las partes auxiliares
        auxBottomPart.setX(x - 10);
        auxBottomPart.setY(y - body.getHeight()/2 + 25);

        auxTopPart.setX(x - 10);
        auxTopPart.setY(y - body.getHeight()/2 + 25);

        // Establecer los nuevos vertices del body
        body.getLogicShape().setVertices(newBodyVertices);
    }

    // Método usado para resetear todas las variables de control y de estados
    private void resetStates(){
        timeToBurst             = 0;
        timeToShoot             = 0;
        shootsFired             = 0;
        burstsFired             = 0;
        timeChargingMainCannon  = 0;
        selectedCannonToFire    = 0;

        shootMainCannon = false;
    }

    public void update(float delta){
        super.update(delta);
        super.updateParticleEffect();

        // Preguntamos cual es el estado por el que está pasando el enemigo
        switch(orangeEnemyState){
            case OPENING_SHIELD:
                // Abrimos el escudo
                shield.setX(shield.getX() + 10*delta);
                if(shield.getX() >= body.getX()+50){
                    orangeEnemyState = OrangeEnemyState.SHIELD_OPENED;
                }
                // Seguimos disparando los cañones secundarios
                shootSecondaryCannonBurst(delta);
                break;
            case SHIELD_OPENED:
                chargeMainCannonEffect.update(delta);
                // Si no hay que disparar el cañon principal
                if(!shootMainCannon){
                    // Cargamos el cañon
                    if(timeChargingMainCannon >= FREQUENCY_OF_SHOOT_MAIN_CANNON){
                        // El cañon está cargado y preparamos las variables que vamos a necesitar
                        // para disparar el cañon principal
                        // reutilizaremos el proceso de ráfaga de los cañones secundarios
                        shootMainCannon = true;
                        timeToBurst=0;
                        timeToShoot=0;
                        shootsFired=0;
                        burstsFired=0;
                    }else{
                        timeChargingMainCannon+=delta;
                        // Aumentamos la velocidad del efecto de particulas
                        // para dar una sensación de "engorde" conforme pasa el tiempo
                        // v = 10 + ( (tiempoActual * MaximaVelocidad) / frecuenciaDeEspera )
                        chargeMainCannonEffect.getEmitters().first().getVelocity().setHigh(10+((timeChargingMainCannon*MAXIMUM_VELOCITY_CHARGING_EFFECT)/(FREQUENCY_OF_SHOOT_MAIN_CANNON)));
                    }
                }else{
                    // Disminuimos la velocidad del efecto de particulas
                    // para dar una sensación de que está gastando toda su energia en los disparos
                    // v = ( cantidadDisparosMaxima - (disparosProducidos + 1) * MaximaVelocidad ) / cantidadDisparosMaxima
                    chargeMainCannonEffect.getEmitters().first().getVelocity().setHigh((( ( (MAX_OF_SHOOTS_MAIN_CANNON - (shootsFired + 1)) * MAXIMUM_VELOCITY_CHARGING_EFFECT) / MAX_OF_SHOOTS_MAIN_CANNON) ));
                    // Disparamos
                    this.shoot(delta);
                }
                break;
            case CLOSING_SHIELD:
                // Cerramos el escudo
                shield.setX(shield.getX() - 10*delta);
                if(shield.getX() <= (body.getX() - SHIELD_OFFSET_X) ){
                    shield.setX(body.getX() - SHIELD_OFFSET_X);
                    // Una vez cerrado reseteamos las variables
                    resetStates();
                    orangeEnemyState = OrangeEnemyState.READY;
                }
                break;
            case READY:
                // Si no estamos aún en ningun estado de transición
                // Disparamos los cañones secundarios
                shootSecondaryCannonBurst(delta);
                // Si ya hemos producido tres rafagas
                if(burstsFired >= NUMBER_OF_SECONDARY_BURSTS){
                    // Abrimos el escudo
                    orangeEnemyState = OrangeEnemyState.OPENING_SHIELD;
                }
                break;
            case APPEAR:
                // Tenemos que hacer aparecer al enemigo
                if(this.getX() > APPEAR_POSITION)
                    moveEnemyWithAppearSpeed(delta);
                else
                    // Si ya hemos alcanzado la posición, el enemigo está listo para actuar
                    orangeEnemyState = OrangeEnemyState.READY;
                break;
        }
    }

    private void shoot(float delta){
        // Si se ha cumplido el tiempo de disparo
        // (el tiempo de disparo para el cañon principal
        // es la quinta parte que la del cañon secundario)
        if(timeToShoot >= FREQUENCY_OF_SHOOTING/5){
            float angle = MathUtils.random(110,250);
            ShootsManager.shootOneOrangeWeapon(this,(int)(getX() - getWidth() + 20),(int)(this.getY()+this.getHeight()/2),angle, CampaignScreen.ship,0);
            timeToShoot=0;
            shootsFired++;
            // Si hemos disparado ya todas las bolas de fuego
            if(shootsFired>= MAX_OF_SHOOTS_MAIN_CANNON){
                // Empezamos a cerrar el escudo
                shootMainCannon=false;
                orangeEnemyState = OrangeEnemyState.CLOSING_SHIELD;
                timeChargingMainCannon = 0;
            }
        }else{
            timeToShoot+=delta;
        }
    }

    private void shootSecondaryCannonBurst(float delta){
        // Esperamos a que sea el momento de iniciar una ráfaga
        if(timeToBurst > FREQUENCY_OF_BURST){
            if(selectedCannonToFire == 0)
                selectCannonToFire();
            // Dentro de cada ráfaga esperamos a que sea el momento de cada disparo
            if(timeToShoot > FREQUENCY_OF_SHOOTING){
                // Disparamos según que cañon fue seleccionado
                switch (selectedCannonToFire){
                    case 1:
                        cannonUpperLeft.shoot();
                        break;
                    case 2:
                        cannonUpperRight.shoot();
                        break;
                    case 3:
                        cannonLowerLeft.shoot();
                        break;
                    case 4:
                        cannonLowerRight.shoot();
                        break;
                }
                shootsFired++;
                timeToShoot = 0;
                // Si ya hemos disparado todas las bolas de fuego
                if(shootsFired >= MAX_OF_SHOOTS_SECUNDARY_CANNON){
                    // Reseteamos para empezar una nueva ráfaga e incrementamos
                    // el número de ráfagas disparadas
                    selectedCannonToFire = 0;
                    shootsFired = 0;
                    burstsFired++;
                    timeToBurst = 0;
                }
            }else{
                timeToShoot+=delta;
            }
        }else{
            timeToBurst+=delta;
        }
    }

    public void render() {
        super.render();
        // Solo si el escudo está abierto se visualizará el efecto de carga
        if(orangeEnemyState.equals(OrangeEnemyState.SHIELD_OPENED) && !isDefeated())
            chargeMainCannonEffect.draw(SpaceGame.batch);
    }

    public void collideWithShoot(Shoot shoot) {
        if(shoot instanceof Orange) {
            // Con cada golpe del shoot, restamos en un valor el tiempo de carga del cañon principal
            // retrasando así nuestra muerte
            timeChargingMainCannon -= AMOUNT_TO_SUBSTRACT_TIME_CHARGING_MAIN_CANNON;
        }
        DamageManager.calculateDamage(shoot,this);

        if (this.getVitality() % 200 == 0)
            orangeEnemyState = OrangeEnemyState.CLOSING_SHIELD;
    }

    private void moveEnemyWithAppearSpeed(float delta){
        this.setX(this.getX() - APPEAR_SPEED*delta);

        shield.setX(shield.getX() - APPEAR_SPEED*delta);

        body.setX(body.getX() - APPEAR_SPEED*delta);
        auxTopPart.setX(auxTopPart.getX() - APPEAR_SPEED*delta);
        auxBottomPart.setX(auxBottomPart.getX() - APPEAR_SPEED*delta);

        cannonLowerLeft.move(-APPEAR_SPEED*delta);
        cannonUpperLeft.move(-APPEAR_SPEED*delta);
        cannonLowerRight.move(-APPEAR_SPEED*delta);
        cannonUpperRight.move(-APPEAR_SPEED*delta);

        chargeMainCannonEffect.getEmitters().first().setPosition(this.getX()-10,this.getY()+this.getHeight()/2);
    }

    private void selectCannonToFire(){
        if(!cannonUpperLeft.isDisable())
            aviableSecondaryCannons.add(1);
        if(!cannonUpperRight.isDisable())
            aviableSecondaryCannons.add(2);
        if(!cannonLowerLeft.isDisable())
            aviableSecondaryCannons.add(3);
        if(!cannonLowerRight.isDisable())
            aviableSecondaryCannons.add(4);

        selectedCannonToFire = aviableSecondaryCannons.random();

        aviableSecondaryCannons.clear();
    }

    public void changeToDeletable() {
        super.changeToDeletable();

        shield.changeToDeletable();

        body.changeToDeletable();

        cannonLowerLeft.changeToDeletable();
        cannonLowerRight.changeToDeletable();

        cannonUpperLeft.changeToDeletable();
        cannonUpperRight.changeToDeletable();

        auxTopPart.changeToDeletable();
        auxBottomPart.changeToDeletable();
    }

    public PartOfEnemy getShield(){
        return shield;
    }

    public PartOfEnemy getBody(){
        return body;
    }

    public PartOfEnemy getCannonUpperLeft(){
        return cannonUpperLeft;
    }
    public PartOfEnemy getCannonUpperRight(){
        return cannonUpperRight;
    }
    public PartOfEnemy getCannonLowerLeft(){
        return cannonLowerLeft;
    }
    public PartOfEnemy getCannonLowerRight(){
        return cannonLowerRight;
    }

    public void collideWithShip() {}

    public Array<PartOfEnemy> getPartsOfEnemy() {
        Array<PartOfEnemy> result = new Array<PartOfEnemy>();

        result.add(getShield());
        result.add(getBody());
        result.add(auxBottomPart);
        result.add(auxTopPart);

        result.add(getCannonUpperLeft());
        result.add(getCannonUpperRight());
        result.add(getCannonLowerLeft());
        result.add(getCannonLowerRight());

        return  result;
    }

}
