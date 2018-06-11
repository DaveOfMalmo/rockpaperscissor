package se.dave.game.dao;

import org.junit.Assert;
import org.junit.Test;
import se.dave.game.data.Game;
import se.dave.game.data.IllegalGameStateException;

public class GameDAOTest {
    private static final String PLAYER_A = "player_a";
    private static final String PLAYER_B = "player_b";
    private static final String PLAYER_C = "player_c";

    @Test
    public void testCreate() throws IllegalGameStateException {
        GameDAO dao = new InMemGameDAOImpl();
        Game game = dao.getNewGame(PLAYER_A);

        Assert.assertNotNull(game);

        Game game2 = dao.getNewGame(PLAYER_B);

        Assert.assertNotNull(game2);
        Assert.assertSame(game, game2);
    }

    @Test
    public void testCreateMultiple() throws IllegalGameStateException {
        GameDAO dao = new InMemGameDAOImpl();
        Game game = dao.getNewGame(PLAYER_A);

        Assert.assertNotNull(game);

        Game game2 = dao.getNewGame(PLAYER_B);

        Assert.assertNotNull(game2);
        Assert.assertSame(game, game2);

        Game game3 = dao.getNewGame(PLAYER_C);

        Assert.assertNotNull(game3);
        Assert.assertNotSame(game, game3);
    }

    @Test
    public void testCreateSamePlayer() throws IllegalGameStateException {
        GameDAO dao = new InMemGameDAOImpl();
        Game game = dao.getNewGame(PLAYER_A);

        Assert.assertNotNull(game);

        try {
            dao.getNewGame(PLAYER_A);
            Assert.fail("Should not be able to get new game for same player twice");
        } catch (IllegalGameStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetExisting() throws IllegalGameStateException {
        GameDAO dao = new InMemGameDAOImpl();
        Game game = dao.getNewGame(PLAYER_A);

        Assert.assertNotNull(game);

        Game game2 = dao.getExisting(game.getId());

        Assert.assertNotNull(game2);
        Assert.assertSame(game, game2);
    }

    @Test
    public void testGetMissing() throws IllegalGameStateException {
        GameDAO dao = new InMemGameDAOImpl();

        try {
            dao.getExisting("DUMMY");
            Assert.fail("Should not be able to get game with non-existing id");
        } catch (IllegalGameStateException e) {
            // Expected
        }
    }
}
