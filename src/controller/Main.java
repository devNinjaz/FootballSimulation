package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.GeneralFootballStatsContainer;
import model.Team;

import java.sql.*;

public class Main extends Application {

    public static String ProjectDir = System.getProperty("user.dir");
    public static String URLToResources = "file:///" + ProjectDir + "/src/resources/";
    public static String PathToResources = ProjectDir + "/src/resources/";
    public static String PathToViews = "file:///" + ProjectDir + "/src/view";
    public static Team teamA, teamB;
    public static int MillisecondsForOneMinute = 300;
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
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
