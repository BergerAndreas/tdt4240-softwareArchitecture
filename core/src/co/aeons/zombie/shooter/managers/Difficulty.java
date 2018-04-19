package co.aeons.zombie.shooter.managers;

public class Difficulty {
    private static Difficulty ourInstance = new Difficulty();
    private static int difficulty = 1;

    public void setDifficulty(int difficulty){
        difficulty = difficulty;
    }

    public static int getDifficulty(){
        return difficulty;
    }
}
