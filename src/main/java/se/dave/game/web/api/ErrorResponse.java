package se.dave.game.web.api;

public class ErrorResponse extends AbstractResponse {
    private String reason;

    public ErrorResponse(String reason) {
        super("error");
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
