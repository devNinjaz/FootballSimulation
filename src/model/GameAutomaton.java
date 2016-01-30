package model;

import controller.DuelsEvaluator;
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
    private RandomUtil rand;
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
    private int attemptedDribbles1, attemptedDribbles2, successfulDribbles1, successfulDribbles2, corners1, corners2;

    // States automaton uses
    enum state {
        initialState, halfTimeState, finalState,
        dribbleA, possessionA, cornerA, passA, shootA, goalA, goalKickA,
        dribbleB, possessionB, cornerB, passB, shootB, goalB, goalKickB
    }
    Player magicPlayer;                                 // Player who was in action
    private state automatonState;                       //  set in constructor
    private DuelsEvaluator duelsEvaluator;              // Object that brings goodies and evaluates duels

    public GameAutomaton(Team team1, Team team2)
    {
        this.team1 = team1;
        this.team2 = team2;
        initializeProperties();
        automatonState = state.initialState;  // setting initial automaton state
        duelsEvaluator = new DuelsEvaluator();
        rand = new RandomUtil();
    }

    private void updateOnGui(Label someLabel, int value)
    {
        // Update of GUI labels is done on JavaFX main thread
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
                simulate(rand.getFromInterval(8, 50));
            } catch (InterruptedException e) {
                System.out.println("");
            }
        }
        System.out.println("Game has ended");
        System.out.println(team1.getTeamName() + " " + goalsTeam1 + "\t" + team2.getTeamName() + " " + goalsTeam2);
    }


    // Moves automaton into next state
    private void simulate(int n)
    {
        float res;
        switch (automatonState) {
           case initialState:
               // At the start of game, teamA has the ball
               automatonState = state.possessionA;
               showCommentary(automatonState, null, null, 0f);
               break;
           case halfTimeState:
               // At half time, teamB has the ball
               automatonState = state.possessionB;
               break;
           case possessionA:
               // TODO implement logic that decides WHAT will team do
               // shoot, pass, cross, dribble...
               handlePossessionState(automatonState);
               break;
           case goalA:
               goalsTeam1++;
               automatonState = state.possessionB;
               updateOnGui(lb_goals_team1, goalsTeam1);
               break;
           case dribbleA:
               Player dribbler = team1.getDribbler();
               Player opposition;
               res = rand.runif();
               if (res <= 0.5f) opposition = team2.getMidfielder();
               else opposition = team2.getDefender();
               res = duelsEvaluator.tryDribbling(
                       dribbler.getStats().get("dribbling"),
                       dribbler.getStats().get("speed"),
                       dribbler.getStats().get("explosive_power"),
                       opposition.getStats().get("body_balance"),
                       opposition.getStats().get("defensive_prowess"),
                       opposition.getStats().get("ball_winning")
               );
               if (res == DuelsEvaluator.successfulEvent) {
                   // Successful dribble!
                   attemptedDribbles1++;
                   successfulDribbles1++;
                   showCommentary(automatonState, dribbler, opposition, res);
               } else {
                    attemptedDribbles1++;
                   showCommentary(automatonState, dribbler, opposition, res);
               }
               break;
            case shootA:
                // we split chances for shot takers the following way:
                Player shooter = team1.getShooter();
                Player goalkeeper = team2.getGoalkeeper();
                res = duelsEvaluator.tryShooting(
                        shooter.getStats().get("attacking_prowess"),
                        shooter.getStats().get("kicking_power"),
                        shooter.getStats().get("finishing"),
                        goalkeeper.getStats().get("overall")
                );
                if (res == DuelsEvaluator.successfulEvent) {
                    // We have a goal!
                    System.out.println("GOAL for " + team1.getTeamName() + " scored by: " + shooter.getFullPlayerName());
                    automatonState = state.goalA;
                    goalsTeam1++;
                    shotsOnTeam1++;
                } else {
                    // Player failed to score
                    // 60 - 40 split on winning ball back
                    shotsOffTeam1++;
                    res = rand.runif();
                    if (res <= 0.4) {
                        automatonState = state.possessionA;
                    } else {
                       automatonState = state.possessionB;
                    }
                }
                break;
            case cornerA:
                Player setPieceTaker = team1.getSetPieceTaker();
                res = duelsEvaluator.tryCorner(setPieceTaker.getStats().get("place_kicking"));
                corners1++;
                if (res == DuelsEvaluator.successfulEvent) {
                    // Team scored from corner
                    goalsTeam1++;
                    automatonState = state.goalA;           // STATE SWITCH
                } else {
                    // if no goal, 50/50 for ball winning by both sides
                    res = rand.runif();
                    if (res <= 0.5f) {
                        automatonState = state.possessionA;
                    } else {
                        automatonState = state.possessionB;
                    }
                }
                break;
           default:
               System.out.println("Not initial state");
       }
    }

    private void handlePossessionState(state automatonState)
    {
        float probabilityToPass = 0.7f;
        float probabilityToDribble = 0.20f;
        float probabilityToShot = 0.1f;             // not used, I've put it just for info
        float bacon = rand.runif();
        if (bacon <= probabilityToPass) {
            // We make a pass
            automatonState = (automatonState == state.possessionA ) ? state.passA : state.passB;
        } else if (bacon <= probabilityToPass + probabilityToDribble) {
            // We make a dribble
            automatonState = (automatonState == state.possessionA ) ? state.dribbleA : state.dribbleB;
        } else {
            // We make a shot
            automatonState = (automatonState == state.possessionA) ? state.shootA : state.shootB;
        }
    }

    // TODO decide on the fate of this function
    private void showCommentary(state automatonState, Player homie1, Player homie2, float res)
    {
        String msg = "";
        switch (automatonState) {
            case initialState:
                msg = "Game has begun.";
                break;
            case halfTimeState:
                msg = "Half time. Result is: " + team1.getTeamName() + " " + goalsTeam1
                        + " : " + goalsTeam2 + " " + team2.getTeamName();
                break;
            case finalState:
                msg = "End of game. Result is: " + team1.getTeamName() + " " + goalsTeam1
                        + " : " + goalsTeam2 + " " + team2.getTeamName();
                break;
            case goalA:
                msg = "GOAL for " + team1.getTeamName();
                msg = "\nScored by: " + homie1.getFullPlayerName();
                break;
            case dribbleA:
                msg = "DRIBBLE from " + homie1.getPlayerName() + " " + homie1.getPlayerSurname()
                        + " against " + homie2.getFullPlayerName() + ": ";
                if (res == DuelsEvaluator.successfulEvent)
                    msg += "SUCCESS";
                else
                    msg += "FAILED";
                break;
        }

        // TODO Bind to some textBox on gui
        System.out.println(msg);
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
