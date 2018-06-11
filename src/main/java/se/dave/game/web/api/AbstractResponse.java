package se.dave.game.web.api;

public class AbstractResponse {
    private String type;

    AbstractResponse(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
