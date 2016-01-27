package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {

    public static String ProjectDir = System.getProperty("user.dir");
    public static String URLToResources = "file:///" + ProjectDir + "/src/resources/";
    public static String PathToResources = ProjectDir + "/src/resources/";
    public static String PathToViews = "file:///" + ProjectDir + "/src/view";


    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // System.out.println(System.class.getResource("../view/sample.fxml").getPath().toString());
        Parent root = FXMLLoader.load(getClass().getResource("../view/ChooseTeam.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws SQLException
    {
        DBController db = new DBController();
        ResultSet resultSet = db.sendQuery("select * from players");//            //STEP 5: Extract data from result set
        while (resultSet.next()) {
            //Retrieve by column name
            int id = resultSet.getInt("player_id");
            String first = resultSet.getString("name");
            String last = resultSet.getString("surname");

            //Display values
            System.out.print("ID: " + id);
            System.out.print(", First: " + first);
            System.out.println(", Last: " + last);
        }
        db.cleanUp();
        launch(args);
        System.out.println("Hello world");

    }
}
