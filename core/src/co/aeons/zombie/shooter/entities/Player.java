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
	private HashMap<String, Weapon> weapons;
	private Weapon currentWeapon;

	private Texture playerTexture;

	private boolean up;
	
	private boolean hit;
	private boolean dead;

	private long score;
	private int extraLives;
	private long requiredScore;

	
	public Player(ArrayList<Bullet> bullets) {
		
		this.bullets = bullets;
		this.weapons = new HashMap<String, Weapon>();

		x = 50;
		y = ZombieShooter.HEIGHT / 2;

		weapons.put("Pistol", new Pistol(getx(), gety()));
		weapons.put("Shotgun", new Shotgun(getx(), gety()));
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

	public void setCurrentWeapon(String key){
		this.currentWeapon = weapons.get(key);
	}

	public Weapon getCurrentWeapon() {
		return currentWeapon;
	}

	public void shoot() {
		for (Bullet bullet : weapons.get("Shotgun").shoot()) {
			bullet.setY(this.y);
			bullets.add(bullet);
		}
		Jukebox.play("shoot");
	}
	
	public void update(float dt) {
		// check extra lives
		if(score >= requiredScore) {
			extraLives++;
			requiredScore += 10000;
			Jukebox.play("extralife");
		}
		for(Weapon weapon : weapons.values()) {
			weapon.update(dt);
		}
	}

	public void draw(SpriteBatch batch) {
		batch.begin();
		batch.draw(playerTexture, x, y, 40, 50);
		batch.end();
	}
}
