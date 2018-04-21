package co.aeons.zombie.shooter.entities;

import java.util.ArrayList;

import co.aeons.zombie.shooter.entities.bullets.Bullet;

public class SecondPlayer extends Player {

    public SecondPlayer(ArrayList<Bullet> bullets) {
        super(bullets);
    }

    public void setWeaponId(int weaponId){
        this.currentWeaponIndex = weaponId;
        this.currentWeapon = weapons.get(currentWeaponIndex % weapons.size());
    }
}
