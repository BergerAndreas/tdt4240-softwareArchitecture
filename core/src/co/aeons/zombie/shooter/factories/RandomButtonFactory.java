package co.aeons.zombie.shooter.factories;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

import co.aeons.zombie.shooter.entities.buttons.DoublePoints;
import co.aeons.zombie.shooter.entities.buttons.EffectButton;
import co.aeons.zombie.shooter.entities.buttons.InstaKill;
import co.aeons.zombie.shooter.entities.buttons.NukeButton;
import co.aeons.zombie.shooter.entities.buttons.WallHealthButton;
import co.aeons.zombie.shooter.utils.utils;

public class RandomButtonFactory {

    private ArrayList<String> effectButtons;
    private OrthographicCamera cam;

    public RandomButtonFactory(OrthographicCamera cam){
        this.cam = cam;

        //Add new buttons ID's here
        effectButtons = new ArrayList<String>();
        effectButtons.add("nuke");
        effectButtons.add("instakill");
        effectButtons.add("double");
        effectButtons.add("carpenter");
    }

    public EffectButton produceRandomEffectButton(){
        String type = effectButtons.get(utils.randInt(0, effectButtons.size() -1));
        Rectangle bounds = this.getNewBounds(this.cam);

        if(type.equals("nuke")){
            return new NukeButton(bounds);
        }
        if(type.equals("instakill")) {
            return new InstaKill(bounds);
        }
        if(type.equals("double")) {
            return new DoublePoints(bounds);
        }
        if(type.equals("carpenter")) {
            return new WallHealthButton(bounds);
        }
        return null;

    }

    private Rectangle getNewBounds(OrthographicCamera cam) {
        Rectangle bound = new Rectangle(
                utils.randInt(100, (int) cam.viewportWidth - 100),
                utils.randInt(100, (int) cam.viewportHeight - 100),
                cam.viewportWidth / 8,
                cam.viewportHeight / 6
        );

        return bound;
    }

}

