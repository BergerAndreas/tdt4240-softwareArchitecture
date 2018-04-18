package co.aeons.zombie.shooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import co.aeons.zombie.shooter.GameScreen;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.*;
import co.aeons.zombie.shooter.gameObjects.campaignMode.CampaignShip;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Inventary;
import co.aeons.zombie.shooter.gameObjects.Shoot;
import co.aeons.zombie.shooter.utils.*;
import co.aeons.zombie.shooter.utils.enums.DialogBoxState;
import co.aeons.zombie.shooter.utils.enums.GameState;

public class CampaignScreen extends GameScreen {

    private final SpaceGame game;

    //Objetos interactuables de la pantalla
    public static CampaignShip ship;
    private Inventary inventary;
    private DialogBox menuExitDialog;
    private Button exit;

    // Vamos a controlar que touch está disparando y cual está controlando la nave
    // Seguiremos el siguiente modelo:
    // -1 para ningún touch asignado
    //  0 para el primer touch
    //  1 para el segundo touch
    public static int whichTouchIsShooting;
    public static int whichControlsTheShip;

    public CampaignScreen(SpaceGame game, String scriptLevel){
        this.game = game;

        ShootsManager.load();
        CollisionsManager.load();
        EnemiesManager.load(scriptLevel);
        DamageManager.load();
        CameraManager.loadShakeEffect(1f,CameraManager.NORMAL_SHAKE);

        //Avisamos al background manager de qué fondo queremos
        BackgroundManager.changeCurrentBackgrounds(BackgroundManager.BackgroundType.CAMPAIGN);

        state = GameState.READY;

        //Creamos los objetos de juego
        ship = new CampaignShip();
        inventary = new Inventary();

        exit = new Button("buttonExit", 750, 430, null,true);

        menuExitDialog = new DialogBox("exitModeQuestion");

        whichTouchIsShooting = -1;
        whichControlsTheShip = -1;

        //Preparamos un listener que si se desliza el dedo a la derecha se abre el inventario
        Gdx.input.setInputProcessor(new SimpleDirectionGestureDetector(new SimpleDirectionGestureDetector.DirectionListener() {

            public void onRight() {
                if (state.equals(GameState.START)) {
                    inventary.restart();
                    state = GameState.PAUSE;
                    AudioManager.pauseMusic();
                    AudioManager.playSound("inventary");
                }
            }

            public void onLeft() {
                if (state.equals(GameState.PAUSE) && !inventary.isClosing()) {
                    inventary.setIsClosing(true);
                    AudioManager.playSound("inventary");
                }
            }

            public void onDown() {
            }

            public void onUp() {
            }

        }));
    }

    @Override
    public void renderEveryState(float delta) { }

    @Override
    public void updateEveryState(float delta) { }

    @Override
    public void renderPause(float delta) {
        inventary.render();
        ship.render();

        if (menuExitDialog.getState().equals(DialogBoxState.HIDDEN))
            exit.render();
        else
            menuExitDialog.render();
    }

    @Override
    public void updatePause(float delta) {

        if (menuExitDialog.getState().equals(DialogBoxState.HIDDEN)) {
            //Se hará una cosa u otra si el inventario está cerrándose o no
            if (inventary.isClosing()) {
                inventary.updateClosing(delta, ship);

                //Si el inventario ya no está cerrándose, volvemos a la partida
                if (!inventary.isClosing()) {
                    state = GameState.START;
                }
            } else {
                inventary.update(delta, ship);
                exit.update();
                if (exit.isPressed())
                    menuExitDialog.setStateToWaiting();
            }
        } else if (menuExitDialog.getState().equals(DialogBoxState.CONFIRMED)) {
            ScreenManager.changeScreen(game, MainMenuScreen.class);
            disposeScreen();
        } else if (menuExitDialog.getState().equals(DialogBoxState.CANCELLED)) {
            menuExitDialog.setStateToHidden();
            exit.setPressed(false);
        } else if (menuExitDialog.getState().equals(DialogBoxState.WAITING)) {
            menuExitDialog.update();
        }
    }

    @Override
    public void renderReady(float delta) {
        FontManager.drawText("tapToStart",370,240);

        if (Gdx.input.justTouched()) {
            state = GameState.START;
        }
    }

    @Override
    public void updateReady(float delta) {

    }

    @Override
    public void renderStart(float delta) {
        ship.render();
        EnemiesManager.render();
        ShootsManager.render();
    }

    @Override
    public void updateStart(float delta) {
        CameraManager.update(delta);

        //Comprobamos si se ha perdido o ganado el juego
        if (ship.isDefeated())
            state = GameState.LOSE;
        if(EnemiesManager.noMoreEnemiesToGenerateOrToDefeat()) {
            state = GameState.WIN;
        }

        // Controlamos si algún touch ya ha dejado de ser pulsado
        if((whichControlsTheShip == 0 && !TouchManager.isFirstTouchActive()) || (whichControlsTheShip == 1 && !TouchManager.isSecondTouchActive()))
            whichControlsTheShip = -1;

        if((whichTouchIsShooting == 0 && !TouchManager.isFirstTouchActive()) || (whichTouchIsShooting == 1 && !TouchManager.isSecondTouchActive()))
            whichTouchIsShooting = -1;

        // Obtenemos un vector de coordenadas si está en la zona de movimiento de la nave
        Vector3 coordinates = TouchManager.getAnyXTouchLowerThan(ship.getX() + ship.getWidth());

        // A priori la nave no puede moverse
        boolean canShipMove = false;
        // Obtenemos a priori un touch que pudiera controlar la nave
        int whoCouldControlTheShip = TouchManager.assignWhichTouchCorresponds(coordinates);

        // Controlamos si la nave puede moverse, esto es:
        // Si las coordenadas pertenecen a algún touch
        // Si es la primera vez que los asignadores estan en el estado inicial
        // Si el posible controlador de la nave no es el mismo touch que uno que esté disparando
        if(coordinates.y != 0 && (whoCouldControlTheShip != whichTouchIsShooting || whoCouldControlTheShip == -1 && whichTouchIsShooting == -1)) {
            canShipMove = true;
            whichControlsTheShip = TouchManager.assignWhichTouchCorresponds(coordinates);
        }
        // Actualizamos la nave pasando la posible coordenada de movimiento y el resultado
        // de preguntar la condicion de movimiento
        ship.update(delta, coordinates.y, canShipMove);

        // Si tocamos la pantalla disparamos
        // Obtenemos un vector de coordenadas. Este vector puede ser cualquier touch que cumpla
        // con la condición de que la posición X sea superior a la dada
        coordinates = TouchManager.getAnyXTouchGreaterThan(ship.getX() + ship.getWidth());
        // Preguntamos si el vector de coordenadas no es un vector de 0's. Si lo fuese es que el jugador
        // no ha tocado la pantalla. Además preguntamos si el toque ha sido solo de una sola vez
        if(!coordinates.equals(Vector3.Zero) && Gdx.input.justTouched() && whichTouchIsShooting == -1) {
            // Disparamos, pasando por parámetro las coordenadas del touch correspondiente
            ship.shoot(coordinates.x, coordinates.y);
            whichTouchIsShooting = TouchManager.assignWhichTouchCorresponds(coordinates);
        }

        //Realizamos la lógica de los objetos en juego
        CollisionsManager.update();
        EnemiesManager.update(delta);
        ShootsManager.update(delta, ship);
    }

    @Override
    public void renderWin(float delta) {
        if(ship.getX() > SpaceGame.width)
            FontManager.drawText("winGame",240,240);
        else
            ship.render();
    }

    @Override
    public void updateWin(float delta) {
        if(ship.getX() > SpaceGame.width){
            if (TouchManager.isTouchedAnyToucher()) {
                ScreenManager.changeScreen(game, DemoMenuScreen.class);
                disposeScreen();
            }
        }else{
            ship.setX(ship.getX() + CampaignShip.SPEED*delta*3);
            ship.update(delta,ship.getY(),false);
        }
    }

    @Override
    public void renderLose(float delta) {
        FontManager.drawText("gameOver",370,240);
        ship.render();
    }

    @Override
    public void updateLose(float delta) {
        if (TouchManager.isTouchedAnyToucher() && ship.destroyEffect.isComplete()) {
            ScreenManager.changeScreen(game, DemoMenuScreen.class);
            disposeScreen();
        }
        ship.update(delta,ship.getY(),false);
    }

    @Override
    public void disposeScreen() {
        ship.dispose();
        EnemiesManager.dispose();
        for(Shoot shoot : ShootsManager.shoots)
            shoot.dispose();
        inventary.dispose();
        menuExitDialog.dispose();
        Gdx.input.setInputProcessor(null);
        super.dispose();
    }
}