package no.ntnu.idatt1002.k204.tasystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.ChoiceBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.stage.Stage;
import no.ntnu.idatt1002.k204.tasystem.Application;
import no.ntnu.idatt1002.k204.tasystem.controller.utils.GroupStageUtils;
import no.ntnu.idatt1002.k204.tasystem.dao.GroupDAO;
import no.ntnu.idatt1002.k204.tasystem.dao.TournamentDAO;
import no.ntnu.idatt1002.k204.tasystem.dialogs.Dialogs;
import no.ntnu.idatt1002.k204.tasystem.model.Team;
import no.ntnu.idatt1002.k204.tasystem.model.TeamRegister;
import no.ntnu.idatt1002.k204.tasystem.model.Tournament;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for group stage
 */
public class GroupStageController implements Initializable {


    @FXML
    private Button knockoutStageBtn;

    @FXML
    private Button commitGroupsBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button finishGroupstageBtn;

    @FXML
    private Label selectedText;


    @FXML
    private TreeTableColumn<Team, String> pointsColTable1;

    @FXML
    private TreeTableColumn<Team, String> pointsColTable2;

    @FXML
    private TreeTableColumn<Team, String> pointsColTable3;

    @FXML
    private TreeTableColumn<Team, String> pointsColTable4;

    @FXML
    private Button generateGroupsBtn;

    @FXML
    private TreeTableColumn<Team, String> teamColTable1;

    @FXML
    private TreeTableColumn<Team, String> teamColTable2;

    @FXML
    private TreeTableColumn<Team, String> teamColTable3;

    @FXML
    private TreeTableColumn<Team, String> teamColTable4;

    @FXML
    private TreeTableView<Team> tableView1;

    @FXML
    private TreeTableView<Team> tableView2;

    @FXML
    private TreeTableView<Team> tableView3;

    @FXML
    private TreeTableView<Team> tableView4;


    private final TreeItem<Team> table1root = new TreeItem<>();
    private final TreeItem<Team> table2root = new TreeItem<>();
    private final TreeItem<Team> table3root = new TreeItem<>();
    private final TreeItem<Team> table4root = new TreeItem<>();
    private TournamentDAO tournamentDAO;
    private GroupDAO groupDAO;
    private TeamRegister teamRegister;
    private ObservableList<String> teamNames;
    ObservableList<Team> teamObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.tournamentDAO = new TournamentDAO();
        this.groupDAO = new GroupDAO();

        this.teamRegister = this.tournamentDAO.getTeamsGivenTournamentId(Tournament.getSelectedTournamentID());
        this.teamNames = FXCollections.observableArrayList();
        this.teamObservableList = FXCollections.observableArrayList();

        String tournamentStatus= tournamentDAO.getTournamentById(Tournament.getSelectedTournamentID()).getStatus();
        knockoutStageBtn.setDisable(tournamentStatus.equals("Groupstage"));
        if(tournamentStatus.equals("Finished")){
            saveBtn.setDisable(true);
            generateGroupsBtn.setDisable(true);
            commitGroupsBtn.setDisable(true);
            finishGroupstageBtn.setDisable(true);
        }

        selectedText.setText(this.tournamentDAO.getTournamentById(Tournament.getSelectedTournamentID()).getName());
        this.teamObservableList.addAll(this.teamRegister.getTeams());

        initializeGroupTables();

        initializeTeamColumns();

        initializePointsColumns();

        addTeamNamesToTeamsList();

        addComboboxToTeamCol(this.teamNames);

        setRootInTreeTables();

        handleEditingTeamCols();


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
     * Handle event to generate groups
     */
    @FXML
    void generateBtnClicked() {
        GroupStageUtils groupStageUtils = new GroupStageUtils(this.teamRegister);

        groupStageUtils.randomize(this.table1root);
        groupStageUtils.randomize(this.table2root);
        groupStageUtils.randomize(this.table3root);
        groupStageUtils.randomize(this.table4root);
    }

    /**
     * Method for when the save button is clicked
     */
    @FXML
    void saveBtnClicked() {
        try {
            GroupStageUtils.saveGroup(this.groupDAO,"Group A", this.tableView1, Tournament.getSelectedTournamentID());
            GroupStageUtils.saveGroup(this.groupDAO,"Group B", this.tableView2, Tournament.getSelectedTournamentID());
            GroupStageUtils.saveGroup(this.groupDAO,"Group C", this.tableView3, Tournament.getSelectedTournamentID());
            GroupStageUtils.saveGroup(this.groupDAO,"Group D", this.tableView4, Tournament.getSelectedTournamentID());
        } catch (IllegalArgumentException e) {
            Dialogs.showAlertDialog(e.getMessage());
            return;
        }
        Dialogs.showInformationDialog("The group stage has been saved");
    }

    /**
     * Start tournament
     */
    @FXML
    void commitGroupsBtnClicked() {
        setNotEditableTeamCols();
        setPointsColumnsAsEditable();
        generateGroupsBtn.setDisable(true);
        commitGroupsBtn.setDisable(true);
    }

    /**
     * Method for when the 'Finish group stage' button is clicked
     */
    @FXML
    void finishGroupstageBtnClicked(){
        try {
            tournamentDAO.updateTournamentStatus(Tournament.getSelectedTournamentID(), "Knockoutstage");
            Application.changeScene("knockOutStageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for when the 'teams' button is clicked
     */
    @FXML
    void teamsBtnClicked() {
        try {
            URL fxmlLocation = getClass().getResource("/no/ntnu/idatt1002/k204/tasystem/selectedTournamentView.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent FrontPageParent = loader.load();
            SelectedTournamentController controller = loader.getController();
            controller.initData(tournamentDAO.getTournamentById(Tournament.getSelectedTournamentID()));
            Stage stage = Application.stage;
            stage.getScene().setRoot(FrontPageParent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize group tables
     */
    private void initializeGroupTables() {
        GroupStageUtils.initWithTeamsFromDatabase(this.groupDAO, "Group A", Tournament.getSelectedTournamentID(), this.table1root);
        GroupStageUtils.initWithTeamsFromDatabase(this.groupDAO, "Group B", Tournament.getSelectedTournamentID(), this.table2root);
        GroupStageUtils.initWithTeamsFromDatabase(this.groupDAO, "Group C", Tournament.getSelectedTournamentID(), this.table3root);
        GroupStageUtils.initWithTeamsFromDatabase(this.groupDAO, "Group D", Tournament.getSelectedTournamentID(), this.table4root);
    }

    /**
     * Set root in tree tables
     */
    private void setRootInTreeTables() {
        setRootInTableView(this.tableView1, this.table1root);
        setRootInTableView(this.tableView2, this.table2root);
        setRootInTableView(this.tableView3, this.table3root);
        setRootInTableView(this.tableView4, this.table4root);
    }

    /**
     * Set root in table view
     */
    private void setRootInTableView(TreeTableView<Team> tableView, TreeItem<Team> root) {
        tableView.setRoot(root);
        tableView.setShowRoot(false);
    }

    /**
     * Set not editable team columns
     * When start button is clicked, don't allow editing teams.
     */
    private void setNotEditableTeamCols() {
        this.teamColTable1.setEditable(false);
        this.teamColTable2.setEditable(false);
        this.teamColTable3.setEditable(false);
        this.teamColTable4.setEditable(false);
    }

    /**
     * Set points columns as editable
     * Allow editing points column when start is clicked.
     */
    private void setPointsColumnsAsEditable() {
        this.pointsColTable1.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        this.pointsColTable2.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        this.pointsColTable3.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        this.pointsColTable4.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
    }

    /**
     * Add team names to teams list
     * Get teams from the team register, add them to an observable list which is used in the combo boxes.
     */
    private void addTeamNamesToTeamsList() {
        for (Team team : this.teamRegister.getTeams()) {
            this.teamNames.add(team.getTeamName());
        }
    }

    /**
     * Add combobox to team column
     * @param teamNames the team names
     */
    private void addComboboxToTeamCol(ObservableList<String> teamNames) {
        this.teamColTable1.setCellFactory(ChoiceBoxTreeTableCell.forTreeTableColumn(teamNames));
        this.teamColTable2.setCellFactory(ChoiceBoxTreeTableCell.forTreeTableColumn(teamNames));
        this.teamColTable3.setCellFactory(ChoiceBoxTreeTableCell.forTreeTableColumn(teamNames));
        this.teamColTable4.setCellFactory(ChoiceBoxTreeTableCell.forTreeTableColumn(teamNames));
    }

    /**
     * Handle editing team columns
     */
    private void handleEditingTeamCols() {
        chooseAndSetTeam(this.teamColTable1, this.tableView1);
        chooseAndSetTeam(this.teamColTable2, this.tableView2);
        chooseAndSetTeam(this.teamColTable3, this.tableView3);
        chooseAndSetTeam(this.teamColTable4, this.tableView4);
    }

    /**
     * Choose and set team
     */
    private void chooseAndSetTeam(TreeTableColumn<Team, String> teamCol, TreeTableView<Team> tableView) {
        //Get column that is being edited
        teamCol.setOnEditCommit(event -> {
            //Get name that has been chosen from combobox
            TreeItem<Team> currentlyChosen = tableView.getTreeItem(event.getTreeTablePosition().getRow());
            //Set name that has been chosen to the cell. Or else it is lost when tournament is started.
            currentlyChosen.getValue().setTeamName(event.getNewValue());
        });
    }

    /**
     * Initialize team columns
     */
    private void initializeTeamColumns() {
        this.teamColTable1.setCellValueFactory((TreeTableColumn.CellDataFeatures<Team, String> param) -> param.getValue().getValue().teamNameProperty());
        this.teamColTable2.setCellValueFactory((TreeTableColumn.CellDataFeatures<Team, String> param) -> param.getValue().getValue().teamNameProperty());
        this.teamColTable3.setCellValueFactory((TreeTableColumn.CellDataFeatures<Team, String> param) -> param.getValue().getValue().teamNameProperty());
        this.teamColTable4.setCellValueFactory((TreeTableColumn.CellDataFeatures<Team, String> param) -> param.getValue().getValue().teamNameProperty());
    }

    /**
     * Initialize points columns
     */
    private void initializePointsColumns() {
        this.pointsColTable1.setCellValueFactory((TreeTableColumn.CellDataFeatures<Team, String> param) -> param.getValue().getValue().pointsProperty());
        this.pointsColTable2.setCellValueFactory((TreeTableColumn.CellDataFeatures<Team, String> param) -> param.getValue().getValue().pointsProperty());
        this.pointsColTable3.setCellValueFactory((TreeTableColumn.CellDataFeatures<Team, String> param) -> param.getValue().getValue().pointsProperty());
        this.pointsColTable4.setCellValueFactory((TreeTableColumn.CellDataFeatures<Team, String> param) -> param.getValue().getValue().pointsProperty());
    }
}
