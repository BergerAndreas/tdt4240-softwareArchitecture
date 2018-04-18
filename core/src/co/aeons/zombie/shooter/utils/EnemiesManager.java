package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.utils.Array;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;

public class EnemiesManager {

    public static Array<Enemy> enemies;
    private static LevelGenerator level;

    public static void load(String scriptLevel){
        enemies = new Array<Enemy>();

        level = LevelGenerator.loadLevel(scriptLevel);
    }

    public static void update(float delta){
        //Actualizamos los tiempos de espera de aparición de los enemigos
        enemies = level.update(enemies,delta);

        for(Enemy enemy: enemies){

            enemy.update(delta);

            //Se eliminan los enemigos que se salgan de la parte izquierda de la pantalla o estén marcados como borrables
            if(enemy.getX() < 0 || enemy.isDeletable()){
                enemies.removeValue(enemy,false);
            }
        }
    }

    public static void render() {
        for(Enemy enemy: enemies)
            //El enemigo se pintará si no está marcado para borrar y no es parte de un enemigo mayor
            if(!enemy.isDeletable())
                enemy.render();
    }

    //Gestiona la reacción de la colisión del enemigo pasado por parámetro con la nave
    public static void manageCollisionWithShip(Enemy enemy) {
        enemy.collideWithShip();
    }

    //Gestiona la reacción de la colisión del enemigo con el arma
    public static void manageCollisionWithShoot(Pair<Shoot, Enemy> shootToEnemy) {
        Shoot shoot = shootToEnemy.getFirst();
        Enemy enemy = shootToEnemy.getSecond();
        enemy.collideWithShoot(shoot);
    }

    public static Enemy getEnemyFromPosition(float x, float y) {
        Enemy result = null;
        for(Enemy enemy: enemies){
            if(enemy.isOverlapingWith(x,y)){
                result = enemy;
                break;
            }
        }
        return result;
    }

    public static boolean noMoreEnemiesToGenerateOrToDefeat(){
        return enemies.size == 0 && level.enemiesToGenerate.size == 0;
    }

    public static void dispose(){
        for(Enemy enemy: EnemiesManager.enemies)
            enemy.dispose();
        enemies = null;
    }
}
