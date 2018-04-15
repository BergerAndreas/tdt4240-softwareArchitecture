package co.aeons.zombie.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.GameKeys;
import co.aeons.zombie.shooter.managers.GameStateManager;

import static co.aeons.zombie.shooter.ZombieShooter.gamePort;


public class GameOverState extends GameState {

    private SpriteBatch sb;
    private ShapeRenderer sr;
    private Skin skin;

    private boolean newHighScore;
    private char[] newName;
    private int currentChar;

    private BitmapFont font;
    private GlyphLayout layout;

    private Stage stage;

    public GameOverState(GameStateManager gsm) {
        super(gsm);
    }

    public void init() {

        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        font = new BitmapFont();
        layout = new GlyphLayout();
        stage = new Stage(gamePort);
        skin = new Skin(Gdx.files.internal("skins/neutralizer-ui.json"));
        System.out.println("GAMEOVER");

        // Control inputs
        Gdx.input.setInputProcessor(this.stage);
        InitMenu();

    }

    public void update(float dt) {
        handleInput();
    }

    public void draw() {

        sb.setProjectionMatrix(ZombieShooter.cam.combined);

        sb.begin();

        String s = "Game Over";
        font.getData().setScale(2, 2);
        layout.setText(font, s);
        float width = layout.width;

//        Draw on screen
        font.draw(sb, s, (ZombieShooter.WIDTH - width) / 2, ZombieShooter.HEIGHT - 25);

        sb.end();

        stage.act();
        stage.draw();

    }

    public void handleInput() {

        if (GameKeys.isPressed(GameKeys.ENTER)) {

            gsm.setState(GameStateManager.MENU);
        }

        if (GameKeys.isPressed(GameKeys.UP)) {
            if (newName[currentChar] == ' ') {
                newName[currentChar] = 'Z';
            } else {
                newName[currentChar]--;
                if (newName[currentChar] < 'A') {
                    newName[currentChar] = ' ';
                }
            }
        }

        if (GameKeys.isPressed(GameKeys.DOWN)) {
            if (newName[currentChar] == ' ') {
                newName[currentChar] = 'A';
            } else {
                newName[currentChar]++;
                if (newName[currentChar] > 'Z') {
                    newName[currentChar] = ' ';
                }
            }
        }

        if (GameKeys.isPressed(GameKeys.RIGHT)) {
            if (currentChar < newName.length - 1) {
                currentChar++;
            }
        }

        if (GameKeys.isPressed(GameKeys.LEFT)) {
            if (currentChar > 0) {
                currentChar--;
            }
        }

    }

    private void InitMenu() {
        //Add stuff here
        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();
        //Create buttons

        TextButton restartButton = new TextButton("Restart", skin);
        TextButton highscoreButton = new TextButton("Highscores", skin);
        TextButton QuitGameButton = new TextButton("Quit Game", skin);

        //Add listeners to buttons
//		Restart takes player to new game
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                TODO change state to whatever the state was prior to "restart press"
//				gsm.resetPlayScreen();
                gsm.setState(GameStateManager.PLAY);
            }
        });

//		Highscore takes player to Highscores
        highscoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//                TODO change state to whatever the state was prior to "restart press"
//				gsm.resetPlayScreen();
                gsm.setState(GameStateManager.HIGHSCORE);
            }
        });

//        Quit Game takes player to main menu screen
        QuitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//				gsm.resetPlayScreen();
                gsm.setState(GameStateManager.MENU);
            }
        });


        //Add buttons to table
        mainTable.add(restartButton);
        mainTable.row();
        mainTable.add(highscoreButton);
        mainTable.row();
        mainTable.add(QuitGameButton);

        // Adds maintable to stage
        stage.addActor(mainTable);
    }

    public void dispose() {
        sb.dispose();
        sr.dispose();
    }

}









