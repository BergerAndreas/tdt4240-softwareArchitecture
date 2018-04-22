package co.aeons.zombie.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.managers.Jukebox;

import static co.aeons.zombie.shooter.ZombieShooter.cam;
import static co.aeons.zombie.shooter.ZombieShooter.gamePort;

public class MenuState extends GameState {

    // Cameras and viewport
    private Stage stage;

    private SpriteBatch sb;
    private ShapeRenderer sr;

    private Skin skin;
    private TextureAtlas atlas;
    private Texture bg;


    public MenuState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        sb = new SpriteBatch();
        sr = new ShapeRenderer();

        stage = new Stage(gamePort);
        atlas = new TextureAtlas(Gdx.files.internal("skins/neutralizer-ui.atlas"));
        skin = new Skin(Gdx.files.internal("skins/neutralizer-ui.json"));
        bg = new Texture(Gdx.files.internal("backgrounds/grasspath2.jpg"));

        Gdx.input.setInputProcessor(this.stage);


        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();
        //Create buttons

        TextButton singleplayerButton = new TextButton("Singleplayer", skin);
        TextButton multiplayerButton = new TextButton("Multiplayer", skin);
        TextButton highscoreButton = new TextButton("Highscore", skin);
        TextButton difficultyButton = new TextButton("Difficulty", skin);
        TextButton tutorialButton = new TextButton("Tutorial", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        //Add listeners to buttons
        singleplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.PLAY);
            }
        });
        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO: Add multiplayer listener
                gsm.setState(GameStateManager.MULTIPLAYERMENU);
            }
        });
        highscoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.HIGHSCORE);
            }
        });
        difficultyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.DIFFICULTY);
            }
        });
        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.HELP);
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //Add buttons to table
        mainTable.add(singleplayerButton).width(150).pad(5);
        mainTable.row();
        mainTable.add(multiplayerButton).width(150).pad(5);
        mainTable.row();
        mainTable.add(difficultyButton).width(150).pad(5);
        mainTable.row();
        mainTable.add(tutorialButton).width(150).pad(5);
        mainTable.row();
        mainTable.add(highscoreButton).width(150).pad(5);
        exitButton.setPosition((cam.viewportWidth - exitButton.getWidth())/2, 50);
        stage.addActor(mainTable);
        stage.addActor(exitButton);
    }

    public void update(float dt) {

    }

    public void draw() {

        sb.setProjectionMatrix(ZombieShooter.cam.combined);
        sr.setProjectionMatrix(ZombieShooter.cam.combined);

        sb.begin();
        sb.draw(bg, 0, 0, ZombieShooter.WIDTH, ZombieShooter.HEIGHT);
        sb.end();

        //Make stage show stuff
        this.stage.act();
        this.stage.draw();
    }

    public void dispose() {
        sb.dispose();
        sr.dispose();
        stage.dispose();
    }

}










