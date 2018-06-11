package se.dave.game.service;

import se.dave.game.dao.GameDAO;
import se.dave.game.data.Game;
import se.dave.game.data.IllegalGameStateException;
import se.dave.game.data.Option;
import se.dave.game.data.Outcome;

import javax.inject.Inject;

public class GameServiceImpl implements GameService {
    private static final long WAIT_TIMEOUT = 30_000;

    @Inject
    private GameDAO gameDAO;

    void setGameDAO(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    @Override
    public String startSession(String playerName) throws IllegalGameStateException {
        Game game = gameDAO.getNewGame(playerName);

        return game.getId();
    }

    @Override
    public Outcome enterSelection(String gameId, String playerName, Option selection) throws IllegalGameStateException {
        Game game = gameDAO.getExisting(gameId);

        synchronized (game) {
            if (!game.hasPlayers()) {
                throw new IllegalGameStateException("Game doesn't have enough players");
            }
            if (!game.hasPlayer(playerName)) {
                throw new IllegalGameStateException("Player " + playerName + " isn't a player of this game");
            }

            Option otherSelection = game.getOtherSelection();
            String selectedPlayerName = game.getSelectedPlayerName();

            if (otherSelection != null && selectedPlayerName.equals(playerName)) {
                throw new IllegalGameStateException("Player " + playerName + " has already made a selection");
            }

            if (otherSelection == null) {
                game.setSelectedPlayerName(playerName);
                game.setOtherSelection(selection);
                return awaitOtherPlayer(game);
            }

            Outcome outcome = selection.competeAgainst(otherSelection);
            game.setLastOutcome(outcome);
            game.setOtherSelection(null);
            game.setSelectedPlayerName(null);

            game.notify();
            return outcome;
        }
    }

    private Outcome awaitOtherPlayer(Game game) throws IllegalGameStateException {
        try {
            game.wait(WAIT_TIMEOUT);
        } catch (InterruptedException e) {
            throw new IllegalGameStateException("Opponent took too long to respond");
        }

        Outcome outcome = game.getLastOutcome().getOpposite();
        game.setLastOutcome(null);
        return outcome;
    }
}
