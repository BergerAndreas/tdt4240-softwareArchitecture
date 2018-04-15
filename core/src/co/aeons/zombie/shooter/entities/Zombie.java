package co.aeons.zombie.shooter.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

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

    private int numPoints;
    private float[] dists;

    private int score;

    private boolean remove;

    public Zombie(float x, float y, int type) {

        this.x = x;
        this.y = y;
        this.type = type;

        if (type == SMALL) {
            numPoints = 8;
            width = height = 12;
            speed = MathUtils.random(70, 100);
            score = 100;
        } else if (type == MEDIUM) {
            numPoints = 10;
            width = height = 20;
            speed = MathUtils.random(50, 60);
            score = 50;
        } else if (type == LARGE) {
            numPoints = 12;
            width = height = 40;
            speed = MathUtils.random(20, 30);
            score = 20;
        }

        rotationSpeed = MathUtils.random(-1, 1);

        radians = MathUtils.random(2 * 3.1415f);
        dx = -50;
        dy = 0;

        shapex = new float[numPoints];
        shapey = new float[numPoints];
        dists = new float[numPoints];

        int radius = width / 2;
        for (int i = 0; i < numPoints; i++) {
            dists[i] = MathUtils.random(radius / 2, radius);
        }

        setShape();
        createRunningAnimation();
        createAttackAnimation();


    }

    private void createRunningAnimation() {
        //Opens textureAtlas containing enemy spritesheet information
        runningAtlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));
        //Fetches all sprites matchin keyword 'spoder'
        runningAnimation = new Animation<TextureRegion>(
                0.1f,
                runningAtlas.findRegions("spoder"),
                Animation.PlayMode.LOOP
        );
        //Initializes statetime for animations
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


    private void setShape() {
        float angle = 0;
        for (int i = 0; i < numPoints; i++) {
            shapex[i] = x + MathUtils.cos(angle + radians) * dists[i];
            shapey[i] = y + MathUtils.sin(angle + radians) * dists[i];
            angle += 2 * 3.1415f / numPoints;
        }
    }

    public int getType() {
        return type;
    }

    public boolean shouldRemove() {
        return remove;
    }

    public int getScore() {
        return score;
    }

    public void update(float dt) {

        if (!isStopped) {
            x += dx * dt;
        }

        y += dy * dt;

        radians += rotationSpeed * dt;
        stateTimeRunning += dt;
        stateTimeAttacking += dt;

        setShape();
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


















