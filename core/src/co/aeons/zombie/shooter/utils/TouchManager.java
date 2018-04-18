package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import co.aeons.zombie.shooter.SpaceGame;

public class TouchManager {

    private static Vector3 firstTouchPos;
    private static Vector3 secondTouchPos;

    public static void initialize(){
        firstTouchPos = new Vector3(Vector3.Zero);
        secondTouchPos = new Vector3(Vector3.Zero);
    }

    /**
     * Pregunta si el jugador ha realizado un primer touch
     * @return True en caso de que si
     */
    public static boolean isFirstTouchActive(){
        return Gdx.input.isTouched(0);
    }

    /**
     * Pregunta si el jugador ha realizado un segundo touch
     * @return True en caso de que si
     */
    public static boolean isSecondTouchActive(){
        return Gdx.input.isTouched(1);
    }

    /**
     * Método para obtener el valor del primer touch (dedo)
     * @return El vector solicitado
     */
    public static Vector3 getFirstTouchPos(){
        firstTouchPos.set(Gdx.input.getX(),Gdx.input.getY(),0);
        firstTouchPos = SpaceGame.camera.unproject(firstTouchPos);
        return firstTouchPos;
    }

    /**
     * Método para obtener el valor del segundo touch (dedo)
     * @return El vector solicitado
     */
    public static Vector3 getSecondTouchPos(){
        secondTouchPos.set(Gdx.input.getX(1),Gdx.input.getY(1),0);
        secondTouchPos = SpaceGame.camera.unproject(secondTouchPos);
        return secondTouchPos;
    }

    /**
     * Calcula si alguno de los dos touch (primer y segundo dedo) cumple con la condicion
     * Condicion: Que el valor X de las posición sea inferior a la dada
     * @param x Parámetro a valorar
     * @return Un vector con el posible touch que cumple la condicion. Si ningún touch la cumple
     * , devuelve un vector vacio (0,0,0) Que será usado
     */
    public static Vector3 getAnyXTouchLowerThan(float x){
        Vector3 result = Vector3.Zero;
        if(isFirstTouchActive() && getFirstTouchPos().x < x)
            result = getFirstTouchPos();
        else if(isSecondTouchActive() && getSecondTouchPos().x < x)
            result = getSecondTouchPos();
        return result;
    }

    /**
     * Calcula si alguno de los dos touch (primer y segundo dedo) cumple con la condicion
     * Condicion: Que el valor X de las posición sea superior a la dada
     * @param x Parámetro a valorar
     * @return Un vector con el posible touch que cumple la condicion. Si ningún touch la cumple
     * , devuelve un vector vacio (0,0,0) Que será usado para comprobar si es un vector correcto
     */
    public static Vector3 getAnyXTouchGreaterThan(float x){
        Vector3 result = Vector3.Zero;
        if(isFirstTouchActive() && getFirstTouchPos().x > x)
            result = getFirstTouchPos();
        else if(isSecondTouchActive() && getSecondTouchPos().x > x)
            result = getSecondTouchPos();
        return result;
    }

    public static Vector3 getTouchFromPosition(float x, float y){
        Vector3 result = null;
        if(isSecondTouchActive()){
            float xSecondDistance = Math.abs(secondTouchPos.x - x);
            float ySecondDistance = Math.abs(secondTouchPos.y - y);

            float xFirstDistance = Math.abs(firstTouchPos.x - x);
            float yFirstDistance = Math.abs(firstTouchPos.y - y);

            float firstDistance = xFirstDistance*xFirstDistance + yFirstDistance*yFirstDistance;
            float secondDistance = xSecondDistance*xSecondDistance + ySecondDistance*ySecondDistance;

            if(firstDistance < secondDistance)
                result = firstTouchPos;
            else
                result = secondTouchPos;
        }else
            result = firstTouchPos;
        return result;
    }

    /**
     * Método usado para saber si un touch (reconocido por un tipo Vector3) está activo o no
     */
    public static boolean isTouchActive(Vector3 vector){
        boolean result = false;
        if(vector.equals(getFirstTouchPos()) && isFirstTouchActive())
            result = true;
        else if (vector.equals(getSecondTouchPos()) && isSecondTouchActive())
            result = true;
        return result;
    }

    /**
     * Método usado para, de un touch concreto (reconocido por un Vector3 posición del touch) saber si se trata
     * del primer touch o del segundo.
     */
    public static int assignWhichTouchCorresponds(Vector3 vector){
        int result = -1;
        if(vector.equals(getFirstTouchPos()))
            result = 0;
        else if (vector.equals(getSecondTouchPos()))
            result = 1;
        return result;
    }

    /**
     * Método usado para devolver el touch dependiendo del pointer pasado por parámetro
     */
    public static Vector3 getTouchFromPointer(int pointer){
        Vector3 result = Vector3.Zero;
        if(pointer == 0)
            result = firstTouchPos;
        else if (pointer == 1)
            result = secondTouchPos;
        return  result;
    }

    /**
     * Método usado para saber si hay algún touch activo (ya sea el primero o el segundo)
     */
    public static boolean isTouchedAnyToucher(){
        return Gdx.input.isTouched();
    }

}
