package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import co.aeons.zombie.shooter.SpaceGame;

public class FontManager {

    //Tipos de fuentes que se usarán
    public static BitmapFont text;
    public static BitmapFont title;

    // Objeto encargado de la internacionalización del juego
    public static I18NBundle bundle;

    public static void initialize(int width) {
        FreeTypeFontGenerator generator;
        FreeTypeFontGenerator.FreeTypeFontParameter parameter;

        generator = new FreeTypeFontGenerator(Gdx.files.internal("pirulen.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = width / 44;
        text = generator.generateFont(parameter);
        parameter.size = width / 15;
        title = generator.generateFont(parameter);

        generator.dispose();

        // Cargamos el bundle desde su correspondiente método del asset manager
        bundle = AssetsManager.loadBundle();
    }

    //Modifica el alpha de los BitmapFont en caso de ser necesario
    public static void setAlpha(float alpha) {
        title.setColor(0,1,1,alpha);
        text.setColor(1,1,1,alpha);
    }

    //Pinta un texto con formato text en la posición dada
    public static void drawText(String string, float x, float y){
        text.draw(SpaceGame.batch,bundle.get(string),x,y);
    }

    //Pinta un texto con formato text seguido de otro sin formato en la posición dada
    public static void drawText(String stringToText, String stringWithoutCode, float x, float y) {
        text.draw(SpaceGame.batch,bundle.get(stringToText) + stringWithoutCode,x,y);
    }

    //Pinta un texto con formato text centrado en el ancho de la pantalla
    public static void drawText(String string, float y) {
        drawCentered(new GlyphLayout(text, bundle.get(string)), y);
    }

    //Pinta un texto con formato text seguido de otro sin formato centrado en el ancho de la pantalla
    public static void drawText(String stringToText, String stringWithoutCode, float y) {
        drawCentered(new GlyphLayout(text, bundle.get(stringToText) + stringWithoutCode), y);
    }

    //Pinta un texto con formato title en la posición dada
    public static void drawTitle(String string, float x, float y){
        title.draw(SpaceGame.batch,bundle.get(string),x,y);
    }

    //Pinta un texto con formato title en el ancho de la pantalla
    public static void drawTitle(String string, float y) {
        drawCentered(new GlyphLayout(title, bundle.get(string)), y);
    }

    //Pinta un texto sin formato en la posición dada
    public static void draw(String string, float x, float y) {
        text.draw(SpaceGame.batch,string,x,y);
    }

    //Pinta un texto sin formato en el ancho de la pantalla
    public static void draw(String string, float y) {
        drawCentered(new GlyphLayout(text, string), y);
    }

    private static void drawCentered(GlyphLayout layout, float y) {
        float fontX = (SpaceGame.width / 2) - (layout.width / 2);

        text.draw(SpaceGame.batch, layout, fontX, y);
    }

    public static String getFromBundle(String string) {
        return bundle.get(string);
    }

    public void dispose() {
        text.dispose();
        title.dispose();
    }

}
