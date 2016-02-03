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

    public void sortPlayersByPosition()
    {
        HashMap<String, Integer> valuators = new HashMap<>();
        valuators.put("gk", 0);
        valuators.put("lb", 1);
        valuators.put("cb", 2);
        valuators.put("rb", 3);
        valuators.put("dm", 4);
        valuators.put("cm", 5);
        valuators.put("am", 6);
        valuators.put("lw", 7);
        valuators.put("rw", 8);
        valuators.put("st", 9);
        Collections.sort(players, (o1, o2) ->
            valuators.get(o1.getPosition()) - valuators.get(o2.getPosition())
        );
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

    public Player getShooter()
    {
        Vector<Player> homies = new Vector<Player>(players);

        // Sorting players by their best shooting stats
        Collections.sort(homies, (o1, o2) ->
                (o2.getStats().get("attacking_prowess") + o2.getStats().get("finishing") + o2.getStats().get("kicking_power"))
                - (o1.getStats().get("attacking_prowess") + o1.getStats().get("finishing") + o1.getStats().get("kicking_power"))
        );

        RandomUtil rand = new RandomUtil();
        float val = rand.runif();
        if (val <= 0.60f) {
            // 60% chances that best two shooters will take a shot
            val = rand.runif();
            if (val <= 0.55f)
                return homies.get(0);
            else
                return homies.get(1);
        } else {
            val = rand.runif();
            if (val <= 0.7f) {
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

    public Player getBestPasser()
    {
        Vector<Player> homies = new Vector<Player>(players);
        Collections.sort(homies, (o1, o2) ->
                (o2.getStats().get("low_pass") + o2.getStats().get("lofted_pass"))
                        - (o1.getStats().get("low_pass") + o1.getStats().get("lofted_pass"))
        );
        return homies.get(0);
    }

    public Player getPasser()
    {
        // In football, midfielders are the ones that have the most of the ball,
        //  after them, defenders, and then attackers.
        // In this simulation, we assume that around 40% of the passing is done by
        // midfielders (dm, cm, am), 30% by defenders (cb, rb, lb), 20% by wingers,
        // and 10% by strikers.
        float probMidfield = 0.4f;
        float probDefense = 0.3f;
        float probWings = 0.2f;
        float probStrikers = 0.1f;          // not used, just for illustration

        RandomUtil rand = new RandomUtil();
        float dragon = rand.runif();
        Vector<Player> homies;
        if (dragon <= probMidfield) {
            // we choose among midfielders
            homies = getMidfilders();
        } else if (dragon <= probMidfield + probDefense) {
            // we choose defense
            homies = getDefenders();
        } else if (dragon <= probMidfield + probDefense + probWings) {
            // we choose wings
            homies = getWingers();
        } else {
            // we choose strikers
            homies = getStrikers();
        }

        // Now we pick a player to play a pass from our chose players
        // 60% that it's the best passer among them, 40% that is someone else

        // sorting by passing
        Collections.sort(homies, (o1, o2) ->
                (o2.getStats().get("low_pass") + o2.getStats().get("lofted_pass"))
                        - (o1.getStats().get("low_pass") + o1.getStats().get("lofted_pass"))
        );
        dragon = rand.runif();
        if (dragon <= 0.6f) {
            return homies.get(0);
        } else {
            if (homies.size() == 1) return homies.get(0);
            else return homies.get(rand.getFromInterval(1, homies.size()-1));
        }
    }

    // --------------------------------------------------------------------------------------------------------
    // Returns team players by position
    // --------------------------------------------------------------------------------------------------------
    public Vector<Player> getWingers()
    {
       Vector<Player> homies = new Vector<>();
        for (Player homie: players)
            if (homie.getPosition().equals("lw") || homie.getPosition().equals("rw"))
                homies.add(homie);
        return homies;
    }

    public Vector<Player> getDefenders()
    {
        Vector<Player> homies = new Vector<>();
        for (Player homie: players)
            if (homie.getPosition().equals("cb") || homie.getPosition().equals("rb") || homie.getPosition().equals("lb"))
                homies.add(homie);
        return homies;
    }

    public Vector<Player> getMidfilders()
    {
        Vector<Player> homies = new Vector<>();
        for (Player homie: players)
            if (homie.getPosition().equals("dm") || homie.getPosition().equals("cm") || homie.getPosition().equals("am"))
                homies.add(homie);
        return homies;
    }

    public Vector<Player> getStrikers()
    {
        Vector<Player> homies = new Vector<>();
        for (Player homie: players)
            if (homie.getPosition().equals("st"))
                homies.add(homie);
        return homies;
    }
    // --------------------------------------------------------------------------------------------------------

    public Player getInterceptor()
    {
        // Returns a player likely to try to intercept a pass
        // Players with most interceptions are mostly midfielders and defenders
        //  As such, we shall distribute: 40% midfielders, 35% defenders, 15% wingers, 5% strikers
        float probMidfield = 0.4f;
        float probDefense = 0.35f;
        float probWings = 0.15f;
        float probStrikers = 0.05f;          // not used, just for illustration

        RandomUtil rand = new RandomUtil();
        float dragon = rand.runif();
        Vector<Player> homies;
        if (dragon <= probMidfield) {
            // we choose among midfielders
            homies = getMidfilders();
        } else if (dragon <= probMidfield + probDefense) {
            // we choose defense
            homies = getDefenders();
        } else if (dragon <= probMidfield + probDefense + probWings) {
            // we choose wings
            homies = getWingers();
        } else {
            // we choose strikers
            homies = getStrikers();
        }

        // Now we pick a player to play a pass from our chose players
        // 60% that it's the best interceptor among them, 40% that is someone else

        // sorting by interception skill
        Collections.sort(homies, (o1, o2) ->
                (o2.getStats().get("defensive_prowess") + o2.getStats().get("ball_winning"))
                        - (o1.getStats().get("defensive_prowess") + o1.getStats().get("ball_winning"))
        );
        dragon = rand.runif();
        if (dragon <= 0.6f) {
            return homies.get(0);
        } else {
            if (homies.size() == 1) return homies.get(0);
            else return homies.get(rand.getFromInterval(1, homies.size()-1));
        }
    }

}
