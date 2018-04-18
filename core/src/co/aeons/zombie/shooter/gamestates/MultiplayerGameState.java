package co.aeons.zombie.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.entities.multiplayerMode.FirstPlayer;
import co.aeons.zombie.shooter.entities.multiplayerMode.SecondPlayer;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.utils.MultiplayerMessage;
import co.aeons.zombie.shooter.utils.enums.MultiplayerState;

import static co.aeons.zombie.shooter.ZombieShooter.cam;
import static co.aeons.zombie.shooter.utils.enums.MultiplayerState.STARTMULTIPLAYER;

public class MultiplayerGameState extends GameState implements InputProcessor {
    private SpriteBatch sb;
    // The first player
    public static FirstPlayer firstPlayer;
    // The second (online) player
    public static SecondPlayer secondPlayer;

    //Max time before the game will start
    private final float MAX_TIME_TO_START_GAME = 5f;
    // Max time to leave the game before it'll start
    private final float MAX_TIME_TO_LEFT_GAME = 1f;

    //These variables are only used if necessary if we need to send the same operation
    //more than once since we're sending over UDP

    // Numer of times to retry sending the same operation
    private final int TIMES_TO_SEND_SAME_OPERATION = 5;

    // Number of times we have sent the operation
    //TODO: change variable name to fit with our game
    private int times_sended_receive_damage_operation;

    //Information message to show to the user
    private BitmapFont infoMessage;
    private GlyphLayout layout;


    //Times start -> finish
    private float timeToStartGame;
    private float timeToLeftGame;

    // FirstPlayer powerups
    //TODO: change these to fit with our powerups
    /*
    public static BurstPowerUp playerBurstPowerUp;
    private static RegLifePowerUp playerRegLifePowerUp;
    private static ShieldPowerUp playerShieldPowerUp;

    //SecondPlayer powerups
    public static BurstPowerUp rivalBurstPowerUp;
    private static RegLifePowerUp rivalRegLifePowerUp;
    private static ShieldPowerUp rivalShieldPoweUp;
    */

    // To know if the FirstPlayer left the game
    private boolean abandonFirstPlayer;

    // To know if the SecondPlayer left the game
    private boolean abandonSecondPlayer;

    //Game input and output messages
    private MultiplayerMessage outcomeMessage;
    private MultiplayerMessage incomeMessage;

    //Check if the player wants to leave the room or not
    private boolean leaveRoom;

    public MultiplayerGameState(GameStateManager gsm, String option) {
        super(gsm);

        //TODO: Initialize background here

        sb = new SpriteBatch();
        sb.setProjectionMatrix(cam.combined);


        outcomeMessage = new MultiplayerMessage();
        incomeMessage = new MultiplayerMessage();

        //state = GameState.READY;

        leaveRoom = false;

        times_sended_receive_damage_operation = 0;

        //Show infomessage
        infoMessage = new BitmapFont();
        layout = new GlyphLayout();

        timeToStartGame = MAX_TIME_TO_START_GAME;
        timeToLeftGame = MAX_TIME_TO_LEFT_GAME;

        firstPlayer = new FirstPlayer();
        secondPlayer = new SecondPlayer();

        abandonFirstPlayer = false;
        abandonSecondPlayer = false;

        //TODO: Initialize powerups

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);


        if (option.equals("QUICK"))
            ZombieShooter.googleServices.startQuickGame();
        else if (option.equals("INVITE"))
            ZombieShooter.googleServices.invitePlayer();
        else
            ZombieShooter.googleServices.seeMyInvitations();
    }

    @Override
    public void init() {


    }

    @Override
    public void update(float dt) {
        String s = "Connecting to server";
        if (ZombieShooter.googleServices.getMultiplayerState().equals(STARTMULTIPLAYER)) {
            sb.begin();
            infoMessage.getData().setScale(2, 2);
            layout.setText(infoMessage, s);
            float width = layout.width;
            infoMessage.draw(sb, s, (cam.viewportWidth - width) / 2, cam.viewportHeight - 25);
            sb.end();
        }
        updateReady(dt);
        //FontManager.text.draw(SpaceGame.batch,infoMessage,SpaceGame.width/3,SpaceGame.height/2);

    }

    public void updateReady(float dt) {
        switch (ZombieShooter.googleServices.getMultiplayerState()) {
            case STARTMULTIPLAYER:
                if (timeToStartGame > 0) {
                    String s = "startGame" + (int) timeToStartGame;
                    // Informaremos al jugador cuanto tiempo queda para empezar la partida
                    sb.begin();
                    infoMessage.getData().setScale(2, 2);
                    layout.setText(infoMessage, s);
                    float width = layout.width;
                    infoMessage.draw(sb, s, (cam.viewportWidth - width) / 2, cam.viewportHeight - 25);
                    sb.end();
                    timeToStartGame -= dt;
                } else {
                    // En el momento que se cumpla el periodo de tiempo, podremos empezar la partida
                    timeToStartGame = 0;
                    //state = GameState.START;
                }
                break;
            case CANCEL:
                gsm.setState(GameStateManager.MENU);
                break;
        }

        updateStart(dt);
    }

    public void updateStart(float dt) {
        updateIncomeMessage(dt);
        updateOutComeMessage(dt);

        if (timeToLeftGame > 0) {
            timeToLeftGame -= dt;
        } else {
            if (Gdx.input.justTouched()) {
                ZombieShooter.googleServices.leaveRoom();
                gsm.setState(GameStateManager.MENU);
            }
        }

        renderLose(dt);

        //Act here
    }

    public void updateIncomeMessage(float dt) {
        //Fetch the input message
        incomeMessage = ZombieShooter.googleServices.receiveGameMessage();
        //Check if opponent has requested to leave the room
        if (incomeMessage.checkOperation(incomeMessage.MASK_LEAVE)) {
            abandonSecondPlayer = true;
            //TODO: exit game or something here?
        }
        // Petición recibida de disparo
        if (incomeMessage.checkOperation(incomeMessage.MASK_SHOOT))
            System.out.println("Shoot");
        //rivalShip.shoot();
        // Petición recibida de powerUp Burst usado
        if (incomeMessage.checkOperation(incomeMessage.MASK_BURST))
            System.out.println("burst");
        //rivalBurstPowerUp.setTouched();
        // Petición recibida de powerUp Regeneración de Vida usado
        if (incomeMessage.checkOperation(incomeMessage.MASK_REG_LIFE))
            System.out.println("life powerup");
        //rivalRegLifePowerUp.setTouched();
        // Petición recibida de powerUp Escudo
        if (incomeMessage.checkOperation(incomeMessage.MASK_SHIELD))
            System.out.println("Shield powerup");
        //rivalShieldPoweUp.setTouched();
        // Petición recibida de recepción de daño
        if (incomeMessage.checkOperation(incomeMessage.MASK_HAS_RECEIVE_DAMAGE)) {
            System.out.println("Damage received");
            //rivalShip.receiveDamage();
        }
        /*
        TODO:Fix win state
        if(rivalShip.isCompletelyDefeated()){
            state = GameState.WIN;
        }
        */
        //Update logic of the rival
        //rivalShip.update(delta,incomeMessage.getPositionY());
        System.out.println("getpositiony: " + incomeMessage.getPositionY());

        // Reset for next update
        incomeMessage.resetOperations();


    }

    public void updateOutComeMessage(float dt) {
        //Update outcome message
        Vector3 coordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        outcomeMessage.setPositionY(Gdx.input.getY());

        //Finally we send the message

        ZombieShooter.googleServices.sendGameMessage(outcomeMessage.getForSendMessage());

        //We reset the operations so as not to interfere in the next interaction
        outcomeMessage.resetOperations();


    }

    public void renderLose(float dt) {
        if (abandonFirstPlayer) {

            System.out.println("multiplayergameabandon");
        }
        //FontManager.draw(FontManager.getFromBundle("multiplayerGamePlayerAbandon"), SpaceGame.height / 2 - 50);
        System.out.println("multiplayergamelose");
        //FontManager.draw(FontManager.getFromBundle("multiplayerGameLoose"), SpaceGame.height / 2);

        if (timeToLeftGame <= 0) {
            System.out.println("multiplayergameext");
            //FontManager.draw(FontManager.getFromBundle("multiplayerGameExit"), SpaceGame.height / 2 + 50);
        }
    }

    @Override
    public void draw() {

    }

    @Override
    public void handleInput() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            leaveRoom = true;
        }
        return false;
    }


    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
