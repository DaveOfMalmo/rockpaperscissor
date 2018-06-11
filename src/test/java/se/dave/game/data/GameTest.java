package se.dave.game.data;

import org.junit.Assert;
import org.junit.Test;

public class GameTest {
    private static final String PLAYER_A = "player_a";
    private static final String PLAYER_B = "player_b";
    private static final String PLAYER_C = "player_c";

    @Test
    public void testAddSamePlayerTwice() throws IllegalGameStateException, InterruptedException {
        Game game = new Game();
        game.addPlayer(PLAYER_A);

        try {
            game.addPlayer(PLAYER_A);
            Assert.fail("Should not be able to add same player twice");
        } catch (IllegalGameStateException e) {
            // Expected
        }
    }

    @Test
    public void testAddTooManyPlayers() throws IllegalGameStateException, InterruptedException {
        Game game = new Game();
        game.addPlayer(PLAYER_A);
        game.addPlayer(PLAYER_B);

        try {
            game.addPlayer(PLAYER_C);
            Assert.fail("Should not be able to add same player twice");
        } catch (IllegalGameStateException e) {
            // Expected
        }
    }
}
