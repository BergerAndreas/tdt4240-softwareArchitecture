package co.aeons.zombie.shooter.utils;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import co.aeons.zombie.shooter.SpaceGame;

public class CameraManager {

    // PROPIEDADES NECESARIAS PARA EL EFECTO SHAKE

    // Propiedad para determinar lo que es una vibracion normal
    public static final float NORMAL_SHAKE = 7f;

    // Tiempo total que estará vibrando la pantalla
    private static float totalTime;
    // Máxima potencia de vibracion
    private static float maxPower;

    // Posición inicial de la cámara antes de iniciar la vibración
    private static Vector3 initialPosition;

    // Potencia actual
    private static float currentPower;
    // Tiempo actual
    private static float currentTime;

    // Sabremos si tenemos que empezar el efecto o no con esta propiedad
    private static boolean startEffect;
    //*********************************

    public static void loadShakeEffect(float total_time, float max_power){
        totalTime = total_time;
        maxPower = max_power;

        initialPosition = new Vector3(SpaceGame.camera.position);

        currentPower = 0;
        currentTime  = 0;

        startEffect = false;
    }

    public static void update(float delta){
        if(startEffect){
            if(!isShakeDone()){
                // Calculamos la potencia actual a aplicar a la cámara
                // Esta potencia disminuirá conforme vaya pasando el tiempo
                currentPower = maxPower * ( (totalTime - currentTime) / totalTime  );

                // Calculamos cuanto vamos a trasladar la cámara
                // Esto es un random entre -1 y 1 y multiplicado por la potencia actual
                float x = (MathUtils.random.nextFloat() - 0.5f) * 2 * currentPower;
                float y = (MathUtils.random.nextFloat() - 0.5f) * 2 * currentPower;

                // Trasladamos la cámara esa cantidad
                SpaceGame.camera.position.x = initialPosition.x + x;
                SpaceGame.camera.position.y = initialPosition.y + y;

                // Aumentamos el tiempo actual con el delta
                currentTime+=delta;
            }else{
                // Restablecemos la posición de la camara con la posición inicial
                SpaceGame.camera.position.x = initialPosition.x;
                SpaceGame.camera.position.y = initialPosition.y;
                startEffect = false;
                currentTime = 0;
            }
        }
    }

    // Con este método sabremos si hemos acabado el efecto de vibración o no
    public static boolean isShakeDone(){
        return currentTime >= totalTime;
    }

    // Con este método iniciaremos el efecto de vibración
    public static void startShake(){
        startEffect = true;
    }
}
