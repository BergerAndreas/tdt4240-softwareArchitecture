package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.utils.Array;
import co.aeons.zombie.shooter.gameObjects.LandscapeShip;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.RivalShip;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.PlayerShip;
import co.aeons.zombie.shooter.screens.CampaignScreen;
import co.aeons.zombie.shooter.screens.MultiplayerScreen;

public class CollisionsManager {

    // Contendrán las colisiones realizadas
    public static Array<Pair<Shoot, Enemy>> shootsToEnemies;
    private static Array<Pair<Shoot, Shoot>> shootsToShoots;

    public static void load() {
        if(ScreenManager.isCurrentScreenEqualsTo(CampaignScreen.class)){
            shootsToEnemies = new Array<Pair<Shoot, Enemy>>();
        }
        shootsToShoots = new Array<Pair<Shoot, Shoot>>();
    }

    public static void update(){
        if(ScreenManager.isCurrentScreenEqualsTo(CampaignScreen.class)){
            updateCampaign();
        }else if(ScreenManager.isCurrentScreenEqualsTo(MultiplayerScreen.class)){
            updateMultiplayer();
        }
    }

    private static void updateCampaign() {
        //Arrays que harán de auxiliares para no sobreescribir las originales
        Array<Enemy> enemies = new Array<Enemy>(EnemiesManager.enemies);
        Array<Shoot> shoots = new Array<Shoot>(ShootsManager.shoots);
        Array<Shoot> shootsSource = new Array<Shoot>(ShootsManager.shoots);

        //Almacenan el enemigo o bala que han chocado con la nave
        Enemy enemyOverlapsShip = null;
        Shoot shootOverlapsShip = null;

        //Indicará que un shoot ya ha dado con un enemigo, y servirá para que no se compruebe posteriormente con otro shoot
        boolean shootIsOverlapped;

        //Primero comprobamos si algún enemigo ha dado a la nave
        for (Enemy enemy: enemies) {
            if (enemy.isOverlapingWith(CampaignScreen.ship) && !CampaignScreen.ship.isUndamagable()) {
                //Si un enemigo ha dado a la nave la almacenamos en la variable
                enemyOverlapsShip = enemy;

                //Borramos el enemigo de la lista y salimos
                enemies.removeValue(enemy, false);
                break;
            }
        }

        //Ahora comprobamos las colisiones de los shoots
        for (Shoot shootDst: shoots) {
            shootIsOverlapped = false;

            //En primer lugar comprobamos si el shoot dió a la nave, siempre y cuando no hubiese sido golpeada antes
            if (enemyOverlapsShip == null && shootOverlapsShip == null && shootDst.getShooter() != CampaignScreen.ship &&
                    shootDst.isOverlapingWith(CampaignScreen.ship) && !shootDst.isShocked() && !CampaignScreen.ship.isUndamagable()) {

                //Almacenamos el shoot y lo eliminamos de la lista a comprobar
                shootOverlapsShip = shootDst;
                shoots.removeValue(shootDst, false);
            } else {

                //Si la bala no dio a la nave, comprobamos si dio a algún enemigo
                for (Enemy enemy : enemies) {
                    if (shootDst.isOverlapingWith(enemy) && !shootDst.isShocked() && !enemy.isDefeated() &&
                            enemy.canCollide() && shootDst.getShooter() != enemy) {

                        //Añadimos el par colisionado a la lista
                        shootsToEnemies.add(new Pair<Shoot, Enemy>(shootDst, enemy));

                        //Borramos los elementos de la colisión para que no se comprueben más
                        shoots.removeValue(shootDst, false);
                        enemies.removeValue(enemy, false);

                        //Si la bala ya ha chocado, salimos del for y marcamos como colisionado el shoot
                        shootIsOverlapped = true;
                        break;
                    }
                }

                //Por último, si la bala no ha chocado con un enemigo, comprobamos si ha chocado con otra bala
                if (!shootIsOverlapped) {

                    for (Shoot shootSrc : shootsSource) {

                        if (!shootDst.equals(shootSrc) && shootDst.isOverlapingWith(shootSrc)
                                && !shootDst.isShocked() && !shootSrc.isShocked()
                                && !shootDst.getShooter().equals(shootSrc) && !shootSrc.getShooter().equals(shootDst)
                                && !shootDst.getShooter().equals(shootSrc.getShooter()) && !shootSrc.getShooter().equals(shootDst.getShooter())) {
                            //Añadimos el par colisionado a la lista
                            shootsToShoots.add(new Pair<Shoot, Shoot>(shootDst, shootSrc));
                            //Borramos los elementos de la colisión para que no se comprueben más
                            shoots.removeValue(shootDst, false);
                            shootsSource.removeValue(shootSrc, false);

                            //Si la bala ya ha chocado, salimos del for
                            break;
                        }
                    }
                }
            }
        }

        //Ahora delegamos el tratamiento de las colisiones a otros métodos
        if (enemyOverlapsShip != null) {
            manageEnemyToShip(enemyOverlapsShip, CampaignScreen.ship);
        } else if (shootOverlapsShip != null) {
            manageShootToShip(shootOverlapsShip, CampaignScreen.ship);
        }
        manageShootsToEnemies();
        manageShootsToShoots();
    }

    private static void updateMultiplayer(){
        Array<Shoot> shoots = new Array<Shoot>(ShootsManager.shoots);
        Array<Shoot> shootsSource = new Array<Shoot>(ShootsManager.shoots);

        Pair<PlayerShip,Shoot> playerShootCollision = null;

        // Comprobamos las colisiones entre los shoots y el jugador y el enemigo
        for(Shoot shoot: shoots){
            // Intento de colision con el enemigo
            if (shoot.getShooter() instanceof PlayerShip &&
                    shoot.isOverlapingWith(MultiplayerScreen.rivalShip) &&
                    !shoot.isShocked() &&
                    !MultiplayerScreen.rivalShip.isUndamagable() &&
                    !MultiplayerScreen.rivalShip.isProtected()) {
                // Colisión con el enemigo
                shoots.removeValue(shoot, false);
            }
            // Intento de colision con el player
            else if(shoot.getShooter() instanceof RivalShip &&
                    shoot.isOverlapingWith(MultiplayerScreen.playerShip) &&
                    !shoot.isShocked() &&
                    !MultiplayerScreen.playerShip.isUndamagable() &&
                    !MultiplayerScreen.playerShip.isProtected()){
                // Colisión con el player
                playerShootCollision = new Pair<PlayerShip, Shoot>(MultiplayerScreen.playerShip,shoot);
            }else{
                for (Shoot shootSrc : shootsSource) {
                    if (!shoot.equals(shootSrc) && shoot.isOverlapingWith(shootSrc)
                            && !shoot.isShocked() && !shootSrc.isShocked()
                            && !shoot.getShooter().equals(shootSrc) && !shootSrc.getShooter().equals(shoot)
                            && !shoot.getShooter().equals(shootSrc.getShooter()) && !shootSrc.getShooter().equals(shoot.getShooter())) {
                        //Añadimos el par colisionado a la lista
                        shootsToShoots.add(new Pair<Shoot, Shoot>(shoot, shootSrc));
                        //Borramos los elementos de la colisión para que no se comprueben más
                        shoots.removeValue(shoot, false);
                        shootsSource.removeValue(shootSrc, false);

                        //Si la bala ya ha chocado, salimos del for
                        break;
                    }
                }
            }
        }

        if(playerShootCollision != null)
            managesPlayerShootCollision(playerShootCollision);

        manageShootsToShoots();
    }

    private static void managesPlayerShootCollision(Pair<PlayerShip,Shoot> playerShootCollision){
        playerShootCollision.getFirst().receiveDamage();
        ShootsManager.shoots.removeValue(playerShootCollision.getSecond(),false);
    }

    //Gestiona una colisión de enemigo a la nave
    private static void manageEnemyToShip(Enemy enemy, LandscapeShip landscapeShip) {
        landscapeShip.receiveDamage();
        EnemiesManager.manageCollisionWithShip(enemy);
    }

    //Gestiona una colisión de shoot a la nave
    private static void manageShootToShip(Shoot shoot, LandscapeShip landscapeShip) {
        landscapeShip.receiveDamage();
        ShootsManager.manageCollisionWithShip(shoot);
    }

    //Gestionará las colisiones entre disparos y enemigos
    private static void manageShootsToEnemies() {
        for (Pair<Shoot, Enemy> shootToEnemy: shootsToEnemies) {
            EnemiesManager.manageCollisionWithShoot(shootToEnemy);
            ShootsManager.manageCollisionWithEnemy(shootToEnemy);
        }
        shootsToEnemies = new Array<Pair<Shoot, Enemy>>();
    }

    //Gestionará las colisiones entre disparos y disparos
    private static void manageShootsToShoots() {
        for (Pair<Shoot, Shoot> shootToShoot: shootsToShoots) {
            ShootsManager.manageCollisionWithShoot(shootToShoot);
        }
        shootsToShoots.clear();
        shootsToShoots = new Array<Pair<Shoot, Shoot>>();
    }
}
