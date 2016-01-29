package model;

import controller.Main;
import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.Random;

/**
 * Created by termninja on 14.1.16..
 */
public class GameAutomaton extends Thread
{
    private Team team1, team2;
    private Random rand;
    // We keep a reference to the labels at the GUI so we can update them
    private Label lb_goals_team1,  lb_goals_team2,  lb_poss_team1,  lb_poss_team2,
                      lb_shots_off_target_team1,  lb_shots_off_target_team2,  lb_shots_on_target_team1,
                      lb_shots_on_target_team2,  lb_attempted_passes_team1,  lb_attempted_passes_team2,
                      lb_successful_passes_team1,  lb_successful_passes_team2,  lb_tackles_team1,
                      lb_tackles_team2,  lb_fouls_team1,  lb_fouls_team2,  lb_interceptions_team1,
                      lb_interceptions_team2,  lb_minute;

    // Values that automaton uses
    private int goalsTeam1, goalsTeam2, possTeam1, possTeam2, shotsOffTeam1, shotsOffTeam2, shotsOnTeam1,
        shotsOnTeam2, foulsTeam1, foulsTeam2, tacklesTeam1, tacklesTeam2, succPassesTeam1, succPassesTeam2,
        attPassesTeam1, attPassesTeam2, interceptTeam1, interceptTeam2, gameMinutes = 0;

    public GameAutomaton(Team team1, Team team2)
    {
        this.team1 = team1;
        this.team2 = team2;
        initializeProperties();
        rand = new Random();
    }

    private void updateOnGui(Label someLabel, int value)
    {
        Platform.runLater(() -> {
            someLabel.setText(value + "");
        });
    }

    private void updateOnGui(Label someLabel, float value)
    {
        Platform.runLater(() -> {
            someLabel.setText(value + "");
        });
    }

    private void initializeProperties()
    {
        // Must be done on main javafx thread
        // maybe init labels from vars in this thingie
    }

    public Team getTeam1()
    {
        return team1;
    }

    public Team getTeam2()
    {
        return team2;
    }

    @Override
    public void run()
    {
        float lol;
        while (gameMinutes < 90) {
            try {
                Thread.sleep(Main.MillisecondsForOneMinute);
                Platform.runLater(() -> {
                    lb_minute.setText(gameMinutes + "");
                });
                nextMinute();
            } catch (InterruptedException e) {
                System.out.println("");
            }
        }
        System.out.println("Game has ended");
        System.out.println(team1.getTeamName() + " " + goalsTeam1 + "\t" + team2.getTeamName() + " " + goalsTeam2);
    }


    public void nextMinute()
    {
        gameMinutes++;
    }

    public void initLabels(Label lb_goals_team1, Label lb_goals_team2, Label lb_poss_team1, Label lb_poss_team2,
                     Label lb_shots_off_target_team1, Label lb_shots_off_target_team2, Label lb_shots_on_target_team1,
                     Label lb_shots_on_target_team2, Label lb_attempted_passes_team1, Label lb_attempted_passes_team2,
                     Label lb_successful_passes_team1, Label lb_successful_passes_team2, Label lb_tackles_team1,
                     Label lb_tackles_team2, Label lb_fouls_team1, Label lb_fouls_team2, Label lb_interceptions_team1,
                     Label lb_interceptions_team2, Label lb_minute)
    {
        Platform.runLater(() -> {
            this.lb_goals_team1 = lb_goals_team1;
            this.lb_poss_team1 = lb_poss_team1;
            this.lb_shots_off_target_team1 = lb_shots_off_target_team1;
            this.lb_shots_on_target_team1 = lb_shots_on_target_team1;
            this.lb_attempted_passes_team1 = lb_attempted_passes_team1;
            this.lb_successful_passes_team1 = lb_successful_passes_team1;
            this.lb_tackles_team1 = lb_tackles_team1;
            this.lb_fouls_team1 = lb_fouls_team1;
            this.lb_interceptions_team1 = lb_interceptions_team1;
            this.lb_minute = lb_minute;
            this.lb_goals_team2 = lb_goals_team2;
            this.lb_poss_team2 = lb_poss_team2;
            this.lb_shots_off_target_team2 = lb_shots_off_target_team2;
            this.lb_shots_on_target_team2 = lb_shots_on_target_team2;
            this.lb_attempted_passes_team2 = lb_attempted_passes_team2;
            this.lb_successful_passes_team2 = lb_successful_passes_team2;
            this.lb_tackles_team2 = lb_tackles_team2;
            this.lb_fouls_team2 = lb_fouls_team2;
            this.lb_interceptions_team2 = lb_interceptions_team2;
            this.lb_minute = lb_minute;
        });
    }

}
