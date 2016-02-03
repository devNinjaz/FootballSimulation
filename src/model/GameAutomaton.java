package model;

import controller.DuelsEvaluator;
import controller.Main;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.util.List;

/**
 * Created by termninja on 14.1.16..
 */
public class GameAutomaton extends Thread
{
    private Team team1, team2;
    private RandomUtil rand;

    // ***************************************************************************************************************
    // We keep a reference to the labels at the GUI so we can update them
    private Label lb_goals_team1,  lb_goals_team2,  lb_poss_team1,  lb_poss_team2,
                      lb_shots_off_target_team1,  lb_shots_off_target_team2,  lb_shots_on_target_team1,
                      lb_shots_on_target_team2,  lb_attempted_passes_team1,  lb_attempted_passes_team2,
                      lb_successful_passes_team1,  lb_successful_passes_team2,  lb_tackles_team1,
                      lb_tackles_team2,  lb_fouls_team1,  lb_fouls_team2,  lb_interceptions_team1,
                      lb_interceptions_team2,  lb_minute, lb_corners_team1, lb_corners_team2;
    private TextArea ta_commentary, ta_scorers;
    // ***************************************************************************************************************

    // Values that automaton uses
    private int goalsTeam1, goalsTeam2, possTeam1, possTeam2, shotsOffTeam1, shotsOffTeam2, shotsOnTeam1,
        shotsOnTeam2, foulsTeam1, foulsTeam2, tacklesTeam1, tacklesTeam2, succPassesTeam1, succPassesTeam2,
        attPassesTeam1, attPassesTeam2, interceptTeam1, interceptTeam2, gameMinutes = 0;
    private int attemptedDribbles1, attemptedDribbles2, successfulDribbles1, successfulDribbles2, corners1, corners2;

    private float probabilityCorner, probabilityInterception, probabilityFoul;
    private int automatonIterations, teamAIterations, teamBIterations;

    public void bindCommentaryBox(TextArea ta_commentary)
    {
        this.ta_commentary = ta_commentary;
    }

    public void bindListViewsScorers(TextArea ta_scorers)
    {
        this.ta_scorers = ta_scorers;
    }


    // States automaton uses
    enum state {
        initialState, halfTimeState, finalState,
        dribbleA, possessionA, cornerA, passA, shootA, goalA, goalKickA,
        dribbleB, possessionB, cornerB, passB, shootB, goalB, goalKickB
    }
    private state automatonState;                       //  set in constructor
    private DuelsEvaluator duelsEvaluator;              // Object that brings goodies and evaluates duels

    public GameAutomaton(Team team1, Team team2)
    {
        this.team1 = team1;
        this.team2 = team2;
        initializeProperties();
        automatonState = state.initialState;            // setting initial automaton state
        duelsEvaluator = new DuelsEvaluator();
        rand = new RandomUtil();

        probabilityCorner = rand.getFromInterval(15, 26) * 1.0f / 100.0f;
        probabilityInterception = rand.getFromInterval(15, 30) * 1.0f / 100.0f;
        probabilityFoul = rand.getFromInterval(15, 30) * 1.0f / 100.0f;

        // Used for calculating team possession
        // TODO check if it's counted correctly
        automatonIterations = 1;
        teamAIterations = 1;
        teamBIterations = 1;
    }

    private void refreshGui()
    {
        updateOnGui(lb_goals_team1, goalsTeam1);
        updateOnGui(lb_attempted_passes_team1, attPassesTeam1);
        updateOnGui(lb_successful_passes_team1, succPassesTeam1);
        updateOnGui(lb_fouls_team1, foulsTeam1);
        updateOnGui(lb_interceptions_team1, interceptTeam1);
        updateOnGui(lb_poss_team1, Math.round((teamAIterations * 1.0f / automatonIterations) * 100.0f));
        updateOnGui(lb_shots_on_target_team1, shotsOnTeam1);
        updateOnGui(lb_shots_off_target_team1, shotsOffTeam1);
        updateOnGui(lb_tackles_team1, tacklesTeam1);
        updateOnGui(lb_goals_team2, goalsTeam2);
        updateOnGui(lb_attempted_passes_team2, attPassesTeam2);
        updateOnGui(lb_successful_passes_team2, succPassesTeam2);
        updateOnGui(lb_fouls_team2, foulsTeam2);
        updateOnGui(lb_interceptions_team2, interceptTeam2);
        updateOnGui(lb_poss_team2, 100 - Math.round((teamAIterations * 1.0f / automatonIterations) * 100.0f));
        updateOnGui(lb_shots_on_target_team2, shotsOnTeam2);
        updateOnGui(lb_shots_off_target_team2, shotsOffTeam2);
        updateOnGui(lb_tackles_team2, tacklesTeam2);
        updateOnGui(lb_minute, gameMinutes);
        updateOnGui(lb_corners_team1, corners1);
        updateOnGui(lb_corners_team2, corners2);
        updateOnGui(ta_commentary, DuelsEvaluator.CommentaryMsg);
    }

    private void updateOnGui(Label someLabel, int value)
    {
        // Update of GUI labels is done on JavaFX main thread
        Platform.runLater(() -> {
            someLabel.setText(value + "");
        });
    }

    private void updateOnGui(TextArea commentary, String msg)
    {
        Platform.runLater(() -> {
            commentary.appendText(msg + "\n");
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
        while (gameMinutes < 90) {
            try {
                Thread.sleep(Main.MillisecondsForOneMinute);
                Platform.runLater(() -> {
                    lb_minute.setText(gameMinutes + "");
                });
                nextMinute();
                simulate(rand.getFromInterval(2, 9));
            } catch (InterruptedException e) {
                System.out.println("");
            }
        }
        System.out.println("Game has ended");
        System.out.println(team1.getTeamName() + " " + goalsTeam1 + "\t" + team2.getTeamName() + " " + goalsTeam2);
    }


    // Moves automaton into next state n times

    private void simulate(int n)
    {
        if (gameMinutes == 90) {
            automatonState = state.finalState;
            return;
        } else if (gameMinutes == 45) {
            automatonState = state.halfTimeState;
            System.out.println("Half time!");
            return;
        }

        for (int i = 0; i < n; i++) {
            automatonIterations++;
            switch (automatonState) {

                // INITIAL
                case initialState:
                    // At the start of game, teamA has the ball
                    automatonState = state.possessionA;
                    break;

                // HALF TIME
                case halfTimeState:
                    // At half time, teamB has the ball
                    automatonState = state.possessionB;
                    break;

                // POSSESSION (most important, controls most of the automaton)
                case possessionA:
                    automatonState = handlePossessionState(automatonState);           // switches global variable automatonState
                    break;
                case possessionB:
                    automatonState = handlePossessionState(automatonState);
                    break;

                // GOALS
                case goalA:
                    handleGoalAction(1);
                    break;
                case goalB:
                    handleGoalAction(2);
                    break;

                // DRIBBLING
                case dribbleA:
                    handleDribbleAction(1);
                    break;
                case dribbleB:
                    handleDribbleAction(2);
                    break;

                // PASSING
                case passA:
                    handlePassingAction(1);
                    break;
                case passB:
                    handlePassingAction(2);
                    break;

                // SHOOTING
                case shootA:
                    handleShootingAction(1);
                    break;
                case shootB:
                    handleShootingAction(2);
                    break;

                // CORNERS
                case cornerA:
                    handleCornerAction(1);
                    break;
                case cornerB:
                    handleCornerAction(2);
                    break;

                default:
                    System.out.println("STUCK ON DEFAULT: Current state:" + automatonState);
                    return;
            }
        }
        refreshGui();
    }

    private void handleCornerAction(int teamNumber)
    {
        if (teamNumber == 1) {
            teamAIterations++;
            Player setPieceTaker = team1.getSetPieceTaker();
            float res = duelsEvaluator.tryCorner(setPieceTaker);
            corners1++;
            if (res == DuelsEvaluator.successfulEvent) {
                // Team scored from corner
                shotsOnTeam1++;
                automatonState = state.goalA;           // STATE SWITCH
                duelsEvaluator.setLastScorer(team1.getDefender());
            } else {
                // if no goal, 50/50 for ball winning by both sides
                res = rand.runif();
                if (res <= 0.5f) {
                    automatonState = state.possessionA;
                } else {
                    automatonState = state.possessionB;
                }
            }
//            System.out.println("CORNER A");

        } else {
            teamBIterations++;
            Player setPieceTaker = team2.getSetPieceTaker();
            float res = duelsEvaluator.tryCorner(setPieceTaker);
            corners2++;
            if (res == DuelsEvaluator.successfulEvent) {
                // Team scored from corner
                shotsOnTeam2++;
                automatonState = state.goalB;           // STATE SWITCH
                duelsEvaluator.setLastScorer(team2.getDefender());
            } else {
                // if no goal, 50/50 for ball winning by both sides
                res = rand.runif();
                if (res <= 0.5f) {
                    automatonState = state.possessionB;
                } else {
                    automatonState = state.possessionA;
                }
            }
//            System.out.println("CORNER B");
        }
    }

    private void handleShootingAction(int teamNumber)
    {
        if (teamNumber == 1) {
            teamAIterations++;
            Player shooter = team1.getShooter();
            Player goalkeeper = team2.getGoalkeeper();
            float res = duelsEvaluator.tryShooting(shooter, goalkeeper);
            if (res == DuelsEvaluator.successfulEvent) {
                // We have a goal!
                System.out.println("GOAL for " + team1.getTeamName() + " scored by: " + shooter.getFullPlayerName());
                duelsEvaluator.setLastScorer(shooter);
                automatonState = state.goalA;
                shotsOnTeam1++;
            } else {
                teamBIterations++;
                // Player failed to score
                // odds for corner: 40%
                // odds for losing possession: 40%
                // odds for retaining possesion: 20%
                shotsOffTeam1++;
                res = rand.runif();
                if (res <= 0.4f) {
                    automatonState = state.cornerA;
                } else if (res <= 0.4f + 0.4f) {
                    automatonState = state.possessionB;
                } else {
                    automatonState = state.possessionA;
                }
            }
        } else {
            teamBIterations++;
            Player shooter = team2.getShooter();
            Player goalkeeper = team1.getGoalkeeper();
            float res = duelsEvaluator.tryShooting(shooter, goalkeeper);
            if (res == DuelsEvaluator.successfulEvent) {
                // We have a goal!
                System.out.println("GOAL for " + team2.getTeamName() + " scored by: " + shooter.getFullPlayerName());
                duelsEvaluator.setLastScorer(shooter);
                automatonState = state.goalB;
                shotsOnTeam2++;
            } else {
                // Player failed to score
                // odds for corner: 40%
                // odds for losing possession: 40%
                // odds for retaining possesion: 20%
                shotsOffTeam2++;
                res = rand.runif();
                if (res <= 0.4f) {
                    automatonState = state.cornerB;
                } else if (res <= 0.4f + 0.4f) {
                    automatonState = state.possessionA;
                } else {
                    automatonState = state.possessionB;
                }
            }
        }
    }

    private void updateScorers(TextArea ta_scorers, Player lastKnownScorer)
    {
        Platform.runLater(() -> {
            ta_scorers.appendText(gameMinutes + "'\t" + lastKnownScorer.getFullPlayerName() + "\n");
        });
    }

    private void handleGoalAction(int teamNumber)
    {
        if (teamNumber == 1) {
            teamAIterations++;
            goalsTeam1++;
            automatonState = state.possessionB;
            System.out.println("GOAL A");
        } else {
            teamBIterations++;
            goalsTeam2++;
            automatonState = state.possessionA;
            System.out.println("GOAL B");
        }
        updateScorers(ta_scorers, duelsEvaluator.getLastKnownScorer());
    }

    // TODO optimize this
    private void handlePassingAction(int teamNumber)
    {
        if (teamNumber == 1) {
            teamAIterations++;
            Player passer = team1.getPasser();
            Player intercepter = team2.getInterceptor();
            float res = duelsEvaluator.tryPassing(passer, intercepter);
            if (res == duelsEvaluator.successfulEvent) {
                // pass success!
                attPassesTeam1++;
                succPassesTeam1++;
            } else {
                // teamA lost possession
               // TODO improve TRYPASSING with interception fix
                // Let's see if it's real interception
                if (rand.runif() <= probabilityInterception) {
                    interceptTeam2++;
                } else {
                    // else, ball goes into no man's land, 50%50 split
                    if (rand.runif() <= 0.5f) automatonState = state.possessionA;
                    else automatonState = state.possessionB;
                }
                attPassesTeam1++;

//                automatonState = state.possessionB;
            }
//            System.out.println("PASSING A");
        } else {
            teamBIterations++;
            Player passer = team2.getPasser();
            Player intercepter = team1.getInterceptor();
            float res = duelsEvaluator.tryPassing(passer, intercepter);
            if (res == duelsEvaluator.successfulEvent) {
                // pass success!
                attPassesTeam2++;
                succPassesTeam2++;
            } else {
                // teamA lost possession
                if (rand.runif() <= probabilityInterception) {
                    interceptTeam1++;
                } else {
                    // else, ball goes into no man's land, 50%50 split
                    if (rand.runif() <= 0.5f) automatonState = state.possessionB;
                    else automatonState = state.possessionA;
                }
                attPassesTeam2++;
//                automatonState = state.possessionA;
            }
//            System.out.println("PASSING B");
        }
    }

    private void handleDribbleAction(int teamNumber)
    {
        if (teamNumber == 1) {
            teamAIterations++;
            Player dribbler = team1.getDribbler();
            Player opposition;
            float res = rand.runif();
            if (res <= 0.5f) opposition = team2.getMidfielder();
            else opposition = team2.getDefender();
            res = duelsEvaluator.tryDribbling(dribbler, opposition);
            attemptedDribbles1++;
            if (res == DuelsEvaluator.successfulEvent) {
                // Successful dribble!
                successfulDribbles1++;
                automatonState = state.possessionA;
            } else {
                if (rand.runif() <= probabilityFoul) {
                    // Perhaps it was a foul?
                    foulsTeam2++;
                    automatonState = state.possessionA;
                } else if (rand.runif() <= probabilityCorner) {
                    // Maybe a corner?
                    automatonState = state.cornerA;
                    corners1++;
                } else {
                    // Player 2 wins the ball
                    automatonState = state.possessionB;
                    tacklesTeam2++;
                }
            }

//            System.out.println("DRIBBLING A");
        } else {
            teamBIterations++;
            Player dribbler = team2.getDribbler();
            Player opposition;
            float res = rand.runif();
            if (res <= 0.5f) opposition = team1.getMidfielder();
            else opposition = team1.getDefender();
            res = duelsEvaluator.tryDribbling(dribbler, opposition);
            if (res == DuelsEvaluator.successfulEvent) {
                // Successful dribble!
                attemptedDribbles2++;
                successfulDribbles2++;
                automatonState = state.possessionB;
            } else {
                attemptedDribbles2++;
                automatonState = state.possessionA;
                tacklesTeam1++;
            }
            if (rand.runif() <= probabilityFoul) {
                // Perhaps it was a foul? If so,was made by player 1
                foulsTeam1++;
                automatonState = state.possessionB;     // We give ball to opposing team
            } else if (rand.runif() <= probabilityCorner) {
                // Maybe a corner?
                automatonState = state.cornerB;         // Corner for team2
                corners2++;
            } else {
                // Player 1 wins the ball
                automatonState = state.possessionA;
                tacklesTeam1++;
            }
        }
//        System.out.println("DRIBBLING A");
    }


    private state handlePossessionState(state automatonState)
    {
        float probabilityToPass = 0.7f;
        float probabilityToDribble = 0.20f;
        float probabilityToShot = 0.1f;             // not used, I've put it just for info
        float bacon = rand.runif();
        if (bacon <= probabilityToPass) {
            // We make a pass
            automatonState = (automatonState == state.possessionA ) ? state.passA : state.passB;
//            System.out.println("Switched to: " + ((automatonState == state.possessionA ) ? state.passA : state.passB));
        } else if (bacon <= probabilityToPass + probabilityToDribble) {
            // We make a dribble
            automatonState = (automatonState == state.possessionA ) ? state.dribbleA : state.dribbleB;
        } else {
            // We make a shot
            automatonState = (automatonState == state.possessionA) ? state.shootA : state.shootB;
        }
//        System.out.println("handlePossessionState returning: " + automatonState);
        return automatonState;
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
                     Label lb_interceptions_team2, Label lb_minute, Label lb_corners_team1, Label lb_corners_team2)
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
            this.lb_corners_team1 = lb_corners_team1;
            this.lb_corners_team2 = lb_corners_team2;
        });
    }

}
