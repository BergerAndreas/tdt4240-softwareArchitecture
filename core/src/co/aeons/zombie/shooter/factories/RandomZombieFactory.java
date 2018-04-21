package co.aeons.zombie.shooter.factories;

import java.util.ArrayList;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.entities.SinusZombie;
import co.aeons.zombie.shooter.entities.Trump;
import co.aeons.zombie.shooter.utils.utils;
import co.aeons.zombie.shooter.entities.Zombie;
import co.aeons.zombie.shooter.managers.Difficulty;

public class RandomZombieFactory {

    private ArrayList<String> zombieTypes;

    public RandomZombieFactory(){
        zombieTypes = new ArrayList<String>();
        zombieTypes.add("basic");
        zombieTypes.add("trump");
        zombieTypes.add("sinus");
    }

    public Zombie spawnRandomZombie() {
        float x = utils.randInt(ZombieShooter.WIDTH + 10, ZombieShooter.WIDTH + 50);
        float y = utils.randInt(0, ZombieShooter.HEIGHT - 100);

        // TODO: change to be less uniformly random
        String type = zombieTypes.get(utils.randInt(0, zombieTypes.size() - 1));

        if (type.equals("basic")) {
           return new Zombie(x, y, Difficulty.getDifficulty());
        }
        if(type.equals("trump")) {
            return new Trump(x, y, Difficulty.getDifficulty());
        }
        if(type.equals("sinus")) {
            return new SinusZombie(x, y, Difficulty.getDifficulty());
        }
        return null;
    }

}
