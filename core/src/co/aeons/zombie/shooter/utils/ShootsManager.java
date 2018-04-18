package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.LandscapeShip;
import co.aeons.zombie.shooter.gameObjects.campaignMode.CampaignShip;
import co.aeons.zombie.shooter.gameObjects.campaignMode.enemies.PartOfEnemy;
import co.aeons.zombie.shooter.gameObjects.Burst;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.BasicShootMultiplayer;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.RivalShip;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.PlayerShip;
import co.aeons.zombie.shooter.screens.CampaignScreen;
import co.aeons.zombie.shooter.screens.MultiplayerScreen;
import co.aeons.zombie.shooter.utils.enums.TypeShoot;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.campaignMode.enemies.RedEnemy;
import co.aeons.zombie.shooter.gameObjects.campaignMode.shoots.*;
import co.aeons.zombie.shooter.gameObjects.Shoot;

public class ShootsManager {

    //Almacenará todos los shoots en pantalla
    public static Array<Shoot> shoots;
    public static Array<Burst> bursts;


    public static void load() {
        shoots = new Array<Shoot>();
        bursts = new Array<Burst>();
    }

    /**
     * Prepara la realización de una ráfaga de tres disparos
     * @param shooter - El shooter que realizó el disparo
     */
    public static void shootBurstBasicWeaponForShip(GameObject shooter){
        if(ScreenManager.isCurrentScreenEqualsTo(CampaignScreen.class)){
            if(isCampaignShipReadyToShoot(TypeShoot.BASIC) && shooter instanceof CampaignShip){
                bursts.add(new Burst(shooter,3,0,TypeShoot.BASIC,null,2.0));
            }
        }else if(ScreenManager.isCurrentScreenEqualsTo(MultiplayerScreen.class)){
            if(isMultiplayerShipReadyToShoot() && shooter instanceof PlayerShip){
                bursts.add(new Burst(shooter,3,0,TypeShoot.BASIC,null,2.0));
            }else if(shooter instanceof RivalShip && isMultiplayerRivalShipReadyToShoot()){
                bursts.add(new Burst(shooter,3,0,TypeShoot.BASIC,null,2.0));
            }
        }
    }

    public static BasicShootMultiplayer shootOneBasicMultiplayerWeapon(GameObject shooter){
        BasicShootMultiplayer basic = new BasicShootMultiplayer(shooter, 0,0);

        int x = (int) (shooter.getX() - basic.getWidth());
        int y = (int) (shooter.getY() + shooter.getHeight()/2);

        if(shooter instanceof PlayerShip){
            x += shooter.getWidth() + basic.getWidth();
            y -= (shooter.getHeight()/2 - basic.getHeight()/2);
        }

        basic.setX(x);
        basic.setY(y);

        shoots.add(basic);

        return basic;
    }

    /**
     * Lanza un único disparo básico
     * @param shooter - El shooter que realizó el disparo
     */
    public static Basic shootOneBasicWeapon(GameObject shooter) {
        Basic basic = new Basic(shooter,0,0);

        int x = (int) (shooter.getX() - basic.getWidth());
        int y = (int) (shooter.getY() + shooter.getHeight()/2);

        if (shooter instanceof CampaignShip) {
            x += shooter.getWidth() + basic.getWidth();
            y -= (shooter.getHeight()/2 - basic.getHeight()/2);
        }

        addToShoots(basic,x,y);

        return basic;
    }

    private static boolean isMultiplayerRivalShipReadyToShoot(){
        boolean canShootAgain = true;
        for(Shoot shoot: shoots){
            if(shoot.getShooter() instanceof RivalShip && !shoot.isShocked()) {
                canShootAgain = false;
                break;
            }
        }
        return canShootAgain;
    }

    private static boolean isMultiplayerShipReadyToShoot(){
        boolean result = false;
        Array<Shoot> selected = new Array<Shoot>();
        //Obtenemos todos los disparos en pantalla que realizó la nave
        for(Shoot shoot: shoots){
            if(shoot.getShooter() instanceof PlayerShip && !shoot.isShocked())
                selected.add(shoot);
        }
        if(selected.size <= 0)
            result = true;

        if(MultiplayerScreen.playerBurstPowerUp.isShooting())
            result = false;

        return result;
    }

    /**
     * Indica si la nave puede disparar en el momento actual, según el arma
     * @param type - El tipo de disparo equipado en la nave
     * @return Indica si puede o no disparar la nave
     */
    private static boolean isCampaignShipReadyToShoot(TypeShoot type) {
        boolean result = false;
        Array<Shoot> selected = new Array<Shoot>();

        //Obtenemos todos los disparos en pantalla que realizó la nave
        for(Shoot shoot: shoots){
            if(shoot.getShooter() instanceof CampaignShip && !shoot.isShocked())
                selected.add(shoot);
        }

        //Según el tipo de disparo, la condición será distinta
        switch (type){
            case BASIC:
                if(selected.size <= 0)
                    result = true;
                break;
            case RED:
                if(selected.size <= 0)
                    result = true;
                break;
            case BLUE:
                if(selected.size <= 1)
                    result = true;
                break;
            case YELLOW:
                if(selected.size <= 0)
                    result = true;
                break;
            case PURPLE:
                if(selected.size <= 1)
                    result = true;
                break;
            case ORANGE:
                if(selected.size <= 0)
                    result = true;
                break;
            case GREEN:
                if(selected.size <= 1)
                    result = true;
                break;
            default:
                throw new IllegalArgumentException("Se ha seleccionado un tipo de arma inválido");
        }
        return result;
    }

    public static void render(){
        for(Shoot shoot : shoots)
            shoot.render();
    }

    public static void update(float delta, LandscapeShip landscapeShip){
        for(Shoot shoot: shoots){
            shoot.update(delta);

            //Si algún disparo sobresale los limites de la pantalla o está marcado como borrable, se eliminará
            if(shoot.getX() > SpaceGame.width || shoot.getX()+shoot.getWidth() < 0 ||
                    shoot.getY()+shoot.getHeight() < 0 || shoot.getY() > SpaceGame.height ||
                    shoot.isDeletable()){
                shoot.changeToDeletable();
                shoots.removeValue(shoot,false);
            }
        }

        for(Burst burst: bursts){
            if(!burst.isEndShooting()){
                burst.updateBurst(landscapeShip);
            }else
                bursts.removeValue(burst, false);
        }
    }



    public static void shootOneType5Weapon(GameObject shooter) {
        BigShoot bigShoot = new BigShoot(shooter,0,0,0f);

        int x = (int) (shooter.getX());
        int y = (int) (50);

        addToShoots(bigShoot,x,y);
    }

    public static void shootRedWeapon(GameObject shooter) {
        Red redShoot;

        if (shooter instanceof CampaignShip){
            if(isCampaignShipReadyToShoot(TypeShoot.RED)){
                redShoot = new Red(shooter,0,0);

                int x = (int) (shooter.getX() + shooter.getWidth());
                int y = (int) (shooter.getY() + shooter.getHeight()/2);

                addToShoots(redShoot,x,y);
            }
        }else if (shooter instanceof RedEnemy){
            redShoot = new Red(shooter,0,0);

            int x = (int) (shooter.getX() - redShoot.getWidth());
            int y = (int) (shooter.getY() + shooter.getHeight()/2);

            addToShoots(redShoot, x, y);
        }

    }

    public static void shootBlueWeapon(GameObject shooter, float yTarget) {
        Blue blueShoot;

        if (shooter instanceof CampaignShip) {
            if (isCampaignShipReadyToShoot(TypeShoot.BLUE)) {

                int x = (int) (shooter.getX() + shooter.getWidth());
                int y = (int) (shooter.getY() + shooter.getHeight() / 3);

                blueShoot = new Blue(shooter, x, y, yTarget);

                addToShoots(blueShoot, x, y);
        }
    } else {



        int x = (int) (shooter.getX());
        int y = (int) (shooter.getY() + shooter.getHeight() / 2);

            blueShoot = new Blue(shooter, x, y, CampaignScreen.ship.getY() + (CampaignScreen.ship.getHeight()/2));

            x = (int)blueShoot.getX() - (int)blueShoot.getWidth();

            addToShoots(blueShoot, x, y);
        }
    }

    public static void shootYellowWeapon(GameObject shooter, float xTarget, float yTarget) {
        Yellow yellowShoot;

        if (shooter instanceof CampaignShip) {
            if (isCampaignShipReadyToShoot(TypeShoot.YELLOW)) {
                yellowShoot = new Yellow(shooter, (int)xTarget, (int)yTarget);
                yellowShoot.playShootFX();
                shoots.add(yellowShoot);
            }
        } else {
            yellowShoot = new Yellow(shooter, (int)xTarget, (int)yTarget);
            yellowShoot.playShootFX();
            shoots.add(yellowShoot);
        }
    }

    public static void shootPurpleWeapon(GameObject shooter, float xTarget, float yTarget) {
        Purple purpleShoot;

        if (shooter instanceof CampaignShip) {
            if (isCampaignShipReadyToShoot(TypeShoot.PURPLE)) {
                int x = (int) (shooter.getX() + shooter.getWidth());
                int y = (int) (shooter.getY() + shooter.getHeight() / 2);

                purpleShoot = new Purple(shooter, x, y, xTarget, yTarget);

                addToShoots(purpleShoot,x,y);
            }
        }else if (shooter instanceof Enemy) {
            int x = (int) (shooter.getX());
            int y = (int) (shooter.getY() + shooter.getHeight() / 2);

            purpleShoot = new Purple(shooter,x,y, CampaignScreen.ship.getX() + (CampaignScreen.ship.getWidth()), CampaignScreen.ship.getY() + (CampaignScreen.ship.getHeight()/2));

            addToShoots(purpleShoot,x,y);
        }
    }


    public static void shootBurstOrangeWeapon(GameObject shooter, float x, float y) {
        Enemy enemy = EnemiesManager.getEnemyFromPosition(x,y);
        if(enemy != null && isCampaignShipReadyToShoot(TypeShoot.ORANGE) && enemy.canCollide()){
            if(enemy instanceof PartOfEnemy){
                PartOfEnemy partOfEnemy = (PartOfEnemy) enemy;
                if(partOfEnemy.damageable){
                    bursts.add(new Burst(shooter,12,0,TypeShoot.ORANGE, enemy, 1.0));
                    enemy.setTargettedByShip(true);
                }
            }else{
                bursts.add(new Burst(shooter,12,0,TypeShoot.ORANGE, enemy, 1.0));
                enemy.setTargettedByShip(true);
            }
        }else if(enemy == null && isCampaignShipReadyToShoot(TypeShoot.ORANGE)){
            // Posiblemente para que dispare aun si no ha tocado a un enemigo y dispare a la posición seleccionada
            GameObject target = new GameObject(null,(int)x,(int)y);
            bursts.add(new Burst(shooter,12,0,TypeShoot.ORANGE, target, 1.0));
        }
    }

    public static Orange shootOneOrangeWeapon(GameObject shooter,int xShoot, int yShoot, float angle,  GameObject target, int orangeShootsFired) {
        Orange result = null;

        if(shooter instanceof CampaignShip){
            angle =  (shoots.size/(float)(orangeShootsFired + shoots.size)) * angle;
            angle =  MathUtils.random(-angle,angle);
        }

        result = new Orange(shooter,xShoot,yShoot, angle, target);
        addToShoots(result,xShoot,yShoot);

        return result;
    }

    public static void shootGreenWeapon(GameObject shooter, float yTarget) {
        Green greenShoot;

        if (shooter instanceof CampaignShip) {
            if (isCampaignShipReadyToShoot(TypeShoot.GREEN)) {
                int x = (int) (shooter.getX() + shooter.getWidth());
                int y = (int) (shooter.getY() + shooter.getHeight() / 3);

                greenShoot = new Green(shooter, x, y, yTarget);

                addToShoots(greenShoot,x,y);
            }
        } else {
            int x = (int) (shooter.getX());
            int y = (int) (shooter.getY() + shooter.getHeight() / 2);

            greenShoot = new Green(shooter, x, y, CampaignScreen.ship.getY() + (CampaignScreen.ship.getHeight()/2));
            x = (int)greenShoot.getX() - (int)greenShoot.getWidth();

            addToShoots(greenShoot,x,y);
        }
    }

    public static void shootGreenFireWeapon(GameObject shooter, float xTarget, float yTarget) {
        GreenFire greenFireShoot = new GreenFire(shooter, xTarget, yTarget);
        addToShoots(greenFireShoot,(int)xTarget,(int)yTarget);
    }

    //Devuelve el arma verde en pantalla disparada por el shooter pasado por parámetro, si no existe devuelve null
    public static Green getGreenShootByShooterOnScreen(GameObject shooter) {
        Green green = null;
        for (Shoot shoot: shoots) {
            if (shoot instanceof Green && shoot.getShooter().equals(shooter) && !shoot.isShocked())
                green = (Green) shoot;
        }
        return green;
    }

    //Gestiona la reacción de la colisión del shoot pasado por parámetro con la nave
    public static void manageCollisionWithShip(Shoot shoot) {
        shoot.collideWithShip();
    }

    //Gestiona la reacción de la colisión del shoot y el enemigo pasados por parámetro
    public static void manageCollisionWithEnemy(Pair<Shoot,Enemy> shootToEnemy) {
        Shoot shoot = shootToEnemy.getFirst();
        Enemy enemy = shootToEnemy.getSecond();

        shoot.collideWithEnemy(enemy);
    }

    //Gestiona la reacción de la colisión de los dos shoots pasados por parámetro
    public static void manageCollisionWithShoot(Pair<Shoot, Shoot> shootToShoot) {
        Shoot shoot1 = shootToShoot.getFirst();
        Shoot shoot2 = shootToShoot.getSecond();

        shoot1.collideWithShoot(shoot2);
        shoot2.collideWithShoot(shoot1);
    }

    private static void addToShoots(Shoot shoot, int x, int y){
        shoot.setX(x);
        shoot.setY(y);

        shoot.playShootFX();

        shoots.add(shoot);
    }
}
