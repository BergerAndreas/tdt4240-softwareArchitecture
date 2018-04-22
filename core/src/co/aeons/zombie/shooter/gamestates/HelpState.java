package co.aeons.zombie.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.GameStateManager;

import static co.aeons.zombie.shooter.ZombieShooter.cam;
import static co.aeons.zombie.shooter.ZombieShooter.gamePort;

/**
 * Created by Erikkvo on 22-Apr-18.
 */

public class HelpState extends GameState {

    private BitmapFont font;
    private GlyphLayout layout;

    private SpriteBatch sb;
    private Skin skin;
    private Stage stage;
    private Texture bg;

    public HelpState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        sb = new SpriteBatch();
        font = new BitmapFont();
        stage = new Stage(gamePort);
        layout = new GlyphLayout();
        bg = new Texture(Gdx.files.internal("backgrounds/tutorial.png"));

        skin = new Skin(Gdx.files.internal("skins/neutralizer-ui.json"));
        Gdx.input.setInputProcessor(this.stage);
        TextButton backButton = new TextButton("Back", skin);

//        Back button takes user back to Menu screen
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.MENU);
            }
        });

        backButton.setPosition((ZombieShooter.WIDTH - backButton.getWidth()) / 2, 25);
        stage.addActor(backButton);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw() {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        String s = "Tutorial";
        font.getData().setScale(2, 2);
        layout.setText(font, s);
        float width = layout.width;

//        Draw tutorial image on screen
        sb.draw(bg, 0, 60, ZombieShooter.WIDTH, ZombieShooter.HEIGHT-100);
//        Draw Tutorial on screen
        font.draw(sb, s, (cam.viewportWidth - width)/2, cam.viewportHeight - 10);

        sb.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        sb.dispose();
        font.dispose();
        stage.dispose();
    }
}
