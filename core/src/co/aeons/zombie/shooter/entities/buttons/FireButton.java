package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Torstein on 4/8/2018.
 */

public class FireButton extends GameButton {

    //Creates the listener interface fo r this button

    public FireButton(Rectangle bounds) {
        super(bounds);
        // Assigns listener to this button
    }

    //Used to define what sprite to draw
    @Override
    protected String getTexturePath() {
        return "buttons/fireButton.png";
    }

}
