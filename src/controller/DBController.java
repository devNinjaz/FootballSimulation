package controller;

/**
 * Created by devninja on 8.1.16..
 */


import java.sql.*;

public class DBController
{
    // JDBC driver name and database URL
//    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//    private final String DB_URL = "jdbc:mysql://db4free.net:3306/ppaurora";
    private final String DB_URL = "jdbc:mysql://localhost/seminarski";

    //  Database credentials
//    private final String USER = ProjectParams.onlinedbusr;
//    final String PASS = ProjectParams.onlinedbpss;
    private final String USER = ProjectParams.localdbusr;
    final String PASS = ProjectParams.localdbpss;

    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;

    public DBController() { }

    public ResultSet sendQuery(String query)
    {
        try {
            // STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // STEP 3: Open a connection
            System.out.println("Connecting to database...");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);

            // STEP 4: Execute a query
            System.out.println("Creating statement...");
            statement = connection.createStatement();

            resultSet = statement.executeQuery(query);
            return resultSet;


        } catch (SQLException se) {
            // Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            // Handle errors for Class.forName
            e.printStackTrace();
        }
        return null;
    }

    public void cleanUp()
    {
        try {
            statement.close();
            connection.close();
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Failed closing DB connection!");
        } finally {
            // finally block used to close resources
            try {
                if (statement != null)
                    statement.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            } //end finally try
        }
    }
}
