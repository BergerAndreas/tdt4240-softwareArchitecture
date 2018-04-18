package co.aeons.zombie.shooter.gameObjects.multiplayerMode;


import com.badlogic.gdx.graphics.Color;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.LandscapeShip;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.MultiplayerShip;

public abstract class PowerUp extends GameObject{

    private boolean touched;

    public PowerUp(String textureName, int x, int y) {
        super(textureName, x, y);
        touched = false;
    }

    public abstract void act(float delta, MultiplayerShip g);

    public boolean isTouched(){
        return touched;
    }

    public void setTouched(){
        touched = true;
    }

    public void render(){
        if(isTouched()){
            Color c = SpaceGame.batch.getColor();
            c.a = 0.2f;
            SpaceGame.batch.setColor(c);
            super.render();
            c.a = 1.0f;
            SpaceGame.batch.setColor(c);
        }else{
            super.render();
        }
    }
}
