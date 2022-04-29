package no.ntnu.idatt1002.k204.tasystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import no.ntnu.idatt1002.k204.tasystem.Application;
import no.ntnu.idatt1002.k204.tasystem.dao.TournamentDAO;
import no.ntnu.idatt1002.k204.tasystem.dialogs.Dialogs;
import no.ntnu.idatt1002.k204.tasystem.model.Team;
import no.ntnu.idatt1002.k204.tasystem.model.TeamRegister;
import no.ntnu.idatt1002.k204.tasystem.model.Tournament;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

/**
 * Controller for handling the knockout stage
 */
public class KnockoutStageController implements Initializable {

    @FXML
    private Label selectedText;

    @FXML
    private Text quarterFinalTeam1Txt;

    @FXML
    private Text quarterFinalTeam2Txt;

    @FXML
    private Text quarterFinalTeam3Txt;

    @FXML
    private Text quarterFinalTeam4Txt;

    @FXML
    private Text quarterFinalTeam5Txt;

    @FXML
    private Text quarterFinalTeam6Txt;

    @FXML
    private Text quarterFinalTeam7Txt;

    @FXML
    private Text quarterFinalTeam8Txt;

    @FXML
    private Text semiFinalTeam1Txt;

    @FXML
    private Text semiFinalTeam2Txt;

    @FXML
    private Text semiFinalTeam3Txt;

    @FXML
    private Text semiFinalTeam4Txt;

    @FXML
    private Text finalTeam1Txt;

    @FXML
    private Text finalTeam2Txt;

    @FXML
    private Text winnerTeamTxt;

    @FXML
    private RadioButton quarterFinalTeam1Btn;

    @FXML
    private RadioButton quarterFinalTeam2Btn;

    @FXML
    private RadioButton quarterFinalTeam3Btn;

    @FXML
    private RadioButton quarterFinalTeam4Btn;

    @FXML
    private RadioButton quarterFinalTeam5Btn;

    @FXML
    private RadioButton quarterFinalTeam6Btn;

    @FXML
    private RadioButton quarterFinalTeam7Btn;

    @FXML
    private RadioButton quarterFinalTeam8Btn;

    @FXML
    private RadioButton semiFinalTeam1Btn;

    @FXML
    private RadioButton semiFinalTeam2Btn;

    @FXML
    private RadioButton semiFinalTeam3Btn;

    @FXML
    private RadioButton semiFinalTeam4Btn;

    @FXML
    private RadioButton finalTeam1Btn;

    @FXML
    private RadioButton finalTeam2Btn;

    @FXML
    private Button finishTournamentBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button groupStageBtn;

    @FXML
    private Text tournamentWinnerTxt;

    private TournamentDAO tournamentDAO;
    private Tournament tournament;
    private TeamRegister teamRegister;

    private int numberOfTeams;

    /**
     * List containing all matches and teams in the knockout stage
     *
     * The knockout stage consists of seven matches: four quarterfinals, two semifinals and one final.
     * Each row in the list represents a match, and consists of three columns.
     * Column 0 and 1 contains the two teams participating in the match.
     * Column 2 contains the winner of the match.
     * If any cells are empty (null), the team/match has not been decided yet.
     * Rows 0-3 are quarterfinals, 4-5 are semifinals and row 6 is the grand final.
     */
    private final Team[][] matches = new Team[7][3];


    /**
     * Initializes the knockout stage
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.teamRegister = new TeamRegister();
        this.tournamentDAO = new TournamentDAO();

        this.tournament = tournamentDAO.getTournamentById(Tournament.getSelectedTournamentID());
        this.teamRegister = tournamentDAO.getTeamsGivenTournamentId(Tournament.getSelectedTournamentID());

        numberOfTeams = tournamentDAO.getTeamsGivenTournamentId(Tournament.getSelectedTournamentID()).getTeams().size();

        selectedText.setText(tournament.getName());

        initializeMatches();

        redrawBrackets();

    }

    /**
     * Initializes the 'matches' list by retrieving it from the database.
     *
     * If the list hasn't been filled with teams yet (i.e. the first team is null),
     * it will be filled with the 8 teams from the group stage with the most points.
     */
    private void initializeMatches() {

        tournamentDAO.getKnockoutMatches(Tournament.getSelectedTournamentID(), matches);

        if (matches[0][0] == null){
            ArrayList<Team> sortedList = new ArrayList<>(teamRegister.getTeams());

            sortedList.sort(Comparator.comparing(Team::getPointsAsInt));

            for (int i = 0; i < 8; i++) {
                matches[i/2][i%2] = sortedList.get(sortedList.size()-1-i);
            }
        }
    }

    /**
     * Fills in all text fields and radio buttons to correspond with the current status of the knockout stage
     */
    private void redrawBrackets() {

        /* Fills in team names in the right places: */

        Text[] text = new Text[]{
                quarterFinalTeam1Txt, quarterFinalTeam2Txt, quarterFinalTeam3Txt, quarterFinalTeam4Txt,
                quarterFinalTeam5Txt, quarterFinalTeam6Txt, quarterFinalTeam7Txt, quarterFinalTeam8Txt,
                semiFinalTeam1Txt, semiFinalTeam2Txt, semiFinalTeam3Txt, semiFinalTeam4Txt,
                finalTeam1Txt, finalTeam2Txt, winnerTeamTxt
        };

        // The 7 matches:
        for (int i = 0; i < 14; i++) setTextToTeamName(text[i], matches[i/2][i%2]);

        // The final winner:
        setTextToTeamName(text[14], matches[6][2]);


        /* Selects the correct radio buttons: */

        RadioButton[] buttons = new RadioButton[]{
                quarterFinalTeam1Btn, quarterFinalTeam2Btn, quarterFinalTeam3Btn, quarterFinalTeam4Btn,
                quarterFinalTeam5Btn, quarterFinalTeam6Btn, quarterFinalTeam7Btn, quarterFinalTeam8Btn,
                semiFinalTeam1Btn, semiFinalTeam2Btn, semiFinalTeam3Btn, semiFinalTeam4Btn, finalTeam1Btn, finalTeam2Btn
        };

        for (int i = 0, j = 0; i < matches.length; i++, j+=2) {
            if (matches[i][2] != null) {
               if (matches[i][0].getTeamName().equals(matches[i][2].getTeamName())) {
                   buttons[j].setSelected(true);
               } else if (matches[i][1].getTeamName().equals(matches[i][2].getTeamName())) {
                   buttons[j+1].setSelected(true);
               }
            }
        }


        /* Disables the radio buttons that are currently not in use: */

        // The 8 quarterfinal radio buttons:
        for (int i = 0; i < 8; i++) buttons[i].setDisable(matches[(i<4) ? 4 : 5][2] != null);

        // The semifinal buttons:
        if (matches[6][2] == null) {

            if (matches[0][2] != null && matches[1][2] != null) {
                for (int i = 8; i < 10; i++) buttons[i].setDisable(false);
            }
            if (matches[2][2] != null && matches[3][2] != null) {
                for (int i = 10; i < 12; i++) buttons[i].setDisable(false);
            }

        } else {
            for (int i = 8; i < 12; i++) buttons[i].setDisable(true);
        }

        // The final radio buttons:
        if (!tournament.getStatus().equals("Finished")) {
            if (matches[4][2] != null && matches[5][2] != null)
                for (int i = 12; i < 14; i++) buttons[i].setDisable(false);
            if (buttons[12].isSelected() || buttons[13].isSelected())
                finishTournamentBtn.setDisable(false);
        }


        /* Additional adjustments if the tournament is finished: */

        if (tournament.getStatus().equals("Finished")) {
            tournamentWinnerTxt.setVisible(true);
            finishTournamentBtn.setDisable(true);
            saveBtn.setDisable(true);

            for (RadioButton button : buttons) button.setDisable(true);

            if (numberOfTeams > 8) groupStageBtn.setDisable(false);
        }
    }

    /**
     * Decides what happens when either of the quarterfinal buttons are clicked
     */
    @FXML
    public void quarterFinalBtnClicked(ActionEvent event) {
        int id = Integer.parseInt(((Node) event.getTarget()).getId().substring(16,17))-1;

        matches[id/2][2] = matches[(id<4) ? 4 : 5][(id/2)%2] = matches[id/2][id%2];

        redrawBrackets();
    }

    /**
     * Decides what happens when either of the semifinal buttons are clicked
     */
    @FXML
    public void semiFinalBtnClicked(ActionEvent event) {
        int id = Integer.parseInt(((Node) event.getTarget()).getId().substring(13,14))+7;

        matches[(id<10) ? 4 : 5][2] = matches[6][(id<10) ? 0 : 1] = matches[(id<10) ? 4 : 5][id%2];

        redrawBrackets();
    }

    /**
     * Sets a winner of the tournament
     */
    public void finalBtnClicked(ActionEvent event) {
        int id = Integer.parseInt(((Node) event.getTarget()).getId().substring(9,10))+11;

        matches[6][2] = matches[6][id%2];

        redrawBrackets();
    }

    /**
     * Saves the current knockout stage progress in the database
     */
    @FXML
    void saveBtnClicked() {
        tournamentDAO.updateKnockoutMatches(Tournament.getSelectedTournamentID(), matches);

        Dialogs.showInformationDialog("The knockout stage has been saved!");
    }

    /**
     * Finishes the tournament by updating its status
     */
    @FXML
    void finishTournamentBtnClicked() {

        tournament.setStatus("Finished");
        tournamentDAO.updateTournamentStatus(tournament.getTournamentID(), tournament.getStatus());

        redrawBrackets();

        saveBtnClicked();
    }

    /**
     * Method used to set a team's name to a Text field in the knockout stage.
     * @param text the text field where the team name will be displayed
     * @param team the team to be displayed. If the team is null, no text will be displayed
     */
    private void setTextToTeamName(Text text, Team team) {
        if (team != null) text.setText(team.getTeamName());
    }

    /**
     * Navigate back to the front page
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
     * Handle events when knockout stage button is clicked
     */
    @FXML
    void knockoutStageBtnClicked() {
        try {
            Application.changeScene("knockOutStageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Change to group stage scene
     */
    @FXML
    void groupStageBtnClicked() {
        try {
            Application.changeScene("groupStageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates back to the teams-page
     */
    @FXML
    void teamsBtnClicked() {
        try {
            URL fxmlLocation = getClass().getResource("/no/ntnu/idatt1002/k204/tasystem/selectedTournamentView.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent FrontPageParent = loader.load();
            SelectedTournamentController controller = loader.getController();
            controller.initData(tournament);
            Stage stage = Application.stage;
            stage.getScene().setRoot(FrontPageParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}