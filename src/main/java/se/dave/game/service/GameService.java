package se.dave.game.service;


import se.dave.game.data.IllegalGameStateException;
import se.dave.game.data.Option;
import se.dave.game.data.Outcome;

/**
 * Service class handling rock paper scissor games
 */
public interface GameService {

    /**
     * Start a game session
     *
     * @param playerName name of the player
     * @return unique identifier for the started game
     * @throws IllegalGameStateException
     */
    String startSession(String playerName) throws IllegalGameStateException;

    /**
     * Enter player selection
     *
     * @param gameId id of game
     * @param playerName player name
     * @param selection the selected option
     * @throws IllegalGameStateException
     */
    Outcome enterSelection(String gameId, String playerName, Option selection) throws IllegalGameStateException;
}
