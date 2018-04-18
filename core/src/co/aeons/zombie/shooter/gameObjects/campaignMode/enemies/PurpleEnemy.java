package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.gameObjects.campaignMode.enemies.partsOfEnemy.Eye;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.DamageManager;

public class PurpleEnemy extends Enemy {

    private enum PurpleEnemyState{
        APPEAR,OPEN_EYES,OPEN_MAIN_EYE
    }

    // Indica la velocidad a la que se moverá el enemigo
    private final int SPEED = 75;
    // Indica la frecuencia de disparo de los ojos pequeños
    private final float FREQUENCY_EYE_SHOOTING = 1f;
    // Indica el tiempo máximo en el cual el ojo principal estará abierto
    private final float FREQUENCY_MAIN_EYE_OPEN = 3f;
    // Posición que debe de alcanzar para cambiar de estado
    public final int APPEAR_POSITION_X = 480;

    // Tiempo actual del ojo principal abierto
    private float timeMainEyeOpen;
    // Tiempo actual de la frecuencia de disparo
    private float timeToEyeShoot;
    // Estado en el que se encuentra el enemigo
    private PurpleEnemyState state;

    private int selectedEye;

    //Partes referentes a los ojos del enemigo
    private Array<Eye> eyes;

    //Parte del enemigo correspondiente al cuerpo del enemigo
    private PartOfEnemy body;

    private Texture eye_center_closed;
    private Texture eye_center_opened;

    public PurpleEnemy(int x, int y) {
        super("purple_eye_center_opened", x, y, 1100, AssetsManager.loadParticleEffect("purple_destroyed"));

        eye_center_opened = getTexture();
        eye_center_closed = AssetsManager.loadTexture("purple_eye_center_closed");

        state = PurpleEnemyState.APPEAR;

        //Creamos las distintas partes que compondrán al enemigo
        body = new PartOfEnemy("purple_body", x, y, 1,
                AssetsManager.loadParticleEffect("purple_destroyed"), this, false, false);

        eyes = new Array<Eye>();
        eyes.add(new Eye(x + 80, y + (int)body.getHeight()/5     - 32 , this));
        eyes.add(new Eye(x + 30 , y + ((int)body.getHeight()/5)*2 - 32 , this));
        eyes.add(new Eye(x + 30, y + ((int)body.getHeight()/5)*3 - 32, this));
        eyes.add(new Eye(x + 80, y + ((int)body.getHeight()/5)*4 - 32, this));


        //Actualizamos el ojo central para hacerlo conincidir con su posición dentro del cuerpo del enemigo
        this.setX(body.getCenter().x - getWidth() / 2);
        this.setY(body.getCenter().y - getHeight() /2);

        //Inicializamos las variables de control
        timeToEyeShoot  = 0;
        timeMainEyeOpen = 0;
        // Por defecto el ojo seleccionado es el primero
        selectedEye = 0;
    }

    public void update(float delta) {
        super.update(delta);

        if (!this.isDefeated()) {
            switch (state){
                case APPEAR:
                    if(body.getX() >= APPEAR_POSITION_X){
                        this.setX(this.getX() - SPEED * delta);
                        body.setX(body.getX() - SPEED * delta);
                        moveEyes(- SPEED * delta);
                    }else{
                        openAllEyes();

                        // El primer ojo realiza la espera para disparar
                        eyes.get(selectedEye).waitToShoot();

                        //Una vez en la posición designada, el enemigo estará listo para empezar a disparar
                        state = PurpleEnemyState.OPEN_EYES;
                    }
                    break;
                case OPEN_EYES:
                    // Si se ha cumplido el tiempo para disparar, disparamos
                    if(timeToEyeShoot >= FREQUENCY_EYE_SHOOTING){
                        this.shoot();
                        timeToEyeShoot = 0;
                    }else{
                        timeToEyeShoot+=delta;
                    }
                    // Si todos los ojos han sido dañados (tienen sus ojos cerrados)
                    // Cambiamos de estado
                    if(isAllEyesClosed())
                        state = PurpleEnemyState.OPEN_MAIN_EYE;
                    break;
                case OPEN_MAIN_EYE:
                    // Esperamos con el ojo principal abierto para resetear los estados
                    if(timeMainEyeOpen >= FREQUENCY_MAIN_EYE_OPEN){
                        // Abrimos todos los ojos
                        openAllEyes();
                        // Todos los ojos no esperan a disparar
                        changeWaitingStatusAllEyes(false);
                        // Restablememos el ojo seleccionado por defecto
                        selectedEye = 0;
                        // Hacemos que ese ojo por defecto esté esperando para disparar
                        eyes.get(selectedEye).waitToShoot();
                        // Cambiamos el estado del enemigo
                        state = PurpleEnemyState.OPEN_EYES;
                        // Resetear los tiempos de control
                        timeMainEyeOpen = 0;
                        timeToEyeShoot  = 0;
                    }else{
                        timeMainEyeOpen+=delta;
                    }
                    break;
            }
        }
    }

    public Array<PartOfEnemy> getPartsOfEnemy() {
        Array<PartOfEnemy> partsOfEnemy = new Array<PartOfEnemy>();
        partsOfEnemy.add(body);
        partsOfEnemy.addAll(eyes);
        return partsOfEnemy;
    }

    public void render(){
        /*El ojo central (enemigo en sí), solo será visible y por lo tanto dañable cuando los cuatro ojos que disparan
          estén abatidos*/
        if (isAllEyesClosed() && !state.equals(PurpleEnemyState.APPEAR)) {
            if(getTexture().equals(eye_center_closed))
                changeTexture(eye_center_opened);
        }else{
            if(getTexture().equals(eye_center_opened))
                changeTexture(eye_center_closed);
        }

        super.render();
    }

    public void changeToDeletable() {
        super.changeToDeletable();
        body.changeToDeletable();
        for(Eye eye: eyes){
            eye.changeToDeletable();
        }
    }

    public void shoot(){
        /*Comenzamos la secuencia del patrón de disparo disparando el primer ojo y después irán disparando en orden
          cada uno de ellos*/
        eyes.get(selectedEye).shoot();
        eyes.get(selectedEye).setWaitToShoot(false);

        selectedEye = selectNewEye();

        eyes.get(selectedEye).setWaitToShoot(true);

    }

    private int selectNewEye(){
        int result = -1;

        // Elementos auxiliares para la consulta
        Array<Integer> aux = new Array<Integer>();

        // Filtramos los eyes por los que están abiertos aún
        for(int i = 0; i<eyes.size ; i++){
            if(!eyes.get(i).isClosed()){
                aux.add(i);
            }
        }

        // En este punto "aux" está formado por las posiciones de eyes que están disponibles
        // Buscamos ahora el ojo siguiente disponible
        // Si la longitud de los ojos disponibles es mayor que 1 elemento
        if(aux.size > 1){
            // Si el ojo seleccionado actual es el último de la lista de disponibles
            // escogemos el primero de dicha lista
            if ( selectedEye == aux.get(aux.size-1)){
                result = aux.first();
             }else{
                // Recorremos la lista y obtenemos el elemento siguiente del ojo seleccionado
                for(int i = 0; i<aux.size ; i++){
                    if(aux.get(i) > selectedEye){
                        result = aux.get(i);
                        break;
                    }
                }
             }
        }else if (aux.size == 1){
            result = aux.first();
        }
        return result;
    }

    public void collideWithShoot(Shoot shoot) {
        if (this.canCollide() && isAllEyesClosed()) {
            DamageManager.calculateDamage(shoot,this);
        }
    }

    private boolean isAllEyesClosed(){
        boolean result = true;
        for(Eye eye: eyes){
            if(!eye.isClosed()){
                result = false;
                break;
            }
        }
        return result;
    }

    private void closeAllEyes(){
        for(Eye eye: eyes){
            eye.closeEye();
        }
    }

    private void openAllEyes(){
        for(Eye eye: eyes){
            eye.openEye();
        }
    }

    private void moveEyes(float amount){
        for(Eye eye: eyes){
            eye.setX(eye.getX() + amount);
        }
    }

    private void changeWaitingStatusAllEyes(boolean b){
        for(Eye eye: eyes){
            eye.setWaitToShoot(false);
        }
    }

    public void dispose(){
        super.dispose();
        for(Eye eye: eyes){
            eye.dispose();
        }
        body.dispose();
    }

}
