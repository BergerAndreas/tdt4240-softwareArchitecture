package co.aeons.zombie.shooter.gameObjects.campaignMode;

import co.aeons.zombie.shooter.gameObjects.LandscapeShip;
import co.aeons.zombie.shooter.gameObjects.campaignMode.shoots.Green;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.utils.enums.ColorShip;
import co.aeons.zombie.shooter.utils.ShootsManager;

public class CampaignShip extends LandscapeShip {

    //Indica la velocidad para el movimiento de la nave
    public static final float SPEED = 50;

    //Indica la posición que tendrá la nave con respecto al eje X
    private static final float X_POSITION = 80;

    //Indica el color de la nave
    private ColorShip color;

    public CampaignShip() {
        super("ship", 0, 0, 5);

        this.setX(X_POSITION);
        this.setY(SpaceGame.height / 2 - getHeight() / 2);

        //Inicializamos el color de la nava a incoloro
        color = ColorShip.COLORLESS;
    }


    @Override
    public void render(){
        super.render();
    }

    public void update(float delta, float y, boolean canShipMove) {
        super.update(delta);

        if(!this.isDefeated()){
            //Movimiento de la nave
            if (canShipMove) {
                if (y < (this.getY() + this.getHeight() / 2))
                    this.setY(this.getY() - (Math.abs(y - (this.getY() + this.getHeight() / 2)) * SPEED * delta));
                if (y > (this.getY() + this.getHeight() / 2))
                    this.setY(this.getY() + (Math.abs(y - (this.getY() + this.getHeight() / 2)) * SPEED * delta));
            }
            //Controlamos si la nave se sale de la pantalla
            if (this.getY() < 0)
                this.setY(0);
            if (this.getY() > SpaceGame.height - getHeight())
                this.setY(SpaceGame.height - getHeight());
        }

    }

    public void setX(float x, float delta){
        super.setX(x);
        super.updateParticleEffect();
        fireEffect.update(delta);
    }

    public void setToInitialX(float delta) {
        this.setX(X_POSITION, delta);
    }

    public void changeColor(ColorShip color){
        this.color = color;
    }

    //Realiza un disparo, en función del arma equipada
    public void shoot(float x, float y) {
        switch (color) {
            case COLORLESS:
                ShootsManager.shootBurstBasicWeaponForShip(this);
                break;
            case RED:
                ShootsManager.shootRedWeapon(this);
                break;
            case BLUE:
                ShootsManager.shootBlueWeapon(this, y);
                break;
            case YELLOW:
                ShootsManager.shootYellowWeapon(this,x,y);
                break;
            case GREEN:
                Green green = ShootsManager.getGreenShootByShooterOnScreen(this);
                if (green != null)
                    ShootsManager.shootGreenFireWeapon(green, x, y);
                else
                    ShootsManager.shootGreenWeapon(this, y);
                break;
            case ORANGE:
                ShootsManager.shootBurstOrangeWeapon(this, x, y);
                break;
            case PURPLE:
                ShootsManager.shootPurpleWeapon(this, x, y);
                break;
            default:
                ShootsManager.shootBurstBasicWeaponForShip(this);
                break;
        }
    }

}