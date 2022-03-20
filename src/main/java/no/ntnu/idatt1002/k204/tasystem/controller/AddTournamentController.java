package no.ntnu.idatt1002.k204.tasystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import no.ntnu.idatt1002.k204.tasystem.Application;

import java.io.IOException;

/**
 * Controller for adding tournaments
 */
public class AddTournamentController {

    @FXML
    private Button AddTournamentBtn;

    @FXML
    private Button backBtn;

    @FXML
    private TextField dateTimeTextField;

    @FXML
    private Button groupKnockoutBtn;

    @FXML
    private Button knockoutBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField requirementsTextfield;

    /**
     * Handle add tournament events
     *
     * Add tournament to database
     */
    @FXML
    void addTournamentBtnClicked() {
        //TODO
        // ADD ME: Add tournament to database
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
     * Toggle group + knockout option
     */
    @FXML
    void groupKnockoutBtnClicked() {
        //TODO
        // ADD ME: Toggle group + knockout option (maybe use radio buttons here)
    }

    /**
     * Toggle knockout option
     */
    @FXML
    void knockoutBtnClicked() {
        //TODO
        // ADD ME: Toggle knockout option (maybe use radio buttons here)
    }

    /**
     * Handle logout event.
     *
     * Logout and send back to log in screen
     */
    @FXML
    void logoutBtnClicked() {
        try {
            Application.logout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
