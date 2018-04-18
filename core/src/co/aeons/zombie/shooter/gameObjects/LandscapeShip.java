package co.aeons.zombie.shooter.gameObjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.MultiplayerShip;
import co.aeons.zombie.shooter.screens.CampaignScreen;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.CameraManager;
import co.aeons.zombie.shooter.utils.ScreenManager;
import co.aeons.zombie.shooter.utils.enums.GameState;

public class LandscapeShip extends GameObject {

    //Indica el máximo de golpes que se puede recibir
    protected final int VITALITY;
    //Indicará la duración del estado invulnerable
    private final float DURATION_UNDAMAGABLE = 2.0f;
    //Indica el rango por el que se moverá el timeForInvisible
    private final int RANGE_INVISIBLE_TIMER = 5;

    //Indica la cantidad de golpes recibidos
    protected int damageReceived;
    //Variable usada para hacer la nave invulnerable cuando es golpeada
    protected boolean undamagable;
    //Se usará como contador para volver la nave vulnerable
    private float timeToUndamagable;

    //Imagen de la cabina que irá sobre la nave y que se actualizará con los daños
    protected Texture cockpit;
    protected int cockpitOffsetX,cockpitOffsetY;
    //Sirve para indicar los tiempos en los que la nave parpadeará a ser invulnerable
    private int timeForInvisible;

    //Efecto de partículas para el fuego de la nave
    protected ParticleEffect fireEffect;

    public ParticleEffect destroyEffect;

    public LandscapeShip(String textureName, int x, int y, int vitality) {
        super(textureName, x, y);
        VITALITY = vitality;

        timeForInvisible = RANGE_INVISIBLE_TIMER;
        damageReceived = 0;
        timeToUndamagable = DURATION_UNDAMAGABLE;

        cockpit = AssetsManager.loadTexture("cockpit");
        cockpitOffsetX = 35;
        cockpitOffsetY = 17;

        //Creamos el efecto de partículas del fuego
        fireEffect = AssetsManager.loadParticleEffect("propulsion_ship_effect");

        destroyEffect = AssetsManager.loadParticleEffect("ship_defeated");

        //Lo iniciamos, pero aunque lo iniciemos si no hay un update no avanzará
        fireEffect.start();
        destroyEffect.start();

        updateParticleEffect();
    }

    protected void updateParticleEffect() {
        fireEffect.getEmitters().first().setPosition(this.getX() - 5,this.getY() + this.getHeight()/2 - 5);
        destroyEffect.getEmitters().first().setPosition(this.getCenter().x,this.getCenter().y);
    }

    public void update(float delta){
        //Actualizamos la posición del efecto de particulas de acuerdo con la posición del shooter
        this.updateParticleEffect();

        if(this.isDefeated())
            destroyEffect.update(delta);
        else{
            //Actualizamos el efecto de particulas
            fireEffect.update(delta);

            //Si la nave está en estado invulnerable, el contador se reduce y actualizamos el valor de timeForInvisible
            if (this.isUndamagable()) {
                //timeForInvisible irá saltando de uno en uno de un valor negativo a positivo según el rango, y vuelta a empezar
                if (timeForInvisible >= RANGE_INVISIBLE_TIMER) {
                    timeForInvisible = -RANGE_INVISIBLE_TIMER;
                }
                timeForInvisible++;
                timeToUndamagable -= delta;
            }

            if (timeToUndamagable <= 0)
                this.changeToDamagable();
        }
    }

    public void render(){
        if(isDefeated())
            destroyEffect.draw(SpaceGame.batch);
        else{
            /* Mostramos la nave si:
             * -> La nave no está en modo invulnerable
             * -> La nave está en modo invulnerable y timeForInvisible es positivo
             * -> Estamos en el modo campaña y el estado está en pause
             */
            if (!this.isUndamagable() || (timeForInvisible > 0) || (ScreenManager.isCurrentScreenEqualsTo(CampaignScreen.class) && ScreenManager.isCurrentStateEqualsTo(GameState.PAUSE))) {
                super.render();
                SpaceGame.batch.draw(cockpit, this.getX() + cockpitOffsetX, this.getY() + cockpitOffsetY, 40,10);
            }
            if( !(this instanceof MultiplayerShip))
                fireEffect.draw(SpaceGame.batch);
        }
    }

    //Aumenta en uno el daño a la nave, la vuelve invunerable y cambia la apariencia de la cabina
    public void receiveDamage() {
        if (!undamagable) {
            damageReceived++;
            if (damageReceived < VITALITY) {
                CameraManager.startShake();
                cockpit = AssetsManager.loadTexture("cockpit_damage" + damageReceived);
                undamagable = true;
            }

            //Hacemos vibrar el móvil para hacer más patente el daño
            Gdx.input.vibrate(300);
        }
    }


    //Indica si la nave está derrotada, es decir, que el daño recibido sea mayor o igual a la vitalidad
    public boolean isDefeated() {
        return damageReceived >= VITALITY;
    }

    public boolean isCompletelyDefeated(){
        return destroyEffect.isComplete();
    }

    public int getDamageReceived(){
        return damageReceived;
    }

    public void setDamageReceived(int damageReceived){
        this.damageReceived = damageReceived;
        if(damageReceived == 0)
            cockpit = AssetsManager.loadTexture("cockpit");
        else
            cockpit = AssetsManager.loadTexture("cockpit_damage" + damageReceived);
    }

    //Inicia si la navez puede recibir daños
    public boolean isUndamagable() {
        return undamagable;
    }

    //Hace que la nave pueda volver a recibir daños en el caso en que estuviese vulnerable
    public void changeToDamagable() {
        undamagable = false;
        timeToUndamagable = DURATION_UNDAMAGABLE;
    }

    @Override
    public void dispose() {
        super.dispose();
        cockpit.dispose();
        fireEffect.dispose();
        destroyEffect.dispose();
    }
}
