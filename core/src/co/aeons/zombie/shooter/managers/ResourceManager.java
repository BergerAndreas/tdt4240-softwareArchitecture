package co.aeons.zombie.shooter.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ResourceManager {


    //Zombies
    private static Animation<TextureRegion> zombieRunningAnimation;
    private static  Animation<TextureRegion> zombieAttackAnimation;
    //Trump
    private static Animation<TextureRegion> trumpRunningAnimation;
    private static  Animation<TextureRegion> trumpAttackAnimation;
    //Sanic
    private static Animation<TextureRegion> sanicRunningAnimation;
    private static  Animation<TextureRegion> sanicAttackAnimation;
    //Player texture
    private static  Texture playerTexture;
    //Background
    private static  Texture bg;
    //Bullets
    private static  Sprite bullet;
    private static  Sprite brBullet;
    private static  Sprite shotGunBullet;
    //Wall texture
    private static Texture wallTexture;
    //Weapons
    private static  Texture battleRifleTexture;
    private static  Texture pistolTexture;
    private static  Texture shotgunTexture;
    private static Texture healthBar;


    //Buttons
    // TODO: 4/20/2018 Buttons and powerups

    private static void createZombieRunningAnimation(){
        //Opens textureAtlas containing enemy spritesheet information
        TextureAtlas runningAtlas = new TextureAtlas(Gdx.files.internal("enemies/pack.atlas"));
        //Fetches all sprites matching keyword 'spoder'
        zombieRunningAnimation =
                new Animation<TextureRegion>(
                        0.1f,
                        runningAtlas.findRegions("enemies/spoder"),
                        Animation.PlayMode.LOOP
                );
    }

    private static void createZombieAttackAnimation(){
        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("enemies/spooder.atlas"));
        zombieAttackAnimation = new Animation<TextureRegion>(
                0.1f,
                attackAtlas.findRegions("enemies/spooder"),
                Animation.PlayMode.LOOP
        );
    }

    private static void createTrumpRunningAnimation(){
        TextureAtlas runningAtlas = new TextureAtlas(Gdx.files.internal("enemies/Trump/trump.atlas"));
        trumpRunningAnimation = new Animation<TextureRegion>(
                0.1f,
                runningAtlas.findRegions("trump_walk"),
                Animation.PlayMode.LOOP
        );
    }

    private static void createTrumpAttackAnimation(){
//        TODO: Make attack animation (is currently walk animation)
        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("enemies/Trump/trump.atlas"));
        trumpAttackAnimation = new Animation<TextureRegion>(
                0.1f,
                attackAtlas.findRegions("trump_walk"),
                Animation.PlayMode.LOOP
        );
    }

    private static void createSanicRunningAnimation() {
        TextureAtlas runningAtlas = new TextureAtlas(Gdx.files.internal("enemies/sanic/sanicrun/sanicrunning.atlas"));
        sanicRunningAnimation = new Animation<TextureRegion>(
                0.1f,
                runningAtlas.findRegions("sanicRun"),
                Animation.PlayMode.LOOP
        );
    }

    private static void createSanicAttackAnimation() {
        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("enemies/sanic/sanicattack/sanicattack.atlas"));
        sanicAttackAnimation = new Animation<TextureRegion>(
                0.1f,
                attackAtlas.findRegions("sanicAttack"),
                Animation.PlayMode.LOOP
        );
    }

    private static void createPlayerTexture(){
        playerTexture = new Texture("players/alfyboi.png");
    }

    private static void createBg(){
        bg = new Texture(Gdx.files.internal("backgrounds/grasspath2.jpg"));
    }

    private static void createBullet(){
        bullet = new Sprite(new Texture("weapons/pistol1.png"));
    }

    private static void createBrBullet(){
        brBullet = new Sprite(new Texture("weapons/assault1.png"));
    }

    private static void createShotgunBullet(){
        shotGunBullet = new Sprite(new Texture("weapons/shotgun1.png"));
    }

    private static void createWallTexture(){
        wallTexture = new Texture("walls/redBrick.jpg");
        healthBar = new Texture("walls/bar.png");
    }

    private static void createBattleRifleTexture(){
        battleRifleTexture = new Texture("weapons/assault1.png");
    }

    private static void createPistolTexture(){
        pistolTexture = new Texture("weapons/pistol1.png");
    }

    private static void createShotgunTexture(){
        shotgunTexture = new Texture("weapons/shotgun1.png");
    }

    public static Animation<TextureRegion> getZombieRunningAnimation(){
        return zombieRunningAnimation;
    }

    public static Animation<TextureRegion> getZombieAttackAnimation(){
        return zombieAttackAnimation;
    }

    public static Animation<TextureRegion> getTrumpRunningAnimation(){
        return trumpRunningAnimation;
    }

    public static Animation<TextureRegion> getTrumpAttackAnimation(){
        return trumpAttackAnimation;
    }

    public static Animation<TextureRegion> getSanicRunningAnimation(){
        return sanicRunningAnimation;
    }

    public static Animation<TextureRegion> getSanicAttackAnimation(){
        return sanicAttackAnimation;
    }

    public static Texture getPlayerTexture() {
        return playerTexture;
    }

    public static Texture getBg() {
        return bg;
    }

    public static Sprite getBrBullet() {
        return brBullet;
    }

    public static Sprite getShotGunBullet() {
        return shotGunBullet;
    }

    public static Sprite getBullet() {
        return bullet;
    }

    public static Texture getWallTexture() {
        return wallTexture;
    }

    public static Texture getHealthBar() {
        return healthBar;
    }

    public static Texture getBattleRifleTexture() {
        return battleRifleTexture;
    }

    public static Texture getPistolTexture() {
        return pistolTexture;
    }

    public static Texture getShotgunTexture() {
        return shotgunTexture;
    }

    public static void init() {
//        Animations
        createZombieRunningAnimation();
        createZombieAttackAnimation();
        createTrumpRunningAnimation();
        createTrumpAttackAnimation();
        createSanicRunningAnimation();
        createSanicAttackAnimation();
//        Player
        createPlayerTexture();
//        Background
        createBg();
//        Wall and health
        createWallTexture();
//        Bullets
        createBrBullet();
        createBullet();
        createShotgunBullet();
//        Weapons
        createPistolTexture();
        createShotgunTexture();
        createBattleRifleTexture();
    }
}
