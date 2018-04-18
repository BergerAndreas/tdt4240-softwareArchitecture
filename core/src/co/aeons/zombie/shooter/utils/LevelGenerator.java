package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import co.aeons.zombie.shooter.gameObjects.campaignMode.Enemy;
import co.aeons.zombie.shooter.gameObjects.campaignMode.enemies.*;
import co.aeons.zombie.shooter.gameObjects.campaignMode.enemies.PurpleEnemy;
import co.aeons.zombie.shooter.utils.enums.TypeEnemy;

public class LevelGenerator {

    //Indica el último enemigo mostrado en pantalla
    public Enemy actualEnemy;

    //Cadena de enemigos que se generan desde el script
    public Array<EnemyWrapper> enemiesToGenerate;

    //Clase que se representa un enemigo recogido del script
    private static class EnemyWrapper implements Json.Serializable{
        public TypeEnemy type;
        public int x;
        public int y;
        public float timeToSpawn;

        @Override
        public void write(Json json) {

        }

        @Override
        // Al implementar Serializable tenemos que definir como
        // se van a leer los distintos valores para esta clase
        public void read(Json json, JsonValue jsonData) {
            // El problemas valor es el atributo de tipo
            type = TypeEnemy.valueOf(jsonData.child().asString());

            // El resto van en orden de aparición en el json
            // Tenemos que ejecutar un next por cada valor que queramos
            // comprobar
            x = jsonData.child().next().asInt();
            y = jsonData.child().next().next().asInt();
            timeToSpawn = jsonData.child().next().next().next().asFloat();
        }
    }

    //Convierte los enemigos del json en objetos
    public static LevelGenerator loadLevel(String jsonFile){
        FileHandle file = AssetsManager.loadFile(jsonFile);
        Json json = new Json();

        return json.fromJson(LevelGenerator.class,file);
    }

    /**
     * Este método es el encargado de generar los enemigos
     * Para ello va a reducir en una cantidad delta el tiempo
     * requerido para que aparezca el enemigo.
     * @param enemies Los enemigos generados actualmente
     * @param delta
     * @return Los enemigos que estaban creados más los recien creados
     */
    public Array<Enemy> update(Array<Enemy> enemies,float delta){
        // Recorro los enemigos a poder generar
        for(EnemyWrapper wrapper: enemiesToGenerate){

            if (actualEnemy == null || actualEnemy.isDeletable()) {
                // Resto el tiempo necesario
                wrapper.timeToSpawn -= delta;
            }

            // Si el tiempo se ha cumplido, generamos el enemigo correspondiente
            if(wrapper.timeToSpawn < 0 && (actualEnemy == null || !wrapper.equals(actualEnemy))){

                //Añadimos el nuevo enemigo y lo eliminamos de la lista a generar
                this.addEnemy(enemies, wrapper);
                enemiesToGenerate.removeValue(wrapper,false);
            }
        }
        return enemies;
    }

    private void addEnemy(Array<Enemy> enemies, EnemyWrapper wrapper) {

        switch (wrapper.type){
            case TYPE1:
                enemies.addAll(this.createSquadron(wrapper.x, wrapper.y));
                break;
            case TYPE2:
                enemies.add(new Type2(wrapper.x, wrapper.y));
                break;
            case TYPE3:
                Enemy type3 = new Type3(wrapper.x, wrapper.y);
                enemies.add(type3);
                actualEnemy = type3;
                break;
            case TYPE4:
                //Debemos agregar todas las partes del enemigo
                Type4 type4 = new Type4(wrapper.x, wrapper.y);
                enemies.add(type4);
                enemies.addAll(type4.getPartsOfEnemy());
                actualEnemy = type4;
                break;
            case TYPE5:
                Enemy type5 = new Type5(wrapper.x, wrapper.y);
                enemies.add(type5);
                actualEnemy = type5;
                break;
            case RED:
                Enemy red = new RedEnemy(wrapper.x, wrapper.y);
                enemies.add(red);
                actualEnemy = red;
                break;
            case BLUE:
                Enemy blue = new BlueEnemy(wrapper.x, wrapper.y);
                enemies.add(blue);
                actualEnemy = blue;
                break;
            case YELLOW:
                Enemy yellow = new YellowEnemy(wrapper.x,wrapper.y);
                enemies.add(yellow);
                break;
            case GREEN:
                GreenEnemy green = new GreenEnemy(wrapper.x,wrapper.y);
                enemies.add(green);
                enemies.addAll(green.getPartsOfEnemy());
                actualEnemy = green;
                break;
            case ORANGE:
                OrangeEnemy orange = new OrangeEnemy(wrapper.x,wrapper.y);
                enemies.add(orange);
                enemies.addAll(orange.getPartsOfEnemy());
                actualEnemy = orange;
                break;
            case PURPLE:
                Enemy purple = new PurpleEnemy(wrapper.x, wrapper.y);
                enemies.addAll(purple.getPartsOfEnemy());
                enemies.add(purple);
                actualEnemy = purple;
                break;
            default:
                throw new IllegalArgumentException("Se ha tratado de generar un enemigo de tipo inexistente");
        }
    }

    /**
     * Crea un escuadrón de enemigos tipo 1 (5). Los parámetros de entrada son el lugar exacto
     * de aparición del primer enemigo. Todos irán retrasados respecto a él.
     * @return Un Array de Type1 con 5 elementos
     */
    private Array<Type1> createSquadron(int x, int y){
        Array<Type1> result = new Array<Type1>();
        result.add(new Type1(x,y,0.0f));
        result.add(new Type1(x,y,0.35f));
        result.add(new Type1(x,y,0.65f));
        result.add(new Type1(x,y,0.95f));
        result.add(new Type1(x,y,1.25f));

        return result;
    }
}