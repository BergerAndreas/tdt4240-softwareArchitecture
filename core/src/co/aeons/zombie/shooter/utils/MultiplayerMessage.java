package co.aeons.zombie.shooter.utils;


public class MultiplayerMessage {

    // Máscaras a usar a nivel de operaciones Bit
    public final int MASK_NO_OPT;
    public final int MASK_LEAVE;
    public final int MASK_SHOOT;
    public final int MASK_HAS_RECEIVE_DAMAGE;
    public final int MASK_CYCLE_WEAPON_UP;
    public final int MASK_CYCLE_WEAPON_DOWN;
    public final int MASK_SHIELD;

    private int operations;
    private float positionY;

    private String zombies;
    private String deadBullets;
    private String deadZombies;
    private String buttonSpawn;

    private int weaponID;
    public MultiplayerMessage() {
        // Códigos que representa en binario :
        // 00000
        MASK_NO_OPT = 0;
        // 000001
        MASK_LEAVE = 1;
        // 000010
        MASK_SHOOT = 2;
        // 000100
        MASK_CYCLE_WEAPON_UP = 4;
        // 001000
        MASK_CYCLE_WEAPON_DOWN = 8;
        // 010000
        MASK_HAS_RECEIVE_DAMAGE = 16;
        // 100000
        MASK_SHIELD = 32;

        // Al principio no realizan ninguna operación
        operations = MASK_NO_OPT;

        // Al principio la posición inicial es la mitad de la altura
        // TODO Esto deben de recogerlo de cada clase, no puesto aqui
        positionY = 130;//TODO: FIx ZombieShooter.cam.viewportHeight/2;

        //Assign NONE to set initial state
        zombies = "NONE";
        deadBullets = "NONE";
        deadZombies = "NONE";
        buttonSpawn = "NONE";
        weaponID = 0;
    }

    public void resetOperations() {
        // Operation = 00000
        operations = MASK_NO_OPT;
        zombies = "NONE";
        deadBullets = "NONE";
        deadZombies = "NONE";
        buttonSpawn = "NONE";
    }

    public void setOperation(int i) {
        operations = operations | i;
    }

    public int getOperations() {
        return operations;
    }


    public boolean checkOperation(int i) {
        return (operations & i) == i;
    }

    public float getPositionY() {
        return positionY;
    }

    public void setPositionY(float positionY) {
        this.positionY = positionY;
    }

    public void setZombies(String zombies) {
        this.zombies = zombies;
    }

    public void setDeadBullets(String deadBullets) {
        this.deadBullets = deadBullets;
    }

    public void setDeadZombies(String deadZombies) {
        this.deadZombies = deadZombies;
    }

    public void setButtonSpawn(String buttonSpawn) {
        this.buttonSpawn = buttonSpawn;
    }

    public String getZombies() {
        return this.zombies;
    }

    public String getDeadZombies() {
        return this.deadZombies;
    }

    public String getButtonSpawn() {
        return this.buttonSpawn;
    }


    public void setPropertiesFromMessage(String s) {
        if (!s.isEmpty() || !s.equals("")) {
            String[] result = s.split(";;ANTON;;");
            positionY = Float.parseFloat(result[0]);
            operations = Integer.parseInt(result[1]);
            if (!result[2].equals("NONE")) {
                zombies = result[2];
            }
            if (!result[3].equals("NONE")) {
                deadZombies = result[3];
            }
            if (!result[4].equals("NONE")) {
                deadBullets = result[4];
            }
            weaponID = Integer.parseInt(result[5]);
        }
    }

    public String getForSendMessage() {
        return positionY + ";;ANTON;;" + operations + ";;ANTON;;" + zombies + ";;ANTON;;" +
                deadZombies + ";;ANTON;;" + deadBullets+";;ANTON;;"+weaponID;

    }


    public void setWeaponID(int weaponID) {
        this.weaponID = weaponID;
    }

    public int getWeaponId() {
        return this.weaponID;
    }
}
