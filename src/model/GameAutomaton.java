package model;

import controller.DuelsEvaluator;
import controller.Main;
import javafx.application.Platform;
import javafx.scene.control.Label;

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
    }

    private void refreshGui()
    {
        updateOnGui(lb_goals_team1, goalsTeam1);
        updateOnGui(lb_attempted_passes_team1, attPassesTeam1);
        updateOnGui(lb_successful_passes_team1, succPassesTeam1);
        updateOnGui(lb_fouls_team1, foulsTeam1);
        updateOnGui(lb_interceptions_team1, interceptTeam1);
        updateOnGui(lb_poss_team1, 0);
        updateOnGui(lb_shots_on_target_team1, shotsOnTeam1);
        updateOnGui(lb_shots_off_target_team1, shotsOffTeam1);
        updateOnGui(lb_tackles_team1, tacklesTeam1);
        updateOnGui(lb_goals_team2, goalsTeam2);
        updateOnGui(lb_attempted_passes_team2, attPassesTeam2);
        updateOnGui(lb_successful_passes_team2, succPassesTeam2);
        updateOnGui(lb_fouls_team2, foulsTeam2);
        updateOnGui(lb_interceptions_team2, interceptTeam2);
        updateOnGui(lb_poss_team2, 0);
        updateOnGui(lb_shots_on_target_team2, shotsOnTeam2);
        updateOnGui(lb_shots_off_target_team2, shotsOffTeam2);
        updateOnGui(lb_tackles_team2, tacklesTeam2);
        updateOnGui(lb_minute, gameMinutes);
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
                simulate(rand.getFromInterval(8, 15));
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
            refreshGui();
            switch (automatonState) {

                // INITIAL
                case initialState:
                    // At the start of game, teamA has the ball
                    automatonState = state.possessionA;
                    showCommentary(automatonState, null, null, 0f);
                    System.out.println("initial state");
                    break;

                // HALF TIME
                case halfTimeState:
                    // At half time, teamB has the ball
                    automatonState = state.possessionB;
                    break;

                // POSSESSION (most important, controls most of the automaton)
                case possessionA:
                    automatonState = handlePossessionState(automatonState);           // swithces global variable automatonState
                    System.out.println("possessionA");
                    break;
                case possessionB:
                    automatonState = handlePossessionState(automatonState);
                    System.out.println("Possession B");
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
    }

    private void handleCornerAction(int teamNumber)
    {
        if (teamNumber == 1) {
            Player setPieceTaker = team1.getSetPieceTaker();
            float res = duelsEvaluator.tryCorner(setPieceTaker);
            corners1++;
            if (res == DuelsEvaluator.successfulEvent) {
                // Team scored from corner
                shotsOnTeam1++;
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
            System.out.println("CORNER A");

        } else {
            Player setPieceTaker = team2.getSetPieceTaker();
            float res = duelsEvaluator.tryCorner(setPieceTaker);
            corners2++;
            if (res == DuelsEvaluator.successfulEvent) {
                // Team scored from corner
                shotsOnTeam2++;
                automatonState = state.goalB;           // STATE SWITCH
            } else {
                // if no goal, 50/50 for ball winning by both sides
                res = rand.runif();
                if (res <= 0.5f) {
                    automatonState = state.possessionB;
                } else {
                    automatonState = state.possessionA;
                }
            }
            System.out.println("CORNER B");
        }
    }

    private void handleShootingAction(int teamNumber)
    {
        if (teamNumber == 1) {
            Player shooter = team1.getShooter();
            Player goalkeeper = team2.getGoalkeeper();
            float res = duelsEvaluator.tryShooting(shooter, goalkeeper);
            if (res == DuelsEvaluator.successfulEvent) {
                // We have a goal!
                System.out.println("GOAL for " + team1.getTeamName() + " scored by: " + shooter.getFullPlayerName());
                automatonState = state.goalA;
                shotsOnTeam1++;
            } else {
                // Player failed to score
                // odds for corner: 40%
                // odds for losing possession: 40%
                // odds for retaining possesion: 20%
                shotsOffTeam1++;
                res = rand.runif();
                if (res <= 0.4) {
                    automatonState = state.cornerA;
                } else if (res <= 0.4f + 0.4f) {
                    automatonState = state.possessionB;
                } else {
                    automatonState = state.possessionA;
                }
            }
        } else {
            Player shooter = team2.getShooter();
            Player goalkeeper = team1.getGoalkeeper();
            float res = duelsEvaluator.tryShooting(shooter, goalkeeper);
            if (res == DuelsEvaluator.successfulEvent) {
                // We have a goal!
                System.out.println("GOAL for " + team2.getTeamName() + " scored by: " + shooter.getFullPlayerName());
                automatonState = state.goalB;
                shotsOnTeam2++;
            } else {
                // Player failed to score
                // odds for corner: 40%
                // odds for losing possession: 40%
                // odds for retaining possesion: 20%
                shotsOffTeam2++;
                res = rand.runif();
                if (res <= 0.4) {
                    automatonState = state.cornerB;
                } else if (res <= 0.4f + 0.4f) {
                    automatonState = state.possessionA;
                } else {
                    automatonState = state.possessionB;
                }
            }
        }
    }

    private void handleGoalAction(int teamNumber)
    {
        if (teamNumber == 1) {
            goalsTeam1++;
            automatonState = state.possessionB;
            System.out.println("GOAL A");
        } else {
            goalsTeam2++;
            automatonState = state.possessionA;
            System.out.println("GOAL B");
        }
    }

    private void handlePassingAction(int teamNumber)
    {
        if (teamNumber == 1) {
            Player passer = team1.getPasser();
            Player intercepter = team2.getInterceptor();
            float res = duelsEvaluator.tryPassing(passer, intercepter);
            if (res == duelsEvaluator.successfulEvent) {
                // pass success!
                attPassesTeam1++;
                succPassesTeam1++;
            } else {
                // teamA lost possession
                attPassesTeam1++;
                interceptTeam2++;
                automatonState = state.possessionB;
            }
            System.out.println("PASSING A");
        } else {
            Player passer = team2.getPasser();
            Player intercepter = team1.getInterceptor();
            float res = duelsEvaluator.tryPassing(passer, intercepter);
            if (res == duelsEvaluator.successfulEvent) {
                // pass success!
                attPassesTeam2++;
                succPassesTeam2++;
            } else {
                // teamA lost possession
                attPassesTeam2++;
                interceptTeam1++;
                automatonState = state.possessionA;
            }
            System.out.println("PASSING B");
        }
    }

    private void handleDribbleAction(int teamNumber)
    {
        if (teamNumber == 1) {
            Player dribbler = team1.getDribbler();
            Player opposition;
            float res = rand.runif();
            if (res <= 0.5f) opposition = team2.getMidfielder();
            else opposition = team2.getDefender();
            res = duelsEvaluator.tryDribbling(dribbler, opposition);
            if (res == DuelsEvaluator.successfulEvent) {
                // Successful dribble!
                attemptedDribbles1++;
                successfulDribbles1++;
                showCommentary(automatonState, dribbler, opposition, res);
                automatonState = state.possessionA;
            } else {
                attemptedDribbles1++;
                showCommentary(automatonState, dribbler, opposition, res);
                automatonState = state.possessionB;
            }
            System.out.println("DRIBBLING A");
        } else {
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
                showCommentary(automatonState, dribbler, opposition, res);
                automatonState = state.possessionB;
            } else {
                attemptedDribbles2++;
                showCommentary(automatonState, dribbler, opposition, res);
                automatonState = state.possessionA;
            }
            System.out.println("DRIBBLING A");
        }
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
            System.out.println("Switched to: " + ((automatonState == state.possessionA ) ? state.passA : state.passB));
        } else if (bacon <= probabilityToPass + probabilityToDribble) {
            // We make a dribble
            automatonState = (automatonState == state.possessionA ) ? state.dribbleA : state.dribbleB;
        } else {
            // We make a shot
            automatonState = (automatonState == state.possessionA) ? state.shootA : state.shootB;
        }
        System.out.println("handlePossessionState returning: " + automatonState);
        return automatonState;
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
