package co.aeons.zombie.shooter.managers;

public class Difficulty {
    private static Difficulty ourInstance = new Difficulty();
    private static int difficulty = 1;

    public static void setDifficulty(int d){
        difficulty = d;
    }

    public static int getDifficulty(){
        return difficulty;
    }
}
