package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.managers.Jukebox;


/**
 * Created by Erikkvo on 12-Apr-18.
 */

public class MuteButton extends GameButton {

    public MuteButton(Rectangle bounds) {
        super(bounds);
        // Assigns listener to this button
    }

    //Used to define what sprite to draw
    @Override
    protected String getTexturePath() {
        if (Jukebox.isMuted()) {
            return "buttons/muted.png";
        }
        return "buttons/unmuted.png";
    }

}
