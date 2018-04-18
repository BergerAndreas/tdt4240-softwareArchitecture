package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet extends SuperObject {
	
	private float lifeTime;
	private float lifeTimer;

	private boolean remove;

	private Sprite bullet;

	private Rectangle bulletBounds;

	private int damage;

	public Bullet(float x, float y) {
		
		this.x = x;
		this.y = y;

		this.height = 10;
		this.width = 10;

		float speed = 350;
		dx = speed;

		width = height = 2;

		this.damage = 5;

		lifeTimer = 0;
		lifeTime = 2;
		bullet = new Sprite(new Texture("pistol1.png"));
		bulletBounds = new Rectangle(this.x, this.y, 10, 10);

	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update(float dt) {
		
		x += dx * dt;
		bulletBounds.setPosition(x,y);
		
		lifeTimer += dt;
		if(lifeTimer > lifeTime) {
			remove = true;
		}
		
	}
	
	public void draw(SpriteBatch sb) {
		sb.begin();
        //sb.draw(bullet,x-width/2, y-height/2);
		sb.draw(bullet, this.x, this.y, 10, 10);
		sb.end();
	}

	public Rectangle getRectangle() {
		return bulletBounds;
	}

	public int getDamage() {
		return damage;
	}
}


















