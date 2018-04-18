package co.aeons.zombie.shooter.gameObjects.campaignMode.shoots;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.campaignMode.CampaignShip;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.enums.TypeShoot;

public class Red extends Shoot{

    //Velocidad de movimiento
    public static final int SPEED = 800;

    /*Tiempo necesario a esperar para que empiece a moverse el disparo
     Se trata de un delay de aparición*/
    private float timeToMove;

    //Variable para guardar el tamaño del disparo a pintar en pantalla, así conseguimos pintar el disparo de manera dinámica
    private int pixelsToDraw;

    //Variable para guardar la textura
    private TextureRegion texture;

    //Efecto de particulas de este disparo
    private ParticleEffect shoot;

    public Red(GameObject shooter, int x, int y) {
        super("red_shoot", x, y, shooter,
                AssetsManager.loadParticleEffect("red_shoot_effect_shoot"),
                AssetsManager.loadParticleEffect("red_effect_shock"),"shoot_red_FX");

        //Creamos el efecto de particulas
        shoot = AssetsManager.loadParticleEffect("red_shoot_effect");

        this.updateParticleEffect();

        //Los iniciamos, pero aunque los iniciemos si no haya un update no avanzarán
        shoot.start();

        //Inicializamos las variables para pintar el disparo a trozos y no la textura entera
        texture = new TextureRegion(this.getTexture());
        texture.setRegion(0,0,10,this.getHeight());
        timeToMove = 0f;
        pixelsToDraw = 0;
    }

    public void updateParticleEffect() {
        super.updateParticleEffect();

        //Comprobamos si el disparo ha chocado
        if (!this.isShocked()) {
            //Se actuará de forma distinta si el shooter es enemigo o no
            if (this.getShooter() instanceof CampaignShip) {
                shoot.getEmitters().first().setPosition(this.getX() - 10,this.getY() + 3);
            } else if (this.getShooter() instanceof Enemy){
                shoot.getEmitters().first().setPosition(this.getX(), this.getY());

                //Rotamos el efecto de particulas 180º
                shoot.getEmitters().first().getAngle().setHigh(180,180);
            }
        }
    }

    public void update(float delta) {
        if (!this.isShocked()) {
            //Esperaremos a que sea el momento correcto para moverse
            if (timeToMove < 0) {
                //Actualizamos el movimiento del disparo
                if (getShooter() instanceof CampaignShip) {
                    this.setX(this.getX() + (SPEED * delta));
                }else{
                    this.setX(this.getX() - (SPEED * delta));
                }
                //Actualizamos la posición del efecto de particulas de acuerdo con la posición del shooter
                this.updateParticleEffect();

                //Actualizamos el efecto de particulas
                shoot.update(delta);
                super.update(delta);
            }
            //Mientras no sea el momento para moverse
            else {
                //Vamos a ir restando el tiempo de delay con el delta hasta que sea menor que 0
                timeToMove -= pixelsToDraw;
                //Podemos además ir actualizando la posición Y por si el shooter se está moviendo
                this.setY(getShooter().getY() + getShooter().getHeight() / 2 - this.getHeight() / 2);
            }
        } else {
            // Actualizamos el efecto de particulas
            this.updateParticleEffect();
            super.update(delta);
        }

    }

    public void render(){
        super.render();
        if (!this.isShocked()) {
            //Vamos pintando parte por parte del disparo, en lugar de que salga la textura entera
            if(pixelsToDraw<=texture.getRegionWidth()){
                texture.setRegion(0,0,pixelsToDraw,texture.getRegionHeight());
                pixelsToDraw += 10;
            }
            SpaceGame.batch.draw(texture, this.getX(), this.getY());
            if(timeToMove < 0) {
                shoot.draw(SpaceGame.batch);
            }
        }
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
        shoot.dispose();
    }
}
