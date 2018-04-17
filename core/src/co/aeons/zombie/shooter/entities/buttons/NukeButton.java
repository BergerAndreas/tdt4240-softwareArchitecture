package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import co.aeons.zombie.shooter.entities.Zombie;
import co.aeons.zombie.shooter.gamestates.PlayState;

public class NukeButton extends EffectButton{

    public NukeButton(Rectangle bounds) {
        super(bounds);
    }

    //Nuke effect: kill all zombies on screen
    public void effect(ArrayList<Zombie> zombies) {
        zombies.clear();
}

    @Override
    protected String getTexturePath() {
        return "buttons/nukeButton.png";
    }

    @Override
    public void effect(PlayState playState) {

        playState.getZombies().clear();

    }

}
