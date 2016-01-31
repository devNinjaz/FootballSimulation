package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by termninja on 8.1.16..
 */
public class LineupsController implements Initializable
{
    @FXML
    public Label pl1,pl2,pl3,pl4,pl5,pl6,pl7,pl8,pl9,pl10,pl11, pl12,pl21,pl31,pl41,pl51,pl61,pl71,pl81,pl91,pl101,pl111;

    @FXML
    Button bt_back;

    @FXML
    public void bt_back_to_choose_team(ActionEvent event) throws IOException
    {
        Stage stage;
        Parent root;
        stage = (Stage) bt_back.getScene().getWindow();
//        root = FXMLLoader.load(getClass().getResource("../view/ChooseTeam.fxml"));
        root = FXMLLoader.load(new URL(Main.PathToViews + "/ChooseTeam.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        System.out.println(Main.teamB.getTeamName());
        if (Main.lineupsChosenTeam == 1) {
            Main.teamA.sortPlayersByPosition();
            pl1.setText(Main.teamA.getPlayers().get(0).getPlayerSurname());
            pl2.setText(Main.teamA.getPlayers().get(1).getPlayerSurname());
            pl3.setText(Main.teamA.getPlayers().get(2).getPlayerSurname());
            pl4.setText(Main.teamA.getPlayers().get(3).getPlayerSurname());
            pl5.setText(Main.teamA.getPlayers().get(4).getPlayerSurname());
            pl6.setText(Main.teamA.getPlayers().get(5).getPlayerSurname());
            pl7.setText(Main.teamA.getPlayers().get(6).getPlayerSurname());
            pl8.setText(Main.teamA.getPlayers().get(7).getPlayerSurname());
            pl9.setText(Main.teamA.getPlayers().get(8).getPlayerSurname());
            pl10.setText(Main.teamA.getPlayers().get(9).getPlayerSurname());
            pl11.setText(Main.teamA.getPlayers().get(10).getPlayerSurname());
            pl12.setText(String.valueOf(Main.teamA.getPlayers().get(0).getPlayerStats("overall")));
            pl21.setText(String.valueOf(Main.teamA.getPlayers().get(1).getPlayerStats("overall")));
            pl31.setText(String.valueOf(Main.teamA.getPlayers().get(2).getPlayerStats("overall")));
            pl41.setText(String.valueOf(Main.teamA.getPlayers().get(3).getPlayerStats("overall")));
            pl51.setText(String.valueOf(Main.teamA.getPlayers().get(4).getPlayerStats("overall")));
            pl61.setText(String.valueOf(Main.teamA.getPlayers().get(5).getPlayerStats("overall")));
            pl71.setText(String.valueOf(Main.teamA.getPlayers().get(6).getPlayerStats("overall")));
            pl81.setText(String.valueOf(Main.teamA.getPlayers().get(7).getPlayerStats("overall")));
            pl91.setText(String.valueOf(Main.teamA.getPlayers().get(8).getPlayerStats("overall")));
            pl101.setText(String.valueOf(Main.teamA.getPlayers().get(9).getPlayerStats("overall")));
            pl111.setText(String.valueOf(Main.teamA.getPlayers().get(10).getPlayerStats("overall")));
        } else {
            Main.teamB.sortPlayersByPosition();
            pl1.setText(Main.teamB.getPlayers().get(0).getPlayerSurname());
            pl2.setText(Main.teamB.getPlayers().get(1).getPlayerSurname());
            pl3.setText(Main.teamB.getPlayers().get(2).getPlayerSurname());
            pl4.setText(Main.teamB.getPlayers().get(3).getPlayerSurname());
            pl5.setText(Main.teamB.getPlayers().get(4).getPlayerSurname());
            pl6.setText(Main.teamB.getPlayers().get(5).getPlayerSurname());
            pl7.setText(Main.teamB.getPlayers().get(6).getPlayerSurname());
            pl8.setText(Main.teamB.getPlayers().get(7).getPlayerSurname());
            pl9.setText(Main.teamB.getPlayers().get(8).getPlayerSurname());
            pl10.setText(Main.teamB.getPlayers().get(9).getPlayerSurname());
            pl11.setText(Main.teamB.getPlayers().get(10).getPlayerSurname());
            pl12.setText(String.valueOf(Main.teamB.getPlayers().get(0).getPlayerStats("overall")));
            pl21.setText(String.valueOf(Main.teamB.getPlayers().get(1).getPlayerStats("overall")));
            pl31.setText(String.valueOf(Main.teamB.getPlayers().get(2).getPlayerStats("overall")));
            pl41.setText(String.valueOf(Main.teamB.getPlayers().get(3).getPlayerStats("overall")));
            pl51.setText(String.valueOf(Main.teamB.getPlayers().get(4).getPlayerStats("overall")));
            pl61.setText(String.valueOf(Main.teamB.getPlayers().get(5).getPlayerStats("overall")));
            pl71.setText(String.valueOf(Main.teamB.getPlayers().get(6).getPlayerStats("overall")));
            pl81.setText(String.valueOf(Main.teamB.getPlayers().get(7).getPlayerStats("overall")));
            pl91.setText(String.valueOf(Main.teamB.getPlayers().get(8).getPlayerStats("overall")));
            pl101.setText(String.valueOf(Main.teamB.getPlayers().get(9).getPlayerStats("overall")));
            pl111.setText(String.valueOf(Main.teamB.getPlayers().get(10).getPlayerStats("overall")));
        }
    }
}