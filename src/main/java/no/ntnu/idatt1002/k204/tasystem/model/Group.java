package no.ntnu.idatt1002.k204.tasystem.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class that defines a group.
 */
public class Group {
    private final String groupName;
    private final ArrayList<Team> teams;

    /**
     * Instantiates a new Group.
     *
     * @param groupName the group name
     */
    public Group(String groupName) {
        this.groupName = groupName;
        this.teams = new ArrayList<>();
        validateName(groupName);;
    }

    /**
     * Validates a team name
     *
     * @param name the team
     */
    private void validateName(String name){
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Please provide a group name!");
        }
    }

    /**
     * Add team boolean.
     *
     * @param teamName   the team name
     * @param teamPoints the team points
     * @return the boolean
     */
    public boolean addTeam(String teamName, String teamPoints) {
        return this.teams.add(new Team(teamName, teamPoints));
    }

    /**
     * Gets teams.
     *
     * @return the teams
     */
    public ArrayList<Team> getTeams() {
        return teams;
    }

    /**
     * Check equality of a group object.
     * Groups are equal if they have the same group name and contains same teams.
     *
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((!(o instanceof Group))) return false;
        Group group = (Group) o;
        return Objects.equals(groupName, group.groupName) && Objects.equals(teams, group.teams);
    }

    /**
     * Create a string of a group instance
     * Used when debugging.
     *
     * @return string of a group item.
     */
    @Override
    public String toString() {
        return groupName + " " + this.teams;
    }
}
