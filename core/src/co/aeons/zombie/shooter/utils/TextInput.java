package co.aeons.zombie.shooter.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class TextInput implements Input.TextInputListener{

    private String title;
    private String hint;

    private String text;

    private boolean show;
    private boolean acceptButton;

    public TextInput(String title, String hint){
        this.title  = title;
        this.hint   = hint;

        text = "";

        show = false;
        acceptButton = false;
    }

    public void show(){
        if(!show){
            Gdx.input.getTextInput(this,title,"",hint);
            show = true;
        }
    }

    public String getText(){
        return text;
    }

    public boolean acceptInputDialog(){
        return acceptButton;
    }

    @Override
    public void input(String text) {
        this.text = text;
        acceptButton = true;
    }

    @Override
    public void canceled() {

    }
}
