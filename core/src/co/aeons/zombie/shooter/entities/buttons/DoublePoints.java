package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.gamestates.PlayState;
import co.aeons.zombie.shooter.managers.Jukebox;

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
        Jukebox.play("quickMafs");
    }

    @Override
    public String getButtonType() {
        //Returns d for double points
        return "d";
    }
}
