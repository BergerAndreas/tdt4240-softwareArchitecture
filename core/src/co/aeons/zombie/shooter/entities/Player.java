package co.aeons.zombie.shooter.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.entities.weapons.Pistol;
import co.aeons.zombie.shooter.entities.weapons.Shotgun;
import co.aeons.zombie.shooter.entities.weapons.Weapon;
import co.aeons.zombie.shooter.managers.Jukebox;

public class Player extends SuperObject {
	
	private ArrayList<Bullet> bullets;
	private ArrayList<Weapon> weapons;
	private Weapon currentWeapon;
	private int currentWeaponIndex;

	private Texture playerTexture;

	private boolean up;
	
	private boolean hit;
	private boolean dead;

	private long score;
	private int extraLives;
	private long requiredScore;

	int testThing = 0;
	
	public Player(ArrayList<Bullet> bullets) {
		
		this.bullets = bullets;
		this.weapons = new ArrayList<Weapon>();
		this.weapons.add(new Pistol(x, y));
		this.weapons.add(new Shotgun(x, y));

		this.currentWeaponIndex = 0;
		this.currentWeapon = weapons.get(currentWeaponIndex);

		x = 50;
		y = ZombieShooter.HEIGHT / 2;


		this.playerTexture = new Texture("nukjøyrarme.png");
		
		score = 0;
		extraLives = 1;
		requiredScore = 10000;

	}

	public void setPosition(float x, float y) {
		super.setPosition(this.x, y);
	}
	
	public boolean isHit() { return hit; }
	public boolean isDead() { return dead; }
	public void reset() {
		x = 50;
		y = ZombieShooter.HEIGHT / 2;
		hit = dead = false;
	}
	
	public long getScore() { return score; }
	public int getLives() { return extraLives; }
	
	public void loseLife() { extraLives--; }
	public void incrementScore(long l) { score += l; }

	public void nextWeapon() {
		currentWeaponIndex ++;
		this.currentWeapon = weapons.get(currentWeaponIndex % weapons.size());
	}

	public void prevWeapon() {
		currentWeaponIndex --;
		if(currentWeaponIndex < 0) {
			currentWeaponIndex = weapons.size() - 1;
		}
		this.currentWeapon = weapons.get(currentWeaponIndex);
	}

	public Weapon getCurrentWeapon() {
		return currentWeapon;
	}

	public void shoot() {
		for (Bullet bullet : currentWeapon.shoot()) {
			bullet.setY(this.y);
			bullets.add(bullet);
		}
		Jukebox.play("shoot");
	}
	
	public void update(float dt) {
		// check extra lives

		testThing ++;

		if(testThing % 50 == 0) {
			nextWeapon();
			System.out.println("New weapon");
		}

		if(score >= requiredScore) {
			extraLives++;
			requiredScore += 10000;
			Jukebox.play("extralife");
		}
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
