package se.dave.game.service;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import se.dave.game.dao.GameDAO;
import se.dave.game.data.Game;
import se.dave.game.data.IllegalGameStateException;
import se.dave.game.data.Option;
import se.dave.game.data.Outcome;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GameServiceTest {
    private static ExecutorService executorService;

    private static final String PLAYER_A = "player_a";
    private static final String PLAYER_B = "player_b";
    private static final String PLAYER_C = "player_c";
    
    private static final String GAME_1 = "game_1";

    @BeforeClass
    public static void setup() {
        executorService = Executors.newFixedThreadPool(5);
    }

    @AfterClass
    public static void tearDown() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    public void testNoPlayers() throws IllegalGameStateException, InterruptedException {
        GameServiceImpl service = new GameServiceImpl();
        
        Game game = new Game();
        GameDAO dao = Mockito.mock(GameDAO.class);
        Mockito.when(dao.getExisting(GAME_1)).thenReturn(game);
        service.setGameDAO(dao);
        
        try {
            service.enterSelection(GAME_1, PLAYER_A, Option.PAPER);
            Assert.fail("Should not be able to play a game without players");
        } catch (IllegalGameStateException e) {
            // Expected
        }
    }

    @Test
    public void testOnlyOnePlayer() throws IllegalGameStateException, InterruptedException {
        GameServiceImpl service = new GameServiceImpl();

        Game game = new Game();
        GameDAO dao = Mockito.mock(GameDAO.class);
        Mockito.when(dao.getExisting(GAME_1)).thenReturn(game);
        service.setGameDAO(dao);

        game.addPlayer(PLAYER_A);

        try {
            service.enterSelection(GAME_1, PLAYER_A, Option.PAPER);
            Assert.fail("Should not be able to play a game with one player");
        } catch (IllegalGameStateException e) {
            // Expected
        }
    }

    @Test
    public void testWrongPlayer() throws IllegalGameStateException, InterruptedException {
        GameServiceImpl service = new GameServiceImpl();

        Game game = new Game();
        GameDAO dao = Mockito.mock(GameDAO.class);
        Mockito.when(dao.getExisting(GAME_1)).thenReturn(game);
        service.setGameDAO(dao);

        game.addPlayer(PLAYER_A);
        game.addPlayer(PLAYER_B);

        try {
            service.enterSelection(GAME_1, PLAYER_C, Option.PAPER);
            Assert.fail("Should not be able to play a game with the wrong player");
        } catch (IllegalGameStateException e) {
            // Expected
        }
    }

    @Test
    public void testDraw() throws IllegalGameStateException, InterruptedException {
        GameServiceImpl service = new GameServiceImpl();

        Game game = new Game();
        GameDAO dao = Mockito.mock(GameDAO.class);
        Mockito.when(dao.getExisting(GAME_1)).thenReturn(game);
        service.setGameDAO(dao);

        game.addPlayer(PLAYER_A);
        game.addPlayer(PLAYER_B);

        Future<Outcome> outcomeA = play(service, PLAYER_A, Option.PAPER);
        Future<Outcome> outcomeB = play(service, PLAYER_B, Option.PAPER);

        assertOutcome(outcomeA, Outcome.DRAW);
        assertOutcome(outcomeB, Outcome.DRAW);
    }

    @Test
    public void testFirstPlayerWin() throws IllegalGameStateException, InterruptedException {
        GameServiceImpl service = new GameServiceImpl();

        Game game = new Game();
        GameDAO dao = Mockito.mock(GameDAO.class);
        Mockito.when(dao.getExisting(GAME_1)).thenReturn(game);
        service.setGameDAO(dao);

        game.addPlayer(PLAYER_A);
        game.addPlayer(PLAYER_B);

        Future<Outcome> outcomeA = play(service, PLAYER_A, Option.SCISSORS);
        Future<Outcome> outcomeB = play(service, PLAYER_B, Option.PAPER);

        assertOutcome(outcomeA, Outcome.WIN);
        assertOutcome(outcomeB, Outcome.LOSS);
    }

    @Test
    public void testFirstPlayerLoss() throws IllegalGameStateException, InterruptedException {
        GameServiceImpl service = new GameServiceImpl();

        Game game = new Game();
        GameDAO dao = Mockito.mock(GameDAO.class);
        Mockito.when(dao.getExisting(GAME_1)).thenReturn(game);
        service.setGameDAO(dao);

        game.addPlayer(PLAYER_A);
        game.addPlayer(PLAYER_B);

        Future<Outcome> outcomeA = play(service, PLAYER_A, Option.SCISSORS);
        Future<Outcome> outcomeB = play(service, PLAYER_B, Option.ROCK);

        assertOutcome(outcomeA, Outcome.LOSS);
        assertOutcome(outcomeB, Outcome.WIN);
    }

    @Test
    public void testPlaySamePlayerTwice() throws IllegalGameStateException, InterruptedException {
        GameServiceImpl service = new GameServiceImpl();

        Game game = new Game();
        GameDAO dao = Mockito.mock(GameDAO.class);
        Mockito.when(dao.getExisting(GAME_1)).thenReturn(game);
        service.setGameDAO(dao);

        game.addPlayer(PLAYER_A);
        game.addPlayer(PLAYER_B);

        // Simulate that someone has already entered a selecition for player A
        game.setSelectedPlayerName(PLAYER_A);
        game.setOtherSelection(Option.SCISSORS);

        try {
            service.enterSelection(GAME_1, PLAYER_A, Option.PAPER);
            Assert.fail("Should not be able to play same player twice");
        } catch (IllegalGameStateException e) {
            // Expected
        }
    }

    private Future<Outcome> play(GameService service, String player, Option selection) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Future<Outcome> future = executorService.submit(() -> {
            latch.countDown();
            return service.enterSelection(GAME_1, player, selection);
        });

        // Make sure that task has been started
        latch.await();
        return future;
    }

    private void assertOutcome(Future<Outcome> future, Outcome expected) {
        try {
            Assert.assertEquals(expected, future.get(30, TimeUnit.SECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Assert.fail("Exception when awaiting future: " + e.getMessage());
        }
    }
}
