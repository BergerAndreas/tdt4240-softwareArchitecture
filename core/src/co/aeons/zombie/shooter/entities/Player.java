package co.aeons.zombie.shooter.entities;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
;
import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.Jukebox;


public class Player extends SpaceObject {
	
	private final int MAX_BULLETS = 4;
	private ArrayList<Bullet> bullets;

	private boolean up;
	
	private boolean hit;
	private boolean dead;
	
	private float hitTimer;
	private float hitTime;
	private Line2D.Float[] hitLines;
	private Point2D.Float[] hitLinesVector;
	
	private long score;
	private int extraLives;
	private long requiredScore;
	
	public Player(ArrayList<Bullet> bullets) {
		
		this.bullets = bullets;
		
		x = 50;
		y = ZombieShooter.HEIGHT / 2;
		
		shapex = new float[4];
		shapey = new float[4];

		radians = 0;
		
		hit = false;
		hitTimer = 0;
		hitTime = 2;
		
		score = 0;
		extraLives = 3;
		requiredScore = 10000;
		
	}
	
	private void setShape() {
		shapex[0] = x + MathUtils.cos(radians) * 8;
		shapey[0] = y + MathUtils.sin(radians) * 8;
		
		shapex[1] = x + MathUtils.cos(radians - 4 * 3.1415f / 5) * 8;
		shapey[1] = y + MathUtils.sin(radians - 4 * 3.1415f / 5) * 8;
		
		shapex[2] = x + MathUtils.cos(radians + 3.1415f) * 5;
		shapey[2] = y + MathUtils.sin(radians + 3.1415f) * 5;
		
		shapex[3] = x + MathUtils.cos(radians + 4 * 3.1415f / 5) * 8;
		shapey[3] = y + MathUtils.sin(radians + 4 * 3.1415f / 5) * 8;
	}

	public void setPosition(float x, float y) {
		super.setPosition(this.x, y);
		setShape();
	}
	
	public boolean isHit() { return hit; }
	public boolean isDead() { return dead; }
	public void reset() {
		x = 50;
		y = ZombieShooter.HEIGHT / 2;
		setShape();
		hit = dead = false;
	}
	
	public long getScore() { return score; }
	public int getLives() { return extraLives; }
	
	public void loseLife() { extraLives--; }
	public void incrementScore(long l) { score += l; }
	
	public void shoot() {
		if(bullets.size() == MAX_BULLETS) return;
		bullets.add(new Bullet(x, y));
		Jukebox.play("shoot");
	}
	
	public void hit() {
		
		if(hit) return;
		
		hit = true;
		Jukebox.stop("thruster");
		
		hitLines = new Line2D.Float[4];
		for(int i = 0, j = hitLines.length - 1;
			i < hitLines.length;
			j = i++) {
			hitLines[i] = new Line2D.Float(
						shapex[i], shapey[i], shapex[j], shapey[j]
						);
		}
		
		hitLinesVector = new Point2D.Float[4];
		hitLinesVector[0] = new Point2D.Float(
			MathUtils.cos(radians + 1.5f),
			MathUtils.sin(radians + 1.5f)
		);
		hitLinesVector[1] = new Point2D.Float(
			MathUtils.cos(radians - 1.5f),
			MathUtils.sin(radians - 1.5f)
		);
		hitLinesVector[2] = new Point2D.Float(
			MathUtils.cos(radians - 2.8f),
			MathUtils.sin(radians - 2.8f)
		);
		hitLinesVector[3] = new Point2D.Float(
			MathUtils.cos(radians + 2.8f),
			MathUtils.sin(radians + 2.8f)
		);
		
	}
	
	public void update(float dt) {
		
		// check if hit
		if(hit) {
			hitTimer += dt;
			if(hitTimer > hitTime) {
				dead = true;
				hitTimer = 0;
			}
			for(int i = 0; i < hitLines.length; i++) {
				hitLines[i].setLine(
					hitLines[i].x1 + hitLinesVector[i].x * 10 * dt,
					hitLines[i].y1 + hitLinesVector[i].y * 10 * dt,
					hitLines[i].x2 + hitLinesVector[i].x * 10 * dt,
					hitLines[i].y2 + hitLinesVector[i].y * 10 * dt
				);
			}
			return;
		}
		
		// check extra lives
		if(score >= requiredScore) {
			extraLives++;
			requiredScore += 10000;
			Jukebox.play("extralife");
		}

		
		// set shape
		setShape();

		
		// screen wrap
		wrap();
		
	}
	
	public void draw(ShapeRenderer sr) {
		
		sr.setColor(1, 1, 1, 1);
		
		sr.begin(ShapeType.Line);
		
		// check if hit
		if(hit) {
			for(int i = 0; i < hitLines.length; i++) {
				sr.line(
					hitLines[i].x1,
					hitLines[i].y1,
					hitLines[i].x2,
					hitLines[i].y2
				);
			}
			sr.end();
			return;
		}
		
		// draw ship
		for(int i = 0, j = shapex.length - 1;
			i < shapex.length;
			j = i++) {
			
			sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
			
		}
		
		
		sr.end();
		
	}
	
}


















