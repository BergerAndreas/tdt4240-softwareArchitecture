package co.aeons.zombie.shooter.managers;

import co.aeons.zombie.shooter.gamestates.DifficultyState;
import co.aeons.zombie.shooter.gamestates.GameOverState;
import co.aeons.zombie.shooter.gamestates.GameState;
import co.aeons.zombie.shooter.gamestates.HighscoreState;
import co.aeons.zombie.shooter.gamestates.MenuState;
import co.aeons.zombie.shooter.gamestates.MultiplayerMenuState;
import co.aeons.zombie.shooter.gamestates.MultiplayerGameState;
import co.aeons.zombie.shooter.gamestates.PlayState;
import co.aeons.zombie.shooter.gamestates.SplashState;

public class GameStateManager {

    // current game state
    private GameState gameState;

    public static final int MENU = 0;
    public static final int PLAY = 893746;
    public static final int HIGHSCORE = 3847;
    public static final int DIFFICULTY = 87239234;
    public static final int GAMEOVER = 928478;
    public static final int SPLASH = 3465283;
    public static final int MULTIPLAYERMENU = 1235125;
    public static final int MULTIPLAYERINVITE = 23847239;
    public static final int MULTIPLAYERQUICK = 238123239;
    public static final int MULTIPLAYERSEEINVITE = 231223239;

    public GameStateManager() {
        setState(SPLASH);
    }

    public void setState(int state) {
        if (gameState != null) gameState.dispose();
        if (state == SPLASH) {
            gameState = new SplashState(this);
        }
        if (state == MENU) {
            gameState = new MenuState(this);
        }
        if (state == PLAY) {
            gameState = new PlayState(this);
        }
        if (state == HIGHSCORE) {
            gameState = new HighscoreState(this);
        }
        if (state == DIFFICULTY) {
            gameState = new DifficultyState(this);
        }
        if (state == GAMEOVER) {
            gameState = new GameOverState(this);
        }
        if(state == MULTIPLAYERMENU){
            gameState = new MultiplayerMenuState(this);
        }
        if(state == MULTIPLAYERQUICK){
            gameState = new MultiplayerGameState(this,"QUICK");

        }
        if(state == MULTIPLAYERINVITE){
            gameState = new MultiplayerGameState(this,"INVITE");
        }
        if(state == MULTIPLAYERSEEINVITE){
            gameState = new MultiplayerGameState(this,"SEE_INVITATIONS");
        }
    }

    public void update(float dt) {
        gameState.update(dt);
    }

    public void draw() {
        gameState.draw();
    }

}











