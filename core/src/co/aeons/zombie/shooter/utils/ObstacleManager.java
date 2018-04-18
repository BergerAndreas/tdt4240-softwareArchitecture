package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import co.aeons.zombie.shooter.GameObject;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.gameObjects.arcadeMode.ArcadeShip;
import co.aeons.zombie.shooter.gameObjects.arcadeMode.Obstacle;
import co.aeons.zombie.shooter.screens.ArcadeScreen;
import co.aeons.zombie.shooter.utils.enums.TypeObstacle;

public class ObstacleManager {

    //Escala de los obstáculos en la capa superior
    public static final float TOP_SCALE = 1f;
    //Escala de los obstáculos en la capa inferior
    public static final float BOTTOM_SCALE = 0.5f;
    //Valor mínimo que podrá tener bottomProbability
    public static final float MIN_BOTTOM_PROBABILITY = 1f;
    //Valor mínimo que podrá tener topProbability
    public static final float MIN_TOP_PROBABILITY = 0.5f;
    //Velocidad inicial a la que se moverán los obstáculos
    public final static int INITIAL_SPEED = 150;
    //Aceleración con la que aumentará la velocidad
    public final static float ACCELERATION = 1.5f;

    //Obstáculos de la capa superior
    public static Array<Obstacle> obstaclesInTop;

    //Obstáculos de la capa inferior
    public static Array<Obstacle> obstaclesInBottom;

    //Listas que almacenan los obstáculos que se salen de la pantalla para ser reutilizados
    public static Array<Obstacle> deletedSmallObstacles;
    public static Array<Obstacle> deletedMediumObstacles;
    public static Array<Obstacle> deletedBigObstacles;

    //Indica las probabilidades de aparecer un meteorito en cada capa
    public static float bottomProbability;
    public static float topProbability;

    //Velocidad a la que se moverán los obstáculos
    public static float speed;

    //Carga inicial del gestor necesario para ponerse en marcha
    public static void load(){
        obstaclesInTop = new Array<Obstacle>();
        obstaclesInBottom = new Array<Obstacle>();
        deletedSmallObstacles = new Array<Obstacle>();
        deletedMediumObstacles = new Array<Obstacle>();
        deletedBigObstacles = new Array<Obstacle>();

        bottomProbability = MIN_BOTTOM_PROBABILITY;
        topProbability = MIN_TOP_PROBABILITY;
        speed = INITIAL_SPEED;
    }

    public static void update(float delta){
        generateObstacles();
        updateProbabilities(delta);
        updateObstacles(delta, obstaclesInTop, TOP_SCALE);
        updateObstacles(delta, obstaclesInBottom, BOTTOM_SCALE);
    }

    //
    private static void generateObstacles() {
        createObstacle(obstaclesInBottom, -1, bottomProbability);
        createObstacle(obstaclesInTop, 1, topProbability);
    }

    //Crea un obstáculo según una lista, una escala y una probabilidad que indicará si finalmente se crea
    private static void createObstacle(Array<Obstacle> obstacles, float layer, float probability) {
        if (MathUtils.random(0, 100) <= probability) {
            Obstacle obstacle;
            float probabilityType = MathUtils.random(0, 100);

            /* Hay diferentes probabilidades de que se creen obstáculos:
             * - Obstáculo pequeño: 90%
             * - Obstáculo mediano: 8%
             * - Obstáculo grande: 2%
             * Antes de crearlo, comprobamos que si hay alguno guardado en la lista de borrados para ser reutilizado
             */
            if (probabilityType >= 98) {
                if (deletedBigObstacles.size != 0) {
                    obstacle = deletedBigObstacles.get(0);
                    deletedBigObstacles.removeIndex(0);
                } else {
                    obstacle = new Obstacle("big_obstacle", 0, 0, TypeObstacle.BIG);
                }
            } else if (probabilityType >= 90) {
                if (deletedMediumObstacles.size != 0) {
                    obstacle = deletedMediumObstacles.get(0);
                    deletedMediumObstacles.removeIndex(0);
                } else {
                    obstacle = new Obstacle("medium_obstacle", 0, 0, TypeObstacle.MEDIUM);
                }
            } else {
                if (deletedSmallObstacles.size != 0) {
                    obstacle = deletedSmallObstacles.get(0);
                    deletedSmallObstacles.removeIndex(0);
                } else {
                    obstacle = new Obstacle("small_obstacle", 0, 0, TypeObstacle.SMALL);
                }
            }

            //Colocamos el obstáculo en una posición 'X' aleatoria
            obstacle.setX(MathUtils.random(0, SpaceGame.width - obstacle.getWidth()));
            //Colocamos el obstáculo en una posición 'Y' arriba de la pantalla y fuera de ésta
            obstacle.setY(SpaceGame.height + obstacle.getHeight());
            //Escalamos el objeto según la escala dada
            float scale = (layer == 1) ? TOP_SCALE : BOTTOM_SCALE;
            obstacle.setScale(scale, scale);

            //Finalmente añadimos el obstáculo a la lista, siempre y cuando no choque con algún otro obstáculo
            if (!existsCollision(obstacle, layer)) {
                obstacles.add(obstacle);
            }
        }
    }

    /* Actualiza las probabilidades de aparición de obstáculos según el siguiente criterio:
     * - Aumenta si la nave se encuentra en esa capa
     * - Disminuye si no se encuentra en ella
     * - La variación de probabilidad es mayor en la capa de abajo al haber mayor espacio de movimiento
     */
    private static void updateProbabilities(float delta) {
        if (ArcadeScreen.layer == 1) {
            topProbability += (delta * 0.05);
            if (bottomProbability > MIN_BOTTOM_PROBABILITY)
                bottomProbability -= (delta * 0.025);
        } else {
            bottomProbability += (delta * 0.1);
            if (topProbability > MIN_TOP_PROBABILITY)
                topProbability -= (delta * 0.013);
        }
    }

    //Actualizamos la posición de los obstáculos que están creados
    private static void updateObstacles(float delta, Array<Obstacle> obstacles, float scale) {
        for (Obstacle obstacle: obstacles) {
            //Actualizamos el obstáculo concreto
            obstacle.update(delta, speed * scale);

            //Eliminamos el obstáculo si se ha salido de la pantalla
            if(obstacle.getY() < -obstacle.getHeight()){
                removeObstacle(obstacle, obstacles);
            }
        }

        //Aumentamos la velocidad de los obstáculos
        speed += (delta * ACCELERATION);
    }

    //Borra un obstáculo de la lista pasada por parámetro y lo incluye en su correspondiente lista de borrados
    private static void removeObstacle(Obstacle obstacle, Array<Obstacle> obstacles) {
        //Lo quitamos de la lista que pasamos por parámetro
        obstacles.removeValue(obstacle,false);

        //Reiniciamos sus grados para que cuando vuelva a recuperarse valga de nuevo 0
        obstacle.resetDegrees();

        //Por último, según el obstáculo que sea, lo metemos en la lista de borrados correspondiente
        if (obstacle.getType().equals(TypeObstacle.BIG)) {
            deletedBigObstacles.add(obstacle);
        } else if (obstacle.getType().equals(TypeObstacle.MEDIUM)) {
            deletedMediumObstacles.add(obstacle);
        } else if  (obstacle.getType().equals(TypeObstacle.SMALL)) {
            deletedSmallObstacles.add(obstacle);
        }
    }

    //Pintamos los obstáculos de arriba forma normal o transparente según en la capa que estemos
    public static void renderTop(float alpha){
        for (Obstacle obstacle: obstaclesInTop)
            obstacle.render(alpha);
    }

    //Pintamos los obstáculos de abajo forma normal o transparente según en la capa que estemos
    public static void renderBottom(float alpha){
        for (Obstacle obstacle: obstaclesInBottom)
            obstacle.render(alpha);
    }

    //Comprueba si hay colisión entre el gameObject y algún obstáculo de la capa que esté activa en ArcadeScreen
    public static boolean existsCollision(GameObject gameObject, float layer) {
        boolean res = false;
        Array<Obstacle> obstacles;

        //Según la capa donde estemos, se comprobará una lista de obstáculos u otra
        if (layer == 1)
            obstacles = obstaclesInTop;
        else
            obstacles = obstaclesInBottom;

        //Finalmente comprobamos si hay colisión
        for (Obstacle obstacle: obstacles) {

            //Pregutnamos de forma distinta si se trata de un obstáculo o bien la nave
            if ((gameObject instanceof Obstacle && obstacle.isOverlapingWithAnotherObstacle((Obstacle) gameObject)) ||
                    (gameObject instanceof ArcadeShip && gameObject.isOverlapingWith(obstacle))) {
                res = true;
                break;
            }
        }

        return res;
    }

}
