package co.aeons.zombie.shooter;

import co.aeons.zombie.shooter.IGoogleServices;
import co.aeons.zombie.shooter.utils.MultiplayerMessage;
import co.aeons.zombie.shooter.utils.enums.MultiplayerState;

//Será una clase cuyo único fin es permitirnos ejecutar la aplicación en escritorio sin que dé fallo
public class DesktopGoogleServices implements IGoogleServices
{

    @Override
    public void submitScore(long score)
    {
        System.out.println("DesktopGoogleServices: submitScore(" + score + ")");
    }


    @Override
    public boolean isSignedIn()
    {
        System.out.println("DesktopGoogleServices: isSignedIn()");
        return false;
    }

    @Override
    public void unlockAchievement(String achievementId) {
        System.out.println("DesktopGoogleServices: unlockAchievement()");
    }

    @Override
    public void showAchievements() {
        System.out.println("DesktopGoogleServices: showAchievements()");
    }

    @Override
    public void startQuickGame() {

    }

    @Override
    public void invitePlayer() {

    }

    @Override
    public void seeMyInvitations() {

    }

    @Override
    public void leaveRoom() {

    }

    @Override
    public MultiplayerState getMultiplayerState() {
        return null;
    }

    @Override
    public void sendGameMessage(String message) {

    }

    @Override
    public MultiplayerMessage receiveGameMessage() {
        return null;
    }
}
