package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.gamestates.PlayState;

public class DoublePoints extends EffectButton {

    public DoublePoints(Rectangle bounds) {
        super(bounds);
    }

    @Override
    protected String getTexturePath() {
        return "buttons/double_points.png";
    }

    @Override
    public void effect(PlayState playState) {
        playState.setEffectTimer(500);
        playState.setScoreModifier(2);
    }

    @Override
    public void playSound() {

    }

    @Override
    public String getButtonType() {
        //Returns d for double points
        return "d";
    }
}
