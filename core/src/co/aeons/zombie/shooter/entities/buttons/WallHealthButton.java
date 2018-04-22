package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.gamestates.PlayState;
import co.aeons.zombie.shooter.managers.Jukebox;

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
        playState.getWall().increaseWallHealth(100);
    }

    @Override
    public void playSound() {
        Jukebox.play("repair");
    }

    @Override
    public String getButtonType() {
        //Returns h for health
        return "h";
    }
}
