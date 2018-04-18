package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

public class ShapeLoader {

    // Atributo que va a guardar todas las shapes que puede tener el juego
    // En principio guardará las shapes de enemigos, nave y disparos
    public Array<ShapeWrapper> shapeEntities;

    // Clase auxiliar para obtener del json concreto las shapes
    private static class ShapeWrapper implements Json.Serializable{
        String entity;
        float[] vertices;
        float[] desiredSize;

        @Override
        public void write(Json json) {

        }

        @Override
        public void read(Json json, JsonValue jsonData) {
            entity = jsonData.child().asString();
            vertices = jsonData.child().next().asFloatArray();
            if(jsonData.child().next().next() != null)
                desiredSize = jsonData.child().next().next().asFloatArray();
        }
    }

    // Método que usaremos para inicializar la carga de los shapes dado el fichero en settings
    public static ShapeLoader initialize(String jsonFile){
        FileHandle file = AssetsManager.loadFile(jsonFile);
        Json json = new Json();

        return json.fromJson(ShapeLoader.class,file);
    }

    /**
     * Obtiene los vertices dado el nombre de una entidad
     * Este nombre de entidad coincide con el nombre del objeto expuesto en el AssetManager
     * Así que sí se cambia, hay que cambiarlo del archivo settings
     */
    public float[] getVertices(String entity){
        ShapeWrapper result = new ShapeWrapper();

        for(ShapeWrapper wrapper: shapeEntities){
            if(wrapper.entity.equals(entity)){
                result = wrapper;
                break;
            }
        }
        return result.vertices;
    }

    /**
     * Obtiene el tamaño deseado dado el nombre de una entidad
     * Este nombre de entidad coincide con el nombre del objeto expuesto en el AssetManager
     */
    public float[] getDesiredSize(String entity){
        ShapeWrapper result = new ShapeWrapper();

        for(ShapeWrapper wrapper: shapeEntities){
            if(wrapper.entity.equals(entity)){
                result = wrapper;
                break;
            }
        }

        if(result.desiredSize == null)
            return null;

        return result.desiredSize;
    }

}
