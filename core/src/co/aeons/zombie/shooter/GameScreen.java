package co.aeons.zombie.shooter;

import co.aeons.zombie.shooter.utils.AchievementsManager;
import co.aeons.zombie.shooter.utils.enums.GameState;

public abstract class GameScreen extends BasicScreen {

    //Estado en el que se encuentra el juego
    public GameState state;

    /**
     * Esta sección está pensada para lo que se deba mostrar sin importar el estado del juego
     * Es decir, siempre
     */
    public abstract void renderEveryState(float delta);

    /**
     * La sección de Update para la renderización que no pertenece a ningún estado del juego, por lo que se hace siempre
     */
    public abstract void updateEveryState(float delta);

    /**
     * En esta sección renderizamos lo que se deba mostrar cuando el estado de juego sea de READY
     */
    public abstract void renderReady(float delta);

    /**
     * Asociada a la renderización, también tenemos la sección de Update si el juego está en el estado READY
     */
    public abstract void updateReady(float delta);

    /**
     * En esta sección renderizamos lo que se deba mostrar cuando el estado de juego sea de START
     */
    public abstract void renderStart(float delta);

    /**
     * Asociada a la renderización, también tenemos la sección de Update si el juego está en el estado START
     */
    public abstract void updateStart(float delta);

    /**
     * En esta sección renderizamos lo que se deba mostrar cuando el estado de juego sea de PAUSE
     */
    public abstract void renderPause(float delta);

    /**
     * Asociada a la renderización, también tenemos la sección de Update si el juego está en el estado PAUSE
     */
    public abstract void updatePause(float delta);

    /**
     * En esta sección renderizamos lo que se deba mostrar cuando el estado de juego sea de WIN
     */
    public abstract void renderWin(float delta);

    /**
     * Asociada a la renderización, también tenemos la sección de Update si el juego está en el estado WIN
     */
    public abstract void updateWin(float delta);

    /**
     * En esta sección renderizamos lo que se deba mostrar cuando el estado de juego sea de LOSE
     */
    public abstract void renderLose(float delta);

    /**
     * Asociada a la renderización, también tenemos la sección de Update si el juego está en el estado LOSE
     */
    public abstract void updateLose(float delta);

    /**
     * Hacemos el dispose de los objetos aquí. También hay que llamar al super.dispose para terminar de hacerlo
     * correctamente
     */
    public abstract void disposeScreen();

    @Override
    public void mainRender(float delta) {
        // Hacemos lo que se tenga que hacer en cada estado del juego
        renderEveryState(delta);
        updateEveryState(delta);

        // Ahora dependiendo de cada estado se hace llamar a su render y update correspondiente
        switch (state) {
            case LOSE:
                renderLose(delta);
                updateLose(delta);
                break;
            case PAUSE:
                renderPause(delta);
                updatePause(delta);
                break;
            case READY:
                renderReady(delta);
                updateReady(delta);
                break;
            case START:
                renderStart(delta);
                updateStart(delta);
                break;
            case WIN:
                renderWin(delta);
                updateWin(delta);
                break;
            default:
                throw new IllegalArgumentException("Estado de juego no válido");
        }

        //Actualizamos la gestión de logros
        AchievementsManager.update(delta);
    }

    @Override
    public void dispose() {}
    @Override
    public void show() {}
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

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
}