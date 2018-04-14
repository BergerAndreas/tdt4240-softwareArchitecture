package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Torstein on 4/8/2018.
 */

public class FireButton extends GameButton{

    //Creates the listener interface for this button

    public interface FireButtonListener {
        public void onFire();
    }

    private FireButtonListener listener;

    public FireButton(Rectangle bounds, FireButtonListener listener) {
        super(bounds);
        // Assigns listener to this button
        this.listener = listener;
    }

    //Used to define what sprite to draw
    @Override
    protected String getTexturePath() {
        return "fireButton.png";
    }

    //Method called on listener when touched
    @Override
    public void touched() {
        listener.onFire();
    }

}
