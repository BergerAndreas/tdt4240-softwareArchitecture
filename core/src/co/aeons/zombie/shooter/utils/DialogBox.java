package co.aeons.zombie.shooter.utils;

import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.Button;
import co.aeons.zombie.shooter.utils.enums.DialogBoxState;

public class DialogBox extends GameObject {

    //Botón que convierte el estado en CANCELLED
    private Button cancel;

    //Botón que convierte el estado en CONFIRMED
    private Button confirm;

    //Será el texto que se muestre como pregunta del cuadro
    private String question;

    /*Estado que tendrá la venta de diálogo
     * HIDDEN: La ventana no se muestra
     * WAITING: La venta se muestra y está a la espera de que se seleccione una opción
     * CANCELLED: Se ha seleccionado la opción cancel
     * CONFIRMED: Se ha seleccionado la opción confirm
     */
    private DialogBoxState state;

    public DialogBox(String question){
        super("ventana", 0, 0);

        //Inicializamos los objetos en la pantalla en estado HIDDEN
        cancel = new Button("buttonCancel", 0, 0, null,false);
        confirm = new Button("buttonConfirm", 0, 0, null,false);
        this.question = question;
        state = DialogBoxState.HIDDEN;

        //Centramos los elementos en la pantalla
        this.setX((SpaceGame.width / 2) - (this.getWidth() / 2));
        this.setY((SpaceGame.height / 2) - (this.getHeight() / 2));
        cancel.setX((SpaceGame.width / 2) + cancel.getWidth());
        cancel.setY((SpaceGame.height / 2) - cancel.getHeight());
        confirm.setX((SpaceGame.width / 2) - (confirm.getWidth() * 2));
        confirm.setY((SpaceGame.height / 2) - confirm.getHeight());
    }

    public void render() {
        //Se mostrarán los elementos si no están en estado HIDDEN
        if (!state.equals(DialogBoxState.HIDDEN)) {
            super.render();
            cancel.render();
            confirm.render();
            FontManager.drawText(question, this.getY() + this.getHeight() - 50);
        }
    }

    public void update() {
        cancel.update();
        confirm.update();

        //Si se ha seleccionado una opción, cambiamos el estado
        if (cancel.isPressed()) {
            state = DialogBoxState.CANCELLED;
            cancel.setPressed(false);
        } else if (confirm.isPressed()) {
            state = DialogBoxState.CONFIRMED;
            confirm.setPressed(false);
        }
    }

    public DialogBoxState getState() {
        return state;
    }

    public void setStateToWaiting() {
        state = DialogBoxState.WAITING;
    }

    public void setStateToHidden() {
        state = DialogBoxState.HIDDEN;
    }

    public void dispose(){
        super.dispose();
        cancel.dispose();
        confirm.dispose();
    }
}
