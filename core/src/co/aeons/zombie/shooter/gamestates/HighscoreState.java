package co.aeons.zombie.shooter.gamestates;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.GameKeys;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.managers.Save;

import static co.aeons.zombie.shooter.ZombieShooter.gamePort;
import static com.badlogic.gdx.Gdx.app;

/**
 * Created by Erikkvo on 15-Apr-18.
 */

public class HighscoreState extends GameState {

    private BitmapFont font;
    private GlyphLayout layout;

    private SpriteBatch sb;
    private Skin skin;
    private Stage stage;

    private long[] highscores;
    private String[] names;


    public HighscoreState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {
        sb = new SpriteBatch();
        font = new BitmapFont();
        stage = new Stage(gamePort);
        layout = new GlyphLayout();
        System.out.println("HIGHSCORES");

        Save.load();
        highscores = Save.gd.getHighScores();
        names = Save.gd.getNames();

        skin = new Skin(Gdx.files.internal("skins/neutralizer-ui.json"));
        Gdx.input.setInputProcessor(this.stage);
        TextButton backButton = new TextButton("Back", skin);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.GAMEOVER);
            }
        });

        backButton.setPosition((ZombieShooter.WIDTH - backButton.getWidth()) / 2, 50);
        stage.addActor(backButton);

    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void draw() {
        sb.setProjectionMatrix(ZombieShooter.cam.combined);
        sb.begin();

        String s = "Highscores";
        font.getData().setScale(2, 2);
        layout.setText(font, s);
        float width = layout.width;

//        Draw highscores on screen
        font.draw(sb, s, (ZombieShooter.WIDTH - width) / 2, ZombieShooter.HEIGHT - 25);
        for (int i = 0; i < highscores.length; i++) {
            s = String.format("%2d. %7s %s", i + 1, highscores[i], names[i]);
            layout.setText(font, s);
            float w = layout.width;
            font.draw(sb, s, (ZombieShooter.WIDTH - w) / 2, 400 - 30 * i);
        }
        sb.end();

        stage.act();
        stage.draw();

    }

    @Override
    public void handleInput() {
        if (GameKeys.isPressed(GameKeys.ENTER) || GameKeys.isPressed(GameKeys.ESCAPE)) {
            gsm.setState(GameStateManager.MENU);
        }
    }

    @Override
    public void dispose() {
        sb.dispose();
        font.dispose();
        stage.dispose();
    }
}
