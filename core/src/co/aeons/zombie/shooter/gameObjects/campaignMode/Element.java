package co.aeons.zombie.shooter.gameObjects.campaignMode;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.GameObject;

public class Element extends GameObject {

    //Indica si el jugador ha pulsado en el elemento para colocarlo en el equipamiento
    private boolean isActivate;

    //Efecto de partículas asociado al elemento que se mostrará si isActivate es true
    private ParticleEffect activatedParticleEffect;

    //Se muestra permanentemente encima del elemento
    private ParticleEffect particleEffect;

    public Element(String texture, int x, int y) {
        super(texture, x, y);

        activatedParticleEffect = AssetsManager.loadParticleEffect(texture + "_selected");
        particleEffect = AssetsManager.loadParticleEffect(texture + "_element");
    }

    public void setX(float delta, float x) {
        super.setX(x);
        particleEffect.getEmitters().first().setPosition((getWidth()/2) + getX(), (getHeight()/2) + getY());
        this.update(delta);
    }

    public void render() {
        this.particleEffect.draw(SpaceGame.batch);
        if (isActivate) {
            this.activatedParticleEffect.draw(SpaceGame.batch);
        }
    }

    public void update(float delta) {
        particleEffect.update(delta);
        if (isActivate()) {
            activatedParticleEffect.update(delta);
        }
    }

    public boolean isActivate() {
        activatedParticleEffect.getEmitters().first().setPosition((getWidth()/2) + getX(), (getHeight()/2) + getY());
        return isActivate;
    }

    public void setIsActivate(boolean isActivate) {
        this.isActivate = isActivate;
    }

}
