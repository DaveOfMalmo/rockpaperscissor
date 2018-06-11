package se.dave.game.dao;

import se.dave.game.data.Game;
import se.dave.game.data.IllegalGameStateException;

/**
 * Data access interface
 */
public interface GameDAO {
    /**
     * Get game needing another player of create one
     *
     * @param playerName the name of the player
     * @return the game
     */
    Game getNewGame(String playerName) throws IllegalGameStateException;

    /**
     * Get game for player
     *
     * @param gameId the unique id of the game
     * @return the game with the given id
     * @throws IllegalArgumentException if there is no game for that user
     */
    Game getExisting(String gameId) throws IllegalGameStateException;
}