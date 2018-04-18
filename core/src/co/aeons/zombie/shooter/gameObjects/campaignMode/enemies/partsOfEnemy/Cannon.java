package co.aeons.zombie.shooter.gameObjects.campaignMode.enemies.partsOfEnemy;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.screens.CampaignScreen;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.gameObjects.campaignMode.enemies.PartOfEnemy;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.ShootsManager;

public class Cannon extends PartOfEnemy {

    // Frecuencia del efecto de inhabilitación
    private final float FREQUENCY_OF_DISABLE_EFFECT = 30f;
    // Máxima emisión para el efecto de inhabilitación
    private final int   MAX_EMISSION_DISABLE_EFFECT = 250;
    // Máxima vitalidad del cañon
    private final int   MAX_VITALITY = 20;

    // Tiempo para volver a habilitar el cañon
    private float timeDisableEffect;
    // Posición de disparo
    private Vector2 shootingPosition;
    // Angulo de disparo
    private float shootAngle;
    // Efecto de particulas de inhabilitación
    private ParticleEffect disabledEffect;
    // Inhabilitado o no
    private boolean disable;

    private Enemy father;

    public Cannon(float x, float y, Enemy father, float xShoot, float yShoot, float angle , float shootAngle) {
        super("orange_enemy_cannon", 0, 0, 20, AssetsManager.loadParticleEffect("basic_destroyed"), father, true, true);

        this.setX(x);
        this.setY(y);

        shootingPosition = new Vector2(xShoot,yShoot);
        this.shootAngle = shootAngle;

        this.getLogicShape().rotate(angle);

        timeDisableEffect = 0;

        this.father = father;

        disabledEffect = AssetsManager.loadParticleEffect("orange_secondary_cannon_disabled");
        disabledEffect.getEmitters().first().setPosition(shootingPosition.x,shootingPosition.y);
        disabledEffect.getEmitters().first().getAngle().setLow(shootAngle-30,shootAngle+30);

    }

    public void shoot(){
        // Disparamos
        if(!disable)
            ShootsManager.shootOneOrangeWeapon(this, (int)shootingPosition.x, (int)shootingPosition.y, shootAngle, CampaignScreen.ship,0);
    }
    public void move(float speed){
        this.setX(this.getX() + speed);
        disabledEffect.getEmitters().first().setPosition(getCenter().x,getCenter().y);
        shootingPosition.x += speed;
    }
    public void update(float delta){
        super.update(delta);
        if(disable){
            disabledEffect.update(delta);
            // Esperamos a que haya pasado el tiempo necesario para volver a habilitar el cañon
            if(timeDisableEffect >= FREQUENCY_OF_DISABLE_EFFECT){
                // Reseteamos el efecto y variables de control
                // Ademas le recuperamos la vida
                this.setVitality(MAX_VITALITY);
                timeDisableEffect = 0;
                disable=false;
                disabledEffect.reset();
            }else{
                timeDisableEffect+=delta;
                // Reducimos la cantidad de emisión del efecto
                disabledEffect.getEmitters().first().getEmission().setHigh((
                        (FREQUENCY_OF_DISABLE_EFFECT - timeDisableEffect )
                                * MAX_EMISSION_DISABLE_EFFECT)
                        / FREQUENCY_OF_DISABLE_EFFECT);
            }
        }

    }
    public void render() {
        super.render();
        if (disable && !father.isDefeated() )
            disabledEffect.draw(SpaceGame.batch);
    }

    public boolean isDisable(){
        return disable;
    }
    public void collideWithShoot(Shoot shoot) {
        if(damageable){
            if(!disable)
                this.damage(1);

            if(getVitality() < 3)
                disable = true;
        }
    }
}