package co.aeons.zombie.shooter.gameObjects;

import co.aeons.zombie.shooter.utils.AudioManager;

public class OptionButton extends Button{

    //Variable para guardar si la opción está activada o no
    private boolean desactivated;

    //Variables para guardar las texturas para cuando la opción está activada y cuando no
    private String textureActivated;
    private String textureDesactivated;

    public OptionButton(String textureActivated, String textureDesactivated,int x, int y) {
        super(textureActivated, x, y, null, false);

        this.textureActivated = textureActivated;
        this.textureDesactivated = textureDesactivated;

        desactivated = false;
    }

    public boolean isDesactivated(){
        return desactivated;
    }

    public void setDesactivated(boolean b){
        desactivated = b;
    }

    public boolean press(float x, float y) {
        //Realizamos las comprociones para cambiar el estado del botón en función de si estaba desactivado o no
        if (super.isOverlapingWith(x, y) && desactivated) {
            this.setPressed(true);
            setDesactivated(false);
            return true;
        }

        if (super.isOverlapingWith(x,y) && !desactivated){
            this.setPressed(true);
            setDesactivated(true);
            return true;
        }

        return false;
    }

    public void render() {
        if (desactivated){
            this.setTexture(textureDesactivated);
            super.render();
        } else {
            super.render();
        }

        //En función de si el botón está activado o no, se pintará una textura u otra
        if (this.isPressed() && desactivated){
            this.setTexture(textureDesactivated);
            super.render();

        } else if (this.isPressed() && !desactivated){
            this.setTexture(textureActivated);
            super.render();
        }

    }
}
