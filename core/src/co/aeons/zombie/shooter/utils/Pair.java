package co.aeons.zombie.shooter.utils;

public class Pair<F, S> {
    private F first; //first member of pair
    private S second; //second member of pair

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object other){
        boolean result = false;

        if(this == other)
            result = true;

        if(!result && (other instanceof Pair)){
            Pair otherPair = (Pair) other;
            result = this.getFirst().equals(otherPair.getFirst()) && this.getSecond().equals(otherPair.getSecond());
        }

        return result;
    }
}