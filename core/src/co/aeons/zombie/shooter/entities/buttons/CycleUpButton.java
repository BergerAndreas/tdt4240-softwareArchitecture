package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

public class CycleUpButton extends GameButton {

    public CycleUpButton(Rectangle bounds) {
        super(bounds);
    }

    @Override
    protected String getTexturePath() {
        return "buttons/arrow_right.png";
    }
}
