package co.aeons.zombie.shooter.gamestates;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.entities.Wall;
import co.aeons.zombie.shooter.entities.Zombie;
import co.aeons.zombie.shooter.entities.Bullet;
import co.aeons.zombie.shooter.entities.Player;
import co.aeons.zombie.shooter.entities.Trump;
import co.aeons.zombie.shooter.entities.buttons.EffectButton;
import co.aeons.zombie.shooter.entities.buttons.FireButton;
import co.aeons.zombie.shooter.entities.buttons.InstaKill;
import co.aeons.zombie.shooter.entities.buttons.MuteButton;
import co.aeons.zombie.shooter.entities.buttons.NukeButton;
import co.aeons.zombie.shooter.factories.RandomButtonFactory;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.managers.Jukebox;

import static co.aeons.zombie.shooter.ZombieShooter.gamePort;
import static co.aeons.zombie.shooter.ZombieShooter.cam;

public class PlayState extends GameState {

    protected SpriteBatch sb;
    protected ShapeRenderer sr;


    private Player player;
    protected ArrayList<Bullet> bullets;
    private ArrayList<Zombie> zombies;
    private Wall wall;

    //Boundaries
    private Rectangle playerLane;
    private Rectangle fireBounds;
    private Rectangle effectButtonBounds;
    private Rectangle muteBounds;

    //buttons
    private RandomButtonFactory buttonFactory;
    private FireButton fireButton;
    private EffectButton effectButton;
    private MuteButton muteButton;
    private NukeButton nukeButton;

    private int level;
    private int totalZombies;

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


        //Set up variables for powerups
        spawnDelay = randInt(0, 10);
        isClicked = true;


        //Button initialization
        buttonFactory = new RandomButtonFactory(cam);

        //Create bounds for buttons
        fireBounds = new Rectangle(cam.viewportWidth - 200, 0,
                cam.viewportWidth / 8, cam.viewportHeight / 6);
        muteBounds = new Rectangle(cam.viewportWidth - 100, cam.viewportHeight - 75,
                cam.viewportWidth / 16, cam.viewportHeight / 16);
        playerLane = new Rectangle(0, 0, cam.viewportWidth / 3,
                cam.viewportHeight);
        //Create buttons with above bounds
        fireButton = new FireButton(fireBounds);
        muteButton = new MuteButton(muteBounds);
        //Create empty button
        effectButton = new InstaKill(new Rectangle(0, 0, 0, 0));

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
                    onFireButtonPressed();
                }

                //Mute button
                if (muteButton.getBounds().contains(tmpVec2.x, tmpVec2.y)) {
                    onMuteButtonPressed();
                }

                //Instakill button
                if (effectButton.getBounds().contains(tmpVec2.x, tmpVec2.y)) {
                    //stage.touchDown(x, y, pointer, button);
                    onEffectButtonPressed();
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



    private void spawnZombies() {

        int numToSpawn = 4 + level - 1;
        totalZombies = numToSpawn;

        for (int i = 0; i < numToSpawn; i++) {
            float x = randInt(ZombieShooter.WIDTH + 50, ZombieShooter.WIDTH + 150);
            float y = randInt(0, ZombieShooter.HEIGHT - 100);
            zombies.add(new Trump(x, y));
            zombies.add(new Zombie(x, y));
            // TODO: 17/04/2018 Unfucke logikken for spawning, nå hanver Trump på toppen av en zambi 

        }

    }

    public void update(float dt) {

        // check collision
        checkCollisions();

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

        // update buttons
        if (timer > spawnDelay && isClicked) {

            effectButton = buttonFactory.produceRandomEffectButton();
            this.stage.addActor(effectButton);

            //Reset variables for next spawning
            //TODO: Change spawndelay range later
            isClicked = false;
            spawnDelay = randInt(0, 10);
            timer = 0;
        }

        if (wall.getHealth() <= 0) {
            Jukebox.getIngameMusic().stop();
            Jukebox.playGameoverMusic();
            gsm.setState(GameStateManager.GAMEOVER);
        }
    }

    private void checkCollisions() {
        //zombie-wall collision
        for (int i = 0; i < zombies.size(); i++) {
            Zombie zombie = zombies.get(i);
            if(zombie.collide(wall)){
                zombie.setStopped(true);
                wall.takeDamage(zombie.attack());
            }

        }

        // bullet-zombie collision
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            for (int j = 0; j < zombies.size(); j++) {
                Zombie a = zombies.get(j);
                if (a.collide(b)) {
                    bullets.remove(i);
                    i--;

                    a.getHurt(10);

                    if (a.getHealt() <= 0){
                        zombies.remove(j);
                        j--;

                    }

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
        player.draw(sb);
        
        // draw bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(sb);
        }

        // draw zombies
        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).draw(sb);
        }

        // draw wall
        wall.draw(sb);

        // draw buttons
        sb.setColor(0, 1, 1, 1);
        sb.begin();
        fireButton.draw(sb, 1);
        muteButton.draw(sb, 1);
        effectButton.draw(sb, 1);
        sb.end();

        // draw buttons
        this.stage.addActor(fireButton);
        this.stage.addActor(muteButton);
        this.stage.act();
        this.stage.draw();

    }

    public void dispose() {
        sb.dispose();
        sr.dispose();
    }

    // helper function to generate random integer
    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    //Method called when FireButton pressed
    private void onFireButtonPressed() {
        player.shoot();
        System.out.println("FireButton pressed");
        Jukebox.play("gunshot");
    }

    //Method called when FireButton pressed
    private void onMuteButtonPressed() {
        Jukebox.toggleMuteMusic();
        muteButton.loadTextureRegion();
    }


    private void onEffectButtonPressed() {
        System.out.println("Instakill activated");
        Jukebox.play("powerup");
        effectButton.effect(this);
        effectButton.remove();
        effectButton = new InstaKill(new Rectangle(0, 0, 0, 0));
        isClicked = true;
    }

    public ArrayList<Zombie> getZombies() {
        return zombies;
    }
}
