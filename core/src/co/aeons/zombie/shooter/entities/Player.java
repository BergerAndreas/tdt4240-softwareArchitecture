package co.aeons.zombie.shooter.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.entities.bullets.Bullet;
import co.aeons.zombie.shooter.entities.weapons.BattleRifle;
import co.aeons.zombie.shooter.entities.weapons.Pistol;
import co.aeons.zombie.shooter.entities.weapons.Shotgun;
import co.aeons.zombie.shooter.entities.weapons.Weapon;
import co.aeons.zombie.shooter.managers.Jukebox;

public class Player extends SuperObject {
	
	private ArrayList<Bullet> bullets;
	protected ArrayList<Weapon> weapons;
	protected Weapon currentWeapon;
	protected int currentWeaponIndex;

	private Texture playerTexture;

	public Player(ArrayList<Bullet> bullets) {
		
		this.bullets = bullets;
		this.weapons = new ArrayList<Weapon>();

		//Add new weapons here
		this.weapons.add(new Pistol(x,y));
		this.weapons.add(new Shotgun(x,y));
		this.weapons.add(new BattleRifle(x, y));

		this.currentWeaponIndex = 0;
		this.currentWeapon = weapons.get(currentWeaponIndex);

		x = 40;
		y = ZombieShooter.HEIGHT / 2;


		this.playerTexture = new Texture("players/alfyboi.png");
		
	}

	public void setPosition(float x, float y) {
		super.setPosition(this.x, y);
	}

  public void nextWeapon() {
		currentWeaponIndex ++;
		this.currentWeapon = weapons.get(currentWeaponIndex % weapons.size());
	}

	public void prevWeapon() {
		currentWeaponIndex --;
		if(currentWeaponIndex < 0 || currentWeaponIndex >= weapons.size()) {
			currentWeaponIndex = weapons.size() - 1;
		}
		this.currentWeapon = weapons.get(currentWeaponIndex);
	}

	public Weapon getCurrentWeapon() {
		return currentWeapon;
	}

	public boolean shoot() {
		boolean canShoot = false;
		for (Bullet bullet : currentWeapon.shoot()) {
			bullet.setY(this.y);
			bullet.setX(this.x);
			bullets.add(bullet);
			canShoot = true;
		}
		return canShoot;
	}
	public int getWeaponId(){
		return currentWeaponIndex;
	}
	
	public void update(float dt) {
   
		for(Weapon weapon : weapons) {
			weapon.update(dt);
		}
	}

	public void draw(SpriteBatch batch) {
		batch.begin();
		batch.draw(playerTexture, x, y, 40, 50);
		batch.end();
	}
}
