package se.dave.game.web.api;

public class StartResponse extends AbstractResponse {
    private final String gameId;

    public StartResponse(String gameId) {
        super("start");
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }
}
