package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.Jukebox;
import co.aeons.zombie.shooter.managers.ResourceManager;

public class Zombie extends SuperObject {
	protected boolean isStopped = false;
	protected String type;

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

    protected float attackTimer = 1.0f;

	private float attackCooldown = 2.0f;
    private int attackCounter = 0;

    protected int damage;

    private boolean remove;

	public Zombie(float x, float y, int difficulty) {

		this.x = x;
		this.y = y;

		this.damage = 10*difficulty;
		this.type = "z";
		width = height = 40;
		speed = MathUtils.random(20, 30);
		score = 20;

		bounds = new Rectangle(0, 0, width, height);
		dx = -50*(4+difficulty-1)/4;

		this.health = 10*difficulty;

		createIdleAnimation();
		createAttackAnimation();

    }

	protected void createIdleAnimation() {
		this.runningAnimation = ResourceManager.getZombieRunningAnimation();
		stateTimeRunning = 0f;
	}

	protected void createAttackAnimation() {
		this.attackAnimation = ResourceManager.getZombieAttackAnimation();
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

	public void deathSound() {
		Jukebox.play("blyat");
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
