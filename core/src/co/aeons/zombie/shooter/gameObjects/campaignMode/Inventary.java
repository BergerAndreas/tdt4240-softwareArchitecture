package co.aeons.zombie.shooter.gameObjects.campaignMode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.utils.TouchManager;
import co.aeons.zombie.shooter.utils.enums.ColorShip;
import co.aeons.zombie.shooter.utils.enums.TypeElement;

public class Inventary extends GameObject {

    //Son las esferas de los elementos que tiene el inventario
    private Element red;
    private Element yellow;
    private Element blue;

    //Son los huecos donde se puede equipar los elementos para aplicar al arma
    private Slot slot1;
    private Slot slot2;

    //Flecha indicadora para facilitar al jugador dónde colocar los elementos
    private GameObject arrow1;
    private GameObject arrow2;

    //Constante de velocidad con la que el inventario se abrirá y cerrará
    private final static int TRANSITION_SPEED = 900;

    //Velocidad con la que las flechas suben y bajan, y que puede cambiar de sentido
    private int transitionArrow;

    //Atributo auxiliar que almacenará los cálculos de la posición del inventario en el movimiento
    private float relativePos;

    //Indica si el inventario se está cerrando
    private boolean isClosing;

    public Inventary() {
        super("inventary", 0, 0);

        red = new Element("red", 0, 0);
        yellow = new Element("yellow", 0, 0);
        blue = new Element("blue", 0, 0);

        slot1 = new Slot("slot", 0, 0);
        slot2 = new Slot("slot", 0, 0);

        arrow1 = new GameObject("arrow", 0, 0);
        arrow2 = new GameObject("arrow", 0, 0);

        relativePos = 0.0f;

        transitionArrow = 0;
    }

    //Se ejecutará cada vez que se active el inventario
    public void restart() {
        isClosing = false;

        this.setToInitialState(0.0f);

        this.setX(-this.getWidth());
        red.setX(red.getX() - this.getWidth());
        yellow.setX(yellow.getX() - this.getWidth());
        blue.setX(blue.getX() - this.getWidth());

        slot1.setX(slot1.getX() - this.getWidth());
        slot2.setX(slot2.getX() - this.getWidth());
    }

    //Coloca el inventario en su posición inicial
    private void setToInitialState(float delta) {
        this.setX(0);
        this.setY(0);

        red.setX(delta, 32);
        red.setY(350);
        yellow.setX(delta, 31);
        yellow.setY(193);
        blue.setX(delta, 29);
        blue.setY(39);

        red.setIsActivate(false);
        blue.setIsActivate(false);
        yellow.setIsActivate(false);

        slot1.setX(delta, 190);
        slot1.setY(delta, 151);
        slot2.setX(delta, 190);
        slot2.setY(delta, 276);

        arrow1.setX(203);
        arrow1.setY(213);
        arrow2.setX(203);
        arrow2.setY(340);

        transitionArrow = 150;
    }

    public void render() {
        super.render();

        red.render();
        yellow.render();
        blue.render();

        slot1.render();
        slot2.render();

        if (red.isActivate() || blue.isActivate() || yellow.isActivate()) {
            arrow1.render();
            arrow2.render();
        }
    }

    public void update(float delta, CampaignShip ship) {

        //Primero comprobamos si el inventario no está colocado en su sitio
        if (this.getX() < 0) {
            //En caso de no estar en su sitio quiere decir que está en movimiento, por tanto movemos los elementos y la nave según la posición relativa
            relativePos = TRANSITION_SPEED * delta;
            this.moveAllElements(delta, relativePos);
            ship.setX(ship.getX() + relativePos, delta);

            //Evitamos que el inventario se pase de largo
            if (this.getX() > 0) {
                this.setToInitialState(delta);
            }
        } else {
            //Como el inventario está colocado en su sitio, ahora comprobamos si el jugador está interactuando con algún elemento

            if (Gdx.input.justTouched()) {
                Vector3 v = TouchManager.getFirstTouchPos();

                //Comprobamos si se ha tocado un slot y actuamos en consecuencia
                this.checkSlotIsTouched(slot1, v.x, v.y, ship);
                this.checkSlotIsTouched(slot2, v.x, v.y, ship);

                //Desactivamos los elementos antes de activar el que se haya pulsado (si se dió el caso)
                deactivateElements();

                //Si se ha pulsado un elemento, lo activamos
                this.checkAnyElementIsTouched(v.x, v.y);
            }

            //Actualizamos cada objeto en los casos que sean necesario
            updateAllObjects(delta);

            //Actualizamos la nave para mantener su efecto de partículas activo
            ship.setX(ship.getX(),delta);
        }
    }

    //Mueve todos los elementos según la posición relativa
    private void moveAllElements(float delta, float distance) {
        this.setX(this.getX() + distance);
        red.setX(delta, red.getX() + distance);
        yellow.setX(delta, yellow.getX() + distance);
        blue.setX(delta, blue.getX() + distance);
        slot1.setX(delta, slot1.getX() + distance);
        slot2.setX(delta, slot2.getX() + distance);
    }

    //Invoca los updates de todos los elementos dentro del inventario
    private void updateAllObjects(float delta) {
        red.update(delta);
        blue.update(delta);
        yellow.update(delta);
        slot1.update(delta);
        slot2.update(delta);
        updateArrows(delta);
    }

    //Actualiza el movimiento de las flechas que indican la posición de los slots
    private void updateArrows(float delta) {
        if ((arrow1.getY() > 220 && transitionArrow > 0) || (arrow1.getY() < 190 && transitionArrow < 0))
            transitionArrow *= -1;
        arrow1.setY(arrow1.getY() + transitionArrow * delta);
        arrow2.setY(arrow2.getY() + transitionArrow * delta);
    }

    //Actualiza el cierre del inventario
    public void updateClosing(float delta, CampaignShip ship) {
        //Hacemos una cosa u otra según si el inventario no se ha cerrado completamente
        if (this.getX() > -this.getWidth()) {
            relativePos = TRANSITION_SPEED * delta;

            this.setX(this.getX() - relativePos);
            red.setX(delta, red.getX() - relativePos);
            yellow.setX(delta, yellow.getX() - relativePos);
            blue.setX(delta, blue.getX() - relativePos);

            slot1.setX(delta, slot1.getX() - relativePos);
            slot2.setX(delta, slot1.getX() - relativePos);

            ship.setX(ship.getX() - relativePos, delta);
        } else {
            //Si el inventario se ha cerrado completamente, cambiamos la bandera y recolocamos la nave
            isClosing = false;
            ship.setToInitialX(delta);
        }
    }

    //Indica si el inventario está cerrándose
    public boolean isClosing() {
        return isClosing;
    }

    //Pondrá el inventario en el estado en el que está cerrándose, y por tanto desactivará el elemento que lo estuviese
    public void setIsClosing(boolean isClosing) {
        deactivateElements();
        this.isClosing = isClosing;
    }

    //Coloca el elemento que estuviese activado sobre un slot si éste ha sido tocado
    public void checkSlotIsTouched(Slot slot, float x, float y, CampaignShip ship) {
        if (slot.isOverlapingWith(x, y)) {
            if (red.isActivate()) {
                slot.equipElement(TypeElement.RED);
            } else if (blue.isActivate()) {
                slot.equipElement(TypeElement.BLUE);
            } else if (yellow.isActivate()) {
                slot.equipElement(TypeElement.YELLOW);
            } else {
                slot.unequip();
            }

            //Actualizamos el color de la nave en el caso de haberse cambiado los slots
            this.changeColorOfShip(ship);
        }
    }

    //Cambia el color de la nave según los elementos equipados
    public void changeColorOfShip(CampaignShip ship) {
        if (slot1.hasElementEquipped() && slot2.hasElementEquipped()) {
            if ((slot1.hasSpecifiedElement(TypeElement.BLUE) && slot2.hasSpecifiedElement(TypeElement.YELLOW)) ||
                    (slot1.hasSpecifiedElement(TypeElement.YELLOW) && slot2.hasSpecifiedElement(TypeElement.BLUE))) {
                ship.setTexture("ship_green");
                ship.changeColor(ColorShip.GREEN);
            } else if ((slot1.hasSpecifiedElement(TypeElement.RED) && slot2.hasSpecifiedElement(TypeElement.YELLOW)) ||
                        (slot1.hasSpecifiedElement(TypeElement.YELLOW) && slot2.hasSpecifiedElement(TypeElement.RED))) {
                ship.setTexture("ship_orange");
                ship.changeColor(ColorShip.ORANGE);
            } else if ((slot1.hasSpecifiedElement(TypeElement.BLUE) && slot2.hasSpecifiedElement(TypeElement.RED)) ||
                        (slot1.hasSpecifiedElement(TypeElement.RED) && slot2.hasSpecifiedElement(TypeElement.BLUE))) {
                ship.setTexture("ship_purple");
                ship.changeColor(ColorShip.PURPLE);
            } else if (slot1.hasSpecifiedElement(TypeElement.RED) && slot2.hasSpecifiedElement(TypeElement.RED)) {
                ship.setTexture("ship_red");
                ship.changeColor(ColorShip.RED);
            } else if (slot1.hasSpecifiedElement(TypeElement.BLUE) && slot2.hasSpecifiedElement(TypeElement.BLUE)) {
                ship.setTexture("ship_blue");
                ship.changeColor(ColorShip.BLUE);
            } else if (slot1.hasSpecifiedElement(TypeElement.YELLOW) && slot2.hasSpecifiedElement(TypeElement.YELLOW)) {
                ship.setTexture("ship_yellow");
                ship.changeColor(ColorShip.YELLOW);
            }
        } else if (slot1.hasElementEquipped() && !slot2.hasElementEquipped()) {
            if (slot1.hasSpecifiedElement(TypeElement.RED)) {
                ship.setTexture("ship_red");
                ship.changeColor(ColorShip.RED);
            } else if (slot1.hasSpecifiedElement(TypeElement.BLUE)) {
                ship.setTexture("ship_blue");
                ship.changeColor(ColorShip.BLUE);
            } else if (slot1.hasSpecifiedElement(TypeElement.YELLOW)) {
                ship.setTexture("ship_yellow");
                ship.changeColor(ColorShip.YELLOW);
            }
        } else if (!slot1.hasElementEquipped() && slot2.hasElementEquipped()) {
            if (slot2.hasSpecifiedElement(TypeElement.RED)) {
                ship.setTexture("ship_red");
                ship.changeColor(ColorShip.RED);
            } else if (slot2.hasSpecifiedElement(TypeElement.BLUE)) {
                ship.setTexture("ship_blue");
                ship.changeColor(ColorShip.BLUE);
            } else if (slot2.hasSpecifiedElement(TypeElement.YELLOW)) {
                ship.setTexture("ship_yellow");
                ship.changeColor(ColorShip.YELLOW);
            }
        } else {
            ship.setTexture("ship");
            ship.changeColor(ColorShip.COLORLESS);
        }
    }

    //Activa o desactiva un elemento si éste ha sido tocado
    public void checkAnyElementIsTouched(float x, float y) {
        if (Gdx.input.isTouched()) {
            if (red.isOverlapingWith(x, y)) {
                red.setIsActivate(!red.isActivate());
            } else if (yellow.isOverlapingWith(x, y)) {
                yellow.setIsActivate(!yellow.isActivate());
            } else if (blue.isOverlapingWith(x, y)) {
                blue.setIsActivate(!blue.isActivate());
            } else {
                deactivateElements();
            }
        }
    }

    public void deactivateElements() {
        red.setIsActivate(false);
        blue.setIsActivate(false);
        yellow.setIsActivate(false);
    }

    public void dispose() {
        red.dispose();
        blue.dispose();
        yellow.dispose();
        slot1.dispose();
        slot2.dispose();
        arrow1.dispose();
        arrow2.dispose();
    }

}