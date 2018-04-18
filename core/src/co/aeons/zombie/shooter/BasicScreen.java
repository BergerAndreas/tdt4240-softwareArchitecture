package co.aeons.zombie.shooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import co.aeons.zombie.shooter.utils.AudioManager;
import co.aeons.zombie.shooter.utils.BackgroundManager;
import co.aeons.zombie.shooter.utils.FontManager;

public abstract class BasicScreen implements Screen, InputProcessor {

    // Indica si está activada la atenuación
    private static boolean attenuation;

    // Almacenará el valor de alpha que irá aumentando para la atenuación
    private static float alpha;

    // Indicará el valor con el que incrementará alpha
    private static float attenuation_speed;

    @Override
    public void render(float delta) {
        // Limpiamos la pantalla con un color negro no transparente
        Gdx.gl.glClearColor(0, 0, 0, 1);
        // Además limpiamos el buffer de la pantalla
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Actualizamos la camara y actualizamos la matriz de proyección del sprite batch
        SpaceGame.camera.update();
        SpaceGame.batch.setProjectionMatrix(SpaceGame.camera.combined);

        // Iniciamos el proceso de renderización del spritebatch
        SpaceGame.batch.begin();

        // Pintamos fondo y juego, con atenuación o no dependiendo de si está activada
        if (attenuation) {

            // En el momento en que alpha supere 1, paramos la atenuación
            if (alpha >= 1) {
                alpha = 1;
                attenuation = false;
            }

            // Realizamos finalmente la atenuación
            Color c = SpaceGame.batch.getColor();
            float oldAlpha = c.a;

            c.a = alpha;
            SpaceGame.batch.setColor(c);
            FontManager.setAlpha(alpha);

            BackgroundManager.render();
            this.mainRender(delta);

            c.a = oldAlpha;
            SpaceGame.batch.setColor(c);
            FontManager.setAlpha(oldAlpha);

            // Actualizamos alpha para la siguiente iteración
            alpha += delta * attenuation_speed;

            // Actualizamos el incremento para que aumente exponencialmente
            attenuation_speed *= 1.05;
        } else {
            // Si no tenemos atenuación, pintamos normal
            BackgroundManager.render();
            this.mainRender(delta);
        }

        // Terminamos el proceso de renderizado
        SpaceGame.batch.end();

        // Actualizamos los fondos y el resto del juego
        BackgroundManager.update(delta);
        this.update(delta);

        // Actualizamos el audio
        AudioManager.update();
    }

    // Se llama cuando se desea activar una atenuación a la hora de pintarse
    public static void activeInitialAttenuation() {
        attenuation = true;
        alpha = 0.0f;
        attenuation_speed = 0.01f;
    }

    // Deberá ser sobreescrito por la clase hija para indicar lo que se debe pintar en pantalla
    protected void mainRender(float delta) {}

    // Deberá ser sobreescrito por la clase hija para actualizar su estado
    protected void update(float delta) {}

    @Override
    public boolean keyDown(int keycode) {
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
    @Override
    public void show() { }
    @Override
    public void resize(int width, int height) { }
    @Override
    public void pause() { }
    @Override
    public void resume() { }
    @Override
    public void hide() { }
    @Override
    public void dispose() { }
}
