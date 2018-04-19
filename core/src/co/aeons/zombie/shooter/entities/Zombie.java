package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Zombie extends SuperObject {
	private boolean isStopped = false;

	// Anitmations
	protected Animation<TextureRegion> runningAnimation;
	protected Animation<TextureRegion> attackAnimation;
	protected TextureAtlas runningAtlas;
	protected TextureAtlas attackAtlas;

	// Tracks elapsed time for animations
	protected float stateTimeRunning;
	protected float stateTimeAttacking;
	protected int score;

	protected float dx;

	protected float health;

    private float attackTimer;
    private float attackCooldown;
    private int attackCounter;

    protected int damage;

    private boolean remove;

	public Zombie(float x, float y, int difficulty) {

		this.x = x;
		this.y = y;

		this.damage = 10*difficulty;

		width = height = 40;
		speed = MathUtils.random(20, 30);
		score = 20;

		bounds = new Rectangle(0, 0, 40, 50);
		dx = -50*(4+difficulty-1)/4;



		this.health = 10*difficulty;

		createIdleAnimation();
		createAttackAnimation();

        attackTimer = 1.0f;
        attackCooldown = 2.0f;
        attackCounter = 0;
    }


	private void createIdleAnimation() {
		//Opens textureAtlas containing enemy spritesheet information
		runningAtlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));
		//Fetches all sprites matchin keyword 'spoder'
		runningAnimation =
				new Animation<TextureRegion>(
						0.1f,
						runningAtlas.findRegions("spoder"),
						Animation.PlayMode.LOOP
				);
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

	public boolean shouldRemove() { return remove; }
	public int getScore() { return score; }

	public void update(float dt) {

		if(!isStopped){
			x += dx * dt;
		}

		stateTimeRunning += dt;
		stateTimeAttacking += dt;

		bounds.setPosition(x, y);

        attackTimer += dt;
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
        if (Math.floor(attackTimer) != attackCooldown) {
            attackCooldown += 1.0f;
            if (Math.floor(attackTimer % 2) == 0) {

                if (attackCounter == 0) {
                    // Extra counter needed for weired timer behavior
                    attackCounter++;
                    return this.damage;
                } else return 0;

            } else {
                attackCounter = 0;
                return 0;
            }
        } else return 0;
    }

    public float getHealth(){
		return this.health;
	}

	public void getHurt(float damage){
		this.health -= damage;
	}
}
