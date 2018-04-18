package co.aeons.zombie.shooter;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import co.aeons.zombie.shooter.utils.AssetsManager;
import co.aeons.zombie.shooter.utils.ShapeRendererManager;

public class GameObject {

    //Textura asociada al objeto
    private Texture texture;

    //Almacena el centro de la textura
    private Vector2 center;

    //Distancia entre el centro y el vértice más alejado
    private float radio;

    //Objeto lógico, con el que trabajaremos para interactuar con los demás elementos
    private Polygon logicShape;

    private float width;
    private float height;

    private float initialXScale;
    private float initialYScale;

    private float scaleX;
    private float scaleY;

    public GameObject(String textureName, int x, int y) {
        texture = AssetsManager.loadTexture(textureName);
        center = new Vector2();

        initialXScale =1f;
        initialYScale =1f;

        scaleX = 1f;
        scaleY = 1f;

        float[] vertices = SpaceGame.loadShape(textureName);

        if(vertices == null && texture != null){


            vertices = new float[8];

            vertices[0] = 0;
            vertices[1] = 0;

            vertices[2] = 0;
            vertices[3] = texture.getHeight();

            vertices[4] = texture.getWidth();
            vertices[5] = texture.getHeight();

            vertices[6] = texture.getWidth();
            vertices[7] = 0;
        }else if(texture == null){
            vertices = new float[6];

            vertices[0] = 0;
            vertices[1] = 0;

            vertices[2] = 0;
            vertices[3] = 0;

            vertices[4] = 0;
            vertices[5] = 0;
        }

        this.loadWidthAndHeight(vertices);
        this.loadScaleFactors(textureName);
        this.applyScaleToVertices(vertices);

        logicShape = new Polygon(vertices);
        logicShape.setPosition(x,y);

        this.relocateCenter();
        this.calculateRadio();

        //Recolocamos el origen del logic shape para cuando se realice un giro con setRotation
        this.getLogicShape().setOrigin(getWidth()/2, getHeight()/2);
    }

    private void applyScaleToVertices(float[] vertices) {
        for(int i=0; i < vertices.length; i+=2){
            vertices[i]     *= initialXScale;
            vertices[i+1]   *= initialYScale;
        }
    }

    private void loadScaleFactors(String textureName){
        float[] desiredSize = SpaceGame.loadDesiredSize(textureName);

        if(desiredSize != null){

            setInitialScale(desiredSize[0] / width, desiredSize[1] / height);

            width*= initialXScale;
            height*= initialYScale;
        }
    }

    private void loadWidthAndHeight(float[] vertices){

        float widthLowestPoint = vertices[0];
        float widthGreaterPoint = vertices[0];

        float heightLowestPoint = vertices[1];
        float heightGreaterPoint = vertices[1];

        for(int i = 2; i < vertices.length; i++){

            if(i%2 == 0){
                if(vertices[i] < widthLowestPoint)
                    widthLowestPoint = vertices[i];

                if(vertices[i] > widthGreaterPoint)
                    widthGreaterPoint = vertices[i];
            }else{
                if(vertices[i] < heightLowestPoint)
                    heightLowestPoint = vertices[i];

                if(vertices[i] > heightGreaterPoint)
                    heightGreaterPoint = vertices[i];
            }
        }

        width = widthGreaterPoint - widthLowestPoint;
        height = heightGreaterPoint - heightLowestPoint;

    }

    public void relocateCenter() {
        center.set(this.getX() + (this.getWidth() / 2),
                   this.getY() + (this.getHeight() / 2));
    }

    public void calculateRadio() {
        float[] vertices = this.getLogicShape().getVertices();
        float distance = 0.0f;
        float aux;

        for (int i=0; i<vertices.length; i+=2) {
            aux = Vector2.dst(vertices[i], vertices[i+1], this.getCenter().x, this.getCenter().y);
            if (distance < aux)
                distance = aux;
        }

        radio = distance;
    }

    public Vector2 getCenter(){ return center; }

    public float getRadio() { return radio; }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return logicShape.getX();
    }

    public float getY() {
        return logicShape.getY();
    }

    public void setX(float x) {
        logicShape.setPosition( x , logicShape.getY());
        this.relocateCenter();
    }

    public void setY(float y) {
        logicShape.setPosition(logicShape.getX(), y);
        this.relocateCenter();
    }

    public Texture getTexture(){
        return texture;
    }

    public void setTexture(String textureName) { texture = AssetsManager.loadTexture(textureName); }

    public void changeTexture(Texture newTexture){
        texture = newTexture;
    }

    public Polygon getLogicShape() {
        return logicShape;
    }

    public void setScale(float x, float y){
        scaleX = x; scaleY = y;
        getLogicShape().setScale(x,y);
    }

    public void setInitialScale(float x, float y) {
        initialXScale = x;
        initialYScale = y;
    }

    public void setRotation(float angle){
        logicShape.setRotation(angle);
    }
    public void setOrigin(float x,float y){
        logicShape.setOrigin(x,y);
    }

    public void render(){
        if(texture!=null)
            SpaceGame.batch.draw(new TextureRegion(texture), getX(), getY(), getLogicShape().getOriginX(), getLogicShape().getOriginY(), getWidth(), getHeight(), scaleX, scaleY, getLogicShape().getRotation());
        //ShapeRendererManager.renderPolygon(this.getLogicShape().getTransformedVertices(), Color.WHITE);
    }

    //Método para pintar un objeto rotando N grados su textura
    public void renderRotate(float n){
        SpaceGame.batch.draw(new TextureRegion(texture), getX(), getY(), getWidth()/2, getHeight()/2,
                                this.getWidth(), this.getHeight(),
                                this.getLogicShape().getScaleX(), this.getLogicShape().getScaleY(), n);
        //ShapeRendererManager.renderPolygon(this.getLogicShape().getTransformedVertices(), Color.WHITE);
    }

    public void dispose() {
        texture.dispose();
    }

    //Indica si hay una colisión con el objeto pasado por parámetro
    public boolean isOverlapingWith(GameObject g) {
        boolean result = false;

        float distance = Vector2.dst(this.getCenter().x, this.getCenter().y, g.getCenter().x, g.getCenter().y);
        float totalRadios = this.getRadio() + g.getRadio();

        //Solo se comprueba la colisión si la distancia entre los centros es menor a la suma de los radios
        if (distance < totalRadios)
            result = Intersector.overlapConvexPolygons(this.getLogicShape(), g.getLogicShape());

        return result;
    }

    //Indica si el objeto está sobre el píxel indicado por parámetro
    public boolean isOverlapingWith(float x, float y) {
        return getLogicShape().contains(x,y);
    }
}
