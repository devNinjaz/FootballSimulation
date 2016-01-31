package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.GeneralFootballStatsContainer;
import model.Team;

import java.net.URL;
import java.sql.*;

public class Main extends Application {

    // Deployment
    public static String ProjectDir = System.getProperty("user.dir");
    public static String URLToResources = "file:///" + ProjectDir + "/resources/";
    public static String PathToResources = ProjectDir + "/resources/";
    public static String PathToViews = "file:///" + ProjectDir + "/view";



//    public static String ProjectDir = "/home/devninja/IdeaProjects/FootballSimulation";
//    public static String URLToResources = "file:///" + ProjectDir + "/src/resources/";
//    public static String PathToResources = ProjectDir + "/src/resources/";
//    public static String PathToViews = "file:///" + ProjectDir + "/src/view";
    public static Team teamA, teamB;
    public static int lineupsChosenTeam;
    public static int MillisecondsForOneMinute = 100;
    public static int InitialChosenLeague1 = 0;
    public static int InitialChosenLeague2 = 1;
    public static int InitialChosenTeam1 = 0;
    public static int InitialChosenTeam2 = 0;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(new URL(PathToViews + "/MainScreen.fxml"));
//        Parent root = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        primaryStage.setTitle("Project Aurora");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        GeneralFootballStatsContainer generalFootballStatsContainer= new GeneralFootballStatsContainer();
    }

    public static void main(String[] args) throws SQLException
    {
        launch(args);
    }
}
