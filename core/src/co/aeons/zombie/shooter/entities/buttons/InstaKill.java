package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;
import co.aeons.zombie.shooter.gamestates.PlayState;

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
        return "buttons/skullPlaceholder.png";
    }

    @Override
    public void effect(PlayState playState) {
        playState.setDamageModifier(100000);
        playState.setEffectTimer(500);
    }
}
