package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.math.Rectangle;

public class SuperObject {

	protected float x;
	protected float y;

	protected float dx;
	protected float dy;

	protected float speed;

	protected Rectangle bounds;

	protected int width;
	protected int height;

	public float getx() { return x; }
	public float gety() { return y; }

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Rectangle getRectangle() {
		return bounds;
	}

	public boolean collide(SuperObject other) {
		return this.bounds.overlaps(other.getRectangle());
	}
}