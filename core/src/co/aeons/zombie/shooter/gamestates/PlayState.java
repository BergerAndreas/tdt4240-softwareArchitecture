package co.aeons.zombie.shooter.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.entities.Zombie;
import co.aeons.zombie.shooter.entities.Bullet;
import co.aeons.zombie.shooter.entities.FlyingSaucer;
import co.aeons.zombie.shooter.entities.Player;
import co.aeons.zombie.shooter.managers.GameInputProcessor;
import co.aeons.zombie.shooter.managers.GameKeys;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.managers.Jukebox;

public class PlayState extends GameState {

    private SpriteBatch sb;
    private ShapeRenderer sr;

    private BitmapFont font;
    private Player hudPlayer;

    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Zombie> zombies;
    private ArrayList<Bullet> enemyBullets;

    private FlyingSaucer flyingSaucer;
    private float fsTimer;
    private float fsTime;
    private Rectangle playerLane;

    private int level;
    private int totalAsteroids;
    private int numAsteroidsLeft;

    private float maxDelay;
    private float minDelay;
    private float currentDelay;
    private float bgTimer;
    private boolean playLowPulse;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        sb = new SpriteBatch();
        sr = new ShapeRenderer();


        bullets = new ArrayList<Bullet>();

        player = new Player(bullets);

        zombies = new ArrayList<Zombie>();


        level = 1;
        spawnAsteroids();

        hudPlayer = new Player(null);

        fsTimer = 0;
        fsTime = 15;
        enemyBullets = new ArrayList<Bullet>();

        // set up bg music
        maxDelay = 1;
        minDelay = 0.25f;
        currentDelay = maxDelay;
        bgTimer = maxDelay;
        playLowPulse = true;
        playerLane = new Rectangle(0, 0, ZombieShooter.WIDTH/3,
                ZombieShooter.HEIGHT);
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {
                return true;
            }

            @Override
            public boolean touchUp(int x, int y, int pointer, int button) {
                // your touch up code here
                return true; // return true to indicate the event was handled
            }

            @Override
            public boolean touchDragged(int x, int y, int pointer) {
                Vector2 tmpVec2 = new Vector2();
                //translateScreenToWorldCoordinates(x, y);
                //stage.getViewport().unproject(tmpVec2.set(x, y));

                if (playerLane.contains(x, y)) {
                //player.setTransform(new Vector2(player.getUserData().getRunningPosition().x, tmpVec2.y / B2DConstants.PPM), 0);

                player.setPosition(player.getx(), ZombieShooter.HEIGHT-y);
                }
                return true;
            }
        });

    }


    private void splitAsteroids(Zombie a) {
        numAsteroidsLeft--;
        currentDelay = ((maxDelay - minDelay) *
                numAsteroidsLeft / totalAsteroids)
                + minDelay;
        if (a.getType() == Zombie.LARGE) {
            zombies.add(
                    new Zombie(a.getx(), a.gety(), Zombie.MEDIUM));
            zombies.add(
                    new Zombie(a.getx(), a.gety(), Zombie.MEDIUM));
        }
        if (a.getType() == Zombie.MEDIUM) {
            zombies.add(
                    new Zombie(a.getx(), a.gety(), Zombie.SMALL));
            zombies.add(
                    new Zombie(a.getx(), a.gety(), Zombie.SMALL));
        }
    }

    private void spawnAsteroids() {

        zombies.clear();

        int numToSpawn = 4 + level - 1;
        totalAsteroids = numToSpawn * 7;
        numAsteroidsLeft = totalAsteroids;
        currentDelay = maxDelay;

        for (int i = 0; i < numToSpawn; i++) {

            float x = MathUtils.random(ZombieShooter.WIDTH);
            float y = MathUtils.random(ZombieShooter.HEIGHT);

            float dx = x - player.getx();
            float dy = y - player.gety();
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            while (dist < 100) {
                x = MathUtils.random(ZombieShooter.WIDTH);
                y = MathUtils.random(ZombieShooter.HEIGHT);
                dx = x - player.getx();
                dy = y - player.gety();
                dist = (float) Math.sqrt(dx * dx + dy * dy);
            }

            zombies.add(new Zombie(x, y, Zombie.LARGE));

        }

    }

    public void update(float dt) {

        // get user input
        handleInput();

        // next level
        if (zombies.size() == 0) {
            level++;
            spawnAsteroids();
        }

        // update player
        player.update(dt);
        if (player.isDead()) {
            if (player.getLives() == 0) {
                Jukebox.stopAll();
                gsm.setState(GameStateManager.GAMEOVER);
                return;
            }
            player.reset();
            player.loseLife();
            flyingSaucer = null;
            Jukebox.stop("smallsaucer");
            Jukebox.stop("largesaucer");
            return;
        }

        // update player bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(dt);
            if (bullets.get(i).shouldRemove()) {
                bullets.remove(i);
                i--;
            }
        }

        // update flying saucer
        if (flyingSaucer == null) {
            fsTimer += dt;
            if (fsTimer >= fsTime) {
                fsTimer = 0;
                int type = MathUtils.random() < 0.5 ?
                        FlyingSaucer.SMALL : FlyingSaucer.LARGE;
                int direction = MathUtils.random() < 0.5 ?
                        FlyingSaucer.RIGHT : FlyingSaucer.LEFT;
                flyingSaucer = new FlyingSaucer(
                        type,
                        direction,
                        player,
                        enemyBullets
                );
            }
        }
        // if there is a flying saucer already
        else {
            flyingSaucer.update(dt);
            if (flyingSaucer.shouldRemove()) {
                flyingSaucer = null;
                Jukebox.stop("smallsaucer");
                Jukebox.stop("largesaucer");
            }
        }

        // update fs bullets
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).update(dt);
            if (enemyBullets.get(i).shouldRemove()) {
                enemyBullets.remove(i);
                i--;
            }
        }

        // update zombies
        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).update(dt);
            if (zombies.get(i).shouldRemove()) {
                zombies.remove(i);
                i--;
            }
        }


        // check collision
        checkCollisions();

        // play bg music
        bgTimer += dt;
        if (!player.isHit() && bgTimer >= currentDelay) {
            if (playLowPulse) {
                Jukebox.play("pulselow");
            } else {
                Jukebox.play("pulsehigh");
            }
            playLowPulse = !playLowPulse;
            bgTimer = 0;
        }

    }

    private void checkCollisions() {

        // player-asteroid collision
        if (!player.isHit()) {
            for (int i = 0; i < zombies.size(); i++) {
                Zombie a = zombies.get(i);
                if (a.intersects(player)) {
                    player.hit();
                    zombies.remove(i);
                    i--;
                    splitAsteroids(a);
                    Jukebox.play("explode");
                    break;
                }
            }
        }

        // bullet-asteroid collision
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            for (int j = 0; j < zombies.size(); j++) {
                Zombie a = zombies.get(j);
                if (a.contains(b.getx(), b.gety())) {
                    bullets.remove(i);
                    i--;
                    zombies.remove(j);
                    j--;
                    splitAsteroids(a);
                    player.incrementScore(a.getScore());
                    Jukebox.play("explode");
                    break;
                }
            }
        }

        // player-flying saucer collision
        if (flyingSaucer != null) {
            if (player.intersects(flyingSaucer)) {
                player.hit();
                flyingSaucer = null;
                Jukebox.stop("smallsaucer");
                Jukebox.stop("largesaucer");
                Jukebox.play("explode");
            }
        }

        // bullet-flying saucer collision
        if (flyingSaucer != null) {
            for (int i = 0; i < bullets.size(); i++) {
                Bullet b = bullets.get(i);
                if (flyingSaucer.contains(b.getx(), b.gety())) {
                    bullets.remove(i);
                    i--;

                    player.incrementScore(flyingSaucer.getScore());
                    flyingSaucer = null;
                    Jukebox.stop("smallsaucer");
                    Jukebox.stop("largesaucer");
                    Jukebox.play("explode");
                    break;
                }
            }
        }

        // player-enemy bullets collision
        if (!player.isHit()) {
            for (int i = 0; i < enemyBullets.size(); i++) {
                Bullet b = enemyBullets.get(i);
                if (player.contains(b.getx(), b.gety())) {
                    player.hit();
                    enemyBullets.remove(i);
                    i--;
                    Jukebox.play("explode");
                    break;
                }
            }
        }

        // flying saucer-asteroid collision
        if (flyingSaucer != null) {
            for (int i = 0; i < zombies.size(); i++) {
                Zombie a = zombies.get(i);
                if (a.intersects(flyingSaucer)) {
                    zombies.remove(i);
                    i--;
                    splitAsteroids(a);
                    flyingSaucer = null;
                    Jukebox.stop("smallsaucer");
                    Jukebox.stop("largesaucer");
                    Jukebox.play("explode");
                    break;
                }
            }
        }

        // asteroid-enemy bullet collision
        for (int i = 0; i < enemyBullets.size(); i++) {
            Bullet b = enemyBullets.get(i);
            for (int j = 0; j < zombies.size(); j++) {
                Zombie a = zombies.get(j);
                if (a.contains(b.getx(), b.gety())) {
                    zombies.remove(j);
                    j--;
                    splitAsteroids(a);
                    enemyBullets.remove(i);
                    i--;
                    Jukebox.play("explode");
                    break;
                }
            }
        }

    }

    public void draw() {

        sb.setProjectionMatrix(ZombieShooter.cam.combined);
        sr.setProjectionMatrix(ZombieShooter.cam.combined);

        // draw player
        player.draw(sr);

        // draw bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(sr);
        }

        // draw flying saucer
        if (flyingSaucer != null) {
            flyingSaucer.draw(sr);
        }

        // draw fs bullets
        // for (int i = 0; i < enemyBullets.size(); i++) {
        //   enemyBullets.get(i).draw(sr);
        //}

        // draw zombies
        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).draw(sb);
        }


        // draw score
        sb.setColor(1, 1, 1, 1);
        sb.begin();
        sb.end();

        // draw lives
        for (int i = 0; i < player.getLives(); i++) {
            hudPlayer.setPosition(40 + i * 10, 360);
            hudPlayer.draw(sr);
        }

    }

    public void handleInput() {

        //Handle input logic
        if (!player.isHit()) {
            player.setLeft(GameKeys.isDown(GameKeys.LEFT));
            player.setRight(GameKeys.isDown(GameKeys.RIGHT));
            player.setUp(GameKeys.isDown(GameKeys.UP));
            if (GameKeys.isPressed(GameKeys.SPACE)) {
                player.shoot();
            }
        }

    }

    public void dispose() {
        sb.dispose();
        sr.dispose();
        font.dispose();
    }


}









