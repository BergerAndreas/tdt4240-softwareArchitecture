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
        //Fetches all sprites matchin keyword 'spoder'
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
    public void createTrumpRunningAnimation(){

    }
    public void createTrumpAttackAnimation(){

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

    public static void init() {
        createZombieRunningAnimation();
        createZombieAttackAnimation();
    }
}
