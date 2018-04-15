package co.aeons.zombie.shooter.gamestates;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.entities.Wall;
import co.aeons.zombie.shooter.entities.Zombie;
import co.aeons.zombie.shooter.entities.Bullet;
import co.aeons.zombie.shooter.entities.Player;
import co.aeons.zombie.shooter.entities.buttons.FireButton;
import co.aeons.zombie.shooter.entities.buttons.InstaKill;
import co.aeons.zombie.shooter.entities.buttons.MuteButton;
import co.aeons.zombie.shooter.managers.GameKeys;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.managers.Jukebox;

import static co.aeons.zombie.shooter.ZombieShooter.gamePort;
import static co.aeons.zombie.shooter.ZombieShooter.cam;

public class PlayState extends GameState {

    private SpriteBatch sb;
    private ShapeRenderer sr;

    private Player hudPlayer;

    private Player player;
    private ArrayList<Bullet> bullets;
    private ArrayList<Zombie> zombies;
    private ArrayList<Bullet> enemyBullets;
    private Wall wall;

    private float fsTimer;
    private float fsTime;

    //Boundaries
    private Rectangle playerLane;
    private Rectangle fireBounds;
    private Rectangle instakillBounds;
    private Rectangle muteBounds;

    //buttons
    private FireButton fireButton;
    private InstaKill instakillButton;
    private MuteButton muteButton;

    private int level;
    private int totalZombies;
    private int numAsteroidsLeft;

    private float maxDelay;
    private float minDelay;
    private float currentDelay;
    private float bgTimer;
    private boolean musicStarted;

    //Spawndelay for powerups
    private int spawnDelay;
    private float timer;
    private float spawnCooldown;
    private float spawnTimer;

    //Flag to check if powerup is used
    private boolean isClicked;

    private Stage stage;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        sb = new SpriteBatch();
        sr = new ShapeRenderer();

        //sets up camera
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
        cam.update();
        stage = new Stage(gamePort, sb);

        bullets = new ArrayList<Bullet>();
        player = new Player(bullets);
        zombies = new ArrayList<Zombie>();
        wall = new Wall();

        level = 1;
        spawnTimer = 1.0f;
        spawnCooldown = 1.0f;

        spawnZombies();

        hudPlayer = new Player(null);

        fsTimer = 0;
        fsTime = 15;
        enemyBullets = new ArrayList<Bullet>();

        //Set up variables for powerups
        spawnDelay = randInt(0, 10);
        isClicked = true;


        //Button initialization
        //Create bounds for buttons
        fireBounds = new Rectangle(cam.viewportWidth - 200, 0,
                cam.viewportWidth / 8, cam.viewportHeight / 6);
        muteBounds = new Rectangle(cam.viewportWidth - 100, cam.viewportHeight - 75,
                cam.viewportWidth / 16, cam.viewportHeight / 16);
        playerLane = new Rectangle(0, 0, cam.viewportWidth / 3,
                cam.viewportHeight);
        //Create buttons with above bounds
        fireButton = new FireButton(fireBounds, new GameFireButtonListener());
        muteButton = new MuteButton(muteBounds, new GameMuteButtonListener());
        //Create empty button
        instakillButton = new InstaKill(new Rectangle(0, 0, 0, 0), new GameInstaKillListener());

        // set up bg music
        maxDelay = 1;
        minDelay = 0.25f;
        currentDelay = maxDelay;
        bgTimer = maxDelay;
        musicStarted = false;
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int x, int y, int pointer, int button) {

                //Need to have this or buttons won't work
                Vector2 tmpVec2 = new Vector2();
                stage.getViewport().unproject(tmpVec2.set(x, y));


                //Fire button
                if (fireButton.getBounds().contains(tmpVec2.x, tmpVec2.y)) {
                    stage.touchDown(x, y, pointer, button);
                    player.shoot();
                }

                //Mute button
                if (muteButton.getBounds().contains(tmpVec2.x, tmpVec2.y)) {
                    stage.touchDown(x, y, pointer, button);
                }

                //Instakill button
                if (instakillButton.getBounds().contains(tmpVec2.x, tmpVec2.y)) {
                    stage.touchDown(x, y, pointer, button);
                }

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
                stage.getViewport().unproject(tmpVec2.set(x, y));

                if (playerLane.contains(tmpVec2.x, tmpVec2.y)) {
                    //player.setTransform(new Vector2(player.getUserData().getRunningPosition().x, tmpVec2.y / B2DConstants.PPM), 0);

                    player.setPosition(player.getx(), tmpVec2.y);
                }
                return true;
            }
        });
    }


    private void splitAsteroids(Zombie a) {
        numAsteroidsLeft--;
        currentDelay = ((maxDelay - minDelay) *
                numAsteroidsLeft / totalZombies)
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

    private void spawnZombies() {

        int numToSpawn = 4 + level - 1;
        totalZombies = numToSpawn * 7;
        numAsteroidsLeft = totalZombies;
        currentDelay = maxDelay;

        for (int i = 0; i < numToSpawn; i++) {
            float x = randInt(ZombieShooter.WIDTH + 50, ZombieShooter.WIDTH + 150);
            float y = randInt(0, ZombieShooter.HEIGHT - 100);
            zombies.add(new Zombie(x, y, Zombie.LARGE));

        }

    }

    public void update(float dt) {

        // get user input
        handleInput();

        // next level

        spawnTimer += dt;
        if (Math.floor(spawnTimer) != spawnCooldown) {
            if (Math.floor(spawnTimer) % 9 == 0) {
                spawnZombies();
            }
            spawnCooldown += 1.0f;

            if (spawnCooldown % 17 == 0) {
                System.out.println("Difficulty increased");
                level += 2;
            }

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

        // update zombies
        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).update(dt);
            if (zombies.get(i).shouldRemove()) {
                zombies.remove(i);
                i--;
            }
        }

        // update spawn powerup button timer
        if (isClicked) {
            this.timer += dt;
        }

        // update instakill
        if (timer > spawnDelay && isClicked) {
            instakillBounds = new Rectangle(randInt(100, (int) cam.viewportWidth - 100), randInt(100, (int) cam.viewportHeight - 100),
                    cam.viewportWidth / 8, cam.viewportHeight / 6);
            //Creates a new instakill button with above bounds
            instakillButton = new InstaKill(instakillBounds, new GameInstaKillListener());
            //Adds instakill button to stage
            this.stage.addActor(instakillButton);

            //Reset variables for next spawning
            //TODO: Change spawndelay range later
            isClicked = false;
            spawnDelay = randInt(0, 10);
            timer = 0;
        }

        if (wall.getHealth() <= 0) {
            gsm.setState(GameStateManager.GAMEOVER);
        }

        // check collision
        checkCollisions();

        // play bg music
        bgTimer += dt;
        if (!player.isHit() && bgTimer >= currentDelay) {
            bgTimer = 0;
        }
    }

    private void checkCollisions() {
        //zombie-wall collision
        for (int i = 0; i < zombies.size(); i++) {
            Zombie zombie = zombies.get(i);
            if (wall.intersects(zombie)) {
                zombie.setStopped(true);

                //FIXME: The way attacks currently work
                wall.takeDamage(zombie.attack());
            }

        }

        // bullet-zombie collision
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
                    Jukebox.play("zombieHit");
                    break;
                }
            }
        }
    }

    public void draw() {

        sb.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);

        // draw player
        player.draw(sr);

        // draw bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(sb);
        }

        // draw zombies
        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).draw(sb);
        }

        //Draw wall
        wall.draw(sb);

        // draw buttons
        sb.setColor(0, 1, 1, 1);
        sb.begin();
        fireButton.draw(sb, 1);
        muteButton.draw(sb, 1);
        instakillButton.draw(sb, 1);
        sb.end();

        // draw lives
        for (int i = 0; i < player.getLives(); i++) {
            hudPlayer.setPosition(40 + i * 10, 360);
            hudPlayer.draw(sr);
        }

        // Draw buttons
        this.stage.addActor(fireButton);
        this.stage.addActor(muteButton);
        this.stage.act();
        this.stage.draw();

    }

    public void handleInput() {

        //Handle input logic
        if (!player.isHit()) {
            if (GameKeys.isPressed(GameKeys.SPACE)) {
                player.shoot();
            }
        }

    }

    public void dispose() {
        sb.dispose();
        sr.dispose();
    }

    //Helper function to generate random integer
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    // Button listeners

    //Firebutton listener class
    private class GameFireButtonListener implements FireButton.FireButtonListener {

        // Adds observer
        @Override
        public void onFire() {
            //Calls this method when button is pressed
            onFireButtonPressed();
        }
    }

    //Method called when FireButton pressed
    private void onFireButtonPressed() {
        //TODO: implement fire button logic
        System.out.println("FireButton pressed");
        Jukebox.play("gunshot");
    }

    //Mutebutton listener class
    private class GameMuteButtonListener implements MuteButton.MuteButtonListener {

        // Adds observer
        @Override
        public void onMute() {
            //Calls this method when button is pressed
            onMuteButtonPressed();
            System.out.println("Mute");
        }
    }

    //Method called when FireButton pressed
    private void onMuteButtonPressed() {
        //TODO: implement mute button logic
        Jukebox.toggleMuteMusic();
    }

    //Instakill listener class
    private class GameInstaKillListener implements InstaKill.InstakillListener {

        @Override
        public void instaKillActivate() {
            InstakillPressed();
            Jukebox.play("powerup");
        }
    }

    private void InstakillPressed() {
        //TODO: Fix button
        System.out.println("Instakill activated");
        instakillButton.remove();
        instakillButton = new InstaKill(new Rectangle(0, 0, 0, 0), new GameInstaKillListener());
        isClicked = true;
    }
}
