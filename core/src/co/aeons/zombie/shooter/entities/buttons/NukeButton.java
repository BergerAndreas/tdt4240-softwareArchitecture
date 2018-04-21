package co.aeons.zombie.shooter.entities.buttons;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import co.aeons.zombie.shooter.entities.Zombie;
import co.aeons.zombie.shooter.gamestates.PlayState;
import co.aeons.zombie.shooter.managers.Jukebox;

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

    @Override
    public void playSound() {
        Jukebox.play("rawSauce");
    }

    @Override
    public String getButtonType() {
        //Returns N for nuke
        return "n";
    }

}
