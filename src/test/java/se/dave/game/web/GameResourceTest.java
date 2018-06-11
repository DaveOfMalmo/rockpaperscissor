package se.dave.game.web;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import se.dave.game.data.IllegalGameStateException;
import se.dave.game.data.Option;
import se.dave.game.data.Outcome;
import se.dave.game.service.GameService;
import se.dave.game.web.api.ErrorResponse;
import se.dave.game.web.api.PlayResponse;
import se.dave.game.web.api.StartResponse;

public class GameResourceTest {
    private static final String PLAYER_A = "player_a";
    private static final String GAME_1 = "game_1";

    @Test
    public void testStart() throws IllegalGameStateException {
        GameResource servlet = new GameResource();

        GameService service = Mockito.mock(GameService.class);
        Mockito.when(service.startSession(PLAYER_A)).thenReturn(GAME_1);

        servlet.setGameService(service);

        StartResponse response = (StartResponse) servlet.addPlayer(PLAYER_A);

        Assert.assertEquals(GAME_1, response.getGameId());
    }

    @Test
    public void testPlay() throws IllegalGameStateException {
        GameResource servlet = new GameResource();

        GameService service = Mockito.mock(GameService.class);
        Mockito.when(service.enterSelection(GAME_1, PLAYER_A, Option.SCISSORS)).thenReturn(Outcome.WIN);

        servlet.setGameService(service);

        PlayResponse response = (PlayResponse) servlet.enterSelection(GAME_1, PLAYER_A, "SCISSORS");

        Assert.assertEquals(Outcome.WIN, response.getOutcome());
    }

    @Test
    public void testPlayIllegalSelection() throws IllegalGameStateException {
        GameResource servlet = new GameResource();

        ErrorResponse response = (ErrorResponse) servlet.enterSelection(GAME_1, PLAYER_A, "SCISSOR");

        Assert.assertNotNull(response.getReason());
    }

    @Test
    public void testErrorWhenPlaying() throws IllegalGameStateException {
        GameResource servlet = new GameResource();

        GameService service = Mockito.mock(GameService.class);
        Mockito.when(service.enterSelection(GAME_1, PLAYER_A, Option.SCISSORS)).thenThrow(new IllegalGameStateException("ERROR"));

        servlet.setGameService(service);

        ErrorResponse response = (ErrorResponse) servlet.enterSelection(GAME_1, PLAYER_A, "SCISSORS");

        Assert.assertTrue(response.getReason().contains("ERROR"));
    }

    @Test
    public void testErrorWhenStarting() throws IllegalGameStateException {
        GameResource servlet = new GameResource();

        GameService service = Mockito.mock(GameService.class);
        Mockito.when(service.startSession(PLAYER_A)).thenThrow(new IllegalGameStateException("ERROR"));

        servlet.setGameService(service);

        ErrorResponse response = (ErrorResponse) servlet.addPlayer(PLAYER_A);

        Assert.assertTrue(response.getReason().contains("ERROR"));
    }
}
