package co.aeons.zombie.shooter.entities.bullets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.entities.SuperObject;

public class Bullet extends SuperObject {
	
	protected float lifeTime;
	protected float lifeTimer;

	protected boolean remove;

	protected Sprite bullet;

	protected Rectangle bulletBounds;

	protected int damage;
	protected float speed;

	public Bullet(float x, float y) {
		
		this.x = x;
		this.y = y;

		this.height = 10;
		this.width = 10;

		this.speed = 350;
		dx = speed;

		width = height = 2;

		this.damage = 15;

		lifeTimer = 0;
		lifeTime = 2;
		bullet = new Sprite(new Texture("weapons/pistol1.png"));
		bulletBounds = new Rectangle(this.x, this.y, width, height);

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

	public void setY(float y) {
	    this.y = y;
    }

    public void setX(float x) { this.x = x; }

	public void setDamage(int damage) {
		this.damage = damage;
	}

}


















