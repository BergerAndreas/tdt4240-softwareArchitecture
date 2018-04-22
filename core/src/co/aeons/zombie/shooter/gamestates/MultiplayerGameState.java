package co.aeons.zombie.shooter.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.entities.SecondPlayer;
import co.aeons.zombie.shooter.entities.SinusZombie;
import co.aeons.zombie.shooter.entities.Trump;
import co.aeons.zombie.shooter.entities.Zombie;
import co.aeons.zombie.shooter.entities.bullets.BRBullet;
import co.aeons.zombie.shooter.entities.bullets.Bullet;
import co.aeons.zombie.shooter.entities.buttons.EffectButton;
import co.aeons.zombie.shooter.entities.buttons.InstaKill;
import co.aeons.zombie.shooter.managers.Difficulty;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.managers.Jukebox;
import co.aeons.zombie.shooter.managers.Save;
import co.aeons.zombie.shooter.utils.MultiplayerMessage;

import static co.aeons.zombie.shooter.ZombieShooter.cam;
import static co.aeons.zombie.shooter.ZombieShooter.gamePort;
import static co.aeons.zombie.shooter.utils.enums.MultiplayerState.STARTMULTIPLAYER;

public class MultiplayerGameState extends PlayState {

    //Initialize SpriteBatch used to display info about multiplayer state
    private SpriteBatch sb;
    // The second (online) player
    public static SecondPlayer secondPlayer;

    //Check if we're host
    boolean isHost;

    //Dead zombie buffer used to sync dead zombies
    private ArrayList<String> deadZombiesBuffer;

    //Max time before the game will start
    private final float MAX_TIME_TO_START_GAME = 5f;
    // Max time to leave the game before it'll start
    private final float MAX_TIME_TO_LEFT_GAME = 1f;

    //These variables are only used if necessary if we need to send the same operation
    //more than once since we're sending over UDP

    // Numer of times to retry sending the same operation
    private final int TIMES_TO_SEND_SAME_OPERATION = 5;

    // Number of times we have sent the operation
    //TODO: change variable name to fit with our game
    private int times_sended_receive_damage_operation;

    //Information message to show to the user
    private BitmapFont infoMessage;
    private GlyphLayout layout;

    //Dead bullets and zombies
    String deadZombies = "NONE";
    String deadBullets = "NONE";

    //Times start -> finish
    private float timeToStartGame;
    private float timeToLeftGame;

    private int spawnZombieFlag = 0;

    // FirstPlayer powerups
    //TODO: change these to fit with our powerups
    /*
    public static BurstPowerUp playerBurstPowerUp;
    private static RegLifePowerUp playerRegLifePowerUp;
    private static ShieldPowerUp playerShieldPowerUp;

    //SecondPlayer powerups
    public static BurstPowerUp rivalBurstPowerUp;
    private static RegLifePowerUp rivalRegLifePowerUp;
    private static ShieldPowerUp rivalShieldPoweUp;
    */

    // To know if the FirstPlayer left the game
    private boolean abandonFirstPlayer;

    // To know if the SecondPlayer left the game
    private boolean abandonSecondPlayer;

    //Game input and output messages
    private MultiplayerMessage outcomeMessage;
    private MultiplayerMessage incomeMessage;

    //Check if the player wants to leave the room or not
    private boolean leaveRoom;
    private EffectButton effectButton;
    private int deadZombiesBufferlength;
    private int bulletShotFlag;
    private int incomingBulletFlag;
    private String bulletAPI;

    public MultiplayerGameState(GameStateManager gsm, String option) {
        super(gsm);

        sb = new SpriteBatch();

        outcomeMessage = new MultiplayerMessage();
        incomeMessage = new MultiplayerMessage();

        //state = GameState.READY;

        leaveRoom = false;

        times_sended_receive_damage_operation = 0;

        //Show infomessage
        infoMessage = new BitmapFont();
        layout = new GlyphLayout();

        timeToStartGame = MAX_TIME_TO_START_GAME;
        timeToLeftGame = MAX_TIME_TO_LEFT_GAME;
        bulletShotFlag = 0;
        bulletAPI = bulletShotFlag + "#NONE";


        incomingBulletFlag = 0;

        abandonFirstPlayer = false;
        abandonSecondPlayer = false;

        //FIXME: Remove
        this.stage = new Stage(gamePort, sb);
        this.effectButton = new InstaKill(new Rectangle(0, 0, 0, 0));
        deadZombiesBuffer = new ArrayList<>();
        deadZombiesBufferlength = 8;

        //TODO: Initialize powerups

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);

        //Switch statement to choose which mode to display
        switch (option) {
            case "QUICK":
                ZombieShooter.googleServices.startQuickGame();
                break;
            case "INVITE":
                ZombieShooter.googleServices.invitePlayer();
                break;
            default:
                ZombieShooter.googleServices.seeMyInvitations();
                break;
        }
    }

    @Override
    public void init() {
        //TODO: Move this to improve multiplayer?
        super.init();
        //Set multiplayer difficulty
        Difficulty.setDifficulty(1);
        // Create second player
        secondPlayer = new SecondPlayer(super.bullets);


    }

    @Override
    public void update(float dt) {

        if (!ZombieShooter.googleServices.getMultiplayerState().equals(STARTMULTIPLAYER)) {
            String message = "Connecting to server";
            this.sb.begin();
            infoMessage.getData().setScale(2, 2);
            layout.setText(infoMessage, message);
            float width = layout.width;
            infoMessage.draw(this.sb, message, (cam.viewportWidth - width) / 2, cam.viewportHeight - 25);
            this.sb.end();
        }
        updateReady(dt);

    }

    public void updateReady(float dt) {
        switch (ZombieShooter.googleServices.getMultiplayerState()) {
            case STARTMULTIPLAYER:

                if (timeToStartGame > 0) {
                    String message = "Game starting in" + (int) timeToStartGame;
                    String host = ZombieShooter.googleServices.getHostId();
                    String myId = ZombieShooter.googleServices.getMyId();
                    //Inform player how much time is left until we can start the game
                    this.sb.begin();
                    infoMessage.getData().setScale(2, 2);
                    layout.setText(infoMessage, message);
                    float width = layout.width;
                    infoMessage.draw(this.sb, message, (cam.viewportWidth - width) / 2, cam.viewportHeight - 25);
                    this.sb.end();

                    if (host.equals(myId)) {
                        this.isHost = true;
                    }

                    timeToStartGame -= dt;
                } else {
                    // code here to handle game start
                    timeToStartGame = 0;

                    //Here we start the game
                    updateStart(dt);
                    //state = GameState.START;
                }
                break;
            case CANCEL:
                gsm.setState(GameStateManager.MENU);
                break;
        }

    }

    public void updateStart(float dt) {
        updateIncomeMessage(dt);
        updateOutComeMessage(dt);
        if (isHost) {
            this.checkZombieBulletCollision();
            super.checkZombieWallCollision();
            super.updateTimers(dt);
            super.spawnZombie();
            super.player.update(dt);
            super.updatePlayerBullets(dt);
            super.updateZombies(dt);
            updateWallHealth();
            secondPlayer.update(dt);

        } else {
            super.checkZombieWallCollision();
            super.updateTimers(dt);
            super.player.update(dt);
            super.updatePlayerBullets(dt);
            super.updateZombies(dt);
            secondPlayer.update(dt);


        }


        //Act here
    }

    @Override
    protected void checkZombieBulletCollision() {
        //To send bullets and zombies to be removed
        String oldDeadBullets = deadBullets;
        deadZombies = "";
        deadBullets = "";

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            for (int j = 0; j < zombies.size(); j++) {
                Zombie a = zombies.get(j);
                if (a.collide(b)) {
                    deadBullets += b.getId() + ",";
                    bullets.remove(i);
                    i--;
                    a.getHurt(b.getDamage() * damageModifier);
                    if (a.getHealth() <= 0) {
                        a.deathSound();
                        deadZombies += a.getId() + ",";
                        deadZombiesBuffer.add(a.getId());
                        zombies.remove(j);
                        j--;
                        this.incrementScore(a.getScore());
                    }
                    break;
                }
            }
        }
        int iterationStart = 0;
        if (deadZombiesBuffer.size() - deadZombiesBufferlength >= 0) {
            iterationStart = deadZombiesBuffer.size() - deadZombiesBufferlength;
        }
        deadZombies = "";
        for (int i = iterationStart; i < deadZombiesBuffer.size(); i++) {
            deadZombies += deadZombiesBuffer.get(i) + ",";
        }

        if (deadZombies.equals("")) {
            deadZombies = "NONE";
        }
        if (deadBullets.equals("")) {
            deadBullets = oldDeadBullets;
        }

    }

    public void updateIncomeMessage(float dt) {
        //Fetch the input message
        incomeMessage = ZombieShooter.googleServices.receiveGameMessage();

        if (incomeMessage.checkOperation(incomeMessage.MASK_LEAVE)) {
            ZombieShooter.googleServices.leaveRoom();
            gsm.setState(GameStateManager.MENU);
        } else {
            timeToLeftGame = MAX_TIME_TO_LEFT_GAME;
        }

        //Check if opponent has requested to leave the room
        /*
        TODO:Fix win state
        if(rivalShip.isCompletelyDefeated()){
            state = GameState.WIN;
        }
        */
        //Update logic of the rival
        //rivalShip.update(delta,incomeMessage.getPositionY());
        if (!isHost) {
            //TODO: Add to get zombies
            clientSpawnZombies(incomeMessage.getZombies());
            clientDeadZombies(incomeMessage.getDeadZombies());
            clientDeadBullets(incomeMessage.getDeadBullets());
            super.getWall().setCurrentWallHealth(incomeMessage.getWallHealth());
            super.setScore(incomeMessage.getScore());

        }
        secondPlayer.setPosition(secondPlayer.getx(), incomeMessage.getPositionY());

        if (incomeMessage.checkOperation(incomeMessage.MASK_GAMEFINISHED)) {
            Save.gd.setTentativeScore(this.getScore());
            gsm.setState(GameStateManager.GAMEOVER);

        }
        secondPlayerShootBullets(incomeMessage.getBullets());


        /*
        if (incomeMessage.checkOperation(incomeMessage.MASK_SHOOT)) {
            secondPlayer.setWeaponId(incomeMessage.getWeaponId());
            secondPlayer.shoot();
        }
        */
        // Reset for next update
        incomeMessage.resetOperations();


    }

    private void secondPlayerShootBullets(String incomingBullets) {

        if (Integer.parseInt(incomingBullets.split("#")[0]) > incomingBulletFlag) {
            incomingBullets = incomingBullets.split("#")[1];
            if (!incomingBullets.equals("NONE")) {
                List<String> bulletList = Arrays.asList(incomingBullets.split(";"));
                for (String b : bulletList) {
                    String bulletType = b.split(":")[0];
                    String bulletInfo = b.split(":")[1];
                    if (bulletType.equals("b")) {
                        float bulletX = Float.parseFloat(bulletInfo.split(",")[0]);
                        float bulletY = Float.parseFloat(bulletInfo.split(",")[1]);
                        String bulletID = bulletInfo.split(",")[2];
                        Bullet newBullet = new Bullet(bulletX, bulletY);
                        newBullet.setId(bulletID);
                        bullets.add(newBullet);
                    } else if (bulletType.equals("br")) {
                        float bulletX = Float.parseFloat(bulletInfo.split(",")[0]);
                        float bulletY = Float.parseFloat(bulletInfo.split(",")[1]);
                        String bulletID = bulletInfo.split(",")[2];
                        Bullet newBullet = new BRBullet(bulletX, bulletY, 0);
                        newBullet.setId(bulletID);
                        bullets.add(newBullet);
                    } else if (bulletType.equals("s")) {
                        float bulletY = Float.parseFloat(bulletInfo.split(",")[1]);
                        secondPlayer.setPosition(secondPlayer.getx(), bulletY);
                        //2 because shotgun id
                        secondPlayer.setWeaponId(1);
                        secondPlayer.shoot();
                    }
                }
                incomingBulletFlag++;
            }
        }

    }

    private void clientDeadBullets(String deadBullets) {
        if (!deadBullets.equals("NONE")) {
            String[] deadBulletsId = deadBullets.split(",");
            List<String> deadBulletsIdsList = Arrays.asList(deadBulletsId);
            for (int i = 0; i < bullets.size(); i++) {
                if (deadBulletsIdsList.contains(bullets.get(i).getId())) {
                    bullets.remove(i);
                    i--;
                }
            }
        }
    }


    private void clientDeadZombies(String deadZombies) {
        if (!deadZombies.equals("NONE")) {
            String[] deadZombieIds = deadZombies.split(",");
            List<String> deadZombieIdsList = Arrays.asList(deadZombieIds);
            for (int i = 0; i < zombies.size(); i++) {
                if (deadZombieIdsList.contains(zombies.get(i).getId())) {
                    zombies.remove(i);
                    i--;
                }
            }
        }

    }

    private void clientSpawnZombies(String incomingZombies) {
        if (Integer.parseInt(incomingZombies.split("#")[0]) > spawnZombieFlag) {
            incomingZombies = incomingZombies.split("#")[1];
            if (!incomingZombies.equals("NONE")) {
                String[] z = incomingZombies.split(";");
                for (String s : z) {
                    String type = s.split(":")[0];
                    String coordinates = s.split(":")[1];
                    float x = Float.parseFloat(coordinates.split(",")[0]);
                    float y = Float.parseFloat(coordinates.split(",")[1]);
                    String id = coordinates.split(",")[2];
                    if (type.equals("z")) {
                        //TODO: fix difficulty in multiplayer
                        Zombie newZombie = new Zombie(x, y, Difficulty.getDifficulty());
                        newZombie.setId(id);
                        zombies.add(newZombie);
                    } else if (type.equals("t")) {
                        Trump newTrump = new Trump(x, y, Difficulty.getDifficulty());
                        newTrump.setId(id);
                        zombies.add(newTrump);
                    } else if (type.equals("s")) {
                        SinusZombie newSinusZombie = new SinusZombie(x, y, Difficulty.getDifficulty());
                        newSinusZombie.setId(id);
                        zombies.add(newSinusZombie);
                    }
                }
            }
            spawnZombieFlag++;

        }
    }

    public void updateOutComeMessage(float dt) {
        //Update outcome message
        //Vector2 tmpVec = new Vector2();
        //stage.getViewport().unproject(tmpVec.set(Gdx.input.getX(), Gdx.input.getY()));
        outcomeMessage.setPositionY(player.gety());

        if (isHost) {
            outcomeMessage.setZombies(zombieAPI);
            outcomeMessage.setDeadBullets(deadBullets);
            outcomeMessage.setDeadZombies(deadZombies);
            //FIXME: Kanskje vi trenger disse
            //deadZombies = "NONE";
            //deadBullets = "NONE";
            //zombieAPI = "NONE";
            outcomeMessage.setScore(super.getScore());
            outcomeMessage.setWallHealth(super.getWall().getCurrentWallHealth());
        }
        outcomeMessage.setWeaponID(player.getWeaponId());

        if (leaveRoom) {
            outcomeMessage.setOperation(outcomeMessage.MASK_LEAVE);
            gsm.setState(GameStateManager.MENU);
        }

        //Finally we send the message


        ZombieShooter.googleServices.sendGameMessage(outcomeMessage.getForSendMessage());

        //We reset the operations so as not to interfere in the next interaction
        outcomeMessage.resetOperations();


    }

    @Override
    protected void onFireButtonPressed() {
        if (player.shoot()) {
            bulletShotFlag++;
            bulletAPI = bulletShotFlag + "#";

            String currentWeaponType = player.getCurrentWeapon().getType();
            if (currentWeaponType.equals("p")) {
                Bullet b = bullets.get(bullets.size() - 1);
                String bulletID = b.getId();
                String bulletType = b.getType();
                float bulletX = b.getx();
                float bulletY = b.gety();
                bulletAPI += bulletType + ":" + bulletX + "," + bulletY + "," + bulletID;
            } else if (currentWeaponType.equals("br")) {
                for (int i = 0; i < 3; i++) {
                    Bullet b = bullets.get(bullets.size() - 1 - i);
                    String bulletID = b.getId();
                    String bulletType = b.getType();
                    float bulletX = b.getx();
                    float bulletY = b.gety();
                    bulletAPI += bulletType + ":" + bulletX + "," + bulletY + "," + bulletID + ";";
                }
            } else if (currentWeaponType.equals("s")) {
                float bulletX = player.getx();
                float bulletY = player.gety();
                bulletAPI += currentWeaponType + ":" + bulletX + "," + bulletY;
            }

            outcomeMessage.setBullets(bulletAPI);

        }

    }


    @Override
    protected void updateWallHealth() {
        if (super.wall.getCurrentWallHealth() <= 0) {
            wall.playSound();
            Jukebox.getIngameMusic().stop();
            Jukebox.playGameoverMusic();
            outcomeMessage.setOperation(outcomeMessage.MASK_GAMEFINISHED);
            ZombieShooter.googleServices.sendGameMessage(outcomeMessage.getForSendMessage());
            Save.gd.setTentativeScore(this.getScore());
            gsm.setState(GameStateManager.GAMEOVER);
        }
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            leaveRoom = true;
            return true;
        }
        return false;
    }


    @Override
    public void draw() {
        if (isHost) {
            super.draw();
        } else {
            super.draw();
            super.sb.begin();
            effectButton.draw(super.sb, 1);
            super.sb.end();
        }
        sb.setProjectionMatrix(cam.combined);
        //Draw other player
        //TODO: Move second player to super spritebatch?
        secondPlayer.draw(sb);
    }


    @Override
    public void dispose() {

    }

}
