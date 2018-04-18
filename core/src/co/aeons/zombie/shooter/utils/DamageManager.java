package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.utils.ArrayMap;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.gameObjects.campaignMode.enemies.*;
import co.aeons.zombie.shooter.gameObjects.campaignMode.shoots.*;

public class DamageManager {

    public static ArrayMap<Pair<Class,Class>,Integer> damageByTypeOfShootAndEnemy;

    public static void load(){
        damageByTypeOfShootAndEnemy = new ArrayMap<Pair<Class, Class>, Integer>();
        // Arma básica con los enemigos a los que daña
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Basic.class,Type1.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Basic.class,Type2.class),4);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Basic.class,Type3.class),6);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Basic.class,Type4.class),12);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Basic.class,Type5.class),19);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Basic.class,YellowEnemy.class),4);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Basic.class,RedEnemy.class),2);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Basic.class,BlueEnemy.class),3);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Basic.class,OrangeEnemy.class),20);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Basic.class,PurpleEnemy.class),3);

        // Arma amarilla con los enemigos a los que daña
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Yellow.class,Type1.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Yellow.class,Type2.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Yellow.class,YellowEnemy.class),3);

        // Arma verde con los enemigos a los que daña
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Green.class,YellowEnemy.class),3);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Green.class,Type1.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Green.class,Type2.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Green.class,Type3.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Green.class,Type4.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Green.class,Type5.class),2);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Green.class,GreenEnemy.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Green.class,BlueEnemy.class),1);

        // Arma verde (su fuego) con los enemigos a los que daña
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(GreenFire.class,YellowEnemy.class),3);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(GreenFire.class,Type1.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(GreenFire.class,Type2.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(GreenFire.class,Type3.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(GreenFire.class,Type4.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(GreenFire.class,Type5.class),2);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(GreenFire.class,GreenEnemy.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(GreenFire.class,BlueEnemy.class),1);

        // Arma roja con los enemigos a los que daña
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Red.class,RedEnemy.class),5);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Red.class,Type1.class),5);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Red.class,Type2.class),8);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Red.class,Type3.class),12);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Red.class,Type4.class),18);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Red.class,Type5.class),35);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Red.class,PurpleEnemy.class),10);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Red.class,OrangeEnemy.class),15);

        // Arma azul con los enemigos a los que daña
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Blue.class,BlueEnemy.class),30);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Blue.class,Type1.class),5);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Blue.class,Type2.class),10);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Blue.class,Type3.class),10);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Blue.class,Type4.class),20);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Blue.class,Type5.class),25);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Blue.class,PurpleEnemy.class),5);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Blue.class,GreenEnemy.class),30);

        // Arma morada con los enemigos a los que daña
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Purple.class,PurpleEnemy.class),20);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Purple.class,Type1.class),5);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Purple.class,Type2.class),10);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Purple.class,Type3.class),11);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Purple.class,Type4.class),20);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Purple.class,Type5.class),30);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Purple.class,RedEnemy.class),2);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Purple.class,BlueEnemy.class),15);

        // Arma naranja con los enemigos a los que daña
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Orange.class,OrangeEnemy.class),10);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Orange.class,Type1.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Orange.class,Type2.class),3);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Orange.class,Type3.class),6);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Orange.class,Type4.class),14);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Orange.class,Type5.class),10);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Orange.class,YellowEnemy.class),1);
        damageByTypeOfShootAndEnemy.put(new Pair<Class, Class>(Orange.class,RedEnemy.class),1);
    }

    // Calcula el daño y realiza la accion del damage en caso de que sea necesario
    public static void calculateDamage(Shoot shoot, Enemy enemy){
        Integer damage = damageByTypeOfShootAndEnemy.get(new Pair<Class, Class>(shoot.getClass(),enemy.getClass()));

        // Si hemos intentado calcular el daño de un shoot-enemy que no existe, no realizamos ese daño
        if(damage != null)
            enemy.damage(damage);
    }
}
