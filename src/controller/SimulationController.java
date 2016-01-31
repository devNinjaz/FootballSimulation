package controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.GameAutomaton;
import model.Team;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by devninja on 8.1.16..
 */
public class SimulationController extends Thread implements Initializable
{
    Team teamA, teamB;
    GameAutomaton gameAutomaton;

    // Binders for GUI
    SimpleStringProperty team1Name, team2Name;

    // **************************************************************************************************
    @FXML Label lb_team1, lb_team2, lb_minute;
    @FXML ImageView img_team1, img_team2;
    @FXML Label lb_goals_team1, lb_goals_team2, lb_poss_team1, lb_poss_team2,
        lb_shots_off_target_team1, lb_shots_off_target_team2, lb_shots_on_target_team1,
        lb_shots_on_target_team2, lb_attempted_passes_team1, lb_attempted_passes_team2,
        lb_successful_passes_team1, lb_successful_passes_team2, lb_tackles_team1, lb_tackles_team2,
        lb_fouls_team1, lb_fouls_team2, lb_interceptions_team1, lb_interceptions_team2,
        lb_corners_team1, lb_corners_team2;
    @FXML TextArea ta_commentary, ta_scorers;
    @FXML Button bt_home;
    // **************************************************************************************************

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // Connect chosen teams to global vars
        teamA = Main.teamA;
        teamB = Main.teamB;

        // Creating game automaton
        gameAutomaton = new GameAutomaton(teamA, teamB);

        // Binding values to GUI elements
        bindGuiElements(gameAutomaton);

        ta_commentary.setText("Welcome to Project Aurora.\n");
        ta_commentary.appendText("Playing today: " + teamA.getTeamName() + " versus " + teamB.getTeamName() + "\n");
        ta_commentary.appendText("Starting from the center of the pitch: " + teamA.getTeamName() + "\n");

        // Starting simulation
        gameAutomaton.start();
    }

    @FXML
    private void bt_homeAction(ActionEvent event) throws IOException
    {
        Stage stage;
        Parent root;

        // We also need to kill automaton thread
        // and deference it so it doesn't take unneeded memory
        gameAutomaton.interrupt();
        gameAutomaton = null;
        stage = (Stage) bt_home.getScene().getWindow();
//        root = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        root = FXMLLoader.load(new URL(Main.PathToViews + "/MainScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void bindGuiElements(GameAutomaton gameAutomaton)
    {
        team1Name = new SimpleStringProperty(teamA.getTeamName());
        team2Name = new SimpleStringProperty(teamB.getTeamName());
        lb_team1.textProperty().bind(team1Name);
        lb_team2.textProperty().bind(team2Name);
        img_team1.setImage(teamA.getTeam_logo());
        img_team2.setImage(teamB.getTeam_logo());

        // Binds labels from GUI onto Automaton properties (used to reflect states from automaton on screen)
        gameAutomaton.initLabels(
                lb_goals_team1, lb_goals_team2, lb_poss_team1, lb_poss_team2,
                lb_shots_off_target_team1, lb_shots_off_target_team2, lb_shots_on_target_team1,
                lb_shots_on_target_team2, lb_attempted_passes_team1, lb_attempted_passes_team2,
                lb_successful_passes_team1, lb_successful_passes_team2, lb_tackles_team1, lb_tackles_team2,
                lb_fouls_team1, lb_fouls_team2, lb_interceptions_team1, lb_interceptions_team2, lb_minute,
                lb_corners_team1, lb_corners_team2
        );
        gameAutomaton.bindCommentaryBox(ta_commentary);
        gameAutomaton.bindListViewsScorers(ta_scorers);
    }
}
