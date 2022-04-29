package no.ntnu.idatt1002.k204.tasystem.model;

/**
 * Class that defines a player.
 */
public class Player {
    private String gamertag;
    private String rank;

    /**
     * Player Constructor
     * @param gamertag gamer-tag of player
     * @param rank rank of player
     */
    public Player(String gamertag, String rank) throws IllegalArgumentException {
        if(gamertag != null && !gamertag.equals("")) {
            this.gamertag = gamertag;
        } else {
            throw new IllegalArgumentException("Gamer-tag cannot be null or empty");
        }
        if (rank != null && !rank.equals("")){
            this.rank = rank;
        } else{
            this.rank = "Unranked";
        }
    }

    /**
     * Get gamer-tag of player
     * @return gamertag
     */
    public String getGamertag() {
        return gamertag;
    }

    /**
     * Get rank of player
     * @return rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * Modifies the gamer-tag of player
     * @param gamertag gamer-tag of player
     */
    public void setGamertag(String gamertag) {
        if(gamertag != null && !gamertag.equals("")) {
            this.gamertag = gamertag;
        } else {
            throw new IllegalArgumentException("Gamertag cannot be null or empty");
        }
    }

    /**
     * Modifies the rank of player
     * @param rank rank of player
     */
    public void setRank(String rank) {
        if (rank != null && !rank.equals("")){
            this.rank = rank;
        } else{
            this.rank = "Unranked";
        }

    }

    @Override
    public String toString() {
        return "Player{" +
                "gamertag='" + gamertag + '\'' +
                ", rank='" + rank + '\'' +
                '}';
    }
}
