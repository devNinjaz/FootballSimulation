package model;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

/**
 * Created by devninja on 8.1.16..
 */
public class Team
{
    private String teamName, league_name;
    private int team_id, league_id;
    private Image team_logo;
    // todo STATS
    // private double shots_pg, dribbling_p........from CSV files

    public String getTeamName()
    {
        return teamName;
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

    public Team(String teamName, int team_id, Image team_logo, int league_id, String league_name)
    {
        this.teamName = teamName;
        this.team_id = team_id;
        this.team_logo = team_logo;
        this.league_id = league_id;
        this.league_name = league_name;
    }
}
