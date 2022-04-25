package no.ntnu.idatt1002.k204.tasystem.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * The Tournament register that holds all tournaments.
 */
public class TournamentRegister {
    private final ArrayList<Tournament> tournaments = new ArrayList<>();

    /**
     * The constructor for the TournamentRegister class.
     */
    public TournamentRegister() {
    }

    /**
     * Adds a new tournament to the register.
     * @param name the name of the tournament
     * @param rankRequirement the minimum rank required to take part in the tournament
     * @param dateTime the date and time of the tournament
     */
    public void addTournament(String name, String rankRequirement, String otherRequirement, LocalDateTime dateTime) {
        tournaments.add(new Tournament(name, rankRequirement, otherRequirement, dateTime));
    }

    /**
     * Add tournament.
     *
     * @param tournament the tournament
     */
    public void addTournament(Tournament tournament) {
        this.tournaments.add(tournament);
    }

    /**
     * Gets a list of all tournaments in the register.
     * @return a list of tournaments
     */
    public ArrayList<Tournament> getTournaments() {
        return tournaments;
    }

    /**
     * Searches for a specific tournament in the register.
     * @param tournamentName the name of the tournament, as a String
     * @return the corresponding Tournament object if a matching tournament is found. If not, null is returned.
     */
    public Tournament getTournamentByName(String tournamentName) {
        for (Tournament tournament: tournaments) {
            if (tournament.getName().equals(tournamentName)) {
                return tournament;
            }
        }
        return null;
    }


}
