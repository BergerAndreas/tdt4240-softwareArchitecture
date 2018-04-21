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
import co.aeons.zombie.shooter.managers.Save;

import static co.aeons.zombie.shooter.ZombieShooter.cam;
import static co.aeons.zombie.shooter.ZombieShooter.gamePort;

/**
 * Created by Erikkvo on 15-Apr-18.
 */

public class HighscoreState extends GameState {

    private BitmapFont font;
    private GlyphLayout layout;

    private SpriteBatch sb;
    private Skin skin;
    private Stage stage;
    private Texture bg;

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
        bg = new Texture(Gdx.files.internal("backgrounds/grasspath2.jpg"));

//        Load save file to screen
        Save.load();
        highscores = Save.gd.getHighScores();
        names = Save.gd.getNames();

        skin = new Skin(Gdx.files.internal("skins/neutralizer-ui.json"));
        Gdx.input.setInputProcessor(this.stage);
        TextButton backButton = new TextButton("Back", skin);

//        Back button takes user to Menu screen
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.MENU);
            }
        });

        backButton.setPosition((ZombieShooter.WIDTH - backButton.getWidth()) / 2, 50);
        stage.addActor(backButton);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw() {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        sb.draw(bg, 0, 0, ZombieShooter.WIDTH, ZombieShooter.HEIGHT);
        String s = "Highscores";
        font.getData().setScale(2, 2);
        layout.setText(font, s);
        float width = layout.width;

//        Draw highscores on screen
        font.draw(sb, s, (cam.viewportWidth - width)/2, cam.viewportHeight - 25);

        font.getData().setScale(1, 1);
        for(int i=0; i<highscores.length; i++){
            s = String.format("%2d. %-8s %-10s%n", i+1, highscores[i], names[i]);
//            Sample string for scores to handle width
            layout.setText(font, "9. 9999 BOBBYBR");
            float sampleScoreWidth = layout.width;
            layout.setText(font, s);
            font.draw(sb, s, (cam.viewportWidth - sampleScoreWidth)/2, cam.viewportHeight - 75 - 20*i);
        }

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
