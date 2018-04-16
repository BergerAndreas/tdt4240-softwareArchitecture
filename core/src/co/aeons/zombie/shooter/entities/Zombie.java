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

	// Anitmations
	private Animation<TextureRegion> runningAnimation;
	private Animation<TextureRegion> attackAnimation;
	private TextureAtlas runningAtlas;
	private TextureAtlas attackAtlas;

	// Tracks elapsed time for animations
	private float stateTimeRunning;
	private float stateTimeAttacking;
	
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
		createAttackAnimation();
	}

	private void createIdleAnimation() {
		//Opens textureAtlas containing enemy spritesheet information
		runningAtlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));
		//Fetches all sprites matchin keyword 'spoder'
		runningAnimation =
				new Animation<TextureRegion>(0.1f, runningAtlas.findRegions("spoder"), Animation.PlayMode.LOOP);
		//Initializes statetime for this animation
		stateTimeRunning = 0f;
	}

	private void createAttackAnimation() {
		attackAtlas = new TextureAtlas(Gdx.files.internal("spooder.atlas"));
		attackAnimation = new Animation<TextureRegion>(
				0.1f,
				attackAtlas.findRegions("spooder"),
				Animation.PlayMode.LOOP
		);
		stateTimeAttacking = 0f;

	}

	public int getType() { return type; }
	public boolean shouldRemove() { return remove; }
	public int getScore() { return score; }
	
	public void update(float dt) {
		
		if(!isStopped){
			x += dx * dt;
		}

		y += dy * dt;

		stateTimeRunning += dt;

		bounds.setPosition(x, y);
	}
  
  public void draw(SpriteBatch batch) {
        batch.begin();
        if (!isStopped) {
            TextureRegion currentRunningFrame = runningAnimation.getKeyFrame(stateTimeRunning, true);
            batch.draw(currentRunningFrame, x, y, width, height);
        } else {
            TextureRegion currentAttackFrame = attackAnimation.getKeyFrame(stateTimeAttacking, true);
            batch.draw(currentAttackFrame, x, y, width, height);
        }

        batch.end();
    }
    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public int attack() {
        if (Math.floor(stateTimeAttacking) % 2 == 0) {
            return 1;
        } else return 0;
    }
}
