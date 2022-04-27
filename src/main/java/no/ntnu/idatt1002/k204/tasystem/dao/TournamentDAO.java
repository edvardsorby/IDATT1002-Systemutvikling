package no.ntnu.idatt1002.k204.tasystem.dao;

import no.ntnu.idatt1002.k204.tasystem.model.Team;
import no.ntnu.idatt1002.k204.tasystem.model.TeamRegister;
import no.ntnu.idatt1002.k204.tasystem.model.Tournament;
import no.ntnu.idatt1002.k204.tasystem.model.TournamentRegister;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data access object for tournament
 */
public class TournamentDAO {
    private static boolean isTest = Database.isJunitTest();

    /**
     * Add tournament to database.
     *
     * @param name             the name
     * @param status           the status
     * @param rankRequirement  the rankRequirement
     * @param otherRequirement the other requirement
     * @param date             the date
     * @param time             the time
     */
    public void addTournament(int id, String name, String status, String rankRequirement, String otherRequirement, String date, String time) {
        String sql;
        if (isTest) {
            sql = "INSERT INTO TESTtournament VALUES(? , ? , ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO tournament VALUES(? , ? , ?, ?, ?, ?, ?)";
        }

        if (id <= getMaxTournamentID()) {
            id = getMaxTournamentID() + 1;
        }

        PreparedStatement statement = null;
        try {
            statement = Database.getConnection().prepareStatement(sql);
            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setString(3, status);
            statement.setString(4, rankRequirement);
            statement.setString(5, otherRequirement);
            statement.setString(6, date);
            statement.setString(7, time);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the maximum tournament id from the database
     * Used for making sure all tournaments have unique id's
     *
     * @return the highest id in the database
     */
    private int getMaxTournamentID() {
        String sql;
        if (isTest) {
            sql = "SELECT MAX(tournament_id) AS MaxID FROM TESTtournament";
        } else {
            sql = "SELECT MAX(tournament_id) AS MaxID FROM tournament";
        }

        int maxID = 0;

        ResultSet result;
        try {
            result = Database.getConnection().prepareStatement(sql).executeQuery();
            while (result.next()) {
                maxID = result.getInt("MaxID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxID;
    }

    /**
     * Fills a tournament-register with tournaments from the database
     *
     * @param register the register to be filled
     */
    public void getTournament(TournamentRegister register) {
        //Reset count tournaments since we update each time new tournament is added.
        Tournament.setCountTournaments(0);
        String sql;

        if (isTest) {
            sql = "SELECT * FROM TESTtournament";
        } else {
            sql = "SELECT * FROM tournament";
        }

        ResultSet result = null;

        try {
            result = Database.getConnection().prepareStatement(sql).executeQuery();
            while (result.next()) {
                Tournament tournament = new Tournament(result.getString("tournament_id"), result.getString("name"), result.getString("status"), result.getString("rankRequirement"),
                        result.getString("otherRequirement"), result.getString("start_date"), result.getString("start_time"));
                register.addTournament(tournament);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Add tournament with participating teams to database
     *
     * @param tournamentID the id of the tournament
     * @param teamName     the name of the team
     */
    public void addTournamentAndTeam(int tournamentID, String teamName) {
        String sql;

        if (isTest) {
            sql = "INSERT INTO TESTtournament_team VALUES(? , ?, ?)";
        } else {
            sql = "INSERT INTO tournament_team VALUES(? , ?, ?)";
        }

        if (tournamentAndTeamExist(tournamentID, teamName)) {
            return;
        }

        PreparedStatement statement = null;
        try {
            statement = Database.getConnection().prepareStatement(sql);
            statement.setInt(1, tournamentID);
            statement.setString(2, teamName);
            statement.setInt(3, 0);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tournamentAndTeamExist(int tournamentId, String teamName) {
        String sql;

        if (isTest) {
            sql = "SELECT * FROM TESTtournament_team WHERE tournament_id = ? AND teamName = ?";
        } else {
            sql = "SELECT * FROM tournament_team WHERE tournament_id = ? AND teamName = ?";
        }

        ResultSet res = null;
        PreparedStatement statement = null;
        Tournament tournament = null;
        boolean exists = false;

        try {
            statement = Database.getConnection().prepareStatement(sql);
            statement.setInt(1, tournamentId);
            statement.setString(2, teamName);
            res = statement.executeQuery();
            exists = res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return exists;
    }

    /**
     * Deletes a tournament from the database
     *
     * @param tournamentID the id of the tournament to be deleted
     */
    public void deleteTournament(int tournamentID) {
        String sql1;
        String sql2;
        String sql3;
        String sql4;

        if (isTest) {
            sql1 = "DELETE FROM TESTgrp WHERE tournament_id = ?";
            sql2 = "DELETE FROM TESTknockout_match WHERE tournament_id = ?";
            sql3 = "DELETE FROM TESTtournament_team WHERE tournament_id = ?";
            sql4 = "DELETE FROM TESTtournament WHERE tournament_id = ?";
        } else {
            sql1 = "DELETE FROM grp WHERE tournament_id = ?";
            sql2 = "DELETE FROM knockout_match WHERE tournament_id = ?";
            sql3 = "DELETE FROM tournament_team WHERE tournament_id = ?";
            sql4 = "DELETE FROM tournament WHERE tournament_id = ?";
        }
        PreparedStatement statement = null;
        try {
            statement = Database.getConnection().prepareStatement(sql1);
            statement.setInt(1, tournamentID);
            statement.executeUpdate();
            statement = Database.getConnection().prepareStatement(sql2);
            statement.setInt(1, tournamentID);
            statement.executeUpdate();
            statement = Database.getConnection().prepareStatement(sql3);
            statement.setInt(1, tournamentID);
            statement.executeUpdate();
            statement = Database.getConnection().prepareStatement(sql4);
            statement.setInt(1, tournamentID);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get teams that are currently participating in a tournament given the tournament id from database
     *
     * @param tournamentID the id of the tournament
     * @return a team-register with the teams
     */
    public TeamRegister getTeamsGivenTournamentId(int tournamentID) {
        String sql;

        if (isTest) {
            sql = "SELECT * from TESTtournament_team WHERE tournament_id = ?";
        } else {
            sql = "SELECT * from tournament_team WHERE tournament_id = ?";
        }

        PreparedStatement statement = null;
        ResultSet result = null;

        TeamRegister teamRegister = new TeamRegister();
        try {
            statement = Database.getConnection().prepareStatement(sql);
            statement.setInt(1, tournamentID);

            result = statement.executeQuery();
            while (result.next()) {
                Team team = new Team(result.getString("teamName"), result.getString("points"));

                teamRegister.addTeam(team);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.close(statement, result);
        }
        return teamRegister;
    }

    /**
     * Gets a tournament from the database
     *
     * @param tournamentID the ID of the tournament
     * @return the tournament object
     */
    public Tournament getTournamentById(int tournamentID) {
        String sql;

        if (isTest) {
            sql = "SELECT * from TESTtournament WHERE tournament_id = ?";
        } else {
            sql = "SELECT * from tournament WHERE tournament_id = ?";
        }

        PreparedStatement statement = null;
        ResultSet result = null;

        Tournament tournament = null;
        try {
            statement = Database.getConnection().prepareStatement(sql);
            statement.setInt(1, tournamentID);

            result = statement.executeQuery();
            while (result.next()) {
                tournament = new Tournament(result.getString("tournament_id"), result.getString("name"),
                        result.getString("status"), result.getString("rankRequirement"),
                        result.getString("otherRequirement"), result.getString("start_date"),
                        result.getString("start_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.close(statement, result);
        }
        return tournament;
    }

    /**
     * Method used to update a tournament in the database.
     *
     * @param id               the selected tournament ID
     * @param name             the new name of the tournament
     * @param rankRequirement  the new rank requirement
     * @param otherRequirement the new general requirement
     * @param date             the new date
     * @param time             the new time
     */
    public void updateTournament(int id, String name, String rankRequirement, String otherRequirement, String date, String time) {
        String sql;
        if (isTest) {
            sql = "UPDATE TESTtournament SET name = ?, rankRequirement = ?, otherRequirement = ?, start_date = ?, start_time = ? WHERE tournament_id = ?";
        } else {
            sql = "UPDATE tournament SET name = ?, rankRequirement = ?, otherRequirement = ?, start_date = ?, start_time = ? WHERE tournament_id = ?";
        }

        PreparedStatement statement = null;
        try {
            statement = Database.getConnection().prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, rankRequirement);
            statement.setString(3, otherRequirement);
            statement.setString(4, date);
            statement.setString(5, time);
            statement.setInt(6, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the status of a tournament
     *
     * @param tournamentId the id of the tournament
     * @param status       the new status (for example "Finished" or "Group stage")
     */
    public void updateTournamentStatus(int tournamentId, String status) {
        String sql;
        if (isTest) {
            sql = "UPDATE TESTtournament SET status = ? WHERE tournament_id = ?";
        } else {
            sql = "UPDATE tournament SET status = ? WHERE tournament_id = ?";
        }

        PreparedStatement statement = null;
        try {
            statement = Database.getConnection().prepareStatement(sql);
            statement.setString(1, status);
            statement.setInt(2, tournamentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets all knockout stage matches from the database
     *
     * @param tournamentID the id of the tournament
     * @param matches      a list containing all the matches
     */
    public void getKnockoutMatches(int tournamentID, Team[][] matches) {
        String sql;
        if (isTest) {
            sql = "SELECT * from TESTknockout_match WHERE tournament_id = ?";
        } else {
            sql = "SELECT * from knockout_match WHERE tournament_id = ?";
        }

        PreparedStatement statement = null;
        ResultSet result = null;

        try {
            statement = Database.getConnection().prepareStatement(sql);
            statement.setInt(1, tournamentID);

            result = statement.executeQuery();

            int i = 0;
            while (result.next()) {
                Team team1 = null;
                Team team2 = null;
                Team winner = null;

                if (!result.getString("team1").equals("")) {
                    team1 = new Team(result.getString("team1"));
                }

                if (!result.getString("team2").equals("")) {
                    team2 = new Team(result.getString("team2"));
                }

                if (!result.getString("winner").equals("")) {
                    winner = new Team(result.getString("winner"));
                }

                matches[i] = (new Team[]{team1, team2, winner});

                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            Database.close(statement, result);
        }
    }

    /**
     * Updates all the knockout stage matches in the database
     *
     * @param tournamentID the id of the tournament
     * @param matches      a list containing all the matches
     */
    public void updateKnockoutMatches(int tournamentID, Team[][] matches) {
        String sql1;
        String sql2;

        if (isTest) {
            sql1 = "SELECT FROM TESTknockout_match WHERE tournament_id = ?";
            sql2 = "INSERT INTO TESTknockout_match VALUES(?, ?, ?, ?, ?)";
        } else {
            sql1 = "SELECT * from knockout_match WHERE tournament_id = ?";
            sql2 = "INSERT INTO knockout_match VALUES(?, ?, ?, ?, ?)";
        }

        PreparedStatement statement = null;
        try {
            statement = Database.getConnection().prepareStatement(sql1);
            statement.setInt(1, tournamentID);
            statement.executeUpdate();

            for (int i = 0; i < matches.length; i++) {

                statement = Database.getConnection().prepareStatement(sql2);
                statement.setInt(1, tournamentID);
                statement.setInt(2, i + 1);

                if (matches[i][0] == null) {
                    statement.setString(3, "");
                } else {
                    statement.setString(3, matches[i][0].getTeamName());
                }

                if (matches[i][1] == null) {
                    statement.setString(4, "");
                } else {
                    statement.setString(4, matches[i][1].getTeamName());
                }

                if (matches[i][2] == null) {
                    statement.setString(5, "");
                } else {
                    statement.setString(5, matches[i][2].getTeamName());
                }

                statement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
