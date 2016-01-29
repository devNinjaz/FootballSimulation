package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.League;
import model.Player;
import model.Team;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Created by devninja on 8.1.16..
 */
public class ChooseTeamController implements Initializable
{
    // Contains all leagues from database
    Vector<League> leagues;
    int position_of_league_1 = 0;
    int position_of_league_2 = 1;
    int position_of_team_1 = 0;
    int position_of_team_2 = 0;
    Vector<Player> players_team_A;
    Vector<Player> players_team_B;

    @FXML
    Label lb_league1;
    @FXML
    Label lb_league2;
    @FXML
    Label lb_team1;
    @FXML
    Label lb_team2;

    @FXML
    Button bt_league1_left;
    @FXML
    Button bt_league1_right;
    @FXML
    Button bt_league2_left;
    @FXML
    Button bt_league2_right;
    @FXML
    Button bt_team1_left;
    @FXML
    Button bt_team1_right;
    @FXML
    Button bt_team2_left;
    @FXML
    Button bt_team2_right;
    @FXML
    Button bt_back;
    @FXML
    Button bt_start;

    @FXML
    ImageView img_league1;
    @FXML
    ImageView img_league2;
    @FXML
    ImageView img_team1;
    @FXML
    ImageView img_team2;

    private void loadLeaguesAndTeams()
    {
        leagues = new Vector<League>();
        DBController dbController = new DBController();

        // Reading leagues and mapping them to objects
        ResultSet league_query = dbController.sendQuery("select * from league");
        int num_of_leagues = 0;
        try {
            while (league_query.next()) {
                Blob league_blob = league_query.getBlob("logo");
                int league_logo_length = (int) league_blob.length();
                byte[] league_logo_bytes = league_blob.getBytes(1, league_logo_length);
                FileOutputStream league_logo = new FileOutputStream(Main.PathToResources + league_query.getString("name") + ".png");
                league_logo.write(league_logo_bytes);
                league_logo.close();
                league_blob.free();
                Image league_image_logo = new Image(Main.URLToResources + league_query.getString("name") + ".png");
                leagues.add(new League(
                        league_query.getString("name"),
                        league_query.getInt("league_id"),
                        league_image_logo,
                        new Vector<Team>()
                ));
                num_of_leagues++;
            }
        } catch (SQLException e) {
            System.out.println("Failed loading leagues...");
        } catch (FileNotFoundException e) {
            System.out.println("failed creating league image from binary file at resources!");
        } catch (IOException e) {
            System.out.println("Failed writing league logo at resources!");
        }


        // Reading teams and mapping them to objects
        ResultSet result = dbController.sendQuery("select * from teams t join league l on t.league_id = l.league_id");

        try {
            while (result.next()) {
                Blob blob1 = result.getBlob("t.logo");

                int team_logo_length = (int) blob1.length();
                byte[] team_logo_bytes = blob1.getBytes(1, team_logo_length);

                // release the blob and free up memory. (since JDBC 4.0)
                blob1.free();
                FileOutputStream team_logo = new FileOutputStream(Main.PathToResources + result.getString("t.name") + ".png");
                team_logo.write(team_logo_bytes);
                team_logo.close();
                Image team_image = new Image(Main.URLToResources + result.getString("t.name") + ".png");
                int find_me_league = result.getInt("l.league_id") - 1;
                leagues.get(find_me_league).getTeams().add(new Team(
                        result.getString("t.name"),
                        result.getInt("t.team_id"),
                        team_image,
                        find_me_league + 1,
                        result.getString("l.name")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Crashed at ChooseTeamController while iterating through query league result!");
        } catch (FileNotFoundException e) {
            System.out.println("Failed creating output mapped image...");
        } catch (IOException e) {
            System.out.println("Failed writing bytes into memory");
        }

        dbController.cleanUp();
    }

    //function for change league player 1 left
    @FXML
    public void change_league_1_left(ActionEvent event)
    {
        if ((position_of_league_1 - 1) < 0)
            position_of_league_1 = leagues.size() - 1;
        else
            position_of_league_1 = position_of_league_1 - 1;

        img_league1.setImage(leagues.get(position_of_league_1).getLeague_logo());
        position_of_team_1 = 0;
        lb_league1.setText(leagues.get(position_of_league_1).getLeague_name());
        lb_team1.setText(leagues.get(position_of_league_1).getTeams().get(position_of_team_1).getTeamName());
        img_team1.setImage(leagues.get(position_of_league_1).getTeams().get(position_of_team_1).getTeam_logo());
    }

    //function for change league player 1 right
    @FXML
    public void change_league_1_right(ActionEvent event)
    {
        position_of_league_1 = (position_of_league_1 + 1) % (leagues.size() - 1);
        img_league1.setImage(leagues.get(position_of_league_1).getLeague_logo());
        position_of_team_1 = 0;
        lb_league1.setText(leagues.get(position_of_league_1).getLeague_name());
        lb_team1.setText(leagues.get(position_of_league_1).getTeams().get(position_of_team_1).getTeamName());
        img_team1.setImage(leagues.get(position_of_league_1).getTeams().get(position_of_team_1).getTeam_logo());
    }

    //function for change league player 2 left
    @FXML
    public void change_league_2_left(ActionEvent event)
    {
        if ((position_of_league_2 - 1) < 0)
            position_of_league_2 = leagues.size() - 1;
        else
            position_of_league_2 = position_of_league_2 - 1;

        img_league2.setImage(leagues.get(position_of_league_2).getLeague_logo());
        position_of_team_2 = 0;
        lb_league2.setText(leagues.get(position_of_league_2).getLeague_name());
        lb_team2.setText(leagues.get(position_of_league_2).getTeams().get(position_of_team_2).getTeamName());
        img_team2.setImage(leagues.get(position_of_league_2).getTeams().get(position_of_team_2).getTeam_logo());
    }

    //function for change league player 2 left
    @FXML
    public void change_league_2_right(ActionEvent event)
    {
        position_of_league_2 = (position_of_league_2 + 1) % (leagues.size() - 1);
        img_league2.setImage(leagues.get(position_of_league_2).getLeague_logo());
        position_of_team_2 = 0;
        lb_league2.setText(leagues.get(position_of_league_2).getLeague_name());
        lb_team2.setText(leagues.get(position_of_league_2).getTeams().get(position_of_team_2).getTeamName());
        img_team2.setImage(leagues.get(position_of_league_2).getTeams().get(position_of_team_2).getTeam_logo());
    }

    @FXML
    public void change_team_1_left(ActionEvent event)
    {
        if ((position_of_team_1 - 1) < 0)
            position_of_team_1 = leagues.get(position_of_league_1).getTeams().size() - 1;
        else
            position_of_team_1 = position_of_team_1 - 1;

        lb_team1.setText(leagues.get(position_of_league_1).getTeams().get(position_of_team_1).getTeamName());
        img_team1.setImage(leagues.get(position_of_league_1).getTeams().get(position_of_team_1).getTeam_logo());
    }

    //function for change league player 2 left
    @FXML
    public void change_team_1_right(ActionEvent event)
    {
        int num_of_teams = leagues.get(position_of_league_1).getTeams().size();
        position_of_team_1 = (position_of_team_1 + 1) % (num_of_teams);
        lb_team1.setText(leagues.get(position_of_league_1).getTeams().get(position_of_team_1).getTeamName());
        img_team1.setImage(leagues.get(position_of_league_1).getTeams().get(position_of_team_1).getTeam_logo());
    }

    @FXML
    public void change_team_2_left(ActionEvent event)
    {
        if ((position_of_team_2 - 1) < 0)
            position_of_team_2 = leagues.get(position_of_league_2).getTeams().size() - 1;
        else
            position_of_team_2 = position_of_team_2 - 1;

        lb_team2.setText(leagues.get(position_of_league_2).getTeams().get(position_of_team_2).getTeamName());
        img_team2.setImage(leagues.get(position_of_league_2).getTeams().get(position_of_team_2).getTeam_logo());
    }


    //function for change league player 2 left
    @FXML
    public void change_team_2_right(ActionEvent event)
    {
        int num_of_teams = leagues.get(position_of_league_2).getTeams().size();
        position_of_team_2 = (position_of_team_2 + 1) % (num_of_teams);
        lb_team2.setText(leagues.get(position_of_league_2).getTeams().get(position_of_team_2).getTeamName());
        img_team2.setImage(leagues.get(position_of_league_2).getTeams().get(position_of_team_2).getTeam_logo());
    }

    @FXML
    public void go_to_main_screen(ActionEvent event) throws IOException
    {
        // switches to ChooseTeam.fxml
        Stage stage;
        Parent root;

        stage = (Stage) bt_back.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void setChosenTeams()
    {
        // todo
        @Deprecated
//        Main.teamA = leagues.get(position_of_league_1).getTeams().get(position_of_team_1);
//        Main.teamB = leagues.get(position_of_league_2).getTeams().get(position_of_team_2);

        DBController query = new DBController();
        String query1 = "select * from players  p join players_stats ps on p.player_id = ps.player_id ";
        query1 += "join teams t on t.team_id = p.team_id ";
        query1 += "join league l on l.league_id = t.league_id where t.name ='" + lb_team1.getText() + "'";

        String query2 = "select * from players  p join players_stats ps on p.player_id = ps.player_id ";
        query2 += "join teams t on t.team_id = p.team_id ";
        query2 += "join league l on l.league_id = t.league_id where t.name ='" + lb_team2.getText() + "'";

        ResultSet players_team_1 = query.sendQuery(query1);
        ResultSet players_team_2 = query.sendQuery(query2);

        players_team_A = new Vector<Player>();
        players_team_B = new Vector<Player>();

        int i = 0;
        // creating vectors of players
        try {
            while (players_team_1.next()) {
                players_team_A.add(i++, new Player(players_team_1));
            }

            i = 0;
            while (players_team_2.next()) {
                players_team_B.add(i++, new Player(players_team_2));
            }
        } catch (SQLException e) {
            System.out.println("Sql exp simulation controller");
        }

        query1 = "select * from players p join players_stats ps on p.player_id = ps.player_id ";
        query1 += "join teams t on t.team_id = p.team_id ";
        query1 += "join league l on l.league_id = t.league_id where t.name ='" + lb_team1.getText() + "'";

        query2 = "select * from players p join players_stats ps on p.player_id = ps.player_id ";
        query2 += "join teams t on t.team_id = p.team_id ";
        query2 += "join league l on l.league_id = t.league_id where t.name ='" + lb_team2.getText() + "'";

        players_team_1 = query.sendQuery(query1);
        players_team_2 = query.sendQuery(query2);

        // Creating global team objects for teams to be accessed from different parts of program
        try {
            while (players_team_1.next()) {
                Main.teamA = new Team(lb_team1.getText(),
                        players_team_1.getInt("t.team_id"),
                        players_team_1.getInt("l.league_id"),
                        players_team_1.getString("l.name"),
                        players_team_A);
                Main.teamA.setTeam_logo(new Image(Main.URLToResources + Main.teamA.getTeamName() + ".png"));

            }
            while (players_team_2.next()) {
                Main.teamB = new Team(lb_team2.getText(),
                        players_team_2.getInt("t.team_id"),
                        players_team_2.getInt("l.league_id"),
                        players_team_2.getString("l.name"),
                        players_team_B);
                Main.teamB.setTeam_logo(new Image(Main.URLToResources + Main.teamB.getTeamName() + ".png"));
            }

        } catch (SQLException e) {
            System.out.println("Glogalni objekat team FAIL");
        }

       query.cleanUp();
    }

    @FXML
    public void go_to_simulation(ActionEvent event) throws IOException
    {
        setChosenTeams(); // NOTE sets static variables from Main class, maybe refactor this later? TODO

        // switches to ChooseTeam.fxml
        Stage stage;
        Parent root;

        //get reference to the button's stage
        stage = (Stage) bt_back.getScene().getWindow();

        root = FXMLLoader.load(getClass().getResource("../view/Simulation.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadLeaguesAndTeams();
        img_league1.setImage(leagues.get(0).getLeague_logo());
        img_league2.setImage(leagues.get(1).getLeague_logo());
        img_team1.setImage(leagues.get(0).getTeams().get(0).getTeam_logo());
        img_team2.setImage(leagues.get(1).getTeams().get(0).getTeam_logo());
        lb_league1.setText(leagues.get(0).getLeague_name());
        lb_league2.setText(leagues.get(1).getLeague_name());
        lb_team1.setText(leagues.get(0).getTeams().get(0).getTeamName());
        lb_team2.setText(leagues.get(1).getTeams().get(0).getTeamName());
    }
}
