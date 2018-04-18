package co.aeons.zombie.shooter.gameObjects.multiplayerMode.powerUps;


import co.aeons.zombie.shooter.gameObjects.multiplayerMode.MultiplayerShip;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.PowerUp;

public class RegLifePowerUp extends PowerUp {

    public boolean actted;

    public RegLifePowerUp(String textureName, int x, int y) {
        super(textureName, x, y);
        actted = false;
    }

    @Override
    public void act(float delta, MultiplayerShip ship) {
        if(!actted) {
            int damRec = ship.getDamageReceived();
            damRec -= damRec / 2;
            if (damRec < 0)
                damRec = 0;

            ship.setDamageReceived(damRec);
            actted = true;
        }
    }
}
