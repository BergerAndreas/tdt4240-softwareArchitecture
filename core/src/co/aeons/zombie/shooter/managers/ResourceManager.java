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
    private static  Sprite brBullet;
    private static  Sprite shotGunBullet;
    //Wall texture
    private static Texture wallTexture;
    //Weapons
    private static  Texture battleRifleTexture;
    private static  Texture pistolTexture;
    private static  Texture shotgunTexture;

    //Buttons
    // TODO: 4/20/2018 Buttons and powerups

    public static void createZombieRunningAnimation(){
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

    public static void createZombieAttackAnimation(){
        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("enemies/spooder.atlas"));
        zombieAttackAnimation = new Animation<TextureRegion>(
                0.1f,
                attackAtlas.findRegions("enemies/spooder"),
                Animation.PlayMode.LOOP
        );
    }

    public static void createTrumpRunningAnimation(){
        TextureAtlas runningAtlas = new TextureAtlas(Gdx.files.internal("enemies/Trump/trump.atlas"));
        trumpRunningAnimation = new Animation<TextureRegion>(
                0.1f,
                runningAtlas.findRegions("trump_walk"),
                Animation.PlayMode.LOOP
        );
    }

    public static void createTrumpAttackAnimation(){
//        TODO: Make attack animation (is currently walk animation)
        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("enemies/Trump/trump.atlas"));
        trumpAttackAnimation = new Animation<TextureRegion>(
                0.1f,
                attackAtlas.findRegions("trump_walk"),
                Animation.PlayMode.LOOP
        );
    }

    public static void createSanicRunningAnimation() {
        TextureAtlas runningAtlas = new TextureAtlas(Gdx.files.internal("enemies/sanic/sanicrun/sanicrunning.atlas"));
        sanicRunningAnimation = new Animation<TextureRegion>(
                0.1f,
                runningAtlas.findRegions("sanicRun"),
                Animation.PlayMode.LOOP
        );
    }

    public static void createSanicAttackAnimation() {
        TextureAtlas attackAtlas = new TextureAtlas(Gdx.files.internal("enemies/sanic/sanicattack/sanicattack.atlas"));
        sanicAttackAnimation = new Animation<TextureRegion>(
                0.1f,
                attackAtlas.findRegions("sanicAttack"),
                Animation.PlayMode.LOOP
        );
    }

    public void createPlayerTexture(){

    }

    public void createBg(){

    }

    public void createBrBullet(){

    }

    public void createShotgunBullet(){

    }

    public void createWallTexture(){

    }

    public void createBattleRifleTexture(){

    }

    public void createPistolTexture(){

    }

    public void createShotgunTexture(){

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

    public static void init() {
        createZombieRunningAnimation();
        createZombieAttackAnimation();
        createTrumpRunningAnimation();
        createTrumpAttackAnimation();
        createSanicRunningAnimation();
        createSanicAttackAnimation();
    }
}
