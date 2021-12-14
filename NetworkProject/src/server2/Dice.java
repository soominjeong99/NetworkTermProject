package server2;

public class Dice {
    public static int getDice() {
        return (int) (Math.random() * 6) + 1;
    }
}
