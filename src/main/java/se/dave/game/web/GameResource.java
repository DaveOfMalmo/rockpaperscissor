package se.dave.game.web;

import se.dave.game.data.IllegalGameStateException;
import se.dave.game.data.Option;
import se.dave.game.data.Outcome;
import se.dave.game.service.GameService;
import se.dave.game.web.api.AbstractResponse;
import se.dave.game.web.api.ErrorResponse;
import se.dave.game.web.api.PlayResponse;
import se.dave.game.web.api.StartResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Resource class handling request for rock paper scissor games
 */
@Path("game")
public class GameResource {

    @Inject
    private GameService gameService;

    void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AbstractResponse addPlayer(@QueryParam("name") String name) {
        String gameId;
        try {
            gameId = gameService.startSession(name);
        } catch (IllegalGameStateException e) {
            return new ErrorResponse("Error when starting game: " + e.getMessage());
        }

        return new StartResponse(gameId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/play")
    public AbstractResponse enterSelection(@PathParam("id") String id,
                                       @QueryParam("name") String name,
                                       @QueryParam("selection") String selection) {
        Option selectedOption;
        try {
            selectedOption = Option.valueOf(selection);
        } catch (IllegalArgumentException e) {
            return new ErrorResponse("Value of parameter selection invalid: " + selection);
        }

        Outcome outcome;
        try {
            outcome = gameService.enterSelection(id, name, selectedOption);
        } catch (IllegalGameStateException e) {
            return new ErrorResponse("Error when entering selection: " + e.getMessage());
        }

        return new PlayResponse(outcome);
    }
}
