package co.aeons.zombie.shooter.gamestates;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.tween.SpriteAccessor;

/**
 * Created by Erikkvo on 15-Apr-18.
 */

public class SplashState extends GameState {

    private SpriteBatch sb;
    private Sprite splash;
    private TweenManager tweenManager;
    private Texture splashTexture;

    public SplashState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {

        sb = new SpriteBatch();
        tweenManager = new TweenManager();
        splashTexture = new Texture("logo.png");

        //        Tween does not know there is a successor, thus we need to register it
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        splash = new Sprite(splashTexture);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //        Creating the animations
        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager); // Start at transparency = 0 (not visible)
        //        Actual animation
        Tween.to(splash, SpriteAccessor.ALPHA, 1).target(1).repeatYoyo(1, 1).setCallback(new TweenCallback() { // Delay and fade out

            //        Switch to menu screen
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                gsm.setState(GameStateManager.MENU);
            }
        }).start(tweenManager);

    }

    @Override
    public void update(float dt) {
        tweenManager.update(dt);
    }

    @Override
    public void draw() {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        splash.draw(sb);
        sb.end();

    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {
        sb.dispose();
        splash.getTexture().dispose();
    }
}
