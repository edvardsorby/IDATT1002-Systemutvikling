package no.ntnu.idatt1002.k204.tasystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import no.ntnu.idatt1002.k204.tasystem.Application;
import no.ntnu.idatt1002.k204.tasystem.dao.TeamDAO;
import no.ntnu.idatt1002.k204.tasystem.dao.TournamentDAO;
import no.ntnu.idatt1002.k204.tasystem.model.Team;
import no.ntnu.idatt1002.k204.tasystem.model.TeamRegister;
import no.ntnu.idatt1002.k204.tasystem.model.Tournament;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import static no.ntnu.idatt1002.k204.tasystem.dialogs.Dialogs.showAlertDialog;
import static no.ntnu.idatt1002.k204.tasystem.dialogs.Dialogs.showInformationDialog;

public class AddEligibleTeamsController implements Initializable {

    @FXML
    private TableColumn <?,?> teamNameCol;

    @FXML
    private TableColumn<?, ?> lowRankCol;

    @FXML
    private TableView<Team> teamsTableView;

    private TeamRegister teamRegister;

    private TeamDAO teamDAO;

    private ObservableList<Team> teamObservableList;

    private Tournament selectedTournament;

    private TournamentDAO tournamentDAO;

    private ArrayList<Team> eligibleTeams;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.teamNameCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));
        this.lowRankCol.setCellValueFactory(new PropertyValueFactory<>("lowestRank"));
        this.tournamentDAO = new TournamentDAO();
        this.teamRegister = new TeamRegister();
        this.teamDAO = new TeamDAO();
        this.teamDAO.getTeam(this.teamRegister);

        eligibleTeams = new ArrayList<>();
        String[] ranks = {"Unranked", "Iron", "Bronze", "Silver", "Gold", "Platinum", "Diamond", "Master", "Grandmaster", "Challenger"};
        ArrayList<String> ranksArraylist = new ArrayList();
        Collections.addAll(ranksArraylist, ranks);
        for (Team team : teamRegister.getTeams()) {
            if (tournamentDAO.getTeamsGivenTournamentId(Tournament.getSelectedTournamentID()).getTeams().stream().noneMatch(p -> p.getTeamName().equals(team.getTeamName()))) {
                if (ranksArraylist.indexOf(team.getLowestRank()) >= ranksArraylist.indexOf(tournamentDAO.getTournamentById(Tournament.getSelectedTournamentID()).getRankRequirement())) {
                    eligibleTeams.add(team);
                }
            }
        }
        updateTable();
            handleTeamSelection();
    }

    /**
     * Handles team selection
     */
    private void handleTeamSelection() {
        try{
        teamsTableView.setRowFactory(table -> {
            TableRow<Team> row = new TableRow<>();

            row.hoverProperty().addListener(observable -> {//Listen for hover on row
                Team team = row.getItem();
                if (row.isHover() && team != null) {
                    row.setOnMouseEntered(mouseEvent1 -> {//Listen when mouse is hovered over a row
                        teamsTableView.setCursor(Cursor.HAND);//Change courser
                        row.setOnMouseClicked(mouseEvent2 -> { //Listen for click event
                            selectedTournament.addTeam(team);
                            showInformationDialog(team.getTeamName() + " has been added to " + selectedTournament.getName());
                            eligibleTeams.remove(team);
                            updateTable();

                        });
                    });
                } else {
                    row.setOnMouseEntered(mouseEvent -> { //Default cursor when row is empty
                        teamsTableView.setCursor(Cursor.DEFAULT);
                    });
                }
            });

            return row;
        });}catch (IllegalArgumentException e){
            showAlertDialog(e);
        }
    }
    /**
     * Navigate back to previous scene
     */
    public void initData(Tournament tournament){
        selectedTournament = tournament;

    }

    /**
     * Handles back button clicks
     */
    @FXML
    void backBtnClicked() {
        try {
            URL fxmlLocation = getClass().getResource("/no/ntnu/idatt1002/k204/tasystem/selectedTournamentView.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent FrontPageParent = loader.load();
            SelectedTournamentController controller = loader.getController();
            controller.initData(selectedTournament);
            Stage stage = Application.stage;
            stage.getScene().setRoot(FrontPageParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the table
     */
    private void updateTable(){
        this.teamObservableList = FXCollections.observableArrayList(eligibleTeams);
        this.teamsTableView.setItems(this.teamObservableList);
    }
}
