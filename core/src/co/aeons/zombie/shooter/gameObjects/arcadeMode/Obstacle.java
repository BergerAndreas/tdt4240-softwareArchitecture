package co.aeons.zombie.shooter.gameObjects.arcadeMode;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.utils.FontManager;
import co.aeons.zombie.shooter.utils.ObstacleManager;
import co.aeons.zombie.shooter.utils.ShapeRendererManager;
import co.aeons.zombie.shooter.utils.enums.TypeObstacle;

public class Obstacle extends GameObject {

    //Velocidad de giro según los tamaños
    private final static int BIG_ROTATION_SPEED = 20;
    private final static int MEDIUM_ROTATION_SPEED = 50;
    private final static int SMALL_ROTATION_SPEED = 200;

    //Rango en el que puede variar la velocidad de rotación
    private final static int RANGE_FOR_ADITIVE_SPEED_ROTATION = 20;

    //Tipo de obstáculo según constitución
    private TypeObstacle type;

    //Grados en los que girará el obstáculo
    private float degrees;

    //Dirección de giro
    private int direction;

    public Obstacle(String textureName, int x, int y, TypeObstacle type) {
        super(textureName, x, y);

        this.type = type;
        this.resetDegrees();
        direction = (MathUtils.random(-1, 1) >= 0) ? 1 : -1;
    }

    public void update(float delta, float speed) {
        //Primero rotamos el obstáculo
        this.setRotation(degrees);

        //Aumentamos los grados para la siguiente ocasión
        this.updateDegrees(delta);

        //Actualizamos la posición del obstáculo
        this.setY(this.getY() - (speed * delta));
    }

    private void updateDegrees(float delta) {
        //Calculamos un valor para aumentar o decrementar la velocidad dentro de un rango
        int aditiveSpeed = MathUtils.random(-RANGE_FOR_ADITIVE_SPEED_ROTATION, RANGE_FOR_ADITIVE_SPEED_ROTATION);

        //Según el tipo de obstáculo usaremos una velocidad de rotación u otra
        if (type.equals(TypeObstacle.BIG)) {
            degrees += (BIG_ROTATION_SPEED + aditiveSpeed) * delta * direction;
        } else if (type.equals(TypeObstacle.MEDIUM)) {
            degrees += (MEDIUM_ROTATION_SPEED + aditiveSpeed) * delta * direction;
        } else if (type.equals(TypeObstacle.SMALL)) {
            degrees += (SMALL_ROTATION_SPEED + aditiveSpeed) * delta * direction;
        }
    }

    //Pinta aplicando una transparencia dada
    public void render(float alpha) {
        Color c = SpaceGame.batch.getColor();
        float oldAlpha = c.a;

        /*
        c.a = alpha;
        SpaceGame.batch.setColor(c);

        this.renderRotate(degrees / 2);

        c.a = oldAlpha;
        */

        c.r -= 1 - alpha;
        c.g -= 1 - alpha;
        c.b -= 1 - alpha;
        c.a = alpha;

        SpaceGame.batch.setColor(c);

        this.renderRotate(degrees / 2);

        c.r = oldAlpha;
        c.g = oldAlpha;
        c.b = oldAlpha;
        c.a = oldAlpha;

        SpaceGame.batch.setColor(c);
    }

    //Indica si hay una colisión con el obstáculo, aunque sólo preguntando por sus radios, para que la colisión sea más probable
    public boolean isOverlapingWithAnotherObstacle(Obstacle o) {
        float distance = Vector2.dst(this.getCenter().x, this.getCenter().y, o.getCenter().x, o.getCenter().y);
        float totalRadios = this.getRadio() + o.getRadio();

        return distance < totalRadios;
    }

    public TypeObstacle getType() {
        return type;
    }

    public void resetDegrees() {
        this.degrees = 0;
    }

    public void dispose() {
        super.dispose();
    }

}