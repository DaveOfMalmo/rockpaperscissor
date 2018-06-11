package se.dave.game.dao;

import se.dave.game.data.Game;
import se.dave.game.data.IllegalGameStateException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory implementation of GameDAO
 */
public class InMemGameDAOImpl implements GameDAO {
    private final Map<String, Game> games = new ConcurrentHashMap<String, Game>();
    private Game nextGame;

    public synchronized Game getNewGame(String playerName) throws IllegalGameStateException {
        if (nextGame == null) {
            nextGame = createNewGame();
        }

        Game game = nextGame;
        game.addPlayer(playerName);

        if (game.hasPlayers()) {
            nextGame = null;
        }

        return game;
    }

    private Game createNewGame() {
        Game game = new Game();
        games.put(game.getId(), game);

        return game;
    }

    public Game getExisting(String gameId) throws IllegalGameStateException {
        Game existingGame = games.get(gameId);
        if (existingGame == null) {
            throw new IllegalGameStateException("No game for given game id");
        }

        return existingGame;
    }
}
