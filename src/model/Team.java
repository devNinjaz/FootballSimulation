package model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import java.util.*;

/**
 * Created by devninja on 8.1.16..
 */
public class Team
{
    private String teamName, league_name;
    private int team_id, league_id;
    private Image team_logo;
    private Vector<Player> players;
    // todo STATS
    // private double shots_pg, dribbling_p........from CSV files

    public Team()
    {

    }

    public Team(String teamName, int team_id, int league_id, String league_name , Vector<Player> players)
    {
        this.players = players;
        this.teamName = teamName;
        this.team_id = team_id;
        this.league_id = league_id;
        this.league_name = league_name;
    }

    public void setTeam_logo(Image team_logo)
    {
        this.team_logo = team_logo;
    }

    public Team(String teamName, int team_id, Image team_logo, int league_id, String league_name )
    {
        this.teamName = teamName;
        this.team_id = team_id;
        this.team_logo = team_logo;
        this.league_id = league_id;
        this.league_name = league_name;
    }

    public String getLeague_name()
    {
        return league_name;
    }

    public int getTeam_id()
    {
        return team_id;
    }

    public int getLeague_id()
    {
        return league_id;
    }

    public Image getTeam_logo()
    {
        return team_logo;
    }

    public void setPlayers(Vector players)
    {
        this.players = players;
    }

    public Vector<Player> getPlayers() {

        return players;
    }

    // Gets somebody most likely to dribble from a team
    public Player getDribbler()
    {
        Vector<Player> homies = new Vector<Player>(players);
        Collections.sort(homies, (o1, o2) -> o2.getStats().get("dribbling") - o1.getStats().get("dribbling"));

        RandomUtil rand = new RandomUtil();
        float val = rand.runif();
        if (val <= 0.7f) {
            val = rand.runif();
            if (val <= 0.5f)
                return homies.get(0);
            else
                return homies.get(1);
        } else {
            val = rand.runif();
            if (val <= 0.5f) {
                // 3 4
                return homies.get(rand.getFromInterval(2, 3));
            } else {
                return homies.get(rand.getFromInterval(4, 10));
            }
        }
    }

    // Returns somebody most likely to make a tackle
    public Player getTackler()
    {
        Vector<Player> homies = new Vector<Player>(players);
        Collections.sort(homies, (o1, o2) -> o2.getStats().get("ball_winning") - o1.getStats().get("ball_winning"));

        RandomUtil rand = new RandomUtil();
        float val = rand.runif();
        if (val <= 0.73f) {
            val = rand.runif();
            if (val <= 0.55f)
                return homies.get(0);
            else
                return homies.get(1);
        } else {
            val = rand.runif();
            if (val <= 0.5f) {
                // 3 4
                return homies.get(rand.getFromInterval(2, 3));
            } else {
                return homies.get(rand.getFromInterval(4, 10));
            }
        }
    }

    public Player getMidfielder()
    {
        Vector<Player> homies = new Vector<>();
        for (Player homie: players)
            if (homie.getPosition().equals("am") || homie.getPosition().equals("cm") || homie.getPosition().equals("dm"))
                homies.add(homie);

        int randomPlayer = (new RandomUtil()).getFromInterval(0, homies.size()-1);
        return homies.get(randomPlayer);
    }

    // Returns one winger from a team
    public Player getWinger()
    {
        Vector<Player> homies = new Vector<>();
        for (Player homie: players)
            if (homie.getPosition().equals("lw") || homie.getPosition().equals("rw"))
                homies.add(homie);

        int randomPlayer = (new RandomUtil()).getFromInterval(0, homies.size()-1);
        return homies.get(randomPlayer);
    }

    // Returns one defender from a team
    public Player getDefender()
    {
        Vector<Player> homies = new Vector<>();
        for (Player homie: players)
            if (homie.getPosition().equals("cb") || homie.getPosition().equals("lb") || homie.getPosition().equals("rb"))
                homies.add(homie);

        int randomPlayer = (new RandomUtil()).getFromInterval(0, homies.size()-1);
        return homies.get(randomPlayer);
    }

    // Returns one striker from the team
    public Player getStriker()
    {
        Vector<Player> homies = new Vector<>();
        for (Player homie: players)
            if (homie.getPosition().equals("st"))
                homies.add(homie);

        int randomPlayer = (new RandomUtil()).getFromInterval(0, homies.size()-1);
        return homies.get(randomPlayer);
    }

    public Player getGoalkeeper()
    {
        Player goalkeeper = null;
        for (Player homie: players) {
            if (homie.getPosition().equals("gk")) {
                goalkeeper = homie;
                break;
            }
        }
        return goalkeeper;
    }

    // Returns a player most likely to take a set piece
    public Player getSetPieceTaker()
    {
        Vector<Player> homies = new Vector<Player>(players);
        Collections.sort(homies, (o1, o2) -> o2.getStats().get("place_kicking") - o1.getStats().get("place_kicking"));

        RandomUtil rand = new RandomUtil();
        float val = rand.runif();
        // 80% that player best at taking set pieces will take it
        if (val <= 0.8f)
            return homies.get(0);
        // otherwise, second best guy will do it
        else
            return homies.get(1);
    }

    public String getTeamName()
    {
        return teamName;
    }
}
