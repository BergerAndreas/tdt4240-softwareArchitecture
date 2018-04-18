package co.aeons.zombie.shooter.utils;


import co.aeons.zombie.shooter.SpaceGame;

public class MultiplayerMessage {

    // Máscaras a usar a nivel de operaciones Bit
    public final int MASK_NO_OPT;
    public final int MASK_LEAVE;
    public final int MASK_SHOOT;
    public final int MASK_HAS_RECEIVE_DAMAGE;
    public final int MASK_REG_LIFE;
    public final int MASK_BURST;
    public final int MASK_SHIELD;

    private int operations;
    private float positionY;

    public MultiplayerMessage(){
        // Códigos que representa en binario :
        // 00000
        MASK_NO_OPT             = 0;
        // 000001
        MASK_LEAVE              = 1;
        // 000010
        MASK_SHOOT              = 2;
        // 000100
        MASK_REG_LIFE           = 4;
        // 001000
        MASK_BURST              = 8;
        // 010000
        MASK_HAS_RECEIVE_DAMAGE = 16;
        // 100000
        MASK_SHIELD             = 32;

        // Al principio no realizan ninguna operación
        operations = MASK_NO_OPT;

        // Al principio la posición inicial es la mitad de la altura
        // TODO Esto deben de recogerlo de cada clase, no puesto aqui
        positionY = SpaceGame.height/2;
    }

    public void resetOperations(){
        // Operation = 00000
        operations = MASK_NO_OPT;
    }

    public void setOperation(int i){
        operations = operations | i;
    }

    public int getOperations(){
        return operations;
    }


    public boolean checkOperation(int i){
        return (operations & i) == i;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }


    public void setPropertiesFromMessage(String s){
        if(!s.isEmpty() || !s.equals("")){
            String[] result = s.split(":");
            positionY = Float.parseFloat(result[0]);
            operations = Integer.parseInt(result[1]);
        }
    }

    public String getForSendMessage(){
        return positionY +":"+ operations;
    }

}
