package co.aeons.zombie.shooter.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.Jukebox;

public class Player extends SuperObject {
	
	private final int MAX_BULLETS = 4;
	private ArrayList<Bullet> bullets;

	private Texture playerTexture;


	private long score;

	public Player(ArrayList<Bullet> bullets) {
		
		this.bullets = bullets;
		
		x = 50;
		y = ZombieShooter.HEIGHT / 2;

		this.playerTexture = new Texture("nukjøyrarme.png");
		
		score = 0;

	}

	public void setPosition(float x, float y) {
		super.setPosition(this.x, y);
	}

	
	public long getScore() { return score; }

	public void incrementScore(long l) { score += l; }
	
	public void shoot() {
		if(bullets.size() == MAX_BULLETS) return;
		bullets.add(new Bullet(x, y));
		Jukebox.play("shoot");
	}
	
	public void update(float dt) {

		// check extra lives
		if(score >= requiredScore) {
			extraLives++;
			requiredScore += 10000;
			Jukebox.play("extralife");
		}
	}

	public void draw(SpriteBatch batch) {
		batch.begin();
		batch.draw(playerTexture, x, y, 40, 50);
		batch.end();
	}
	
}

