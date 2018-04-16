package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.ZombieShooter;

public class Wall extends SpaceObject {

    private Texture wallTexture;
    private int health;

    public Wall() {

        //TODO: MOVE TO GAME CONSTANTS
        this.x = 75;
        this.y = 0;
        this.width = 50;
        this.height = ZombieShooter.HEIGHT;

        this.bounds = new Rectangle(x,y,width,height);

        this.wallTexture = new Texture("logo.png");
        this.health = 500;
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(wallTexture, this.x, this.y, this.width, this.height);
        batch.end();
    }

    public void takeDamage(int damage){
        this.health -= damage;
        System.out.println(this.health);
    }

    public int getHealth() {
        return health;
    }
}


//spar blekk. ikke bruk mange new lines pless!










// Bli pult torstain



