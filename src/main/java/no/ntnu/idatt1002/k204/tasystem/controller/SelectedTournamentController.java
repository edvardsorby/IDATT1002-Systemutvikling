package no.ntnu.idatt1002.k204.tasystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import no.ntnu.idatt1002.k204.tasystem.Application;
import no.ntnu.idatt1002.k204.tasystem.model.Team;
import no.ntnu.idatt1002.k204.tasystem.model.TeamRegister;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for handling events when tournament is selected
 */
public class SelectedTournamentController implements Initializable {

    @FXML
    private Button addEligibleTeamBtn;

    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<?, ?> countPlayersCol;

    @FXML
    private Button groupStageBtn;

    @FXML
    private Button knockoutStageBtn;

    @FXML
    private TableColumn<?, ?> teamNameCol;

    @FXML
    private Button teamsBtn;

    @FXML
    private TableView<Team> teamTableView;

    private TeamRegister teamRegister;

    private ObservableList<Team> teamObservableList;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //TODO
        // ADD ME
        // - 1. Connect table cells/columns to team fields
        // - 2. Get teams from database to display
        //      Needs:
        //          - Tournaments register
        //          - Observable list
    }

    /**
     * Change to add eligible team scene and start adding teams
     */
    @FXML
    void addEligibleTeamBtnClicked() {
        try {
            Application.changeScene("addEligibleTeams.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Navigate back to previous scene
     */
    @FXML
    void backBtnClicked() {
        try {
            Application.changeScene("frontPageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Change to group stage scene
     */
    @FXML
    void groupStageBtnClicked() {
        //TODO
        // ADD ME Change to group stage scene
    }

    /**
     * Change to knockout stage scene
     */
    @FXML
    void knockoutStageBtnClicked() {
        //TODO
        // ADD ME Change to knockout stage scene
    }

}
