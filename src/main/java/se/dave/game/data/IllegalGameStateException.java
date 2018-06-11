package se.dave.game.data;

public class IllegalGameStateException extends Exception {
    public IllegalGameStateException(String message) {
        super(message);
    }
}
