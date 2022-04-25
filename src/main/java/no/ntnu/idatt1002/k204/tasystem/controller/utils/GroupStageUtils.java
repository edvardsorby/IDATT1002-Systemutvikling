package no.ntnu.idatt1002.k204.tasystem.controller.utils;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import no.ntnu.idatt1002.k204.tasystem.dao.GroupDAO;
import no.ntnu.idatt1002.k204.tasystem.model.Group;
import no.ntnu.idatt1002.k204.tasystem.model.Team;
import no.ntnu.idatt1002.k204.tasystem.model.TeamRegister;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * The type Group stage utils.
 */
public class GroupStageUtils {

    private TeamRegister teamRegister;
    private ObservableList<Team> teamsObservableList;


    public GroupStageUtils(TeamRegister teamRegister) {
        this.teamRegister = teamRegister;
        this.teamsObservableList = FXCollections.observableArrayList(teamRegister.getTeams());
    }

    /**
     * Initialize tree items with teams from database.
     *
     * @param groupDAO     the group dao
     * @param groupName    the group name
     * @param tournamentId the tournament id
     * @param root         the root
     */
    public static void initWithTeamsFromDatabase(GroupDAO groupDAO, String groupName, int tournamentId, TreeItem<Team> root) {
        ArrayList<TreeItem<Team>> teamItems = new ArrayList();
        ArrayList<TreeItem<String>> pointItems = new ArrayList();
        Group group = groupDAO.getGroup(groupName, tournamentId);

        if (group == null) {
            initWithDefaultText(root);
        } else {
            for (int i = 0; i < 3; i++) {//3 teams per group
                TreeItem<Team> teamItem = new TreeItem<>(group.getTeams().get(i));
                TreeItem<String> pointItem = new TreeItem<String>(group.getTeams().get(i).pointsProperty().getValue());
                teamItems.add(teamItem);
                pointItems.add(pointItem);
            }
            root.getChildren().setAll(teamItems);
        }
    }

    /**
     * Initialize tree items with default text.
     *
     * @param root tree item root node
     */
    private static void initWithDefaultText(TreeItem<Team> root) {
        ArrayList<TreeItem<Team>> teamItems = new ArrayList();

        for (int i = 0; i < 3; i++) {//3 teams per group
            TreeItem<Team> teamItem = new TreeItem<>(new Team("<Double click to choose team>"));
            teamItems.add(teamItem);
        }

        root.getChildren().setAll(teamItems);
    }

    /**
     * Randomize teams inside a tree tableview
     *
     * @param root tree item root node
     */
    public void randomize(TreeItem<Team> root) {
        FXCollections.shuffle(this.teamsObservableList);

        ArrayList<TreeItem<Team>> teamItems = new ArrayList();

        //This loop is for filling last tree item root.
        // Otherwise index out of bounds error
        if (this.teamsObservableList.size() == 3) {
            for (Team t : this.teamsObservableList) {
                Team team = new Team(t);
                TreeItem<Team> teamItem = new TreeItem<>(team);
                teamItems.add(teamItem);
                root.getChildren().setAll(teamItems);
            }
            return;
        }

        for (int i = 0; i <= 2; i++) {//3 teams per group
            Team team = new Team(this.teamsObservableList.get(i));
            TreeItem<Team> teamItem = new TreeItem<>(team);
            teamItems.add(teamItem);
            this.teamsObservableList.remove(this.teamRegister.getTeamByName(team.getTeamName()));
        }
        root.getChildren().setAll(teamItems);
    }


    /**
     * Save group.
     *
     * @param groupDAO     the group dao
     * @param groupName    the group name
     * @param tableView    the table view
     * @param tournamentId the tournament id
     */
    public static void saveGroup(GroupDAO groupDAO, String groupName, TreeTableView<Team> tableView, int tournamentId) {
        TreeItem<Team> team1 =tableView.getTreeItem(0);
        TreeItem<Team> team2 =tableView.getTreeItem(1);
        TreeItem<Team> team3 =tableView.getTreeItem(2);

        String team1PointsValue = team1.valueProperty().getValue().pointsProperty().getValue();
        String team2PointsValue = team2.valueProperty().getValue().pointsProperty().getValue();
        String team3PointsValue = team3.valueProperty().getValue().pointsProperty().getValue();
        int team1Points = 0;
        int team2Points = 0;
        int team3Points = 0;
        if(team1PointsValue == null){
            team1Points = 0;
        } else {
            team1Points = Integer.parseInt(team1PointsValue);
        }
        if(team2PointsValue == null){
            team2Points = 0;
        } else {
            team2Points = Integer.parseInt(team2PointsValue);
        }
        if(team3PointsValue ==null){
            team3Points = 0;
        } else {
            team3Points = Integer.parseInt(team3PointsValue);
        }
        groupDAO.addGroup(
                groupName,
                team1.getValue().getTeamName(),
                team2.getValue().getTeamName(),
                team3.getValue().getTeamName(),
                team1Points,
                team2Points,
                team3Points,
                tournamentId);
    }
}
