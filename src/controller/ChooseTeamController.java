package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.League;
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

    private void loadLeaguesAndTeams ()
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
            System.out.println("failed creating league image at resources!");
        } catch (IOException e) {
            System.out.println("Failed writing league logo at resources!");
        }


        // Reading teams and mapping them to objects
        ResultSet result = dbController.sendQuery("select * from teams t join league l on t.league_id = l.league_id");

        try {
            while (result.next()) {
//                    public Team(String teamName, int team_id, Image team_logo, int league_id, String league_name)
                Blob blob1 = result.getBlob("t.logo");

                int team_logo_length = (int) blob1.length();
                byte[] team_logo_bytes = blob1.getBytes(1, team_logo_length);

                //release the blob and free up memory. (since JDBC 4.0)
                blob1.free();
                FileOutputStream team_logo = new FileOutputStream(Main.PathToResources + result.getString("t.name") + ".png");
                team_logo.write(team_logo_bytes);
                team_logo.close();
                Image team_image = new Image(Main.URLToResources + result.getString("t.name") + ".png");
                System.out.println(result.getString("t.name"));
                int find_me_league = result.getInt("l.league_id") - 1;
                leagues.get(find_me_league).getTeams().add(new Team(
                        result.getString("t.name"),
                        result.getInt("t.team_id"),
                        team_image,
                        find_me_league+1,
                        result.getString("l.name")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Pukao u ChooseTeamController kod iteracije kroz rezultate za lige");
        } catch (FileNotFoundException e) {
            System.out.println("Failed creating output mapped image...");
        } catch (IOException e) {
            System.out.println("Failed writing bytes into memory");
        }

        dbController.cleanUp();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        loadLeaguesAndTeams();
//        imageView.setImage(new Image(Main.URLToResources + "chelsea_logo.png"));
        img_league1.setImage(leagues.get(0).getLeague_logo());
        img_league2.setImage(leagues.get(1).getLeague_logo());
        img_team1.setImage(leagues.get(0).getTeams().get(0).getTeam_logo());
        img_team2.setImage(leagues.get(1).getTeams().get(0).getTeam_logo());


    }
}
