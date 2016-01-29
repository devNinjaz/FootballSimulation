package controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.GameAutomaton;
import model.Team;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by devninja on 8.1.16..
 */
public class SimulationController extends Thread implements Initializable
{
    Team teamA, teamB;
    GameAutomaton gameAutomaton;
    @FXML Label lb_team1, lb_team2, lb_minute;
    @FXML ImageView img_team1, img_team2;

    // Stats
    @FXML Label lb_goals_team1, lb_goals_team2, lb_poss_team1, lb_poss_team2,
        lb_shots_off_target_team1, lb_shots_off_target_team2, lb_shots_on_target_team1,
        lb_shots_on_target_team2, lb_attempted_passes_team1, lb_attempted_passes_team2,
        lb_successful_passes_team1, lb_successful_passes_team2, lb_tackles_team1, lb_tackles_team2,
        lb_fouls_team1, lb_fouls_team2, lb_interceptions_team1, lb_interceptions_team2;

    // Binders for GUI
    SimpleStringProperty team1Name, team2Name;

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

        // Starting simulation
        gameAutomaton.start();

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
                lb_fouls_team1, lb_fouls_team2, lb_interceptions_team1, lb_interceptions_team2, lb_minute
        );
    }
}
