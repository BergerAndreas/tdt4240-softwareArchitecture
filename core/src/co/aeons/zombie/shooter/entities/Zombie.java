package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import org.w3c.dom.css.Rect;

public class Zombie extends SpaceObject {
	
	private int type;
	public static final int SMALL = 0;
	public static final int MEDIUM = 1;
	public static final int LARGE = 2;
	private boolean isStopped = false;

	private Animation<TextureRegion> runningAnimation;
	TextureAtlas atlas;
	private Texture stopTexture;
	// A variable for tracking elapsed time for the animation
	float stateTime;

	private int numPoints;
	private float[] dists;
	
	private int score;

	private boolean remove;

	public Zombie(float x, float y, int type) {
		
		this.x = x;
		this.y = y;
		this.type = type;
		

		width = height = 40;
		speed = MathUtils.random(20, 30);
		score = 20;


		bounds = new Rectangle(0, 0, 40, 50);

		dx = -50;
		dy = 0;
		createIdleAnimation();
		stopTexture = new Texture("spoder2.png");

	}

	private void createIdleAnimation() {
		//Opens textureAtlas containing enemy spritesheet information
		atlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));
		//Fetches all sprites matchin keyword 'spoder'
		runningAnimation =
				new Animation<TextureRegion>(0.1f, atlas.findRegions("spoder"), Animation.PlayMode.LOOP);
		//Initializes statetime for this animation
		stateTime = 0f;
	}
	
	public int getType() { return type; }
	public boolean shouldRemove() { return remove; }
	public int getScore() { return score; }
	
	public void update(float dt) {
		
		if(!isStopped){
			x += dx * dt;
		}

		y += dy * dt;

		radians += rotationSpeed * dt;
		stateTime += Gdx.graphics.getDeltaTime();

		bounds.setPosition(x, y);
	}

	public void draw(SpriteBatch batch) {
		batch.begin();
		if (!isStopped) {
			TextureRegion currentRunningFrame = runningAnimation.getKeyFrame(stateTime, true);
			batch.draw(currentRunningFrame, x, y, width, height);
		} else {
			batch.draw(stopTexture, x, y, width, height);
		}
		batch.end();
	}

	public void setStopped(boolean stopped){
		isStopped = stopped;
	}

	public int attack(){
		if(Math.floor(stateTime) % 2 == 0){
			System.out.println("Zombie attack");
			return 1;
		}
		else return 0;
	}
}


















