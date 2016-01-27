package model;

import javafx.scene.image.Image;

import java.util.Vector;

/**
 * Created by devninja on 14.1.16..
 */
public class League
{
    private String league_name;
    private int league_id;
    private Image league_logo;
    private Vector<Team> teams;

    public League(String league_name, int league_id, Image league_logo, Vector<Team> teams)
    {
        this.league_id = league_id;
        this.league_logo = league_logo;
        this.league_name = league_name;
        this.teams = teams;
    }

    public Vector<Team> getTeams()
    {
        return teams;
    }

    public Image getLeague_logo()
    {
        return league_logo;
    }

    public int getLeague_id()
    {
        return league_id;
    }

    public String getLeague_name()
    {
        return league_name;
    }

    public String toString()
    {
        return league_name;
    }
}
