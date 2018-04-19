package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Trump extends Zombie {
    public Trump(float x, float y, int difficulty) {
        super(x, y, difficulty);
        this.dx = -10;
        this.health = 50;
    }

    private void createIdleAnimation() {
        // TODO: 17/04/2018 Mekk Trump sprite 
        //Opens textureAtlas containing enemy spritesheet information
        runningAtlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));
        //Fetches all sprites matchin keyword 'spoder'
        runningAnimation =
                new Animation<TextureRegion>(
                        0.1f,
                        runningAtlas.findRegions("spoder"),
                        Animation.PlayMode.LOOP
                );
        //Initializes statetime for this animation
        stateTimeRunning = 0f;
    }
}
