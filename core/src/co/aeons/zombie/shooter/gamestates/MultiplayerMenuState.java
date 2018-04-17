package co.aeons.zombie.shooter.gamestates;

import com.badlogic.gdx.Gdx;
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

import static co.aeons.zombie.shooter.ZombieShooter.gamePort;

public class MultiplayerMenuState extends GameState {

    // Cameras and viewport
    private Stage stage;

    private SpriteBatch sb;
    private ShapeRenderer sr;

    private Skin skin;
    private TextureAtlas atlas;

    public MultiplayerMenuState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void init() {

        sb = new SpriteBatch();
        sr = new ShapeRenderer();

        stage = new Stage(gamePort);

        atlas = new TextureAtlas(Gdx.files.internal("skins/neutralizer-ui.atlas"));
        skin = new Skin(Gdx.files.internal("skins/neutralizer-ui.json"));
        Gdx.input.setInputProcessor(this.stage);

        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();
        //Create buttons

        TextButton singleplayerButton = new TextButton("Invite", skin);
        TextButton multiplayerButton = new TextButton("Quick", skin);
        TextButton optionsButton = new TextButton("See invitations", skin);
        TextButton backButton = new TextButton("Back", skin);

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
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gsm.setState(GameStateManager.MENU);
            }
        });


        //Add buttons to table
        mainTable.add(singleplayerButton);
        mainTable.row();
        mainTable.add(multiplayerButton);
        mainTable.row();
        mainTable.add(optionsButton);
        mainTable.row();
        mainTable.add(backButton);
        stage.addActor(mainTable);


    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw() {

        sb.setProjectionMatrix(ZombieShooter.cam.combined);
        sr.setProjectionMatrix(ZombieShooter.cam.combined);


        //Make stage show stuff
        this.stage.act();
        this.stage.draw();


    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {
        sb.dispose();
        sr.dispose();
        stage.dispose();
    }
}
