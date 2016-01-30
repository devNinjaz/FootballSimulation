package controller;

import model.Player;

import java.util.Random;

/**
 * Created by devninja on 30.1.16..
 */
public class DuelsEvaluator
{
    private Random rand;
    public static float failedEvent = 0.1f;
    public static float successfulEvent = 0.9f;
    // Penalty variables -> Used to tweak simulation
    // They are extremely important because you can't really
    // simulate a football game only from stats of players.
    // Later, these penalties could be evaluated from more parameters instead of being const.

    // Passing pens
    float passingInitialSuccessPenalty = 0.8f;
    float passingAfterSuccInterceptPenalty = 0.5f;
    float passingAfterFailInterceptPenalty = 0.8f;

    // Dribbling pens
    float dribblingInitialSucessPenalty = 0.6f;
    float dribblingAfterSuccTacklePenalty = 0.3f;
    float dribblingAfterFailTacklePenalty = 0.8f;

    // Corner
    float cornerPenalty = 0.2f;

    // Throw in
    float throwInPenalty = 0.75f;

    // Shooting event
    float ShootingInitialPenalty = 0.5f;
    float ShootingSuccessfulShotPenalty = 0.5f;
    float ShootingFailedShotPenalty = 0.9f;

    public DuelsEvaluator()
    {
        rand = new Random();
    }

    private float toProb(int val)
    {
        return val * 1.0f / 100.0f;
    }

    private void logEvent(String msg, float eventResult)
    {

        if (eventResult == successfulEvent)
            msg += " SUCCESS!";
        else
            msg += " FAILED!";

        System.out.println(msg);
        System.out.println();
    }
    // **********************************************************************************************************
    //  Functions here are used to simulate events that occur in a game. They return a float because
    //  probably later, some function will be changed to return more then 2 possible events.
    //  The better the functions are, simulation is more precise.
    // **********************************************************************************************************
    public float tryPassing(Player passer, Player opposition)
    {
        int shortPassing = passer.getStats().get("low_pass");
        int longPassing = passer.getStats().get("lofted_pass");
        int defensiveProwess = opposition.getStats().get("defensive_prowess");
        int ballWinning = opposition.getStats().get("ball_winning");
        // TODO to be done AFTER creating automaton construction
        float dragon = rand.nextFloat();
        float probNoPressure = 0.5f;
        float eventResult;
        // We assume that around 50% of the time, players are under no pressure to make a pass
        if (dragon <= probNoPressure) {
            // Around 70% of the time, players make a short pass
            dragon = rand.nextFloat();
            if (dragon <= 0.7f) {
                // short pass
                dragon = rand.nextFloat();
                if (dragon <= toProb(shortPassing)) {
                    // pass success
                    eventResult = successfulEvent;
                } else {
                    // pass failed
                    // very rare in football, we give player another chance
                    dragon = rand.nextFloat();
                    eventResult = (dragon <= toProb(shortPassing) ) ? successfulEvent : failedEvent;
                }
            } else {
                // long pass
                eventResult = (dragon <= toProb(longPassing)) ? successfulEvent : failedEvent;
            }

        } else {
            // player is under pressure
            if (dragon <= (toProb(shortPassing) + toProb(longPassing))/2.0f * passingInitialSuccessPenalty) {
                // Pass check success
                dragon = rand.nextFloat();
                if (dragon <= (toProb(defensiveProwess) + toProb(ballWinning))/2.0f * passingAfterSuccInterceptPenalty) {
                    // successful interception
                    eventResult = failedEvent;
                } else {
                    eventResult = successfulEvent;
                }

            } else {
                // Pass check fail
                dragon = rand.nextFloat();
                if (dragon <= (toProb(defensiveProwess) + toProb(ballWinning))/2.0f * passingAfterFailInterceptPenalty) {
                    eventResult = failedEvent;
                } else {
                    eventResult = successfulEvent;
                }
            }
        }
       logEvent("PASSING:", eventResult);
        return eventResult;
    }


    public float tryDribbling(Player dribbler, Player opposition)
    {
        // Extracting required stats
        int dribbling = dribbler.getStats().get("dribbling");
        int speed = dribbler.getStats().get("speed");
        int explosivePower = dribbler.getStats().get("explosive_power");
        int ballWinning = opposition.getStats().get("ball_winning");
        int bodyBalance = opposition.getStats().get("body_balance");
        int defensiveProwess = opposition.getStats().get("defensive_prowess");

        // TODO randomly match 3 vs 3 using a vector or something

        // Evaluating event
        float dragon = rand.nextFloat();
        float eventResult;
        if (dragon <= (toProb(dribbling)+toProb(speed)+toProb(explosivePower))/3.0f*dribblingInitialSucessPenalty)
        {
            dragon = rand.nextFloat();
            if (dragon <= (toProb(bodyBalance)+toProb(defensiveProwess)+toProb(ballWinning))/3.0f* dribblingAfterSuccTacklePenalty) {
               eventResult = failedEvent;
            } else eventResult = successfulEvent;

        } else {
            dragon = rand.nextFloat();
            if (dragon <= (toProb(bodyBalance)+toProb(defensiveProwess)+toProb(ballWinning))/3.0f* dribblingAfterFailTacklePenalty) {
                eventResult = failedEvent;
            } else eventResult = successfulEvent;
        }

        logEvent("SHOOTING : ", eventResult);
        return eventResult;
    }

    public float tryCorner(Player taker)
    {
        int placeKicking = taker.getStats().get("place_kicking");
        float dragon = rand.nextFloat();
        float eventResult;
        if (dragon <= toProb(placeKicking) * cornerPenalty) {
            eventResult = successfulEvent;
        } else eventResult = failedEvent;
        logEvent("CORNER: ", eventResult);
        return eventResult;
    }

    public float tryThrowIn()
    {
        float dragon = rand.nextFloat();
        float eventResult;
        if (dragon <= throwInPenalty) {
            eventResult = successfulEvent;
        } else {
            eventResult = failedEvent;
        }
       logEvent("THROW IN: ", eventResult);
        return eventResult;
    }

    public float dummyFunction()
    {
        float dragon = rand.nextFloat();
        if (dragon <= 0.5)
            return successfulEvent;
        else
            return failedEvent;
    }
    // **********************************************************************************************************
    // **********************************************************************************************************

    @Override
    public String toString()
    {
        return "Hello world, I am used to simulate events in a football game.";
    }

    public float tryShooting(Player shooter, Player goalkeeper)
    {
        // find the average probability of player's shooting quality
        int attackingProwess = shooter.getStats().get("attacking_prowess");
        int finishing = shooter.getStats().get("finishing");
        int kickingPower = shooter.getStats().get("kicking_power");
        int gkOverall = goalkeeper.getStats().get("overall");

        float avg = (toProb(attackingProwess) + toProb(kickingPower) + toProb(finishing)) / 3.0f;
        float dragon = rand.nextFloat();
        float eventResult;

        if (dragon <= avg * ShootingInitialPenalty) {
            dragon = rand.nextFloat();
            if (dragon <= toProb(gkOverall) * ShootingSuccessfulShotPenalty) {
                eventResult = failedEvent;
            } else {
                eventResult = successfulEvent;
            }
        } else {
            dragon = rand.nextFloat();
            if (dragon <= toProb(gkOverall) * ShootingFailedShotPenalty) {
                eventResult = failedEvent;
            } else {
                eventResult = successfulEvent;
            }
        }
        return eventResult;
//        return (rand.nextFloat() <= 0.7) ? failedEvent : successfulEvent;
    }
}
