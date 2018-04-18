package co.aeons.zombie.shooter.gameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector3;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.utils.AudioManager;
import co.aeons.zombie.shooter.utils.FontManager;

public class Button extends GameObject {

    //Indica si el botón ha sido pulsado
    private boolean pressed;

    //Será el texto que habrá en el botón, en el caso de haberlo
    private String content;

    //Almacena la posición del content
    private float contentX;
    private float contentY;

    //Indica si el botón una vez pulsado deberá volverse oscuro
    private boolean haveDarkEffect;

    //Controla si el efecto de sonido al pulsar el botón ya se ejecutó
    private boolean justSound;

    public Button(String texture, int x, int y, String content, boolean haveDarkEffect) {
        super(texture, x, y);

        pressed = false;
        justSound = false;
        this.content = content;
        this.haveDarkEffect = haveDarkEffect;

        //Si tenemos un texto para contenido, lo centramos en el botón
        if (content != null) {
            GlyphLayout layout = new GlyphLayout();
            layout.setText(FontManager.text, FontManager.getFromBundle(content));
            contentX = x + (this.getWidth() - layout.width) / 2;
            contentY = y + (this.getHeight() + layout.height) / 2;
        }
    }

    public String getContent() {
        return content;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void update() {
        if (Gdx.input.justTouched()) {
            Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            v = SpaceGame.camera.unproject(v);

            if (super.isOverlapingWith(v.x, v.y))
                this.setPressed(true);
        }
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    //Si el botón está pulsado, se oscurecerá
    public void render() {
        if (this.isPressed() && haveDarkEffect){
            this.renderDark();
            if (!justSound) {
                AudioManager.playSound("button_forward");
                justSound = true;
            }
        } else {
            super.render();
        }

        if (content != null) {
            FontManager.drawText(content,contentX,contentY);
        }
    }

    //Oscurede el botón dando la impresión de haberse pulsado
    private void renderDark() {
        Color c = SpaceGame.batch.getColor();
        float oldR = c.r;
        float oldG = c.g;
        float oldB = c.b;

        c.r += 0.5;
        c.g += 0.5;
        c.b += 0.5;
        SpaceGame.batch.setColor(c);

        super.render();

        c.r = oldR;
        c.g = oldG;
        c.b = oldB;
        SpaceGame.batch.setColor(c);
    }

}
