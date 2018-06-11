package se.dave.game.data;

/**
 * Possible outcomes in a game of rock paper scissors
 */
public enum Outcome {
    WIN, LOSS, DRAW;

    /**
     * Get the outcome for an opponent if a game ended with this outcome
     * @return the opposite outcome
     */
    public Outcome getOpposite() {
        return this == DRAW ? DRAW : this == WIN ? LOSS : WIN;
    }
}
