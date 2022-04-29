package no.ntnu.idatt1002.k204.tasystem.dao;

import no.ntnu.idatt1002.k204.tasystem.model.Player;
import no.ntnu.idatt1002.k204.tasystem.model.Team;
import no.ntnu.idatt1002.k204.tasystem.model.TeamRegister;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TeamDAO {
    private static boolean isTest = Database.isJunitTest();

    /**
     * Add team to database.
     *
     * @param players  players in team
     * @param teamName name of team
     */

    public void addTeam(ArrayList<Player> players, String teamName) {
        String sql;

        if (isTest) {
            sql = "INSERT INTO TESTteam VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO team VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        if (teamExists(teamName)) {
            return;
        }

        PreparedStatement statement = null;
        try {
            statement = Database.getConnection().prepareStatement(sql);
            statement.setString(1, teamName);

            statement.setString(2, players.get(0).getGamertag());
            statement.setString(3, players.get(0).getRank());
            statement.setString(4, players.get(1).getGamertag());
            statement.setString(5, players.get(1).getRank());
            statement.setString(6, players.get(2).getGamertag());
            statement.setString(7, players.get(2).getRank());
            statement.setString(8, players.get(3).getGamertag());
            statement.setString(9, players.get(3).getRank());
            statement.setString(10, players.get(4).getGamertag());
            statement.setString(11, players.get(4).getRank());


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
     * Checks if a team exists in the database
     *
     * @param name the name of the team
     * @return true or false
     */
    private boolean teamExists(String name) {
        String sql;

        if (isTest){
            sql = "SELECT name FROM TESTteam WHERE name = ?";
        } else {
            sql = "SELECT name FROM team WHERE name = ?";
        }

        ResultSet res = null;
        PreparedStatement statement = null;
        Team team = null;
        boolean exists = false;

        try {
            statement = Database.getConnection().prepareStatement(sql);
            statement.setString(1, name);
            res = statement.executeQuery();
            while (res.next()) {
                team = new Team(res.getString("name"));
            }
            if (team != null) {
                exists = true;
                return exists;
            }
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
     * Fills a team-register with teams from the database
     *
     * @param register the team register to be filled
     */
    public void getTeam(TeamRegister register) {
        String sql;

        if (isTest) {
            sql = "SELECT * FROM TESTteam";
        } else {
            sql = "SELECT * FROM team";
        }

        ResultSet res = null;

        try {
            res = Database.getConnection().prepareStatement(sql).executeQuery();
            while (res.next()) {
                Team team = new Team(res.getString("name"), res.getString("p1name"),
                        res.getString("p1rank"), res.getString("p2name"), res.getString("p2rank"),
                        res.getString("p3name"), res.getString("p3rank"), res.getString("p4name"),
                        res.getString("p4rank"), res.getString("p5name"), res.getString("p5rank"));
                register.addTeam(team);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method used to delete a team from the database
     *
     * @param teamName the name of the team to be deleted
     */
    public void deleteTeam(String teamName) {
        String sql;
        String sql1;

        if (isTest) {
            sql = "DELETE FROM TESTtournament_team WHERE teamName = ?";
            sql1 = "DELETE FROM TESTteam WHERE name = ?";
        } else {
            sql = "DELETE FROM tournament_team WHERE teamName = ?";
            sql1 = "DELETE FROM team WHERE name = ?";
        }

        PreparedStatement statement = null;
        try {
            statement = Database.getConnection().prepareStatement(sql);
            statement.setString(1, teamName);
            statement.executeUpdate();
            statement = Database.getConnection().prepareStatement(sql1);
            statement.setString(1, teamName);
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
     * Method used to update a team in the database.
     *
     * @param teamName the name of the team
     * @param players  the list of players
     */
    public void updateTeam(String teamName, ArrayList<Player> players) {
        String sql1;
        if (isTest) {
            sql1 = "UPDATE TESTteam SET  p1name = ?, p2name = ?, p3name = ?, p4name = ?, p5name = ?, " +
                    "p1rank = ?, p2rank = ?, p3rank = ?, p4rank = ?, p5rank = ?  WHERE name = ?";
        } else {
            sql1 = "UPDATE team SET  p1name = ?, p2name = ?, p3name = ?, p4name = ?, p5name = ?," +
                    "p1rank = ?, p2rank = ?, p3rank = ?, p4rank = ?, p5rank = ?  WHERE name = ?";
        }

        PreparedStatement statement = null;
        try {
            statement = Database.getConnection().prepareStatement(sql1);
            statement.setString(1, players.get(0).getGamertag());
            statement.setString(2, players.get(1).getGamertag());
            statement.setString(3, players.get(2).getGamertag());
            statement.setString(4, players.get(3).getGamertag());
            statement.setString(5, players.get(4).getGamertag());
            statement.setString(6, players.get(0).getRank());
            statement.setString(7, players.get(1).getRank());
            statement.setString(8, players.get(2).getRank());
            statement.setString(9, players.get(3).getRank());
            statement.setString(10, players.get(4).getRank());
            statement.setString(11, teamName);
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
}
