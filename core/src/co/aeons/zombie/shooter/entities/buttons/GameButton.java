package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Torstein on 4/8/2018.
 */

public abstract class GameButton extends Button {

    protected Rectangle bounds;

    public GameButton(Rectangle bounds) {
        this.bounds = bounds;
        setWidth(bounds.width);
        setHeight(bounds.height);
        setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
        loadTextureRegion();
    }

    public void loadTextureRegion() {
        ButtonStyle style = new ButtonStyle();
        String texture = getTexturePath();
        if(getTexturePath() == null){
            texture = "weapons/pistol1.png";
        }
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture(texture)));
        setStyle(style);
    }

    protected abstract String getTexturePath();

    public Rectangle getBounds() {
        return bounds;
    }

}