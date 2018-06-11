package se.dave.game.web.api;

import se.dave.game.data.Outcome;

public class PlayResponse extends AbstractResponse {
    private final Outcome outcome;

    public PlayResponse(Outcome outcome) {
        super("play");
        this.outcome = outcome;
    }

    public Outcome getOutcome() {
        return outcome;
    }
}
