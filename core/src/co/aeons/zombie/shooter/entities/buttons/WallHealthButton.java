package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.gamestates.PlayState;

public class WallHealthButton extends EffectButton {
    public WallHealthButton(Rectangle bounds) {
        super(bounds);
    }

    @Override
    protected String getTexturePath() {
        return "buttons/carpenter.png";
    }

    @Override
    public void effect(PlayState playState) {
        playState.increaseWallHealth(100);
    }

    @Override
    public void playSound() {

    }

    @Override
    public String getButtonType() {
        //Returns h for health
        return "h";
    }
}
