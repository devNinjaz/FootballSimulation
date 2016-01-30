package model;

import controller.DBController;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by devninja on 30.1.16..
 */
public class GeneralFootballStatsContainer
{
    public static float averageShotsOnTarget;
    public static float shotsPerGame;
    public static float possessionPerGame;
    public static float passSuccessPerGame;
    public static float tacklesPerGame;
    public static float interceptionsPerGame;
    public static float foulsPerGame;
    public static float fouledPerGame;
    public static float dribblesPerGame;

    private DBController dbController;

    public GeneralFootballStatsContainer() throws SQLException
    {
        dbController = new DBController();

        //------------------------------------------------------------------------------------------------------------
        // Handling summary table
        //------------------------------------------------------------------------------------------------------------
        String query = "select ";
        query += "avg(shots_per_game) shotsPerGame, avg(possession_per_game) possPerGame, avg(pass_success_per_game) passSuccPerGame ";
        query += "from summary_stats";

        ResultSet resultSet = dbController.sendQuery(query);
        while (resultSet.next()) {
            shotsPerGame = resultSet.getFloat("shotsPerGame");
            possessionPerGame = resultSet.getFloat("possPerGame");
            passSuccessPerGame = resultSet.getFloat("passSuccPerGame");
        }
        dbController.cleanUp();

        //-----------------------------------------------------------------------
        // Handling defensive table
        //-----------------------------------------------------------------------
        query = "select ";
        query += "avg(tackles_per_game) tacklesPerGame, avg(interceptions_per_game) interceptionsPerGame, ";
        query += " avg(fouls_per_game) foulsPerGame ";
        query += "from defensive_stats";
        resultSet = dbController.sendQuery(query);
        while (resultSet.next()) {
            tacklesPerGame = resultSet.getFloat("tacklesPerGame");
            interceptionsPerGame = resultSet.getFloat("interceptionsPerGame");
            foulsPerGame = resultSet.getFloat("foulsPerGame");
        }
        dbController.cleanUp();

        //-----------------------------------------------------------------------
        // Handling offensive table
        //-----------------------------------------------------------------------
        query = "select ";
        query += "avg(shots_on_target_per_game) shotsOnTarget, avg(dribbles_per_game) dribbles, avg(fouled_per_game) fouled ";
        query += "from offensive_stats";
        resultSet = dbController.sendQuery(query);
        while (resultSet.next()) {
            averageShotsOnTarget = resultSet.getFloat("shotsOnTarget");
            dribblesPerGame = resultSet.getFloat("dribbles");
            fouledPerGame = resultSet.getFloat("fouled");
        }
        dbController.cleanUp();
    }
}
