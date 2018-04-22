package co.aeons.zombie.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.Difficulty;
import co.aeons.zombie.shooter.managers.GameStateManager;

import static co.aeons.zombie.shooter.ZombieShooter.cam;
import static co.aeons.zombie.shooter.ZombieShooter.gamePort;

/**
 * Created by Erikkvo on 19-Apr-18.
 */

public class DifficultyState extends GameState {

    private Stage stage;
    private SpriteBatch sb;
    private Skin skin;

    private BitmapFont titleFont, difficultyFont;
    private GlyphLayout layout;
    private String s;
    private Texture bg;

    public DifficultyState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {
        sb = new SpriteBatch();
        stage = new Stage(gamePort);
        skin = new Skin(Gdx.files.internal("skins/neutralizer-ui.json"));
        bg = new Texture(Gdx.files.internal("backgrounds/grasspath2.jpg"));
        Gdx.input.setInputProcessor(this.stage);

        difficultyFont = new BitmapFont();
        titleFont = new BitmapFont();
        layout = new GlyphLayout();

        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();
        //Create buttons

        final TextButton ezButton = new TextButton("EZ", skin);
        final TextButton hotButton = new TextButton("Hot", skin);
        final TextButton spicyButton = new TextButton("Spicy", skin);
        final TextButton jesusButton = new TextButton("Jesus Take The Wheel", skin);
        TextButton backButton = new TextButton("Back", skin);

        s = "";

        //Add listeners to buttons
        ezButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Difficulty.setDifficulty(1);
                s = ezButton.getText().toString();
            }
        });
        hotButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Difficulty.setDifficulty(2);
                s = hotButton.getText().toString();
            }
        });
        spicyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Difficulty.setDifficulty(3);
                s = spicyButton.getText().toString();
            }
        });
        jesusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Difficulty.setDifficulty(4);
                s = jesusButton.getText().toString();
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.MENU);
            }
        });

        //Add buttons to table
        mainTable.add(ezButton).width(175).pad(5);
        mainTable.row();
        mainTable.add(hotButton).width(175).pad(5);
        mainTable.row();
        mainTable.add(spicyButton).width(175).pad(5);
        mainTable.row();
        mainTable.add(jesusButton).width(175).pad(5);
        backButton.setPosition((cam.viewportWidth - backButton.getWidth())/2, 50);
        stage.addActor(mainTable);
        stage.addActor(backButton);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw() {
        sb.setProjectionMatrix(ZombieShooter.cam.combined);
        sb.begin();

        sb.draw(bg, 0, 0, ZombieShooter.WIDTH, ZombieShooter.HEIGHT);
        titleFont.getData().setScale(2,2);
        layout.setText(titleFont, "Difficulty:");
        titleFont.draw(sb, layout, (cam.viewportWidth - layout.width)/2, cam.viewportHeight - 10);
        layout.setText(difficultyFont, s);
        difficultyFont.draw(sb, layout, (cam.viewportWidth - layout.width)/2, cam.viewportHeight - 60);

        sb.end();
        //Make stage show stuff
        this.stage.act();
        this.stage.draw();
    }

    @Override
    public void dispose() {

    }
}
