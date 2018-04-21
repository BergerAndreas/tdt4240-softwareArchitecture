package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.gamestates.PlayState;
import co.aeons.zombie.shooter.managers.Jukebox;

public abstract class EffectButton extends GameButton {


    public EffectButton(Rectangle bounds) {
        super(bounds);
    }

    @Override
    protected abstract String getTexturePath();

    public abstract void effect(PlayState playState);

    public abstract void playSound();

    public abstract String getButtonType();

}
