package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Bullet extends SpaceObject {
	
	private float lifeTime;
	private float lifeTimer;
	
	private boolean remove;

	private Sprite bullet;
	
	public Bullet(float x, float y, float radians) {
		
		this.x = x;
		this.y = y;
		this.radians = radians;
		
		float speed = 350;
		dx = MathUtils.cos(radians) * speed;
		dy = MathUtils.sin(radians) * speed;
		
		width = height = 2;
		
		lifeTimer = 0;
		lifeTime = 1;
		bullet = new Sprite(new Texture("pistol1.png"));
		
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update(float dt) {
		
		x += dx * dt;
		y += dy * dt;
		
		wrap();
		
		lifeTimer += dt;
		if(lifeTimer > lifeTime) {
			remove = true;
		}
		
	}
	
	public void draw(SpriteBatch sb) {
		sb.begin();
        sb.draw(bullet,x-width/2, y-height/2);
		sb.end();
	}
	
}


















