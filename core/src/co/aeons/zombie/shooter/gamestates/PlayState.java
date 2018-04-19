package co.aeons.zombie.shooter.gamestates;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.entities.Wall;
import co.aeons.zombie.shooter.entities.Zombie;
import co.aeons.zombie.shooter.entities.bullets.Bullet;
import co.aeons.zombie.shooter.entities.Player;
import co.aeons.zombie.shooter.entities.Trump;
import co.aeons.zombie.shooter.entities.buttons.CycleDownButton;
import co.aeons.zombie.shooter.entities.buttons.CycleUpButton;
import co.aeons.zombie.shooter.entities.buttons.EffectButton;
import co.aeons.zombie.shooter.entities.buttons.FireButton;
import co.aeons.zombie.shooter.entities.buttons.InstaKill;
import co.aeons.zombie.shooter.entities.buttons.MuteButton;
import co.aeons.zombie.shooter.factories.RandomButtonFactory;
import co.aeons.zombie.shooter.managers.Difficulty;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.managers.Jukebox;

import static co.aeons.zombie.shooter.ZombieShooter.gamePort;
import static co.aeons.zombie.shooter.ZombieShooter.cam;

public class PlayState extends GameState {

    protected SpriteBatch sb;
    protected ShapeRenderer sr;
    private BitmapFont scoreFont, magazineFont, wallHealthFont;
    private GlyphLayout layout;

    protected Player player;
    protected ArrayList<Bullet> bullets;
    private ArrayList<Zombie> zombies;
    private Wall wall;
    private long score;

    //Boundaries
    private Rectangle playerLane;
    private Rectangle fireBounds;
    private Rectangle effectButtonBounds;
    private Rectangle cUpBounds;
    private Rectangle cDownBounds;
    private Rectangle muteBounds;

    //buttons
    private RandomButtonFactory buttonFactory;
    private FireButton fireButton;
    private MuteButton muteButton;
    private CycleUpButton cycleUpButton;
    private CycleDownButton cycleDownButton;
    private EffectButton effectButton;

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

    //Duration of an effect
    private int effectTimer;

    //DamageModifier
    private int damageModifier;


    //Flag to check if powerup is used
    private boolean isClicked;

    protected Stage stage;

    public PlayState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        scoreFont = new BitmapFont();
        wallHealthFont = new BitmapFont();
        magazineFont = new BitmapFont();
        layout = new GlyphLayout();

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
        damageModifier = 1;
        effectTimer = 0;

        //Button initialization
        buttonFactory = new RandomButtonFactory(cam);

        //Create bounds for buttons
        fireBounds = new Rectangle(
                cam.viewportWidth - 200,
                0,
                cam.viewportWidth / 8,
                cam.viewportHeight / 6
        );
        muteBounds = new Rectangle(
                cam.viewportWidth - 50,
                cam.viewportHeight - 50,
                cam.viewportWidth / 16,
                cam.viewportHeight / 16
        );
        playerLane = new Rectangle(
                0,
                0,
                cam.viewportWidth / 3,
                cam.viewportHeight
        );
        cUpBounds = new Rectangle(
                cam.viewportWidth - 300,
                0,
                cam.viewportWidth / 16,
                cam.viewportHeight / 12
        );
        cDownBounds = new Rectangle(
                cam.viewportWidth - 350,
                0,
                cam.viewportWidth / 16,
                cam.viewportHeight / 12
        );

        //Create buttons with above bounds
        fireButton = new FireButton(fireBounds);
        muteButton = new MuteButton(muteBounds);
        cycleUpButton = new CycleUpButton(cUpBounds);
        cycleDownButton = new CycleDownButton(cDownBounds);

        //Create empty button
        effectButton = new InstaKill(new Rectangle(0, 0, 0, 0));

        // set up bg music
        maxDelay = 1;
        minDelay = 0.25f;
        currentDelay = maxDelay;
        bgTimer = maxDelay;
        musicStarted = false;

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
    }



    private void spawnZombies() {

        int numToSpawn = 4 + level - 1;
        totalZombies = numToSpawn;

        for (int i = 0; i < numToSpawn; i++) {
            float x = randInt(ZombieShooter.WIDTH + 50, ZombieShooter.WIDTH + 150);
            float y = randInt(0, ZombieShooter.HEIGHT - 100);
            zombies.add(new Trump(x, y, Difficulty.getDifficulty()));
            zombies.add(new Zombie(x, y, Difficulty.getDifficulty()));
            // TODO: 17/04/2018 Unfucke logikken for spawning, nå hanver Trump på toppen av en zambi

        }

    }

    public void update(float dt) {
        // check collision
        checkCollisions();

        // reset modifier
        effectTimer -= dt;
        System.out.println(effectTimer);
        if(effectTimer <= 0) {
            resetEffects();
        }

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

    //Insert new modifierresets here
    private void resetEffects() {
        this.damageModifier = 1;
        this.effectTimer = 0;
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

                    a.getHurt(b.getDamage()*damageModifier);

                    if (a.getHealth() <= 0){
                        zombies.remove(j);
                        j--;
                        this.incrementScore(a.getScore());
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

        //Draw firebutton background rect
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.DARK_GRAY);
        sr.rect(fireBounds.x, fireBounds.y, fireBounds.width, fireBounds.height);
        sr.end();

        // draw buttons
        sb.setColor(0, 1, 1, 1);
        sb.begin();


//        Various HUDs

//        Score
        String scoreOutput = "Score: " + Long.toString(this.getScore());
        this.layout.setText(scoreFont, scoreOutput);
        scoreFont.draw(sb, layout, (this.wall.getx() - layout.width)/2, cam.viewportHeight-5);
//        Magazine-size
        String magazineOutput = Integer.toString(player.getCurrentWeapon().getRemainingBullets()) + "/" + Integer.toString(player.getCurrentWeapon().getClipSize());
        this.layout.setText(magazineFont, magazineOutput);
        scoreFont.draw(sb, layout, cam.viewportWidth - 250, (fireButton.getY() + fireBounds.getHeight())/2);
//        Wall-health
        String wallHealthOutput = "❤" + Integer.toString(this.wall.getHealth());
        this.layout.setText(wallHealthFont, wallHealthOutput);
        scoreFont.draw(sb, layout, (this.wall.getx()+this.wall.getRectangle().getWidth())/2 + 25, (this.wall.gety()+this.wall.getRectangle().getHeight())/2);

        fireButton.draw(sb, 1);
        muteButton.draw(sb, 1);
        effectButton.draw(sb, 1);
        cycleUpButton.draw(sb, 1);
        cycleDownButton.draw(sb, 1);
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

    private void onCycleUpPressed() {
        System.out.println("Next Weapon");
        player.nextWeapon();
        reloadFireButtonTexture();
    }

    private void onCycleDownPressed(){
        player.prevWeapon();
        System.out.println("Previous Weapon");
        reloadFireButtonTexture();
    }

    private void reloadFireButtonTexture() {
        fireButton.setTexturePath(player.getCurrentWeapon().getTexturePath());
        fireButton.loadTextureRegion();
    }

    //Getters and setters
    public ArrayList<Zombie> getZombies() {
        return zombies;
    }


    public void setDamageModifier(int damageModifier) {
        this.damageModifier = damageModifier;
    }

    public void setEffectTimer(int effectTimer) {
        this.effectTimer = effectTimer;
    }
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

        if (cycleUpButton.getBounds().contains(tmpVec2.x, tmpVec2.y)) {
            onCycleUpPressed();
        }

        if (cycleDownButton.getBounds().contains(tmpVec2.x, tmpVec2.y)) {
            onCycleDownPressed();
        }

        return true;
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

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public long getScore() {
        return score;
    }

    public void incrementScore(long score) {
        this.score += score;
    }
}
