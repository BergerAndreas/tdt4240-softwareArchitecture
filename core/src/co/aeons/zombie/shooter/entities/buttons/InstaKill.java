package co.aeons.zombie.shooter.actors.buttons;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Danny Duy Nguyen on 09.04.2018.
 */

public class InstaKill extends GameButton{

    public interface InstakillListener {
        public void instaKillActivate();
    }
    private InstakillListener listener;

    public InstaKill(Rectangle bounds, InstakillListener listener) {
        super(bounds);
        this.listener = listener;
    }

    @Override
    protected String getTexturePath() { return "skullPlaceholder.png"; }

    @Override
    public void touched() { listener.instaKillActivate(); }
}
