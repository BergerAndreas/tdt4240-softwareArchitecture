package co.aeons.zombie.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Scanner;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.managers.Jukebox;
import co.aeons.zombie.shooter.managers.Save;

import static co.aeons.zombie.shooter.ZombieShooter.gamePort;
import static java.awt.Event.ENTER;


public class GameOverState extends GameState {
	
	private SpriteBatch sb;
	private Skin skin;

	private boolean newHighScore;
	TextField usernameTextField;
	private TextButton submitHighscoreButton;

	private BitmapFont font;
	private GlyphLayout layout;
	
	private Stage stage;
	private Texture bg;
	
	public GameOverState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {
		
		sb = new SpriteBatch();
		font = new BitmapFont();
		layout = new GlyphLayout();
		stage = new Stage(gamePort);
		skin = new Skin(Gdx.files.internal("skins/neutralizer-ui.json"));
		bg = new Texture(Gdx.files.internal("backgrounds/grasspath2.jpg"));
		newHighScore = Save.gd.isHighScore(Save.gd.getTentativeScore());

		// Control inputs
		Gdx.input.setInputProcessor(this.stage);

//		Check if new highscore, if so -> let user input name to highscore list
		if(newHighScore){
			String s = "New Highscore: " + Save.gd.getTentativeScore();
			font.getData().setScale(2,2);
			layout.setText(font, s);
			initHighscore();
		}else{
			//        Draw "Game Over" to screen
			String s = "Game Over";
			font.getData().setScale(2, 2);
			layout.setText(font, s);
			InitMenu();
		}
	}


    public void update(float dt) {

	}

    public void draw() {

        sb.setProjectionMatrix(ZombieShooter.cam.combined);
        sb.begin();

		sb.draw(bg, 0, 0, ZombieShooter.WIDTH, ZombieShooter.HEIGHT);
		font.draw(sb, layout, (ZombieShooter.WIDTH - layout.width) / 2, ZombieShooter.HEIGHT - 25);

        sb.end();
        stage.act();
        stage.draw();
    }

	private void InitMenu() {
        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();
        //Create buttons

        final TextButton restartButton = new TextButton("Restart", skin);
        TextButton highscoreButton = new TextButton("Highscores", skin);
        final TextButton QuitGameButton = new TextButton("Quit Game", skin);

        //Add listeners to buttons
//		Restart takes player to new game

		restartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
//				Stop gameover music, and start ingame music
				Jukebox.getGameoverMusic().stop();
				Jukebox.playIngameMusic();
				gsm.setState(GameStateManager.PLAY);
			}
		});

//		Highscore takes player to Highscores
		highscoreButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
//				Stop gameover music, and start ingame music
				Jukebox.getGameoverMusic().stop();
				Jukebox.playIngameMusic();
				gsm.setState(GameStateManager.HIGHSCORE);
			}
		});


//        Quit Game takes player to main menu screen
        QuitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
//				Stop gameover music, and start ingame music
				Jukebox.getGameoverMusic().stop();
				Jukebox.playIngameMusic();
				gsm.setState(GameStateManager.MENU);
			}
		});

        //Add buttons to table
        mainTable.add(restartButton).width(150).pad(5);
        mainTable.row();
        mainTable.add(highscoreButton).width(150).pad(5);
        mainTable.row();
        mainTable.add(QuitGameButton).width(150).pad(5);

        // Adds maintable to stage
        stage.addActor(mainTable);
    }

    private void initHighscore(){

//		Highscore input for player's name
		usernameTextField = new TextField("", skin);
		usernameTextField.setSize(100, 20);
		usernameTextField.setMaxLength(8); // Max chars
		usernameTextField.setAlignment(1); // Center text
		usernameTextField.setPosition((ZombieShooter.WIDTH - usernameTextField.getWidth())/2,(ZombieShooter.HEIGHT - usernameTextField.getHeight())/2);

//		Button to submit highscore
		submitHighscoreButton = new TextButton("Submit", skin);
		submitHighscoreButton.setPosition((ZombieShooter.WIDTH - submitHighscoreButton.getWidth())/2, usernameTextField.getY()-usernameTextField.getHeight()-25);

		//		submitHighscore takes player to Highscores
		submitHighscoreButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Save.gd.addHighScore(Save.gd.getTentativeScore(), usernameTextField.getText());
				Save.save();
//				Stop gameover music, and start ingame music
				Jukebox.getGameoverMusic().stop();
				Jukebox.playIngameMusic();
				gsm.setState(GameStateManager.HIGHSCORE);
			}
		});
		stage.addActor(usernameTextField);
		stage.addActor(submitHighscoreButton);
	}

    public void dispose() {
        sb.dispose();
        font.dispose();
        stage.dispose();
    }

}









