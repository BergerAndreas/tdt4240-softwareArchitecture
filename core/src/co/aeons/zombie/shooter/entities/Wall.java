package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import co.aeons.zombie.shooter.ZombieShooter;

public class Wall extends SpaceObject{

    private Texture wallTexture;
    private int health;

    public Wall(){

        //TODO: MOVE TO GAME CONSTANTS
        this.x = 75;
        this.y = 0;
        this.width = 50;
        this.height = ZombieShooter.HEIGHT;
        this.wallTexture = new Texture("logo.png");
        this.health = 500;

        shapex = new float[4];
        shapey = new float[4];
        setShape();
    }

    private void setShape(){

        shapex[0] = this.x;
        shapex[1] = this.x + this.width;
        shapex[2] = this.x + this.width;
        shapex[3] = this.x;

        shapey[0] = this.y;
        shapey[1] = this.y;
        shapey[2] = this.y + this.height;
        shapey[3] = this.y + this.height;
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
