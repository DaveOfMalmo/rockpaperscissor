package se.dave.game.data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Abstraction of a game of rock paper scissors
 */
public class Game {
    private String selectedPlayerName;
    private Option otherSelection;
    private Outcome lastOutcome;

    private final Set<String> playerNames = new HashSet<String>();
    private final String id;

    public Game() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    /**
     * Add player as a participant of this game
     *
     * @param playerName name of the player to add
     * @throws IllegalGameStateException if there already is two players in this game
     */
    public synchronized void addPlayer(String playerName) throws IllegalGameStateException {
        if (playerNames.size() == 2) {
            throw new IllegalGameStateException("Game already has two players");
        } else if (playerNames.contains(playerName)) {
            throw new IllegalGameStateException("Game already has a player with name: " + playerName);
        }

        playerNames.add(playerName);
    }

    /**
     * Check if the game has the correct amount of players
     *
     * @return true if there are two players in the game
     */
    public boolean hasPlayers() {
        return playerNames.size() == 2;
    }

    /**
     * Check if game has a player with the given name
     *
     * @param playerName name to check for
     * @return true if there is a player with the given name
     */
    public boolean hasPlayer(String playerName) {
        return playerNames.contains(playerName);
    }

    /**
     * Get name of player that has already made his selection
     *
     * @return the name of the player
     */
    public String getSelectedPlayerName() {
        return selectedPlayerName;
    }

    /**
     * Set name of player making the first selection
     *
     * @param selectedPlayerName name of the player
     */
    public void setSelectedPlayerName(String selectedPlayerName) {
        this.selectedPlayerName = selectedPlayerName;
    }

    /**
     * Get first selection
     *
     * @return the first selection or null if there is none
     */
    public Option getOtherSelection() {
        return otherSelection;
    }

    /**
     * Set selection of player making the first selection
     * @param otherSelection
     */
    public void setOtherSelection(Option otherSelection) {
        this.otherSelection = otherSelection;
    }

    /**
     * Get outcome of the game for the other player
     *
     * @return the outcome for the other player of this game
     */
    public Outcome getLastOutcome() {
        return lastOutcome;
    }

    /**
     * Set outcome for this game
     *
     * @param lastOutcome the outcome for current user
     */
    public void setLastOutcome(Outcome lastOutcome) {
        this.lastOutcome = lastOutcome;
    }
}
