package co.aeons.zombie.shooter.gameObjects.campaignMode;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.utils.enums.TypeElement;

public class Slot extends GameObject {

    //Tipo de elemento que se ha colocado en el slot
    private TypeElement equippedElement;

    //Efecto de partícula en el caso de tener equipado un elemento
    private ParticleEffect particleEffect;

    public Slot(String texture, int x, int y) {
        super(texture, x, y);

        equippedElement = TypeElement.COLORLESS;
        particleEffect = null;
    }

    public void setX(float delta, float x) {
        super.setX(x);
        this.updateParticleEffect(delta);
    }

    public void setY(float delta, float y) {
        super.setY(y);
        this.updateParticleEffect(delta);
    }

    public void render() {
        if (this.hasElementEquipped()) {
            this.particleEffect.draw(SpaceGame.batch);
        }
    }

    //Recoloca el efecto de partícula sobre la posición del slot
    public void updateParticleEffect(float delta) {
        if (particleEffect != null) {
            particleEffect.getEmitters().first().setPosition((getWidth() / 2) + getX(), (getHeight() / 2) + getY());
            this.update(delta);
        }
    }

    public void update(float delta) {
        if (this.hasElementEquipped()) {
            particleEffect.update(delta);
        }
    }

    //Devuelve true si hay algún elemento equipado
    public boolean hasElementEquipped() {
        return !equippedElement.equals(TypeElement.COLORLESS);
    }

    //Devuelve true si tiene equipado el elemento que se pasa como parámetro
    public boolean hasSpecifiedElement(TypeElement type) { return equippedElement.equals(type); }

    public void equipElement(TypeElement type) {
        equippedElement = type;
        if (!type.equals(TypeElement.COLORLESS)) {
            String color = "red";

            if (type.equals(TypeElement.RED))
                color = "red";
            else if (type.equals(TypeElement.BLUE))
                color = "blue";
            else if (type.equals(TypeElement.YELLOW))
                color = "yellow";

            particleEffect = AssetsManager.loadParticleEffect(color + "_equipped");
            particleEffect.getEmitters().first().setPosition((getWidth() / 2) + getX(), (getHeight() / 2) + getY());
        }
    }

    public void unequip() {
        equippedElement = TypeElement.COLORLESS;
        particleEffect = null;
    }

}
