package controller;

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
    public float tryPassing(int shortPassing, int defensiveProwess)
    {
        // TODO implement logic that creates 100% successful passes
        // TODO because nobody is near the player making the pass
        // TODO to be done AFTER creating automaton construction
        float dragon = rand.nextFloat();
        float eventResult;
        if (dragon <= toProb(shortPassing)) {
            // Pass check success
            dragon = rand.nextFloat();
            if (dragon <= toProb(defensiveProwess) * passingAfterSuccInterceptPenalty) {
                // successful interception
                eventResult = failedEvent;
            } else {
                eventResult = successfulEvent;
            }

        } else {
            // Pass check fail
            dragon = rand.nextFloat();
            if (dragon <= toProb(defensiveProwess) * passingAfterFailInterceptPenalty) {
                eventResult = failedEvent;
            } else {
                eventResult = successfulEvent;
            }
        }
       logEvent("DRIBBLING:", eventResult);
        return eventResult;
    }

    // TODO can't implement this until GK stats are fixed
    // UPDATE I've decided to eval GK overall against required params until GK stats are fixed
    public float tryShooting(int attackingProwess, int shootingPower, int fininshing, int gkOverall)
    {
        // find the average probability of player's shooting quality
        float avg = (toProb(attackingProwess) + toProb(shootingPower) + toProb(fininshing)) / 3.0f;
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

        return (rand.nextFloat() <= 0.7) ? failedEvent : successfulEvent;
    }

    public float tryDribbling(int dribblingA, int speedA, int explosivePowerA,
                              int bodyBalanceB, int defensiveProwessB, int ballWinningB)
    {
        // TODO randomly match 3 vs 3 using a vector or something
        float dragon = rand.nextFloat();
        float eventResult;
        if (dragon <= (toProb(dribblingA)+toProb(speedA)+toProb(explosivePowerA))/3.0f*dribblingInitialSucessPenalty)
        {
            dragon = rand.nextFloat();
            if (dragon <= (toProb(bodyBalanceB)+toProb(defensiveProwessB)+toProb(ballWinningB))/3.0f*dribblingAfterSuccTacklePenalty) {
               eventResult = failedEvent;
            } else eventResult = successfulEvent;

        } else {
            dragon = rand.nextFloat();
            if (dragon <= (toProb(bodyBalanceB)+toProb(defensiveProwessB)+toProb(ballWinningB))/3.0f*dribblingAfterFailTacklePenalty) {
                eventResult = failedEvent;
            } else eventResult = successfulEvent;
        }

        logEvent("SHOOTING : ", eventResult);
        return eventResult;
    }

    public float tryCorner(int placeKicking)
    {
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
}
