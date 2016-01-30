package model;

import java.util.Random;

/**
 * Created by devninja on 30.1.16..
 */
public class RandomUtil
{
    private Random rand;
    public RandomUtil ()
    {
        rand = new Random();
    }

    // Returns an integer from [a, b] interval
    public int getFromInterval(int a, int b)
    {
        return rand.ints(1, a, b+1).findFirst().getAsInt();
    }

    // Returns number from [0, 1) from Uniform distribution
    public float runif()
    {
        return rand.nextFloat();
    }
}
