package co.aeons.zombie.shooter.gameObjects.multiplayerMode;


import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.gameObjects.LandscapeShip;
import co.aeons.zombie.shooter.utils.ShootsManager;

public class MultiplayerShip extends LandscapeShip {

    public final int SPEED = 20;

    private GameObject shield;
    private boolean protectedShip;

    public MultiplayerShip(String textureName, int x, int y) {
        super(textureName,x,y,5);
        shield = new GameObject("shieldProtection",x-5,y);
        protectedShip = false;
    }

    public void render(){
        super.render();
        if(isProtected())
            shield.render();
    }

    public GameObject getShield(){
        return shield;
    }

    public boolean isProtected(){
        return protectedShip;
    }
    public void protectShip(){
        protectedShip = true;
    }
    public void unprotectShip(){
        protectedShip = false;
    }
    public void updateShield(){
        shield.setY(this.getY()-(this.getHeight()/2) + 5);
    }

    public void shoot() {
        ShootsManager.shootBurstBasicWeaponForShip(this);
    }
}
