package co.aeons.zombie.shooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import co.aeons.zombie.shooter.BasicScreen;
import co.aeons.zombie.shooter.GameScreen;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.Button;
import co.aeons.zombie.shooter.gameObjects.OptionButton;
import co.aeons.zombie.shooter.utils.*;
import co.aeons.zombie.shooter.utils.enums.DialogBoxState;
import co.aeons.zombie.shooter.utils.enums.GameState;

import java.awt.*;

public class OptionsScreen extends BasicScreen {

    private final SpaceGame game;

    //Variable para guardar el record del jugador en el modo arcade
    private String record;

    //Objetos interactuables de la pantalla
    private OptionButton music;
    private OptionButton effect;
    private Button back;
    private Button resetArcadePuntuation;
    private Button viewAchievements;
    private DialogBox menuResetDialog;

    //Representa el tiempo que dura el efecto visual de pulsado sobre una opción
    private float timeUntilExit;

    public OptionsScreen(final SpaceGame game) {
        this.game = game;

        //Avisamos al background manager de qué fondo queremos
        BackgroundManager.changeCurrentBackgrounds(BackgroundManager.BackgroundType.MENU);

        //Guardamos el record del usuario en la variable
        record = "record:    " + ArcadeScreen.obtainRecord() + " - ";

        //Creamos los botones para la pantalla de opciones
        music = new OptionButton("buttonMusic", "buttonMusicCancel",200, 265);
        effect = new OptionButton("buttonEffect", "buttonEffectCancel",260, 265);
        back = new Button("arrow_back", 750, 430, null, false);
        viewAchievements = new Button("button", 100, 100, "viewAchievements", true);
        resetArcadePuntuation = new Button("button", record.length() + 215, 180, "resetArcadePuntuation", true);
        //Cambiamos la escala botón para resetear la puntuación para hacerlo más adecuado al texto
        resetArcadePuntuation.setScale(0.6f,0.6f);

        //Creamos el cuadro de diálogo para borrar la puntuación del modo arcade
        menuResetDialog = new DialogBox("resetArcadePuntuationQuestion");

        /*Comprobamos el estado del volumen de la música para saber si es necesario cambiar el estado por defecto del
          botón*/
        if (AudioManager.getVolumeMusic()==0.0f){
            music.setDesactivated(true);
        }else {
            music.setDesactivated(false);
        }

        /*Comprobamos el estado del volumen de los efectos de sonido para saber si es necesario cambiar el estado por
        defecto del botón*/
        if (AudioManager.getVolumeEffect()==0.0f){
            effect.setDesactivated(true);
        }else {
            effect.setDesactivated(false);
        }

        //Inicializamos el timer de espera para el efecto en los botones
        timeUntilExit = 0.5f;

        //Cambiamos el valor de la variable que guarda el record, para poder utilizar el DrawText en el render
        record = record.substring(6,record.length());
    }

	@Override
	public void show() {

	}

	@Override
	public void mainRender(float delta) {
        //Pintamos el título de la pantalla
        FontManager.drawTitle("optionsTitle", 280, 420);

        //Pintamos las opciones para el audio del juego
        FontManager.drawText("audio",100,300);

        //Delegamos el render de los botones
        music.render();
        effect.render();
        back.render();
        viewAchievements.render();

        //Pintamos la puntuación del usuario en el modo arcade
        FontManager.drawText("record", record, 100, 211);

        //En función de si oculto o no, pintamos el cuadro de diálogo para borrar la puntuación del modo arcade
        if (menuResetDialog.getState().equals(DialogBoxState.HIDDEN))
            resetArcadePuntuation.render();
        else
            menuResetDialog.render();
	}

    public void update(float delta) {
        //Actualizamos los botones del menú de opciones
        back.update();
        resetArcadePuntuation.update();
        viewAchievements.update();

        //Si se ha tocado algún botón, lo marcamos como pulsado
        if (Gdx.input.justTouched()) {

            Vector3 v = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            v = SpaceGame.camera.unproject(v);

            if (music.press(v.x, v.y) || effect.press(v.x, v.y)) {
                //Reiniciamos el contador en caso de haberse pulsado un botón
                timeUntilExit=0.5f;
            }
        }

        //Si el contador es cero, comprobamos si hay algún botón pulsado y actuamos en consecuencia
        if (timeUntilExit <= 0) {
            /*Si el botón para la música ha sido pulsado y estaba desactivado, muteamos la música; en caso contario
              volvemos a poner el volumen en su estado natural*/
            if (music.isPressed() && music.isDesactivated()) {
                AudioManager.setVolumeMusic(0.0f);
            } else if (music.isPressed() && !music.isDesactivated()){
                AudioManager.setVolumeMusic(0.3f);
            }

            /*Si el botón para los efectos de sonido ha sido pulsado y estaba desactivado, muteamos los efectos de
              sonido; en caso contario volvemos a poner el volumen en su estado natural*/
            if (effect.isPressed() && effect.isDesactivated()) {
                AudioManager.setVolumeEffect(0.0f);
            } else if (effect.isPressed() && !effect.isDesactivated()){
                AudioManager.setVolumeEffect(0.7f);
            }

            //Si el botón de atrás es pulsado, volvermos al menú principal
            if (back.isPressed()){
               game.setScreen(new MainMenuScreen(game));
            }

            /*Si el botón de ver logros es pulsado, veremos la vista con los logros del usuario, volviendo al estado
              anterior para el botón*/
            if (viewAchievements.isPressed()){
                SpaceGame.googleServices.showAchievements();
                viewAchievements.setPressed(false);
            }
        } else {
            timeUntilExit -= delta;
        }

        //Realizamos las comprobaciones para tratar el cuadro de diálogo para borrar el record del modo arcade
        if (menuResetDialog.getState().equals(DialogBoxState.HIDDEN)) {
            if (resetArcadePuntuation.isPressed()){
                menuResetDialog.setStateToWaiting();
            }
        } else if (menuResetDialog.getState().equals(DialogBoxState.CONFIRMED)) {
            ArcadeScreen.resetRecord();
            this.game.setScreen(new OptionsScreen(game));
        } else if (menuResetDialog.getState().equals(DialogBoxState.CANCELLED)) {
            menuResetDialog.setStateToHidden();
            resetArcadePuntuation.setPressed(false);
        } else if (menuResetDialog.getState().equals(DialogBoxState.WAITING)) {
            menuResetDialog.update();
        }
    }

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
        music.dispose();
        effect.dispose();
        back.dispose();
        resetArcadePuntuation.dispose();
        menuResetDialog.dispose();
        viewAchievements.dispose();
	}
}
