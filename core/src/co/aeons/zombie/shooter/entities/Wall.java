package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.Jukebox;
import co.aeons.zombie.shooter.managers.ResourceManager;

public class Wall extends SuperObject {

    private Texture wallTexture;

    private float maxWallHealth;
    private float currentWallHealth;
    private Texture bar;

    public Wall() {

        //TODO: MOVE TO GAME CONSTANTS
        this.x = 75;
        this.y = 0;
        this.width = 50;
        this.height = ZombieShooter.HEIGHT;

        this.bounds = new Rectangle(x,y,width,height);

        this.wallTexture = ResourceManager.getWallTexture();
        this.bar = ResourceManager.getHealthBar();
        this.maxWallHealth = 500;
        this.currentWallHealth = maxWallHealth;
    }

    public void draw(SpriteBatch batch) {
        batch.begin();
        batch.draw(wallTexture, this.x, this.y, this.width, this.height); // Wall texture
        batch.draw(bar, this.x, ZombieShooter.HEIGHT/2, this.width*getCurrentWallHealth()/getMaxWallHealth(), 20); // Health bar texture
        batch.end();
    }

    public void takeDamage(int damage){
        this.currentWallHealth -= damage;
    }

    public float getCurrentWallHealth() {
        return currentWallHealth;
    }

    public void setCurrentWallHealth(float currentWallHealth) {
        this.currentWallHealth = currentWallHealth;
    }

    public void increaseWallHealth(int health) {
        if( (getCurrentWallHealth() + health > getMaxWallHealth()) ){
            setCurrentWallHealth(getMaxWallHealth());
        }else{
            takeDamage(-health);
        }
    }

    public Texture getBar() {
        return bar;
    }

    public float getMaxWallHealth() {
        return maxWallHealth;
    }

    public int getWidth(){
        return width;
    }

    public void playSound(){
        Jukebox.play("wilhelm");
    }

}
