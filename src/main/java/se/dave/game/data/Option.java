package se.dave.game.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Selection options in a game of rock paper scissors
 */
public enum Option {
    ROCK,
    PAPER,
    SCISSORS;

    private static Map<Option, Option> WINS_AGAINST = new HashMap<Option, Option>();
    static {
        WINS_AGAINST.put(ROCK, SCISSORS);
        WINS_AGAINST.put(PAPER, ROCK);
        WINS_AGAINST.put(SCISSORS, PAPER);
    }

    /**
     * Get outcome if this options is up against the given one
     *
     * @param opponentSelection opposing option
     * @return the outcome
     */
    public Outcome competeAgainst(Option opponentSelection) {
        if (this == opponentSelection) {
            return Outcome.DRAW;
        }

        Option winsAgainst = WINS_AGAINST.get(this);
        return opponentSelection == winsAgainst ? Outcome.WIN : Outcome.LOSS;
    }
}
