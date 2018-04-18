package co.aeons.zombie.shooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import co.aeons.zombie.shooter.GameScreen;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.RivalShip;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.PlayerShip;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.powerUps.BurstPowerUp;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.powerUps.RegLifePowerUp;
import co.aeons.zombie.shooter.gameObjects.multiplayerMode.powerUps.ShieldPowerUp;
import co.aeons.zombie.shooter.utils.*;
import co.aeons.zombie.shooter.utils.enums.GameState;
import co.aeons.zombie.shooter.utils.enums.MultiplayerState;

public class MultiplayerScreen extends GameScreen{
    final SpaceGame game;

    // Nave del jugador
    public static PlayerShip playerShip;
    // Nave del rival
    public static RivalShip rivalShip;

    // Tiempo máximo para poder empezar la partida
    private final float MAX_TIME_TO_START_GAME = 5f;
    // Tiempo máximo para poder salir de la partida
    private final float MAX_TIME_TO_LEFT_GAME = 1f;

    // Estas variables solo se usan en caso de necesidad cuando tengamos que enviar
    // más de una vez una misma operación
    // Esto es debido a que enviamos los paquetes por UDP y la lógica del juego
    // corre más que la preparación y envio del mensaje.

    // Número de veces que vamos a enviar una misma operación
    private final int TIMES_TO_SEND_SAME_OPERATION = 5;
    // Número de veces que hemos enviado la operación de recibir daño (por mi parte, al jugador rival)
    private int times_sended_receive_damage_operation;

    // Mensaje de información para mostrar al usuario
    private String infoMessage;

    // Tiempos -> de comienzo y de salida
    private float timeToStartGame;
    private float timeToLeftGame;

    // PowerUps del jugador
    public static BurstPowerUp playerBurstPowerUp;
    private static RegLifePowerUp playerRegLifePowerUp;
    private static ShieldPowerUp playerShieldPowerUp;

    // PowerUps del rival
    public static BurstPowerUp rivalBurstPowerUp;
    private static RegLifePowerUp rivalRegLifePowerUp;
    private static ShieldPowerUp rivalShieldPoweUp;

    // Sabremos si el jugador abandonó la partida
    private boolean abandonPlayer;
    // Sabremos si el rival abandonó la partida
    private boolean abandonRival;

    // Mensajes de entrada y de salida del juego
    private MultiplayerMessage outcomeMessage;
    private MultiplayerMessage incomeMessage;

    // Comprobaremos si el jugador quiere abandonar o no la habitación
    private boolean leaveRoom;

    public MultiplayerScreen(final SpaceGame game, String option){
        this.game = game;

        //Avisamos al background manager de qué fondo queremos
        BackgroundManager.changeCurrentBackgrounds(BackgroundManager.BackgroundType.MULTIPLAYER);

        outcomeMessage  = new MultiplayerMessage();
        incomeMessage = new MultiplayerMessage();

        state = GameState.READY;

        leaveRoom = false;

        times_sended_receive_damage_operation = 0;

        infoMessage = FontManager.getFromBundle("connectServer");

        timeToStartGame = MAX_TIME_TO_START_GAME;
        timeToLeftGame = MAX_TIME_TO_LEFT_GAME;

        playerShip  = new PlayerShip();
        rivalShip = new RivalShip();

        abandonRival = false;
        abandonPlayer = false;

        playerBurstPowerUp = new BurstPowerUp("burstPlayer", SpaceGame.width/3 - 25, 5);
        playerRegLifePowerUp = new RegLifePowerUp("regLifePlayer", SpaceGame.width/2 - 25, 5);
        playerShieldPowerUp = new ShieldPowerUp("shieldPlayer", (SpaceGame.width*2)/3 - 25, 5);

        rivalBurstPowerUp = new BurstPowerUp("burstEnemy",SpaceGame.width/3 - 25,SpaceGame.height - 55);
        rivalRegLifePowerUp = new RegLifePowerUp("regLifeEnemy",SpaceGame.width/2 - 25,SpaceGame.height-55);
        rivalShieldPoweUp = new ShieldPowerUp("shieldEnemy",(SpaceGame.width*2)/3 - 25,SpaceGame.height-55);

        CollisionsManager.load();
        ShootsManager.load();
        CameraManager.loadShakeEffect(1f, CameraManager.NORMAL_SHAKE);

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);

        if(option.equals("QUICK"))
            SpaceGame.googleServices.startQuickGame();
        else if(option.equals("INVITE"))
            SpaceGame.googleServices.invitePlayer();
        else
            SpaceGame.googleServices.seeMyInvitations();
    }

    @Override
    public void renderEveryState(float delta) {
    }

    @Override
    public void updateEveryState(float delta) {
    }

    @Override
    public void renderReady(float delta) {
        if(SpaceGame.googleServices.getMultiplayerState().equals(MultiplayerState.STARTMULTIPLAYER))
            FontManager.text.draw(SpaceGame.batch,infoMessage,SpaceGame.width/3,SpaceGame.height/2);
    }

    @Override
    public void updateReady(float delta) {
        // Lógica de espera para empezar la partida
        switch (SpaceGame.googleServices.getMultiplayerState()){
            case STARTMULTIPLAYER:
                if(timeToStartGame > 0){
                    // Informaremos al jugador cuanto tiempo queda para empezar la partida
                    infoMessage = FontManager.getFromBundle("startGame")+"  "+(int)timeToStartGame;
                    timeToStartGame-=delta;
                }else {
                    // En el momento que se cumpla el periodo de tiempo, podremos empezar la partida
                    timeToStartGame = 0;
                    state = GameState.START;
                }
                break;
            case CANCEL:
                ScreenManager.changeScreen(game,MultiplayerMenuScreen.class);
                break;
        }
    }

    @Override
    public void renderStart(float delta) {
        playerShip.render();
        rivalShip.render();

        ShootsManager.render();

        playerBurstPowerUp.render();
        playerRegLifePowerUp.render();
        playerShieldPowerUp.render();

        rivalBurstPowerUp.render();
        rivalRegLifePowerUp.render();
        rivalShieldPoweUp.render();
    }

    @Override
    public void updateStart(float delta) {

        CollisionsManager.update();
        ShootsManager.update(delta, playerShip);
        CameraManager.update(delta);

        // Actualizaremos la lógica por parte de la entrada del mensaje
        updateIncomeMessage(delta);
        // Actaulizaremos la lógica por parte de la salida del mensaje
        updateOutcomeMessage(delta);

        // En esta sección actualizamos la lógica de los powerUps
        if(playerBurstPowerUp.isTouched())
            playerBurstPowerUp.act(delta, playerShip);

        if(playerRegLifePowerUp.isTouched())
            playerRegLifePowerUp.act(delta, playerShip);

        if(playerShieldPowerUp.isTouched())
            playerShieldPowerUp.act(delta, playerShip);

        if(rivalBurstPowerUp.isTouched())
            rivalBurstPowerUp.act(delta, rivalShip);

        if(rivalRegLifePowerUp.isTouched())
            rivalRegLifePowerUp.act(delta, rivalShip);

        if(rivalShieldPoweUp.isTouched())
            rivalShieldPoweUp.act(delta, rivalShip);
    }

    private void updateIncomeMessage(float delta){
        // Obtenemos el mensaje de entrada
        incomeMessage = SpaceGame.googleServices.receiveGameMessage();

        // Comprobamos si el rival nos ha enviado una petición de salida del juego
        if (incomeMessage.checkOperation(incomeMessage.MASK_LEAVE)) {
            // En cuyo caso habremos ganado y si la nave rival no ha sido derrotada
            // marcamos como que ha sido un abandono del rival
            if (!rivalShip.isDefeated())
                abandonRival = true;
            state = GameState.WIN;
        }
        // Petición recibida de disparo
        if (incomeMessage.checkOperation(incomeMessage.MASK_SHOOT))
            rivalShip.shoot();
        // Petición recibida de powerUp Burst usado
        if (incomeMessage.checkOperation(incomeMessage.MASK_BURST))
            rivalBurstPowerUp.setTouched();
        // Petición recibida de powerUp Regeneración de Vida usado
        if (incomeMessage.checkOperation(incomeMessage.MASK_REG_LIFE))
            rivalRegLifePowerUp.setTouched();
        // Petición recibida de powerUp Escudo
        if (incomeMessage.checkOperation(incomeMessage.MASK_SHIELD))
            rivalShieldPoweUp.setTouched();
        // Petición recibida de recepción de daño
        if(incomeMessage.checkOperation(incomeMessage.MASK_HAS_RECEIVE_DAMAGE)){
            rivalShip.receiveDamage();
        }

        // Si la nave rival ha sido completamente derrotada
        // Habremos ganado la partida
        if(rivalShip.isCompletelyDefeated()){
            state = GameState.WIN;
        }

        // Actualizamos la lógica del rival
        rivalShip.update(delta,incomeMessage.getPositionY());

        // Reseteamos las operaciones para no interferir en la siguiente iteración
        incomeMessage.resetOperations();
    }

    private void updateOutcomeMessage(float delta){

        // Vamos a construir el mensaje de salir
        // Como vemos es muy parecido en principio al usado en el modo campaña

        Vector3 coordinates = TouchManager.getAnyXTouchLowerThan(playerShip.getX() + playerShip.getWidth());

        boolean canShipMove = false;
        if(!coordinates.equals(Vector3.Zero))
            canShipMove = true;

        playerShip.update(delta, coordinates.y , canShipMove);
        // Ubicamos la posición de nuestra nave en la salida del mensaje
        outcomeMessage.setPositionY(playerShip.getCenter().y);

        coordinates = TouchManager.getAnyXTouchGreaterThan(playerShip.getX() + playerShip.getWidth());

        if(!coordinates.equals(Vector3.Zero))

            if(playerBurstPowerUp.isOverlapingWith(coordinates.x,coordinates.y)){
                if(!playerBurstPowerUp.isTouched())
                    playerBurstPowerUp.setTouched();
                // Ubicamos la petición de haber usado el powerUp Burst
                outcomeMessage.setOperation(outcomeMessage.MASK_BURST);
            }
            else if(playerRegLifePowerUp.isOverlapingWith(coordinates.x,coordinates.y)){
                if(!playerRegLifePowerUp.isTouched())
                    playerRegLifePowerUp.setTouched();
                // Ubicamos la petición de haber usado el powerUp Regeneración de Vida
                outcomeMessage.setOperation(outcomeMessage.MASK_REG_LIFE);
            }
            else if(playerShieldPowerUp.isOverlapingWith(coordinates.x,coordinates.y)){
                if(!playerShieldPowerUp.isTouched())
                    playerShieldPowerUp.setTouched();
                // Ubicamos la petición de haber usado el powerUp Regeneración de Vida
                outcomeMessage.setOperation(outcomeMessage.MASK_SHIELD);
            }
            else{
                playerShip.shoot();
                // Ubicamos la petición de disparo
                outcomeMessage.setOperation(outcomeMessage.MASK_SHOOT);
            }

        // Si la nave ha sido dañada Ó si ha sido derrotada, ubicamos la petición de recepción de daño
        // Este envío se realiza unas veces para asegurarse de que este mensaje llegue correctamente a su destino
        if(playerShip.isUndamagable() || playerShip.isDefeated()){
            if(times_sended_receive_damage_operation <= TIMES_TO_SEND_SAME_OPERATION){
                outcomeMessage.setOperation(outcomeMessage.MASK_HAS_RECEIVE_DAMAGE);
                times_sended_receive_damage_operation++;
            }
        }else{
            times_sended_receive_damage_operation = 0;
        }

        // Si el jugador desea abandonar la partida
        // Ubicamos la petición de salida de partida
        if(leaveRoom){
            if(!playerShip.isDefeated())
                abandonPlayer = true;
            outcomeMessage.setOperation(outcomeMessage.MASK_LEAVE);
            state = GameState.LOSE;
        }

        // Si nuestra nave ha sido completamente derrotada
        // habremos perdido la partida
        if(playerShip.isCompletelyDefeated()){
            state = GameState.LOSE;
        }

        // Finalmente enviamos el mensaje
        SpaceGame.googleServices.sendGameMessage(outcomeMessage.getForSendMessage());

        // Reseteamos las operaciones para no interferir en la siguiente interación
        outcomeMessage.resetOperations();
    }

    @Override
    public void renderPause(float delta) {

    }

    @Override
    public void updatePause(float delta) {

    }

    @Override
    public void renderWin(float delta) {
        if(abandonRival)
            FontManager.draw(FontManager.getFromBundle("multiplayerGameEnemyAbandon"),SpaceGame.height/2 + 50);
        FontManager.draw(FontManager.getFromBundle("multiplayerGameWon"),SpaceGame.height/2);

        if(timeToLeftGame <= 0)
            FontManager.draw(FontManager.getFromBundle("multiplayerGameExit"),SpaceGame.height/2 - 50);
    }

    @Override
    public void updateWin(float delta) {
        if(timeToLeftGame > 0){
            timeToLeftGame-=delta;
        }else {
            if(Gdx.input.justTouched()){
                SpaceGame.googleServices.leaveRoom();
                ScreenManager.changeScreen(game, MainMenuScreen.class);
            }
        }
    }

    @Override
    public void renderLose(float delta) {
        if(abandonPlayer)
            FontManager.draw(FontManager.getFromBundle("multiplayerGamePlayerAbandon"),SpaceGame.height/2 - 50);
        FontManager.draw(FontManager.getFromBundle("multiplayerGameLoose"),SpaceGame.height/2);

        if(timeToLeftGame <= 0)
            FontManager.draw(FontManager.getFromBundle("multiplayerGameExit"),SpaceGame.height/2 + 50);
    }

    @Override
    public void updateLose(float delta) {
        if(timeToLeftGame > 0){
            timeToLeftGame-=delta;
        }else {
            if(Gdx.input.justTouched()){
                SpaceGame.googleServices.leaveRoom();
                ScreenManager.changeScreen(game, MainMenuScreen.class);
            }
        }
    }

    @Override
    public void disposeScreen() {
        playerShip.dispose();
        rivalShip.dispose();
        for(Shoot shoot: ShootsManager.shoots){
            shoot.dispose();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            leaveRoom = true;
        }
        return false;
    }
}
