package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.managers.Jukebox;


/**
 * Created by Erikkvo on 12-Apr-18.
 */

public class MuteButton extends GameButton {

    //Creates the listener interface for this button
    public interface MuteButtonListener {
        public void onMute();
    }

    private MuteButton.MuteButtonListener listener;

    public MuteButton(Rectangle bounds, MuteButton.MuteButtonListener listener) {
        super(bounds);
        // Assigns listener to this button
        this.listener = listener;
    }

    //Used to define what sprite to draw
    @Override
    protected String getTexturePath() {
        if(Jukebox.isMuted()){
            return "buttons/muted.png";
        }
        return "buttons/unmuted.png";
    }

    //Method called on listener when touched
    @Override
    public void touched() {
        listener.onMute();
    }

}
