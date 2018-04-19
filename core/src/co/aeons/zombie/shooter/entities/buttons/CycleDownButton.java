package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

public class CycleDownButton extends GameButton {
    public CycleDownButton(Rectangle bounds) {
        super(bounds);
    }

    @Override
    protected String getTexturePath() {
        return "buttons/arrow_left.png";
    }
}
