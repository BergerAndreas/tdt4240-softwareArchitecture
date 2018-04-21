package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.Jukebox;

public class Wall extends SuperObject {

    private Texture wallTexture;
    private int wallHealth;

    public Wall() {

        //TODO: MOVE TO GAME CONSTANTS
        this.x = 75;
        this.y = 0;
        this.width = 50;
        this.height = ZombieShooter.HEIGHT;

        this.bounds = new Rectangle(x,y,width,height);

        this.wallTexture = new Texture("walls/redBrick.jpg");
        this.wallHealth = 5000;
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(wallTexture, this.x, this.y, this.width, this.height);
        batch.end();
    }

    public void takeDamage(int damage){
        this.wallHealth -= damage;
    }

    public int getWallHealth() {
        return wallHealth;
    }
}
