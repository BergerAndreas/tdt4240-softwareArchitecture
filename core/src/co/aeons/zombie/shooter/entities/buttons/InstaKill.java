package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Danny Duy Nguyen on 09.04.2018.
 */

public class InstaKill extends EffectButton {

    public InstaKill(Rectangle bounds) {
        super(bounds);
    }

    public void effect(){
        System.out.println("pew pew");
    }

    @Override
    protected String getTexturePath() {
        return "skullPlaceholder.png";
    }

    @Override
    public void effect(PlayState playState) {
        System.out.println("Dåse");
    }
}
